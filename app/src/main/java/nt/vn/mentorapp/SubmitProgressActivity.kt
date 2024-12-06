package nt.vn.mentorapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import nt.vn.mentorapp.R

class SubmitProgressActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_progress)

        val submitButton = findViewById<Button>(R.id.submitProgressButton)
        val progressDetails = findViewById<EditText>(R.id.progressDetails)

        submitButton.setOnClickListener {
            val progress = progressDetails.text.toString()
            if (progress.isNotEmpty()) {
                Toast.makeText(this, "Progress submitted!", Toast.LENGTH_SHORT).show()
                // Logic to submit progress can be added here
            } else {
                Toast.makeText(this, "Please enter your progress details", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
