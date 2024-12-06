package nt.vn.mentorapp.classes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import nt.vn.mentorapp.R
import nt.vn.mentorapp.models.Skill

class SkillsAdapter(private var skillsList: List<Skill>) : RecyclerView.Adapter<SkillsAdapter.SkillsViewHolder>() {

    class SkillsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_skill, parent, false)
        return SkillsViewHolder(view)
    }

    override fun onBindViewHolder(holder: SkillsViewHolder, position: Int) {
        val skill = skillsList[position]
        holder.nameTextView.text = skill.name

        holder.itemView.setOnClickListener {
            showSkillDialog(holder.itemView.context, skill.name)
        }
    }

    private fun showSkillDialog(context: Context, skillName: String) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_skill_levels, null)
        val skillLevelSpinner: Spinner = dialogView.findViewById(R.id.filterExpertise)

        AlertDialog.Builder(context)
            .setTitle("Select Skill Level for $skillName")
            .setView(dialogView)
            .setPositiveButton("Save") { dialog, _ ->
                val selectedSkillLevel = skillLevelSpinner.selectedItem.toString()
                saveSkillLevelToFirebase(context, skillName, selectedSkillLevel)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun saveSkillLevelToFirebase(context: Context, skillName: String, skillLevel: String) {
        val database = FirebaseDatabase.getInstance().reference
        val skillRef = database.child("skills")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child(skillName)

        skillRef.setValue(skillLevel)
            .addOnSuccessListener {
                Toast.makeText(context, "Successfully added.", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, exception.message.toString(), Toast.LENGTH_LONG).show()
            }
    }

    override fun getItemCount(): Int = skillsList.size

    fun updateData(newSkillsList: List<Skill>) {
        skillsList = newSkillsList
        notifyDataSetChanged()
    }
}
