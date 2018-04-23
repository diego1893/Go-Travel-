package com.monsordi.gotravel.api;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.monsordi.gotravel.dto.Orden;
import com.monsordi.gotravel.dto.Usuario;


import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by diego on 19/04/18.
 */

public class UserApi implements Response.ErrorListener, Response.Listener<JSONArray> {
    public static final String USER = "usuario/";

    private UserApi.UserListeners userListeners;
    private UserOrdersListeners userOrdersListeners;

    public UserApi(UserApi.UserListeners userListeners) {
        this.userListeners = userListeners;
    }

    public UserApi(UserOrdersListeners userOrdersListeners) {
        this.userOrdersListeners = userOrdersListeners;
    }

    public interface UserListeners{
        void onErrorResponse(VolleyError error);
        void onResponse(List<Usuario> userList);
    }

    public interface UserOrdersListeners {
        void onErrorResponse(VolleyError error);
        void onResponse(List<Orden> orderList);
    }

    public void getUserList(){
        String url = AppController.BASE_URL + USER;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,url,null,this,this);
        AppController.getInstance().addToRequestQueue(request);
    }

    public void getOrdersById(Long idOrder,String token){
        String url = AppController.BASE_URL + USER + idOrder + "/ordenes?token=" + token;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,url,null,orderResponseListener,orderErrorListener);
        AppController.getInstance().addToRequestQueue(request);
    }

    Response.Listener<JSONArray> orderResponseListener = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {
            Type listType = new TypeToken<ArrayList<Orden>>(){}.getType();
            List<Orden> ordersList = new Gson().fromJson(response.toString(),listType);
            userOrdersListeners.onResponse(ordersList);
        }
    };

    Response.ErrorListener orderErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            userOrdersListeners.onErrorResponse(error);
        }
    };

    @Override
    public void onErrorResponse(VolleyError error) {
        userListeners.onErrorResponse(error);
    }

    @Override
    public void onResponse(JSONArray response) {
        Type listType = new TypeToken<ArrayList<Usuario>>(){}.getType();
        List<Usuario> userList = new Gson().fromJson(response.toString(),listType);
        userListeners.onResponse(userList);
    }
}
