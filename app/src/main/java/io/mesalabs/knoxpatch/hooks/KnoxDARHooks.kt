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

import java.security.cert.Certificate

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.java.BooleanType

object KnoxDARHooks : YukiBaseHooker() {
    private const val TAG: String = "KnoxDARHooks"

    override fun onHook() {
        loggerD(msg = "$TAG: onHook: loaded.")

        /* Bypass ICCC verification */
        if (Build.VERSION.SDK_INT >= 31) {
            findClass("com.android.server.knox.dar.DarManagerService").hook {
                injectMember {
                    method {
                        name = "checkDeviceIntegrity"
                        param(Array<Certificate>::class.java)
                        returnType = BooleanType
                    }
                    beforeHook {
                        resultTrue()
                    }
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
                    beforeHook {
                        resultTrue()
                    }
                }
            }
        }
    }

}
