package hu.nfc_gps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        button_sign_in.setOnClickListener {
            startActivity(Intent(this@LoginActivity, MainMenuActivity::class.java))
        }
    }
}
