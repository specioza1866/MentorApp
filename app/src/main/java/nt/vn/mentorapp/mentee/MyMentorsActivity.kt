package nt.vn.mentorapp.mentee

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import nt.vn.mentorapp.R
import nt.vn.mentorapp.ViewMentorsActivity
import nt.vn.mentorapp.classes.MentorsAdapter
import nt.vn.mentorapp.models.RequestConn

class MyMentorsActivity : AppCompatActivity() {
    private lateinit var recyclerViewMentors: RecyclerView
    private lateinit var mentorsAdapter: MentorsAdapter
    private val mentorsList = mutableListOf<RequestConn>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_mentors)

        val addMentorBtn = findViewById<ImageButton>(R.id.addMentor)
        recyclerViewMentors = findViewById(R.id.mentorsRecyclerView)

        // Set up RecyclerView
        recyclerViewMentors.layoutManager = LinearLayoutManager(this)
        mentorsAdapter = MentorsAdapter(mentorsList)
        recyclerViewMentors.adapter = mentorsAdapter

        // Navigate to ViewMentorsActivity
        addMentorBtn.setOnClickListener {
            startActivity(Intent(this@MyMentorsActivity, ViewMentorsActivity::class.java))
        }

        fetchMentors()
    }

    private fun fetchMentors() {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserUid == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val database = FirebaseDatabase.getInstance().reference

        database.child("connectionRequests")
            .orderByChild("senderUid")
            .equalTo(currentUserUid) // Fetch requests for the current user
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    mentorsList.clear() // Clear the list to prevent duplication
                    for (data in snapshot.children) {
                        val name = data.child("name").getValue(String::class.java)
                        val receiverId = data.child("receiverUid").getValue(String::class.java)
                        val senderId = data.child("senderUid").getValue(String::class.java)
                        val status = data.child("status").getValue(String::class.java)
                        val requestId = data.child("requestId").getValue(String::class.java)

                        if (!name.isNullOrEmpty() && !receiverId.isNullOrEmpty() &&
                            !senderId.isNullOrEmpty() && !status.isNullOrEmpty() &&
                            !requestId.isNullOrEmpty()
                        ) {
                            val connection = RequestConn(name=name, receiverId = receiverId, senderId = senderId, status = status, requestId = requestId)
                            if(connection.status=="accepted") {
                                mentorsList.add(connection)
                            }
                        }
                    }
                    mentorsAdapter.notifyDataSetChanged() // Notify the adapter of data changes
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@MyMentorsActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
