package com.example.zyed.fitnessapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orm.SugarContext;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActiveMembers extends AppCompatActivity implements SearchView.OnQueryTextListener{

    Filter filterResult ;
    Member m;

    //RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    CustomRecyclerAdapter customRecyclerAdapter;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    List<Member> personUtilsList;
    static ProgressDialog mProgressDialog;
    static boolean ActiveProgress=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_members);

        // Define the Toolbar as the new Actionbar
        Toolbar myToolbar = findViewById(R.id.activemembers_appbar);
        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        getSupportActionBar().setTitle("");

        // Enable the Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Change the back button on the action bar to a custom drawable
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home);

        SugarContext.init(this);

        layoutManager = new LinearLayoutManager(this);

        recyclerView =  findViewById(R.id.recycleViewContainer);
        recyclerView.setHasFixedSize(true);
        //recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setNestedScrollingEnabled(false);


        recyclerView.setLayoutManager(layoutManager);





        /*customRecyclerAdapter = new CustomRecyclerAdapter(this, getMember());

        customRecyclerAdapter.setHasStableIds(true);

        recyclerView.setAdapter(customRecyclerAdapter);




        filterResult = customRecyclerAdapter.getFilter();*/



       //Define the ListView
       //ListView listView = findViewById(R.id.active_member_listview);
        //listView.setAdapter(new CustomAdapterForActiveMembers(this));
        //adapter = new CustomAdapter(this, getMember());


        //listView.setAdapter(adapter);


      //  filterResult = adapter.getFilter();




        //getSupportActionBar().setLogo(R.drawable.ic_fitness);  TO ATTACH A LOGO TO THE ACTIVITY NAME




        // to get rid of the laggy listView ==> don't generate photos when you're scrolling
       /* listView.setOnScrollListener(new AbsListView.OnScrollListener() {
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


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // Toast.makeText(ActiveMembers.this, getMember().get(position).getName(), Toast.LENGTH_SHORT).show();

                Member m = adapter.FindMember(getMember().get(position).getEmail());
                String member_id = m.getId().toString();

                Intent toProfil = new Intent(ActiveMembers.this, Profil.class);
                Bundle extras = new Bundle();
                extras.putString("adapter_id", member_id);
                toProfil.putExtras(extras);
                startActivity(toProfil);
            }
        });
*/





    }

    @Override
    protected void onStart() {
        super.onStart();

        new GenerateImageasync(customRecyclerAdapter).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);

        //
        getMenuInflater().inflate(R.menu.menu_active_members, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

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

            NavUtils.navigateUpFromSameTask(this);

        }

        if (item.getItemId() == R.id.menu_item_active_members) {

            Intent toAllMemebers = new Intent(ActiveMembers.this, AllMembers.class);
            startActivity(toAllMemebers);
        }

        if (item.getItemId() == R.id.menu_search) {

            //Managed by the CustomAdapter
        }
        if (item.getItemId() == R.id.menu_other){

            Intent toAttendanceList = new Intent(ActiveMembers.this, AttendanceList.class);
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

    private ArrayList<Member> getMember(){

        ArrayList<Member> memberArrayList=new ArrayList<Member>();
        List<Member> members = Member.listAll(Member.class);


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

            mProgressDialog = new ProgressDialog(ActiveMembers.this);
            mProgressDialog.setTitle("Loading full list...");
            mProgressDialog.setMessage("Please wait while we load and set the list.");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
            ActiveProgress =true;
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


//******************* Old class**************************
//********************************************************
    /*
Filter filterResult;
    Member m;
    CustomAdapter adapter;

    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;

    List<Member> personUtilsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_members);

        SugarContext.init(this);

        //Define the ListView
        ListView listView = findViewById(R.id.active_member_listview);
        //listView.setAdapter(new CustomAdapterForActiveMembers(this));
        adapter = new CustomAdapter(this, getMember());

        listView.setAdapter(adapter);


        filterResult = adapter.getFilter();




        // Define the Toolbar as the new Actionbar
        Toolbar myToolbar = findViewById(R.id.activemembers_appbar);
        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        getSupportActionBar().setTitle("At the Gym");

        // Enable the Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Change the back button on the action bar to a custom drawable
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home);

        //getSupportActionBar().setLogo(R.drawable.ic_fitness);  TO ATTACH A LOGO TO THE ACTIVITY NAME




        // to get rid of the laggy listView ==> don't generate photos when you're scrolling
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
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


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(ActiveMembers.this, getMember().get(position).getName(), Toast.LENGTH_SHORT).show();

                Member m = adapter.FindMember(getMember().get(position).getEmail());
                String member_id = m.getId().toString();

                Intent toProfil = new Intent(ActiveMembers.this, Profil.class);
                Bundle extras = new Bundle();
                extras.putString("adapter_id", member_id);
                toProfil.putExtras(extras);
                startActivity(toProfil);
            }
        });




    }





    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);

        //
        getMenuInflater().inflate(R.menu.menu_active_members, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

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

            NavUtils.navigateUpFromSameTask(this);

        }

        if (item.getItemId() == R.id.menu_item_active_members) {

            Intent toAllMemebers = new Intent(ActiveMembers.this, AllMembers.class);
            startActivity(toAllMemebers);
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

    private ArrayList<Member> getMember()
    {
        ArrayList<Member> memberArrayList=new ArrayList<Member>();
        List<Member> members = Member.listAll(Member.class);


        for(Member member : members)
        {
            Bitmap bmp = BitmapFactory.decodeByteArray(member.getImagev(), 0, member.getImagev().length);
            m=new Member(member.getName(),member.getEmail(),bmp);
            memberArrayList.add(m);
        }

        return memberArrayList;
    }

}

*/
    //******************************************************************************


