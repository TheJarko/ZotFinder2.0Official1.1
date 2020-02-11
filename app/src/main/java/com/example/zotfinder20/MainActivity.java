package com.example.zotfinder20;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

//This is the main menu of the application
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button button_find_a_class;
    private Button button_find_a_building;
    private Button button_find_a_room;
    private Button button_help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_find_a_class = findViewById(R.id.outdoor_classroom_button);
        button_find_a_building = findViewById(R.id.building_button);
        button_find_a_room = findViewById(R.id.indoor_classroom_button);
        button_help = findViewById(R.id.help_button);

        button_find_a_class.setOnClickListener(this);
        button_find_a_building.setOnClickListener(this);
        button_find_a_room.setOnClickListener(this);
        button_help.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.outdoor_classroom_button:
                Intent intent_selected_classroom = new Intent(this, selected_classroom.class);
                startActivity(intent_selected_classroom);
                finish();
                break;
            case R.id.building_button:
                Intent intent_selected_building = new Intent(this, selected_building.class);
                startActivity(intent_selected_building);
                finish();
                break;
            case R.id.indoor_classroom_button:
                Intent intent_selected_room = new Intent(this,selected_indoor_warning_message.class);
                startActivity(intent_selected_room);
                finish();
                break;
            case R.id.help_button:
                Intent intent_selected_help = new Intent(this,selected_other.class);
                startActivity(intent_selected_help);
                finish();
                break;

        }
    }
}
