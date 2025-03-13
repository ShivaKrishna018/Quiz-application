package com.kronos.mainLayouts

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
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
import com.kronos.quizgame.databinding.ActivityQuizBinding
import kotlin.random.Random

class QuizActivity : AppCompatActivity() {
    lateinit var quizBinding: ActivityQuizBinding
    val firebase: FirebaseDatabase = FirebaseDatabase.getInstance()
    val reference: DatabaseReference = firebase.getReference("question")

    var question = ""
    var answerA = ""
    var answerB = ""
    var answerC = ""
    var answerD = ""
    var correctAnswer =""
    var questionNumber = 0
    var questionCount = 0
    var user = ""
    var correctValue = 0
    var wrongValue = 0
    lateinit var timer : CountDownTimer
    var totalTime = 60000L
    var timeLeft = totalTime
    var timeContinue = false

    val auth = FirebaseAuth.getInstance()
    val userInfo = auth.currentUser
    val scoreRef = firebase.reference

    val questions = HashSet<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quizBinding = ActivityQuizBinding.inflate(layoutInflater)
        val view = quizBinding.root
        setContentView(view)

        do {
            val number = Random.nextInt(1, 11)
            questions.add(number)
        }while (questions.size < 5)

        gameLogic()

        quizBinding.finishBtn.setOnClickListener {
            scoreStore()
        }

        quizBinding.nextBtn.setOnClickListener {
            reset()
            gameLogic()
        }
        quizBinding.answerA.setOnClickListener {
            pause()
            user = "a"
            if (correctAnswer == user) {

                quizBinding.answerA.setBackgroundColor(Color.GREEN)
                correctValue++
                quizBinding.correctValue.text = correctValue.toString()

            }else{

                quizBinding.answerA.setBackgroundColor(Color.RED)
                wrongValue++
                quizBinding.wrongValue.text = wrongValue.toString()
                findCorrectAnswer()

            }

            disableClickable()

        }
        quizBinding.answerB.setOnClickListener {
            pause()
            user = "b"
            if (correctAnswer == user) {

                quizBinding.answerB.setBackgroundColor(Color.GREEN)
                correctValue++
                quizBinding.correctValue.text = correctValue.toString()

            }else{

                quizBinding.answerB.setBackgroundColor(Color.RED)
                wrongValue++
                quizBinding.wrongValue.text = wrongValue.toString()
                findCorrectAnswer()

            }

            disableClickable()
        }
        quizBinding.answerC.setOnClickListener {
            pause()
            user = "c"
            if (correctAnswer == user) {

                quizBinding.answerC.setBackgroundColor(Color.GREEN)
                correctValue++
                quizBinding.correctValue.text = correctValue.toString()

            }else{

                quizBinding.answerC.setBackgroundColor(Color.RED)
                wrongValue++
                quizBinding.wrongValue.text = wrongValue.toString()
                findCorrectAnswer()

            }

            disableClickable()
        }
        quizBinding.answerD.setOnClickListener {
            pause()
            user = "d"
            if (correctAnswer == user) {

                quizBinding.answerD.setBackgroundColor(Color.GREEN)
                correctValue++
                quizBinding.correctValue.text = correctValue.toString()

            }else{

                quizBinding.answerD.setBackgroundColor(Color.RED)
                wrongValue++
                quizBinding.wrongValue.text = wrongValue.toString()
                findCorrectAnswer()

            }

            disableClickable()
        }



    }

    private fun gameLogic() {
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                time()

                restore()

                questionCount = snapshot.childrenCount.toInt()
                
                if (questionNumber < questions.size) {
                    
                question = snapshot.child(questions.elementAt(questionNumber).toString()).child("q").value.toString()
                answerA = snapshot.child(questions.elementAt(questionNumber).toString()).child("a").value.toString()
                answerB = snapshot.child(questions.elementAt(questionNumber).toString()).child("b").value.toString()
                answerC = snapshot.child(questions.elementAt(questionNumber).toString()).child("c").value.toString()
                answerD = snapshot.child(questions.elementAt(questionNumber).toString()).child("d").value.toString()
                correctAnswer = snapshot.child(questions.elementAt(questionNumber).toString()).child("answer").value.toString()
                
                quizBinding.questions.setText(question)
                quizBinding.answerA.setText(answerA)
                quizBinding.answerB.setText(answerB)
                quizBinding.answerC.setText(answerC)
                quizBinding.answerD.setText(answerD)

                    quizBinding.quizTopLayout.visibility = View.VISIBLE
                    quizBinding.quizMainLayout.visibility = View.VISIBLE
                    quizBinding.quizButtonLayout.visibility =View.VISIBLE
                    quizBinding.progressBar5.visibility = View.INVISIBLE
                }else {
                    val dialogMessage = AlertDialog.Builder(this@QuizActivity)
                        .setTitle("Quiz Game")
                        .setMessage("Congratulation!!\n you have answered all the question")
                        .setCancelable(false)
                        .setPositiveButton("Result", DialogInterface.OnClickListener { dialog, which ->
                            scoreStore()
                        })
                        .setNegativeButton("Play Again", DialogInterface.OnClickListener { dialog, which ->
                            val intent = Intent(this@QuizActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        })

                        dialogMessage.create().show()


                }
                questionNumber++

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "error.message", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun disableClickable() {
        quizBinding.answerA.isClickable = false
        quizBinding.answerB.isClickable = false
        quizBinding.answerC.isClickable = false
        quizBinding.answerD.isClickable = false
    }

    private fun findCorrectAnswer() {
        when(correctAnswer) {
            "a" -> quizBinding.answerA.setBackgroundColor(Color.GREEN)
            "b" -> quizBinding.answerB.setBackgroundColor(Color.GREEN)
            "c" -> quizBinding.answerC.setBackgroundColor(Color.GREEN)
            "d" -> quizBinding.answerD.setBackgroundColor(Color.GREEN)
        }
    }


    private fun restore() {

        quizBinding.answerA.isClickable = true
        quizBinding.answerB.isClickable = true
        quizBinding.answerC.isClickable = true
        quizBinding.answerD.isClickable = true

        quizBinding.answerA.setBackgroundColor(Color.WHITE)
        quizBinding.answerB.setBackgroundColor(Color.WHITE)
        quizBinding.answerC.setBackgroundColor(Color.WHITE)
        quizBinding.answerD.setBackgroundColor(Color.WHITE)

    }

    private fun time() {
        timer = object : CountDownTimer(timeLeft, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
                updateTimeText()
            }

            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                disableClickable()
                reset()
                updateTimeText()
                quizBinding.questions.text = "Sorry! time's up"

                timeContinue = false
            }
        }.start()
        timeContinue = true
    }

    private fun pause() {
        timer.cancel()
        timeContinue = false
    }

    private fun reset() {
       pause()
        timeLeft = totalTime
        updateTimeText()

    }

    private fun updateTimeText() {
        val remainingTimer: Int = (timeLeft/1000).toInt()
        quizBinding.timeValue.text = remainingTimer.toString()
    }

    private fun scoreStore() {
        userInfo?.let {
            val userUID = it.uid
            scoreRef.child("Score").child(userUID).child("correct").setValue(correctValue)
            scoreRef.child("Score").child(userUID).child("wrong").setValue(wrongValue)

                .addOnSuccessListener {
                    Toast.makeText(applicationContext, "see your results", Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext, ResultActivity::class.java)
                    startActivity(intent)
                    finish()
                }
        }

    }


}