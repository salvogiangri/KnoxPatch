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

public class Constants {

    public static final int ONEUI_1_0 = 100000;
    public static final int ONEUI_1_1 = 100100;
    public static final int ONEUI_1_5 = 100500;
    public static final int ONEUI_2_0 = 110000;
    public static final int ONEUI_2_1 = 110100;
    public static final int ONEUI_2_5 = 110500;
    public static final int ONEUI_3_0 = 120000;
    public static final int ONEUI_3_1 = 120100;
    public static final int ONEUI_3_1_1 = 120500;
    public static final int ONEUI_4_0 = 130000;
    public static final int ONEUI_4_1 = 130100;
    public static final int ONEUI_4_1_1 = 130500;
    public static final int ONEUI_5_0 = 140000;
    public static final int ONEUI_5_1 = 140100;

    public enum EnterpriseKnoxSdkVersion {
        KNOX_ENTERPRISE_SDK_VERSION_NONE,
        KNOX_ENTERPRISE_SDK_VERSION_1_0,
        KNOX_ENTERPRISE_SDK_VERSION_1_0_1,
        KNOX_ENTERPRISE_SDK_VERSION_1_0_2,
        KNOX_ENTERPRISE_SDK_VERSION_1_1_0,
        KNOX_ENTERPRISE_SDK_VERSION_1_2_0,
        KNOX_ENTERPRISE_SDK_VERSION_2_0,
        KNOX_ENTERPRISE_SDK_VERSION_2_1,
        KNOX_ENTERPRISE_SDK_VERSION_2_2,
        KNOX_ENTERPRISE_SDK_VERSION_2_3,
        KNOX_ENTERPRISE_SDK_VERSION_2_4,
        KNOX_ENTERPRISE_SDK_VERSION_2_4_1,
        KNOX_ENTERPRISE_SDK_VERSION_2_5,
        KNOX_ENTERPRISE_SDK_VERSION_2_5_1,
        KNOX_ENTERPRISE_SDK_VERSION_2_6,
        KNOX_ENTERPRISE_SDK_VERSION_2_7,
        KNOX_ENTERPRISE_SDK_VERSION_2_7_1,
        KNOX_ENTERPRISE_SDK_VERSION_2_8,
        KNOX_ENTERPRISE_SDK_VERSION_2_9,
        KNOX_ENTERPRISE_SDK_VERSION_3_0,
        KNOX_ENTERPRISE_SDK_VERSION_3_1,
        KNOX_ENTERPRISE_SDK_VERSION_3_2,
        KNOX_ENTERPRISE_SDK_VERSION_3_2_1,
        KNOX_ENTERPRISE_SDK_VERSION_3_3,
        KNOX_ENTERPRISE_SDK_VERSION_3_4,
        KNOX_ENTERPRISE_SDK_VERSION_3_4_1,
        KNOX_ENTERPRISE_SDK_VERSION_3_5,
        KNOX_ENTERPRISE_SDK_VERSION_3_6,
        KNOX_ENTERPRISE_SDK_VERSION_3_7,
        KNOX_ENTERPRISE_SDK_VERSION_3_7_1,
        KNOX_ENTERPRISE_SDK_VERSION_3_8,
        KNOX_ENTERPRISE_SDK_VERSION_3_9;

        public String getInternalVersion() {
            switch (EnterpriseKnoxSdkVersion.values()[ordinal()]) {
                case KNOX_ENTERPRISE_SDK_VERSION_1_0:
                    return "1.0.0";
                case KNOX_ENTERPRISE_SDK_VERSION_1_0_1:
                    return "1.0.1";
                case KNOX_ENTERPRISE_SDK_VERSION_1_0_2:
                    return "1.0.2";
                case KNOX_ENTERPRISE_SDK_VERSION_1_1_0:
                    return "1.1.0";
                case KNOX_ENTERPRISE_SDK_VERSION_1_2_0:
                    return "1.2.0";
                case KNOX_ENTERPRISE_SDK_VERSION_2_0:
                    return "2.0.0";
                case KNOX_ENTERPRISE_SDK_VERSION_2_1:
                    return "2.1.0";
                case KNOX_ENTERPRISE_SDK_VERSION_2_2:
                    return "2.2.0";
                case KNOX_ENTERPRISE_SDK_VERSION_2_3:
                    return "2.3.0";
                case KNOX_ENTERPRISE_SDK_VERSION_2_4:
                    return "2.4.0";
                case KNOX_ENTERPRISE_SDK_VERSION_2_4_1:
                    return "2.4.1";
                case KNOX_ENTERPRISE_SDK_VERSION_2_5:
                    return "2.5.0";
                case KNOX_ENTERPRISE_SDK_VERSION_2_5_1:
                    return "2.5.1";
                case KNOX_ENTERPRISE_SDK_VERSION_2_6:
                    return "2.6.0";
                case KNOX_ENTERPRISE_SDK_VERSION_2_7:
                    return "2.7.0";
                case KNOX_ENTERPRISE_SDK_VERSION_2_7_1:
                    return "2.7.1";
                case KNOX_ENTERPRISE_SDK_VERSION_2_8:
                    return "2.8.0";
                case KNOX_ENTERPRISE_SDK_VERSION_2_9:
                    return "2.9.0";
                case KNOX_ENTERPRISE_SDK_VERSION_3_0:
                    return "3.0.0";
                case KNOX_ENTERPRISE_SDK_VERSION_3_1:
                    return "3.1.0";
                case KNOX_ENTERPRISE_SDK_VERSION_3_2:
                    return "3.2.0";
                case KNOX_ENTERPRISE_SDK_VERSION_3_2_1:
                    return "3.2.1";
                case KNOX_ENTERPRISE_SDK_VERSION_3_3:
                    return "3.3.0";
                case KNOX_ENTERPRISE_SDK_VERSION_3_4:
                    return "3.4.0";
                case KNOX_ENTERPRISE_SDK_VERSION_3_4_1:
                    return "3.4.1";
                case KNOX_ENTERPRISE_SDK_VERSION_3_5:
                    return "3.5";
                case KNOX_ENTERPRISE_SDK_VERSION_3_6:
                    return "3.6";
                case KNOX_ENTERPRISE_SDK_VERSION_3_7:
                    return "3.7";
                case KNOX_ENTERPRISE_SDK_VERSION_3_7_1:
                    return "3.7.1";
                case KNOX_ENTERPRISE_SDK_VERSION_3_8:
                    return "3.8";
                case KNOX_ENTERPRISE_SDK_VERSION_3_9:
                    return "3.9";
                default:
                    return "N/A";
            }
        }
    }

    public static final String ENHANCER_SYSTEM_FEATURE = "io.mesalabs.knoxpatch_enhancer";

    public static final String AUTHFW_PACKAGE_NAME = "com.samsung.android.authfw";
    public static final String FIND_MY_MOBILE_PACKAGE_NAME = "com.samsung.android.fmm";
    public static final String PRIVATE_SHARE_PACKAGE_NAME = "com.samsung.android.privateshare";
    public static final String SAMSUNG_ACCOUNT_PACKAGE_NAME = "com.osp.app.signin";
    public static final String SAMSUNG_FLOW_PACKAGE_NAME = "com.samsung.android.galaxycontinuity";
    public static final String SAMSUNG_HEALTH_PACKAGE_NAME = "com.sec.android.app.shealth";
    public static final String SAMSUNG_HEALTH_MONITOR_PACKAGE_NAME = "com.samsung.android.shealthmonitor";
    public static final String SAMSUNG_WALLET_PACKAGE_NAME = "com.samsung.android.spay";
    public static final String SECURE_FOLDER_PACKAGE_NAME = "com.samsung.knox.securefolder";
    public static final String SECURE_WIFI_PACKAGE_NAME = "com.samsung.android.fast";
    public static final String SYSTEM_PACKAGE_NAME = "android";

}
