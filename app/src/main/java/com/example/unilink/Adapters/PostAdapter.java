package com.example.unilink.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.unilink.Activities.PostViewActivity;
import com.example.unilink.Models.PostModel;
import com.example.unilink.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
    private Context context;
    private List<PostModel> postModelList;

    public PostAdapter(Context context, List<PostModel> postModelList) {
        this.context = context;
        this.postModelList = postModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_recycler_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PostModel postModel = postModelList.get(position);

        // Display Post Title
        holder.postTitle.setText(postModel.getPostTitle());

        // Display Post Image if available
        if (postModel.getPostImage() != null) {
            Glide.with(context).load(postModel.getPostImage()).into(holder.postImage);
        }

        // Display Post Description
        holder.postDescription.setText(postModel.getPostDescription());

        // Display Posting Time and Writer Name
        holder.timeStamp_userName.setText(convert_time((Long) postModel.getTimeStamp()) + " | " + postModel.getUserName());

    }


    private String convert_time(long Time) {
        Calendar calendar = Calendar.getInstance(Locale.KOREA);
        calendar.setTimeInMillis(Time);
        String date = DateFormat.format("dd-mm-yyy", calendar).toString();
        return date;
    }

    @Override
    public int getItemCount() {
        return postModelList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView timeStamp_userName, postTitle, postDescription;
        private ImageView postImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            timeStamp_userName = itemView.findViewById(R.id.rec_timeStamp_userName);
            postTitle = itemView.findViewById(R.id.rec_postTitle);
            postDescription = itemView.findViewById(R.id.rec_postDescription);
            postImage = itemView.findViewById(R.id.rec_postImage);

            // view the post detail on a click
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // send post data to PostViewActivity so that data can be loaded from PostViewActivity
                    Intent postViewActivity = new Intent(context, PostViewActivity.class);
                    int position = getAdapterPosition();

                    postViewActivity.putExtra("postTitle", postModelList.get(position).getPostTitle());
                    postViewActivity.putExtra("postImage", postModelList.get(position).getPostImage());
                    postViewActivity.putExtra("postDescription", postModelList.get(position).getPostDescription());
                    postViewActivity.putExtra("postKey", postModelList.get(position).getPostKey());
                    postViewActivity.putExtra("userName", postModelList.get(position).getUserName());
                    long timestamp = (long) postModelList.get(position).getTimeStamp();
                    postViewActivity.putExtra("timeStamp", timestamp);

                    context.startActivity(postViewActivity);

                }
            });

        }
    }


}
