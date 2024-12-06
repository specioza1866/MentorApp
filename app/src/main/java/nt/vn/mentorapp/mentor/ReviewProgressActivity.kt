package nt.vn.mentorapp.mentor

import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import nt.vn.mentorapp.R
import nt.vn.mentorapp.models.SessionProgress
import androidx.appcompat.app.AppCompatActivity
import nt.vn.mentorapp.classes.ProgressAdapter

class ReviewProgressActivity : AppCompatActivity() {

    private lateinit var progressRecyclerView: RecyclerView
    private lateinit var database: DatabaseReference
    private val progressList = mutableListOf<SessionProgress>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_progress)

        progressRecyclerView = findViewById(R.id.progressRecyclerView)
        progressRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = ProgressAdapter(progressList)
        progressRecyclerView.adapter = adapter

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().getReference("progressTracking")

        // Fetch progress data
        fetchProgress(adapter)
    }

    private fun fetchProgress(adapter: ProgressAdapter) {
        val courseId = intent.getStringExtra("courseId") ?: ""
        val menteeUid = intent.getStringExtra("menteeUid") ?: ""

        if (courseId.isEmpty() || menteeUid.isEmpty()) {
            Toast.makeText(this, "Invalid data received", Toast.LENGTH_SHORT).show()
            return
        }

        database.child(menteeUid).child(courseId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    progressList.clear()  // Clear the list before populating it again
                    for (sessionSnapshot in snapshot.children) {
                        val sessionId = sessionSnapshot.key ?: continue

                        // Fetch all session details from the session node
                        val date = sessionSnapshot.child("date").getValue(String::class.java) ?: "N/A"
                        val time = sessionSnapshot.child("time").getValue(String::class.java) ?: "N/A"
                        val topic = sessionSnapshot.child("topic").getValue(String::class.java) ?: "N/A"
                        val onlineLink = sessionSnapshot.child("onlineLink").getValue(String::class.java) ?: "N/A"
                        val progressStatus = sessionSnapshot.child("progressStatus").getValue(String::class.java) ?: "Not Started"

                        // Create a SessionProgress object
                        val sessionProgress = SessionProgress(
                            sessionId,
                            progressStatus,
                            date,
                            time,
                            topic,
                            onlineLink
                        )

                        progressList.add(sessionProgress)  // Add the session progress to the list
                    }
                    adapter.notifyDataSetChanged()  // Notify the adapter that data has been updated
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ReviewProgressActivity, "Failed to fetch progress: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })

    }
}
