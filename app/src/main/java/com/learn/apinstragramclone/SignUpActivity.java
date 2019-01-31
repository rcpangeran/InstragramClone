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

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText edtSignUpEmail, edtSignUpUserName, edtSignUpPassword;
    private Button btnSignUpSignUp, btnSignUpLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Save the current Installation to Back4App
        ParseInstallation.getCurrentInstallation().saveInBackground();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setTitle("Sign Up");

        edtSignUpEmail = findViewById(R.id.edtSignUpEmail);
        edtSignUpUserName = findViewById(R.id.edtSignUpUserName);
        edtSignUpPassword = findViewById(R.id.edtSignUpPassword);
        btnSignUpSignUp = findViewById(R.id.btnSignUpSignUp);
        btnSignUpLogin = findViewById(R.id.btnSignUpLogin);

        edtSignUpPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    onClick(btnSignUpSignUp);
                }
                return false;
            }
        });

        btnSignUpSignUp.setOnClickListener(SignUpActivity.this);
        btnSignUpLogin.setOnClickListener(SignUpActivity.this);

        if (ParseUser.getCurrentUser() != null) {
            // ParseUser.logOut();
            // transitionToSocialMediaActivity();
        }
   }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case (R.id.btnSignUpSignUp) :
                if (edtSignUpEmail.getText().toString().equals("") || edtSignUpUserName.getText().toString().equals("") || edtSignUpPassword.getText().toString().equals("")){
                    FancyToast.makeText(SignUpActivity.this,
                            "Email, User Name, Password is required!",
                            Toast.LENGTH_SHORT,
                            FancyToast.INFO,
                            true).show();
                } else {
                    final ParseUser appUser = new ParseUser();
                    appUser.setEmail(edtSignUpEmail.getText().toString());
                    appUser.setUsername(edtSignUpUserName.getText().toString());
                    appUser.setPassword(edtSignUpPassword.getText().toString());

                    final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
                    progressDialog.setMessage("Signing up " + edtSignUpUserName.getText().toString());
                    progressDialog.show();

                    appUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                FancyToast.makeText(SignUpActivity.this,
                                        appUser.getUsername() + " is signed up",
                                        Toast.LENGTH_SHORT,
                                        FancyToast.SUCCESS,
                                        true).show();
                                transitionToSocialMediaActivity();
                            } else {
                                FancyToast.makeText(SignUpActivity.this,
                                        "There was an error",
                                        Toast.LENGTH_SHORT,
                                        FancyToast.ERROR,
                                        true).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
                }
                break;
            case (R.id.btnSignUpLogin) :
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }

    }

    public void rootLayoutTapped(View view) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void transitionToSocialMediaActivity() {
        Intent intent = new Intent(SignUpActivity.this, SocialMediaActivity.class);
        startActivity(intent);
    }
}
