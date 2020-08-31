package com.example.mychatapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mychatapp.R


class ChatsFragment : Fragment() {




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_chats, container, false)
        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



//        var useQuery = FirebaseDatabase.getInstance()
//            .reference
//            .child("Users")
//
//        val options = FirebaseRecyclerOptions.Builder<Users>()
//            .setQuery(useQuery,Users::class.java)
//            .setLifecycleOwner(this)
//            .build()
//
//        val adapter = object : FirebaseRecyclerAdapter<Users, RecyclerView.ViewHolder>(options) {
//
//            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersAdapter.ViewHolder {
//                return UsersAdapter.ViewHolder(
//                    LayoutInflater.from(parent.context)
//                        .inflate(R.layout.users_row, parent, false)
//                )
//            }
//
//            protected override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, model: Users) {
//                holder.bind(model)
//            }
//
//
//
//        }



    }

}


