package com.example.mytest_1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class intro_Activity extends AppCompatActivity {
    Button button_intro ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intero);

        Intent intent = new Intent(this,MainActivity.class);
        button_intro = findViewById(R.id.Button_start_intro);
        button_intro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(intent);
            }
        });
    }
}