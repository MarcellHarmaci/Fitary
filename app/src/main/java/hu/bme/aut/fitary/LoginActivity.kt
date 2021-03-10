package hu.bme.aut.fitary

import android.content.Intent
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.fitary.dataSource.model.UserProfile
import hu.bme.aut.fitary.extensions.validateNonEmpty
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        firebaseAuth = FirebaseAuth.getInstance()

        btnRegister.setOnClickListener { registerClick() }
        btnLogin.setOnClickListener { loginClick() }
    }

    private fun validateForm() = etEmail.validateNonEmpty() && etPassword.validateNonEmpty()

    private fun registerClick() {
        if (!validateForm()) {
            return
        }

        showProgressDialog()

        firebaseAuth
            .createUserWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString())
            .addOnSuccessListener { result ->
                hideProgressDialog()

                val firebaseUser = result.user
                val profileChangeRequest = UserProfileChangeRequest.Builder()
                    .setDisplayName(firebaseUser?.email?.substringBefore('@'))
                    .build()
                firebaseUser?.updateProfile(profileChangeRequest)

                saveUser(firebaseUser)
                toast("Registration successful")
                loginClick()
            }
            .addOnFailureListener { exception ->
                hideProgressDialog()

                toast(exception.message)
            }
    }

    private fun loginClick() {
        if (!validateForm()) {
            return
        }

        showProgressDialog()

        firebaseAuth
            .signInWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString())
            .addOnSuccessListener {
                hideProgressDialog()

                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener { exception ->
                hideProgressDialog()

                toast(exception.localizedMessage)
            }
    }

    // TODO Remove when refactoring to use RainbowCake
    private fun saveUser(firebaseUser: FirebaseUser?) {
        val key = FirebaseDatabase
                .getInstance()
                .reference
                .child("users")
                .push().key ?: return

        if (firebaseUser == null) {
            toast("User not yet logged in")
            return
        }

        val newUser = UserProfile(
            id = firebaseUser.uid,
            mail = firebaseUser.email ?: "No mail",
            username = firebaseUser.email?.substringBefore('@') ?: "No name"
        )

        FirebaseDatabase.getInstance().reference
            .child("workouts")
            .child(key)
            .setValue(newUser)
    }
}
