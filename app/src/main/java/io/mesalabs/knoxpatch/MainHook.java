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

package io.mesalabs.knoxpatch;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import io.mesalabs.knoxpatch.hooks.AuthFwHooks;
import io.mesalabs.knoxpatch.hooks.FastHooks;
import io.mesalabs.knoxpatch.hooks.KnoxDARHooks;
import io.mesalabs.knoxpatch.hooks.KnoxGuardHooks;
import io.mesalabs.knoxpatch.hooks.TIMAHooks;
import io.mesalabs.knoxpatch.hooks.SamsungHealthHooks;
import io.mesalabs.knoxpatch.hooks.SamsungKeystoreHooks;
import io.mesalabs.knoxpatch.utils.BuildUtils;
import io.mesalabs.knoxpatch.utils.Constants;

public class MainHook implements IXposedHookLoadPackage {
    private static final String TAG = "MainHook";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        final int sepVersion = BuildUtils.getSEPVersion();

        switch (sepVersion) {
            case Constants.ONEUI_2_0:
            case Constants.ONEUI_2_1:
            case Constants.ONEUI_2_5: {
                if ((Constants.SYSTEM_PACKAGE_NAME.equals(lpparam.packageName))
                        && (lpparam.processName.equals(Constants.SYSTEM_PACKAGE_NAME))) {
                    new TIMAHooks().handleLoadPackage(lpparam);
                }
            } break;

            case Constants.ONEUI_4_0:
            case Constants.ONEUI_4_1:
            case Constants.ONEUI_4_1_1:
            case Constants.ONEUI_5_0: {
                if ((Constants.SYSTEM_PACKAGE_NAME.equals(lpparam.packageName))
                        && (lpparam.processName.equals(Constants.SYSTEM_PACKAGE_NAME))) {
                    new KnoxDARHooks().handleLoadPackage(lpparam);
                    new KnoxGuardHooks().handleLoadPackage(lpparam);
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
