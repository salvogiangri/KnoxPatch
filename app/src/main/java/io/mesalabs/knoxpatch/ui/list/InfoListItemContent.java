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

package io.mesalabs.knoxpatch.ui.list;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

import io.mesalabs.knoxpatch.R;

public class InfoListItemContent {
    private final List<String> mTitles = new ArrayList<>();
    private final List<String> mSummaries = new ArrayList<>();

    InfoListItemContent(@NonNull Context context) {
        mTitles.add(context.getString(R.string.oneui_version_title));
        mSummaries.add(InfoListViewUtils.getOneUIVersion());
        mTitles.add(context.getString(R.string.android_version_title));
        mSummaries.add(InfoListViewUtils.getAndroidVersion());
        mTitles.add(context.getString(R.string.build_number_title));
        mSummaries.add(InfoListViewUtils.getBuildNumber());
        mTitles.add(context.getString(R.string.knox_version_title));
        mSummaries.add(InfoListViewUtils.getKnoxComponentsVersion(context));
        mTitles.add(context.getString(R.string.knox_features_title));
        mSummaries.add(InfoListViewUtils.getKnoxFeatures());
    }

    public String[] getItemContentAt(int index) {
        return new String[] {
                mTitles.get(index),
                mSummaries.get(index)
        };
    }

    public int getListSize() {
        final int titlesSize = mTitles.size();
        final int summariesSize = mSummaries.size();

        if (titlesSize == summariesSize) {
            return titlesSize;
        } else {
            throw new RuntimeException("Something's wrong with the items list.");
        }
    }

}
