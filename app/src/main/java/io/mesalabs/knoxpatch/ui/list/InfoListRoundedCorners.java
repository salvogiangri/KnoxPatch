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
import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.appcompat.util.SeslRoundedCorner;
import androidx.recyclerview.widget.RecyclerView;

public class InfoListRoundedCorners extends RecyclerView.ItemDecoration {
    private final SeslRoundedCorner mRoundedCorner;

    public InfoListRoundedCorners(@NonNull Context context) {
        mRoundedCorner = new SeslRoundedCorner(context);
        mRoundedCorner.setRoundedCorners(SeslRoundedCorner.ROUNDED_CORNER_ALL);
    }

    @Override
    public void seslOnDispatchDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        mRoundedCorner.drawRoundedCorner(c);
    }
}
