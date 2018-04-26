package uk.ac.tees.com2060.oreo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;

import uk.ac.tees.com2060.oreo.ApiCallLib.ApiCall;
import uk.ac.tees.com2060.oreo.ApiCallLib.ApiResponse;
import uk.ac.tees.com2060.oreo.ApiCallLib.ResponseListener;

public class BidsAdapter extends BaseAdapter
{
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Bid> mDataSource;

    public BidsAdapter(Context context, ArrayList<Bid> items)
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
        View rowView = mInflater.inflate(R.layout.fragment_listing_detail_listview, viewGroup, false);

        final ImageView bidThumbnail = rowView.findViewById(R.id.bid_list_thumbnail);
        TextView bidUser = rowView.findViewById(R.id.bid_list_user);
        TextView bidMessage = rowView.findViewById(R.id.bid_list_message);
        TextView bidAmount = rowView.findViewById(R.id.bid_list_amount);

        Bid bid = (Bid) getItem(i);

        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        Picasso.get().load(mContext.getString(R.string.API_BASE_URL) + "user/" + bid.user_id() + "/photo").into(bidThumbnail);

        bidUser.setText(bid.userName());
        bidMessage.setText(bid.message());
        bidAmount.setText(formatter.format(bid.amount()));

        return rowView;
    }
}