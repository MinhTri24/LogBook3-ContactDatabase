package com.example.logbook3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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

import java.time.LocalDate;

public class MainActivity extends AppCompatActivity {

    EditText nameInput, emailInput;
    TextView dobControl;
    RadioGroup genderGroup;
    RadioButton boyRd, girlRd, otherRd;
    ImageView boyImg, girlImg, androidImg;
    String imageChosen;
    Button saveBtn, viewBtn;

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
            return new DatePickerDialog(getActivity(), this, year, --month, day);}
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day){
            LocalDate dob = LocalDate.of(year, ++month, day);
            ((MainActivity)getActivity()).updateDOB(dob);
        }
    }

    public void updateDOB(LocalDate dob){
        TextView dobControl = findViewById(R.id.dob_control);
        dobControl.setText(dob.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameInput = findViewById(R.id.name_input);
        emailInput = findViewById(R.id.email_input);
        dobControl = findViewById(R.id.dob_control);

        dobControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
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

        saveBtn = findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper db = new MyDatabaseHelper(MainActivity.this);
                String name = nameInput.getText().toString();
                String email = emailInput.getText().toString();
                String dob = dobControl.getText().toString();

                if (boyRd.isChecked()){
                    imageChosen = getResources().getResourceEntryName(R.drawable.ic_boy);
                } else if (girlRd.isChecked()) {
                    imageChosen = getResources().getResourceEntryName(R.drawable.ic_girl);
                } else if (otherRd.isChecked()) {
                    imageChosen = getResources().getResourceEntryName(R.drawable.ic_android);
                }

                if (name.equals("") || email.equals("") || dob.equals("") || imageChosen.equals("")){
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Error")
                            .setMessage("Please fill in all fields")
                            .setPositiveButton("Back", null)
                            .show();
                    return;
                } else if (email.indexOf('@') == -1 || email.indexOf('.') == -1) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Error")
                            .setMessage("Please enter a valid email address")
                            .setPositiveButton("Back", null)
                            .show();
                    return;
                }
                db.addPerson(name, dob, email, imageChosen);
            }
        });

        viewBtn = findViewById(R.id.view_btn);
        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class); // DetailsActivity is not created yet
                startActivity(intent);
            }
        });
    }
}