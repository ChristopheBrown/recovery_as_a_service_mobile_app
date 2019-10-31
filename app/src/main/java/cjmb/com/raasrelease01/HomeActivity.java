package cjmb.com.raasrelease01;

/*This is the master Activity that holds all the fragments (or in other words, the pages/tabs you see
* when swiping left and right. Nothing much happens here but do not mix this up with HomeFragment.
*
* All Fragments in this project live within this Activity*/

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.google.gson.Gson;

import cjmb.com.raasrelease01.get.GetPersonalInformationResponse;

public class HomeActivity extends AppCompatActivity
            implements  HomeFragment.OnFragmentInteractionListener,
                        PlanFragment.OnFragmentInteractionListener,
                        WorkFragment.OnFragmentInteractionListener,
                        UserFragment.OnFragmentInteractionListener,
                        InfoFragment.OnFragmentInteractionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        welcomeUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWebexTeams();
//                onLunchAnotherApp();
//                startNewActivity();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
//        public void onFragmentInteraction(Uri uri);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = rootView = inflater.inflate(R.layout.fragment_home, container, false);

            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = HomeFragment.newInstance(null,null);
                    break;
                case 1:
                    fragment = PlanFragment.newInstance(null,null);
                    break;
                case 2:
                    fragment = WorkFragment.newInstance(null,null);
                    break;
                case 3:
                    fragment = UserFragment.newInstance(null,null);
                    break;
                case 4:
                    fragment = InfoFragment.newInstance(null,null);
                    break;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }
    }

    private void welcomeUser() {
        Context mContext = getApplicationContext();
        final GlobalValue globalVariable = (GlobalValue) mContext;

        RecoveryAsAServiceNetworkTransaction setName =
                new RecoveryAsAServiceNetworkTransaction(mContext,"/get/personalinformation",globalVariable.getUserNameGetRequest());

        setName.run(new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                GetPersonalInformationResponse getPersonalInformationResponse =
                        new Gson().fromJson(result,GetPersonalInformationResponse.class);

                globalVariable.setFirstName(getPersonalInformationResponse.getFirstName());
                globalVariable.setLastName(getPersonalInformationResponse.getLastName());
                globalVariable.setAddress(getPersonalInformationResponse.getAddress());
                globalVariable.setHeight((double)getPersonalInformationResponse.getHeight());
                globalVariable.setWeight((double)getPersonalInformationResponse.getWeight());
                globalVariable.setPhoneNumber((double)getPersonalInformationResponse.getPhone());

                new PopTart(findViewById(android.R.id.content),"Welcome back " + globalVariable.getFirstName() + "!");
//                WelcomeHeaderTextView.setText("Welcome back " + globalValue.getFirstName()+ "!");
            }
        });
    }


    public void openWebexTeams() {
        /* The envelope button visible on each tab of the app is the "messaging" interface. We
        * leverage Cisco webex teams for this*/
        Context context = getApplicationContext();
        String packageName = "com.cisco.wx2.android";

        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            // We found the activity now start the activity
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + packageName));
            context.startActivity(intent);
        }
    }
}
