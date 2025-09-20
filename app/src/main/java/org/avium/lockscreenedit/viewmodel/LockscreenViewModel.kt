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
        isBlurEnabled: Boolean
    ) {
        val hourColorHex = String.format("%08X", hourColor.toArgb()).takeLast(6)
        val minuteColorHex = String.format("%08X", minuteColor.toArgb()).takeLast(6)
        
        SystemSettingsManager.applyCustomColors(
            context, 
            styleId, 
            hourColorHex, 
            minuteColorHex, 
            isBlurEnabled
        )
    }
}