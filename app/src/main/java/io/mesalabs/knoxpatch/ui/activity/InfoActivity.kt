/*
 * KnoxPatch
 * Copyright (C) 2023 BlackMesa123
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
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.FeatureInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.SemSystemProperties
import android.os.SystemClock
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import android.view.SemWindowManager
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.EdgeEffect
import android.widget.Toast

import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.SeslEdgeEffect
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.EdgeEffectFactory

import dev.rikka.tools.refine.Refine

import io.mesalabs.knoxpatch.BuildConfig
import io.mesalabs.knoxpatch.R
import io.mesalabs.knoxpatch.databinding.ActivityInfoBinding
import io.mesalabs.knoxpatch.ui.list.InfoListRoundedCorners
import io.mesalabs.knoxpatch.ui.list.InfoListViewAdapter
import io.mesalabs.knoxpatch.utils.Constants

class InfoActivity : AppCompatActivity() {
    companion object {
        private const val DOT_SEPARATOR: String = "  •  "
    }

    private lateinit var binding: ActivityInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        applyLandscapeFullScreen()
        setContentSideMargin(binding.mainContent)

        initToolbar()
        initAppBanner()
        initListView()

        showCryptoWarningDialog()
    }

    private fun initToolbar() {
        setSupportActionBar(binding.mainToolbar)
        val toolBar: ActionBar = supportActionBar!!
        toolBar.setDisplayHomeAsUpEnabled(true)
        toolBar.setDisplayShowTitleEnabled(false)
        binding.mainToolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_info, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_app_info) {
            val intent = Intent(
                "android.settings.APPLICATION_DETAILS_SETTINGS",
                Uri.fromParts("package", BuildConfig.APPLICATION_ID, null))
            intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            return true
        }

        return false
    }

    private fun initAppBanner() {
        binding.mainAppIcon.setImageDrawable(
            packageManager.getApplicationIcon(applicationInfo))
        binding.mainAppVersion.text = getModuleVersion()
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

    private fun initListView() {
        val listView: RecyclerView = binding.mainList
        if (Build.VERSION.SDK_INT < 31) {
            listView.edgeEffectFactory = object : EdgeEffectFactory() {
                @SuppressLint("RestrictedApi")
                override fun createEdgeEffect(
                    view: RecyclerView,
                    direction: Int): EdgeEffect {
                    val edgeEffect = SeslEdgeEffect(view.context)
                    edgeEffect.setHostView(view, false)
                    return edgeEffect
                }
            }
        }

        listView.layoutManager = LinearLayoutManager(this)
        listView.adapter = InfoListViewAdapter(this)
        listView.addItemDecoration(InfoListRoundedCorners(this))
        listView.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        listView.seslSetFillBottomEnabled(true)
        listView.seslSetLastRoundedCorner(true)
    }

    private fun showCryptoWarningDialog() {
        if (Build.VERSION.SDK_INT == 30) {
            val cryptoState: String = SemSystemProperties.get("ro.crypto.state", "")
            val cryptoType: String = SemSystemProperties.get("ro.crypto.type", "")

            if (cryptoState != "unencrypted" && cryptoType.isNotEmpty()) {
                val dialog = AlertDialog.Builder(this)
                    .setTitle(R.string.sep_12_crypto_warning_dialog_title)
                    .setMessage(R.string.sep_12_crypto_warning_dialog_message)
                    .setPositiveButton(R.string.sep_12_crypto_warning_dialog_btn, null)
                    .create()
                dialog.show()
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun applyLandscapeFullScreen() {
        val config: Configuration = resources.configuration
        val attributes: WindowManager.LayoutParams = window.attributes

        if (!isInMultiWindowMode
            && config.smallestScreenWidthDp < 420
            && config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (Build.VERSION.SDK_INT >= 30) {
                window.insetsController!!.hide(WindowInsets.Type.statusBars())
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            }
        } else {
            if (Build.VERSION.SDK_INT >= 30) {
                window.insetsController!!.show(WindowInsets.Type.statusBars())
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            }
        }

        Refine.unsafeCast<SemWindowManager.LayoutParams>(attributes)
            .semAddExtensionFlags(SEM_EXTENSION_FLAG_RESIZE_FULLSCREEN_WINDOW_ON_SOFT_INPUT)
        window.attributes = attributes
    }

    private fun setContentSideMargin(layout: ViewGroup) {
        if (!isDestroyed && !isFinishing) {
            findViewById<View>(android.R.id.content).post {
                var m: Int = getSideMargin()
                if (m < 0)
                    m = 0

                val lp: ViewGroup.MarginLayoutParams =
                    layout.layoutParams as ViewGroup.MarginLayoutParams
                lp.setMargins(m, 0, m, 0)
                layout.layoutParams = lp
            }
        }
    }

    private fun getSideMargin(): Int {
        val config: Configuration = resources.configuration

        val ratio = {
            val screenWidthDp: Int = config.screenWidthDp
            val screenHeightDp: Int = config.screenHeightDp
            if (screenWidthDp in 589..959) {
                (screenWidthDp * if (screenHeightDp < 411) 1.0f else 0.86f).toInt()
            } else if (screenWidthDp >= 960) {
                840
            } else {
                screenWidthDp
            }
        }

        val density = resources.displayMetrics.density
        return ((config.screenWidthDp - ratio()) / 2 * density).toInt()
    }


    private inner class GitHubIconListener : View.OnClickListener {
        private var lastClickTime: Long = 0L

        override fun onClick(v: View) {
            val uptimeMillis: Long = SystemClock.uptimeMillis()

            if (uptimeMillis - lastClickTime > 600L) {
                val url = "https://github.com/BlackMesa123/KnoxPatch"

                try {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setData(Uri.parse(url))
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this@InfoActivity,
                        "No suitable activity found", Toast.LENGTH_SHORT).show()
                }
            }

            lastClickTime = uptimeMillis
        }
    }
}
