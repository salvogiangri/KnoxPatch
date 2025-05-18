/*
 * KnoxPatch
 * Copyright (C) 2023 Salvo Giangreco
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.mesalabs.knoxpatch.ui.activity

import android.view.SemWindowManager.LayoutParams.SEM_EXTENSION_FLAG_RESIZE_FULLSCREEN_WINDOW_ON_SOFT_INPUT

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.FeatureInfo
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.SemSystemProperties
import android.os.SystemClock
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.SemWindowManager
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.EdgeEffect
import android.widget.Toast

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.SeslEdgeEffect
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.EdgeEffectFactory
import androidx.window.layout.WindowMetricsCalculator

import com.highcapable.yukihookapi.YukiHookAPI

import dev.rikka.tools.refine.Refine

import io.mesalabs.knoxpatch.BuildConfig
import io.mesalabs.knoxpatch.R
import io.mesalabs.knoxpatch.databinding.ActivityInfoBinding
import io.mesalabs.knoxpatch.databinding.MainSwitchViewBinding
import io.mesalabs.knoxpatch.ui.list.InfoListItemDecoration
import io.mesalabs.knoxpatch.ui.list.InfoListViewAdapter
import io.mesalabs.knoxpatch.utils.Constants

import kotlin.math.max

class InfoActivity : AppCompatActivity() {
    companion object {
        private const val DOT_SEPARATOR: String = "  •  "
    }

    private lateinit var binding: ActivityInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= 35) {
            ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
                val i = insets.getInsets(WindowInsetsCompat.Type.systemBars()
                        or WindowInsetsCompat.Type.displayCutout())
                v.setPadding(i.left, i.top, i.right, i.bottom)
                WindowInsetsCompat.CONSUMED
            }
        }

        applyLandscapeFullScreen()
        setContentSideMargin(binding.mainContent)

        initToolbar()
        initAppBanner()
        initMainSwitch()
        initListView()

        showCryptoWarningDialog()
    }

    private fun initToolbar() {
        setSupportActionBar(binding.mainToolbar)
        with(supportActionBar!!) {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_info, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_app_info) {
            val intent = Intent(
                "android.settings.APPLICATION_DETAILS_SETTINGS",
                Uri.fromParts("package", BuildConfig.APPLICATION_ID, null))
            intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initAppBanner() {
        binding.mainAppIcon.setImageDrawable(
            packageManager.getApplicationIcon(applicationInfo))
        binding.mainAppVersion.text = getModuleVersion()
        binding.mainAppGithub.tooltipText = getString(R.string.github)
        binding.mainAppGithub.setOnClickListener(GitHubIconListener())
    }

    private fun getModuleVersion(): SpannableStringBuilder {
        val span = SpannableStringBuilder()
        span.append(BuildConfig.VERSION_NAME)

        val featuresList: Array<FeatureInfo> = packageManager.systemAvailableFeatures
        for (f in featuresList) {
            if (f.name != null && f.name == Constants.ENHANCER_SYSTEM_FEATURE) {
                span.append(DOT_SEPARATOR)

                var eVer = "Enhanced"
                eVer += " (v" + f.version / 100 + "." + f.version % 100 + ")"

                span.append(eVer)
                span.setSpan(
                    ForegroundColorSpan(getColor(R.color.sep_theme_functional_green_color)),
                    span.length - eVer.length,
                    span.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                break
            }
        }

        return span
    }

    private fun initMainSwitch() {
        val switchView: MainSwitchViewBinding = binding.mainSwitchView
        val isModuleEnabled: Boolean = YukiHookAPI.Status.isModuleActive

        val mlp: ViewGroup.MarginLayoutParams = switchView.root.layoutParams as ViewGroup.MarginLayoutParams
        mlp.setMargins(getListHorizontalPadding(), 0, getListHorizontalPadding(), 0)

        DrawableCompat.setTintList(
            DrawableCompat.wrap(switchView.root.background).mutate(),
            ColorStateList.valueOf(
                getColor(if (isModuleEnabled) {
                    R.color.sep_theme_main_switch_on_background_color
                } else {
                    R.color.sep_theme_main_switch_off_background_color
                })
            )
        )

        switchView.mainSwitchText.setTextColor(
            getColor(if (isModuleEnabled) {
                R.color.sep_theme_main_switch_on_text_color
            } else {
                R.color.sep_theme_main_switch_off_text_color
            })
        )
        switchView.mainSwitchText.text =
            getString(if (isModuleEnabled) {
                R.string.main_switch_on
            } else {
                R.string.main_switch_off
            })

        switchView.mainSwitchWidget.isChecked = isModuleEnabled
    }

    private fun initListView() {
        with(binding.mainList) {
            if (Build.VERSION.SDK_INT < 31) {
                edgeEffectFactory = object: EdgeEffectFactory() {
                    @SuppressLint("RestrictedApi")
                    override fun createEdgeEffect(view: RecyclerView,
                                                  direction: Int): EdgeEffect {
                        val edgeEffect = SeslEdgeEffect(view.context)
                        edgeEffect.setHostView(view, false)
                        return edgeEffect
                    }
                }
            }

            layoutManager = LinearLayoutManager(this@InfoActivity)
            adapter = InfoListViewAdapter(this@InfoActivity)

            setPadding(getListHorizontalPadding(), 0, getListHorizontalPadding(), 0)
            scrollBarStyle = View.SCROLLBARS_OUTSIDE_OVERLAY
            seslSetFillHorizontalPaddingEnabled(true)

            addItemDecoration(InfoListItemDecoration(this@InfoActivity))

            seslSetFillBottomEnabled(true)
            seslSetLastRoundedCorner(true)
        }
    }

    private fun showCryptoWarningDialog() {
        if (Build.VERSION.SDK_INT < 31) {
            val cryptoState: String = SemSystemProperties.get("ro.crypto.state", "")
            val cryptoType: String = SemSystemProperties.get("ro.crypto.type", "")

            if (cryptoState == "encrypted" && cryptoType == "file") {
                val dialog = AlertDialog.Builder(this)
                    .setTitle(R.string.crypto_warning_dialog_title)
                    .setMessage(R.string.crypto_warning_dialog_message)
                    .setPositiveButton(R.string.crypto_warning_dialog_btn, null)
                    .create()
                dialog.show()
            }
        }
    }

    @SuppressLint("WrongConstant")
    @Suppress("DEPRECATION")
    private fun applyLandscapeFullScreen() {
        val config: Configuration = resources.configuration

        if (!isInMultiWindowMode
            && config.smallestScreenWidthDp < 420
            && config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (Build.VERSION.SDK_INT >= 30) {
                val controller: WindowInsetsController = window.insetsController ?: return
                controller.hide(WindowInsets.Type.statusBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            }
        } else {
            if (Build.VERSION.SDK_INT >= 30) {
                val controller: WindowInsetsController = window.insetsController ?: return
                controller.show(WindowInsets.Type.statusBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_BARS_BY_SWIPE
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            }
        }

        val attributes: WindowManager.LayoutParams = window.attributes
        Refine.unsafeCast<SemWindowManager.LayoutParams>(attributes)
            .semAddExtensionFlags(SEM_EXTENSION_FLAG_RESIZE_FULLSCREEN_WINDOW_ON_SOFT_INPUT)
        window.attributes = attributes
    }

    private fun setContentSideMargin(layout: ViewGroup) {
        if (!isDestroyed && !isFinishing) {
            findViewById<View>(android.R.id.content).post {
                val lp: ViewGroup.MarginLayoutParams =
                    layout.layoutParams as ViewGroup.MarginLayoutParams
                val m: Int = getSideMargin()
                lp.setMargins(m, 0, m, 0)
                layout.layoutParams = lp
            }
        }
    }

    private fun getSideMargin(): Int {
        val bounds = Rect(WindowMetricsCalculator.getOrCreate()
            .computeCurrentWindowMetrics(this).bounds)
        val density = resources.displayMetrics.density
        val screenWidthDp = bounds.width() / density
        val screenHeightDp = bounds.height() / density

        val ratio = {
            if (screenWidthDp in 589.0f..959.0f) {
                screenWidthDp * if (screenHeightDp < 411.0f) 1.0f else 0.86f
            } else if (screenWidthDp >= 960.0f) {
                840.0f
            } else {
                screenWidthDp
            }
        }

        return max(0, ((screenWidthDp - ratio()) / 2 * density).toInt())
    }

    private fun getListHorizontalPadding(): Int {
        val bounds = Rect(WindowMetricsCalculator.getOrCreate()
            .computeCurrentWindowMetrics(this).bounds)
        val density = resources.displayMetrics.density
        val screenWidthDp = bounds.width() / density
        val screenHeightDp = bounds.height() / density

        val padding = {
            if (
                (screenWidthDp in 589.0f..959.0f && screenHeightDp >= 411.0f)
                || screenWidthDp >= 960.0f
            ) {
                0.0f
            } else {
                10.0f
            }
        }

        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            padding(),
            resources.displayMetrics
        ).toInt()
    }


    private inner class GitHubIconListener : View.OnClickListener {
        private var lastClickTime: Long = 0L

        override fun onClick(v: View) {
            val uptimeMillis: Long = SystemClock.uptimeMillis()

            if (uptimeMillis - lastClickTime > 600L) {
                val url = "https://github.com/salvogiangri/KnoxPatch"
                val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse(url))
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                } else {
                    Toast.makeText(this@InfoActivity,
                        getString(R.string.activity_not_found), Toast.LENGTH_SHORT).show()
                }
            }

            lastClickTime = uptimeMillis
        }
    }
}
