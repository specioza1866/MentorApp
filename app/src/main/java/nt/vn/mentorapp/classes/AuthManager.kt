package nt.vn.mentorapp.classes

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import nt.vn.mentorapp.AdminControlPanel
import nt.vn.mentorapp.HomeActivity
import nt.vn.mentorapp.MenteesDashboardActivity
import nt.vn.mentorapp.models.Mentor
import nt.vn.mentorapp.models.User

class AuthManager {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().getReference("users")

    fun registerUser(
        context: Context,
        username: String,
        email: String,
        password: String,
        role: String,
        callback: (Boolean) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = task.result.user?.uid ?: return@addOnCompleteListener
                    val user = User(userId, email, username, role, true)
                    if(role=="MENTOR")
                    {
                        val mentor= Mentor(name = username, MentorId = userId)
                       FirebaseDatabase.getInstance().getReference("mentors").child(userId).setValue(mentor)
                    }
                    else if(role=="MENTEE")
                    {
                        val mentee=Mentor(name = username, MentorId = userId)
                    }
                    database.child(userId).setValue(user)
                        .addOnSuccessListener { callback(true) }
                        .addOnFailureListener { callback(false) }
                } else {
                    Toast.makeText(context, task.exception?.message ?: "Registration failed", Toast.LENGTH_LONG).show()
                    callback(false)
                }
            }
    }

    fun loginUser(
        context: Context,
        email: String,
        password: String,
        callback: (Boolean) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = task.result.user?.uid ?: return@addOnCompleteListener
                    database.child(userId).child("role")
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val role = snapshot.value as? String
                                when (role) {
                                    "MENTEE" -> {
                                        context.startActivity(Intent(context, MenteesDashboardActivity::class.java))
                                        Toast.makeText(context, "You are a mentee", Toast.LENGTH_SHORT).show()
                                    }
                                    "MENTOR" -> {
                                        context.startActivity(Intent(context, HomeActivity::class.java))
                                        Toast.makeText(context, "You are a mentor", Toast.LENGTH_SHORT).show()
                                    }
                                    "admin" -> {
                                        context.startActivity(Intent(context, AdminControlPanel::class.java))
                                        Toast.makeText(context, "You are an admin", Toast.LENGTH_SHORT).show()
                                    }
                                    else -> {
                                        Toast.makeText(context, "Role not defined", Toast.LENGTH_LONG).show()
                                    }
                                }
                                callback(true)
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_LONG).show()
                                callback(false)
                            }
                        })
                } else {
                    Toast.makeText(context, task.exception?.message ?: "Login failed", Toast.LENGTH_SHORT).show()
                    callback(false)
                }
            }
    }

    fun logoutUser() {
        auth.signOut()
    }
}
