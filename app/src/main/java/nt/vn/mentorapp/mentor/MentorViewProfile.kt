package nt.vn.mentorapp.mentor

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import nt.vn.mentorapp.models.Mentor
import nt.vn.mentorapp.R
import nt.vn.mentorapp.models.Skill

class MentorViewProfile : AppCompatActivity() {

    private lateinit var mentorImageView: ImageView
    private lateinit var mentorNameTextView: TextView
    private lateinit var mentorTitleTextView: TextView
    private lateinit var mentorExperienceTextView: TextView
    private lateinit var mentorSkillsTextView: TextView
    private lateinit var mentorAvailabilityTextView: TextView
    private lateinit var mentorBioTextView: TextView
    private lateinit var contactButton: Button
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentor_view_profile)

        // Initialize UI components
        mentorImageView = findViewById(R.id.mentor_profile_image)
        mentorNameTextView = findViewById(R.id.mentor_name)
        mentorTitleTextView = findViewById(R.id.mentor_title)
        mentorExperienceTextView = findViewById(R.id.mentor_experience)
        mentorSkillsTextView = findViewById(R.id.mentor_skills)
        mentorAvailabilityTextView = findViewById(R.id.mentor_availability)
        mentorBioTextView = findViewById(R.id.mentor_bio)
        contactButton = findViewById(R.id.btn_contact_mentor)

        database = FirebaseDatabase.getInstance().getReference("mentors")

        // Fetch mentor data and display
        fetchMentorData(FirebaseAuth.getInstance().currentUser!!.uid)

        contactButton.text = "Update Profile"
        contactButton.setOnClickListener {
            showUpdateProfileDialog()
            Toast.makeText(this, "Edit Mentor Profile...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchMentorData(mentorId: String) {
        database.child(mentorId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val mentor = dataSnapshot.getValue(Mentor::class.java)
                    mentor?.let {
                        mentorNameTextView.text = it.name ?: "N/A"
                        mentorTitleTextView.text = it.title ?: "N/A"
                        mentorExperienceTextView.text = "Experience: ${it.rating ?: "N/A"}"
                        mentorAvailabilityTextView.text = it.availability ?: "N/A"
                        mentorBioTextView.text = it.bio ?: "N/A"

                        // Display skills as a formatted string
                        val skillsText = it.skills.joinToString(separator = "\n") { skill ->
                            "${skill.name} - Expertise: ${skill.expertise}/10, Experience: ${skill.experience} years"
                        }
                        mentorSkillsTextView.text = if (skillsText.isNotEmpty()) skillsText else "No skills listed"

                        Picasso.with(this@MentorViewProfile)
                            .load(it.profileImageUrl).placeholder(R.drawable.ic_profile)
                            .into(mentorImageView)
                    }
                } else {
                    Toast.makeText(this@MentorViewProfile, "Mentor data not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@MentorViewProfile, "Failed to load data: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showUpdateProfileDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_update_profile, null)
        val builder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Update Profile")

        val alertDialog = builder.show()

        val nameEditText = dialogView.findViewById<EditText>(R.id.edit_mentor_name)
        val titleEditText = dialogView.findViewById<EditText>(R.id.edit_mentor_title)
        val experienceEditText = dialogView.findViewById<EditText>(R.id.edit_mentor_experience)
        val bioEditText = dialogView.findViewById<EditText>(R.id.edit_mentor_bio)
        val updateButton = dialogView.findViewById<Button>(R.id.btn_update_profile)

        database.child(FirebaseAuth.getInstance().currentUser!!.uid).get().addOnSuccessListener { dataSnapshot ->
            nameEditText.setText(dataSnapshot.child("name").getValue(String::class.java))
            titleEditText.setText(dataSnapshot.child("title").getValue(String::class.java))
            experienceEditText.setText(dataSnapshot.child("experience").getValue(String::class.java))
            bioEditText.setText(dataSnapshot.child("bio").getValue(String::class.java))
        }

        updateButton.setOnClickListener {
            val updatedMentor = Mentor(
                name = nameEditText.text.toString(),
                title = titleEditText.text.toString(),
                bio = bioEditText.text.toString()
            )

            database.child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(updatedMentor)
                .addOnSuccessListener {
                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                    alertDialog.dismiss()
                    fetchMentorData(FirebaseAuth.getInstance().currentUser!!.uid)
                }
                .addOnFailureListener { error ->
                    Toast.makeText(this, "Failed to update profile: ${error.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
