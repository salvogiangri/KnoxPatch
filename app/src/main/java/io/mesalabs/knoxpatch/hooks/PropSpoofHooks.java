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

package io.mesalabs.knoxpatch.hooks;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class PropSpoofHooks implements IXposedHookLoadPackage {
    private final static String TAG = "PropSpoofHooks";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("KnoxPatch: " + TAG + " handleLoadPackage: " + lpparam.packageName);

        /* Spoof critical system props */
        XposedHelpers.findAndHookMethod(
                "android.os.SemSystemProperties",
                lpparam.classLoader,
                "get", String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        String key = (String) param.args[0];

                        if (key.equals("ro.build.type")) {
                            param.setResult("eng");
                        }
                    }
                });
        XposedHelpers.findAndHookMethod(
                "android.os.SemSystemProperties",
                lpparam.classLoader,
                "get", String.class, String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        String key = (String) param.args[0];
                        String def = (String) param.args[1];

                        if (key.equals("ro.boot.flash.locked")
                                || key.equals("ro.boot.warranty_bit")
                                || key.equals("ro.config.iccc_version")) {
                            param.setResult(def);
                        }
                    }
                });
    }

}
