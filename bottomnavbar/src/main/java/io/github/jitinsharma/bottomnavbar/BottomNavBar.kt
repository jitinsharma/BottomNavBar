package io.github.jitinsharma.bottomnavbar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import io.github.jitinsharma.bottomnavbar.model.CustomProps
import io.github.jitinsharma.bottomnavbar.model.NavObject
import kotlinx.android.synthetic.main.bottom_nav_bar.view.*

/**
 * Created by jsharma on 22/11/17.
 */
@SuppressLint("NewApi")
class BottomNavBar(c: Context?, attrs: AttributeSet?) : ConstraintLayout(c, attrs) {
    private var weight : Float = 20.0f
    private var itemSize : Int = 0
    private var properties : CustomProps = CustomProps()

    init {
        val typedArray = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.BottomNavBar,
                0,0
        )
        properties.secondaryTextColor = typedArray.getColor(
                R.styleable.BottomNavBar_secondary_txt_color,
                Color.BLACK)
        properties.primaryTextColor = typedArray.getColor(
                R.styleable.BottomNavBar_primary_txt_color,
                Color.BLACK)
        properties.primaryButtonBg = typedArray.getColor(
                R.styleable.BottomNavBar_primary_btn_bg,
                Color.BLACK)
        properties.lineColor = typedArray.getColor(
                R.styleable.BottomNavBar_line_color,
                Color.BLACK)
        properties.stripBg = typedArray.getColor(
                R.styleable.BottomNavBar_strip_bg,
                Color.WHITE)
    }

    fun init(primaryNavObject: NavObject,
             secondaryNavObjects: List<NavObject>,
             listener : (position : Int, primaryClicked : Boolean) -> Unit) {

        val layoutInflater : LayoutInflater = context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        layoutInflater.inflate(R.layout.bottom_nav_bar, this, true)

        setSizeVariables(secondaryNavObjects)
        setItemStrip()
        setPrimaryItem(primaryNavObject)
        addNavItems(secondaryNavObjects)
        addDummyView()

        for (k in 0 until itemStrip.childCount) {
            val child = itemStrip.getChildAt(k)
            child.setOnClickListener {
                when {
                    k > itemSize/2 -> listener(k-1,false)
                    else -> listener(k,false)
                }
            }
        }

        primaryButton.setOnClickListener {
            listener(-1, true)
        }
        primaryText.setOnClickListener {
            listener(-1, true)
        }
    }

    private fun setSizeVariables(navObjects : List<NavObject>) {
        itemSize = navObjects.size
        when {
            itemSize % 2 != 0 -> {
                Toast.makeText(context, "Secondary items should be of even size",
                        Toast.LENGTH_LONG).show()
                return
            }
            itemSize > 4 -> navObjects.dropLast(4)
            itemSize == 2 -> weight = 33.3f
        }
    }

    private fun setItemStrip() {
        itemStrip.setBackgroundColor(properties.stripBg)
        aboveLollipop {
            mainLayout.elevation = 8.toPx()
        }
    }

    private fun setPrimaryItem(primaryNavObject: NavObject) {
        primaryText.text = primaryNavObject.name
        primaryButton.setImageDrawable(primaryNavObject.image)
        val gradient : GradientDrawable = primaryButton.background as GradientDrawable
        gradient.setColor(properties.primaryButtonBg)
        primaryText.setTextColor(properties.primaryTextColor)
        aboveLollipop {
            primaryButton.elevation = 8.toPx()
        }
    }

    private fun addNavItems(navObjects : List<NavObject>) {
        navObjects.forEach { navObject : NavObject ->
            val navItem = NavItem(context, null)
            val params : LinearLayout.LayoutParams = LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT)
            params.weight = weight
            navItem.layoutParams = params
            navItem.setItem(navObject.name, navObject.image, properties.secondaryTextColor)
            navItem.gravity = Gravity.CENTER_HORIZONTAL
            itemStrip.addView(navItem)
        }
    }

    private fun addDummyView() {
        val navItem = NavItem(context, null)
        val params : LinearLayout.LayoutParams = LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        params.weight = weight
        navItem.layoutParams = params
        navItem.gravity = Gravity.CENTER_HORIZONTAL
        navItem.visibility = View.INVISIBLE
        navItem.isClickable = false
        itemStrip.addView(navItem, itemSize/2)
    }

    private fun Int.toPx(): Float {
        val displayMetrics = context.resources.displayMetrics
        return Math.round(this * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).toFloat()
    }

    private inline fun aboveLollipop(body : () -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            body()
        }
    }
}