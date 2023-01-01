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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unbound.patches.R;

public class InfoListViewAdapter extends RecyclerView.Adapter<InfoListViewViewHolder> {
    private final static int TITLE = 0;
    private final static int SUMMARY = 1;

    private final Context mContext;
    private final InfoListItemContent mListContent;
    private final View.OnClickListener mDummyClickListener = v -> { };

    public InfoListViewAdapter(@NonNull Context context) {
        super();
        mContext = context;
        mListContent = new InfoListItemContent(context);
    }

    @NonNull
    @Override
    public InfoListViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(
                R.layout.list_item_view, parent, false);
        return new InfoListViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InfoListViewViewHolder holder, int position) {
        holder.itemView.setOnClickListener(mDummyClickListener);

        String[] itemContent = mListContent.getItemContentAt(position);
        holder.getTitleTextView().setText(itemContent[TITLE]);
        holder.getSummaryTextView().setText(itemContent[SUMMARY]);
    }

    @Override
    public int getItemCount() {
        return mListContent.getListSize();
    }
}
