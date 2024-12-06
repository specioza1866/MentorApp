package nt.vn.mentorapp.models

data class RequestConn(
    val senderId: String = "",    // UID of the sender
    val receiverId: String = "", // UID of the receiver
    val status: String = "pending",
    val name: String = "",
    val requestId: String=""
    // Status of the request: "pending", "accepted", or "rejected"
)
