package nt.vn.mentorapp.models

data class Mentor(
    val name: String = "",
    val title: String = "",
    val bio: String = "",
    val profileImageUrl: String = "R.drawable.ic_profile",
    val rating: Double = 0.0,
    val availability: String = "",
    val expertise:String ="",
    val experience:String="",
    val MentorId: String="",
    val skills: List<Skill> = listOf() // List of skills
) {

}
