package com.kronos.quizgame

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.kronos.auth.ForgotPassword
import com.kronos.quizgame.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var loginBinding: ActivityLoginBinding
    val fireAuth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val view = loginBinding.root
        setContentView(view)

        activityResultLaunch()

        val googleAppearance = loginBinding.googleSign.getChildAt(0) as TextView
        googleAppearance.text = "Continue with Google"
        googleAppearance.setTextColor(Color.BLACK)
        googleAppearance.textSize = 15F

        loginBinding.googleSign.setOnClickListener {

            googleSignInClient()

        }

        loginBinding.signUpBtn.setOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }
        loginBinding.loginBtn.setOnClickListener {
            val getEmail = loginBinding.getEmail.text.toString().trim()
            val getPassword = loginBinding.getPassword.text.toString().trim()
            getLogin(getEmail, getPassword)
        }

        loginBinding.forgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)

        }

    }

    private fun getLogin(userEmailGet: String, userPasswordGet: String) {
        fireAuth.signInWithEmailAndPassword(userEmailGet, userPasswordGet).addOnCompleteListener {task ->

            if (task.isSuccessful) {
                Toast.makeText(applicationContext, "welcome to quiz show", Toast.LENGTH_SHORT).show()
                finish()
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(applicationContext, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }

    }


    override fun onStart() {
        super.onStart()
        val user = fireAuth.currentUser

        if (user != null) {
            Toast.makeText(applicationContext, "welcome to quiz show", Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()

        }
    }

    private fun googleSignInClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1063256632450-2m3lg5qmlf3uo2mivfk832bofmmbf1h8.apps.googleusercontent.com")
            .requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        signIn()
    }

    private fun signIn() {
        val googleSignIntent : Intent = googleSignInClient.signInIntent

        activityResultLauncher.launch(googleSignIntent)

    }

    fun activityResultLaunch() {
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback { result ->

                val resultCode = result.resultCode
                val resultData = result.data

                if(resultCode == RESULT_OK && resultData != null) {

                    val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(resultData)

                    firebaseSignInWithGoogle(task)

                }
            })
    }

    fun firebaseSignInWithGoogle(task: Task<GoogleSignInAccount>) {

        try {
            val account : GoogleSignInAccount = task.getResult(ApiException::class.java)
            Toast.makeText(applicationContext, "Welcome to quiz show", Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()

            firebaseGoogleAccount(account)

        }catch (e:Exception) {
            Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

   fun firebaseGoogleAccount(account: GoogleSignInAccount) {

        val authCredential = GoogleAuthProvider.getCredential(account.idToken, null)
       fireAuth.signInWithCredential(authCredential).addOnCompleteListener {task ->
           if(task.isSuccessful) {

           }else{

           }
       }

    }


}