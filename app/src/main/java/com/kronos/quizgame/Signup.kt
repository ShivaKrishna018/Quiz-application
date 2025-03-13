package com.kronos.quizgame

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import com.google.firebase.auth.FirebaseAuth
import com.kronos.quizgame.databinding.ActivitySignupBinding

class Signup : AppCompatActivity() {
    lateinit var signBinding: ActivitySignupBinding
    val fireAuth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signBinding = ActivitySignupBinding.inflate(layoutInflater)
        val view = signBinding.root
        setContentView(view)


        signBinding.signUpMain.setOnClickListener {
            val emailInfo = signBinding.signUpEmail.text.toString().trim()
            val passwordInfo = signBinding.signPassword.text.toString().trim()
            getInfo(emailInfo, passwordInfo)

        }
    }

    private fun getInfo(userEmail: String, userPassword: String) {

        signBinding.progressBar.visibility = View.VISIBLE
        signBinding.signUpMain.isClickable = false

        fireAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener { task ->

            if (task.isSuccessful){
                Toast.makeText(applicationContext, "signUp successful", Toast.LENGTH_SHORT).show()
                finish()
                signBinding.signUpMain.isClickable = true
                signBinding.progressBar.visibility = View.INVISIBLE
            }else{
                Toast.makeText(applicationContext, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()

            }
        }
    }
}