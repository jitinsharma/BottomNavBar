package io.github.jitinsharma.bottombarandroid

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import io.github.jitinsharma.bottomnavbar.model.NavObject
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomBar.init(NavObject(
                name = "Flights",
                image = ContextCompat.getDrawable(applicationContext, R.drawable.ic_flight_black_24dp)!!
        ), arrayListOf(
                NavObject(
                        name = "Hotel",
                        image = ContextCompat.getDrawable(applicationContext, R.drawable.ic_hotel_black_24dp)!!),
                NavObject(
                        name = "Chat",
                        image = ContextCompat.getDrawable(applicationContext, R.drawable.ic_forum_black_24dp)!!),
                NavObject(
                        name = "Profile",
                        image = ContextCompat.getDrawable(applicationContext, R.drawable.ic_account_circle_black_24dp)!!),
                NavObject(
                        name = "Settings",
                        image = ContextCompat.getDrawable(applicationContext, R.drawable.ic_settings_black_24dp)!!)
        )) { position, primaryClicked ->
            when (position) {
                0 -> showFragment("Hotel")
                1 -> showFragment("Chat")
                2 -> showFragment("Profile")
                3 -> showFragment("Settings")
                else -> if (primaryClicked) showFragment("Flights")
            }
        }
    }

    @SuppressLint("PrivateResource")
    private fun showFragment(displayString: String) {
        val fragment: Fragment = SomeFragment.newInstance(displayString)
        val transaction = supportFragmentManager.beginTransaction()
        transaction
                .replace(R.id.container, fragment)
                .commit()
    }
}