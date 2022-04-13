package com.example.culturenearby;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.culturenearby.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        if (isExistCurrentUser()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            return;
        }
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.btnLogin.setOnClickListener(view -> {
            String login = binding.editLogin.getText().toString().trim();
            String pass = binding.editPass.getText().toString().trim();

            if (login.isEmpty() || pass.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Լրացրեք բոլոր դաշտերը", Toast.LENGTH_SHORT).show();
                return;
            }

            if (login.equals(getString(R.string.admin_login)) && pass.equals(getString(R.string.admin_pass))) {
                insertCurrentUser(null, login, pass);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
                return;
            }

            if (isExistUser(login, pass)) {
                getUserAndInsertCurrent(login, pass);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
                return;
            }
            Toast.makeText(LoginActivity.this, "Սխալ տվյալներ", Toast.LENGTH_SHORT).show();
        });

        binding.btnRegister.setOnClickListener(view ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    public boolean isExistUser(String login, String pass) {
        Cursor c = null;
        try {
            String query = "select count(*) from usertable where login = ? and pass=?";
            c = db.rawQuery(query, new String[]{login, pass});
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

    private void insertCurrentUser(String email, String login, String pass) {
        db.execSQL("delete from " + "currentusertable");
        ContentValues cv = new ContentValues();
        cv.put("email", email);
        cv.put("login", login);
        cv.put("pass", pass);

        db.insert("currentusertable", null, cv);
    }

    private void getUserAndInsertCurrent(String login, String pass) {
        Cursor c = null;
        try {
            String query = "select * from usertable where login = ? and pass=?";
            c = db.rawQuery(query, new String[]{login, pass});
            if (c.moveToFirst()) {
                int emailIndex = c.getColumnIndex("email");
                String email = c.getString(emailIndex);
                insertCurrentUser(email, login, pass);
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    private boolean isExistCurrentUser() {
        Cursor c = null;
        try {
            String query = "select count(*) from currentusertable";
            c = db.rawQuery(query, new String[]{});
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