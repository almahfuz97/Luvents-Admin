package com.example.myapplication.Classes;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myapplication.Models.CreateModel;
import com.example.myapplication.R;
import com.example.myapplication.activities.Bottom_Navigation;
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

import java.util.Calendar;

public class CreateEventWorks {


    Calendar calendar=Calendar.getInstance();
    final int YEAR=calendar.get(Calendar.YEAR);
    final int MONTH=calendar.get(Calendar.MONTH);
    int DATE=calendar.get(Calendar.DATE);

    int HOUR=calendar.get(Calendar.HOUR);
    int MIN=calendar.get(Calendar.MINUTE);

    private ProgressDialog progressDialog;

    StorageTask storageTask;
    Uri mUri;

    private StorageReference storageReference=FirebaseStorage.getInstance().getReference("images");
    String imageUri;

    View view;
    Button choosePhoto;
    private EditText eventName,address,description;

    private String mEventName,mAddress,mDescription,hostedBy;

    private int mDate=DATE,mYear=YEAR,mMonth=MONTH,mEndDate2= DATE,mEndYear2=YEAR,mEndMonth2=MONTH,mMin=MIN,mHour=HOUR,mEndMin=MIN,mEndHour=HOUR;

    private TextView date,dateEnd,timeEnd,time;

    private Context context;
    private Activity mActivity;


    public CreateEventWorks(Context c, Activity activity) {

        context=c;
        mActivity=activity;
    }

    public void datePicker()
    {
        date=mActivity.findViewById(R.id.dateid);
        dateEnd=mActivity.findViewById(R.id.dateEndid);
        time=mActivity.findViewById(R.id.timeid);
        timeEnd=mActivity.findViewById(R.id.timeEndid);

        date.setError(null);
        dateEnd.setError(null);
        timeEnd.setError(null);
        time.setError(null);

        DatePickerDialog datePickerDialog=new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int Year, int Month, int Date) {

                mYear=Year;
                mMonth=Month;
                mDate=Date;




                if(!((mYear>YEAR )|| (mYear==YEAR && mMonth>MONTH) || (mYear==YEAR && MONTH==mMonth && mDate>=DATE)))
                {
                    date.setError("Select an aprropriate date!");
                    date.requestFocus();
                    return;
                }
                else
                {
                    date.setError(null);
                    dateEnd.setError(null);
                    timeEnd.setError(null);
                    time.setError(null);
                    String dates=Date+"/"+(Month+1)+"/"+Year;
                    date.setText(dates);
                }

            }
        },YEAR,MONTH,DATE);

        datePickerDialog.show();

    }
   public void dateEndPicker()
    {
        date=mActivity.findViewById(R.id.dateid);
        dateEnd=mActivity.findViewById(R.id.dateEndid);
        time=mActivity.findViewById(R.id.timeid);
        timeEnd=mActivity.findViewById(R.id.timeEndid);

        date.setError(null);
        dateEnd.setError(null);
        timeEnd.setError(null);
        time.setError(null);

        DatePickerDialog datePickerDialog=new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int Year, int Month, int Date) {

                mEndYear2=Year;
                mEndMonth2=Month;
                mEndDate2=Date;

                if(!((mEndYear2>mYear )|| (mYear==mEndYear2 && mMonth<mEndMonth2) || (mYear==mEndYear2 && mEndMonth2==mMonth && mDate<=mEndDate2)))
                {
                    dateEnd.setError("Select an aprropriate date!");
                    dateEnd.requestFocus();
                    return;
                }
                else
                {
                    dateEnd.setError(null);
                    time.setError(null);
                    date.setError(null);
                    timeEnd.setError(null);
                    String dates=Date+"/"+(Month+1)+"/"+Year;
                    dateEnd.setText(dates);
                }




            }
        },YEAR,MONTH,DATE);
        datePickerDialog.show();

    }

    public void timePicker()
    {
        date=mActivity.findViewById(R.id.dateid);
        dateEnd=mActivity.findViewById(R.id.dateEndid);
        time=mActivity.findViewById(R.id.timeid);
        timeEnd=mActivity.findViewById(R.id.timeEndid);

        date.setError(null);
        dateEnd.setError(null);
        timeEnd.setError(null);
        time.setError(null);



        TimePickerDialog timePickerDialog=new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                mMin=min;
                mHour=hour;

                if(!((mYear>YEAR) || (mYear==YEAR && mMonth>MONTH) || (mYear==YEAR && mMonth==MONTH && mDate>DATE) || (mYear==YEAR && mMonth==MONTH && mDate==DATE && (mHour>HOUR || (mHour==HOUR && mMin>MIN)))))
                {

                    time.setError("Select an appropriate time!");
                    time.requestFocus();
                    return;
                }
                else
                {
                    time.setError(null);
                    date.setError(null);
                    dateEnd.setError(null);
                    timeEnd.setError(null);
                    String times=String.format("%02d:%02d",hour,min);
                    time.setText(times);
                }


            }
        },HOUR,MIN,true);

        timePickerDialog.show();

    }
    public void timeEndPicker()
    {
        date=mActivity.findViewById(R.id.dateid);
        dateEnd=mActivity.findViewById(R.id.dateEndid);
        time=mActivity.findViewById(R.id.timeid);
        timeEnd=mActivity.findViewById(R.id.timeEndid);

        date.setError(null);
        dateEnd.setError(null);
        timeEnd.setError(null);
        time.setError(null);
        TimePickerDialog timePickerDialog=new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min)

            {
                mEndHour=hour;
                mEndMin=min;

                if((mMonth==mEndMonth2 && mDate==mEndDate2 && mYear==mEndYear2 && mHour>=mEndHour ) )
                {
                    timeEnd.setError("Select an appropriate time");
                    timeEnd.requestFocus();
                    return;
                }
                else
                {
                    timeEnd.setError(null);
                    date.setError(null);
                    time.setError(null);
                    dateEnd.setError(null);
                    String times=String.format("%02d:%02d",hour,min);
                    timeEnd.setText(times);
                }



            }
        },HOUR,MIN,true);
        timePickerDialog.show();

    }


    public void spinnerSelect(String spinner)

    {
        hostedBy=spinner;

    }



    public void createEvent(Uri mUri2,String extension) {

        mUri=mUri2;

        eventName = mActivity.findViewById(R.id.eventnameEdittextId);
        address = mActivity.findViewById(R.id.addressid);
        description = mActivity.findViewById(R.id.descriptionId);
        date = mActivity.findViewById(R.id.dateid);
        dateEnd = mActivity.findViewById(R.id.dateEndid);
        time = mActivity.findViewById(R.id.timeid);
        timeEnd = mActivity.findViewById(R.id.timeEndid);

        final String mEventname = eventName.getText().toString().trim();
        final String mAddress = address.getText().toString().trim();
        final String mDescription = description.getText().toString();


        //
        //time date error checking
        if (mEventname.isEmpty()) {
            eventName.setError("Name can't be empty!");
            eventName.requestFocus();
            return;
        }
        if (mAddress.isEmpty()) {
            address.setError("Address can't be empty!");
            address.requestFocus();
            return;
        }


        if (!((mYear > YEAR) || (mYear == YEAR && mMonth > MONTH) || (mYear == YEAR && MONTH == mMonth && mDate >= DATE))) {
            date.setError("Select an aprropriate date!");
            date.requestFocus();
            return;
        } else {
            date.setError(null);
        }
        if (!((mEndYear2 > mYear) || (mYear == mEndYear2 && mMonth < mEndMonth2) || (mYear == mEndYear2 && mEndMonth2 == mMonth && mDate <= mEndDate2))) {
            dateEnd.setError("Select an aprropriate date!");
            dateEnd.requestFocus();
            return;
        } else {
            dateEnd.setError(null);

        }
        if (!((mYear > YEAR) || (mYear == YEAR && mMonth > MONTH) || (mYear == YEAR && mMonth == MONTH && mDate > DATE) || (mYear == YEAR && mMonth == MONTH && mDate == DATE && (mHour > HOUR || (mHour == HOUR && mMin > MIN))))) {

            time.setError("Select an appropriate time!");
            time.requestFocus();
            return;
        } else {
            time.setError(null);

        }
        if ((mMonth == mEndMonth2 && mDate == mEndDate2 && mYear == mEndYear2 && mHour >= mEndHour)) {
            timeEnd.setError("Select an appropriate time");
            timeEnd.requestFocus();
            return;
        } else {
            timeEnd.setError(null);

        }


        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Creating Event...");
        progressDialog.show();


        //Adding to firestore

        if (mUri == null) {
            imageUri = "No Image";

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
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });


        }
    }

    private void storeInDatabase(String mName,String mAdd,String mDesc)
    {
        mEventName=mName;
        mAddress=mAdd;
        mDescription=mDesc;
        CreateModel createModel=new CreateModel(hostedBy,mEventName,mAddress,mDescription,imageUri,"gfgj",mYear,mMonth+1,mDate,mEndYear2,mEndMonth2+1,mEndDate2,mHour,mMin,mEndHour,mEndMin,System.currentTimeMillis(),0);
        FirebaseFirestore.getInstance().collection("Events").add(createModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override

            public void onComplete(@NonNull Task<DocumentReference> task) {
                progressDialog.cancel();

                CreateModel createModel2=new CreateModel(hostedBy,mEventName,mAddress,mDescription,imageUri,task.getResult().getId(),mYear,mMonth+1,mDate,mEndYear2,mEndMonth2+1,mEndDate2,mHour,mMin,mEndHour,mEndMin,System.currentTimeMillis(),0);

                FirebaseFirestore.getInstance().collection("Events").document(task.getResult().getId()).set(createModel2).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override

                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful())
                        {
                            Toast.makeText(context, "Upload Succesfull", Toast.LENGTH_LONG).show();
                            context.startActivity(new Intent(context, Bottom_Navigation.class));
                        }
                        else
                        {
                            Toast.makeText(context, "Not Succesfull", Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.cancel();
                Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();

            }
        });


    }

}
