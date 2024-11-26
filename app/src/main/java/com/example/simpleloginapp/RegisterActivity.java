package com.example.simpleloginapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText inputUsername, inputEmail, inputPassword;
    private Button btnRegister;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inisialisasi view
        inputUsername = findViewById(R.id.username);
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.input_password);
        btnRegister = findViewById(R.id.btn_register);

        // Inisialisasi DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Set klik tombol Register
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = inputUsername.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Validasi input
                if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Harap isi semua field!", Toast.LENGTH_SHORT).show();
                } else if (dbHelper.checkUsernameExist(username)) {
                    Toast.makeText(RegisterActivity.this, "Username sudah digunakan!", Toast.LENGTH_SHORT).show();
                } else if (dbHelper.checkEmailExist(email)) {
                    Toast.makeText(RegisterActivity.this, "Email sudah terdaftar!", Toast.LENGTH_SHORT).show();
                } else {
                    // Menyimpan data ke database
                    long result = dbHelper.addUser(username, email, password);
                    if (result != -1) {
                        Toast.makeText(RegisterActivity.this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show();
                        // Pindah ke halaman login setelah registrasi berhasil
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registrasi gagal!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
