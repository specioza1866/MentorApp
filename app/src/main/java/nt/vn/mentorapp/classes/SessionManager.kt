package nt.vn.mentorapp.classes

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import nt.vn.mentorapp.models.SessionProgress
import java.util.*

class SessionManager {

    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val currentUserUid: String = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    private val coursesRef: DatabaseReference = firebaseDatabase.getReference("courses")
    private val progressRef: DatabaseReference = firebaseDatabase.getReference("progressTracking")

    // Create a course outline
    fun createCourseOutline(courseId: String, courseName: String, outline: Map<String, String>) {
        if (currentUserUid.isNotEmpty()) {
            val course = hashMapOf(
                "mentorUid" to currentUserUid,
                "courseName" to courseName,
                "outline" to outline
            )
            coursesRef.child(courseId).setValue(course)
                .addOnSuccessListener {
                    println("Course outline created successfully")
                }
                .addOnFailureListener {
                    println("Failed to create course outline: ${it.message}")
                }
        }
    }

    // Schedule a session for the course with detailed information
    fun scheduleSession(
        courseId: String,
        sessionId: String,
        sessionTitle: String,
        sessionDate: String,
        sessionDescription: String,
        meetingLink: String
    ) {
        val session = SessionProgress(
            sessionId = sessionId,
            sessionTitle = sessionTitle,
            sessionDate = sessionDate,
            sessionDescription = sessionDescription,
            progressStatus = "Not Started",
            meetingLink = meetingLink
        )

        coursesRef.child(courseId).child("sessions").child(sessionId).setValue(session)
            .addOnSuccessListener {
                println("Session scheduled successfully")
            }
            .addOnFailureListener {
                println("Failed to schedule session: ${it.message}")
            }
    }

    // Track progress of mentee for a specific session
    fun trackProgress(
        menteeUid: String,
        courseId: String,
        sessionId: String,
        progressStatus: String
    ) {
        val progress = hashMapOf(
            "sessionId" to sessionId,
            "progressStatus" to progressStatus
        )

        progressRef.child(menteeUid).child(courseId).child(sessionId).setValue(progress)
            .addOnSuccessListener {
                println("Progress updated successfully")
            }
            .addOnFailureListener {
                println("Failed to update progress: ${it.message}")
            }
    }

    // Fetch course details including sessions and mentee progress
    fun fetchCourseDetails(courseId: String, callback: (courseDetails: Map<String, Any>?) -> Unit) {
        coursesRef.child(courseId).get().addOnSuccessListener { courseSnapshot ->
            if (courseSnapshot.exists()) {
                val courseDetails = courseSnapshot.value as Map<String, Any>
                callback(courseDetails)
            } else {
                println("Course not found")
                callback(null)
            }
        }.addOnFailureListener {
            println("Failed to fetch course details: ${it.message}")
            callback(null)
        }
    }

    // Fetch progress of a mentee for a specific course
    fun fetchMenteeProgress(
        menteeUid: String,
        courseId: String,
        callback: (progress: List<SessionProgress>) -> Unit
    ) {
        progressRef.child(menteeUid).child(courseId).get().addOnSuccessListener { progressSnapshot ->
            val progressList = mutableListOf<SessionProgress>()
            for (session in progressSnapshot.children) {
                val sessionId = session.child("sessionId").value as? String ?: ""
                val progressStatus = session.child("progressStatus").value as? String ?: ""

                val sessionProgress = SessionProgress(
                    sessionId = sessionId,
                    sessionTitle = "", // Add fetching logic for title if needed
                    sessionDate = "", // Add fetching logic for date if needed
                    sessionDescription = "", // Add fetching logic for description if needed
                    progressStatus = progressStatus,
                    meetingLink = "" // Add fetching logic for link if needed
                )
                progressList.add(sessionProgress)
            }
            callback(progressList)
        }.addOnFailureListener {
            println("Failed to fetch mentee progress: ${it.message}")
            callback(emptyList())
        }
    }
}
