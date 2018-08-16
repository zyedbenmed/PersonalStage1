package com.example.zyed.fitnessapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.orm.SugarContext;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Z'YED on 24/07/2018.
 */

public class CustomGoalAdapter extends RecyclerView.Adapter<CustomGoalAdapter.ViewHolder> {

    Context context;
    int pos , pos1;
    TextView tv_note;
    String old_actual , old_objectives , new_actual , new_objectives, title , note;
    Member cpu_member;
    ArrayList<Goals> goalsArrayList;

    public CustomGoalAdapter(Context context, ArrayList<Goals> goalsArrayList) {
        this.context = context;
        this.goalsArrayList = goalsArrayList;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.goal_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        SugarContext.init(context);


        holder.itemView.setTag(goalsArrayList.get(position));

        final Goals pu = goalsArrayList.get(position);

        holder.pTitle.setText(pu.getTitle());
        //cpu_member = pu.getMember();
        holder.pActual.setText(pu.getActual());
        holder.pObjective.setText(pu.getGoal());

        final Goals this_goal_in_DB = FindGoal(pu.getTitle());

        if(this_goal_in_DB.getNote().equals("")){
            holder.pNotes.setTextColor(context.getResources().getColor(R.color.orange));

        }
        else{
            holder.pNotes.setTextColor(context.getResources().getColor(R.color.gray));
            //holder.pNotes.setBackgroundColor(context.getResources().getColor(R.color.gray));
            Drawable image = context.getResources().getDrawable(R.drawable.ic_note_gray );
            image.setBounds( 0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight() );
            holder.pNotes.setCompoundDrawables(image,null,null,null);
        }

        holder.pUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder_photo = new AlertDialog.Builder(context);

                View dialogView = LayoutInflater.from(context).inflate(R.layout.alert_dialog_update_objective,null);
                builder_photo.setView(dialogView);

                final TextView tv_title =dialogView.findViewById(R.id.ed_title);
                final EditText ed_actual =dialogView.findViewById(R.id.ed_actual);
                final EditText ed_objective =dialogView.findViewById(R.id.ed_goal);
                Button btn_positive = dialogView.findViewById(R.id.btn_ok);
                Button btn_negative = dialogView.findViewById(R.id.btn_cancel);

                tv_title.setText(pu.getTitle());

                final AlertDialog alertDialog = builder_photo.create();

                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                btn_positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();

                        old_actual = pu.getActual();
                        old_objectives = pu.getGoal();
                        cpu_member = pu.getMember();


                        new_actual = ed_actual.getText().toString();
                        new_objectives = ed_objective.getText().toString();
                        title = tv_title.getText().toString();

                        if(Empty(new_actual)){
                            ed_actual.setError("Please enter new state");
                            ed_actual.requestFocus();
                        }
                        else if(Empty(new_objectives)){
                            ed_objective.setText(pu.getGoal());
                            new_objectives = pu.getGoal();
                        }

                        Goals this_goal_in_DB = FindGoal(pu.getTitle());

                        this_goal_in_DB.setActual(new_actual);
                        this_goal_in_DB.setGoal(new_objectives);
                        this_goal_in_DB.setOld_actual(old_actual);
                        this_goal_in_DB.setOld_goal(old_objectives);
                        this_goal_in_DB.save();

                        Toast.makeText(context, "Please refresh", Toast.LENGTH_SHORT).show();
                    }
                });

                btn_negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();

                    }
                });
                alertDialog.show();
            }
        });

        holder.pNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder_note = new AlertDialog.Builder(context);

                View dialogView = LayoutInflater.from(context).inflate(R.layout.alert_dialog_notes,null);
                builder_note.setView(dialogView);

                final EditText ed_note =dialogView.findViewById(R.id.ed_note);
                tv_note = dialogView.findViewById(R.id.tv_show_note);
                final Button btn_positive_note = dialogView.findViewById(R.id.btn_add_note);
                final Button btn_negative_note = dialogView.findViewById(R.id.btn_cancel_note);
                final Button btn_save_note = dialogView.findViewById(R.id.btn_save_note);


                final AlertDialog alertDialog = builder_note.create();

                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));




                if(this_goal_in_DB.getNote().equals("")){
                    tv_note.setText("No note registered yet!");
                }
                else{
                    tv_note.setText(this_goal_in_DB.getNote().toString());
                }

                btn_positive_note.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //cpu_member = pu.getMember();



                        ed_note.setVisibility(View.VISIBLE);
                        btn_negative_note.setVisibility(View.GONE);
                        btn_positive_note.setVisibility(View.GONE);
                        btn_save_note.setVisibility(View.VISIBLE);
                        tv_note.setVisibility(View.GONE);

                        btn_save_note.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                note = ed_note.getText().toString();

                                if(Empty(note)) {
                                    ed_note.setError("Enter your note here");
                                    ed_note.requestFocus();
                                }

                                this_goal_in_DB.setNote(note);
                                this_goal_in_DB.save();

                                alertDialog.cancel();

                                Toast.makeText(context, "Note added successfully", Toast.LENGTH_SHORT).show();
                            }
                        });




                    }
                });

                btn_negative_note.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();

                    }
                });
                alertDialog.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return goalsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView pTitle, pActual, pObjective , pUpdate , pNotes;
        public Member cpu_member;

        public ViewHolder(final View itemView) {
            super(itemView);

            pTitle =  itemView.findViewById(R.id.tv_title);
            pActual =  itemView.findViewById(R.id.tv_actual);
            pObjective = itemView.findViewById(R.id.tv_objective);
            pUpdate = itemView.findViewById(R.id.tv_update);
            pNotes = itemView.findViewById(R.id.tv_notes);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                  /*  Member cpu = (Member) view.getTag();

                    String mail = cpu.getEmail();

                    String member_id = FindMember(mail).getId().toString();

                    Intent toProfil = new Intent(context, ProfileInformation.class);
                    Bundle extras = new Bundle();
                    extras.putString("adapter_id", member_id);
                    toProfil.putExtras(extras);
                    context.startActivity(toProfil);

                    //Toast.makeText(view.getContext(), cpu.getName(), Toast.LENGTH_SHORT).show();
*/
                }
            });

            /*
            pUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Goals cpu = (Goals) itemView.getTag();

                    AlertDialog.Builder builder_objective = new AlertDialog.Builder(context);

                    //View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.goal_view, parent, false);

                    View dialogView = LayoutInflater.from(context).inflate(R.layout.alert_dialog_update_objective,null);

                    builder_objective.setView(dialogView);

                    final TextView tv_title =dialogView.findViewById(R.id.ed_title);
                    final EditText ed_actual =dialogView.findViewById(R.id.ed_actual);
                    final EditText ed_objective =dialogView.findViewById(R.id.ed_goal);
                    Button btn_positive = dialogView.findViewById(R.id.btn_ok);
                    Button btn_negative = dialogView.findViewById(R.id.btn_cancel);

                    tv_title.setText(cpu.getTitle());

                    final AlertDialog alertDialog = builder_objective.create();



                    btn_positive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.cancel();

                            old_actual = cpu.getActual();
                            old_objectives = cpu.getGoal();
                            cpu_member = cpu.getMember();

                            new_actual = ed_actual.getText().toString();
                            new_objectives = ed_objective.getText().toString();
                            title = tv_title.getText().toString();


                            cpu.setActual(new_actual);
                            cpu.setGoal(new_objectives);
                            cpu.setOld_actual(old_actual);
                            cpu.setOld_goal(old_objectives);
                            cpu.save();

                            Goals before = cpu;
                            cpu.delete();
                            Goals after = cpu;
                            Toast.makeText(context, "test", Toast.LENGTH_SHORT).show();





                            //Goals updated_goal = new Goals(title,new_actual,new_objectives , cpu_member , old_actual,old_objectives );
                            //updated_goal.save();
                        }
                    });
                    btn_negative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });

                    alertDialog.show();

                }
            });

            */
        }
    }

    boolean Empty(String number){
        return TextUtils.isEmpty(number.trim());
    }

    public Goals FindGoal(String title){

        String id="1";

        List<Goals> goal_list = Goals.listAll(Goals.class);
        for(Goals gg : goal_list){

            if(gg.getTitle().equals(title)){
                id = gg.getId().toString();
            }
        }

        return Goals.findById(Goals.class, Integer.parseInt(id));

    }


}
