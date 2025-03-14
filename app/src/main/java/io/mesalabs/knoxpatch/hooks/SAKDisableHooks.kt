/*
 * KnoxPatch
 * Copyright (C) 2025 Salvo Giangreco
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
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.log.YLog
import com.highcapable.yukihookapi.hook.type.java.StringClass

object SAKDisableHooks : YukiBaseHooker() {
    private const val TAG: String = "DisableSAKHooks"

    override fun onHook() {
        YLog.debug(msg = "$TAG: onHook: loaded.")

        /* Disable SAK support */
        "android.os.SemSystemProperties".toClass()
            .method {
                name = "get"
                param(String::class.java, String::class.java)
                returnType = StringClass
            }.hook {
                before {
                    val key: String = args(0).string()
                    val def: String = args(1).string()

                    if (key == "ro.security.keystore.keytype") {
                        result = def
                    }
                }
            }
    }

}
