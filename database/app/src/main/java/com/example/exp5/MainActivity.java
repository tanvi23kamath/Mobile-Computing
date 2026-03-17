package com.example.exp5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.*;
import android.database.Cursor;
import android.app.AlertDialog;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText name, age;
    Button addBtn;
    ListView listView;

    DBHelper db;

    ArrayList<String> list;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        addBtn = findViewById(R.id.addBtn);
        listView = findViewById(R.id.listView);

        db = new DBHelper(this);

        list = new ArrayList<>();

        loadStudents();

        // ADD STUDENT BUTTON
        addBtn.setOnClickListener(view -> {

            String n = name.getText().toString().trim();
            String a = age.getText().toString().trim();

            if (n.isEmpty() || a.isEmpty()) {
                Toast.makeText(MainActivity.this,
                        "Please enter name and age",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            boolean inserted = db.insertStudent(n, a);

            if (inserted) {

                Toast.makeText(MainActivity.this,
                        "Student Added",
                        Toast.LENGTH_SHORT).show();

                name.setText("");
                age.setText("");

                loadStudents();
            } else {

                Toast.makeText(MainActivity.this,
                        "Error inserting data",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // LONG PRESS TO DELETE STUDENT WITH CONFIRMATION
        listView.setOnItemLongClickListener((parent, view, position, id) -> {

            String item = list.get(position);
            int studentId = Integer.parseInt(item.split(" - ")[0]);

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Delete Student")
                    .setMessage("Are you sure you want to delete this student?")
                    .setPositiveButton("Yes", (dialog, which) -> {

                        db.deleteStudent(studentId);

                        Toast.makeText(MainActivity.this,
                                "Student Deleted",
                                Toast.LENGTH_SHORT).show();

                        loadStudents();
                    })
                    .setNegativeButton("No", null)
                    .show();

            return true;
        });
    }

    // LOAD STUDENTS FROM DATABASE
    void loadStudents() {

        list.clear();

        Cursor cursor = db.getStudents();

        while (cursor.moveToNext()) {

            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int age = cursor.getInt(2);

            list.add(id + " - " + name + " (" + age + ")");
        }

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                list);

        listView.setAdapter(adapter);
    }
}