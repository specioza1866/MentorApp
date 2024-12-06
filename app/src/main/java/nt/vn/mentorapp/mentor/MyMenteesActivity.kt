package nt.vn.mentorapp.mentor

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import nt.vn.mentorapp.R
import nt.vn.mentorapp.classes.MenteesAdapter
import nt.vn.mentorapp.models.RequestConn

class MyMenteesActivity : AppCompatActivity() {

    private lateinit var recyclerViewMentees: RecyclerView
    private lateinit var menteesAdapter: MenteesAdapter
    private val menteesList = mutableListOf<RequestConn>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_mentees)

        recyclerViewMentees = findViewById(R.id.recyclerViewMentees)

        // Use the same adapter as mentors
        menteesAdapter = MenteesAdapter(menteesList)
        recyclerViewMentees.adapter = menteesAdapter

        fetchMentees()
    }

    private fun fetchMentees() {
        val currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid // Replace with the actual logged-in mentor UID
        val database = FirebaseDatabase.getInstance().reference

        database.child("connectionRequests")
            .orderByChild("receiverUid")
            .equalTo(currentUserUid) // Fetch requests for the current user
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    menteesList.clear() // Clear the list to prevent duplication
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
                                menteesList.add(connection)
                            }
                        }
                    }
                    menteesAdapter.notifyDataSetChanged() // Notify the adapter of data changes
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@MyMenteesActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
