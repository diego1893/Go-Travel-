package com.monsordi.gotravel.api;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.monsordi.gotravel.dto.PrestadorServicio;
import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by diego on 19/04/18.
 */

public class ServiceApi implements Response.ErrorListener, Response.Listener<JSONArray> {

    public static final String SERVICE = "prestadores/";

    private ServiceListeners serviceListeners;

    public ServiceApi(ServiceListeners serviceListeners) {
        this.serviceListeners = serviceListeners;
    }

    public interface ServiceListeners{
        void onErrorResponse(VolleyError error);
        void onResponse(List<PrestadorServicio> serviceList);
    }

    public void getServiceList(){
        String url = AppController.BASE_URL + SERVICE;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,url,null,this,this);
        AppController.getInstance().addToRequestQueue(request);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        serviceListeners.onErrorResponse(error);
    }

    @Override
    public void onResponse(JSONArray response) {
        Type listType = new TypeToken<ArrayList<PrestadorServicio>>(){}.getType();
        List<PrestadorServicio> serviceList = new Gson().fromJson(response.toString(),listType);
        serviceListeners.onResponse(serviceList);
    }
}
