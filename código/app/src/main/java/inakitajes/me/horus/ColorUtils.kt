package inakitajes.me.horus

import android.content.Context
import android.graphics.Color
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat


/**
 * Created by inakitajesreiris on 25/09/2017.
 */

object ColorUtils {

    fun fetchColor(@ColorRes color: Int, context: Context): Int {
        return ContextCompat.getColor(context, color)
    }

    fun manipulateColor(color: Int, factor: Float): Int {
        val a = Color.alpha(color)
        val r = Math.round(Color.red(color) * factor)
        val g = Math.round(Color.green(color) * factor)
        val b = Math.round(Color.blue(color) * factor)
        return Color.argb(a,
                Math.min(r, 255),
                Math.min(g, 255),
                Math.min(b, 255))
    }

    fun manipulateAlphaColor(color: Int, factor: Float): Int {
        val a = Math.round(Color.alpha(color) * factor)
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)
        return Color.argb(Math.min(a,255),r,g,b)
    }

}
