package nt.vn.mentorconnect.classes

import com.google.firebase.database.FirebaseDatabase
import nt.vn.mentorapp.models.Skill

class SkillManager {
    private val database = FirebaseDatabase.getInstance().getReference("skills")

    fun addSkill(skill: Skill, callback: (Boolean) -> Unit) {
        val skillId = database.push().key
        skillId?.let {
            database.child(it).setValue(skill)
                .addOnCompleteListener { task ->
                    callback(task.isSuccessful)
                }
        }
    }

    fun searchSkills(query: String, callback: (List<Skill>) -> Unit) {
        database.orderByChild("name").startAt(query).endAt("$query\uf8ff")
            .get().addOnSuccessListener { dataSnapshot ->
                val skillList = mutableListOf<Skill>()
                dataSnapshot.children.forEach { child ->
                    val skill = child.getValue(Skill::class.java)
                    skill?.let { skillList.add(it) }
                }
                callback(skillList)
            }
    }
}
