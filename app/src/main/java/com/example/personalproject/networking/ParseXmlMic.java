package com.example.personalproject.networking;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;
import android.util.Xml;

import com.example.personalproject.combustible.Combustible;
import com.example.personalproject.combustible.RssFeedMic;

public class ParseXmlMic {
    private static final String NAMESPACE = "rssversion";


    private static final String ITEM = "item"; /* parent node */
    private static final String TITLE = "title";
    private static final String PUB_DATE = "pubDate";
    private static final String DESCRIPTION = "description";


    //TODO: this block now is never used
    private static final String GASOLINE_P = "gas95";
    private static final String GASOLINE_R = "gas89";
    private static final String DIESEL_P = "gasoilp";
    private static final String DIESEL_R = "gasoilr";
    private static final String KEROSENE = "kerosene";
    private static final String GLP = "glp";
    private static final String GNV = "gnv";
    private static final String NS = null;


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

        List<Combustible> combustibles = new ArrayList<Combustible>();

        RssFeedMic rssFeedItem = new RssFeedMic();

        int eventType = parser.getEventType();

        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (eventType != XmlPullParser.START_TAG) {
                continue;
            }

            //TODO: set Description for parse no raw data.
            String name = parser.getName();
            String text = null;

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

    /***
     *
     * @param parser
     * @throws XmlPullParserException
     * @throws IOException
     */

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

    private List<Combustible> readDescriptionItem(String text) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);

        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(new StringReader(text));

        List<Combustible> combustiblesList = new ArrayList<Combustible>();

        String temp = "";

        //Get all the String in the xml.
        while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
            System.out.println("eventType INSIDE: " + xpp.getEventType());

            if (xpp.getEventType() == XmlPullParser.TEXT) {
                temp += xpp.getText().trim() + "-";
            }

            xpp.next();
        }

        if (!temp.isEmpty()) {
            temp = temp.replaceAll("--", "-").replaceAll("RD$", "");

            String[] split = temp.split("-");

            for (int i = 1; i < split.length; i += 2) {
                Combustible combustible = new Combustible();
                combustible.setDescription(split[i]);
                combustible.setPrice(getMoneyWithoutSpecialCharacter(split[i + 1]));

                //TODO: remove went you save the data.
                Random random  = new Random();
                double number = random.nextInt(23 -(- 7));

                Log.i("TAG_Number", String.valueOf(number));

               // (Math.nextI * 4)

                combustible.setLastPrice(combustible.getPrice() + number);

                Log.i("TAG_Price", String.valueOf(combustible.getLastPrice()));

                combustiblesList.add(combustible);
            }
        }

        return combustiblesList;
    }
    
     private double getMoneyWithoutSpecialCharacter(String value) throws NumberFormatException{
         StringBuilder builder = new StringBuilder(value);
         builder.replace(0, 3, "");

         return Double.valueOf(builder.toString());
     }


    //TODO: second method to parse the data from description.
    /*private RssFeedMic readEntry(XmlPullParser parser)
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
    } */
}
