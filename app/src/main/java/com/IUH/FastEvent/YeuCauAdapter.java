package com.IUH.FastEvent;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.IUH.FastEvent.Model.YeuCau;

import java.util.ArrayList;

public class YeuCauAdapter extends RecyclerView.Adapter<YeuCauAdapter.YeuCauViewHolder> {
    private ArrayList<YeuCau> yeuCauArrayList;
    private OnClickYeuCau onClickYeuCau;

    public void setYeuCauArrayList(ArrayList<YeuCau> yeuCauArrayList, OnClickYeuCau onClickYeuCau) {
        this.yeuCauArrayList = yeuCauArrayList;
        this.onClickYeuCau = onClickYeuCau;
    }

    @NonNull
    @Override
    public YeuCauViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_yeucau, parent, false);
        return new YeuCauViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YeuCauViewHolder holder, int position) {
        YeuCau yeuCau = yeuCauArrayList.get(position);
        if (yeuCau != null){
            holder.sinhVienBan.setText(yeuCau.getMssvyeucau().toString());
            holder.sinhVienNhan.setText(yeuCau.getMssvnhan().toString());
        }
    }

    @Override
    public int getItemCount() {
        if (yeuCauArrayList != null){
            return yeuCauArrayList.size();
        }
        return 0;

    }

    public class YeuCauViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView sinhVienBan;
        private TextView sinhVienNhan;
        private CardView chiTietYeuCau;
        private ConstraintLayout noiDungYeuCau;

        public YeuCauViewHolder(@NonNull View itemView) {
            super(itemView);
            sinhVienBan = itemView.findViewById(R.id.sinhVienBan);
            sinhVienNhan =itemView.findViewById(R.id.sinhVienNhan);
            chiTietYeuCau = itemView.findViewById(R.id.layoutYeuCauChiTiet);
            noiDungYeuCau =itemView.findViewById(R.id.noiDungYeuCau);
            chiTietYeuCau.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickYeuCau.onItemClick(v,getAdapterPosition());
        }
    }
    public interface OnClickYeuCau{
        void onItemClick(View view, int position);
    }
}
