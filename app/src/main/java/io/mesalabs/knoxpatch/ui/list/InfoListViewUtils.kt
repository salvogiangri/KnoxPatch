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

package io.mesalabs.knoxpatch.ui.list

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.SemSystemProperties
import com.samsung.android.feature.SemFloatingFeature
import com.samsung.android.knox.SemPersonaManager
import com.samsung.android.knox.SemPersonaManager.KnoxContainerVersion
import com.samsung.android.knox.ddar.DualDARPolicy
import com.samsung.android.knox.hdm.HdmManager

import java.lang.reflect.Field

import org.lsposed.hiddenapibypass.HiddenApiBypass

import io.mesalabs.knoxpatch.R
import io.mesalabs.knoxpatch.utils.BuildUtils
import io.mesalabs.knoxpatch.utils.Constants

object InfoListViewUtils {

    fun getAndroidVersion(): String = Build.VERSION.RELEASE

    fun getBuildNumber(): String {
        var buildDisplay = ""

        val buildId: String = SemSystemProperties.get("ro.build.id", "")
        if (buildId.isNotBlank()) {
            buildDisplay += buildId + "."
        }
        buildDisplay += Build.VERSION.INCREMENTAL

        return buildDisplay.ifBlank {
            Build.DISPLAY
        }
    }

    fun isKnoxAvailable(): Boolean {
        return if (Build.VERSION.SDK_INT < 35) {
            val currentVersion: KnoxContainerVersion = SemPersonaManager.getKnoxContainerVersion()
            currentVersion >= KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_2_0
        } else {
            true
        }
    }

    @Suppress("DEPRECATION")
    fun getKnoxComponentsVersion(context: Context): String {
        var knoxVersion = ""

        // Knox version
        knoxVersion += context.getString(R.string.knox_version_knox) + " "
        knoxVersion += BuildUtils.getEnterpriseKnoxSdkVersion().internalVersion

        // Knox API level
        knoxVersion += "\n" + context.getString(R.string.knox_version_knox_api) + " "
        knoxVersion += BuildUtils.getKnoxAPIVersion()

        // Knox ML version
        try {
            val knoxMLApp: PackageInfo = context.packageManager.getPackageInfo(
                "com.samsung.android.app.kfa", PackageManager.GET_META_DATA)
            knoxMLApp.versionName?.let {
                knoxVersion += "\n" + context.getString(R.string.knox_version_knox_ml) + " "
                knoxVersion += it.substring(0, it.length - 3)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            // no-op
        }

        // DualDAR version
        val dualDARVersion: String? = DualDARPolicy.getDualDARVersion()
        if (dualDARVersion != null) {
            knoxVersion += "\n" + context.getString(R.string.knox_version_knox_dualdar) + " "
            knoxVersion += dualDARVersion
        }

        // HDM version
        if (Build.VERSION.SDK_INT >= 30) {
            val hdmVersion: String? = HdmManager.getHdmVersion();
            if (hdmVersion != null) {
                knoxVersion += "\n" + context.getString(R.string.knox_version_knox_hdm) + " "
                knoxVersion += hdmVersion
            }
        }

        // Knox POS SDK version
        if (Build.VERSION.SDK_INT >= 35) {
            try {
                val knoxMposApp: PackageInfo = context.packageManager.getPackageInfo(
                    "com.samsung.android.knox.mpos", PackageManager.GET_META_DATA)
                val knoxMposVer: String? = knoxMposApp.applicationInfo?.metaData?.getString("KNOX_MPOS_VERSION")
                if (!knoxMposVer.isNullOrBlank()) {
                    knoxVersion += "\n" + knoxMposVer
                }
            } catch (e: PackageManager.NameNotFoundException) {
                // no-op
            }
        }

        return knoxVersion
    }

    fun getKnoxFeatures(): String {
        val features: ArrayList<String> = ArrayList()

        @Suppress("UNCHECKED_CAST")
        fun getStaticFields(className: String): List<Field> {
            try {
                val cls: Class<*>? = Class.forName(className)
                if (cls != null) {
                    return HiddenApiBypass.getStaticFields(cls) as List<Field>
                }
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }

            return emptyList()
        }

        if (BuildUtils.getSEPVersion() >= Constants.ONEUI_5_0) {
            val darRune = getStaticFields("com.samsung.android.knox.dar.DarRune")
            for (field in darRune) {
                // KNOX_SUPPORT_*
                if (field.name.contains("KNOX_SUPPORT_")) {
                    features.add(field.name + " = " + field.get(null))
                }
            }
        }

        val coreRune = getStaticFields("com.samsung.android.rune.CoreRune")

        for (field in coreRune) {
            // KNOX_SUPPORT_*
            if (field.name.contains("KNOX_SUPPORT_")) {
                features.add(field.name + " = " + field.get(null))
            }
        }

        features.sort()

        for (field in coreRune) {
            // SUPPORT_KNOX
            if (field.name.contains("SUPPORT_KNOX")) {
                features.add(0, field.name + " = " + field.get(null))
            }
        }

        return features.distinct().joinToString(separator = "\n").ifBlank {
            "Unknown"
        }
    }

    fun isSepLiteAvailable(context: Context): Boolean {
        val sepCategoryFeature: String = SemFloatingFeature.getInstance()
            .getString("SEC_FLOATING_FEATURE_COMMON_CONFIG_SEP_CATEGORY")
        return if (sepCategoryFeature == "sep_lite" || sepCategoryFeature == "sep_lite_new") {
            true
        } else {
            context.packageManager
                .hasSystemFeature("com.samsung.feature.samsung_experience_mobile_lite")
        }
    }

}
