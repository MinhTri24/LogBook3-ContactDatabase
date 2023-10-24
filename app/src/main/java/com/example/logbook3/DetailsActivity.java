package com.example.logbook3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class DetailsActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    MyDatabaseHelper myDb;
    ArrayList<String> demo_id, demo_name, demo_dob, demo_email, demo_image;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        myDb = new MyDatabaseHelper(DetailsActivity.this);
        demo_id = new ArrayList<>();
        demo_name = new ArrayList<>();
        demo_dob = new ArrayList<>();
        demo_email = new ArrayList<>();
        demo_image = new ArrayList<>();

        StoreDataInArrays();

        customAdapter = new CustomAdapter(DetailsActivity.this, demo_id, demo_name, demo_dob, demo_email, demo_image);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(DetailsActivity.this));
    }

    public void StoreDataInArrays(){
        Cursor cursor = myDb.readAllData();
        if(cursor.getCount() == 0){
            // If there is no data, then display a toast message to the user.
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }else{
            while(cursor.moveToNext()){
                demo_id.add(cursor.getString(0));
                demo_name.add(cursor.getString(1));
                demo_dob.add(cursor.getString(2));
                demo_email.add(cursor.getString(3));
                demo_image.add(cursor.getString(4));
            }
        }
    }
}