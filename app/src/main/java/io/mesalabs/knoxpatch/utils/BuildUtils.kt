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

package io.mesalabs.knoxpatch.utils

import android.os.SemBuild
import android.os.SemSystemProperties
import com.samsung.android.knox.EnterpriseDeviceManager

import io.mesalabs.knoxpatch.utils.Constants.EnterpriseKnoxSdkVersion

object BuildUtils {
    private const val ONE_UI_VERSION_SEP_VERSION_GAP = 90000

    fun getSEPVersion(): Int {
        return try {
            SemBuild.VERSION.SEM_PLATFORM_INT
        } catch (e: NoSuchFieldError) {
            -1
        }
    }

    fun getFormattedOneUIVersion(): String {
        val oneUiOwnVersion: Int = SemSystemProperties.getInt(
            "ro.build.version.oneui", 0)

        if (oneUiOwnVersion > 0) {
            val major = oneUiOwnVersion / 10000
            val minor = oneUiOwnVersion % 10000 / 100
            val patch = oneUiOwnVersion % 100

            return if (patch == 0) {
                "$major.$minor"
            } else {
                "$major.$minor.$patch"
            }
        } else {
            return try {
                val sepVersion: Int =
                    SemBuild.VERSION.SEM_PLATFORM_INT - ONE_UI_VERSION_SEP_VERSION_GAP
                (sepVersion / 10000).toString() + "." + sepVersion % 10000 / 100
            } catch (e: NoSuchFieldError) {
                "Unknown"
            }
        }
    }

    fun getKnoxAPIVersion(): Int {
        val sepVersion: Int = getSEPVersion()

        return if (sepVersion != -1) {
            EnterpriseDeviceManager.getAPILevel()
        } else {
            -1
        }
    }

    fun getEnterpriseKnoxSdkVersion(): EnterpriseKnoxSdkVersion {
        return when (getKnoxAPIVersion()) {
            13 -> EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_2_2
            14 -> EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_2_3
            15 -> EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_2_4
            16 -> EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_2_4_1
            17 -> EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_2_5
            18 -> EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_2_5_1
            19 -> EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_2_6
            20 -> EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_2_7
            21 -> EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_2_7_1
            22 -> EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_2_8
            23 -> EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_2_9
            24 -> EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_3_0
            25 -> EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_3_1
            26 -> EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_3_2
            27 -> EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_3_2_1
            28 -> EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_3_3
            29 -> EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_3_4
            30 -> EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_3_4_1
            31 -> EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_3_5
            32 -> EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_3_6
            33 -> EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_3_7
            34 -> EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_3_7_1
            35 -> EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_3_8
            36 -> EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_3_9
            37 -> EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_3_10
            38 -> EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_3_11
            39 -> EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_3_12
            else -> EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_2_1
        }
    }

}
