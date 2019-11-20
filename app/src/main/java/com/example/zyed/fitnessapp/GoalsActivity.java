package com.example.zyed.fitnessapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.orm.SugarContext;

import java.util.ArrayList;
import java.util.List;

public class GoalsActivity extends AppCompatActivity {


    RecyclerView.Adapter mAdapter;
    CustomGoalAdapter customRecyclerAdapter;
    RecyclerView.LayoutManager layoutManager;
    int IdFinal;
    Member member;
    RecyclerView personUtilsList;
    Goals g;
    String toolbar_title = "Goals";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        SugarContext.init(this);
        // Define the Toolbar as the new Actionbar
        Toolbar myToolbar = findViewById(R.id.goal_appbar);
        setSupportActionBar(myToolbar);
        // Get a support ActionBar corresponding to this toolbar
        getSupportActionBar().setTitle(toolbar_title);
        myToolbar.setTitleTextColor(this.getResources().getColor(R.color.goal_background));
        //myToolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        // Enable the Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);


        Bundle extras = getIntent().getExtras();
        String member_id = extras.getString("idFromProfile");
        IdFinal = Integer.parseInt(member_id);

        member = Member.findById(Member.class, IdFinal);

        if(getGoals().isEmpty()){

            Toast.makeText(this, "No Goals Registered yet!", Toast.LENGTH_SHORT).show();

        }
        else {

            RecyclerView recyclerView = findViewById(R.id.recycleViewContainerg);
            recyclerView.setHasFixedSize(true);

            layoutManager = new LinearLayoutManager(this);

            recyclerView.setLayoutManager(layoutManager);

            customRecyclerAdapter = new CustomGoalAdapter(this, getGoals());

            customRecyclerAdapter.setHasStableIds(true);

            recyclerView.setAdapter(customRecyclerAdapter);
            recyclerView.setNestedScrollingEnabled(false);



        }

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);

        //
        getMenuInflater().inflate(R.menu.menu_goals, menu);


        MenuItem actionMenuItem = menu.findItem(R.id.menu_goals_refresh);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == android.R.id.home) { // MANAGE THE UP BAR (return to MainActivity)

            //NavUtils.navigateUpFromSameTask(this);
            Intent toProfil = new Intent(GoalsActivity.this, Profile.class);
            Bundle extras = new Bundle();
            extras.putString("idFromGoals", String.valueOf(IdFinal));
            toProfil.putExtras(extras);
            startActivity(toProfil);
        }

        if (item.getItemId() == R.id.menu_goals_refresh) {

            Intent refresh = new Intent(GoalsActivity.this,GoalsActivity.class);
            Bundle extras = new Bundle();
            extras.putString("idFromProfile", String.valueOf(IdFinal));
            refresh.putExtras(extras);
            startActivity(refresh);
        }




        return true;
    }





    private ArrayList<Goals> getGoals(){

        ArrayList<Goals> goalArrayList=new ArrayList<Goals>();

        List<Goals> gl1 = Goals.find(Goals.class, "member = ?", member.getId().toString());


        for(Goals goal: gl1)
        {
            g=new Goals(goal.getTitle(),goal.getActual(),goal.getGoal(), member ,"","","");
            goalArrayList.add(g);
        }

        return goalArrayList;
    }
}
