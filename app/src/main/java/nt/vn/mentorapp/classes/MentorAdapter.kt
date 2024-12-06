package nt.vn.mentorapp.classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import nt.vn.mentorapp.R
import nt.vn.mentorapp.models.Mentor

class MentorAdapter(private var mentorList: List<Mentor>) : RecyclerView.Adapter<MentorAdapter.MentorViewHolder>() {

    class MentorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        val skillTextView: TextView = view.findViewById(R.id.skillTextView)
        val experienceTextView: TextView = view.findViewById(R.id.experienceTextView)
        val ratingTextView: TextView = view.findViewById(R.id.ratingTextView)
        val requestButton:Button=view.findViewById(R.id.connectionRequestButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MentorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mentor_item, parent, false)
        return MentorViewHolder(view)
    }

    override fun onBindViewHolder(holder: MentorViewHolder, position: Int) {
        val mentor = mentorList[position]

        // Set mentor's name
        holder.nameTextView.text = mentor.name ?: "N/A"

        // Format and set skills
        val skillsText = mentor.skills.joinToString(separator = "\n") { skill ->
            "${skill.name} - Expertise: ${skill.expertise}/10, Experience: ${skill.experience} years"
        }
        holder.skillTextView.text = if (skillsText.isNotEmpty()) skillsText else "No skills listed"

        // Set experience
        holder.experienceTextView.text = "Experience: ${mentor.experience ?: "N/A"} years"

        // Set rating
        holder.ratingTextView.text = "Rating: ${mentor.rating}"
        holder.requestButton.setOnClickListener {
            ConnectionManager().sendConnectionRequest(mentor.MentorId)
        }
    }

    override fun getItemCount(): Int {
        return mentorList.size
    }

    fun updateData(newMentorList: List<Mentor>) {
        mentorList = newMentorList
        notifyDataSetChanged()
    }
}
