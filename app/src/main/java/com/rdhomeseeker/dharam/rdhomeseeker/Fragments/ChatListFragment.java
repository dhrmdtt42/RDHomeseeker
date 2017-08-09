package com.rdhomeseeker.dharam.rdhomeseeker.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rdhomeseeker.dharam.rdhomeseeker.Adapter.ContactListRecyclerAdapter;
import com.rdhomeseeker.dharam.rdhomeseeker.Models.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatListFragment extends Fragment {
    RecyclerView recyclerview;
    List<User> userList;


    public ChatListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        userList = new ArrayList<User>();

        recyclerview = (RecyclerView) inflater.inflate(R.layout.fragment_chatlist, container, false);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());

//        User user = new User("Dharam","dharam@gmail.com","12345","ddm.kk");
//        userList.add(user);
//        User user1 = new User("Dhara","dhara@gmail.com","85695","ddmmk.kk");
//        userList.add(user1);



        // Inflate the layout for this fragment
        return recyclerview;
    }

    @Override
    public void onStart() {
        super.onStart();
        userList.clear();
        Query query = FirebaseDatabase.getInstance().getReference().child("user");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext())
                {
                    DataSnapshot dataSnapshot1 = (DataSnapshot) iterator.next();
                    Iterator iterator1 = dataSnapshot1.getChildren().iterator();
                    User user = new User();
                    user.uid = dataSnapshot1.getKey();
                    while (iterator1.hasNext())
                    {

                        DataSnapshot dataSnapshot2 = (DataSnapshot) iterator1.next();
                        if(dataSnapshot2.getKey().equals("name")){
                            user.name = dataSnapshot2.getValue().toString();
                        }
                        if(dataSnapshot2.getKey().equals("email")){
                            user.email = dataSnapshot2.getValue().toString();
                        }
                        if(dataSnapshot2.getKey().equals("contact")){
                            user.contact = dataSnapshot2.getValue().toString();
                        }
                        if(dataSnapshot2.getKey().equals("picture_url")){
                            user.picture_url = dataSnapshot2.getValue().toString();
                        }
                    }
                    userList.add(user);

                }
                ContactListRecyclerAdapter chatListRecyclerAdapter = new ContactListRecyclerAdapter(getActivity(), userList);
                recyclerview.setAdapter(chatListRecyclerAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
