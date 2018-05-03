package uk.ac.tees.com2060.oreo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class RatingsAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Rating> mDataSource;

    public RatingsAdapter(Context context, ArrayList items) {
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

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView;
        rowView = mInflater.inflate(R.layout.fragment_rating_view, viewGroup, false);

        Rating rating = (Rating) getItem(i);

        CircleImageView profilePic = rowView.findViewById(R.id.ImageView_rating_profile);
        TextView message = rowView.findViewById(R.id.rating_message);
        TextView type = rowView.findViewById(R.id.rating_type);

        Picasso.get().load("https://getshipr.com/api/user/" + rating.raterId() + "/photo").placeholder(R.drawable.default_profile_photo).into(profilePic);

        type.setText(rating.type().toUpperCase());

        if (rating.type.equals("good")) {
            type.setTextColor(ContextCompat.getColor(mContext, R.color.colorGreen));
        } else {
            type.setTextColor(ContextCompat.getColor(mContext, R.color.colorRed));
        }

        message.setText(rating.message);

        return rowView;
    }
}