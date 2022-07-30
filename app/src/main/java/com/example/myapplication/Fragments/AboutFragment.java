package com.example.myapplication.Fragments;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.example.myapplication.Adapters.ScheduleAdapter;
import com.example.myapplication.Models.ScheduleModel;
import com.example.myapplication.Models.SingleEventModel;
import com.example.myapplication.R;
import com.example.myapplication.activities.SingleScheduleActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment implements View.OnClickListener {


    View view;
    private ReadMoreTextView aboutDescriptionTv;
    private TextView aboutGoingTv;
    String going,description;
    Bundle bundle;


    Calendar calendar=Calendar.getInstance();
    final int YEAR=calendar.get(Calendar.YEAR);
    final int MONTH=calendar.get(Calendar.MONTH);
    int DATE=calendar.get(Calendar.DATE);

    int mYear;
    int mMonth;
    int mDate;

    int HOUR=calendar.get(Calendar.HOUR);
    int MIN=calendar.get(Calendar.MINUTE);
    int mMin,mHour,busHour,busMin;

    MaterialEditText scheduleTitleEt,scheduleDescEt;
    TextView scheduleDateTv,scheduleTimeTv,scheduleBusTimeTv,noScheduleTv;
    RecyclerView recyclerView;

    List<ScheduleModel> scheduleDataList=new ArrayList<>();
    Query query;

    ScheduleAdapter scheduleAdapter;


    FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();


    Button addScheduleButton;

    ProgressDialog progressDialog;
    private  AlertDialog alertDialog;
    private int eventYear,eventMonth,eventDate,eventHour,eventMin,eventEndYear,eventEndMonth,eventEndDate;
    String eventId,scheduleTitle,scheduleDesc;

    public AboutFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bundle=this.getArguments();
        view= inflater.inflate(R.layout.fragment_about, container, false);

        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");



        //findingview
        aboutGoingTv=view.findViewById(R.id.aboutGoingId);
        aboutDescriptionTv=view.findViewById(R.id.aboutDescriptionId);
        addScheduleButton=view.findViewById(R.id.aboutFragAddScheduleBtnId);
        recyclerView=view.findViewById(R.id.aboutfragRecyclerView);
        noScheduleTv=view.findViewById(R.id.aboutFragNoScheduleid);

        addScheduleButton.setOnClickListener(this);


            SharedPreferences sharedPreferences=getActivity().getSharedPreferences("aboutFragData", Context.MODE_PRIVATE);
             description = sharedPreferences.getString("description","");
             going=sharedPreferences.getString("going","");
             eventYear=sharedPreferences.getInt("startYear",0);
             eventMonth=sharedPreferences.getInt("startMonth",0);
             eventDate=sharedPreferences.getInt("startDate",0);
             eventHour=sharedPreferences.getInt("startHour",0);
             eventMin=sharedPreferences.getInt("startMin",0);
             eventEndYear=sharedPreferences.getInt("endYear",0);
             eventEndYear=sharedPreferences.getInt("endYear",0);
             eventEndMonth=sharedPreferences.getInt("endMonth",0);
             eventEndDate=sharedPreferences.getInt("endDate",0);
             eventHour=sharedPreferences.getInt("startHour",0);
             eventMin=sharedPreferences.getInt("startMin",0);
             eventId=sharedPreferences.getString("eventId","");

             mYear=eventYear;
             mDate=eventDate;
             mMonth=eventMonth;
             mMin=eventMin;
             mHour=eventHour;

             setRecycler();


       // else        Toast.makeText(getContext(),"Kichhu aisena",Toast.LENGTH_LONG).show();

        //Toast.makeText(getContext(),description,Toast.LENGTH_LONG).show();


        String mGoing;
        if (going==null)mGoing="0 going";
        else mGoing=going+" going";

        aboutDescriptionTv.setText(description);
        aboutGoingTv.setText(mGoing);



    return view;
    }

    private void setRecycler() {

        query=firebaseFirestore.collection("Events").document(eventId).collection("Schedules").orderBy("create_at", Query.Direction.ASCENDING);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    scheduleDataList.clear();

                    if (task.getResult()!=null)
                    {
                        for (DocumentSnapshot doc: task.getResult())
                        {
                            ScheduleModel scheduleModel=doc.toObject(ScheduleModel.class);
                            scheduleDataList.add(scheduleModel);
                        }

                        scheduleAdapter=new ScheduleAdapter(scheduleDataList,getContext());
                        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());

                        if (scheduleDataList.isEmpty())noScheduleTv.setVisibility(View.VISIBLE);
                        else noScheduleTv.setVisibility(View.GONE);

                        setItemClickListener(scheduleAdapter);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setAdapter(scheduleAdapter);


                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setItemClickListener(ScheduleAdapter scheduleAdapter) {

        scheduleAdapter.setOnItemClickListener(new ScheduleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ScheduleModel scheduleModel) {

                Intent intent=new Intent(getActivity(), SingleScheduleActivity.class);
                intent.putExtra("eventId",eventId);
                intent.putExtra("scheduleTitle",scheduleModel.getScheduleTitle());
                intent.putExtra("scheduleDate",scheduleModel.getScheduleDate());
                intent.putExtra("scheduleMonth",scheduleModel.getScheduleMonth());
                intent.putExtra("scheduleId",scheduleModel.getScheduleItemId());
                intent.putExtra("scheduleYear",mYear);
                intent.putExtra("scheduleDesc",scheduleModel.getScheduleDescription());
                intent.putExtra("scheduleHour",scheduleModel.getScheduleHour());
                intent.putExtra("scheduleBusHour",scheduleModel.getBusHour());
                intent.putExtra("scheduleBusMin",scheduleModel.getBusMin());
                startActivity(intent);
                getActivity().finish();


            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.aboutFragAddScheduleBtnId:
                showAlertDialogue();
                        break;
        }

    }

    private void showAlertDialogue() {

        AlertDialog.Builder alert=new AlertDialog.Builder(getContext());

        //  View mView=LayoutInflater.from(getContext()).inflate(R.layout.edit_post_alert_dialogue,null);
        final View mView=getLayoutInflater().inflate(R.layout.schedule_alert_dialogue,null);

        alert.setView(mView);
        alertDialog=alert.create();
        alertDialog.setCancelable(true);
        alertDialog.show();

        scheduleTitleEt=mView.findViewById(R.id.schedule_titleid);
        scheduleDescEt=mView.findViewById(R.id.schedule_descId);
        scheduleTimeTv=mView.findViewById(R.id.schedule_timeId);
        scheduleDateTv=mView.findViewById(R.id.schedule_dateId);
        scheduleBusTimeTv=mView.findViewById(R.id.schedule_bus_time_Id);
        addScheduleButton=mView.findViewById(R.id.schedule_add_buttonId);

        String time=String.format("%02d:%02d",eventHour,eventMin);
        scheduleTimeTv.setText(time); 
        String bustime=String.format("%02d:%02d",eventHour,eventMin);
        scheduleBusTimeTv.setText(bustime);
        String date=String.format("%02d",eventDate)+"/"+String.format("%02d",eventMonth)+"/"+eventYear;
        scheduleDateTv.setText(date);


        scheduleDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker();
            }
        });

        scheduleTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePicker();
            }
        });

        scheduleBusTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                busTimePicker();
            }
        });
        
        addScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addScheduleToDatabase();
            }
        });


    }

    private void addScheduleToDatabase() {

        scheduleTitle=scheduleTitleEt.getText().toString().trim();
        scheduleDesc=scheduleDescEt.getText().toString().trim();

        if (scheduleTitle.isEmpty())
        {
            scheduleTitleEt.setError("write a title");
            scheduleTitleEt.requestFocus();
            return;
        }
        if (scheduleDesc.isEmpty())
                {
                    scheduleDescEt.setError("write a title");
                    scheduleDescEt.requestFocus();
                    return;
                }


        if (!(mYear>=eventYear && mYear<=eventEndYear && mMonth+1>=eventMonth && mMonth+1<=eventEndMonth && mDate>=eventDate && mDate<=eventEndDate) )
        {
            scheduleDateTv.setError("Select an appropriate date");
            scheduleDateTv.requestFocus();
            return;
        }

        if(!( mHour>eventHour || (mHour==eventHour && mMin>eventMin)))
        {
            scheduleTimeTv.setError("Select an appropriate time");
            scheduleTimeTv.requestFocus();
            return;
        }
        progressDialog.show();

        ScheduleModel scheduleModel=new ScheduleModel(eventId,scheduleTitle,scheduleDesc,"",mHour,mMin,mDate,busHour,busMin,System.currentTimeMillis(),mMonth);

        firebaseFirestore.collection("Events").document(eventId).collection("Schedules")
                .add(scheduleModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                String scheduleItemId=task.getResult().getId();
                progressDialog.cancel();
                if (task.isSuccessful())
                {
                    ScheduleModel scheduleModel2=new ScheduleModel(eventId,scheduleTitle,scheduleDesc,scheduleItemId,mHour,mMin,mDate,busHour,busMin,System.currentTimeMillis(),mMonth);

                    firebaseFirestore.collection("Events").document(eventId).collection("Schedules").document(scheduleItemId)
                            .set(scheduleModel2).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getContext(), "Schedule added", Toast.LENGTH_SHORT).show();
                            getActivity().recreate();
                            alertDialog.cancel();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.cancel();
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });



    }


    public void datePicker()
    {


//        date.setError(null);
//        dateEnd.setError(null);
//        timeEnd.setError(null);
//        time.setError(null);

        DatePickerDialog datePickerDialog=new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int Year, int Month, int Date) {

                mYear=Year;
                mMonth=Month;
                mDate=Date;


                    if (!(mYear>=eventYear && mYear<=eventEndYear && mMonth+1>=eventMonth && mMonth+1<=eventEndMonth && mDate>=eventDate && mDate<=eventEndDate) )
                    {
                        scheduleDateTv.setError("Select an aprropriate date!");
                        Toast.makeText(getContext(), "hello "+eventMonth+"= "+mMonth, Toast.LENGTH_SHORT).show();
                        scheduleDateTv.requestFocus();
                        return;
                    }

                else
                {
                    scheduleDateTv.setError(null);
                    scheduleTimeTv.setError(null);
                    String dates=Date+"/"+(Month+1)+"/"+Year;
                    scheduleDateTv.setText(dates);
                }

            }
        },YEAR,MONTH,DATE);

        datePickerDialog.show();

    }

    public void timePicker()

    {

        scheduleDateTv.setError(null);
        scheduleTimeTv.setError(null);


        TimePickerDialog timePickerDialog=new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                mMin=min;
                mHour=hour;

                if(!( mHour>eventHour || (mHour==eventHour && mMin>eventMin)))
                {

                    scheduleTimeTv.setError("Select an appropriate time!");
                    scheduleTimeTv.requestFocus();
                    return;
                }
                else
                {
                    scheduleTimeTv.setError(null);
                    scheduleDateTv.setError(null);

                    String times=String.format("%02d:%02d",hour,min);
                    scheduleTimeTv.setText(times);
                }


            }
        },HOUR,MIN,true);

        timePickerDialog.show();

    } public void busTimePicker()
    {




        TimePickerDialog timePickerDialog=new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                busMin=min;
                busHour=hour;


                    String times=String.format("%02d:%02d",hour,min);
                    scheduleBusTimeTv.setText(times);



            }
        },HOUR,MIN,true);

        timePickerDialog.show();

    }
}
