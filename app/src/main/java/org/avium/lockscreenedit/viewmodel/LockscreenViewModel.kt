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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import org.avium.lockscreenedit.config.StyleConfig
import org.avium.lockscreenedit.utils.SystemSettingsManager

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
}