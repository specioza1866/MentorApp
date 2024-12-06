package nt.vn.mentorapp.mentor

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import nt.vn.mentorapp.R
import nt.vn.mentorapp.classes.ResourcesAdapter
import nt.vn.mentorapp.models.Resource
import java.util.*

class AddResources : AppCompatActivity() {
    private lateinit var recyclerViewResources: RecyclerView
    private lateinit var resourcesAdapter: ResourcesAdapter
    private val resourcesList = mutableListOf<Resource>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_resources)

        recyclerViewResources = findViewById(R.id.recyclerViewResources)
        val fabAddResource: FloatingActionButton = findViewById(R.id.fabAddResource)

        recyclerViewResources.layoutManager = LinearLayoutManager(this)
        resourcesAdapter = ResourcesAdapter(resourcesList)
        recyclerViewResources.adapter = resourcesAdapter

        fabAddResource.setOnClickListener { showAddResourceDialog() }

        fetchResources()
    }

    private fun showAddResourceDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_resource, null)
        val titleInput = dialogView.findViewById<EditText>(R.id.editTextResourceTitle)
        val descriptionInput = dialogView.findViewById<EditText>(R.id.editTextResourceDescription)
        val linkInput = dialogView.findViewById<EditText>(R.id.editTextResourceLink)

        AlertDialog.Builder(this)
            .setTitle("Add Resource")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val title = titleInput.text.toString().trim()
                val description = descriptionInput.text.toString().trim()
                val link = linkInput.text.toString().trim()

                if (title.isEmpty() || description.isEmpty()) {
                    Toast.makeText(this, "Title and Description are required", Toast.LENGTH_SHORT)
                        .show()
                    return@setPositiveButton
                }

                addResourceToFirebase(title, description, link)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun addResourceToFirebase(title: String, description: String, link: String?) {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return
        val mentorId = currentUser.uid
        val resourceId = UUID.randomUUID().toString()

        val resource = Resource(
            id = resourceId,
            title = title,
            description = description,
            link = if (link.isNullOrEmpty()) null else link,
            mentorId = mentorId
        )

        val database = FirebaseDatabase.getInstance().reference
        database.child("resources").child(resourceId)
            .setValue(resource)
            .addOnSuccessListener {
                Toast.makeText(this, "Resource added successfully", Toast.LENGTH_SHORT).show()
                resourcesList.add(resource)
                resourcesAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to add resource", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchResources() {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return
        val database = FirebaseDatabase.getInstance().reference

        database.child("resources").orderByChild("mentorId").equalTo(currentUser.uid)
            .get()
            .addOnSuccessListener { snapshot ->
                resourcesList.clear()
                for (child in snapshot.children) {
                    val resource = child.getValue(Resource::class.java)
                    if (resource != null) {
                        resourcesList.add(resource)
                    }
                }
                resourcesAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to fetch resources", Toast.LENGTH_SHORT).show()
            }
    }
}
