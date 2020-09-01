package com.example.mychatapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mychatapp.R
import com.example.mychatapp.adapters.UsersAdapter
import com.example.mychatapp.models.FriendlyMessage
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.custom_bar_image.view.*

class ChatActivity : AppCompatActivity() {

    var userId : String ?= null
    var mDatabase : DatabaseReference ?= null
    var mFirebaseUser : FirebaseUser ?= null

    var linearLayoutManager : LinearLayoutManager ?= null
    var mFirebaseAdapter : FirebaseRecyclerAdapter<FriendlyMessage,MessageViewHolder> ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        mFirebaseUser = FirebaseAuth.getInstance().currentUser

        userId = intent.extras!!.getString("userId")
        var profileImage = intent!!.extras!!.getString("userimage").toString()
        var userName = intent!!.extras!!.getString("userName").toString()

        linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager!!.stackFromEnd = true

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowCustomEnabled(true)

        var inflater = this.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        var actionBarView = inflater.inflate(R.layout.custom_bar_image,null)
        actionBarView.customBarName.text = userName

        Picasso.get().load(profileImage).placeholder(R.drawable.profile_img).into(actionBarView.customBarImage)

        supportActionBar!!.customView = actionBarView


        mDatabase = FirebaseDatabase.getInstance().reference

        val chatQuery = FirebaseDatabase.getInstance()
            .reference
            .child("messages")

        val options = FirebaseRecyclerOptions.Builder<FriendlyMessage>()
            .setQuery(chatQuery,FriendlyMessage::class.java)
            .setLifecycleOwner(this)
            .build()

        mFirebaseAdapter = object : FirebaseRecyclerAdapter<FriendlyMessage,MessageViewHolder>(
           options
        ){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
                var view = LayoutInflater.from(parent.context).inflate(R.layout.item_message,parent,false)
                return MessageViewHolder(view)
            }

            override fun onBindViewHolder(
                holder: MessageViewHolder,
                position: Int,
                friendlyMessage : FriendlyMessage
            ) {
                if (friendlyMessage!!.text != null)
                {
                    holder!!.bindView(friendlyMessage)

                    var currentUserId = mFirebaseUser!!.uid
                    var isMe : Boolean = friendlyMessage!!.id!!.equals(currentUserId)

                    if (isMe)
                    {
                        //current user to right side
                        holder.profileImageViewRight!!.visibility = View.VISIBLE
                        holder.messengerImageView!!.visibility = View.GONE
                        holder.messageTextView!!.gravity = (Gravity.CENTER_VERTICAL or Gravity.RIGHT)
                        holder.messengerTextView!!.gravity = (Gravity.CENTER_VERTICAL or Gravity.RIGHT)

                        //Get Image for the current user
                        mDatabase!!.child("Users")
                            .child(currentUserId)
                            .addValueEventListener(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    var image = snapshot!!.child("image").value.toString()
                                    var displayName = snapshot!!.child("display_name").value.toString()

                                    //holder.messengerTextView!!.text = displayName.toString()
                                    holder.messengerTextView!!.text = "I Wrote..."

                                    Picasso.get().load(image).placeholder(R.drawable.profile_img)
                                        .into(holder.profileImageViewRight)

                                }

                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }
                            })

                    }else{
                        //other user to left side

                        holder.profileImageViewRight!!.visibility = View.GONE
                        holder.messengerImageView!!.visibility = View.VISIBLE
                        holder.messageTextView!!.gravity = (Gravity.START or Gravity.CENTER_VERTICAL)
                        holder.messengerTextView!!.gravity = (Gravity.START or Gravity.CENTER_VERTICAL)

                        //Get Image for the current user
                        mDatabase!!.child("Users")
                            .child(userId!!)
                            .addValueEventListener(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    var image = snapshot!!.child("image").value.toString()
                                    var displayName = snapshot!!.child("display_name").value.toString()

                                   // holder.messengerTextView!!.text = displayName.toString()
                                    holder.messengerTextView!!.text = "$displayName Wrote..."

                                    Picasso.get().load(image).placeholder(R.drawable.profile_img)
                                        .into(holder.messengerImageView)

                                }

                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }
                            })
                    }
                }



            }

        }

        //setting recyclerview
        messageRecyclerView.layoutManager = linearLayoutManager
        messageRecyclerView.setHasFixedSize(true)
        messageRecyclerView.adapter = mFirebaseAdapter

        sendButton.setOnClickListener {
            if (!intent!!.extras!!.get("userName").toString().equals(""))
            {
                var currentUsername = intent!!.extras!!.get("userName")
                var currentUserId = mFirebaseUser!!.uid


                var friendlyMessage = FriendlyMessage(
                    currentUserId,messageEdt.text.toString().trim(),
                    currentUsername.toString().trim())

                mDatabase!!.child("messages")
                    .push().setValue(friendlyMessage)

                messageEdt.setText("")

                mFirebaseAdapter!!.notifyDataSetChanged()
                linearLayoutManager!!.stackFromEnd = true

            }
        }

    }

    class MessageViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        var messageTextView : TextView ?= null
        var messengerTextView : TextView ?= null
        var messengerImageView : CircleImageView ?= null
        var profileImageViewRight : CircleImageView ?= null

        fun bindView(friendlyMessage: FriendlyMessage)
        {
            messageTextView = itemView.findViewById(R.id.messageTextView)
            messengerTextView = itemView.findViewById(R.id.messengerTextView)
            messengerImageView = itemView.findViewById(R.id.messengerImageView)
            profileImageViewRight = itemView.findViewById(R.id.messengerImageViewRight)

            messengerTextView!!.text = friendlyMessage.name.toString()
            messageTextView!!.text = friendlyMessage.text.toString()
        }

    }
}


