package uk.ac.tees.com2060.oreo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;

public class BidsAdapter extends BaseAdapter {
    private Context mContext;


    BidsAdapterListener mCallback;

    interface BidsAdapterListener {
        void bidsAdapterListener(Bundle b);
    }

    private LayoutInflater mInflater;
    private ArrayList<Bid> mDataSource;
    private Listing listing;

    public BidsAdapter(Context context, ArrayList<Bid> items, Listing listing) {
        mContext = context;
        mDataSource = items;
        this.listing = listing;
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
        @SuppressLint("ViewHolder") View rowView = mInflater.inflate(R.layout.fragment_listing_detail_listview, viewGroup, false);

        final Bid bid = (Bid) getItem(i);

        final ImageView bidThumbnail = rowView.findViewById(R.id.ImageView_rating_profile);
        bidThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putInt("userid",bid.user_id());
                if(listing.user_id()==User.getUser().id()){
                    b.putInt("bid", bid.id());
                }
                callbackToActivity(b);
            }
        });

        TextView bidUser = rowView.findViewById(R.id.bid_list_user);

        TextView bidMessage = rowView.findViewById(R.id.bid_list_message);

        TextView bidAmount = rowView.findViewById(R.id.bid_list_amount);

        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        Picasso.get().load(mContext.getString(R.string.API_BASE_URL) + "user/" + bid.user_id() + "/photo").placeholder(R.drawable.default_profile_photo).into(bidThumbnail);

        bidUser.setText(bid.userName());
        bidMessage.setText((bid.message().equals("null") ? "" : bid.message()));
        bidAmount.setText(formatter.format(bid.amount()));

        return rowView;
    }

    public void callbackToActivity(Bundle b) {
        mCallback = (BidsAdapterListener) mContext;
        mCallback.bidsAdapterListener(b);
    }
}