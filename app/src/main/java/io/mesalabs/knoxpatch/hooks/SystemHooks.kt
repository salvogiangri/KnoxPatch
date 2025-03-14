/*
 * KnoxPatch
 * Copyright (C) 2023 Salvo Giangreco
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
import android.os.Build

import java.lang.reflect.Member
import java.security.cert.Certificate

import de.robv.android.xposed.XposedBridge

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.constructor
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.log.YLog
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.StringClass

import io.mesalabs.knoxpatch.utils.BuildUtils
import io.mesalabs.knoxpatch.utils.Constants

object SystemHooks : YukiBaseHooker()  {
    private const val TAG: String = "SystemHooks"

    override fun onHook() {
        YLog.debug(msg = "$TAG: onHook: loaded.")

        /* Fix Secure Folder/Work profile */
        val sepVersion: Int = BuildUtils.getSEPVersion()
        if (sepVersion >= Constants.ONEUI_3_0) {
            applySAKHooks()
        } else if (sepVersion >= Constants.ONEUI_1_0) {
            applyTIMAHooks()
        }

        /* Disable KnoxGuard support */
        applyKGHooks()

        /* Disable ASKS */
        applySPHooks()
    }

    private fun applySAKHooks() {
        if (Build.VERSION.SDK_INT >= 35) {
            "com.samsung.android.security.keystore.AttestParameterSpec".toClassOrNull()?.apply {
                constructor {
                    paramCount = 5
                }.hook {
                    before {
                        args(2).set(true)
                    }
                }
            } ?: YLog.error(msg = "$TAG: couldn't access class " +
                    "com.samsung.android.security.keystore.AttestParameterSpec")
        }

        if (Build.VERSION.SDK_INT >= 31) {
            "com.android.server.knox.dar.DarManagerService".toClass()
                .method {
                    name = "checkDeviceIntegrity"
                    param(Array<Certificate>::class.java)
                    returnType = BooleanType
                }.hook {
                    replaceToTrue()
                }
        } else {
            "com.android.server.pm.PersonaManagerService".toClass()
                .method {
                    name = "isKnoxKeyInstallable"
                    emptyParam()
                    returnType = BooleanType
                }.hook {
                    replaceToTrue()
                }
        }
    }

    private fun applyTIMAHooks() {
        "com.android.server.pm.PersonaServiceHelper".toClass()
            .method {
                name = "isTimaAvailable"
                param(Context::class.java)
                returnType = BooleanType
            }.hook {
                replaceToTrue()
            }

        if (Build.VERSION.SDK_INT >= 29) {
            "com.android.server.SdpManagerService\$LocalService".toClass()
                .method {
                    name = "isKnoxKeyInstallable"
                    emptyParam()
                    returnType = BooleanType
                }.hook {
                    replaceToTrue()
                }
        }

        "com.android.server.locksettings.SyntheticPasswordManager".toClass()
            .method {
                name = "isUnifiedKeyStoreSupported"
                emptyParam()
                returnType = BooleanType
            }.hook {
                replaceToTrue()
            }

        findAndDeoptimizeMethod("com.android.server.locksettings.LockSettingsService",
            "verifyToken")
        findAndDeoptimizeMethod("com.android.server.locksettings.LockSettingsService\$VirtualLock",
            "doVerifyCredential")
        findAndDeoptimizeMethod("com.android.server.locksettings.SyntheticPasswordManager",
            "createSyntheticPasswordBlobSpecific")
        findAndDeoptimizeMethod("com.android.server.locksettings.SyntheticPasswordManager",
            "destroySPBlobKey")
    }

    private fun applyKGHooks() {
        if (Build.VERSION.SDK_INT < 35) {
            "com.samsung.android.knoxguard.service.KnoxGuardService".toClass()
                .constructor {
                    param(Context::class.java)
                }.hook {
                    before {
                        UnsupportedOperationException("KnoxGuard is unsupported").throwToApp()
                    }
                }
        }

        if (Build.VERSION.SDK_INT >= 30) {
            "com.samsung.android.knoxguard.service.KnoxGuardSeService".toClass()
                .constructor {
                    param(Context::class.java)
                }.hook {
                    before {
                        UnsupportedOperationException("KnoxGuard is unsupported").throwToApp()
                    }
                }
        }
    }

    private fun applySPHooks() {
        "android.os.SystemProperties".toClass().apply {
            method {
                name = "get"
                param(String::class.java)
                returnType = StringClass
            }.hook {
                before {
                    val key: String = args(0).string()

                    if (key == "ro.build.official.release") {
                        result = "false"
                    }
                }
            }

            method {
                name = "get"
                param(String::class.java, String::class.java)
                returnType = StringClass
            }.hook {
                before {
                    val key: String = args(0).string()
                    val def: String = args(1).string()

                    if (key == "ro.build.official.release") {
                        result = def
                    }
                }
            }
        }
    }

    private fun findAndDeoptimizeMethod(className: String,
                                        methodName: String) {
        try {
            val clz: Class<*> = Class.forName(className, false, appClassLoader)
            for (m in clz.declaredMethods) {
                if (methodName == m.name) {
                    YLog.debug(msg = "$TAG: findAndDeoptimizeMethod: $m")
                    XposedBridge::class.java.getDeclaredMethod("deoptimizeMethod", Member::class.java)
                        .invoke(null, m)
                }
            }
        } catch (e: Throwable) {
            YLog.error(msg = "$TAG: findAndDeoptimizeMethod: $e")
        }
    }

}
