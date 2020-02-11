package com.example.zotfinder20;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class selected_indoor_warning_message extends AppCompatActivity implements View.OnClickListener {
    private Button yesButton;
    private Button noButton;
    private Button unsureButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_indoor_warning_message);

        yesButton = findViewById(R.id.yes_button);
        noButton = findViewById(R.id.no_button);
        unsureButton = findViewById(R.id.unsure_button);

        yesButton.setOnClickListener(this);
        noButton.setOnClickListener(this);
        unsureButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId())
        {
            case R.id.yes_button:
                Intent intent_selected_yes = new Intent(this, selected_classroom_2.class);
                startActivity(intent_selected_yes);
                finish();
                break;
            case R.id.no_button:
                Intent intent_selected_no = new Intent(this,selected_classroom.class);
                startActivity(intent_selected_no);
                finish();
                break;
            case R.id.unsure_button:
                Intent intent_selected_unsure = new Intent(this,selected_classroom.class);
                startActivity(intent_selected_unsure);
                finish();
                break;
        }
    }
}
