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

package com.unbound.patches;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import com.unbound.patches.hooks.AuthFwHooks;
import com.unbound.patches.hooks.FastHooks;
import com.unbound.patches.hooks.KnoxDARHooks;
import com.unbound.patches.hooks.KnoxGuardHooks;
import com.unbound.patches.hooks.SamsungHealthHooks;
import com.unbound.patches.hooks.SamsungKeystoreHooks;
import com.unbound.patches.hooks.ScreenshotHooks;
import com.unbound.patches.utils.BuildUtils;
import com.unbound.patches.utils.Constants;

public class MainHook implements IXposedHookLoadPackage {
    private static final String TAG = "MainHook";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        final int sepVersion = BuildUtils.getSEPVersion();

        switch (sepVersion) {
            case Constants.ONEUI_4_0:
            case Constants.ONEUI_4_1:
            case Constants.ONEUI_4_1_1:
            case Constants.ONEUI_5_0: {
                if ((Constants.SYSTEM_PACKAGE_NAME.equals(lpparam.packageName))
                        && (lpparam.processName.equals(Constants.SYSTEM_PACKAGE_NAME))) {
                    new KnoxDARHooks().handleLoadPackage(lpparam);
                    new KnoxGuardHooks().handleLoadPackage(lpparam);
                    new ScreenshotHooks().handleLoadPackage(lpparam);
                }

                if (Constants.AUTHFW_PACKAGE_NAME.equals(lpparam.packageName)) {
                    new AuthFwHooks().handleLoadPackage(lpparam);
                }

                if (Constants.SECURE_WIFI_PACKAGE_NAME.equals(lpparam.packageName)) {
                    new SamsungKeystoreHooks().handleLoadPackage(lpparam);
                    new FastHooks().handleLoadPackage(lpparam);
                }

                if (Constants.PRIVATE_SHARE_PACKAGE_NAME.equals(lpparam.packageName)) {
                    new SamsungKeystoreHooks().handleLoadPackage(lpparam);
                }

                if (Constants.SAMSUNG_HEALTH_PACKAGE_NAME.equals(lpparam.packageName)) {
                    new SamsungHealthHooks().handleLoadPackage(lpparam);
                }
            } break;

            default:
                XposedBridge.log("KnoxPatch: " + TAG + " handleLoadPackage: "
                        + "unsupported SEP version: " + sepVersion);
                break;
        }
    }

}
