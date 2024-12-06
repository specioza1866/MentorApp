package nt.vn.mentorapp.mentee

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import nt.vn.mentorapp.models.mentee
import nt.vn.mentorapp.R

class MenteeViewProfileActivity : AppCompatActivity() {

    private lateinit var menteeImageView: ImageView
    private lateinit var menteeNameTextView: TextView
    private lateinit var menteeGoalsTextView: TextView
    private lateinit var menteeSkillsTextView: TextView
    private lateinit var menteeProgressTextView: TextView
    private lateinit var contactMentorButton: Button

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentee_view_profile)

        // Initialize views
        menteeImageView = findViewById(R.id.mentee_profile_image)
        menteeNameTextView = findViewById(R.id.mentee_name)
        menteeGoalsTextView = findViewById(R.id.mentee_goals)
        menteeSkillsTextView = findViewById(R.id.mentee_skills)
        menteeProgressTextView = findViewById(R.id.mentee_progress)
        contactMentorButton = findViewById(R.id.btn_contact_mentor)

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().getReference("mentees")

        // Fetch mentee data from Firebase
        fetchMenteeProfile("menteeId") // Replace "menteeId" with the actual ID

        // Set up Contact Mentor button click action
        contactMentorButton.setOnClickListener {
            contactMentor()
        }
    }

    private fun fetchMenteeProfile(menteeId: String) {
        // Fetch mentee profile data from Firebase using mentee ID
        database.child(menteeId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve Mentee object from the snapshot
                    val mentee = dataSnapshot.getValue(mentee::class.java)
                    mentee?.let {
                        // Set profile details in UI
                        menteeNameTextView.text = it.name ?: "N/A"
                        menteeGoalsTextView.text = it.goals ?: "N/A"
                        menteeSkillsTextView.text = it.skills ?: "N/A"
                        menteeProgressTextView.text = it.progress ?: "N/A"

                        // Load profile image with Picasso if a URL is available
                        Picasso.with(this@MenteeViewProfileActivity)
                            .load(it.profileImageUrl ?: "R.drawable.baseline_account_circle_24")
                            .placeholder(R.drawable.baseline_account_circle_24)
                            .into(menteeImageView)
                    }
                } else {
                    Toast.makeText(this@MenteeViewProfileActivity, "Mentee data not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@MenteeViewProfileActivity, "Failed to load data: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun contactMentor() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf("mentor@example.com")) // Replace with mentor's email
            putExtra(Intent.EXTRA_SUBJECT, "Mentorship Request")
            putExtra(Intent.EXTRA_TEXT, "Hello, I would like to connect with you regarding mentorship.")
        }
        startActivity(Intent.createChooser(intent, "Send Email"))
    }
}
