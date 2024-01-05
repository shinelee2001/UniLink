package com.example.unilink.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


import com.example.unilink.Adapters.PostAdapter;
import com.example.unilink.Models.PostModel;
import com.example.unilink.R;
import com.example.unilink.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    PostAdapter postAdapter;
    List<PostModel> postList;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.logout_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return true;
        }
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // go to writing page on a click
        binding.goWritePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WritePostActivity.class));
            }
        });

        // displaying posts
        loadPosts();

    }

    private void loadPosts() {
        FirebaseDatabase.getInstance().getReference("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // save the data in the post model list
                postList = new ArrayList<>();
                for (DataSnapshot postsanp : snapshot.getChildren()) {
                    PostModel postModel = postsanp.getValue(PostModel.class);
                    postList.add(postModel);
                }

                // load post model list(postList) to postAdapter
                postAdapter = new PostAdapter(MainActivity.this, postList);
                binding.postRecyclerView.setAdapter(postAdapter);
                binding.postRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}