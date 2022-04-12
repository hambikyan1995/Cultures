package com.example.culturenearby;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.culturenearby.databinding.ActivityLoginBinding;
import com.example.culturenearby.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.btnRegister.setOnClickListener(view -> {
            String email = binding.editEmail.getText().toString().trim();
            String login = binding.editLogin.getText().toString().trim();
            String pass = binding.editPass.getText().toString().trim();

            if (email.isEmpty() || login.isEmpty() || pass.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Լրացրեք բոլոր դաշտերը", Toast.LENGTH_SHORT).show();
                return;
            }

            DBHelper dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();

            insertItem(email, login, pass);
        });
    }

    private void insertItem(String email, String login, String pass) {
        if (!isExist(email)) {
            ContentValues cv = new ContentValues();
            cv.put("email", email);
            cv.put("login", login);
            cv.put("pass", pass);

            db.insert("usertable", null, cv);

            Toast.makeText(RegisterActivity.this, "Հաջողվեց", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(RegisterActivity.this, "Email- ը Զբաղված է", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isExist(String email) {
        Cursor c = null;
        try {
            String query = "select count(*) from usertable where email = ?";
            c = db.rawQuery(query, new String[]{email});
            if (c.moveToFirst()) {
                return c.getInt(0) != 0;
            }
            return false;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }
}