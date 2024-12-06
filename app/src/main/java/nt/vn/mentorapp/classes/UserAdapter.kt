package nt.vn.mentorapp.classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import nt.vn.mentorapp.R
import nt.vn.mentorapp.models.User

class UserAdapter(
    private var userList: List<User>,
    private val onDeleteClick: (User) -> Unit,
    private val onUpdateClick: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.userName)
        val userEmail: TextView = itemView.findViewById(R.id.userEmail)
        val userRole: TextView = itemView.findViewById(R.id.userRole)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
        val updateButton: Button = itemView.findViewById(R.id.updateButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.userName.text = user.name
        holder.userEmail.text = user.email
        holder.userRole.text = user.role

        holder.deleteButton.setOnClickListener { onDeleteClick(user) }
        holder.updateButton.setOnClickListener { onUpdateClick(user) }
    }

    override fun getItemCount(): Int = userList.size

    // Update the displayed user list
    fun updateList(newList: List<User>) {
        userList = newList
        notifyDataSetChanged()
    }
}
