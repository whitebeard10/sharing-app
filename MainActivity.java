package com.example.vit_share;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import com.example.vit_share.LoginActivity;




import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<Request> requestList;
    private RequestListAdapter adapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);
        listView = findViewById(R.id.listView);

        requestList = databaseHelper.getAllRequests();
        adapter = new RequestListAdapter(requestList);
        listView.setAdapter(adapter);

        Button newRequestButton = findViewById(R.id.newRequestButton);
        newRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNewRequestDialog();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Request request = (Request) parent.getItemAtPosition(position);
                if (request.getAcceptedBy() == -1) {
                    showAcceptRequestDialog(request, position);
                } else {
                    Toast.makeText(MainActivity.this, "This request has already been accepted.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void showNewRequestDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add A New Request");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.new_request_dialog, (ViewGroup) findViewById(android.R.id.content), false);
        final TextView titleEditText = viewInflated.findViewById(R.id.titleEditText);
        final TextView descriptionEditText = viewInflated.findViewById(R.id.descriptionEditText);
        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                int userId = 1; // TODO: Get the logged-in user ID
                boolean success = databaseHelper.insertRequest(title, description, userId);
                if (success) {
                    Toast.makeText(MainActivity.this, "Request added successfully.", Toast.LENGTH_SHORT).show();
                    requestList.clear();
                    requestList.addAll(databaseHelper.getAllRequests());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to add request.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void showAcceptRequestDialog(final Request request, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("\nAccept Request\n");

        builder.setMessage("\nDo you want to accept this request?\n\nTitle: " + request.getTitle() + "\nDescription: " + request.getDescription());

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int userId = 1; // TODO: Get the logged-in user ID
                boolean success = databaseHelper.acceptRequest(request.getId(), userId);
                if (success) {
                    Toast.makeText(MainActivity.this, "Request accepted successfully!", Toast.LENGTH_SHORT).show();
                    request.setAcceptedBy(userId);
                    requestList.set(position, request);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to accept request!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}