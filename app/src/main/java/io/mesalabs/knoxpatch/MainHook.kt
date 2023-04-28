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

package io.mesalabs.knoxpatch

import com.highcapable.yukihookapi.YukiHookAPI.encase
import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.factory.configs
import com.highcapable.yukihookapi.hook.log.loggerE
import com.highcapable.yukihookapi.hook.xposed.bridge.event.YukiXposedEvent
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit

import de.robv.android.xposed.callbacks.XC_LoadPackage

import io.mesalabs.knoxpatch.hooks.AuthFwHooks
import io.mesalabs.knoxpatch.hooks.KnoxDARHooks
import io.mesalabs.knoxpatch.hooks.KnoxGuardHooks
import io.mesalabs.knoxpatch.hooks.PropSpoofHooks
import io.mesalabs.knoxpatch.hooks.RootDetectionHooks
import io.mesalabs.knoxpatch.hooks.SamsungHealthHooks
import io.mesalabs.knoxpatch.hooks.SamsungKeystoreHooks
import io.mesalabs.knoxpatch.hooks.TIMAHooks
import io.mesalabs.knoxpatch.utils.BuildUtils
import io.mesalabs.knoxpatch.utils.Constants

@InjectYukiHookWithXposed(isUsingResourcesHook = false)
object MainHook : IYukiHookXposedInit {
    private const val TAG: String = "MainHook"

    override fun onInit() = configs {
        debugLog {
            tag = "KnoxPatch"
            isEnable = true
            elements(TAG, PRIORITY, PACKAGE_NAME, USER_ID)
        }
        isDebug = BuildConfig.DEBUG
    }

    override fun onHook() = encase {
        // The OneUI version is determined by the SEP version
        // (SEP 14.0 == OneUI 5.0 | [SEP - 9.0 = OneUI] )
        val sepVersion: Int = BuildUtils.getSEPVersion()

        /*
         * Currently supported versions:
         * - Android 9 (One UI 1.x)
         * - Android 10 (One UI 2.x)
         * - Android 11 (One UI 3.x)
         * - Android 12/12.1 (One UI 4.x)
         * - Android 13 (One UI 5.x)
         */
        if (sepVersion < Constants.ONEUI_1_0 || sepVersion > Constants.ONEUI_5_1) {
            loggerE(msg = "$TAG: onHook: unsupported SEP version: $sepVersion")
            return@encase
        }

        if (sepVersion >= Constants.ONEUI_3_0) {
            loadSystem(KnoxDARHooks)
        } else if (sepVersion >= Constants.ONEUI_1_0) {
            loadSystem(TIMAHooks)
        }
        loadSystem(KnoxGuardHooks)

        loadApp(Constants.SECURE_FOLDER_PACKAGE_NAME, PropSpoofHooks)
        loadApp(Constants.SECURE_WIFI_PACKAGE_NAME, PropSpoofHooks)

        if (sepVersion >= Constants.ONEUI_1_5) {
            loadApp(Constants.FIND_MY_MOBILE_PACKAGE_NAME, SamsungKeystoreHooks)
            loadApp(Constants.SAMSUNG_ACCOUNT_PACKAGE_NAME, SamsungKeystoreHooks)
            loadApp(Constants.SAMSUNG_WALLET_PACKAGE_NAME, SamsungKeystoreHooks)
            loadApp(Constants.SECURE_WIFI_PACKAGE_NAME, SamsungKeystoreHooks)
            loadApp(Constants.PRIVATE_SHARE_PACKAGE_NAME, SamsungKeystoreHooks)
        }
    }

    override fun onXposedEvent() {
        YukiXposedEvent.onHandleLoadPackage { lpparam: XC_LoadPackage.LoadPackageParam ->
            run {
                // The OneUI version is determined by the SEP version
                // (SEP 14.0 == OneUI 5.0 | [SEP - 9.0 = OneUI] )
                val sepVersion: Int = BuildUtils.getSEPVersion()

                /*
                 * Currently supported versions:
                 * - Android 9 (One UI 1.x)
                 * - Android 10 (One UI 2.x)
                 * - Android 11 (One UI 3.x)
                 * - Android 12/12.1 (One UI 4.x)
                 * - Android 13 (One UI 5.x)
                 */
                if (sepVersion < Constants.ONEUI_1_0 || sepVersion > Constants.ONEUI_5_1) {
                    loggerE(msg = "$TAG handleLoadPackage: unsupported SEP version: $sepVersion")
                    return@onHandleLoadPackage
                }

                if (Constants.AUTHFW_PACKAGE_NAME == lpparam.packageName) {
                    AuthFwHooks().handleLoadPackage(lpparam)
                }

                if (Constants.SAMSUNG_HEALTH_PACKAGE_NAME == lpparam.packageName) {
                    SamsungHealthHooks().handleLoadPackage(lpparam)
                }

                if (Constants.SAMSUNG_FLOW_PACKAGE_NAME == lpparam.packageName ||
                    Constants.SAMSUNG_HEALTH_MONITOR_PACKAGE_NAME == lpparam.packageName) {
                    RootDetectionHooks().handleLoadPackage(lpparam)
                }
            }
        }
    }

}
