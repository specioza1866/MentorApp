package nt.vn.mentorapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import nt.vn.mentorapp.R
import nt.vn.mentorapp.classes.MentorAdapter
import nt.vn.mentorapp.models.Mentor
import nt.vn.mentorapp.models.User

class ViewMentorsActivity : AppCompatActivity() {
    private var mentorList = mutableListOf<Mentor>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_mentors)
        val mentorsRecyclerView = findViewById<RecyclerView>(R.id.mentorsRecyclerView)
        mentorsRecyclerView.layoutManager = LinearLayoutManager(this)
        val mentorAdapter=MentorAdapter(mentorList)
        mentorsRecyclerView.adapter = mentorAdapter
        loadUsersFromFirebase(mentorAdapter)
    }

    private fun loadUsersFromFirebase(mentorAdapter: MentorAdapter) {
        val database = FirebaseDatabase.getInstance().getReference("mentors")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mentorList.clear()
                for (userSnapshot in snapshot.children) {
                    val id = userSnapshot.child("mentorId").value.toString()
                    val name = userSnapshot.child("name").value.toString()
                    val bio=userSnapshot.child("bio").value.toString()
                    val experience=userSnapshot.child("experience").value.toString()
                    val title=userSnapshot.child("title").value.toString()
                    val mentor=Mentor(name = name,title=title,experience=experience, bio = bio, MentorId = id)
                    mentorList.add(mentor)
                    mentorAdapter.notifyDataSetChanged()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun getSampleMentors(): List<Mentor> {
        return listOf(
            Mentor("John Doe", "Android Developer"),
            Mentor("Jane Smith", "Data Scientist"),
            Mentor("Michael Johnson", "UI/UX Designer")
        )
    }
}
