package com.rdhomeseeker.dharam.rdhomeseeker.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.rdhomeseeker.dharam.rdhomeseeker.Activity.ChatActivity;
import com.rdhomeseeker.dharam.rdhomeseeker.Models.User;
import com.rdhomeseeker.dharam.rdhomeseeker.R;
import com.rdhomeseeker.dharam.rdhomeseeker.Utils.AppController;

import java.util.List;

import static com.rdhomeseeker.dharam.rdhomeseeker.R.styleable.RecyclerView;



public class ContactListRecyclerAdapter extends RecyclerView.Adapter<ContactListRecyclerAdapter.MyViewHolder> {

    ImageLoader imageLoader;
    Context context;
    List<User> userList;
    public ContactListRecyclerAdapter(Context contex, List<User> userList){
        this.context = contex;
        this.userList=userList;
        imageLoader = AppController.getInstance().getImageLoader();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements OnCompleteListener, View.OnClickListener {

        NetworkImageView network_image;
        TextView txt_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            network_image = (NetworkImageView)itemView.findViewById(R.id.network_image);
            txt_name = (TextView)itemView.findViewById(R.id.txt_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onComplete(@NonNull Task task) {
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            User user =userList.get(position);

            Intent intent=new Intent(context, ChatActivity.class);
            intent.putExtra("otherUserId",user.uid);
            intent.putExtra("name",user.name);

            context.startActivity(intent);
            Toast.makeText(context, "clicked" +user.email+user.email, Toast.LENGTH_LONG ).show();
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        User user = userList.get(position);

        String url=user.picture_url;
       // if(!url.equals("")) {
        //}
//        else{
//        }
//        if(url.equals(""))
//            holder.network_image.setDefaultImageResId(R.drawable.sdf);
//        else

        if (!userList.get(position).picture_url.equals("")){
            holder.network_image.setImageUrl(user.picture_url, imageLoader);
        }else {
            holder.network_image.setDefaultImageResId(R.drawable.sdf);
        }


            holder.txt_name.setText(user.name);

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

}
