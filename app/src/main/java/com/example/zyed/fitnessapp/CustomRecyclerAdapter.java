package com.example.zyed.fitnessapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Filterable;
import android.widget.Filter;

import com.bumptech.glide.Glide;
import com.orm.SugarContext;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Z'YED on 18/07/2018.
 */

public class CustomRecyclerAdapter extends  RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder> implements Filterable{

    Context context;
    ArrayList<Member> Members,filterList;
    CustomFilter filter;

    public CustomRecyclerAdapter(Context context, ArrayList<Member> Members) {
        this.context = context;
        this.Members = Members;
        this.filterList=Members;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.members_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SugarContext.init(context);
        holder.itemView.setTag(Members.get(position));

        Member pu = Members.get(position);

        holder.pName.setText(pu.getName());
        holder.pJobProfile.setText(pu.getEmail());

        if (pu.getClock().equals("in")){
            holder.pGreenDot.setVisibility(View.VISIBLE);
        }

        String mail = pu.getEmail();
        Member member = FindMember(mail);

        GenerateImage(holder.pImage, member);

        //Bitmap bmp = BitmapFactory.decodeByteArray(member.getImagev(), 0, member.getImagev().length);
        //Picasso.with(context).load(String.valueOf(member.getImagev())).fit().into(holder.pImage);

        /*byte [] buf = member.getImagev();
        String s = null;
        try {
            s = new String(buf, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        URI uri = URI.create(s);

        Picasso.with(context).load(String.valueOf(uri)).fit().into(holder.pImage);*/

        //holder.pImage.setImageBitmap(bmp);
        //Glide.with(context).load(getImageUri(context, bmp)).into(holder.pImage);

    }

    @Override
    public int getItemCount() {
        return Members.size();
    }

    @Override
    public Filter getFilter() {
        if(filter == null)
        {
            filter=new CustomFilter();
        }

        return filter;
    }


        public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView pName;
        public TextView pJobProfile;
        public CircleImageView pImage;
        public ImageView pGreenDot;

        public ViewHolder(View itemView) {
            super(itemView);

            pName =  itemView.findViewById(R.id.member_tv1);
            pJobProfile =  itemView.findViewById(R.id.member_tv2);
            pImage = itemView.findViewById(R.id.member_imagev);
            pGreenDot = itemView.findViewById(R.id.member_imagev2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Member cpu = (Member) view.getTag();

                    String mail = cpu.getEmail();

                    String member_id = FindMember(mail).getId().toString();

                    Intent toProfil = new Intent(context, Profile.class);
                    Bundle extras = new Bundle();
                    extras.putString("adapter_id", member_id);
                    toProfil.putExtras(extras);
                    context.startActivity(toProfil);

                    //Toast.makeText(view.getContext(), cpu.getName(), Toast.LENGTH_SHORT).show();

                }
            });

        }
    }

    //INNER CLASS
    class CustomFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {


            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                //CONSTARINT TO UPPER
                constraint = constraint.toString().toUpperCase();

                ArrayList<Member> filters = new ArrayList<Member>();

                //get specific items
                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getName().toUpperCase().contains(constraint)) {
                        Member p = new Member(filterList.get(i).getName(), filterList.get(i).getEmail(), filterList.get(i).getImage(), filterList.get(i).getClock());

                        filters.add(p);
                    }
                }

                results.count = filters.size();
                results.values = filters;

            } else {
                results.count = filterList.size();
                results.values = filterList;

            }

            return results;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            Members = (ArrayList<Member>) results.values;


            notifyDataSetChanged();
        }
    }

    public Member FindMember(String Email){

        String id="1";

        List<Member> members = Member.listAll(Member.class);
        for(Member member : members){

            if(member.getEmail().equals(Email)){
                id = member.getId().toString();
            }
        }

        return Member.findById(Member.class, Integer.parseInt(id));

    }


    static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public   void GenerateImage(final CircleImageView imageView, final Member mm){

        new  AsyncTask<Bitmap, Void, Bitmap>() {

            protected void onPreExecute(){
                //This could also be an internal resource instead of null
                //imageView.setImageResource(R.drawable.default_profil);
                return;
            }

            @Override
            protected Bitmap doInBackground(Bitmap... params) {


                Bitmap bmp = BitmapFactory.decodeByteArray(mm.getImagev(), 0, mm.getImagev().length);
                return bmp;

            }

            @Override
            protected void onPostExecute(Bitmap result) {
                super.onPostExecute(result);
                imageView.setImageBitmap(result);
                //Picasso.with(context).load(getImageUri(context, result)).placeholder(R.drawable.default_profil).into(imageView);

                //Glide.with(context).load(getImageUri(context, result)).into(imageView);

            }

        }.execute();

    }


}
