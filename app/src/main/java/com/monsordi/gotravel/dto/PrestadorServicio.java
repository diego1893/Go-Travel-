package com.monsordi.gotravel.dto;

import java.io.Serializable;

public class PrestadorServicio implements Serializable {

	Long id;
	
	String name;
	
	String service;
	
	String imageUrl;

	public PrestadorServicio() {}

	public PrestadorServicio(Long id, String name, String service, String imageUrl) {
		this.id = id;
		this.name = name;
		this.service = service;
		this.imageUrl = imageUrl;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	

}
