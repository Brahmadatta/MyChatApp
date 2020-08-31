package com.example.mychatapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mychatapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    var mCurrentUser : FirebaseUser ?= null
    var mUserDatabase : DatabaseReference ?= null
    var userId : String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        supportActionBar!!.title = "Profile"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (intent.extras != null)
        {
            userId = intent!!.extras!!.get("userId").toString()

            mCurrentUser = FirebaseAuth.getInstance().currentUser

            mUserDatabase = FirebaseDatabase.getInstance().reference.child("Users")
                .child(userId!!)

            setUpProfile()
        }
    }

    private fun setUpProfile() {

        mUserDatabase!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                var display_name = snapshot.child("display_name").toString()
                var status = snapshot.child("status").toString()
                var image = snapshot.child("image").toString()

                profileName.text = display_name
                profileStatus.text = status
                Picasso.get().load(image).placeholder(R.drawable.profile_img).into(profilePicture)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}