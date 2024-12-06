package nt.vn.mentorapp.classes

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import nt.vn.mentorapp.R
import nt.vn.mentorapp.mentee.ViewSessions
import nt.vn.mentorapp.models.RequestConn

class MentorsAdapter(private val mentorsList: List<RequestConn>) :
    RecyclerView.Adapter<MentorsAdapter.MentorViewHolder>() {

    // ViewHolder class to hold references to the views in each item layout
    class MentorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mentorName: TextView = itemView.findViewById(R.id.textViewMentorName)
        val viewSessions: Button = itemView.findViewById(R.id.buttonViewSessions)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MentorViewHolder {
        // Inflate the layout for individual items in the RecyclerView
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mentor, parent, false)
        return MentorViewHolder(view)
    }

    override fun onBindViewHolder(holder: MentorViewHolder, position: Int) {
        // Bind the data to the views in the ViewHolder
        val mentor = mentorsList[position]

        FirebaseDatabase.getInstance().getReference("mentors").child(mentor.receiverId).addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                holder.mentorName.text = snapshot.child("name").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        // Set click listener for the "View Sessions" button
        holder.viewSessions.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ViewSessions::class.java)
            intent.putExtra("uid", mentor.receiverId) // Pass the mentor's UID to the next activity
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        // Return the size of the dataset
        return mentorsList.size
    }
}
