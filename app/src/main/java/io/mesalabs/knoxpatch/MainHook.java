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
import io.mesalabs.knoxpatch.hooks.PropSpoofHooks;
import io.mesalabs.knoxpatch.hooks.KnoxDARHooks;
import io.mesalabs.knoxpatch.hooks.KnoxGuardHooks;
import io.mesalabs.knoxpatch.hooks.RootDetectionHooks;
import io.mesalabs.knoxpatch.hooks.TIMAHooks;
import io.mesalabs.knoxpatch.hooks.SamsungHealthHooks;
import io.mesalabs.knoxpatch.hooks.SamsungKeystoreHooks;
import io.mesalabs.knoxpatch.utils.BuildUtils;
import io.mesalabs.knoxpatch.utils.Constants;

public class MainHook implements IXposedHookLoadPackage {
    private static final String TAG = "MainHook";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        // The OneUI version is determined by the SEP version
        // (SEP 14.0 == OneUI 5.0 | [SEP - 9.0 = OneUI] )
        final int sepVersion = BuildUtils.getSEPVersion();

        /*
         * Currently supported versions:
         * - Android 9 (One UI 1.x)
         * - Android 10 (One UI 2.x)
         * - Android 11 (One UI 3.x)
         * - Android 12/12.1 (One UI 4.x)
         * - Android 13 (One UI 5.x)
         */
        if (sepVersion < Constants.ONEUI_1_0 || sepVersion > Constants.ONEUI_5_1) {
            XposedBridge.log("KnoxPatch: " + TAG + " handleLoadPackage: "
                    + "unsupported SEP version: " + sepVersion);
            return;
        }

        if ((Constants.SYSTEM_PACKAGE_NAME.equals(lpparam.packageName))
                && (lpparam.processName.equals(Constants.SYSTEM_PACKAGE_NAME))) {
            if (sepVersion >= Constants.ONEUI_4_0) {
                new KnoxDARHooks().handleLoadPackage(lpparam);
            } else if (sepVersion >= Constants.ONEUI_3_0) {
                // no-op
            } else if (sepVersion >= Constants.ONEUI_1_0) {
                new TIMAHooks().handleLoadPackage(lpparam);
            }

            new KnoxGuardHooks().handleLoadPackage(lpparam);
        }

        if (Constants.AUTHFW_PACKAGE_NAME.equals(lpparam.packageName)) {
            new AuthFwHooks().handleLoadPackage(lpparam);
        }

        if (Constants.SECURE_FOLDER_PACKAGE_NAME.equals(lpparam.packageName) ||
                Constants.SECURE_WIFI_PACKAGE_NAME.equals(lpparam.packageName)) {
            new PropSpoofHooks().handleLoadPackage(lpparam);
        }

        if (Constants.FIND_MY_MOBILE_PACKAGE_NAME.equals(lpparam.packageName) ||
                Constants.SAMSUNG_ACCOUNT_PACKAGE_NAME.equals(lpparam.packageName) ||
                Constants.SAMSUNG_WALLET_PACKAGE_NAME.equals(lpparam.packageName) ||
                Constants.SECURE_WIFI_PACKAGE_NAME.equals(lpparam.packageName) ||
                Constants.PRIVATE_SHARE_PACKAGE_NAME.equals(lpparam.packageName)) {
            new SamsungKeystoreHooks().handleLoadPackage(lpparam);
        }

        if (Constants.SAMSUNG_HEALTH_PACKAGE_NAME.equals(lpparam.packageName)) {
            new SamsungHealthHooks().handleLoadPackage(lpparam);
        }

        if (Constants.SAMSUNG_FLOW_PACKAGE_NAME.equals(lpparam.packageName) ||
                Constants.SAMSUNG_HEALTH_MONITOR_PACKAGE_NAME.equals(lpparam.packageName) ) {
            new RootDetectionHooks().handleLoadPackage(lpparam);
        }
    }

}
