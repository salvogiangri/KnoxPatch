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

package io.mesalabs.knoxpatch.ui.list;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.SemBuild;
import android.os.SemSystemProperties;
import com.samsung.android.knox.SemPersonaManager;
import com.samsung.android.knox.SemPersonaManager.KnoxContainerVersion;
import com.samsung.android.knox.ddar.DualDARPolicy;
import com.samsung.android.knox.hdm.HdmManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;

import org.lsposed.hiddenapibypass.HiddenApiBypass;

import io.mesalabs.knoxpatch.R;
import io.mesalabs.knoxpatch.utils.BuildUtils;
import io.mesalabs.knoxpatch.utils.Constants;

public class InfoListViewUtils {
    private static final int ONE_UI_VERSION_SEP_VERSION_GAP = 90000;

    private enum EnterpriseKnoxSdkVersion {
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

        String getInternalVersion() {
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

    public static String getAndroidVersion() {
        return Build.VERSION.RELEASE;
    }

    public static String getBuildNumber() {
        return Build.DISPLAY;
    }

    private static EnterpriseKnoxSdkVersion getEnterpriseKnoxSdkVersion() {
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

    public static String getKnoxComponentsVersion(@NonNull Context context) {
        final KnoxContainerVersion knoxContainerVersion = SemPersonaManager.getKnoxContainerVersion();
        if (knoxContainerVersion.compareTo(
                KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_2_0) >= 0) {
            String summary = "";
            summary += context.getString(R.string.knox_version_knox) + " ";

            String knoxVersion = getEnterpriseKnoxSdkVersion().getInternalVersion();
            if (Integer.parseInt(Character.toString(knoxVersion.charAt(knoxVersion.length() - 1))) > 0) {
                summary += knoxVersion;
            } else {
                summary += knoxVersion.substring(0, knoxVersion.lastIndexOf('.'));
            }

            summary += "\n" + context.getString(R.string.knox_version_knox_api) + " ";
            summary += BuildUtils.getKnoxAPIVersion();

            try {
                PackageInfo knoxMLApp = context.getPackageManager().getPackageInfo(
                        "com.samsung.android.app.kfa", PackageManager.GET_META_DATA);
                if (knoxMLApp != null) {
                    summary += "\n" + context.getString(R.string.knox_version_knox_ml) + " ";

                    String knoxMLVersion = knoxMLApp.versionName;
                    summary += knoxMLVersion.substring(0, knoxMLVersion.length() - 3);
                }
            } catch (PackageManager.NameNotFoundException e) {
                // no-op
            }

            String dualDARVersion = DualDARPolicy.getDualDARVersion();
            if (dualDARVersion != null) {
                summary += "\n" + context.getString(R.string.knox_version_knox_dualdar) + " ";
                summary += dualDARVersion;
            }

            String hdmVersion = HdmManager.getHdmVersion();
            if (hdmVersion != null) {
                summary += "\n" + context.getString(R.string.knox_version_knox_hdm) + " ";
                summary += hdmVersion;
            }

            return summary;
        } else {
            return "Unsupported";
        }
    }

    public static String getKnoxFeatures() {
        List<String> features = new ArrayList<>();

        if (BuildUtils.getSEPVersion() == Constants.ONEUI_5_0) {
            try {
                Class<?> cls = Class.forName("com.samsung.android.knox.dar.DarRune");
                if (cls != null) {
                    List<Field> fields = HiddenApiBypass.getStaticFields(cls);

                    // KNOX_SUPPORT_*
                    for (Field field : fields) {
                        if (field.getName().contains("KNOX_SUPPORT_")) {
                            features.add(field.getName() + " = " + field.get(null));
                        }
                    }
                }
            } catch (ClassNotFoundException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            Class<?> cls = Class.forName("com.samsung.android.rune.CoreRune");
            if (cls != null) {
                List<Field> fields = HiddenApiBypass.getStaticFields(cls);

                // KNOX_SUPPORT_*
                for (Field field : fields) {
                    if (field.getName().contains("KNOX_SUPPORT_")) {
                        features.add(field.getName() + " = " + field.get(null));
                    }
                }

                Collections.sort(features);

                // SUPPORT_KNOX
                for (Field field : fields) {
                    if (field.getName().contains("SUPPORT_KNOX")) {
                        features.add(0, field.getName() + " = " + field.get(null));
                    }
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return String.join("\n", features);
    }

    public static String getOneUIVersion() {
        final int oneUiOwnVersion = SemSystemProperties.getInt("ro.build.version.oneui", 0);

        if (oneUiOwnVersion > 0) {
            final int major = oneUiOwnVersion / 10000;
            final int minor = (oneUiOwnVersion % 10000) / 100;
            final int patch = oneUiOwnVersion % 100;

            if (patch == 0) {
                return major + "." + minor;
            } else {
                return major + "." + minor + "." + patch;
            }
        } else {
            try {
                final int sepVersion = SemBuild.VERSION.SEM_PLATFORM_INT - ONE_UI_VERSION_SEP_VERSION_GAP;
                return sepVersion / 10000 + "." + (sepVersion % 10000) / 100;
            } catch (NoSuchFieldError e) {
                return "Unknown";
            }
        }
    }
}
