package com.example.personalproject.combustible;

import java.util.ArrayList;
import java.util.List;

public class RssFeedMic {
	//private static final long serialVersionUID = 1L;

	private String title, publicationDate;

	private List<Combustible> combustibles = new ArrayList<Combustible>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(String publicationDate) {
		this.publicationDate = publicationDate;
	}

	public List<Combustible> getCombustibles() {
		return combustibles;
	}

	public void setCombustibles(List<Combustible> combustibles) {
		this.combustibles = combustibles;
	}

}
