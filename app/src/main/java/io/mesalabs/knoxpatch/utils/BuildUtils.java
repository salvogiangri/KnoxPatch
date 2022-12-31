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

package io.mesalabs.knoxpatch.utils;

import android.os.SemBuild;

import com.samsung.android.knox.EdmUtils;
import com.samsung.android.knox.EnterpriseDeviceManager;

public class BuildUtils {

    public static int getSEPVersion() {
        try {
            final int sepVersion = SemBuild.VERSION.SEM_PLATFORM_INT;
            return sepVersion;
        } catch (NoSuchFieldError e) {
            return -1;
        }
    }

    public static int getKnoxAPIVersion() {
        switch (getSEPVersion()) {
            case Constants.ONEUI_4_1:
                return EnterpriseDeviceManager.getAPILevelForInternal();
            case Constants.ONEUI_5_0:
                return EdmUtils.getAPILevelForInternal();
            default:
                return -1;
        }
    }

}
