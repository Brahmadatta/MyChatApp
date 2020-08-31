package com.example.mychatapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mychatapp.R
import com.example.mychatapp.adapters.UsersAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_chats.*
import kotlinx.android.synthetic.main.fragment_users.*


class UsersFragment : Fragment() {

    var layoutManager : RecyclerView.LayoutManager ?= null
    var usersAdapter : UsersAdapter?= null
    var mUserDatabase : DatabaseReference?= null
    var lifecycleOwner : LifecycleOwner?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mUserDatabase = FirebaseDatabase.getInstance().reference.child("Users")


        Log.e("databse",mUserDatabase.toString())

        var linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        friendRecyclerViewId.setHasFixedSize(true)
        friendRecyclerViewId.layoutManager = linearLayoutManager

        lifecycleOwner = this

        usersAdapter = UsersAdapter(lifecycleOwner as UsersFragment)
        friendRecyclerViewId.adapter = usersAdapter
        usersAdapter!!.notifyDataSetChanged()
    }
    
}