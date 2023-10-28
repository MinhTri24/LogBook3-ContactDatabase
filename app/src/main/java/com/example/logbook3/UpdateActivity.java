package com.example.logbook3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.Objects;

public class UpdateActivity extends AppCompatActivity {

    EditText nameInput, emailInput;
    TextView dobControl;
    RadioGroup genderGroup;
    RadioButton boyRd, girlRd, otherRd;
    ImageView boyImg, girlImg, androidImg;
    String imageChosen;
    Button saveBtn, deleteBtn;
    String id, name, dob, email;

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
        {
            LocalDate d = LocalDate.now();
            int year = d.getYear();
            int month = d.getMonthValue();
            int day = d.getDayOfMonth();
            return new DatePickerDialog(requireActivity(), this, year, --month, day);}
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day){
            LocalDate dob = LocalDate.of(year, ++month, day);
            ((UpdateActivity) requireActivity()).updateDOB(dob);
        }
    }

    public void updateDOB(LocalDate dob){
        TextView dobControl = findViewById(R.id.dob_control);
        dobControl.setText(dob.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        nameInput = findViewById(R.id.name_input);
        emailInput = findViewById(R.id.email_input);
        dobControl = findViewById(R.id.dob_control);

        dobControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new UpdateActivity.DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker"); }
        });

        genderGroup = findViewById(R.id.radioGroup);

        boyImg = findViewById(R.id.boyImg);
        girlImg = findViewById(R.id.girlImg);
        androidImg = findViewById(R.id.otherImg);

        boyImg.setImageResource(R.drawable.ic_boy);
        girlImg.setImageResource(R.drawable.ic_girl);
        androidImg.setImageResource(R.drawable.ic_android);

        boyRd = findViewById(R.id.boy_rb);
        girlRd = findViewById(R.id.girl_rb);
        otherRd = findViewById(R.id.android_rb);

        getAndSetIntentData();


        saveBtn = findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper db = new MyDatabaseHelper(UpdateActivity.this);
                name = nameInput.getText().toString().trim();
                email = emailInput.getText().toString().trim();
                dob = dobControl.getText().toString().trim();

                if (boyRd.isChecked()){
                    imageChosen = getResources().getResourceEntryName(R.drawable.ic_boy);
                } else if (girlRd.isChecked()) {
                    imageChosen = getResources().getResourceEntryName(R.drawable.ic_girl);
                } else if (otherRd.isChecked()) {
                    imageChosen = getResources().getResourceEntryName(R.drawable.ic_android);
                }

                if (name.equals("") || email.equals("") || dob.equals("") || imageChosen.equals("")){
                    new AlertDialog.Builder(UpdateActivity.this)
                            .setTitle("Error")
                            .setMessage("Please fill in all fields")
                            .setPositiveButton("Back", null)
                            .show();
                    return;
                } else if (email.indexOf('@') == -1 || email.indexOf('.') == -1) {
                    new AlertDialog.Builder(UpdateActivity.this)
                            .setTitle("Error")
                            .setMessage("Please enter a valid email address")
                            .setPositiveButton("Back", null)
                            .show();
                    return;
                }
                db.updateData(id, name, dob, email, imageChosen);
                Intent intent = new Intent(UpdateActivity.this, DetailsActivity.class);
                startActivity(intent);
            }
        });

        deleteBtn = findViewById(R.id.delete_btn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
            }
        });
    }

    public void getAndSetIntentData(){
        if (getIntent().hasExtra("id") && getIntent().hasExtra("name") &&
                getIntent().hasExtra("dob") && getIntent().hasExtra("email")){

            // Getting data from intent
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            dob = getIntent().getStringExtra("dob");
            email = getIntent().getStringExtra("email");

            // Setting intent data
            nameInput.setText(name);
            dobControl.setText(dob);
            emailInput.setText(email);
        }else{
            // If there is no data, then display a toast message to the user.
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    public void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + name + " ?");
        builder.setMessage("Are you sure you want to delete " + name + " ?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
            myDB.deleteOneRow(id);
            Intent intent = new Intent(UpdateActivity.this, DetailsActivity.class);
            startActivity(intent);
        });
        builder.setNegativeButton("No", (dialog, which) -> {
        });
        builder.create().show();
    }
}