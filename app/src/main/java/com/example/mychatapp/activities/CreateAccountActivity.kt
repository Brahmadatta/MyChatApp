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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_create_account.*

class CreateAccountActivity : AppCompatActivity() {

    var firebaseAuth : FirebaseAuth ?= null

    var mDatabase : DatabaseReference ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)


        firebaseAuth = FirebaseAuth.getInstance()

        accountCreateActBtn.setOnClickListener {

            var email = account_email_et.text.toString().trim()
            var password = account_password_et.text.toString().trim()
            var displayName = account_display_name_et.text.toString().trim()

            if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password) || !TextUtils.isEmpty(displayName))
            {
                createAccount(email, password, displayName)

            }else{
                Toast.makeText(this,"Please fill out fields",Toast.LENGTH_LONG).show()
            }
        }


    }

    fun createAccount(email : String,password : String,displayName : String){

        firebaseAuth!!.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful)
                {

                    var currentUser = firebaseAuth!!.currentUser
                    var userId = currentUser!!.uid


                    mDatabase = FirebaseDatabase.getInstance().reference
                        .child("Users").child(userId.toString())


                    var userObject = HashMap<String,String>()
                    userObject.put("display_name",displayName)
                    userObject.put("status","Hi There!!!")
                    userObject.put("image","default")
                    userObject.put("thumbnail_image","default")


                    mDatabase!!.setValue(userObject).addOnCompleteListener {
                        task: Task<Void> ->
                        if (task.isSuccessful)
                        {

                            var dashboardintent = Intent(this, DashBoardActivity::class.java)
                            dashboardintent.putExtra("name",displayName)
                            startActivity(dashboardintent)
                            finish()

                        }else{
                            Toast.makeText(this,"User Not Created!!!",Toast.LENGTH_LONG).show()
                        }
                    }

                }
            }
    }
}