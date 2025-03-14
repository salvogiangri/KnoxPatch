/*
 * KnoxPatch
 * Copyright (C) 2024 Salvo Giangreco
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

object KnoxMatrixHooks : YukiBaseHooker() {
    private const val TAG: String = "KnoxMatrixHooks"

    override fun onHook() {
        YLog.debug(msg = "$TAG: onHook: loaded.")

        /* Bypass ROT/Knox integrity status check */
        "com.samsung.android.kmxservice.fabrickeystore.keystore.cert.FabricCertUtil".toClass().apply {
            method {
                name = "checkIntegrityStatus"
                paramCount = 1
            }.hook {
                replaceToTrue()
            }

            method {
                name = "checkRootOfTrust"
                paramCount = 1
            }.hook {
                replaceToTrue()
            }
        }
    }

}
