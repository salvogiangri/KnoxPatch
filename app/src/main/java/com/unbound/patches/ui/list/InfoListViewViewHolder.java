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

package com.unbound.patches.ui.list;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.unbound.patches.R;

class InfoListViewViewHolder extends RecyclerView.ViewHolder {
    @NonNull private final AppCompatTextView mTitle;
    @NonNull private final AppCompatTextView mSummary;

    InfoListViewViewHolder(@NonNull View itemView) {
        super(itemView);
        mTitle = itemView.findViewById(R.id.list_item_title);
        mSummary = itemView.findViewById(R.id.list_item_summary);
    }

    @NonNull
    AppCompatTextView getTitleTextView() {
        return mTitle;
    }

    @NonNull
    AppCompatTextView getSummaryTextView() {
        return mSummary;
    }
}
