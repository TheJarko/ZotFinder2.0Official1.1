package com.example.zotfinder20;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class selected_classroom extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Button button_back;
    private Button button_submit;
    private static String classroomChoice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_classroom);

        //Drop down menu
        Spinner spinner = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.outdoor_classroom_name_and_number,android.R.layout.simple_spinner_item); //CHANGE building_name TO THE OTHER ARRAY AT STRINGS.XML
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //Buttons
        button_back = findViewById(R.id.back_button);
        button_submit = findViewById(R.id.submit_button);
        button_back.setOnClickListener(this);
        button_submit.setOnClickListener(this);

    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        classroomChoice = parent.getItemAtPosition(pos).toString();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public static String getData() {
        return classroomChoice;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_button:
                Intent intent_go_back_from_selected_classroom = new Intent(this, MainActivity.class);
                startActivity(intent_go_back_from_selected_classroom);
                finish();
                break;
            case R.id.submit_button:
                Intent intent_submit = new Intent(this, Map_for_selected_classroom.class);
                startActivity(intent_submit);
                finish();
                break;
        }

    }


}
