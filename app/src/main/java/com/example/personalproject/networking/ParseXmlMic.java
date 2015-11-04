package com.example.personalproject.networking;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Xml;

import com.example.personalproject.combustible.Combustible;
import com.example.personalproject.combustible.RssFeedMic;

public class ParseXmlMic {

	// XML node keys
	static final String ITEM = "item"; /* parent node */
	static final String TITLE = "title";
	static final String PUB_DATE = "pubDate";
	static final String GASOLINE_P = "gas95";
	static final String GASOLINE_R = "gas89";
	static final String DIESEL_P = "gasoilp";
	static final String DIESEL_R = "gasoilr";
	static final String KEROSENE = "kerosene";
	static final String GLP = "glp";
	static final String GNV = "gnv";

	public RssFeedMic readEntry(String result) throws XmlPullParserException,
			IOException {
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);

		XmlPullParser parser = factory.newPullParser();
		// parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
		parser.setInput(new StringReader(result));
		parser.nextTag();

		while (parser.next() != XmlPullParser.END_DOCUMENT) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}

			String name = parser.getName();
			if (name.equals(ITEM)) {
				return readEntry(parser);
			}

			parser.next();
		}

		return new RssFeedMic();

		// return readFeed(parser);
	}

	private RssFeedMic readFeed(XmlPullParser parser)
			throws XmlPullParserException, IOException {

		while (parser.next() != XmlPullParser.END_DOCUMENT) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}

			String name = parser.getName();
			if (name.equals(ITEM)) {
				return readEntry(parser);
			}

			parser.next();
		}

		return new RssFeedMic();
	}

	private RssFeedMic readEntry(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		String title = "";
		String pubDate = "";

		List<Combustible> combustibles = new ArrayList<Combustible>();

		RssFeedMic rssFeedItem = new RssFeedMic();

		int eventType = parser.getEventType();

		while (parser.next() != XmlPullParser.END_DOCUMENT) {
			if (eventType != XmlPullParser.START_TAG) {
				continue;
			}

			String name = parser.getName();
			String text = null;

			if (name != null) {
				text = readText(parser);
				Combustible combustible = new Combustible();

				if (name.equalsIgnoreCase(TITLE)) {
					title = text;

				} else if (name.equalsIgnoreCase(PUB_DATE)) {
					pubDate = text;

				} else if (name.equalsIgnoreCase(GASOLINE_P)) {
					combustible.setCode(name);
					combustible.setPrice(Double.valueOf(text));
					combustible.setDescription("Gasolina Premium");

				} else if (name.equalsIgnoreCase(GASOLINE_R)) {
					combustible.setCode(name);
					combustible.setPrice(Double.valueOf(text));
					combustible.setDescription("Gasolina Regular");

				} else if (name.equalsIgnoreCase(DIESEL_P)) {
					combustible.setCode(name);
					combustible.setPrice(Double.valueOf(text));
					combustible.setDescription("Gasoil Premium");

				} else if (name.equalsIgnoreCase(DIESEL_R)) {
					combustible.setCode(name);
					combustible.setPrice(Double.valueOf(text));
					combustible.setDescription("Gasoil Regular");

				} else if (name.equalsIgnoreCase(KEROSENE)) {
					combustible.setCode(name);
					combustible.setPrice(Double.valueOf(text));
					combustible.setDescription("Kerosene");

				} else if (name.equalsIgnoreCase(GLP)) {
					combustible.setCode(name);
					combustible.setPrice(Double.valueOf(text));
					combustible.setDescription("Gas Licuado de Petróleo (GLP)");

				} else if (name.equalsIgnoreCase(GNV)) {
					combustible.setCode(name);
					combustible.setPrice(Double.valueOf(text));
					combustible.setDescription("Gas Natural Vehicular (GNV)");
				}

				if (combustible.getCode() != null) {

					combustibles.add(combustible);
				}
			}
		}

		rssFeedItem.setTitle(title);
		rssFeedItem.setPublicationDate(pubDate);
		rssFeedItem.setCombustibles(combustibles);

		return rssFeedItem;
	}

	private void skipTag(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
			case XmlPullParser.END_TAG:
				depth--;
				break;
			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}
	}

	// For the tags title and summary, extracts their text values.
	private String readText(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}

		return result;
	}
}
