package nt.vn.mentorapp.models

data class SessionProgress(
    val sessionId: String,
    val sessionTitle: String,
    val sessionDate: String, // Format: "yyyy-MM-dd HH:mm" for consistent date-time
    val sessionDescription: String,
    val progressStatus: String,
    val meetingLink: String // Online meeting link (e.g., Zoom, Google Meet, etc.)
)
