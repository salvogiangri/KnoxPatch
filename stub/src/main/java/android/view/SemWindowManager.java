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

package android.view;

import dev.rikka.tools.refine.RefineAs;

@RefineAs(WindowManager.class)
public interface SemWindowManager {

    public static class LayoutParams {
        public static final int SEM_EXTENSION_FLAG_RESIZE_FULLSCREEN_WINDOW_ON_SOFT_INPUT = 1;

        public final void semAddExtensionFlags(int flags) {
            throw new RuntimeException("Stub!");
        }

        public final void semClearExtensionFlags(int flags) {
            throw new RuntimeException("Stub!");
        }
    }

}
