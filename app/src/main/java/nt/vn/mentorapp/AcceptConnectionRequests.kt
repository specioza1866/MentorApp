package nt.vn.mentorapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import nt.vn.mentorapp.classes.ConnectionAdapter
import nt.vn.mentorapp.models.RequestConn

class AcceptConnectionRequests : AppCompatActivity() {
    private var requestList = mutableListOf<RequestConn>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_requests)

        val mentorsRecyclerView = findViewById<RecyclerView>(R.id.mentorsRecyclerView)
        mentorsRecyclerView.layoutManager = LinearLayoutManager(this)

        val connectionsAdapter = ConnectionAdapter(
            requestList,
            onAccept = { request -> handleRequest(request, "accepted") }
        ) { request -> handleRequest(request, "rejected") }
        mentorsRecyclerView.adapter = connectionsAdapter

        loadRequestsFromFirebase(connectionsAdapter)
    }

    private fun loadRequestsFromFirebase(adapter:ConnectionAdapter) {
        val database = FirebaseDatabase.getInstance().getReference("connectionRequests")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                requestList.clear()
                for (requestSnapshot in snapshot.children) {
                    val id = requestSnapshot.child("requestId").value.toString()
                    val name = requestSnapshot.child("name").value.toString()
                    val receiverUid=requestSnapshot.child("receiverUid").value.toString()
                    val senderUid=requestSnapshot.child("senderUid").value.toString()
                    val status = requestSnapshot.child("status").value.toString()
                    if (status == "pending") {
                    val request=RequestConn(requestId=id,senderId=senderUid, receiverId = receiverUid,status=status,name=name)
                        requestList.add(request)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun handleRequest(request: RequestConn, newStatus: String) {
        val database = FirebaseDatabase.getInstance().getReference("connectionRequests")
        database.child(request.requestId).child("status").setValue(newStatus)
            .addOnSuccessListener {
                // Optionally, show success feedback to admin
            }
            .addOnFailureListener {
                // Optionally, show error feedback to admin
            }
    }
}
