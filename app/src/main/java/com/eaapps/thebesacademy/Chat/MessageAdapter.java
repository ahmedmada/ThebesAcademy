package com.eaapps.thebesacademy.Chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eaapps.thebesacademy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by AkshayeJH on 24/07/17.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{


    private List<Messages> mMessageList;
    private DatabaseReference mUserDatabase;
    Context context;
    String mCurrentUserId;

    public MessageAdapter(List<Messages> mMessageList,Context context,String mCurrentUserId) {

        this.mMessageList = mMessageList;
        this.context = context;
        this.mCurrentUserId = mCurrentUserId;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_single_layout ,parent, false);

        return new MessageViewHolder(v);

    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText;
        public TextView time_text_layout;
//        public CircleImageView profileImage;
        public TextView displayName;
        public ImageView messageImage;
        public ImageView read_msg;
        public LinearLayout message_single_layout;

        public MessageViewHolder(View view) {
            super(view);

            messageText = (TextView) view.findViewById(R.id.message_text_layout);
            time_text_layout = (TextView) view.findViewById(R.id.time_text_layout);
//            profileImage = (CircleImageView) view.findViewById(R.id.message_profile_layout);
            displayName = (TextView) view.findViewById(R.id.name_text_layout);
            messageImage = (ImageView) view.findViewById(R.id.message_image_layout);
            read_msg = (ImageView) view.findViewById(R.id.read_msg);
            message_single_layout = (LinearLayout) view.findViewById(R.id.message_single_layout);

        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final MessageViewHolder viewHolder, int i) {

        Messages c = mMessageList.get(i);

        String from_user = c.getFrom();
        String message_type = c.getType();
        long time = c.getTime();
        Boolean seen = c.isSeen();


        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Student User").child(from_user);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
//                String image = dataSnapshot.child("imageUrl").getValue().toString();

                viewHolder.displayName.setText(name);

                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                String formattedDate = dateFormat.format(time).toString();
                viewHolder.time_text_layout.setText(formattedDate);

//                Picasso.with(viewHolder.profileImage.getContext()).load(image)
//                        .placeholder(R.drawable.default_avatar).into(viewHolder.profileImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (from_user.equals(mCurrentUserId)){
            viewHolder.read_msg.setVisibility(View.INVISIBLE);
            viewHolder.message_single_layout.setBackgroundColor(R.color.white);
        }else {
            viewHolder.message_single_layout.setBackgroundColor(R.color.hint);

            if (seen)
                viewHolder.read_msg.setVisibility(View.VISIBLE);
            else
                viewHolder.read_msg.setVisibility(View.INVISIBLE);

        }

        if(message_type.equals("text")) {

            viewHolder.messageText.setText(c.getMessage());
            Picasso.with(context).load(R.drawable.empty).into(viewHolder.messageImage);

        } else {

            viewHolder.messageText.setText("");
            Picasso.with(context).load(c.getMessage()).into(viewHolder.messageImage);

        }

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }






}
