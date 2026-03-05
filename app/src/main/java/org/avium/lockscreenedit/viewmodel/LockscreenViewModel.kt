/*
 * Copyright (C) 2025 The AviumUI Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.avium.lockscreenedit.viewmodel

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import org.avium.lockscreenedit.R
import org.avium.lockscreenedit.config.StyleConfig
import org.avium.lockscreenedit.utils.SystemSettingsManager
import java.io.File
import java.io.FileOutputStream

class LockscreenViewModel : ViewModel() {

    val clockStyles = StyleConfig.availableStyles

    fun onApply(context: Context, styleId: Int) {
        SystemSettingsManager.applySettings(context, styleId)
    }
    
    fun onApplyWithColors(
        context: Context, 
        styleId: Int, 
        hourColor: Color, 
        minuteColor: Color, 
        dayColor: Color,
        dotColor: Color,
        isBlurEnabled: Boolean
    ) {
        val hourColorHex = String.format("%08X", hourColor.toArgb()).takeLast(6)
        val minuteColorHex = String.format("%08X", minuteColor.toArgb()).takeLast(6)
        val dayColorHex = String.format("%08X", dayColor.toArgb()).takeLast(6)
        val dotColorHex = String.format("%08X", dotColor.toArgb()).takeLast(6)
        
        SystemSettingsManager.applyCustomColors(
            context, 
            styleId, 
            hourColorHex, 
            minuteColorHex,
            dayColorHex,
            dotColorHex,
            isBlurEnabled
        )
    }

    fun importAndApplyCustomZip(context: Context, uri: Uri) {
        try {
            val themeName = getFileName(context, uri)?.let { name ->
                if (name.endsWith(".zip", ignoreCase = true)) {
                    name.substring(0, name.length - 4)
                } else {
                    name
                }
            } ?: "custom_theme_${System.currentTimeMillis()}"

            val cacheDir = context.cacheDir
            val zipFile = File(cacheDir, "$themeName.zip")

            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(zipFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            SystemSettingsManager.applyCustomZipTheme(context, zipFile, themeName)
            
            Toast.makeText(
                context,
                context.getString(R.string.custom_zip_apply_success),
                Toast.LENGTH_SHORT
            ).show()

        } catch (e: Exception) {
            Toast.makeText(
                context,
                context.getString(R.string.custom_zip_apply_failed) + ": ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun getFileName(context: Context, uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val idx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (idx >= 0) {
                        result = cursor.getString(idx)
                    }
                }
            }
        }
        if (result == null) {
            result = uri.lastPathSegment
        }
        return result
    }
}
