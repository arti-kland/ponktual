package com.example.punktual

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var providers : List<AuthUI.IdpConfig>
    val MY_REQUEST_CODE: Int = 7117


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // init
        providers = Arrays.asList<AuthUI.IdpConfig>(
            AuthUI.IdpConfig.EmailBuilder().build(), // Email Login
            // AuthUI.IdpConfig.FacebookBuilder().build(), // FaceBook Login
            AuthUI.IdpConfig.GoogleBuilder().build(), // Google Login
            AuthUI.IdpConfig.PhoneBuilder().build() // Phone Login
        )

        showSignInOptions()

        //event
        btn_sign_out.setOnClickListener {
            // signout
            AuthUI.getInstance().signOut(this@MainActivity)
                .addOnCanceledListener {
                    btn_sign_out.isEnabled = false
                    showSignInOptions()
                }
                .addOnFailureListener {
                    e-> Toast.makeText(this@MainActivity,e.message,Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_REQUEST_CODE) {

            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {

                val user = FirebaseAuth.getInstance().currentUser // get current user
                Toast.makeText(this,"Bienvenue " + user!!.email,Toast.LENGTH_SHORT).show()

                btn_sign_out.isEnabled = true
            }
            else{

                Toast.makeText(this,"Une erreur c'est produite ! " + response!!.error!!.message,Toast.LENGTH_SHORT).show()

            }
        }
    }
    private fun showSignInOptions() {
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(R.style.myTheme)

            .build(),MY_REQUEST_CODE)
    }
}
