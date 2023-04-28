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

import android.os.Build

import java.io.IOException

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.StringClass

object RootDetectionHooks : YukiBaseHooker() {
    private const val TAG: String = "RootDetectionHooks"

    private val rootPackages = arrayOf(
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
    )

    override fun onHook() {
        loggerD(msg = "$TAG: onHook: loaded.")

        /* Spoof root checks */
        if (Build.TAGS.contains("test-keys")) {
            Build::class.java.field {
                name = "TAGS"
                type = StringClass
            }.get(null).set("release-keys")
        }

        findClass("java.io.File").hook {
            injectMember {
                constructor {
                    param(String::class.java)
                }
                beforeHook {
                    val pathname: String = args(0).string()

                    if (pathname.endsWith("su") || pathname.contains("Superuser.apk")) {
                        args(0).set("/system/xbin/fakefile")
                    }
                }
            }

            injectMember {
                constructor {
                    param(String::class.java, String::class.java)
                }
                beforeHook {
                    val child: String = args(1).string()

                    if (child == "su" || child == "busybox") {
                        args(1).set("fakebin")
                    }
                }
            }

            injectMember {
                method {
                    name = "canWrite"
                    emptyParam()
                    returnType = BooleanType
                }
                beforeHook {
                    resultFalse()
                }
            }
        }

        findClass("java.lang.Runtime").hook {
            injectMember {
                method {
                    name = "exec"
                    param(String::class.java)
                }
                beforeHook {
                    val command: String = args(0).string()

                    if (command == "su") {
                        IOException().throwToApp()
                    }
                }
            }

            injectMember {
                method {
                    name = "exec"
                    param(Array<String>::class.java)
                }
                beforeHook {
                    val cmdarray: Array<String> = args(0).array()

                    for (cmd in cmdarray) {
                        if (cmd.endsWith("/which") || cmd == "su") {
                            IOException().throwToApp()
                        }
                    }
                }
            }
        }

        findClass("android.app.ApplicationPackageManager").hook {
            injectMember {
                method {
                    name = "getPackageInfo"
                    param(String::class.java, Int)
                }
                beforeHook {
                    val packageName: String = args(0).string()

                    for (cmd in rootPackages) {
                        if (packageName == cmd) {
                            args(0).set("io.fake.pkg")
                            break
                        }
                    }
                }
            }
        }
    }

}
