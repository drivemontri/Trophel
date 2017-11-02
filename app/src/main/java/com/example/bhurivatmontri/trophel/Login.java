package com.example.bhurivatmontri.trophel;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnTrophel = (Button) findViewById(R.id.login_btnTrophel);
        final EditText edtEmail = (EditText) findViewById(R.id.login_email);
        final EditText edtPassword = (EditText) findViewById(R.id.login_password);

        btnTrophel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString() ;
                String password = edtPassword.getText().toString();
                /*if(isValidEmail(email) && isValidPassword(password)){
                    Toast.makeText(Login.this, "Processing : " + email + " / " + password , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this,Home.class);
                    intent.putExtra("email",email);
                    startActivity(intent);
                }else{
                    edtEmail.setError("Your email is invalid type. ");
                }*/

                if(true){
                    if(true){
                        Toast.makeText(Login.this, "Processing : " + email + " / " + password , Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this,Home.class);
                        intent.putExtra("email",email);
                        startActivity(intent);
                    }
                    else{
                        edtPassword.setError("Your password must be at least 8 characters , with at least one upper-case and one number");
                    }
                }
                else{
                    edtEmail.setError("Your email is invalid type.");
                }
            }
        });
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public final  static boolean isValidPassword(final String password){
        return Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]{8,}$").matcher(password).matches();
    }

    public void hideKeyBoard(View view){
        InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}
