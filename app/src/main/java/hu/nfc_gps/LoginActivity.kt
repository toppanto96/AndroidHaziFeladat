package hu.nfc_gps

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        button_sign_in.setOnClickListener {

            val email = text_input_email.editText?.text.toString()
            val password = text_input_password.editText?.text.toString()

            when {
                email.isEmpty() -> text_input_email.error = "Érvénytelen email cím."
                password.isEmpty() -> text_input_password.error = "Érvénytelen jelszó"
                else -> {
                    text_input_email.isErrorEnabled = false
                    text_input_password.isErrorEnabled = false

                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Belépés sikeres", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "Belépés sikertelen", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
            //startActivity(Intent(this@LoginActivity, MainMenuActivity::class.java))
        }
    }
}
