package com.monsordi.gotravel.dto;

import java.io.Serializable;

public class Status implements Serializable {

	Long id;
	
	String status_string;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatus_string() {
		return status_string;
	}

	public void setStatus_string(String status_string) {
		this.status_string = status_string;
	}

	

}
