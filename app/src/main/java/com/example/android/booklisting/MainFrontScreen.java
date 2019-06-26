package com.example.android.booklisting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainFrontScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_front_screen);

        final EditText input_text = findViewById(R.id.search_edit_text);
        Button search = findViewById(R.id.button_search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = input_text.getText().toString();
                if (!input.equals("")) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("input_text",input);
                    setResult(RESULT_OK,resultIntent);
                    finish();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),"Your search is empty, try again!",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
}
