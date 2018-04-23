package com.monsordi.gotravel.api;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.monsordi.gotravel.dto.Encargado;
import com.monsordi.gotravel.dto.Orden;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by diego on 19/04/18.
 */

public class AttendantApi implements Response.Listener<JSONArray>, Response.ErrorListener {

    public static final String ENCARGADO = "encargado/";

    private AttendantApi.AttendantListeners attendantListeners;
    private AttendantOrdersListeners attendantOrdersListeners;

    public AttendantApi(AttendantApi.AttendantListeners attendantListeners) {
        this.attendantListeners = attendantListeners;
    }

    public AttendantApi(AttendantOrdersListeners attendantOrdersListeners){
        this.attendantOrdersListeners = attendantOrdersListeners;
    }

    public interface AttendantListeners{
        void onErrorResponse(VolleyError error);
        void onResponse(List<Encargado> attendantList);
    }

    public interface AttendantOrdersListeners{
        void onErrorResponse(VolleyError error);
        void onResponse(List<Orden> orderList);
    }

    public void getAttendantList(){
        String url = AppController.BASE_URL + ENCARGADO;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,url,null,this,this);
        AppController.getInstance().addToRequestQueue(request);
    }

    public void getOrdersById(Long idOrder,String token){
        String url = AppController.BASE_URL + ENCARGADO + idOrder + "/ordenes?token=" + token;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,url,null,orderResponseListener,orderErrorListener);
        AppController.getInstance().addToRequestQueue(request);
    }

    Response.Listener<JSONArray> orderResponseListener = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {
            Type listType = new TypeToken<ArrayList<Orden>>(){}.getType();
            List<Orden> ordersList = new Gson().fromJson(response.toString(),listType);
            attendantOrdersListeners.onResponse(ordersList);
        }
    };

    Response.ErrorListener orderErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            attendantOrdersListeners.onErrorResponse(error);
        }
    };

    public void onErrorResponse(VolleyError error) {
        attendantListeners.onErrorResponse(error);
    }

    @Override
    public void onResponse(JSONArray response) {
        Type listType = new TypeToken<ArrayList<Encargado>>(){}.getType();
        List<Encargado> attendantList = new Gson().fromJson(response.toString(),listType);
        attendantListeners.onResponse(attendantList);
    }
}
