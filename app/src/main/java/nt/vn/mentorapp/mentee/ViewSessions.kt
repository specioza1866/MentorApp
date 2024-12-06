package nt.vn.mentorapp.mentee

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import nt.vn.mentorapp.R
import nt.vn.mentorapp.classes.SessionsAdapter
import nt.vn.mentorapp.models.SessionProgress
import com.google.firebase.database.*

class ViewSessions : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var sessionAdapter: SessionsAdapter
    private lateinit var sessionList: MutableList<SessionProgress>
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val sessionsRef: DatabaseReference = database.getReference("sessions")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_sessions)

        // Set up the Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "My Sessions"

        // Initialize the RecyclerView
        recyclerView = findViewById(R.id.recyclerViewSessions)
        recyclerView.layoutManager = LinearLayoutManager(this)
        sessionList = mutableListOf()
        sessionAdapter = SessionsAdapter(sessionList)
        recyclerView.adapter = sessionAdapter
        val userUid=intent.extras!!.getString("uid").toString()
        // Fetch sessions from Firebase
        fetchSessions(userUid)
    }

    private fun fetchSessions(userUid: String) {
         // Fetch this dynamically (currently hardcoded)
        sessionsRef.child(userUid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                sessionList.clear() // Clear the current list
                for (sessionSnapshot in snapshot.children) {
                    val session = sessionSnapshot.getValue(SessionProgress::class.java)
                    session?.let { sessionList.add(it) }
                }
                sessionAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors
            }
        })
    }

    // Handle the back button click
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
