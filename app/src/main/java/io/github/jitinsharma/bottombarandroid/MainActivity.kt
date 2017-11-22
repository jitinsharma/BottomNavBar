package io.github.jitinsharma.bottombarandroid

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.widget.FrameLayout
import io.github.jitinsharma.bottomnavbar.BottomNavBar
import io.github.jitinsharma.bottomnavbar.model.NavObject
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomBar = findViewById<BottomNavBar>(R.id.bottomBar)
        bottomBar.init(NavObject(
                name = "Search",
                image = ContextCompat.getDrawable(this, R.drawable.ic_flight_black_24dp)
        ), arrayListOf(
                NavObject(
                        name = "Hotel",
                        image = this.getDrawable(R.drawable.ic_hotel_black_24dp)),
                NavObject(
                        name = "Chat",
                        image = this.getDrawable(R.drawable.ic_forum_black_24dp)),
                NavObject(
                        name = "Profile",
                        image = this.getDrawable(R.drawable.ic_account_circle_black_24dp)),
                NavObject(
                        name = "Settings",
                        image = this.getDrawable(R.drawable.ic_settings_black_24dp))
        )) { position, primaryClicked ->
            makeTopSnackBar("Index: $position Primary: $primaryClicked")
        }
    }

    private fun makeTopSnackBar(message : String) {
        val snack = Snackbar.make(parentView, message,
                Snackbar.LENGTH_SHORT)
        val view = snack.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        view.layoutParams = params
        snack.show()
    }
}
