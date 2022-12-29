/*
 * KnoxPatch
 * Copyright (C) 2022 BlackMesa123
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

package io.mesalabs.knoxpatch;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import io.mesalabs.knoxpatch.hooks.FastHooks;
import io.mesalabs.knoxpatch.hooks.KnoxDARHooks;

public class MainHook implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (("android".equals(lpparam.packageName)) && (lpparam.processName.equals("android"))) {
            new KnoxDARHooks().handleLoadPackage(lpparam);
        }

        if ("com.samsung.android.fast".equals(lpparam.packageName)) {
            new FastHooks().handleLoadPackage(lpparam);
        }
    }

}
