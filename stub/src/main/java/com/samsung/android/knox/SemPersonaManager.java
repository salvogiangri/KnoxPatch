/*
 * KnoxPatch
 * Copyright (C) 2022 Salvo Giangreco
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

package com.samsung.android.knox;

public class SemPersonaManager {

    public enum KnoxContainerVersion {
        KNOX_CONTAINER_VERSION_NONE,
        KNOX_CONTAINER_VERSION_1_0_0,
        KNOX_CONTAINER_VERSION_2_0_0,
        KNOX_CONTAINER_VERSION_2_1_0,
        KNOX_CONTAINER_VERSION_2_2_0,
        KNOX_CONTAINER_VERSION_2_3_0,
        KNOX_CONTAINER_VERSION_2_3_1,
        KNOX_CONTAINER_VERSION_2_4_0,
        KNOX_CONTAINER_VERSION_2_4_1,
        KNOX_CONTAINER_VERSION_2_5_0,
        KNOX_CONTAINER_VERSION_2_5_1,
        KNOX_CONTAINER_VERSION_2_5_2,
        KNOX_CONTAINER_VERSION_2_6_0,
        KNOX_CONTAINER_VERSION_2_6_1,
        KNOX_CONTAINER_VERSION_2_7_0,
        KNOX_CONTAINER_VERSION_2_7_1,
        KNOX_CONTAINER_VERSION_2_8_0,
        KNOX_CONTAINER_VERSION_2_9_0,
        KNOX_CONTAINER_VERSION_3_0_0,
        KNOX_CONTAINER_VERSION_3_1_0,
        KNOX_CONTAINER_VERSION_3_2_0,
        KNOX_CONTAINER_VERSION_3_2_1,
        KNOX_CONTAINER_VERSION_3_3_0,
        KNOX_CONTAINER_VERSION_3_4_0,
        KNOX_CONTAINER_VERSION_3_4_1,
        KNOX_CONTAINER_VERSION_3_5_0,
        KNOX_CONTAINER_VERSION_3_6_0,
        KNOX_CONTAINER_VERSION_3_7_0,
        KNOX_CONTAINER_VERSION_3_7_1,
        KNOX_CONTAINER_VERSION_3_8_0,
        KNOX_CONTAINER_VERSION_3_9_0,
        KNOX_CONTAINER_VERSION_3_10_0
    }

    public static KnoxContainerVersion getKnoxContainerVersion() {
        throw new RuntimeException("Stub!");
    }

}
