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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView

import io.mesalabs.knoxpatch.R

class InfoListViewAdapter(context: Context) : RecyclerView.Adapter<InfoListViewViewHolder>() {
    companion object {
        private const val TITLE: Int = 0
        private const val SUMMARY: Int = 1
    }

    private val context: Context
    private val listContent: InfoListItemContent
    private val dummyClickListener: View.OnClickListener = View.OnClickListener {  }

    init {
        this.context = context
        listContent = InfoListItemContent(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoListViewViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = layoutInflater.inflate(
            R.layout.list_item_view, parent, false)
        return InfoListViewViewHolder(view)
    }

    override fun onBindViewHolder(holder: InfoListViewViewHolder, position: Int) {
        holder.itemView.setOnClickListener(dummyClickListener)

        val itemContent: Array<String> = listContent.getItemContentAt(position)
        holder.titleTextView.text = itemContent[TITLE]
        holder.summaryTextView.text = itemContent[SUMMARY]
    }

    override fun getItemCount(): Int {
        return listContent.listSize
    }
}
