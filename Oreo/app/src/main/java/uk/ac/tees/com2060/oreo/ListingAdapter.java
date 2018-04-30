package uk.ac.tees.com2060.oreo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListingAdapter extends BaseAdapter
{
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList[] mDataSource;

    public ListingAdapter(Context context, ArrayList[] items)
    {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return mDataSource.size();
    }

    @Override
    public Object getItem(int i)
    {
        return  mDataSource.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        View rowView = mInflater.inflate(R.layout.fragment_dashboard_listview, viewGroup, false);

        TextView listingListDescription = rowView.findViewById(R.id.bid_list_user);
        TextView listingListToFrom = rowView.findViewById(R.id.listing_list_to_from);
        TextView listingListSize = rowView.findViewById(R.id.bid_list_amount);

        Listing listing = (Listing) getItem(i);

        listingListDescription.setText(listing.itemDescription());
        listingListToFrom.setText(listing.collectionCity() + " -> " + listing.deliveryCity() + " (" + listing.distance() + " miles)");
        listingListSize.setText(listing.itemSize());

        return rowView;
    }
}