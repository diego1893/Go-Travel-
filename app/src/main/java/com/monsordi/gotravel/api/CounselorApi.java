package com.monsordi.gotravel.api;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.monsordi.gotravel.dto.Asesor;
import com.monsordi.gotravel.dto.Orden;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by diego on 19/04/18.
 */

public class CounselorApi implements Response.ErrorListener, Response.Listener<JSONArray> {

    public static final String COUNSELOR = "asesor/";

    private CounselorApi.CounselorListeners counselorListeners;
    private CounselorOrdersListeners counselorOrdersListeners;

    public CounselorApi(CounselorApi.CounselorListeners counselorListeners) {
        this.counselorListeners = counselorListeners;
    }

    public CounselorApi(CounselorOrdersListeners counselorOrdersListeners) {
        this.counselorOrdersListeners = counselorOrdersListeners;
    }

    public interface CounselorListeners{
        void onErrorResponse(VolleyError error);
        void onResponse(List<Asesor> counselorList);
    }

    public interface CounselorOrdersListeners {
        void onErrorResponse(VolleyError error);
        void onResponse(List<Orden> orderList);
    }

    public void getCounselorList(){
        String url = AppController.BASE_URL + COUNSELOR;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,url,null,this,this);
        AppController.getInstance().addToRequestQueue(request);
    }

    public void getOrdersById(Long idOrder,String token){
        String url = AppController.BASE_URL + COUNSELOR + idOrder + "/ordenes?token=" + token;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,url,null,orderResponseListener,orderErrorListener);
        AppController.getInstance().addToRequestQueue(request);
    }

    Response.Listener<JSONArray> orderResponseListener = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {
            Type listType = new TypeToken<ArrayList<Orden>>(){}.getType();
            List<Orden> ordersList = new Gson().fromJson(response.toString(),listType);
            counselorOrdersListeners.onResponse(ordersList);
        }
    };

    Response.ErrorListener orderErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            counselorOrdersListeners.onErrorResponse(error);
        }
    };


    @Override
    public void onErrorResponse(VolleyError error) {
        counselorListeners.onErrorResponse(error);
    }

    @Override
    public void onResponse(JSONArray response) {
        Type listType = new TypeToken<ArrayList<Asesor>>(){}.getType();
        List<Asesor> counselorList = new Gson().fromJson(response.toString(),listType);
        counselorListeners.onResponse(counselorList);
    }
}
