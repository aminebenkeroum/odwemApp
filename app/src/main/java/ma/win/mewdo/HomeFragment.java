package ma.win.mewdo;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {



    SwipeMenuListView homeList;
    ArrayList<NavItem> homeNavItems = new ArrayList<NavItem>();
    HomeListAdapter homeListAdapter;

    private OnFragmentInteractionListener mListener;

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // populating home
        String url = "https://d8142femxnlg1.cloudfront.net/cropped-profile-photos/6fe8b15f1239fbf813a150c85328e11980f5af40-s300.jpeg";
        homeNavItems.add(new NavItem("Yassine serhane","Something there is wow hehe",url));
        homeNavItems.add(new NavItem("Youssef Azhari","Something there is wow hehe",url));
        homeNavItems.add(new NavItem("Faical Guenni","Something there is wow hehe",url));
        homeNavItems.add(new NavItem("Leila Benkirane","Something there is wow hehe",url));
        homeNavItems.add(new NavItem("Leila Benkirane","Something there is wow hehe",url));
        homeNavItems.add(new NavItem("Leila Benkirane","Something there is wow hehe",url));
        homeNavItems.add(new NavItem("Leila Benkirane","Something there is wow hehe",url));

        homeListAdapter = new HomeListAdapter(this.getContext(),homeNavItems);


        // fin pop
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem requestItem = new SwipeMenuItem(
                        getContext());
                // set item background
                requestItem.setBackground(new ColorDrawable(Color.rgb(46, 204, 113)));
                // set item width
                requestItem.setWidth(400);
                // set item title
                requestItem.setTitle("Send Mewdo Request");
                // set item title fontsize
                requestItem.setTitleSize(16);

                // set item title font color
                requestItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(requestItem);
            }
        };

        homeList = (SwipeMenuListView) view.findViewById(R.id.feedList);
         homeList.setMenuCreator(creator);
        homeList.setAdapter(homeListAdapter);
        homeList.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


    class NavItem {
        String mTitle;
        String mSubtitle;
        String profilePicture;
        int categoryIcon;

        public NavItem(String title, String subtitle, String profile) {
            mTitle = title;
            mSubtitle = subtitle;
            profilePicture = profile;
        }
    }

}
