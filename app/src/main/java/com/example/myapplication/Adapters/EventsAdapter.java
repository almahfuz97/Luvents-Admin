package com.example.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.CreateModel;
import com.example.myapplication.R;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.MyViewHolder> {

    List<CreateModel> dataList;
    Context context;
    String strMonth,interestedPeople;

    public EventsAdapter(List<CreateModel> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.events_data_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final CreateModel createModel=dataList.get(position);

        if(createModel.getImageUri().equals("No Image"))
        {
            holder.imageView.setImageResource(R.drawable.ic_image_blue_gray_24dp);
        }
        else
        Picasso.get().load(createModel.getImageUri()).fit().into(holder.imageView);

        monthGetter(createModel);
        String date=String.valueOf(createModel.getDate());
        String Alldate=strMonth+" "+date;


        holder.monthHead.setText(strMonth);
        holder.dateHead.setText(date);
        holder.eventname.setText(createModel.getEventName());
        holder.allDate.setText(Alldate);
        holder.host.setText(createModel.getHostedBy());

        firebaseDataGetter(createModel);

        if(interestedPeople==null)interestedPeople="0";
        String interestedPeople2=interestedPeople+" People interested";
        holder.interestP.setText(interestedPeople2);
       holder.timeAgo.setText(TimeAgo.from(createModel.getCreate_at()));



    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView monthHead,dateHead,eventname,allDate,host,interestP,timeAgo;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.list_imageViewId);
            monthHead=itemView.findViewById(R.id.list_monthId);
            dateHead=itemView.findViewById(R.id.list_dateId);
            eventname=itemView.findViewById(R.id.list_eventNameId);
            allDate=itemView.findViewById(R.id.list_allDateId);
            host=itemView.findViewById(R.id.hostedbyId);
            interestP=itemView.findViewById(R.id.interestedPeopleid);
            timeAgo=itemView.findViewById(R.id.timeAgoId);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(clickListener!=null)
                    {
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            clickListener.onItemClick(view,position,dataList.get(getAdapterPosition()));
                        }
                    }
                }
            });

            //

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    if(clickListener2!=null)
                    {
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            clickListener2.onItemLongClick(view,position,dataList.get(getAdapterPosition()));
                        }
                    }

                    return false;
                }
            });

        }
    }

    public void  monthGetter(CreateModel createModel)
    {
        if(createModel.getMonth()==1) strMonth="JAN";
        if(createModel.getMonth()==2) strMonth="FEB";
        if(createModel.getMonth()==3) strMonth="MAR";
        if(createModel.getMonth()==4) strMonth="APR";
        if(createModel.getMonth()==5) strMonth="MAY";
        if(createModel.getMonth()==6) strMonth="JUN";
        if(createModel.getMonth()==7) strMonth="JUL";
        if(createModel.getMonth()==8) strMonth="AUG";
        if(createModel.getMonth()==9) strMonth="SEP";
        if(createModel.getMonth()==10) strMonth="OCT";
        if(createModel.getMonth()==11) strMonth="NOV";
        if(createModel.getMonth()==12) strMonth="DEC";


    }

    //firebase
    public void firebaseDataGetter(CreateModel createModel)
    {

        long interest=createModel.getInterested();

        interestedPeople=String.valueOf(interest);
    }

    //searchfilter
    public void setFilter(List<CreateModel> mList) {
        dataList=new ArrayList<>();
        dataList.addAll(mList);
        notifyDataSetChanged();
    }

    //ClickListener
    private OnItemClickListener clickListener;
    public interface OnItemClickListener{
        void onItemClick(View view,int position,CreateModel createModel);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        clickListener=listener;
    }

    //longClickItem
    private OnItemLongClickListener clickListener2;
    public interface OnItemLongClickListener{
        void onItemLongClick(View view,int position,CreateModel createModel);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener)
    {
        clickListener2=listener;
    }
}
