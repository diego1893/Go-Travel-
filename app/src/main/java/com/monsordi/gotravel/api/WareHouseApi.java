package com.monsordi.gotravel.api;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.monsordi.gotravel.dto.Almacen;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by diego on 19/04/18.
 */

public class WareHouseApi implements Response.ErrorListener, Response.Listener<JSONArray> {

    public static final String WAREHOUSE = "almacen/";

    private WareHouseListeners wareHouseListeners;

    public WareHouseApi(WareHouseListeners wareHouseListeners) {
        this.wareHouseListeners = wareHouseListeners;
    }

    public interface WareHouseListeners{
        void onErrorResponse(VolleyError error);
        void onResponse(List<Almacen> warehouseList);
    }

    public void getWareHouseList(){
        String url = AppController.BASE_URL + WAREHOUSE;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,url,null,this,this);
        AppController.getInstance().addToRequestQueue(request);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        wareHouseListeners.onErrorResponse(error);
    }

    @Override
    public void onResponse(JSONArray response) {
        Type listType = new TypeToken<ArrayList<Almacen>>(){}.getType();
        List<Almacen> warehouseList = new Gson().fromJson(response.toString(),listType);
        wareHouseListeners.onResponse(warehouseList);
    }
}
