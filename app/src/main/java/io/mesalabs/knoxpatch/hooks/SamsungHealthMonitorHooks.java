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

package io.mesalabs.knoxpatch.hooks;

import android.os.Build;

import java.io.File;
import java.io.IOException;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class SamsungHealthMonitorHooks implements IXposedHookLoadPackage {
    private final static String TAG = "SamsungHealthMonitorHooks";

    private final String[] rootPackages = {
            "com.noshufou.android.su",
            "com.noshufou.android.su.elite",
            "eu.chainfire.supersu",
            "com.koushikdutta.superuser",
            "com.thirdparty.superuser",
            "com.yellowes.su",
            "com.devadvance.rootcloak",
            "com.devadvance.rootcloakplus",
            "de.robv.android.xposed.installer",
            "com.saurik.substrate",
            "com.zachspong.temprootremovejb",
            "com.amphoras.hidemyroot",
            "com.amphoras.hidemyrootadfree",
            "com.formyhm.hiderootPremium",
            "com.formyhm.hideroot",
            "com.koushikdutta.rommanager",
            "com.koushikdutta.rommanager.license",
            "com.dimonvideo.luckypatcher",
            "com.chelpus.lackypatch",
            "com.ramdroid.appquarantine",
            "com.ramdroid.appquarantinepro"
    };

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("KnoxPatch: " + TAG + " handleLoadPackage: " + lpparam.packageName);

        /* Spoof root checks in RootingCheckUtil class */

        // RootingCheckUtil.detectTestKeys();
        if (Build.TAGS.contains("test-keys")) {
            XposedHelpers.setStaticObjectField(
                    Build.class,
                    "TAGS",
                    "release-keys");
        }

        // RootingCheckUtil.checkBinaryPath();
        XposedHelpers.findAndHookConstructor(
                File.class,
                String.class, String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        String child = (String) param.args[1];

                        if (child != null) {
                            if (child.equals("su") || child.equals("busybox")) {
                                param.args[1] = "fakebin";
                            }
                        }
                    }
                });

        // RootingCheckUtil.checkSuExists();
        XposedHelpers.findAndHookMethod(
                Runtime.class,
                "exec", String[].class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        String[] cmdArray = (String[]) param.args[0];

                        if (cmdArray != null && cmdArray.length > 0) {
                            for (String cmd : cmdArray) {
                                if (cmd != null) {
                                    if (cmd.endsWith("/which") || cmd.equals("su")) {
                                        param.setThrowable(new IOException());
                                    }
                                }
                            }
                        }
                    }
                });

        // RootingCheckUtil.checkRootingPackage();
        XposedHelpers.findAndHookMethod(
                "android.app.ApplicationPackageManager",
                lpparam.classLoader,
                "getPackageInfo", String.class, int.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        String packageName = (String) param.args[0];

                        if (packageName != null) {
                            for (String cmd : rootPackages) {
                                if (packageName.equals(cmd)) {
                                    param.args[0] = "io.fake.pkg";
                                    break;
                                }
                            }
                        }
                    }
                });
    }

}
