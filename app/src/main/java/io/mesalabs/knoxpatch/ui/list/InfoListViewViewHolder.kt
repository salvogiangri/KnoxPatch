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

import android.view.View

import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView

import io.mesalabs.knoxpatch.R

class InfoListViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val titleTextView: AppCompatTextView =
        itemView.findViewById(R.id.list_item_title)
    val summaryTextView: AppCompatTextView =
        itemView.findViewById(R.id.list_item_summary)

}
