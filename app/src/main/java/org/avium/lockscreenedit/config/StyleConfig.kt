package org.avium.lockscreenedit.config

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import org.avium.lockscreenedit.R

object StyleConfig {
    
    data class LockscreenStyle(
        val id: Int,
        @StringRes val nameResId: Int,
        @DrawableRes val previewResId: Int,
        @StringRes val descriptionResId: Int = 0 
    )
    
    val availableStyles = listOf(
        LockscreenStyle(
            id = 1,
            nameResId = R.string.lockscreen_style_title,
            previewResId = R.drawable.preview_style_1,
            descriptionResId = R.string.style_humanistic_description
        ),
        LockscreenStyle(
            id = 2,
            nameResId = R.string.lockscreen_style_magazine,
            previewResId = R.drawable.preview_style_2,
            descriptionResId = R.string.style_magazine_description
        )
        // NEW Like this
        // LockscreenStyle(
        //     id = 3,
        //     nameResId = R.string.your_new_style_name,
        //     previewResId = R.drawable.your_preview_image,
        //     descriptionResId = R.string.your_style_description
        // )
    )
    

    fun getStyleById(id: Int): LockscreenStyle? {
        return availableStyles.find { it.id == id }
    }
    

    fun getStyleCount(): Int = availableStyles.size
    

    fun isValidStyleId(id: Int): Boolean {
        return availableStyles.any { it.id == id }
    }

    fun getStyleDescription(context: Context, id: Int): String {
        val style = getStyleById(id)
        return if (style != null && style.descriptionResId != 0) {
            context.getString(style.descriptionResId)
        } else {
            ""
        }
    }
}