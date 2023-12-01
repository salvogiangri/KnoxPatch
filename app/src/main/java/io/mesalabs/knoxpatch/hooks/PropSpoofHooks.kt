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

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.constructor
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.log.YLog
import com.highcapable.yukihookapi.hook.type.java.StringClass

object PropSpoofHooks : YukiBaseHooker() {
    private const val TAG: String = "PropSpoofHooks"

    override fun onHook() {
        YLog.debug(msg = "$TAG: onHook: loaded.")

        /* Spoof critical system props */
        "java.lang.ProcessBuilder".toClass()
            .constructor {
                param(Array<String>::class.java)
            }.hook {
                before {
                    val cmdarray: Array<String> = args(0).array()

                    // Fix SPCMAgent (SAK)
                    if (cmdarray.size == 2 && cmdarray[0] == "/system/bin/getprop") {
                        when (cmdarray[1]) {
                            "ro.build.type" -> args(0).set(
                                arrayOf("/system/bin/echo", "eng"))
                            "ro.security.keystore.keytype" -> args(0).set(
                                arrayOf("/system/bin/echo", ""))
                        }
                    }
                }
            }

        "android.os.SemSystemProperties".toClass().apply {
            method {
                name = "get"
                param(String::class.java)
                returnType = StringClass
            }.hook {
                before {
                    val key: String = args(0).string()

                    // Fixes:
                    // - Legacy Secure Wi-Fi (ICD)
                    // - SPCMAgent (SAK)
                    when (key) {
                        "ro.build.type" -> result = "eng"
                        "ro.security.keystore.keytype" -> result = ""
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

                    if (key == "ro.boot.flash.locked"
                        || key == "ro.boot.verifiedbootstate"
                        || key == "ro.boot.warranty_bit"
                        || key == "ro.config.iccc_version") {
                        result = def
                    }
                }
            }
        }

    }

}
