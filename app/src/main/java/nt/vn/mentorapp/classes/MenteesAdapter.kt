package nt.vn.mentorapp.classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import nt.vn.mentorapp.R
import nt.vn.mentorapp.models.RequestConn

class MenteesAdapter(private val mentorsList: List<RequestConn>) :
    RecyclerView.Adapter<MenteesAdapter.MentorViewHolder>() {

    class MentorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mentorName: TextView = itemView.findViewById(R.id.textViewMentorName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MentorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mentee, parent, false)
        return MentorViewHolder(view)
    }

    override fun onBindViewHolder(holder: MentorViewHolder, position: Int) {
        val mentor = mentorsList[position]
        holder.mentorName.text = mentor.name
    }

    override fun getItemCount(): Int = mentorsList.size
}

