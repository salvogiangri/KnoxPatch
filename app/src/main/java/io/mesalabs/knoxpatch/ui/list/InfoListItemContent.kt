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

import io.mesalabs.knoxpatch.R
import io.mesalabs.knoxpatch.ui.list.InfoListViewUtils.getAndroidVersion
import io.mesalabs.knoxpatch.ui.list.InfoListViewUtils.getBuildNumber
import io.mesalabs.knoxpatch.ui.list.InfoListViewUtils.getKnoxComponentsVersion
import io.mesalabs.knoxpatch.ui.list.InfoListViewUtils.getKnoxFeatures
import io.mesalabs.knoxpatch.ui.list.InfoListViewUtils.getOneUIVersion
import io.mesalabs.knoxpatch.ui.list.InfoListViewUtils.isKnoxAvailable
import io.mesalabs.knoxpatch.ui.list.InfoListViewUtils.isSepLiteAvailable

class InfoListItemContent(context: Context) {
    private val titles: ArrayList<String> = ArrayList()
    private val summaries: ArrayList<String> = ArrayList()

    init {
        titles.add(context.getString(
            if (isSepLiteAvailable(context))
                R.string.oneui_version_title_seplite
            else
                R.string.oneui_version_title))
        summaries.add(getOneUIVersion())
        titles.add(context.getString(R.string.android_version_title))
        summaries.add(getAndroidVersion())
        titles.add(context.getString(R.string.build_number_title))
        summaries.add(getBuildNumber())
        titles.add(context.getString(R.string.knox_version_title))
        summaries.add(
            if (isKnoxAvailable())
                getKnoxComponentsVersion(context)
            else
                "Unsupported")
        titles.add(context.getString(R.string.knox_features_title))
        summaries.add(getKnoxFeatures())
    }

    fun getItemContentAt(index: Int): Array<String> {
        return arrayOf(titles[index], summaries[index])
    }

    val listSize: Int
        get() {
            val titlesSize = titles.size
            val summariesSize = summaries.size

            if (titlesSize == summariesSize) {
                return titlesSize
            } else {
                throw RuntimeException("Something's wrong with the items list.")
            }
        }

}
