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

import java.io.File
import java.util.concurrent.TimeUnit

import org.gradle.api.Plugin
import org.gradle.api.Project

private var isTag: Boolean = true
private var commitHash: String? = ""

object Config {
    private const val VERSION_MAJOR = 0
    private const val VERSION_MINOR = 6
    private const val VERSION_PATCH = 8

    val versionName: String
        get() {
            var versionName = "${VERSION_MAJOR}.${VERSION_MINOR}.${VERSION_PATCH}"
            if (!isTag && !commitHash.isNullOrBlank()) {
                versionName += "-$commitHash"
            }
            return versionName
        }
    val versionCode: Int get() = VERSION_MAJOR * 100000 + VERSION_MINOR * 1000 + VERSION_PATCH
}

class KnoxPatchPlugin : Plugin<Project> {
    override fun apply(project: Project) = project.applyPlugin()

    private fun Project.applyPlugin() {
        isTag = !"git name-rev --name-only --tags HEAD".runCommand(rootDir).equals("undefined")
        commitHash = "git rev-parse --short HEAD".runCommand(rootDir)
    }

    private fun String.runCommand(
        workingDir: File = File("."),
        timeoutAmount: Long = 60,
        timeoutUnit: TimeUnit = TimeUnit.SECONDS
    ): String? = runCatching {
        ProcessBuilder("\\s".toRegex().split(this))
            .directory(workingDir)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start().also { it.waitFor(timeoutAmount, timeoutUnit) }
            .inputStream.bufferedReader().readText().substringBefore("\n")
    }.onFailure { it.printStackTrace() }.getOrNull()
}
