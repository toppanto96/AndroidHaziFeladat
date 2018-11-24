package hu.nfc_gps

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main_menu.*

class MainMenuActivity : BaseActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    companion object {
        private var calledPersistentEnabled = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        auth = FirebaseAuth.getInstance()

        database = FirebaseDatabase.getInstance()

        if (!calledPersistentEnabled) {
            database.setPersistenceEnabled(true)
            calledPersistentEnabled = true
        }

        tabs_view_pager.adapter = TabsPagerAdapter(supportFragmentManager)

        tabs.setupWithViewPager(tabs_view_pager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.log_out_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.item_logout -> {
                auth.signOut()
                startActivity(Intent(this@MainMenuActivity, LoginActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
