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
import android.os.SemBuild
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
    private const val ONE_UI_VERSION_SEP_VERSION_GAP = 90000

    @JvmStatic
    fun getAndroidVersion(): String {
        return Build.VERSION.RELEASE
    }

    @JvmStatic
    fun getBuildNumber(): String {
        var buildDisplay = ""

        val buildId: String = SemSystemProperties.get("ro.build.id", "")
        if (buildId.isNotEmpty()) {
            buildDisplay += buildId + "."
        }
        buildDisplay += Build.VERSION.INCREMENTAL

        return buildDisplay
    }

    @JvmStatic
    fun isKnoxAvailable(): Boolean {
        val currentVersion: KnoxContainerVersion = SemPersonaManager.getKnoxContainerVersion()
        return currentVersion >= KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_2_0
    }

    @Suppress("DEPRECATION")
    @JvmStatic
    fun getKnoxComponentsVersion(context: Context): String {
        var knoxVersion = ""

        // Knox version
        knoxVersion += context.getString(R.string.knox_version_knox) + " "

        val knoxVersionName: String = BuildUtils.getEnterpriseKnoxSdkVersion().internalVersion
        val lastInt = knoxVersionName[knoxVersionName.length - 1].toString().toInt()

        knoxVersion += if (lastInt > 0) {
            knoxVersionName
        } else {
            val index = knoxVersionName.lastIndexOf('.')
            knoxVersionName.substring(0, index)
        }

        // Knox API level
        knoxVersion += "\n" + context.getString(R.string.knox_version_knox_api) + " "
        knoxVersion += BuildUtils.getKnoxAPIVersion()

        // TIMA version
        val timaProp: String = SemSystemProperties.get("ro.config.tima", "")
        val isTimaSupported: Boolean = timaProp.isNotEmpty() && timaProp == "1"

        if (isTimaSupported) {
            knoxVersion += "\n" + context.getString(R.string.knox_version_knox_tima) + " "

            if (SemSystemProperties.get("ro.config.timaversion", "") == "3.0") {
                if (BuildUtils.getSEPVersion() >= Constants.ONEUI_1_1) {
                    knoxVersion += "4.1.0"
                } else {
                    if (SemPersonaManager.getKnoxContainerVersion()
                        >= KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_7_0) {
                        knoxVersion += "3.3.0"
                    } else {
                        knoxVersion += "3.2.0"
                    }
                }
            } else {
                knoxVersion += SemSystemProperties.get(
                    "ro.config.timaversion", "No Policy Version")
            }
        }

        // Knox ML version
        try {
            val knoxMLApp: PackageInfo? = if (Build.VERSION.SDK_INT >= 33) {
                context.packageManager.getPackageInfo(
                    "com.samsung.android.app.kfa",
                    PackageManager.PackageInfoFlags.of(PackageManager.GET_META_DATA.toLong()))
            } else {
                context.packageManager.getPackageInfo(
                    "com.samsung.android.app.kfa", PackageManager.GET_META_DATA)
            }

            if (knoxMLApp != null) {
                knoxVersion += "\n" + context.getString(R.string.knox_version_knox_ml) + " "

                val knoxMLVersion: String = knoxMLApp.versionName
                knoxVersion += knoxMLVersion.substring(0, knoxMLVersion.length - 3)
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

        return knoxVersion
    }

    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun getKnoxFeatures(): String {
        val features: ArrayList<String> = ArrayList()

        if (BuildUtils.getSEPVersion() >= Constants.ONEUI_5_0) {
            try {
                val cls: Class<*>? = Class.forName(
                    "com.samsung.android.knox.dar.DarRune")
                if (cls != null) {
                    val fields = HiddenApiBypass.getStaticFields(cls) as List<Field>

                    // KNOX_SUPPORT_*
                    for (field in fields) {
                        if (field.name.contains("KNOX_SUPPORT_")) {
                            features.add(field.name + " = " + field.get(null))
                        }
                    }
                }
            } catch (e: ClassNotFoundException) {
                throw RuntimeException(e)
            } catch (e: IllegalAccessException) {
                throw RuntimeException(e)
            }
        }


        try {
            val cls: Class<*>? = Class.forName(
                "com.samsung.android.rune.CoreRune")
            if (cls != null) {
                val fields = HiddenApiBypass.getStaticFields(cls) as List<Field>

                // KNOX_SUPPORT_*
                for (field in fields) {
                    if (field.name.contains("KNOX_SUPPORT_")) {
                        features.add(field.name + " = " + field.get(null))
                    }
                }

                // SUPPORT_KNOX
                for (field in fields) {
                    if (field.name.contains("SUPPORT_KNOX")) {
                        features.add(0, field.name + " = " + field.get(null))
                    }
                }
            }
        } catch (e: ClassNotFoundException) {
            throw RuntimeException(e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException(e)
        }

        return features.joinToString(separator = "\n")
    }

    @JvmStatic
    fun isSepLiteAvailable(context: Context): Boolean {
        val sepCategoryFeature: String = SemFloatingFeature.getInstance()
            .getString("SEC_FLOATING_FEATURE_COMMON_CONFIG_SEP_CATEGORY")
        if (sepCategoryFeature == "sep_lite" || sepCategoryFeature == "sep_lite_new") {
            return true
        } else {
            return context.packageManager
                .hasSystemFeature("com.samsung.feature.samsung_experience_mobile_lite")
        }
    }

    @JvmStatic
    fun getOneUIVersion(): String {
        val oneUiOwnVersion: Int = SemSystemProperties.getInt(
            "ro.build.version.oneui", 0)

        if (oneUiOwnVersion > 0) {
            val major = oneUiOwnVersion / 10000
            val minor = oneUiOwnVersion % 10000 / 100
            val patch = oneUiOwnVersion % 100

            if (patch == 0) {
                return "$major.$minor"
            } else {
                return "$major.$minor.$patch"
            }
        } else {
            try {
                val sepVersion: Int =
                    SemBuild.VERSION.SEM_PLATFORM_INT - ONE_UI_VERSION_SEP_VERSION_GAP
                return (sepVersion / 10000).toString() + "." + sepVersion % 10000 / 100
            } catch (e: NoSuchFieldError) {
                return "Unknown"
            }
        }
    }

}
