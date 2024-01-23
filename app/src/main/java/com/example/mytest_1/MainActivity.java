package com.example.mytest_1;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;


import android.annotation.SuppressLint;

import android.os.Bundle;
import android.text.InputType;
import java.text.DecimalFormat;


import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import com.google.android.material.textfield.TextInputEditText;


import java.util.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout gradesLayout;
    EditText credits, CourseName;
    List <String> courseSelect = new ArrayList<>();
    Map < String , Double > Scale_4 = new HashMap<>();
    Map < String , Double > Scale_5 = new HashMap<>();
    RadioButton Scale4 , Scale5 ;
    TextInputEditText GPA_last , Hours_last ;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GPA_last = findViewById(R.id.cumulativeGPA);
        GPA_last.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        Hours_last = findViewById(R.id.cumulativeHours);
        Hours_last.setInputType(InputType.TYPE_CLASS_NUMBER);
        CourseName = findViewById(R.id.courseName);
        credits = findViewById(R.id.creditText);
        gradesLayout = findViewById(R.id.courseGrades);
        Scale4 = findViewById(R.id.Radio_Scale4);
        Scale5 = findViewById(R.id.Radio_Scale5);
        Button addCourse = findViewById(R.id.button_Add);
        Button CalculateGPA = findViewById(R.id.button_CalculateGPA);

        Scale4.setOnClickListener(this);
        Scale5.setOnClickListener(this);
        addCourse.setOnClickListener(this);
        CalculateGPA.setOnClickListener(this);


        courseSelect.add("A+");
        courseSelect.add("A");
        courseSelect.add("B+");
        courseSelect.add("B");
        courseSelect.add("C+");
        courseSelect.add("C");
        courseSelect.add("D+");
        courseSelect.add("D");
        courseSelect.add("F");

        Scale_4.put("A+",4.0);Scale_4.put("A",3.75);Scale_4.put("B+",3.5);Scale_4.put("B",3.0);
        Scale_4.put("C+",2.5);Scale_4.put("C",2.0);Scale_4.put("D",1.5);Scale_4.put("F",2.0);

        Scale_5.put("A+",5.0);Scale_5.put("A",4.75);Scale_5.put("B+",4.5);Scale_5.put("B",3.0);
        Scale_5.put("C+",2.5);Scale_5.put("C",2.0);Scale_5.put("D",1.5);Scale_5.put("F",2.0);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_Add){
            addView();
        } else if (v.getId() == R.id.button_CalculateGPA){
            calculateProcess();
        }
    }
    private void addView() {
        // Check if the number of child views in gradesLayout is less than 8
        if (gradesLayout.getChildCount() < 6) {
            // Inflate the row_add layout to create a new view
            @SuppressLint("InflateParams") View itemsView = getLayoutInflater().inflate(R.layout.row_add, null, false);
            EditText creditView = itemsView.findViewById(R.id.creditText);
            AppCompatSpinner gradeView = itemsView.findViewById(R.id.gradeSelect);
            ImageView closeView = itemsView.findViewById(R.id.row_remove);

            creditView.setInputType(InputType.TYPE_CLASS_NUMBER);
            ArrayAdapter<String> grade = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courseSelect);
            gradeView.setAdapter(grade);

            closeView.setOnClickListener(v -> removeView(itemsView));

            gradesLayout.addView(itemsView);
        }
    }

    private void removeView(View view) {
        gradesLayout.removeView(view);
    }
    private void calculateProcess() {

        if (gradesLayout.getChildCount() == 0) {
            Toast.makeText(this, "Please Add items to Calculate!", Toast.LENGTH_SHORT).show();
            return;
        }
        double TotalHxC = 0.0;
        int TotalHours = 0;

        for (int i = 0; i < gradesLayout.getChildCount(); i++) {
            View v = gradesLayout.getChildAt(i);
            EditText HourText = v.findViewById(R.id.creditText);
            AppCompatSpinner GradeText = v.findViewById(R.id.gradeSelect);

            // Check if the credit EditText is empty
            if (HourText.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please enter credits for all items", Toast.LENGTH_SHORT).show();
                return;
            }

            int Hour = Integer.parseInt(HourText.getText().toString());
            String grade = GradeText.getSelectedItem().toString();

            TotalHours += Hour;

            if (Scale4.isChecked())
                TotalHxC += (Hour * Scale_4.get(grade));
            else if (Scale5.isChecked())
                TotalHxC += (Hour * Scale_5.get(grade));
            else {
                Toast.makeText(this, "Please select a GPA scale", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (Hours_last.getText().toString().isEmpty() && GPA_last.getText().toString().isEmpty()) {
            // Calculate the GPA based on the total weighted grade points and credit hours
            double GPA = (TotalHxC / TotalHours);
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            String formatGPA = decimalFormat.format(GPA);
            showGpaDialog(Double.parseDouble(formatGPA));
            showGpaDialog(GPA);
        }

        else {
            double GPA_NOW = (TotalHxC / TotalHours);
            int HOURS_LAST = Integer.parseInt(Hours_last.getText().toString());
            double GPA_LAST = Double.parseDouble(GPA_last.getText().toString());
            double GPA = ((HOURS_LAST * GPA_LAST) + (GPA_NOW * TotalHours)) / (HOURS_LAST + TotalHours);
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            String formatGPA = decimalFormat.format(GPA);
            showGpaDialog(Double.parseDouble(formatGPA));
        }
    }
    private void showGpaDialog(double gpa) {

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.success_dialog, null);
        TextView gpaTextView = dialogView.findViewById(R.id.successDesc);
        gpaTextView.setText(String.valueOf(gpa));
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(dialogView);
        builder.setPositiveButton("Done", (dialog, which) -> {
            dialog.dismiss();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}