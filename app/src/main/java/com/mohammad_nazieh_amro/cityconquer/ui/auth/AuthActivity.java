package com.mohammad_nazieh_amro.cityconquer.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mohammad_nazieh_amro.cityconquer.R;
import com.mohammad_nazieh_amro.cityconquer.ui.MainActivity;
import java.util.HashMap;
import java.util.Map;

public class AuthActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput, usernameInput;
    private LinearLayout usernameContainer;
    private Button authActionBtn;
    private TextView tabLogin, tabRegister;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private boolean isLoginMode = true; // Defaults to Login mode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide action bar for cleaner look
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_auth);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        usernameInput = findViewById(R.id.username_input);
        usernameContainer = findViewById(R.id.username_container);
        authActionBtn = findViewById(R.id.auth_action_btn);
        tabLogin = findViewById(R.id.tab_login);
        tabRegister = findViewById(R.id.tab_register);
        progressBar = findViewById(R.id.auth_progress_bar);

        tabLogin.setOnClickListener(v -> {
            if (!isLoginMode) {
                isLoginMode = true;
                updateUI();
            }
        });

        tabRegister.setOnClickListener(v -> {
            if (isLoginMode) {
                isLoginMode = false;
                updateUI();
            }
        });

        authActionBtn.setOnClickListener(v -> handleAuthAction());

        updateUI();
    }

    private void updateUI() {
        if (isLoginMode) {
            // Switch Login tab to active
            tabLogin.setBackground(getDrawable(R.drawable.tab_selected_bg));
            tabLogin.setTextColor(0xFFFFFFFF);
            tabRegister.setBackground(getDrawable(R.drawable.tab_unselected_bg));
            tabRegister.setTextColor(0x88FFFFFF);
            // Hide username field for login
            usernameContainer.setVisibility(View.GONE);
            authActionBtn.setText("LOGIN");
        } else {
            // Switch Register tab to active
            tabRegister.setBackground(getDrawable(R.drawable.tab_selected_bg));
            tabRegister.setTextColor(0xFFFFFFFF);
            tabLogin.setBackground(getDrawable(R.drawable.tab_unselected_bg));
            tabLogin.setTextColor(0x88FFFFFF);
            // Show username field for registration
            usernameContainer.setVisibility(View.VISIBLE);
            authActionBtn.setText("CREATE ACCOUNT");
        }
    }

    private void handleAuthAction() {
        if (isLoginMode) {
            loginUser();
        } else {
            registerUser();
        }
    }

    private void loginUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading(true);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    showLoading(false);
                    navigateToMain();
                })
                .addOnFailureListener(e -> {
                    showLoading(false);
                    Toast.makeText(this, "Login failed: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
    }

    private void registerUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String username = usernameInput.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading(true);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    String userId = authResult.getUser().getUid();

                    Map<String, Object> user = new HashMap<>();
                    user.put("username", username);
                    user.put("email", email);
                    user.put("totalXP", 0);
                    user.put("level", 1);
                    user.put("friends", new java.util.ArrayList<>());

                    // Save user profile to Firestore
                    db.collection("users").document(userId)
                            .set(user)
                            .addOnSuccessListener(unused -> {
                                showLoading(false);
                                navigateToMain();
                            })
                            .addOnFailureListener(e -> {
                                // Firestore write failed. Rollback/signout user so their session is not incomplete
                                mAuth.signOut();
                                showLoading(false);
                                Toast.makeText(this, "Database write failed: " + e.getMessage() +
                                        "\nCheck Firestore Rules!", Toast.LENGTH_LONG).show();
                            });
                })
                .addOnFailureListener(e -> {
                    showLoading(false);
                    Toast.makeText(this, "Registration failed: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            authActionBtn.setEnabled(false);
            emailInput.setEnabled(false);
            passwordInput.setEnabled(false);
            usernameInput.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            authActionBtn.setEnabled(true);
            emailInput.setEnabled(true);
            passwordInput.setEnabled(true);
            usernameInput.setEnabled(true);
        }
    }
}