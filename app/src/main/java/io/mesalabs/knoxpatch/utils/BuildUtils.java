/*
 * KnoxPatch
 * Copyright (C) 2022 BlackMesa123
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

package io.mesalabs.knoxpatch.utils;

import android.os.SemBuild;
import com.samsung.android.knox.EnterpriseDeviceManager;

import io.mesalabs.knoxpatch.utils.Constants.EnterpriseKnoxSdkVersion;

public class BuildUtils {

    public static int getSEPVersion() {
        try {
            final int sepVersion = SemBuild.VERSION.SEM_PLATFORM_INT;
            return sepVersion;
        } catch (NoSuchFieldError e) {
            return -1;
        }
    }

    public static int getKnoxAPIVersion() {
        final int sepVersion = getSEPVersion();

        if (sepVersion == -1) {
            return -1;
        }

        return EnterpriseDeviceManager.getAPILevel();
    }

    public static EnterpriseKnoxSdkVersion getEnterpriseKnoxSdkVersion() {
        switch (BuildUtils.getKnoxAPIVersion()) {
            case 13:
                return EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_2_2;
            case 14:
                return EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_2_3;
            case 15:
                return EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_2_4;
            case 16:
                return EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_2_4_1;
            case 17:
                return EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_2_5;
            case 18:
                return EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_2_5_1;
            case 19:
                return EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_2_6;
            case 20:
                return EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_2_7;
            case 21:
                return EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_2_7_1;
            case 22:
                return EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_2_8;
            case 23:
                return EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_2_9;
            case 24:
                return EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_3_0;
            case 25:
                return EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_3_1;
            case 26:
                return EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_3_2;
            case 27:
                return EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_3_2_1;
            case 28:
                return EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_3_3;
            case 29:
                return EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_3_4;
            case 30:
                return EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_3_4_1;
            case 31:
                return EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_3_5;
            case 32:
                return EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_3_6;
            case 33:
                return EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_3_7;
            case 34:
                return EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_3_7_1;
            case 35:
                return EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_3_8;
            case 36:
                return EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_3_9;
            default:
                return EnterpriseKnoxSdkVersion.KNOX_ENTERPRISE_SDK_VERSION_2_1;
        }
    }

}
