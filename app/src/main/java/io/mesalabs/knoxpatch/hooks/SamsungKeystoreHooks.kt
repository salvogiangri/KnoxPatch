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

object SamsungKeystoreHooks : YukiBaseHooker() {
    private const val TAG: String = "SamsungKeystoreHooks"

    override fun onHook() {
        YLog.debug(msg = "$TAG: onHook: loaded.")

        /* Bypass SAK integrity check */
        "com.samsung.android.security.keystore.AttestParameterSpec".toClassOrNull()?.resolve()?.apply {
            firstMethod {
                name = "isVerifiableIntegrity"
                emptyParameters()
                returnType = Boolean::class
            }.hook {
                replaceToTrue()
            }
        } ?: YLog.error(msg = "$TAG: couldn't access class " +
                "com.samsung.android.security.keystore.AttestParameterSpec")
    }

}
