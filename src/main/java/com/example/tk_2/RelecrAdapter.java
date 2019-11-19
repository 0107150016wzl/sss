package com.example.tk_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

class RelecrAdapter extends RecyclerView.Adapter {
    List<javaBean.DataBean> list=new ArrayList<>();
    Context context;

    public RelecrAdapter(List<javaBean.DataBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View foor= LayoutInflater.from(context).inflate(R.layout.list_item,null);
        return new ListViewHlord(foor);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        javaBean.DataBean dataBean = list.get(position);
        ((ListViewHlord)holder).title.setText(dataBean.getFood_str());
        ((ListViewHlord)holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list!=null){
                    onClick.onClick(v,position);
                }
            }
        });
        Glide.with(context).load(dataBean.getPic()).into(((ListViewHlord)holder).image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addData( List<javaBean.DataBean> lis) {
        this.list.addAll(lis);
        notifyDataSetChanged();
    }

    private class ListViewHlord extends RecyclerView.ViewHolder {

        private final ImageView image;
        private final TextView title;

        public ListViewHlord(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);

        }
    }
    OnClick onClick;

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    interface OnClick{
        void onClick(View v,int i);
    }
}
