package nt.vn.mentorapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import androidx.cardview.widget.CardView
import nt.vn.mentorapp.mentor.ReviewProgressActivity
import nt.vn.mentorapp.mentor.ViewReportsActivity

class AdminControlPanel : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_control_panel)
        val toolbar=findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        val cardViewRequests=findViewById<CardView>(R.id.cardAddSkills)
        val cardViewMentors: CardView = findViewById(R.id.cardViewMentors)
        val cardViewMentees: CardView = findViewById(R.id.cardViewMentees)
        val cardReviewProgress: CardView = findViewById(R.id.cardReviewProgress)
        val cardViewReports: CardView = findViewById(R.id.cardViewReports)
        cardViewRequests.setOnClickListener {
            val pb= ProgressDialog(this)
            pb.show()
            val intent = Intent(this, AcceptConnectionRequests::class.java)
            startActivity(intent)
            pb.dismiss()
        }
        cardViewMentors.setOnClickListener {
            // Navigate to Add Skills Activity
            /*val intent = Intent(this, AddSkillsActivity::class.java)
            startActivity(intent)*/
            val pb= ProgressDialog(this)
            pb.show()
            val intent = Intent(this, CommunityManagerDashboard::class.java)
            intent.putExtra("userType","MENTOR")
            startActivity(intent)
        }

        cardViewMentees.setOnClickListener {
            // Navigate to View Mentees Activity
            val pb= ProgressDialog(this)
            pb.show()
            val intent = Intent(this, CommunityManagerDashboard::class.java)
            intent.putExtra("userType","MENTEE")
            startActivity(intent)

        }

        cardReviewProgress.setOnClickListener {
            // Navigate to Review Progress Activity
            val intent = Intent(this, ReviewProgressActivity::class.java)
            startActivity(intent)
        }

        cardViewReports.setOnClickListener {
            // Navigate to View Reports Activity
             val intent = Intent(this, ViewReportsActivity::class.java)
             startActivity(intent)
        }
    }
}