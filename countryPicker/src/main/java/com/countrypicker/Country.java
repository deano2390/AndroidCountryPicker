package com.countrypicker;

/**
 * POJO
 *
 */
public class Country {
	private String name;
	private String code;

	public String getIso() {
		return iso;
	}

	private String iso;

	public Country(String name, String code, String iso) {
		this.name = name;
		this.code = code;
		this.iso = iso;
	}

	public Country() {

	}

	public String getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}