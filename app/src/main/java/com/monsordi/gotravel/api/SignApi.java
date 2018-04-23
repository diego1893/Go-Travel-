package com.monsordi.gotravel.api;

import android.app.VoiceInteractor;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonObject;
import com.monsordi.gotravel.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by diego on 19/04/18.
 */

public class SignApi {

    public static final String USER = "usuario/";
    public static final String ATTENDANT = "encargado/";
    public static final String COUNSELOR = "asesor/";

    private Context mContext;
    private JsonListener jsonListener;
    private StringListener stringListener;
    private EmailPasswordTasks mEmailPasswordTasks;

    public SignApi(Context mContext, JsonListener jsonListener, EmailPasswordTasks mEmailPasswordTasks) {
        this.mContext = mContext;
        this.jsonListener = jsonListener;
        this.mEmailPasswordTasks = mEmailPasswordTasks;
    }


    public interface JsonListener{
        void onResponse(JSONObject response) throws JSONException;
        void onErrorResponse(VolleyError error);
    }

    Response.Listener<JSONObject> jsonObjectListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            try {
                jsonListener.onResponse(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    Response.ErrorListener jsonErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            jsonListener.onErrorResponse(error);
        }
    };

    //****************************************************************************************************************

    public SignApi(Context mContext, StringListener stringListener, EmailPasswordTasks mEmailPasswordTasks) {
        this.mContext = mContext;
        this.stringListener = stringListener;
        this.mEmailPasswordTasks = mEmailPasswordTasks;
    }

    public interface StringListener{
        void onResponse(String response);
        void onErrorResponse(VolleyError error);
    }

    Response.Listener<String> stringObjectListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            stringListener.onResponse(response);
        }
    };

    Response.ErrorListener stringErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            stringListener.onErrorResponse(error);
        }
    };

    //****************************************************************************************************************

    //Methods to be implemented depending on the current activity.
    public interface EmailPasswordTasks{
        void showProgressDialog(boolean isShown);
        void setEmailErrorCode(Utils.EmailCode emailErrorCode);
        void setPasswordErrorCode(Utils.PasswordCode passwordErrorCode);
    }

    //****************************************************************************************************************

    public void signIn(String email,String password,String type){
        if(!isValidForm(email,password,false))
            return;

        //Changes visibility in some aspects of the UI and attempts to create a new user.
        mEmailPasswordTasks.showProgressDialog(true);
        signInApi(email,password,type);
    }

    private void signInApi(final String email, final String password,String type){
        String url = AppController.BASE_URL + type + "acceder";

        StringRequest request = new StringRequest(Request.Method.POST,url,stringObjectListener,stringErrorListener){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> hashMap = new HashMap<>();
                hashMap.put("correo",email);
                hashMap.put("clave",password);
                return hashMap;
            }
        };
        AppController.getInstance().addToRequestQueue(request);
    }

    //****************************************************************************************************************

    public void signUp(String name,String email, String password,String type) {
        if(!isValidForm(email,password,true))
            return;

        mEmailPasswordTasks.showProgressDialog(true);
        signUpApi(name,email,password,type);
    }

    private void signUpApi(String name, String email,String password,String type){
        String url = AppController.BASE_URL + type + "crear?nombre=" + name + "&correo=" + email + "&password=" + password ;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,url,null,jsonObjectListener,jsonErrorListener);
        AppController.getInstance().addToRequestQueue(request);
    }

    //****************************************************************************************************************

    //Determines if the necessary data in order to sign in or sign up has the right format.
    private boolean isValidForm(String email, String password, boolean checkPasswordForm){
        Utils utils = new Utils(mContext);

        Utils.EmailCode emailCode = utils.isValidEmail(email);
        mEmailPasswordTasks.setEmailErrorCode(emailCode);

        Utils.PasswordCode passwordCode = utils.isValidPassword(checkPasswordForm,password);
        mEmailPasswordTasks.setPasswordErrorCode(passwordCode);

        return emailCode == Utils.EmailCode.VALID_EMAIL && passwordCode == Utils.PasswordCode.VALID_PASSWORD;
    }
}
