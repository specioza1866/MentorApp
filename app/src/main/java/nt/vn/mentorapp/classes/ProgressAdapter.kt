package nt.vn.mentorapp.classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import nt.vn.mentorapp.R
import nt.vn.mentorapp.models.SessionProgress

class ProgressAdapter(private val progressList: List<SessionProgress>) :
    RecyclerView.Adapter<ProgressAdapter.ProgressViewHolder>() {

    class ProgressViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sessionIdTextView: TextView = view.findViewById(R.id.sessionIdTextView)
        val progressStatusTextView: TextView = view.findViewById(R.id.progressStatusTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgressViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_progress, parent, false)
        return ProgressViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProgressViewHolder, position: Int) {
        val progress = progressList[position]
        holder.sessionIdTextView.text = "Session: ${progress.sessionId}"
        holder.progressStatusTextView.text = "Status: ${progress.progressStatus}"
    }

    override fun getItemCount(): Int = progressList.size
}

