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

package io.mesalabs.knoxpatch.ui.list

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup.MarginLayoutParams

import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.util.SeslRoundedCorner
import androidx.core.graphics.Insets
import androidx.core.view.forEach
import androidx.recyclerview.widget.RecyclerView

class InfoListItemDecoration(context: Context) : RecyclerView.ItemDecoration() {
    private val roundedCorner: SeslRoundedCorner
    private val divider: Drawable

    init {
        roundedCorner = SeslRoundedCorner(context)
        roundedCorner.roundedCorners = SeslRoundedCorner.ROUNDED_CORNER_ALL

        val typedValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.listDivider, typedValue, true)
        divider = AppCompatResources.getDrawable(context, typedValue.resourceId)!!
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        val left: Int
        val right: Int
        if (parent.layoutDirection == View.LAYOUT_DIRECTION_RTL) {
            left = parent.left + parent.paddingEnd
            right = parent.right - parent.paddingStart
        } else {
            left = parent.left + parent.paddingStart
            right = parent.right - parent.paddingEnd
        }

        parent.forEach {
            val top: Int = it.bottom + (it.layoutParams as MarginLayoutParams).bottomMargin
            val bottom: Int = divider.intrinsicHeight + top
            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
        }
    }

    override fun seslOnDispatchDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        roundedCorner.drawRoundedCorner(c, Insets.of(parent.paddingLeft, 0, parent.paddingRight, 0))
    }
}
