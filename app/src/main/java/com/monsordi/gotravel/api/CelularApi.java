package com.monsordi.gotravel.api;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.monsordi.gotravel.dto.Celular;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by diego on 19/04/18.
 */

public class CelularApi implements Response.ErrorListener, Response.Listener<JSONArray> {

    public static final String CELULAR = "celular/";

    private CelularListeners celularListeners;

    public CelularApi(CelularListeners celularListeners) {
        this.celularListeners = celularListeners;
    }

    public interface CelularListeners{
        void onErrorResponse(VolleyError error);
        void onResponse(List<Celular> celularList);
    }

    public void getCelularList(){
        String url = AppController.BASE_URL + CELULAR;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,url,null,this,this);
        AppController.getInstance().addToRequestQueue(request);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        celularListeners.onErrorResponse(error);
    }

    @Override
    public void onResponse(JSONArray response) {
        Type listType = new TypeToken<ArrayList<Celular>>(){}.getType();
        List<Celular> celularList = new Gson().fromJson(response.toString(),listType);
        celularListeners.onResponse(celularList);
    }
}
