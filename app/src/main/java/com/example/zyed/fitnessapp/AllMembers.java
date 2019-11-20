package com.example.zyed.fitnessapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Filter;
import android.widget.Toast;
import android.widget.GridView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class AllMembers extends AppCompatActivity implements SearchView.OnQueryTextListener{

    Filter filterResult;
    CustomRecyclerAdapter customRecyclerAdapter;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    static ProgressDialog mProgressDialog;
    static boolean AllProgress = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_members);

        recyclerView =  findViewById(R.id.recycleViewContainerg);
        recyclerView.setHasFixedSize(true);
        //recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        layoutManager = new GridLayoutManager(this,2);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        new GenerateImageasync(customRecyclerAdapter).execute();


        /*customRecyclerAdapter = new CustomRecyclerAdapter(this, getMember());

        customRecyclerAdapter.setHasStableIds(true);

        recyclerView.setAdapter(customRecyclerAdapter);



        filterResult = customRecyclerAdapter.getFilter();*/

        //Define the gridView
        //GridView gridView = findViewById(R.id.allmembers_gridview);

        //final CustomAdapter adapter = new CustomAdapter(this,getMember());
        //gridView.setAdapter(adapter);

        //filterResult = adapter.getFilter();

        // Define the Toolbar as the new Actionbar
        Toolbar myToolbar = findViewById(R.id.allmembers_appbar);
        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        getSupportActionBar().setTitle("");

        // Enable the Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Change the back button on the action bar to a custom drawable
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home);

/*
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    CustomAdapter.test=false;
                } else {
                    CustomAdapter.test=true;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
*/


    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_all_members, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setQueryHint("Enter member name");
        searchView.setOnQueryTextListener(this);


        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) { //NOT CALLED WHILE showASAction="ifRoom"
                                                                        //CALLED WHILE showAsAction="ifRoom|collapseActionView"

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {

                return true;
            }
        });

        MenuItem actionMenuItem = menu.findItem(R.id.menu_item_all_members);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == android.R.id.home){ // MANAGE THE UP BAR (return to mainActivity)

           NavUtils.navigateUpFromSameTask(this);

        }

        if(item.getItemId() == R.id.menu_item_all_members){

            Intent toActiveMemebers = new Intent(AllMembers.this,ActiveMembers.class);
            startActivity(toActiveMemebers);
        }

        if(item.getItemId() == R.id.menu_search){

            //Managed by the CustomAdapter
        }

        if (item.getItemId() == R.id.menu_other){

            Intent toAttendanceList = new Intent(AllMembers.this, AttendanceList.class);
            startActivity(toAttendanceList);
        }

            return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        filterResult.filter(newText);

        return true;
    }


    private ArrayList<Member> getMember()
    {
        ArrayList<Member> memberArrayList=new ArrayList<Member>();
        List<Member> members = Member.listAll(Member.class);

        Member m;
        for(Member member : members)
        {
            Bitmap bmp = BitmapFactory.decodeByteArray(member.getImagev(), 0, member.getImagev().length);
            m=new Member(member.getName(), member.getEmail(), bmp, member.getClock());
            memberArrayList.add(m);
        }

        return memberArrayList;
    }

    public  class GenerateImageasync extends AsyncTask<Void, Void, CustomRecyclerAdapter> {

        CustomRecyclerAdapter customRecyclerAdapter;

        GenerateImageasync(CustomRecyclerAdapter customRecyclerAdapter){ this.customRecyclerAdapter = customRecyclerAdapter; }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ProgressDialog(AllMembers.this);
            mProgressDialog.setTitle("Loading full list...");
            mProgressDialog.setMessage("Please wait while we load and set the list.");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
            AllProgress = true;
        }

        @Override
        protected CustomRecyclerAdapter doInBackground(Void... voids) {

            customRecyclerAdapter = new CustomRecyclerAdapter(getApplicationContext(), getMember());

            return customRecyclerAdapter;

        }

        @Override
        protected void onPostExecute(CustomRecyclerAdapter customRecyclerAdapter) {
            super.onPostExecute(customRecyclerAdapter);

            customRecyclerAdapter.setHasStableIds(true);

            recyclerView.setAdapter(customRecyclerAdapter);

            filterResult = customRecyclerAdapter.getFilter();

            //mProgressDialog.dismiss();

        }
    }


}






