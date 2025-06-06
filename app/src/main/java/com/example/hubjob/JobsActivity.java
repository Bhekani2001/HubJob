package com.example.hubjob;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class JobsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<JobPostModel> jobList;
    JobPostAdapter adapter;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);

        recyclerView = findViewById(R.id.recyclerJobs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        jobList = new ArrayList<>();
        adapter = new JobPostAdapter(jobList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        fetchJobs();
    }

    private void fetchJobs() {
        db.collection("PostedJobs")
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(snapshot -> {
                    jobList.clear();
                    for (QueryDocumentSnapshot doc : snapshot) {
                        JobPostModel job = doc.toObject(JobPostModel.class);
                        jobList.add(job);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("JobsActivity", "Error fetching jobs", e));
    }
}
