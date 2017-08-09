package com.rdhomeseeker.dharam.rdhomeseeker.Fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
public class ContactListFragment extends Fragment {

    List<User> phoneContactList;
    List<User> userList;
    List<User> filterdataList;

    RecyclerView recyclerview;

    public ContactListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        phoneContactList = new ArrayList<User>();
        userList =new ArrayList<User>();
        filterdataList = new ArrayList<User>();

        // Inflate the layout for this fragment
        recyclerview = (RecyclerView) inflater.inflate(R.layout.fragment_chatlist, container, false);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());


        return recyclerview;
    }

    @Override
    public void onStart() {
        super.onStart();

        phoneContactList.clear();
        userList.clear();
        filterdataList.clear();
        Cursor cursor = getActivity().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, "upper("+ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+") ASC");
        //Cursor cursor= getActivity().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
        if(cursor.moveToFirst())
        {

            do{
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))>0)
                {
                    Cursor pCur = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" = ? ",new String[]{id },null);
                    while (pCur.moveToNext())
                    {
                        String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String contactName = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                        User user = new User();
                        user.name = contactName+" -> "+contactNumber;
                        user.contact=contactNumber;
                        phoneContactList.add(user);

                        break;

                    }
                    pCur.close();

                }

            }while (cursor.moveToNext());
            ContactListRecyclerAdapter chatListRecyclerAdapter = new ContactListRecyclerAdapter(getActivity(),phoneContactList);
            recyclerview.setAdapter(chatListRecyclerAdapter);
        }

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
                Log.d("hello", userList.toString()+"============="+phoneContactList.toString());
                for(User firebaseuser :userList)
                {
                    for (User contactuser : phoneContactList){
                        String phoneContact = contactuser.contact.trim();
                        phoneContact =phoneContact.replaceAll(" ","");
                        phoneContact = phoneContact.substring(phoneContact.length()-10);
                        if (firebaseuser.contact.trim().equals(phoneContact) && !firebaseuser.contact.trim().equals("")){
                            filterdataList.add(firebaseuser);
                            break;


                        }
                    }
                }
                ContactListRecyclerAdapter chatListRecyclerAdapter = new ContactListRecyclerAdapter(getActivity(), filterdataList);
                recyclerview.setAdapter(chatListRecyclerAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}



