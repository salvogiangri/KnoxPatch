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
import android.os.Build

import java.lang.reflect.Member
import java.security.cert.Certificate

import de.robv.android.xposed.XposedBridge

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.log.loggerE
import com.highcapable.yukihookapi.hook.type.java.BooleanType

import io.mesalabs.knoxpatch.utils.BuildUtils
import io.mesalabs.knoxpatch.utils.Constants

object SystemHooks : YukiBaseHooker()  {
    private const val TAG: String = "SystemHooks"

    override fun onHook() {
        loggerD(msg = "$TAG: onHook: loaded.")

        /* Fix Secure Folder/Work profile */
        val sepVersion: Int = BuildUtils.getSEPVersion()
        if (sepVersion >= Constants.ONEUI_3_0) {
            applySAKHooks()
        } else if (sepVersion >= Constants.ONEUI_1_0) {
            applyTIMAHooks()
        }

        /* Disable KnoxGuard support */
        applyKGHooks()
    }

    private fun applySAKHooks() {
        if (Build.VERSION.SDK_INT >= 31) {
            findClass("com.android.server.knox.dar.DarManagerService").hook {
                injectMember {
                    method {
                        name = "checkDeviceIntegrity"
                        param(Array<Certificate>::class.java)
                        returnType = BooleanType
                    }
                    replaceToTrue()
                }
            }
        } else {
            findClass("com.android.server.pm.PersonaManagerService").hook {
                injectMember {
                    method {
                        name = "isKnoxKeyInstallable"
                        emptyParam()
                        returnType = BooleanType
                    }
                    replaceToTrue()
                }
            }
        }
    }

    private fun applyTIMAHooks() {
        findClass("com.android.server.pm.PersonaServiceHelper").hook {
            injectMember {
                method {
                    name = "isTimaAvailable"
                    param(Context::class.java)
                    returnType = BooleanType
                }
                replaceToTrue()
            }
        }

        if (Build.VERSION.SDK_INT >= 29) {
            findClass("com.android.server.SdpManagerService\$LocalService").hook {
                injectMember {
                    method {
                        name = "isKnoxKeyInstallable"
                        emptyParam()
                        returnType = BooleanType
                    }
                    replaceToTrue()
                }
            }
        }

        findClass("com.android.server.locksettings.SyntheticPasswordManager").hook {
            injectMember {
                method {
                    name = "isUnifiedKeyStoreSupported"
                    emptyParam()
                    returnType = BooleanType
                }
                replaceToTrue()
            }
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
        findClass("com.samsung.android.knoxguard.service.KnoxGuardService").hook {
            injectMember {
                constructor {
                    param(Context::class.java)
                }
                beforeHook {
                    UnsupportedOperationException("KnoxGuard is unsupported").throwToApp()
                }
            }
        }

        if (Build.VERSION.SDK_INT >= 30) {
            findClass("com.samsung.android.knoxguard.service.KnoxGuardSeService").hook {
                injectMember {
                    constructor {
                        param(Context::class.java)
                    }
                    beforeHook {
                        UnsupportedOperationException("KnoxGuard is unsupported").throwToApp()
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
