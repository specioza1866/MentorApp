package nt.vn.mentorconnect

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.SearchView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import nt.vn.mentorapp.R
import nt.vn.mentorapp.classes.MentorAdapter
import nt.vn.mentorapp.models.Mentor

class SearchActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var mentorAdapter: MentorAdapter
    private lateinit var filteredResultTextView: TextView

    private lateinit var expertiseSpinner: Spinner
    private lateinit var experienceSpinner: Spinner
    private lateinit var ratingSpinner: Spinner

    private var mentorList: MutableList<Mentor> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Initialize UI components
        searchView = findViewById(R.id.searchSkill)
        recyclerView = findViewById(R.id.resultsRecyclerView)
        filteredResultTextView = findViewById(R.id.filteredResult)
        expertiseSpinner = findViewById(R.id.filterExpertise)
        experienceSpinner = findViewById(R.id.filterExperience)
        ratingSpinner = findViewById(R.id.filterRating)

        // Initialize RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        mentorAdapter = MentorAdapter(mentorList)
        recyclerView.adapter = mentorAdapter

        // Load mentor data from Firebase
        loadMentorsFromFirebase()

        // Set up SearchView listener
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { filterMentors(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { filterMentors(it) }
                return true
            }
        })

        // Set up filtering when Spinner values change
        setupFilterListeners()
    }

    private fun loadMentorsFromFirebase() {
        val database = FirebaseDatabase.getInstance().reference.child("mentors")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mentorList.clear()
                for (mentorSnapshot in snapshot.children) {
                    val mentor = mentorSnapshot.getValue(Mentor::class.java)
                    mentor?.let { mentorList.add(it) }
                }
                mentorAdapter.updateData(mentorList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SearchActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun filterMentors(query: String) {
        val expertise = expertiseSpinner.selectedItem.toString()
        val experience = experienceSpinner.selectedItem.toString().toIntOrNull() ?: 0
        val rating = ratingSpinner.selectedItem.toString().toDoubleOrNull() ?: 0.0

        val filteredList = mentorList.filter { mentor ->
            (mentor.name.contains(query, true)) &&
                    (mentor.expertise == expertise || expertise == "Any") &&
                    mentor.experience >= experience.toString() &&
                    mentor.rating >= rating
        }

        mentorAdapter.updateData(filteredList)
        filteredResultTextView.text = "Filtered Results: ${filteredList.size} found"
    }

    private fun setupFilterListeners() {
        val filterListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                filterMentors(searchView.query.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        expertiseSpinner.onItemSelectedListener = filterListener
        experienceSpinner.onItemSelectedListener = filterListener
        ratingSpinner.onItemSelectedListener = filterListener
    }
}
