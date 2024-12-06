package nt.vn.mentorapp.mentor

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import nt.vn.mentorapp.R
import nt.vn.mentorapp.classes.SessionsAdapter
import nt.vn.mentorapp.classes.SessionManager
import nt.vn.mentorapp.models.SessionProgress
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.UUID

class SessionsPlan : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var sessionsAdapter: SessionsAdapter
    private val sessionsList = mutableListOf<SessionProgress>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sessions_plan)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        // Enable the back button in the toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Session Plan" // Set the title of the top bar

        sessionManager = SessionManager()
        sessionsAdapter = SessionsAdapter(sessionsList)

        val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerViewSessions)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = sessionsAdapter

        findViewById<Button>(R.id.btnAddSession).setOnClickListener {
            showAddSessionDialog()
        }

        fetchSessions()
    }

    private fun fetchSessions() {
        val courseId = "YOUR_COURSE_ID" // Replace with dynamic courseId
        sessionManager.fetchCourseDetails(courseId) { courseDetails ->
            val sessions = courseDetails?.get("sessions") as? Map<String, Map<String, String>>
            sessions?.forEach { (sessionId, sessionData) ->
                val session = SessionProgress(
                    sessionId = sessionId,
                    sessionTitle = sessionData["sessionTitle"] ?: "N/A",
                    sessionDate = sessionData["sessionDate"] ?: "N/A",
                    sessionDescription = sessionData["sessionDescription"] ?: "N/A",
                    progressStatus = sessionData["progressStatus"] ?: "Not Started",
                    meetingLink = sessionData["meetingLink"] ?: "N/A"
                )
                sessionsList.add(session)
            }
            sessionsAdapter.notifyDataSetChanged()
        }
    }

    private fun showAddSessionDialog() {
        // Show a dialog to input session details
        MaterialAlertDialogBuilder(this)
            .setTitle("Add Session")
            .setView(R.layout.dialog_add_session) // Layout for input fields
            .setPositiveButton("Add") { dialog, _ ->
                // Fetch data from the dialog and add the session
                val courseId = "YOUR_COURSE_ID" // Replace with dynamic courseId
                val sessionId = UUID.randomUUID().toString()
                val sessionTitle = "New Session Title" // Replace with input data
                val sessionDate = "2024-11-25 14:00" // Replace with input data
                val sessionDescription = "Session Description" // Replace with input data
                val meetingLink = "https://zoom.us/meeting/12345" // Replace with input data

                sessionManager.scheduleSession(
                    courseId,
                    sessionId,
                    sessionTitle,
                    sessionDate,
                    sessionDescription,
                    meetingLink
                )
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
