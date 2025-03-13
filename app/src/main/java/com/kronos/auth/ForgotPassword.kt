package com.kronos.auth

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.kronos.quizgame.R
import com.kronos.quizgame.databinding.ActivityForgotPasswordBinding

class ForgotPassword : AppCompatActivity() {
    lateinit var forgotBinding : ActivityForgotPasswordBinding
    val fireAuth : FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forgotBinding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        val view = forgotBinding.root
        setContentView(view)

        forgotBinding.sendInstructions.setOnClickListener {

            val resetPassword = forgotBinding.resetPassword.text.toString().trim()
            fireAuth.sendPasswordResetEmail(resetPassword).addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    Toast.makeText(this, "check your email", Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(this, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                }

            }

        }

    }
}