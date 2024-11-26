package com.example.simpleloginapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText inputUsername, inputPassword;
    private Button btnLogin, btnRegister, btnLogout;
    private TextView textUsername, textEmail;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi view
        inputUsername = findViewById(R.id.username);
        inputPassword = findViewById(R.id.input_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        textUsername = findViewById(R.id.textUsername);
        textEmail = findViewById(R.id.textEmail);
        btnLogout = findViewById(R.id.btn_logout);

        // Inisialisasi DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Set klik tombol Login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = inputUsername.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Validasi input
                if (username.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Username tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                } else {
                    // Periksa apakah username dan password valid
                    boolean loginSuccessful = dbHelper.authenticateUser(username, password);

                    if (loginSuccessful) {
                        // Jika login berhasil, ambil email dan tampilkan data
                        String email = dbHelper.getEmailByUsername(username);
                        textUsername.setText("Username: " + username);
                        textEmail.setText("Email: " + email);

                        // Menampilkan TextView dan tombol logout
                        textUsername.setVisibility(View.VISIBLE);
                        textEmail.setVisibility(View.VISIBLE);
                        btnLogout.setVisibility(View.VISIBLE);

                        // Menyembunyikan form login setelah berhasil
                        inputUsername.setVisibility(View.GONE);
                        inputPassword.setVisibility(View.GONE);
                        btnLogin.setVisibility(View.GONE);
                        btnRegister.setVisibility(View.GONE);

                        Toast.makeText(MainActivity.this, "Login berhasil!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Jika username atau password salah
                        Toast.makeText(MainActivity.this, "Username atau password salah!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Set klik tombol Register untuk menuju ke RegisterActivity
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Membuka RegisterActivity
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // Set klik tombol Logout
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset tampilan ke form login
                textUsername.setVisibility(View.GONE);
                textEmail.setVisibility(View.GONE);
                btnLogout.setVisibility(View.GONE);

                inputUsername.setVisibility(View.VISIBLE);
                inputPassword.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.VISIBLE);
                btnRegister.setVisibility(View.VISIBLE);
            }
        });
    }
}
