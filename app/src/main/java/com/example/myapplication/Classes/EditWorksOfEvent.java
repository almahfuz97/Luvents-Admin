package com.example.myapplication.Classes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myapplication.activities.Bottom_Navigation;
import com.example.myapplication.Models.CreateModel;
import com.example.myapplication.R;
import com.example.myapplication.activities.SingleEventDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class EditWorksOfEvent {



    private ProgressDialog progressDialog;

    StorageTask storageTask;
    Uri mUri;

    private StorageReference storageReference=FirebaseStorage.getInstance().getReference("images");
    String imageUri;

    Calendar calendar=Calendar.getInstance();
    final int YEAR=calendar.get(Calendar.YEAR);
    final int MONTH=calendar.get(Calendar.MONTH);
    int DATE=calendar.get(Calendar.DATE);

    int HOUR=calendar.get(Calendar.HOUR);
    int MIN=calendar.get(Calendar.MINUTE);

    int x=0,y=0,z=0,k=0;
    View view;
    Button choosePhoto;
    private EditText editEventEventNameEt,editEventAdressEt,editEventDescEt;

    private TextView editEventDateStartTv,editEventDateEndTv,editEventTimeStartTv,editEventTimeEndTv;

    private Context context;
    private Activity mActivity;
    private View mView;
    String eventId1;



    int mYear ,mMonth,mDate, mEndYear, mEndMonth, mEndDate,mHour,mMin,mEndHour,mEndMin;


    String eventName;
  //  String hostedBy;
    String description;
    String going;

    int startDate1;
    int endDate1;
    int StartHour1;
    int EndHour1;
    int startMin1;
    int EndMin1;
    int startYear1;
    int endYear1;
    int startMonth1;
    int endMonth1;


    SharedPreferences sharedPreferences;

    String mDescription;
    String mEventName;
    String eventId;
    String mAddress;
    int dDate;
    int dEndDate;
    int dHour;
    int dEndHour;
    int dMin;
    int dEndMin;
    int dMonth;
    int dEndMonth;
    int dYear;
    int dEndYear;
    long createAt;
    String imgUri;
    AlertDialog alertDialog;
    String hostedBy;
    public EditWorksOfEvent(Context c, Activity activity, View mView, int mYear, int mMonth, int mDate, int mEndYear, int mEndMonth, int mEndDate, int mHour, int mMin, int mEndHour, int mEndMin, String imgUri, AlertDialog alertDialog) {

        context=c;
        mActivity=activity;
        this.mView=mView;
//        this.mYear=mYear;
//        this.mMonth=mMonth;
//        this.mDate=mDate;
//        this.mEndDate=mEndDate;
//        this.mEndMonth=mEndMonth;
//        this.mEndYear=mEndYear;
//        this.mHour=mHour;
//        this.mEndHour=mEndHour;
//        this.mMin=mMin;
//        this.mEndMin=mEndMin;
        //this.imgUri=imgUri;
        this.alertDialog=alertDialog;
        this.sharedPreferences=mView.getContext().getSharedPreferences("aboutFragData", Context.MODE_PRIVATE);
        mDescription = sharedPreferences.getString("description","");
        mEventName=sharedPreferences.getString("eventName","");
        eventId=sharedPreferences.getString("eventId","");
        mAddress=sharedPreferences.getString("address","");
         dDate=sharedPreferences.getInt("startDate",0);
         dEndDate=sharedPreferences.getInt("endDate",0);
       dHour=sharedPreferences.getInt("startHour",0);
        dEndHour=sharedPreferences.getInt("endHour",0);
        dMin=sharedPreferences.getInt("startMin",0);
         dEndMin=sharedPreferences.getInt("endMin",0);
         dMonth=sharedPreferences.getInt("startMonth",0);
         dEndMonth=sharedPreferences.getInt("endMonth",0);
         dYear=sharedPreferences.getInt("startYear",0);
         dEndYear=sharedPreferences.getInt("endYear",0);
         createAt=sharedPreferences.getLong("createAt",0);
         imgUri=sharedPreferences.getString("imageUri","");
         hostedBy=sharedPreferences.getString("hostedBy","");

        imageUri=imgUri;

    }
    //int dEndMin=mEndMin;



    public void datePicker()
    {
        editEventDateStartTv=mView.findViewById(R.id.edit__event_dateid);
        editEventDateEndTv=mView.findViewById(R.id.edit__event_dateEndid);
        editEventTimeStartTv=mView.findViewById(R.id.edit__event_timeid);
        editEventTimeEndTv=mView.findViewById(R.id.edit__event_timeEndid);

        editEventDateStartTv.setError(null);
        editEventDateEndTv.setError(null);
        editEventTimeEndTv.setError(null);
        editEventTimeStartTv.setError(null);

        DatePickerDialog datePickerDialog=new DatePickerDialog(mView.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int Year, int Month, int Date) {

                dYear=Year;
                dMonth=Month;
                dDate=Date;




                if(!((dYear>YEAR )|| (dYear==YEAR && dMonth>MONTH) || (dYear==YEAR && MONTH==dMonth && dDate>=DATE)))
                {
                    editEventDateStartTv.setError("Select an aprropriate date!");
                    editEventDateStartTv.requestFocus();
                    return;
                }
                else
                {
                    editEventDateStartTv.setError(null);
                    editEventDateEndTv.setError(null);
                    editEventTimeEndTv.setError(null);
                    editEventTimeStartTv.setError(null);
                    x=1;

                    String dates=Date+"/"+(Month+1)+"/"+Year;
                    dMonth=dMonth+1;
                    editEventDateStartTv.setText(dates);
                }

            }
        },YEAR,MONTH,DATE);

        datePickerDialog.show();

    }
   public void dateEndPicker()
    {
        editEventDateStartTv=mView.findViewById(R.id.edit__event_dateid);
        editEventDateEndTv=mView.findViewById(R.id.edit__event_dateEndid);
        editEventTimeStartTv=mView.findViewById(R.id.edit__event_timeid);
        editEventTimeEndTv=mView.findViewById(R.id.edit__event_timeEndid);

        editEventDateStartTv.setError(null);
        editEventDateEndTv.setError(null);
        editEventTimeEndTv.setError(null);
        editEventTimeStartTv.setError(null);

        DatePickerDialog datePickerDialog=new DatePickerDialog(mView.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int Year, int Month, int Date) {

                dEndYear=Year;
                dEndMonth=Month;
                dEndDate=Date;

                if(!((dEndYear>dYear )|| (dYear==dEndYear && dMonth<dEndMonth) || (dYear==dEndYear && dEndMonth==dMonth && dDate<=dEndDate)))
                {
                    editEventDateEndTv.setError("Select an aprropriate date!");
                    editEventDateEndTv.requestFocus();
                    return;
                }
                else
                {
                    editEventDateEndTv.setError(null);
                    editEventTimeStartTv.setError(null);
                    editEventDateStartTv.setError(null);
                    editEventTimeEndTv.setError(null);
                    String dates=Date+"/"+(Month+1)+"/"+Year;
                    dEndMonth=dEndMonth+1;
                    editEventDateEndTv.setText(dates);
                    y=1;
                }




            }
        },YEAR,MONTH,DATE);
        datePickerDialog.show();

    }


    public void timePicker()
    {
        editEventDateStartTv=mView.findViewById(R.id.edit__event_dateid);
        editEventDateEndTv=mView.findViewById(R.id.edit__event_dateEndid);
        editEventTimeStartTv=mView.findViewById(R.id.edit__event_timeid);
        editEventTimeEndTv=mView.findViewById(R.id.edit__event_timeEndid);

        editEventDateStartTv.setError(null);
        editEventDateEndTv.setError(null);
        editEventTimeEndTv.setError(null);
        editEventTimeStartTv.setError(null);



        TimePickerDialog timePickerDialog=new TimePickerDialog(mView.getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                dMin=min;
                dHour=hour;

                if(!((dYear>YEAR) || (dYear==YEAR && dMonth>MONTH) || (dYear==YEAR && dMonth==MONTH && dDate>DATE) || (dYear==YEAR && dMonth==MONTH && dDate==DATE && (dHour>HOUR || (dHour==HOUR && dMin>MIN)))))
                {

                    editEventTimeStartTv.setError("Select an appropriate time!");
                    editEventTimeStartTv.requestFocus();
                    return;
                }
                else
                {
                    editEventTimeStartTv.setError(null);
                    editEventDateStartTv.setError(null);
                    editEventDateEndTv.setError(null);
                    editEventTimeEndTv.setError(null);
                    String times=String.format("%02d:%02d",hour,min);
                    editEventTimeStartTv.setText(times);
                    z=1;
                }


            }
        },HOUR,MIN,true);

        timePickerDialog.show();

    }
    public void timeEndPicker()
    {
        editEventDateStartTv=mView.findViewById(R.id.edit__event_dateid);
        editEventDateEndTv=mView.findViewById(R.id.edit__event_dateEndid);
        editEventTimeStartTv=mView.findViewById(R.id.edit__event_timeid);
        editEventTimeEndTv=mView.findViewById(R.id.edit__event_timeEndid);

        editEventDateStartTv.setError(null);
        editEventDateEndTv.setError(null);
        editEventTimeEndTv.setError(null);
        editEventTimeStartTv.setError(null);
        TimePickerDialog timePickerDialog=new TimePickerDialog(mView.getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min)

            {
                dEndHour=hour;
                dEndMin=min;

                if((dMonth==dEndMonth && dDate==dEndDate && dYear==dEndYear && dHour>=dEndHour ) )
                {
                    editEventTimeEndTv.setError("Select an appropriate time");
                    editEventTimeEndTv.requestFocus();
                    return;
                }
                else
                {
                    editEventTimeEndTv.setError(null);
                    editEventDateStartTv.setError(null);
                    editEventTimeStartTv.setError(null);
                    editEventDateEndTv.setError(null);
                    String times=String.format("%02d:%02d",hour,min);
                    editEventTimeEndTv.setText(times);
                    k=1;
                }



            }
        },HOUR,MIN,true);
        timePickerDialog.show();

    }


    public void spinnerSelect(String spinner)

    {
        hostedBy=spinner;

    }


    public void createEvent(Uri mUri2,String extension,String eventId) {

        mUri=mUri2;
        this.eventId1=eventId;


        editEventEventNameEt = mView.findViewById(R.id.edit__event_eventnameEdittextId);
        editEventAdressEt = mView.findViewById(R.id.edit__event_addressid);
        editEventDescEt = mView.findViewById(R.id.edit__event_descriptionId);

        editEventDateStartTv=mView.findViewById(R.id.edit__event_dateid);
        editEventDateEndTv=mView.findViewById(R.id.edit__event_dateEndid);
        editEventTimeStartTv=mView.findViewById(R.id.edit__event_timeid);
        editEventTimeEndTv=mView.findViewById(R.id.edit__event_timeEndid);

        final String mEventname = editEventEventNameEt.getText().toString().trim();
        final String mAddress = editEventAdressEt.getText().toString().trim();
        final String mDescription = editEventDescEt.getText().toString();


        //
        //time date error checking
        if (mEventname.isEmpty()) {
            editEventEventNameEt.setError("Name can't be empty!");
            editEventEventNameEt.requestFocus();
            return;
        }
        if (mAddress.isEmpty()) {
            editEventAdressEt.setError("Address can't be empty!");
            editEventAdressEt.requestFocus();
            return;
        }


        if(x==1 ) {


            if (!((dYear > YEAR) || (dYear == YEAR && dMonth > MONTH) || (dYear == YEAR && MONTH == dMonth && dDate >= DATE))) {
                editEventDateStartTv.setError("Select an aprropriate date!");
                editEventDateStartTv.requestFocus();
                return;
            } else {
                editEventDateStartTv.setError(null);
            }
        }
        if(x==1 || y==1) {


            if (!((dEndYear > dYear) || (dYear == dEndYear && dMonth < dEndYear) || (dYear == dEndYear && dEndMonth == dMonth && dDate <= dEndDate))) {
                editEventDateEndTv.setError("Select an aprropriate date!");
                editEventDateEndTv.requestFocus();
                return;
            } else
                {
                editEventDateEndTv.setError(null);

            }
        }

        if(z==1) {


            if (!((dYear > YEAR) || (dYear == YEAR && dMonth > MONTH) || (dYear == YEAR && dMonth == MONTH && dDate > DATE) || (dYear == YEAR && dMonth == MONTH && dDate == DATE && (dHour > HOUR || (dHour == HOUR && dMin > MIN))))) {

                editEventTimeStartTv.setError("Select an appropriate time!");
                editEventTimeStartTv.requestFocus();
                return;
            } else {
                editEventTimeStartTv.setError(null);

            }
        }

        if(k==1) {


            if ((dMonth == dEndMonth && dDate == dEndDate && dYear == dEndYear && dHour >= dEndHour)) {
                editEventTimeEndTv.setError("Select an appropriate time");
                editEventTimeEndTv.requestFocus();
                return;
            } else {
                editEventTimeEndTv.setError(null);

            }
        }




        progressDialog = new ProgressDialog(mView.getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        //Adding to firestore



        if (mUri == null ) {

            if (imageUri.equals("No Image"))imageUri = "No Image";


            storeInDatabase(mEventname, mAddress, mDescription);


        } else {

            final StorageReference fileRef = storageReference.child(System.currentTimeMillis()
                    + "." + extension);



            fileRef.putFile(mUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override

                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imageUri = uri.toString();

                                    storeInDatabase(mEventname, mAddress, mDescription);
                                }
                            })
                            ;
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(mView.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });


        }
    }

    private void storeInDatabase(String mName,String mAdd,String mDesc)
    {
        mEventName=mName;
        mAddress=mAdd;
        mDescription=mDesc;
       // CreateModel createModel=new CreateModel(hostedBy,mEventName,mAddress,mDescription,imageUri,"gfgj",mYear,mMonth+1,mDate,mEndYear2,mEndMonth2+1,mEndDate2,mHour,mMin,mEndHour,mEndMin,System.currentTimeMillis(),0);

                CreateModel createModel2=new CreateModel(hostedBy,mEventName,mAddress,mDescription,imageUri,eventId1,dYear,dMonth,dDate,dEndYear,dEndMonth,dEndDate,dHour,dMin,dEndHour,dEndMin,createAt,0);

                FirebaseFirestore.getInstance().collection("Events").document(eventId1).set(createModel2).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override

                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful())
                        {

                           // SharedPreferences sharedPreferences = mView.getContext().getSharedPreferences("aboutFragData", Context.MODE_PRIVATE);

                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString("description", mDescription);
                            editor.putString("eventName", mEventName);
                            editor.putInt   ("startDate", dDate);
                            editor.putInt   ("endDate", dEndDate);
                            editor.putInt   ("startHour", dHour);
                            editor.putInt   ("endHour", dEndHour);
                            editor.putInt   ("startMin", dMin);
                            editor.putInt   ("endMin", dEndMin);
                            editor.putInt   ("startMonth", dMonth);
                            editor.putInt   ("endMonth", dEndMonth);
                            editor.putInt   ("starYear", dYear);
                            editor.putInt   ("endYear", dEndYear);
                            editor.putString   ("hostedBy", hostedBy);
                            editor.putString   ("imageUri", imageUri);
                            editor.putString   ("address", mAddress);
                            editor.putInt   ("refresh2", 0);

                            editor.apply();

                            SingleEventDetails singleEventDetails=new SingleEventDetails();

                            Toast.makeText(context, "Upload Succesfull", Toast.LENGTH_LONG).show();
                            alertDialog.cancel();

                            mActivity.recreate();
                            progressDialog.cancel();
                            //context.startActivity(new Intent(context, Bottom_Navigation.class));
                        }
                        else
                        {
                            progressDialog.cancel();
                            Toast.makeText(context, "Not Succesfull", Toast.LENGTH_LONG).show();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();

                        progressDialog.cancel();
                    }
                });
            }


    }


