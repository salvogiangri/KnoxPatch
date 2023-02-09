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

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.lang.reflect.Member;
import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class TIMAHooks implements IXposedHookLoadPackage {
    private final static String TAG = "TIMAHooks";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("KnoxPatch: " + TAG + " handleLoadPackage: " + lpparam.packageName);

        /* Disable TIMA */
        XposedHelpers.findAndHookMethod(
                "com.android.server.pm.PersonaServiceHelper",
                lpparam.classLoader,
                "isTimaAvailable", Context.class,
                XC_MethodReplacement.returnConstant(Boolean.TRUE));

        /* Enable Knox UKS */
        XposedHelpers.findAndHookMethod(
                "com.android.server.locksettings.SyntheticPasswordManager",
                lpparam.classLoader,
                "isUnifiedKeyStoreSupported",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        Log.d("SyntheticPasswordManager", "Unified KeyStore is supported");
                        param.setResult(Boolean.TRUE);
                    }
                });

        /* De-optimize inlined methods */
        findAndDeoptimizeMethod("com.android.server.locksettings.LockSettingsService",
                lpparam.classLoader,
                "verifyToken");
        findAndDeoptimizeMethod("com.android.server.locksettings.LockSettingsService$VirtualLock",
                lpparam.classLoader,
                "doVerifyCredential");
        findAndDeoptimizeMethod("com.android.server.locksettings.SyntheticPasswordManager",
                lpparam.classLoader,
                "createSyntheticPasswordBlobSpecific");
        findAndDeoptimizeMethod("com.android.server.locksettings.SyntheticPasswordManager",
                lpparam.classLoader,
                "destroySPBlobKey");
    }

    private static void findAndDeoptimizeMethod(@NonNull String className,
                                                @NonNull ClassLoader classLoader,
                                                @NonNull String methodName) {
        try {
            Class<?> clz = Class.forName(className, false, classLoader);
            for (Method m : clz.getDeclaredMethods()) {
                if (methodName.equals(m.getName())) {
                    XposedBridge.log("KnoxPatch: " + TAG + " findAndDeoptimizeMethod: " + m);
                    XposedBridge.class.getDeclaredMethod("deoptimizeMethod", Member.class).invoke(null, m);
                }
            }
        } catch (Throwable e) {
            XposedBridge.log("KnoxPatch: " + TAG + " findAndDeoptimizeMethod: " + e);
        }
    }

}
