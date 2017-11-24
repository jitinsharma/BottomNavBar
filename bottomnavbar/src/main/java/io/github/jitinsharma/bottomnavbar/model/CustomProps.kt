package io.github.jitinsharma.bottomnavbar.model

import android.graphics.Color

/**
 * Created by jsharma on 22/11/17.
 */

data class CustomProps(
        var secondaryTextColor : Int = Color.BLACK,
        var primaryTextColor : Int = Color.BLACK,
        var primaryButtonBg : Int = Color.BLACK,
        var lineColor : Int = Color.BLACK,
        var stripBg : Int = Color.WHITE,
        var secondaryItemClickedColor : Int = -1
)