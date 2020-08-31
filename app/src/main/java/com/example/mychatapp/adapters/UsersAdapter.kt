package com.example.mychatapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.mychatapp.R
import com.example.mychatapp.models.Users
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.users_row.view.*

class UsersAdapter(lifecycleOwner: LifecycleOwner) :
    FirebaseRecyclerAdapter<Users, UsersAdapter.ViewHolder>(
        options(lifecycleOwner)
    )
{

    companion object {
        private fun buildQuery() = FirebaseDatabase.getInstance()
            .reference
            .child("Users")

        private fun options(lifecycleOwner: LifecycleOwner) = FirebaseRecyclerOptions.Builder<Users>()
            .setQuery(buildQuery(), Users::class.java)
            .setLifecycleOwner(lifecycleOwner)
            .build()

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var view = LayoutInflater.from(parent.context).inflate(R.layout.users_row,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Users) {

        holder.bindView(model)
        Log.e("dataaa",model.toString())

    }

   class ViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview) {

        var userNameText : String ?=null
        var userStatusText : String ?= null
        var userProfilePicLink : String ?= null


        fun bindView(users: Users)
        {
            var username = itemView.findViewById<TextView>(R.id.userName)
            var userstatus = itemView.findViewById<TextView>(R.id.user_status)
            var usersProfile = itemView.findViewById<CircleImageView>(R.id.usersProfile)

            //setting the strings to pass intent
            userNameText = users.display_name
            userStatusText = users.status
            userProfilePicLink = users.thumb_image

            username.text = users.display_name
            userstatus.text = users.status

            itemView.setOnClickListener {
                Toast.makeText(itemView.context,userNameText.toString(),Toast.LENGTH_LONG).show()
            }

            Picasso.get().load(userProfilePicLink).placeholder(R.drawable.profile_img).into(usersProfile)

        }
    }


}