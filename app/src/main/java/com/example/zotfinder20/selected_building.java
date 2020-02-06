package com.example.zotfinder20;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;



public class selected_building extends AppCompatActivity  implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    private Button button_submit_2;
    private Button button_back_2;
    private static String buildingChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_building);

        //Add the spinner
        Spinner spinner = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.building_name,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //Buttons
        button_back_2 = findViewById(R.id.back_button_from_selected_building_page);
        button_submit_2 = findViewById(R.id.submit_button_from_selected_building_page);
        button_submit_2.setOnClickListener(this);
        button_back_2.setOnClickListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        buildingChoice = parent.getItemAtPosition(pos).toString();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public static String getData() {
        return buildingChoice;
    }


    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.back_button_from_selected_building_page:
                Intent intent_go_back_from_selected_building_page = new Intent(this,MainActivity.class);
                startActivity(intent_go_back_from_selected_building_page);
                finish();
                break;

            case R.id.submit_button_from_selected_building_page:
                Intent intent_submit = new Intent(this, Map_for_selected_building.class);
                startActivity(intent_submit);
                finish();
                break;
        }
    }



}
