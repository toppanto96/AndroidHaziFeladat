package hu.nfc_gps

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var sharedPref: SharedPreferences

    companion object {
        const val EMAIL = "email"
        const val PASSWORD = "password"
        const val MY_SP = "mySp"
        const val CURRENT_USER_ID = "currentUserId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        button_sign_in.setOnClickListener {

            val email = text_input_email.editText?.text.toString()
            val password = text_input_password.editText?.text.toString()

            when {
                email.isEmpty() -> text_input_email.error = getString(R.string.invalidEmailText)
                password.isEmpty() -> text_input_password.error = getString(R.string.invalidPasswordText)
                else -> {
                    text_input_email.isErrorEnabled = false
                    text_input_password.isErrorEnabled = false

                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {

                                sharedPref = this.getSharedPreferences(MY_SP, Context.MODE_PRIVATE)
                                sharedPref.edit().apply {
                                    putString(CURRENT_USER_ID, auth.currentUser?.uid)
                                    apply()
                                }
                                //TODO le kell ellenőrizni, hogy be van-e már jelentkezve valaki(Firebase és SharedPreferences-t is le kell ellenőrizni)
                                //TODO a kijelentkezést is meg kell csinálni(Firebase és SharedPreferences törlése)

                                startActivity(Intent(this@LoginActivity, MainMenuActivity::class.java)
                                    .apply {
                                        putExtra(EMAIL, email)
                                        putExtra(PASSWORD, password)
                                        //TODO lehet az adatokat a shared preferences-be kellene átadni, nem így
                                        //TODO nem is biztos, hogy nyilván kell tartani az email-t, jelszót, elég csak a user ID
                                    })
                            } else {
                                text_input_email.editText?.text = null
                                text_input_password.editText?.text = null

                                Toast.makeText(this, getString(R.string.failedLoginText), Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }
    }
}
