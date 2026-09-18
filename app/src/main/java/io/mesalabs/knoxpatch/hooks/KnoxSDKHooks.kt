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

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.YLog

import com.highcapable.kavaref.KavaRef.Companion.resolve

object KnoxSDKHooks : YukiBaseHooker() {
    private const val TAG: String = "KnoxSDKHooks"

    override fun onHook() {
        YLog.debug(msg = "$TAG: onHook: loaded.")

        /* Disable Knox support */
        "com.samsung.android.knox.EnterpriseDeviceManager".toClass().resolve()
            .firstMethod {
                name = "getAPILevel"
                emptyParameters()
                returnType = Int::class
            }.hook {
                replaceTo(-1)
            }

        /* Bypass Samsung Health's Knox integrity gate before key initialization */
        "com.samsung.android.service.health.security.KnoxAdapter".toClassOrNull()?.resolve()?.apply {
            firstMethodOrNull {
                name = "checkKnoxCompromisedExternal"
                parameterCount = 1
                returnType = String::class
            }?.hook {
                replaceTo(null)
            }

            firstMethodOrNull {
                name = "checkKnoxCompromisedInternal"
                parameterCount = 1
                returnType = Int::class
            }?.hook {
                replaceTo(0)
            }
        }
    }

}
