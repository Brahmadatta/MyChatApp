package com.example.mychatapp.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mychatapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_settings.*
import java.io.File

class SettingsActivity : AppCompatActivity() {

    var mDatabase : DatabaseReference ?= null
    var mcurrentUser : FirebaseUser ?= null
    var mstorageRef : StorageReference ?= null

    var GALLERY_ID : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        mcurrentUser = FirebaseAuth.getInstance().currentUser

        var userId = mcurrentUser!!.uid

        mDatabase = FirebaseDatabase.getInstance().reference
            .child("Users")
            .child(userId)

        mDatabase!!.addValueEventListener( object : ValueEventListener{
            override fun onDataChange(dataSnapshot : DataSnapshot) {

                var displayName = dataSnapshot.child("display_name").value
                var image = dataSnapshot.child("image").value
                var status = dataSnapshot.child("status").value
                var thumb_image = dataSnapshot.child("thumbnail_image").value

                settingDisplayName.text = displayName.toString()
                settingsStatusText.text = status.toString()
            }

            override fun onCancelled(databaseError : DatabaseError) {

            }
        })

        settingsChangeStatus.setOnClickListener {
            var intent = Intent(this,StatusActivity::class.java)
            intent.putExtra("status",settingsStatusText.text.toString().trim())
            startActivity(intent)
        }

        settingchangeImageButton.setOnClickListener {
            var galleryIntent = Intent()
            galleryIntent.type = "image/*"
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(galleryIntent,"SELECT_IMAGE"),GALLERY_ID)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == GALLERY_ID && resultCode == Activity.RESULT_OK)
        {
            var image : Uri = data!!.data!!

            CropImage.activity(image)
                .setAspectRatio(1,1)
                .start(this)
        }

        if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            val result = CropImage.getActivityResult(data)

            if (resultCode == Activity.RESULT_OK)
            {
                val resultUri = result.uri

                var userId = mcurrentUser!!.uid
                var thumbFile = File(resultUri.path)
            }
        }
    }
}