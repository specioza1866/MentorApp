package nt.vn.mentorapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import nt.vn.mentorapp.classes.UserAdapter
import nt.vn.mentorapp.models.User


class CommunityManagerDashboard: AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var usersRecyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private lateinit var searchBar: EditText
    private var userList = mutableListOf<User>()
    private lateinit var userType:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_manager_dashboard)
        userType = intent.extras?.getString("userType").toString()
        Toast.makeText(this,userType,Toast.LENGTH_LONG).show()
        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().getReference("users")

        // Initialize UI elements
        usersRecyclerView = findViewById(R.id.usersRecyclerView)
        searchBar = findViewById(R.id.searchBar)

        // Set up RecyclerView
        usersRecyclerView.layoutManager = LinearLayoutManager(this)
        userAdapter = UserAdapter(userList, ::onUserDelete, ::onUserUpdate)
        usersRecyclerView.adapter = userAdapter

        // Load users from Firebase
        loadUsersFromFirebase()

        // Add User button click listener

        // Search bar functionality
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterUsers(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    // Function to load users from Firebase
    private fun loadUsersFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (userSnapshot in snapshot.children) {
                    val id = userSnapshot.child("id").value.toString()
                    val name = userSnapshot.child("name").value.toString()
                    val email = userSnapshot.child("email").value.toString()
                    val role = userSnapshot.child("role").value.toString()
                    val isactive = userSnapshot.child("active").value.toString()
                    val user=User(id,name,email,role,isactive.toBoolean())
                    if (user != null&& user.role==userType) {
                        userList.add(user)
                    }
                }
                userAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    // Filter users based on search query
    private fun filterUsers(query: String) {
        val filteredList = userList.filter { user ->
            user.name.contains(query, ignoreCase = true) || user.role.contains(query, ignoreCase = true)
        }
        userAdapter.updateList(filteredList)
    }

    // Function to handle user deletion
    private fun onUserDelete(user: User) {

    }

    // Function to handle user updates
    private fun onUserUpdate(user: User) {
        // Open a dialog or another activity to update the user details
        openUpdateUserDialog(user)
    }

    // Function to add a new user (dummy function for now)
    private fun openAddUserDialog() {
        // Implement adding user functionality (open a dialog for input)
    }

    // Function to update user details (dummy function for now)
    private fun openUpdateUserDialog(user: User) {
        // Implement updating user functionality (open a dialog with existing details)
    }
}
