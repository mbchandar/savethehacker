package com.zapota.socialatm.model;

import java.util.ArrayList;

public class Bank {
	private String title, name, iconUrl, logoUrl;		
	private String bankType;
	
	public Bank() {
	}

	public Bank(String name, String logoUrl) {
		this.title = name;		
		this.logoUrl = logoUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String name) {
		this.title = name;
	}
	
	public String getLogoUrl() {
		return logoUrl;
	}

	public String setLogoUrl(String url){
		return this.logoUrl = url;
	}

	
	public String getIconUrl() {
		return iconUrl;
	}

	public String setIconUrl(String url){
		return this.iconUrl = url;
	}
}
