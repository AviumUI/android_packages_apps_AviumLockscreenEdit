/*
 * Copyright (C) 2025-2026 The AviumUI Project
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

package org.avium.lockscreenedit.utils

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.os.SystemProperties
import android.util.Log
import kotlinx.coroutines.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

object SystemSettingsManager {

    private const val TAG = "AVIUM_LOCKSCREEN"

    private const val PROP_ENABLED = "persist.avium.customlockscreen.enable"
    private const val PROP_TYPE = "persist.avium.customlockscreen.type"
    private const val PROP_COLOR = "persist.avium.customlockscreen.color"
    private const val PROP_HOUR_COLOR = "persist.avium.customlockscreen.hour.color"
    private const val PROP_MINUTE_COLOR = "persist.avium.customlockscreen.minute.color"
    private const val PROP_DAY_COLOR = "persist.avium.customlockscreen.day.color"
    private const val PROP_DOT_COLOR = "persist.avium.customlockscreen.dot.color"
    private const val ACTION_SETTINGS_CHANGED = "org.avium.systemui.lockscreen.SETTINGS_CHANGED"

    private const val ACTION_APPLY_THEME = "org.avium.lockscreen.APPLY_THEME"
    private const val EXTRA_ZIP_PATH = "zip_path"
    private const val EXTRA_THEME_NAME = "theme_name"

    fun setEnabled(enabled: Boolean) {
        Log.d(TAG, "Setting lockscreen enabled to: $enabled")
        SystemProperties.set(PROP_ENABLED, enabled.toString())
    }

    fun setClockType(type: Int) {
        Log.d(TAG, "Setting clock type to: $type")
        SystemProperties.set(PROP_TYPE, type.toString())
    }

    fun setClockColor(color: String) {
        Log.d(TAG, "Setting clock color to: $color")
        SystemProperties.set(PROP_COLOR, color)
    }

    fun setHourColor(color: String) {
        Log.d(TAG, "Setting hour color to: $color")
        SystemProperties.set(PROP_HOUR_COLOR, color)
    }

    fun setMinuteColor(color: String) {
        Log.d(TAG, "Setting minute color to: $color")
        SystemProperties.set(PROP_MINUTE_COLOR, color)
    }

    fun setDayColor(color: String) {
        SystemProperties.set(PROP_DAY_COLOR, color)
    }

    fun setDotColor(color: String) {
        SystemProperties.set(PROP_DOT_COLOR, color)
    }

    fun sendSettingsChangedBroadcast(context: Context) {
        Log.d(TAG, "Sending settings changed broadcast")
        val intent = Intent(ACTION_SETTINGS_CHANGED)
        context.sendBroadcast(intent)
    }

    fun applySettings(context: Context, styleId: Int) {
        Log.d(TAG, "Applying settings for styleId: $styleId")
        setEnabled(true)
        setClockType(styleId)
        setHourColor("FFFFFF")
        setMinuteColor("FFFFFF")
        setClockColor("FFFFFF")
        sendSettingsChangedBroadcast(context)
    }
    
    fun applyCustomColors(
        context: Context, 
        styleId: Int, 
        hourColor: String, 
        minuteColor: String, 
        dayColor: String,
        dotColor: String,
        isBlurEnabled: Boolean
    ) {
        setEnabled(true)
        setClockType(styleId)
        
        if (isBlurEnabled) {
            setClockColor("blur")
            setHourColor(hourColor)
            setMinuteColor(minuteColor)
        } else {
            setHourColor(hourColor)
            setMinuteColor(minuteColor)
            setClockColor("FFFFFF")
        }
        
        setDayColor(dayColor)
        setDotColor(dotColor)
        
        sendSettingsChangedBroadcast(context)
    }

    fun applyCustomZipTheme(context: Context, sourceFile: File, themeName: String) {
        
        try {
            val publicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!publicDir.exists()) {
                publicDir.mkdirs()
            }
            
            val destFile = File(publicDir, "lockscreen-theme-${System.currentTimeMillis()}.zip")
            
            FileInputStream(sourceFile).use { fis ->
                FileOutputStream(destFile).use { fos ->
                    fis.copyTo(fos)
                }
            }
            
            val intent = Intent(ACTION_APPLY_THEME)
            intent.putExtra(EXTRA_ZIP_PATH, destFile.absolutePath)
            intent.putExtra(EXTRA_THEME_NAME, themeName)
            context.sendBroadcast(intent)
            
            setEnabled(true)
            setClockType(19)
            
            GlobalScope.launch {
                delay(5000)
                sendSettingsChangedBroadcast(context)
                destFile.delete()
            }
            
            sourceFile.delete()
            
        } catch (e: Exception) {
            throw e
        }
    }
}
