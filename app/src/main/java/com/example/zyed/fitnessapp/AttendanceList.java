package com.example.zyed.fitnessapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Filter;
import android.widget.ImageView;

import com.orm.SugarContext;

import java.util.ArrayList;
import java.util.List;

public class AttendanceList extends AppCompatActivity implements SearchView.OnQueryTextListener {

    Filter filterResult ;
    Member m;

    RecyclerView.Adapter mAdapter;
    CustomRecyclerAdapter customRecyclerAdapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_list);

        SugarContext.init(this);

        RecyclerView recyclerView =  findViewById(R.id.recycleViewContainera);
        recyclerView.setHasFixedSize(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setNestedScrollingEnabled(false);

        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        customRecyclerAdapter = new CustomRecyclerAdapter(this, getMember());

        customRecyclerAdapter.setHasStableIds(true);

        recyclerView.setAdapter(customRecyclerAdapter);




        filterResult = customRecyclerAdapter.getFilter();



        //Define the ListView
        //ListView listView = findViewById(R.id.active_member_listview);
        //listView.setAdapter(new CustomAdapterForActiveMembers(this));
        //adapter = new CustomAdapter(this, getMember());


        //listView.setAdapter(adapter);


        //  filterResult = adapter.getFilter();


        // Define the Toolbar as the new Actionbar
        Toolbar myToolbar = findViewById(R.id.attendance_appbar);
        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        getSupportActionBar().setTitle("Attendance List");
        myToolbar.setTitleTextColor(this.getResources().getColor(R.color.black));

        // Enable the Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Change the back button on the action bar to a custom drawable
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);

        //
        getMenuInflater().inflate(R.menu.menu_attendance_list, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search_attendance);
        SearchView searchView = (SearchView) searchItem.getActionView();
        ImageView searchIcon = searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        searchIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_search));

        //Insert a Hint to the searchBar
        searchView.setQueryHint("Enter member name");
        searchView.setOnQueryTextListener(this  );


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

        MenuItem actionMenuItem = menu.findItem(R.id.menu_item_active_members);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == android.R.id.home) { // MANAGE THE UP BAR (return to MainActivity)

            Intent toActiveMember = new Intent(AttendanceList.this,ActiveMembers.class);
            startActivity(toActiveMember);

        }

        if (item.getItemId() == R.id.menu_search) {

            //Managed by the CustomAdapter
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

    private ArrayList<Member> getMember(){

        ArrayList<Member> memberArrayList=new ArrayList<Member>();
        List<Member> members = Member.listAll(Member.class);


        for(Member member : members)
        {
            if (member.getClock().equals("in")) {

                Bitmap bmp = BitmapFactory.decodeByteArray(member.getImagev(), 0, member.getImagev().length);
                m = new Member(member.getName(), member.getEmail(), bmp, member.getClock());
                memberArrayList.add(m);
            }
        }

        return memberArrayList;
    }
}
