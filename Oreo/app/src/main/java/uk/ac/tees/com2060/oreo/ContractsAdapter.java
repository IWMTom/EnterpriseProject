package uk.ac.tees.com2060.oreo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.content.res.AppCompatResources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;

public class ContractsAdapter extends BaseAdapter {
    private Context mContext;


    ContractsAdapterListener mCallback;

    interface ContractsAdapterListener {
        void contractsAdapterListener(Bundle b);
    }

    private LayoutInflater mInflater;
    private ArrayList<Contract> mDataSource;

    public ContractsAdapter(Context context, ArrayList<Contract> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Object getItem(int i) {
        return mDataSource.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") View rowView = mInflater.inflate(R.layout.contract_list_view, viewGroup, false);

        final Contract contract = (Contract) getItem(i);

        TextView name = rowView.findViewById(R.id.contract_list_package_name);
        name.setText(contract.item_description);

        TextView status = rowView.findViewById(R.id.contract_list_package_status);

        if(contract.senderId == User.getUser().id()){

            if( !contract.collected){
                status.setText("Waiting for collection");
                status.setTextColor(ContextCompat.getColor(mContext, R.color.colorLightGrey));
            }else if( contract.collected){
                status.setText("Enroute to destination");
                status.setTextColor(ContextCompat.getColor(mContext, R.color.colorLightGrey));
            }else if( contract.delivered){
                status.setText("Please confirm delivery");
                status.setTextColor(ContextCompat.getColor(mContext, R.color.colorRed));
            }else if( contract.confirmed){
                status.setText("Delivered!");
                status.setTextColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                name.setTextColor(ContextCompat.getColor(mContext, R.color.colorGreen));
            }

        }else{

            if( !contract.collected){
                status.setText("Waiting for you to collect");
                status.setTextColor(ContextCompat.getColor(mContext, R.color.colorRed));
            }else if( contract.collected){
                status.setText("You have this item in possesion");
                status.setTextColor(ContextCompat.getColor(mContext, R.color.colorLightGrey));
            }else if( contract.delivered){
                status.setText("Waiting for sender to validate");
                status.setTextColor(ContextCompat.getColor(mContext, R.color.colorRed));
            }else if( contract.confirmed){
                status.setText("Job Complete!");
                status.setTextColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                name.setTextColor(ContextCompat.getColor(mContext, R.color.colorGreen));
            }

        }

        return rowView;
    }

    public void callbackToActivity(Bundle b) {
        mCallback = (ContractsAdapterListener) mContext;
        mCallback.contractsAdapterListener(b);
    }
}