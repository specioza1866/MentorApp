package nt.vn.mentorapp

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import nt.vn.mentorapp.mentee.MenteeViewProfileActivity
import nt.vn.mentorapp.mentee.MyMentorsActivity
import nt.vn.mentorapp.mentee.ViewResourcesActivity

class MenteesDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentees_dashboard)

        // Set up click listeners for each card
        findViewById<View>(R.id.cardViewMentors).setOnClickListener {
            navigateToViewMentors()
        }

        findViewById<View>(R.id.cardSubmitProgress).setOnClickListener {
            navigateToSubmitProgress()
        }

        findViewById<View>(R.id.cardViewFeedback).setOnClickListener {
            navigateToViewFeedback()
        }

        findViewById<View>(R.id.cardViewResources).setOnClickListener {
            navigateToViewResources()
        }

        findViewById<View>(R.id.viewProfileButton).setOnClickListener {
            navigateToViewProfile()
        }
    }

    private fun navigateToViewMentors() {
        val pd=ProgressDialog(this)
        pd.setTitle("Opening...")
        pd.show()
        val intent = Intent(this, MyMentorsActivity::class.java)
        startActivity(intent)
        pd.dismiss()
    }

    private fun navigateToSubmitProgress() {
        val intent = Intent(this, SubmitProgressActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToViewFeedback() {
        val intent = Intent(this, ViewFeedbackActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToViewResources() {
        val intent = Intent(this, ViewResourcesActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToViewProfile() {
        val intent = Intent(this, MenteeViewProfileActivity::class.java)
        startActivity(intent)
    }
}
