package com.example.personalproject.rss;

import java.util.Calendar;
import java.util.List;

public class RssFeed {

	private String title,link;
	private Calendar pubDate;

	private List<RssItem> description;

	// private double gasolinaPremium, gasolinaRegular, gasoilPremium,
	// gasoilRegular, kerosene, glp, gnv;

	public final String getTitle() {
		return title;
	}

	public final void setTitle(String title) {
		this.title = title;
	}

	public final Calendar getPubDate() {
		return pubDate;
	}

	public final void setPubDate(Calendar pubDate) {
		this.pubDate = pubDate;
	}

	public final List<RssItem> getDescription() {
		return description;
	}

	public final void setDescription(List<RssItem> description) {
		this.description = description;
	}

	public RssFeed(String title, Calendar pubDate, List<RssItem> description,
			String link) {

		this.title = title;
		this.pubDate = pubDate;
		this.description = description;

	}

	public RssFeed() {

	}
}
