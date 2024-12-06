package nt.vn.mentorapp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import nt.vn.mentorapp.classes.AuthManager

class RegisterActivity : AppCompatActivity() {

    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        authManager = AuthManager()

        // UI Elements
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val roleSpinner = findViewById<Spinner>(R.id.roleSpinner)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val usernameEditText=findViewById<EditText>(R.id.usernameEditText)

        // Set up the spinner with role options
        ArrayAdapter.createFromResource(
            this,
            R.array.role_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            roleSpinner.adapter = adapter
        }

        // Handle user registration
        registerButton.setOnClickListener {
            val email = emailEditText.text.trim().toString()
            val password = passwordEditText.text.toString()
            val selectedRole = roleSpinner.selectedItem.toString()
            val username=usernameEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                authManager.registerUser(this@RegisterActivity,username,email.toString(), password, selectedRole) { success ->
                    if (success) {
                        Toast.makeText(this, "Registration successful ${email} ${password}", Toast.LENGTH_SHORT).show()
                        // Redirect to login screen or home screen
                    } else {
                        Toast.makeText(this, "Registration failed ${email} ${password}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
