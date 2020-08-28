package com.example.mychatapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.mychatapp.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    var mAuth : FirebaseAuth ?= null
    var mDatabase : FirebaseDatabase ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        loginButtonId.setOnClickListener {
            var email = login_email_et.text.toString()
            var password = login_password_et.text.toString()
            if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password))
            {
                loginUser(email,password)
            }else{
                Toast.makeText(this,"Sorry,Login failed",Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun loginUser(email: String, password: String) {

        mAuth!!.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                task: Task<AuthResult> ->
                if (task.isSuccessful)
                {

                    var displayName = email.split("@")[0]

                    var dashboardintent = Intent(this, DashBoardActivity::class.java)
                    dashboardintent.putExtra("name",displayName)
                    startActivity(dashboardintent)

                }else{
                    Toast.makeText(this,"Login Failed",Toast.LENGTH_LONG).show()
                }
            }
    }
}