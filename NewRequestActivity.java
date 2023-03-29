package com.example.vit_share;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NewRequestActivity extends AppCompatActivity {

    private EditText mTitleEditText;
    private EditText mDescriptionEditText;
    private Button mPostButton;

    private DatabaseHelper mDatabaseHelper;
    private int mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request);

        mTitleEditText = findViewById(R.id.title_edit_text);
        mDescriptionEditText = findViewById(R.id.description_edit_text);
        mPostButton = findViewById(R.id.post_button);

        mDatabaseHelper = new DatabaseHelper(this);

        mUserId = getIntent().getIntExtra("user_id", -1);

        mPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mTitleEditText.getText().toString();
                String description = mDescriptionEditText.getText().toString();

                if (TextUtils.isEmpty(title)) {
                    mTitleEditText.setError("Please enter a title");
                    mTitleEditText.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(description)) {
                    mDescriptionEditText.setError("Please enter a description");
                    mDescriptionEditText.requestFocus();
                    return;
                }

                if (mDatabaseHelper.insertRequest(title, description, mUserId)) {
                    Toast.makeText(getApplicationContext(), "Request posted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to post request. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}