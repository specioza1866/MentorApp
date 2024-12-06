package nt.vn.mentorapp.classes

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class ConnectionManager {

    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val currentUserUid: String = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    private val requestsRef: DatabaseReference = firebaseDatabase.getReference("connectionRequests")
    private val mentorsRef: DatabaseReference = firebaseDatabase.getReference("connectedMentors")

    // Send a connection request from a mentee to a mentor
    fun sendConnectionRequest(mentorUid: String) {
        if (currentUserUid.isNotEmpty()) {
            var name = ""
            FirebaseDatabase.getInstance().getReference("users").child(currentUserUid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        name = snapshot.child("name").value.toString()
                        val requestId = requestsRef.push().key ?: return
                        val request = hashMapOf(
                            "name" to name,
                            "senderUid" to currentUserUid,
                            "receiverUid" to mentorUid,
                            "status" to "pending",
                            "requestId" to requestId
                        )
                        requestsRef.child(requestId).setValue(request)
                            .addOnSuccessListener {
                                println("Request sent successfully")
                            }
                            .addOnFailureListener {
                                println("Failed to send request: ${it.message}")
                            }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })

        }
    }

    // Accept a connection request (admin or mentor)
    fun acceptConnectionRequest(requestId: String) {
        val requestRef = requestsRef.child(requestId)
        requestRef.child("status").setValue("accepted")
            .addOnSuccessListener {
                // Add the connection to the connectedMentors node
                val mentorUid = requestRef.child("receiverUid").get().result.toString()
                val menteeUid = requestRef.child("senderUid").get().result.toString()

                // Add both mentor and mentee to each other's "connectedMentors" list
                mentorsRef.child(mentorUid).child(menteeUid).setValue(true)
                mentorsRef.child(menteeUid).child(mentorUid).setValue(true)
                println("Connection accepted and updated successfully")
            }
            .addOnFailureListener {
                println("Failed to accept request: ${it.message}")
            }
    }

    // Reject a connection request (admin or mentor)
    fun rejectConnectionRequest(requestId: String) {
        requestsRef.child(requestId).removeValue()
            .addOnSuccessListener {
                println("Request rejected successfully")
            }
            .addOnFailureListener {
                println("Failed to reject request: ${it.message}")
            }
    }

    // Fetch all connected mentors for the current user
    fun fetchConnectedMentors(callback: (List<String>) -> Unit) {
        mentorsRef.child(currentUserUid).get().addOnSuccessListener { dataSnapshot ->
            val connectedMentors = mutableListOf<String>()
            for (mentor in dataSnapshot.children) {
                connectedMentors.add(mentor.key ?: "")
            }
            callback(connectedMentors)
        }.addOnFailureListener {
            println("Failed to fetch connected mentors: ${it.message}")
        }
    }
}
