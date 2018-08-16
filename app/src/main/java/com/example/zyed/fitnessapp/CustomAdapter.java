package com.example.zyed.fitnessapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import 	android.widget.ListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.orm.SugarContext;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Z'YED on 26/06/2018.
 */


public class CustomAdapter extends BaseAdapter implements Filterable {

    public static boolean test = true;
    String mail;
    Member member;
    ListView listView;
    Context c;
    ArrayList<Member> members, filterList;
    CustomFilter filter;



    public CustomAdapter(Context ctx,ArrayList<Member> members) {
        // TODO Auto-generated constructor stub

        this.c=ctx;
        this.members=members;
        this.filterList=members;
    }

    @Override
    public int getCount() {

        return members.size();
    }

    @Override
    public Object getItem(int pos) {

        return members.get(pos);
    }

    @Override
    public long getItemId(int pos) {

        return members.indexOf(getItem(pos));
    }

    @Override
    public View getView(final int pos, View convertView, final ViewGroup parent) {

        ViewHolder holder;
        SugarContext.init(c);
        LayoutInflater inflater=(LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView==null)
        {
            convertView=inflater.inflate(R.layout.members_view, null);
            holder = new ViewHolder();
            // initialize views
            holder.hName = convertView.findViewById(R.id.member_tv1);
            holder.hEmail =  convertView.findViewById(R.id.member_tv2);
            holder.hpic =  convertView.findViewById(R.id.member_imagev);
            convertView.setTag(holder);

        }

            holder = (ViewHolder) convertView.getTag();

            holder.hName.setText(members.get(pos).getName());
            holder.hEmail.setText(members.get(pos).getEmail());

            mail = members.get(pos).getEmail();
            member = FindMember(mail);

            if (test) {GenerateImage(holder.hpic, member);} // test=true when you're not scrolling

        /*String member_id = member.getId().toString();
        convertView.setOnClickListener(new View.OnClickListener() {
           @Override
          public void onClick(View v) {

               Intent toProfil = new Intent(c, Profil.class);
               Bundle extras = new Bundle();
               extras.putString("adapter_id", member_id);
               toProfil.putExtras(extras);
               c.startActivity(toProfil);

          }
       });*/

        return convertView;


    }

    static class ViewHolder {
        TextView hName;
        TextView hEmail;
        CircleImageView hpic;
    }

    @Override
    public Filter getFilter() {

        if(filter == null)
        {
            filter=new CustomFilter();
        }

        return filter;
    }

    //INNER CLASS
    class CustomFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {


            FilterResults results=new FilterResults();

            if(constraint != null && constraint.length()>0)
            {
                //CONSTARINT TO UPPER
                constraint=constraint.toString().toUpperCase();

                ArrayList<Member> filters=new ArrayList<Member>();

                //get specific items
                for(int i=0;i<filterList.size();i++)
                {
                    if(filterList.get(i).getName().toUpperCase().contains(constraint))
                    {
                        Member p=new Member(filterList.get(i).getName(), filterList.get(i).getEmail(), filterList.get(i).getImage(), filterList.get(i).getClock());

                        filters.add(p);
                    }
                }

                results.count=filters.size();
                results.values=filters;

            }else
            {
                results.count=filterList.size();
                results.values=filterList;

            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {


            members=(ArrayList<Member>) results.values;


            notifyDataSetChanged();
        }
    }


     static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public  void GenerateImage(final ImageView imageView, final Member mm){

        new AsyncTask<Bitmap, Void, Bitmap>() {

            protected void onPreExecute(){
                //This could also be an internal resource instead of null
                imageView.setImageResource(R.drawable.default_profil);
            }

            @Override
            protected Bitmap doInBackground(Bitmap... params) {
                /*HttpRetriever retriever = new HttpRetriever();
                InputStream is = retriever.retrieveStream(url);
                image = BitmapFactory.decodeStream(is);
                return image;*/

                //Picasso.with(c).load(R.drawable.default_profil).into(imageView);

                Bitmap bmp = BitmapFactory.decodeByteArray(mm.getImagev(), 0, mm.getImagev().length);
                return bmp;

            }

            @Override
            protected void onPostExecute(Bitmap result) {
                super.onPostExecute(result);
                //imageView.setImageBitmap(result);
                Picasso.with(c).load(getImageUri(c, result)).placeholder(R.drawable.default_profil).into(imageView);


            }

        }.execute();

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
}
