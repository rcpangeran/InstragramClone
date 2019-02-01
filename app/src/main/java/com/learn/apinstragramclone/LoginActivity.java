package com.learn.apinstragramclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtLoginEmail, edtLoginPassword;
    private Button btnLoginLogin, btnLoginSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Log In");

        edtLoginEmail = findViewById(R.id.edtLoginEmail);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);
        btnLoginLogin = findViewById(R.id.btnLoginLogin);
        btnLoginSignUp = findViewById(R.id.btnLoginSignUp);

        edtLoginPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    onClick(btnLoginLogin);
                }
                return false;
            }
        });

        btnLoginLogin.setOnClickListener(LoginActivity.this);
        btnLoginSignUp.setOnClickListener(LoginActivity.this);

        if (ParseUser.getCurrentUser() != null) {
            transitionToSocialMediaActivity();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case (R.id.btnLoginLogin) :
                if (edtLoginEmail.getText().toString().equals("") || edtLoginPassword.getText().toString().equals("")){
                    FancyToast.makeText(LoginActivity.this,
                            "Email, Password is required!",
                            Toast.LENGTH_SHORT,
                            FancyToast.INFO,
                            true).show();
                } else {
                    final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setMessage("Log In process is being proceed, please wait...");
                    progressDialog.show();

                    ParseUser.logInInBackground(edtLoginEmail.getText().toString(), edtLoginPassword.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (user != null && e == null) {
                                FancyToast.makeText(LoginActivity.this,
                                        user.getUsername() + " is logged in successfully",
                                        Toast.LENGTH_SHORT, FancyToast.SUCCESS,
                                        true).show();
                                transitionToSocialMediaActivity();
                            } else {
                                FancyToast.makeText(LoginActivity.this,
                                        "failed to Log in",
                                        Toast.LENGTH_SHORT, FancyToast.ERROR,
                                        true).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
                }
                break;
            case (R.id.btnLoginSignUp) :
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
                break;
        }

    }

    public void rootLayoutLoginTapped(View view) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void transitionToSocialMediaActivity() {
        Intent intent = new Intent(LoginActivity.this, SocialMediaActivity.class);
        startActivity(intent);
        finish();
    }
}
