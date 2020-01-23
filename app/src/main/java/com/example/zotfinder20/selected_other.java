package com.example.zotfinder20;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class selected_other extends AppCompatActivity  implements View.OnClickListener{

    private Button button_submit_4;
    private Button button_back_4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_other);

        button_back_4 = findViewById(R.id.back_button_from_selected_other_page);
        button_submit_4 = findViewById(R.id.submit_button_from_selected_other_page);
        button_back_4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_button_from_selected_other_page:
                Intent intent_go_back_from_other_page = new Intent(this,MainActivity.class);
                startActivity(intent_go_back_from_other_page);
                finish();
                break;
        }
    }
}
