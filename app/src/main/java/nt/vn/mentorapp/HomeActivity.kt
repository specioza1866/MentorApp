package nt.vn.mentorapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import nt.vn.mentorapp.mentee.*
import nt.vn.mentorapp.mentor.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize profile-related views
        val profileCard = findViewById<CardView>(R.id.cardProfile)
        val viewProfileButton = findViewById<Button>(R.id.viewProfileButton)
        val viewSessionsButton = findViewById<Button>(R.id.viewSessionsButton)
        val viewResourcesButton = findViewById<Button>(R.id.viewResourcesButton)
        // Set onClick listeners for navigating to profile or other activities
        viewProfileButton.setOnClickListener {
            // Open the Profile activity
            val intent = Intent(this, MenteeViewProfileActivity::class.java)
            startActivity(intent)
        }
        viewResourcesButton.setOnClickListener {
            // Open the Profile activity
            val intent = Intent(this, AddResources::class.java)
            startActivity(intent)
        }
        viewSessionsButton.setOnClickListener {
            // Open the Profile activity
            val intent = Intent(this, SessionsPlan::class.java)
            startActivity(intent)
        }
        profileCard.setOnClickListener {
            // Navigate to profile as well
            val intent = Intent(this, MenteeViewProfileActivity::class.java)
            startActivity(intent)
        }

        // Card for adding skills
        findViewById<CardView>(R.id.cardAddSkills).setOnClickListener {
            val intent = Intent(this, AddSkillsActivity::class.java)
            startActivity(intent)
        }

        // Card for viewing mentees
        findViewById<CardView>(R.id.cardViewMentees).setOnClickListener {
            val intent = Intent(this, MyMenteesActivity::class.java)
            startActivity(intent)
        }

        // Card for reviewing progress
        findViewById<CardView>(R.id.cardReviewProgress).setOnClickListener {
            val intent = Intent(this, ReviewProgressActivity::class.java)
            startActivity(intent)
        }

        // Card for viewing reports
        findViewById<CardView>(R.id.cardViewReports).setOnClickListener {
            val intent = Intent(this, ViewReportsActivity::class.java)
            startActivity(intent)
        }
    }
}
