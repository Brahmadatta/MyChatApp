package com.example.mychatapp.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mychatapp.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_settings.*
import java.io.ByteArrayOutputStream
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
        mstorageRef = FirebaseStorage.getInstance().reference

        var userId = mcurrentUser!!.uid

        mDatabase = FirebaseDatabase.getInstance().reference
            .child("Users")
            .child(userId)

        mDatabase!!.addValueEventListener( object : ValueEventListener{
            override fun onDataChange(dataSnapshot : DataSnapshot) {

                var displayName = dataSnapshot.child("display_name").value
                var image = dataSnapshot.child("image").value.toString()
                var status = dataSnapshot.child("status").value
                var thumb_image = dataSnapshot.child("thumbnail_image").value

                settingDisplayName.text = displayName.toString()
                settingsStatusText.text = status.toString()

                if (!image!!.equals("default"))
                {
                    Picasso.get().load(image).placeholder(R.drawable.profile_img).into(settingProfileId);
                }
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
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_ID && resultCode == Activity.RESULT_OK) {
            var image: Uri = data!!.data!!

            CropImage.activity(image)
                .setAspectRatio(1, 1)
                .start(this)
        }

        if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)

            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri

                var userId = mcurrentUser!!.uid
                var thumbFile = File(resultUri.path)


                var thumbBitmap = Compressor(this)
                    .setMaxWidth(200)
                    .setMaxHeight(200)
                    .setQuality(65)
                    .compressToBitmap(thumbFile)

                //upload images to Firebase
                var byteArray = ByteArrayOutputStream()
                thumbBitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArray)
                var thumbByteArray : ByteArray
                thumbByteArray = byteArray.toByteArray()

                var filePath = mstorageRef!!.child("chat_profile_images")
                    .child(userId + ".jpg")

                //create another directory for thumb images (smaller,compressed images)

                var thumbFilePath = mstorageRef!!.child("chat_profile_images")
                    .child("thumbs")
                    .child(userId + ".jpg")

                filePath.putFile(resultUri)
                    .addOnCompleteListener {

                            taskSnapshot: Task<UploadTask.TaskSnapshot> ->

                        if (taskSnapshot.isSuccessful) {
                            var downloadUrl = resultUri.toString()
                            //This is where the magic starts :)

                            //upload image

                            var uploadTask: UploadTask = thumbFilePath.putBytes(thumbByteArray)

                            uploadTask.addOnCompleteListener { task: Task<UploadTask.TaskSnapshot> ->

                                var thumbUrl = filePath.downloadUrl.toString()   //and here as well!

                                if (task.isSuccessful) {

                                    var updateObj = HashMap<String, Any>()
                                    updateObj.put("image", downloadUrl)
                                    updateObj.put("thumb_image", thumbUrl)

                                    //save profile image

                                    mDatabase!!.updateChildren(updateObj)
                                        .addOnCompleteListener {

                                                task: Task<Void> ->
                                            if (task.isSuccessful) {
                                                Toast.makeText(
                                                    this,
                                                    "Profile image saved",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                        }
                                }
                            }
                        }
                    }
            }
        }
    }
}