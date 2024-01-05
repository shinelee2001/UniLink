package com.example.unilink.Adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.unilink.Models.CommentModel;
import com.example.unilink.Models.PostModel;
import com.example.unilink.R;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context context;
    private List<CommentModel> commentModelList;


    public CommentAdapter(Context context, List<CommentModel> commentModelList) {
        this.context = context;
        this.commentModelList = commentModelList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_recycler_view, parent, false);
        return new CommentAdapter.CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {

        CommentModel commentModel = commentModelList.get(position);

        // Display user name
        holder.userName.setText(commentModel.getUname());

        // Display date
        // TODO : date to be displayed
        holder.time.setText(convert_time((long) commentModel.getTimestamp()));

        // Display comment context
        holder.comment.setText(commentModel.getContent());

    }

    @Override
    public int getItemCount() {
        return commentModelList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView userName, time, comment;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.rec_comUname);
            time = itemView.findViewById(R.id.rec_comTime);
            comment = itemView.findViewById(R.id.rec_comContext);

        }
    }

    private String convert_time(long Time) {
        Calendar calendar = Calendar.getInstance(Locale.KOREA);
        calendar.setTimeInMillis(Time);
        String date = DateFormat.format("hh:mm", calendar).toString();
        return date;
    }
}
