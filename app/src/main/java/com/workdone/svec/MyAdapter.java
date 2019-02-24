package com.workdone.svec;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter{

    Context context;
    ArrayList<User> profiles=new ArrayList<User>();

    public MyAdapter(Context c , ArrayList<User> p)
    {
        context = c;
        profiles = p;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= (LayoutInflater.from(context).inflate(R.layout.layout,parent,false));
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((MyViewHolder)viewHolder).bindView(i);
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tname,temail;
        public MyViewHolder(View itemView) {
            super(itemView);
            tname = (TextView) itemView.findViewById(R.id.name);
            //temail = (TextView) itemView.findViewById(R.id.email);
        }
        public void bindView(int position){
            tname.setText(profiles.get(position).getUsername());
            //temail.setText("Users Email");
        }

    }
}