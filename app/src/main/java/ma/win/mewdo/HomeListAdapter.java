package ma.win.mewdo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by AmineBenkeroum on 1/31/2016.
 */
public class HomeListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<HomeFragment.NavItem> mNavItems;

    public HomeListAdapter(Context context, ArrayList<HomeFragment.NavItem> navItems) {
        mContext = context;
        mNavItems = navItems;
    }

    @Override
    public int getCount() {
        return mNavItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mNavItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.home_item, null);
        }
        else {
            view = convertView;
        }

        TextView titleView = (TextView) view.findViewById(R.id.title);
        TextView subtitleView = (TextView) view.findViewById(R.id.subTitle);
        ImageView iconView = (ImageView) view.findViewById(R.id.icon);

        titleView.setText( mNavItems.get(position).mTitle );
        subtitleView.setText(mNavItems.get(position).mSubtitle);
        Picasso.with(view.getContext())
                .load(mNavItems.get(position).profilePicture)
                .resize(100, 100).transform(new RoundImage())
                .centerCrop()
                .into(iconView);

        return view;
    }
}
