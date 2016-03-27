package ma.win.mewdo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements HomeFragment.OnFragmentInteractionListener,SettingsFragment.OnFragmentInteractionListener {

    ListView mDrawerList;

    RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    CharSequence mTitle = "";

    SharedPreferences sharedPreferences;

    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    TextView profileName;
    ImageView profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profileName = (TextView) findViewById(R.id.userName);
        profilePicture = (ImageView) findViewById(R.id.avatar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);

        mNavItems.add(new NavItem("Home", R.drawable.home));
        mNavItems.add(new NavItem("My mewdos", R.drawable.chat));
        mNavItems.add(new NavItem("Settings", R.drawable.settings));
        mNavItems.add(new NavItem("Logout", R.drawable.logout));


        // DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        // Populate the Navigtion Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setDivider(null);

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mTitle = getTitle();


        // sharedPreferences getting the profile
        sharedPreferences = getSharedPreferences("PROFILE",MODE_PRIVATE);
        String profilePictureURL = sharedPreferences.getString("PICTURE","");
        String profileName = sharedPreferences.getString("NAME", "Joe Deeper");

        changeProfile(profileName,profilePictureURL);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainContent,new HomeFragment()).commit();


    }

    public  void changeProfile(String name,String picture){
        profileName.setText(name);
        Picasso.with(this.getApplicationContext())
                .load(picture)
                .resize(50, 50).transform(new RoundImage())
                .centerCrop()
                .into(profilePicture);

    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Called when invalidateOptionsMenu() is invoked
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        //boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
    }


    private void selectItemFromDrawer(int position) {

        FragmentManager fragmentManager = getSupportFragmentManager();


        switch (position){
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.mainContent, new HomeFragment())
                        .commit();
                break;
            case 2:
                fragmentManager.beginTransaction()
                        .replace(R.id.mainContent, new SettingsFragment())
                        .commit();
                break;
            case 3:
                logOut();
                break;
            default:
                break;
        }

        mDrawerList.setItemChecked(position, true);
        setTitle(mNavItems.get(position).mTitle);

        // Close the drawer
        mDrawerLayout.closeDrawer(mDrawerPane);
    }


    public  void logOut(){
        LoginManager.getInstance().logOut();
        Intent loginIntent = new Intent(this.getApplicationContext(),Login.class);
        getSharedPreferences("PROFILE", MODE_PRIVATE).edit().clear();
        startActivity(loginIntent);
    }



    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle
        // If it returns true, then it has handled
        // the nav drawer indicator touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed(){

        final Activity main = this;
        new AlertDialog.Builder(this)
                .setTitle("Leave Mewdo")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                       logOut();
                    }
                }).create().show();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    class NavItem {
        String mTitle;

        int mIcon;

        public NavItem(String title, int icon) {
            mTitle = title;

            mIcon = icon;
        }
    }

}
