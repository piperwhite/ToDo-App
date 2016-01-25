package com.piperwhite.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {


    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String text = getIntent().getStringExtra("text");
        position = getIntent().getIntExtra("position", 0);

        View contentView = findViewById(R.id.id_content_main);
        EditText edEditItem=(EditText) contentView.findViewById(R.id.etEditItem);
        edEditItem.setText(text);
        edEditItem.setSelection(text.length());
        setUpButtonListener(contentView);
       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    private void setUpButtonListener(View contentView) {
        Button saveBtn= (Button) contentView.findViewById(R.id.btnSave);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText etItem = (EditText) findViewById(R.id.etEditItem);
                Intent data = new Intent();
                data.putExtra("text", etItem.getText().toString());
                data.putExtra("position", position);
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

}
