package nt.vn.mentorapp.mentee

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import nt.vn.mentorapp.R
import nt.vn.mentorapp.classes.ResourcesAdapter
import nt.vn.mentorapp.models.Resource

class ViewResourcesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var resourcesAdapter: ResourcesAdapter
    private val resourcesList = mutableListOf<Resource>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_resources)

        recyclerView = findViewById(R.id.recyclerViewResources)
        recyclerView.layoutManager = LinearLayoutManager(this)
        resourcesAdapter = ResourcesAdapter(resourcesList)
        recyclerView.adapter = resourcesAdapter

        // Get the mentor's ID (for filtering)
        val mentorId = FirebaseAuth.getInstance().currentUser!!.uid // Replace with actual mentor ID, you can fetch from auth or intent

        fetchResources(mentorId)
    }

    private fun fetchResources(mentorId: String) {
        val database = FirebaseDatabase.getInstance().reference
        val resourcesRef = database.child("resources")

        resourcesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                resourcesList.clear() // Clear the list to avoid duplicate data
                for (data in snapshot.children) {
                    val id=data.child("id").value.toString()
                        val title=data.child("title").value.toString()
                        val description=data.child("description").value.toString()
                        val link=data.child("link").value.toString()
                        val mentorId=data.child("mentorId").value.toString()
                    val resource = Resource(title=title, description = description, id = id, link = link, mentorId = mentorId)
                    if (resource != null) {
                        resourcesList.add(resource)
                    }
                }
                resourcesAdapter.notifyDataSetChanged() // Notify the adapter that the data has changed
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ViewResourcesActivity, "Error fetching resources: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
