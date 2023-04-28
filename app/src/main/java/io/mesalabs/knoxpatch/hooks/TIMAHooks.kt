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

package io.mesalabs.knoxpatch.hooks

import android.content.Context
import android.util.Log

import java.lang.reflect.Member

import de.robv.android.xposed.XposedBridge

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.log.loggerE
import com.highcapable.yukihookapi.hook.type.java.BooleanType

object TIMAHooks : YukiBaseHooker() {
    private const val TAG: String = "TIMAHooks"

    override fun onHook() {
        loggerD(msg = "$TAG: onHook: loaded.")

        /* Disable TIMA */
        findClass("com.android.server.pm.PersonaServiceHelper").hook {
            injectMember {
                method {
                    name = "isTimaAvailable"
                    param(Context::class.java)
                    returnType = BooleanType
                }
                beforeHook {
                    resultTrue()
                }
            }
        }

        /* Enable Knox UKS */
        findClass("com.android.server.locksettings.SyntheticPasswordManager").hook {
            injectMember {
                method {
                    name = "isUnifiedKeyStoreSupported"
                    emptyParam()
                    returnType = BooleanType
                }
                beforeHook {
                    Log.d("SyntheticPasswordManager", "Unified KeyStore is supported")
                    resultTrue()
                }
            }
        }

        /* De-optimize inlined methods */
        findAndDeoptimizeMethod("com.android.server.locksettings.LockSettingsService",
            "verifyToken")
        findAndDeoptimizeMethod("com.android.server.locksettings.LockSettingsService\$VirtualLock",
            "doVerifyCredential")
        findAndDeoptimizeMethod("com.android.server.locksettings.SyntheticPasswordManager",
            "createSyntheticPasswordBlobSpecific")
        findAndDeoptimizeMethod("com.android.server.locksettings.SyntheticPasswordManager",
            "destroySPBlobKey")
    }

    private fun findAndDeoptimizeMethod(className: String,
                                        methodName: String) {
        try {
            val clz: Class<*> = Class.forName(className, false, appClassLoader)
            for (m in clz.declaredMethods) {
                if (methodName == m.name) {
                    loggerD(msg = "$TAG: findAndDeoptimizeMethod: $m")
                    XposedBridge::class.java.getDeclaredMethod("deoptimizeMethod", Member::class.java)
                        .invoke(null, m)
                }
            }
        } catch (e: Throwable) {
            loggerE(msg = "$TAG: findAndDeoptimizeMethod: $e")
        }
    }

}
