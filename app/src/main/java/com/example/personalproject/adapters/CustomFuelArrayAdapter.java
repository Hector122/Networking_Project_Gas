package com.example.personalproject.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.personalproject.R;
import com.example.personalproject.activitys.FuelPriceActivity;
import com.example.personalproject.combustible.Combustible;

import java.util.ArrayList;

public class CustomFuelArrayAdapter extends RecyclerView.Adapter<CustomFuelArrayAdapter.CombustibleViewHolder> {
    // Activity context
    private final Activity mContext;
    // List of items that are shown in the assistance list view.
    private final ArrayList<Combustible> mAdapterList;


    public CustomFuelArrayAdapter(Activity context,
                                  ArrayList<Combustible> mAdapterList) {
        this.mContext = context;
        this.mAdapterList = mAdapterList;
    }


    @Override
    public void onBindViewHolder(CombustibleViewHolder holder, int position) {
        Combustible combustible = mAdapterList.get(position);

        holder.description.setText(combustible.getDescription());
        holder.price.setText(setNumberFormatToShow(combustible.getPrice()));
        holder.differencePrice.setText(setNumberFormatToShow(combustible.getLastPrice()));
        holder.image.setImageResource(getCorrespondingImage(combustible));
    }


    @Override
    public CombustibleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fuel_list_row, viewGroup, false);
        CombustibleViewHolder viewHolder = new CombustibleViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return mAdapterList.size();
    }

    //TODO: check this.
    private static String setNumberFormatToShow(double money) {
        String moneySymbol = "RD$";
        String numberFormatted = "";

        String number = String.valueOf(money);

        if (number.contains(".")) {
            String[] numberSplit = number.split("\\.");
            String decimalDigit = numberSplit[1];

            if (decimalDigit.length() < 2) {
                decimalDigit += "0";

                numberFormatted = numberSplit[0] + "." + decimalDigit;
            } else if (decimalDigit.length() > 2) {
                decimalDigit = decimalDigit.substring(0, 2);

                numberFormatted = numberSplit[0] + "." + decimalDigit;
            }

        } else {
            numberFormatted = number + ".00";
        }

        return moneySymbol + numberFormatted;
    }

    /**
     * @param combustible
     * @return
     */
    private int getCorrespondingImage(Combustible combustible) {
        int imageById = 0;

        double lastPrice = combustible.getLastPrice();

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

    public double getDifferencePrice(Combustible combustible) {
        return (combustible.getPrice() - combustible.getLastPrice());
    }


    /*********
     * Create a holder Class to contain inflated xml file elements
     *********/
    public static class CombustibleViewHolder extends RecyclerView.ViewHolder {
        protected TextView price;
        protected TextView differencePrice;
        protected TextView description;
        protected ImageView image;

        CombustibleViewHolder(View view) {
            super(view);

            image = (ImageView) view.findViewById(R.id.list_image);
            price = (TextView) view.findViewById(R.id.curren_price);
            description = (TextView) view.findViewById(R.id.description_title);
            differencePrice = (TextView) view.findViewById(R.id.difference_price_value);
        }
    }

    /**
     *
     */
    private class OnItemClickListener implements OnClickListener {
        int position;

        public OnItemClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            FuelPriceActivity fuelPrice = (FuelPriceActivity) mContext;

            /****
             * Call onItemClick Method inside CustomListViewAndroidExample Class
             * ( See Below )
             ****/

            fuelPrice.onItemClick(position);

        }
    }
}
