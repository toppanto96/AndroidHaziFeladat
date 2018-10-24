package hu.nfc_gps

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main_menu.*

class MainMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        tabs_view_pager.adapter = TabsPagerAdapter(supportFragmentManager)

        tabs.setupWithViewPager(tabs_view_pager)
    }
}
