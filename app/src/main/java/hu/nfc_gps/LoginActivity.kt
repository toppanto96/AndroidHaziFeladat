package hu.nfc_gps

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    companion object {
        const val EMAIL = "email"
        const val PASSWORD = "password"
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
                                startActivity(Intent(this@LoginActivity, MainMenuActivity::class.java)
                                    .apply {
                                        putExtra(EMAIL, email)
                                        putExtra(PASSWORD, password)
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
