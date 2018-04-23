package com.monsordi.gotravel.activities;

import android.app.Dialog;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.monsordi.gotravel.MyPreference;
import com.monsordi.gotravel.R;
import com.monsordi.gotravel.Utils;
import com.monsordi.gotravel.api.SignApi;
import com.monsordi.gotravel.dialog.DialogGoTravel;
import com.monsordi.gotravel.dto.Usuario;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener,
        SignApi.EmailPasswordTasks, SignApi.StringListener,
        DialogGoTravel.DialogGoTravelTasks{

    @BindView(R.id.signUp_nameEditText) EditText nameEditText;
    @BindView(R.id.signUp_lastNameEditText) EditText lastNameEditText;
    @BindView(R.id.signUp_emailEditText) EditText emailEditText;
    @BindView(R.id.signUp_passwordEditText) EditText passwordEditText;
    @BindView(R.id.signUp_confirmEditText) EditText confirmPasswordEditText;
    @BindView(R.id.signUp_signUpButton) Button signUpButton;
    @BindView(R.id.signUp_progressBar) ProgressBar progressBar;
    @BindView(R.id.signUp_container) ConstraintLayout container;
    @BindView(R.id.signUp_toolbar) Toolbar toolbar;

    private MyPreference preference;
    private Long id;
    private String name;
    private String email;
    private String password;
    private String confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        signUpButton.setOnClickListener(this);
        preference = new MyPreference(this);
    }

    //****************************************************************************************************************
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            //Reads all fields
            case R.id.signUp_signUpButton:
                String name = nameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();
                this.name = new StringBuilder(name).append(" ").append(lastName).toString();
                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();
                confirmPassword = confirmPasswordEditText.getText().toString();

                //If no field is empty, the user is warned to check their information
                if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(email)
                    && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmPassword)){
                    DialogGoTravel dialogGoTravel = new DialogGoTravel(this,this);
                    dialogGoTravel.showGoTravelDialog(getString(R.string.continue_sign_up));
                }
                else
                    Toast.makeText(this,getString(R.string.fill_all_fields),Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            //Handles back navigation
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //****************************************************************************************************************

    //The next three following methods are the ones related to the selection in the appearing dialog.

    //Closes dialog
    @Override
    public void doCancelTask(Dialog dialog) {
        dialog.dismiss();
    }

    @Override
    public void doOkTask(Dialog dialog) {
        dialog.dismiss();
        proceedWithSignUp(email,password,confirmPassword);
    }

    //Proceeds with signing up process if both passwords match.
    public void proceedWithSignUp(String email,String password,String confirmPassword){
        if(password.equals(confirmPassword)){
            SignApi signUpController = new SignApi(this,jsonListener,this);
            signUpController.signUp(name,email,password,SignApi.USER);
        } else
            Toast.makeText(this,getString(R.string.passwords_dont_match),Toast.LENGTH_SHORT).show();
    }

    //****************************************************************************************************************

    SignApi.JsonListener jsonListener = new SignApi.JsonListener() {
        @Override
        public void onResponse(JSONObject response){
            Gson gson = new Gson();
            Usuario usuario = gson.fromJson(response.toString(),Usuario.class);
            id = usuario.getId();
            SignApi signInController = new SignApi(SignUpActivity.this,SignUpActivity.this,SignUpActivity.this);
            signInController.signIn(usuario.getEmail(),usuario.getPassword(),SignApi.USER);
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            showProgressDialog(false);
            Toast.makeText(SignUpActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    //****************************************************************************************************************

    //These methods handle sign in


    @Override
    public void onResponse(String response) {
        if(response!=null){
            id = Long.parseLong(response.substring(0,response.indexOf("-")));
            String token = response.substring(response.indexOf("-")+1);
            preference.setOld(true);
            preference.setToken(token);
            preference.setId(id);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        showProgressDialog(false);
        if(error instanceof ServerError)
            Toast.makeText(SignUpActivity.this, getString(R.string.error_auth), Toast.LENGTH_SHORT).show();
    }

    //****************************************************************************************************************

    //Changes visibility depending of the sign up state
    @Override
    public void showProgressDialog(boolean isShown) {
        container.setVisibility(isShown? View.GONE : View.VISIBLE);
        progressBar.setVisibility(isShown? View.VISIBLE : View.GONE);
    }

    //Sets an error in the corresponding editText depending on the EmailCode
    @Override
    public void setEmailErrorCode(Utils.EmailCode emailErrorCode) {
        if(emailErrorCode == Utils.EmailCode.UNMATCHED_EMAIL)
            emailEditText.setError(getString(R.string.unmatched_email));
    }

    //Sets an error in the corresponding editText depending on the PasswordCode
    @Override
    public void setPasswordErrorCode(Utils.PasswordCode passwordErrorCode) {
        if(passwordErrorCode == Utils.PasswordCode.UNMATCHED_PASSWORD)
            passwordEditText.setError(getString(R.string.unmatched_password));
    }


}
