package nt.vn.mentorapp.classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import nt.vn.mentorapp.R
import nt.vn.mentorapp.models.RequestConn

class ConnectionAdapter(
    private val requestList: MutableList<RequestConn>,
    private val onAccept: (RequestConn) -> Unit,
    private val onCancel: (RequestConn) -> Unit
) : RecyclerView.Adapter<ConnectionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val senderName: TextView = view.findViewById(R.id.requestSenderName)
        val acceptButton: Button = view.findViewById(R.id.acceptButton)
        val cancelButton: Button = view.findViewById(R.id.cancelButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_request, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val request = requestList[position]
        holder.senderName.text = request.name

        holder.acceptButton.setOnClickListener { onAccept(request) }
        holder.cancelButton.setOnClickListener { onCancel(request) }
    }

    override fun getItemCount(): Int = requestList.size
}
