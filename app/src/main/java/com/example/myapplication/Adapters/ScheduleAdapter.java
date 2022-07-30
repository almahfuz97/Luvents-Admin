package com.example.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.ScheduleModel;
import com.example.myapplication.R;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.MyViewHolder> {

    List<ScheduleModel> dataList;
    Context context;

    public ScheduleAdapter(List<ScheduleModel> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_data_list,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ScheduleModel scheduleModel=dataList.get(position);

        holder.scheduleListTitleTv.setText(scheduleModel.getScheduleTitle());

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView scheduleListTitleTv;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            scheduleListTitleTv=itemView.findViewById(R.id.schedule_list_textviewID);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (clickListener!=null)
                    {
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            clickListener.onItemClick(view,position,dataList.get(getAdapterPosition()));
                        }
                    }
                }
            });
        }


    }

    private OnItemClickListener clickListener;
    public interface OnItemClickListener
    {
        void onItemClick(View view,int position,ScheduleModel scheduleModel);

    }
    public void setOnItemClickListener(OnItemClickListener clickListener)
    {
        this.clickListener=clickListener;
    }


}
