package io.github.jitinsharma.bottomnavbar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
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
    private val properties : CustomProps = CustomProps()
    private var coloredItemIndex : Int = -1

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
        properties.secondaryItemClickedColor = typedArray.getColor(
                R.styleable.BottomNavBar_secondary_item_clicked,
                -1)
        typedArray.recycle()
        inflate()
    }

    private fun inflate() {
        val layoutInflater : LayoutInflater = context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        layoutInflater.inflate(R.layout.bottom_nav_bar, this, true)
    }

    fun init(primaryNavObject: NavObject,
             secondaryNavObjects: List<NavObject>,
             listener : (position : Int, primaryClicked : Boolean) -> Unit) {
        setSizeVariables(secondaryNavObjects)
        setItemStrip()
        setUpPrimaryItem(primaryNavObject)
        setUpSecondaryItems(secondaryNavObjects)
        addDummyView()
        setUpPrimaryItemListener(listener)
        setUpSecondaryItemListener(listener)
    }

    private fun setUpSecondaryItemListener(listener: (position: Int, primaryClicked: Boolean) -> Unit) {
        for (k in 0 until itemStrip.childCount) {
            val child = itemStrip.getChildAt(k)
            child.setOnClickListener {
                if (properties.secondaryItemClickedColor != -1) {
                    resetColoredItem(k)
                    setColorToCurrentItem(child)
                }
                when {
                    k > itemSize/2 -> listener(k-1,false)
                    else -> listener(k,false)
                }
            }
        }
    }

    private fun setUpPrimaryItemListener(listener: (position: Int, primaryClicked: Boolean) -> Unit) {
        primaryButton.setOnClickListener {
            listener(-1, true)
            resetColoredItem(-2)
            val animation = AnimationUtils.loadAnimation(context, R.anim.bounce)
            val bounceInterpolator = BounceInterpolator(0.2, 20.0)
            animation.interpolator = bounceInterpolator
            primaryButton.startAnimation(animation)
        }
        primaryText.setOnClickListener {
            listener(-1, true)
        }
    }

    private fun setColorToCurrentItem(child : View) {
        coloredItemIndex = itemStrip.indexOfChild(child)
        val layout = child as LinearLayout
        val image = layout.findViewById<ImageView>(R.id.navItemImage)
        val text = layout.findViewById<TextView>(R.id.navItemText)
        text.setTextColor(properties.secondaryItemClickedColor)
        image.setColorFilter(properties.secondaryItemClickedColor, PorterDuff.Mode.SRC_ATOP)
    }

    private fun resetColoredItem(currentItemIndex : Int) {
        if (currentItemIndex != coloredItemIndex && coloredItemIndex != -1) {
            val layout = itemStrip.getChildAt(coloredItemIndex) as LinearLayout
            val image = layout.findViewById<ImageView>(R.id.navItemImage)
            val text = layout.findViewById<TextView>(R.id.navItemText)
            text.setTextColor(properties.secondaryTextColor)
            image.setColorFilter(properties.secondaryTextColor, PorterDuff.Mode.SRC_ATOP)
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
        lollipopAndAbove {
            mainLayout.elevation = 8.toPx()
        }
    }

    private fun setUpPrimaryItem(primaryNavObject: NavObject) {
        primaryText.text = primaryNavObject.name
        primaryButton.setImageDrawable(primaryNavObject.image)
        val gradient : GradientDrawable = primaryButton.background as GradientDrawable
        gradient.setColor(properties.primaryButtonBg)
        primaryText.setTextColor(properties.primaryTextColor)
        lollipopAndAbove {
            primaryButton.elevation = 8.toPx()
        }
    }

    private fun setUpSecondaryItems(navObjects : List<NavObject>) {
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

    private inline fun lollipopAndAbove(body : () -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            body()
        }
    }
}