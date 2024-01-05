package com.example.unilink.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.unilink.Adapters.CommentAdapter;
import com.example.unilink.Adapters.PostAdapter;
import com.example.unilink.Models.CommentModel;
import com.example.unilink.Models.PostModel;
import com.example.unilink.R;
import com.example.unilink.databinding.ActivityMainBinding;
import com.example.unilink.databinding.ActivityPostViewBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PostViewActivity extends AppCompatActivity {

    ActivityPostViewBinding binding;
    ImageView viewImage;
    TextView viewTitle, viewDescription, viewTimeStamp_userName;
    EditText txtComment;
    Button commentBtn;
    String postKey;


    CommentAdapter commentAdapter;
    List<CommentModel> commentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // initialize view
        viewImage = binding.viewImage;
        viewTitle = binding.viewTitle;
        viewDescription = binding.viewDescription;
        viewTimeStamp_userName = binding.viewTimeStampUserName;
        txtComment = binding.txtComment;
        commentBtn = binding.commentBtn;

        // comment add button
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Comments").child(postKey).push();

                String comContent = txtComment.getText().toString();
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String uname = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                CommentModel commentModel = new CommentModel(comContent, uid, uname);

                databaseReference.setValue(commentModel)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(PostViewActivity.this, "Comment Added", Toast.LENGTH_SHORT).show();
                                txtComment.setText("");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PostViewActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                txtComment.setText("");
                            }
                        });
            }
        });

        // get the data from MyViewHolder (from PostAdapter)

        String postImage = getIntent().getExtras().getString("postImage");
        Glide.with(this).load(postImage).into(viewImage);

        String postTitle = getIntent().getExtras().getString("postTitle");
        viewTitle.setText(postTitle);

        String postDescription = getIntent().getExtras().getString("postDescription");
        viewDescription.setText(postDescription);

        postKey = getIntent().getExtras().getString("postKey");

        String date = convert_time(getIntent().getExtras().getLong("timeStamp"));
        viewTimeStamp_userName.setText(date);

        // displaying comments
        loadComments();

    }

    private String convert_time(long Time) {
        Calendar calendar = Calendar.getInstance(Locale.KOREA);
        calendar.setTimeInMillis(Time);
        String date = DateFormat.format("dd-mm-yyyy", calendar).toString();
        return date;
    }

    private void loadComments() {
        FirebaseDatabase.getInstance().getReference("Comments").child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // save the data in the comment model list
                commentList = new ArrayList<>();
                for (DataSnapshot commentsnap : snapshot.getChildren()) {
                    CommentModel commentModel = commentsnap.getValue(CommentModel.class);
                    commentList.add(commentModel);
                }

                // load post model list(postList) to postAdapter
                commentAdapter = new CommentAdapter(PostViewActivity.this, commentList);
                binding.comRecyclerView.setAdapter(commentAdapter);
                binding.comRecyclerView.setLayoutManager(new LinearLayoutManager(PostViewActivity.this));

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}