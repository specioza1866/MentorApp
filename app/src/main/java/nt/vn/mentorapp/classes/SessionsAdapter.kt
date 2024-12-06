package nt.vn.mentorapp.classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import nt.vn.mentorapp.R
import nt.vn.mentorapp.models.SessionProgress
import android.widget.TextView
import android.widget.Button

class SessionsAdapter(private val sessions: List<SessionProgress>) :
    RecyclerView.Adapter<SessionsAdapter.SessionViewHolder>() {

    class SessionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sessionTitle: TextView = view.findViewById(R.id.textViewSessionTitle)
        val sessionDate: TextView = view.findViewById(R.id.textViewSessionDate)
        val sessionProgress: TextView = view.findViewById(R.id.textViewProgressStatus)
        val viewProgressButton: Button = view.findViewById(R.id.btnViewProgress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_session, parent, false)
        return SessionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        val session = sessions[position]
        holder.sessionTitle.text = session.sessionTitle
        holder.sessionDate.text = "Date: ${session.sessionDate}"
        holder.sessionProgress.text = "Progress: ${session.progressStatus}"

        holder.viewProgressButton.setOnClickListener {
            // Logic to view session progress details
        }
    }

    override fun getItemCount(): Int = sessions.size
}

