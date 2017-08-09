package com.rdhomeseeker.dharam.rdhomeseeker.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.rdhomeseeker.dharam.rdhomeseeker.Activity.ChatActivity;
import com.rdhomeseeker.dharam.rdhomeseeker.Models.Chat;
import com.rdhomeseeker.dharam.rdhomeseeker.R;
import com.rdhomeseeker.dharam.rdhomeseeker.Utils.AppController;

import java.util.List;


public class ChatListRecyclerAdapter extends RecyclerView.Adapter<ChatListRecyclerAdapter.MyViewHolder> {

    ImageLoader imageLoader;
    Context context;
    List<Chat> chatList;
    public ChatListRecyclerAdapter(Context contex, List<Chat> chatList){
        this.context = contex;
        this.chatList=chatList;
        imageLoader = AppController.getInstance().getImageLoader();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements OnCompleteListener, View.OnClickListener {

        NetworkImageView network_image;
        TextView txt_name;
        TextView txt_message;

        public MyViewHolder(View itemView) {
            super(itemView);
            network_image = (NetworkImageView)itemView.findViewById(R.id.network_image);
            txt_name = (TextView)itemView.findViewById(R.id.txt_name);
            txt_message =(TextView)itemView.findViewById(R.id.txt_message);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onComplete(@NonNull Task task) {
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Chat chat =chatList.get(position);

            Intent intent=new Intent(context, ChatActivity.class);
            intent.putExtra("otherUserId",chat.author_uid);
            intent.putExtra("name",chat.name);

            context.startActivity(intent);

        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Chat chat = chatList.get(position);

        String url=chat.photoUrl;
       // if(!url.equals("")) {
        //}
//        else{
//        }
//        if(url.equals(""))
//            holder.network_image.setDefaultImageResId(R.drawable.sdf);
//        else

        if (!chatList.get(position).photoUrl.equals("")){
            holder.network_image.setImageUrl(chat.photoUrl, imageLoader);
        }else {
            holder.network_image.setDefaultImageResId(R.drawable.sdf);
        }


            holder.txt_name.setText(chat.name);
        holder.txt_message.setText(chat.message);

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

}
