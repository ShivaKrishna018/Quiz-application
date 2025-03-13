package com.kronos.quizgame

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.kronos.mainLayouts.QuizActivity
import com.kronos.quizgame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var mainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

        mainBinding.startGame.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            startActivity(intent)
            finish()
        }

        mainBinding.logOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build()

            val googleSignInClient = GoogleSignIn.getClient(this, gso)
            googleSignInClient.signOut().addOnCompleteListener {task ->
                
                if(task.isSuccessful) {
                    Toast.makeText(this, "signOut successful", Toast.LENGTH_SHORT).show()
                }
            }
            finish()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }

    }
}