package com.example.zotfinder20;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class selected_building extends AppCompatActivity  implements View.OnClickListener{

    private Button button_submit_2;
    private Button button_back_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_building);

        button_back_2 = findViewById(R.id.back_button_from_selected_building_page);
        button_submit_2 = findViewById(R.id.submit_button_from_selected_building_page);
        button_submit_2.setOnClickListener(this);
        button_back_2.setOnClickListener(this);
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

            //case R.id.submit_button_from_selected_building_page:
        }
    }


}
