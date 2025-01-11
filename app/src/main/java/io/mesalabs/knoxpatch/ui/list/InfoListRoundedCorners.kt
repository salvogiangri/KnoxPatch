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

package io.mesalabs.knoxpatch.ui.list

import android.content.Context
import android.graphics.Canvas

import androidx.appcompat.util.SeslRoundedCorner
import androidx.core.graphics.Insets
import androidx.recyclerview.widget.RecyclerView

class InfoListRoundedCorners(context: Context) : RecyclerView.ItemDecoration() {
    private val roundedCorner: SeslRoundedCorner

    init {
        roundedCorner = SeslRoundedCorner(context)
        roundedCorner.roundedCorners = SeslRoundedCorner.ROUNDED_CORNER_ALL
    }

    override fun seslOnDispatchDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        roundedCorner.drawRoundedCorner(c, Insets.of(parent.paddingLeft, 0, parent.paddingRight, 0))
    }
}
