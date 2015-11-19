package com.example.personalproject.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.personalproject.activitys.FuelPriceActivity;
import com.example.personalproject.R;
import com.example.personalproject.combustible.Combustible;
import com.example.personalproject.combustible.RssFeedMic;

public class CustomFuelArrayAdapter extends BaseAdapter {
    // Activity context
    private final Activity context;

    private static LayoutInflater inflater = null;

    RssFeedMic tempValues = null;

    // List of items that are shown in the assistance list view.
    private final ArrayList<Combustible> adapterList;

    public CustomFuelArrayAdapter(Activity context,
                                  ArrayList<Combustible> adapterList, Resources resLocal) {
        this.context = context;
        this.adapterList = adapterList;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {

        return adapterList.size();
    }

    @Override
    public Object getItem(int position) {
        return adapterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (convertView == null) {
            // inflate list_row file for each row
            view = inflater.inflate(R.layout.list_row, null);

            // View Holder item to content
            holder = new ViewHolder();
            holder.image = (ImageView) view.findViewById(R.id.list_image);
            holder.price = (TextView) view.findViewById(R.id.curren_price);
            holder.description = (TextView) view
                    .findViewById(R.id.description_title);
            holder.differencePrice = (TextView) view
                    .findViewById(R.id.diference_price);

            // set the holder with LayoutInflater
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // Set Model values in Holder elements
        Combustible combustible =  adapterList.get(position);

        holder.price.setText(setNumberFormatToShow(combustible.getPrice()));

        holder.description.setText(combustible.getDescription());

        holder.differencePrice
                .setText(setNumberFormatToShow(combustible.getLastPrice()));

        holder.image.setImageResource(getCorrespondingImage(combustible));

        // holder.image.setImageBitmap(getRoundedCornerBitmap(getCorrespondingImage(combustible)));

        // set item click listener

        view.setOnClickListener(new OnItemClickListener(position));

        return view;
    }


    //TODO: check this.
    private static String setNumberFormatToShow(double money){
        String moneySymbol = "RD$";
        String numberFormatted = "";

        String number = String.valueOf(money);

        if(number.contains(".")){
            String [] numberSplit = number.split("\\.");
            String decimalDigit = numberSplit[1];

            if(decimalDigit.length() == 1){
                decimalDigit += "0";

                numberFormatted = numberSplit[0] + "." + decimalDigit;
            }

        }else {

            numberFormatted = number + ".00";
        }

        return moneySymbol + numberFormatted;
    }

    // private
    // byte[] decodedString =
    // Base64.decode(person_object.getPhoto(),Base64.NO_WRAP);
    // InputStream inputStream = new ByteArrayInputStream(decodedString);
    // Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
    // user_image.setImageBitmap(bitmap);

    private int getCorrespondingImage(Combustible combustible) {
        int imageById = 0;

        double lastPrice = combustible.getDescription() != null ? 0.00
                : combustible.getLastPrice();

        double currentPrice = combustible.getPrice();

        if (currentPrice > lastPrice) {
            // price up
            imageById = R.drawable.arrow_up_red;

        } else if (currentPrice == lastPrice) {
            // price the same
            imageById = R.drawable.arrow_ecuals_yellow;

        } else {
            // price down
            imageById = R.drawable.arrow_down_green;
        }

        return imageById;
    }

    public int getDiferencePrice(Combustible combustible) {
        return (int) (combustible.getPrice() - combustible.getLastPrice());
    }

    public Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 12;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /*********
     * Create a holder Class to contain inflated xml file elements
     *********/
    public static class ViewHolder {

        public TextView price;
        public TextView differencePrice;
        public TextView description;
        public ImageView image;

    }

    private class OnItemClickListener implements OnClickListener {
        int position;

        public OnItemClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            FuelPriceActivity fuelPrice = (FuelPriceActivity) context;

            /****
             * Call onItemClick Method inside CustomListViewAndroidExample Class
             * ( See Below )
             ****/

            fuelPrice.onItemClick(position);

        }

    }

}
