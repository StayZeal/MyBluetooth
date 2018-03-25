package com.example.lucifer.mybluetooth.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseRvAdapter extends RecyclerView.Adapter {


    protected Context context;
    protected List datas;
    protected OnItemClickListener onItemClickListener;

    public BaseRvAdapter() {
    }



    public BaseRvAdapter(Context context) {
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }


    public void setData(List datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public void addData(List datas) {
        if (this.datas == null) {
            this.datas = new ArrayList();
        }
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    public List getData() {
        return this.datas;
    }

    public void clearData() {
        if (this.datas != null)
            this.datas.clear();
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }


}
