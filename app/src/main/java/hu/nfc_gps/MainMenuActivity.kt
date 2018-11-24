package hu.nfc_gps

import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main_menu.*

class MainMenuActivity : BaseActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        auth = FirebaseAuth.getInstance()

        tabs_view_pager.adapter = TabsPagerAdapter(supportFragmentManager)

        tabs.setupWithViewPager(tabs_view_pager)
    }
}
