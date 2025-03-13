package com.kronos.mainLayouts

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kronos.quizgame.MainActivity
import com.kronos.quizgame.R
import com.kronos.quizgame.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    lateinit var resultBinding: ActivityResultBinding

    val firebase : FirebaseDatabase = FirebaseDatabase.getInstance()
    val mainReference : DatabaseReference = firebase.getReference("Score")

    val auth : FirebaseAuth = FirebaseAuth.getInstance()
    val userInfo = auth.currentUser

    var correctResult = ""
    var wrongResult = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultBinding = ActivityResultBinding.inflate(layoutInflater)
        val view = resultBinding.root
        setContentView(view)

        mainReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               userInfo?.let {

                   val userUid = it.uid

                   correctResult = snapshot.child(userUid).child("correct").value.toString()
                   wrongResult = snapshot.child(userUid).child("wrong").value.toString()

                   resultBinding.correctValueResult.text = correctResult
                   resultBinding.wrongValueResult.text = wrongResult


               }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })



        resultBinding.playAgain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        resultBinding.exit.setOnClickListener {
            finish()
        }


    }
}