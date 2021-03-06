package com.example.personalproject.networking;

import android.util.Log;
import android.util.Xml;

import com.example.personalproject.combustible.Combustible;
import com.example.personalproject.combustible.RssFeedMic;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Random;

public class ParserXmlMic {
    private static final String ITEM = "item"; /* parent node */
    private static final String TITLE = "title";
    private static final String PUB_DATE = "pubDate";
    private static final String DESCRIPTION = "description";

    /***
     * This function reader the xml data in the string format and return a RssFeeMic with all
     * the data need.
     *
     * @param result String with the inputStream in xml.
     * @return RssFeeMic
     * @throws XmlPullParserException
     * @throws IOException
     */
    public RssFeedMic readEntry(String result) throws XmlPullParserException,
            IOException {
        //Used xml pullParse and not process Namespaces
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(new StringReader(result));
        parser.nextTag();

        while (parser.next() != XmlPullParser.END_DOCUMENT) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {
                //It's start_tag ;
                continue;
            }

            //Get the name of the tag
            String name = parser.getName();

            if (name.equals(ITEM)) {
                return readItem(parser);
            }

            parser.next();
        }

        return new RssFeedMic();
    }

    /***
     * Read the Item xml <Item>{content}<Item/>
     *
     * @param parser
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */

    private RssFeedMic readItem(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        String title = "";
        String pubDate = "";

        ArrayList<Combustible> combustibles = new ArrayList<>();

        RssFeedMic rssFeedItem = new RssFeedMic();

        int eventType = parser.getEventType();

        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (eventType != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            String text;

            if (name != null) {
                text = readText(parser);

                if (name.equalsIgnoreCase(TITLE)) {
                    title = text;

                } else if (name.equalsIgnoreCase(PUB_DATE)) {
                    pubDate = text;

                } else if (name.equalsIgnoreCase(DESCRIPTION)) {
                    combustibles = readDescriptionItem(text);

                }
            }
        }

        rssFeedItem.setTitle(title);
        rssFeedItem.setPublicationDate(pubDate);
        rssFeedItem.setCombustibles(combustibles);

        return rssFeedItem;
    }

    /**
     * For the tags title and summary, extracts their text values.
     *
     * @param parser
     * @return String
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readText(XmlPullParser parser) throws IOException,
            XmlPullParserException {
        String result = "";

        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }

        return result.trim();
    }

    /***
     * return a list of Combustible.
     *
     * @param text
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */

    private ArrayList<Combustible> readDescriptionItem(String text) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);

        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(new StringReader(text));

        ArrayList<Combustible> combustiblesList = new ArrayList<>();

        String temp = "";

        //Get all the String in the xml.
        while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
            if (xpp.getEventType() == XmlPullParser.TEXT) {
                temp += xpp.getText().trim() + "-";
            }

            xpp.next();
        }

        //TODO: remove

        Log.i("TEXT", temp);

        if (!temp.isEmpty()) {
            temp = temp.replaceAll("--", "-").replaceAll("RD$", "");

            String[] split = temp.split("-");

            int count = split.length;
            for (int i = 1; i < count; i += 2) {
                Combustible combustible = new Combustible();
                combustible.setDescription(split[i].replace(":",""));
                combustible.setPrice(getMoneyWithoutSpecialCharacter(split[i + 1]));

                //TODO: remove went you save the data.
                Random random = new Random();
                double number = random.nextInt(23 - (-7));

                //TODO: remove TAGS
                Log.i("TAG_Descrip", split[i]);
                Log.i("TAG_Price", split[i + 1]);
                Log.i("TAG_RandomNumber", String.valueOf(number));

                combustible.setLastPrice(combustible.getPrice() + number);

                //  Log.i("TAG_Price", String.valueOf(combustible.getLastPrice()));

                combustiblesList.add(combustible);
            }
        }

        return combustiblesList;
    }

    /***
     * Get the String price RD$ and remove the currency notation.
     *
     * @param value
     * @return
     * @throws NumberFormatException
     */
    private double getMoneyWithoutSpecialCharacter(String value) throws NumberFormatException {
        String moneyWithoutCharacter = new StringBuilder(value).replace(0, 3, "").toString();
        return Double.valueOf(moneyWithoutCharacter);
    }
}
