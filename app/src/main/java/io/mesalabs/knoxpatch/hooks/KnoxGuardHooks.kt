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

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD

object KnoxGuardHooks : YukiBaseHooker() {
    private const val TAG: String = "KnoxGuardHooks"

    override fun onHook() {
        loggerD(msg = "$TAG: onHook: loaded.")

        /* Disable KnoxGuard support */
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

}
