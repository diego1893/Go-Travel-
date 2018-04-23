package com.monsordi.gotravel.dto;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Timestamp;

public class Token implements Serializable {

	Long id;
	
	String value;
	
	Timestamp creado;
	
	Timestamp expira;

	String rol;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Timestamp getCreado() {
		return creado;
	}

	public void setCreado(Timestamp creado) {
		this.creado = creado;
	}

	public Timestamp getExpira() {
		return expira;
	}

	public void setExpira(Timestamp expira) {
		this.expira = expira;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}
	
}
