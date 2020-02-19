package com.example.zotfinder20;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class selected_classroom_2 extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private Button button_back2;
    private Button button_submit2;
    private static String classroomChoice2;


    @Override
    protected void onCreate(Bundle savedInsanceState) {
        super.onCreate(savedInsanceState);
        setContentView(R.layout.activity_selected_classroom_2);


        //Drop down menu
        Spinner spinner2 = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.indoor_classroom_name_and_number,android.R.layout.simple_spinner_item); //CHANGE building_name TO THE OTHER ARRAY AT STRINGS.XML
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter);
        spinner2.setOnItemSelectedListener(this);

        //Buttons
        button_back2 = findViewById(R.id.back_button2);
        button_submit2 = findViewById(R.id.submit_button2);
        button_back2.setOnClickListener(this);
        button_submit2.setOnClickListener(this);

    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        classroomChoice2 = parent.getItemAtPosition(pos).toString();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public static String getData() {
        return classroomChoice2;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_button2:
                Intent intent_go_back_from_selected_classroom = new Intent(this, MainActivity.class);
                startActivity(intent_go_back_from_selected_classroom);
                finish();
                break;
                /*
            case R.id.submit_button2:
                Intent intent_submit = new Intent(this, selected_indoor_warning_message.class);
                startActivity(intent_submit);
                finish();
                break;

                 */
        }

    }
}
