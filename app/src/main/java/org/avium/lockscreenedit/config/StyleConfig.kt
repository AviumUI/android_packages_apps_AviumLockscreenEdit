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
        ),
        LockscreenStyle(
            id = 3,
            nameResId = R.string.lockscreen_style_vertical,
            previewResId = R.drawable.preview_style_3,
            descriptionResId = R.string.style_vertical_description
        ),
        LockscreenStyle(
            id = 4,
            nameResId = R.string.lockscreen_style_text,
            previewResId = R.drawable.preview_style_4,
            descriptionResId = R.string.style_text_description
        ),
        LockscreenStyle(
            id = 5,
            nameResId = R.string.lockscreen_style_gallery,
            previewResId = R.drawable.preview_style_5,
            descriptionResId = R.string.style_gallery_description
        ),
        LockscreenStyle(
            id = 6,
            nameResId = R.string.lockscreen_style_longer,
            previewResId = R.drawable.preview_style_6,
            descriptionResId = R.string.style_longer_description
        ),
        LockscreenStyle(
            id = 8,
            nameResId = R.string.lockscreen_style_classic,
            previewResId = R.drawable.preview_style_8,
            descriptionResId = R.string.style_classic_description
        ),
        LockscreenStyle(
            id = 9,
            nameResId = R.string.lockscreen_style_rolling,
            previewResId = R.drawable.preview_style_9,
            descriptionResId = R.string.style_rolling_description
        ),
        LockscreenStyle(
            id = 10,
            nameResId = R.string.lockscreen_style_anytime,
            previewResId = R.drawable.preview_style_10,
            descriptionResId = R.string.style_anytime_description
        )
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