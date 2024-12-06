package nt.vn.mentorapp.classes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import nt.vn.mentorapp.R
import nt.vn.mentorapp.models.Resource

class ResourcesAdapter(private val resources: List<Resource>) :
    RecyclerView.Adapter<ResourcesAdapter.ResourceViewHolder>() {

    class ResourceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.textViewTitle)
        val description: TextView = itemView.findViewById(R.id.textViewDescription)
        val link: TextView = itemView.findViewById(R.id.textViewLink)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_resource, parent, false)
        return ResourceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResourceViewHolder, position: Int) {
        val resource = resources[position]
        holder.title.text = resource.title
        holder.description.text = resource.description
        holder.link.text = resource.link ?: "No Link"
    }

    override fun getItemCount(): Int = resources.size
}
