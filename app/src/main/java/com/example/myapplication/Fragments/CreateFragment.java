package com.example.myapplication.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Classes.CreateEventWorks;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateFragment extends Fragment implements View.OnClickListener {


    public CreateFragment() {
        // Required empty public constructor
    }

    private static final int PICK_IMAGE_REQUEST = 1;
    private int  STORAGE_PERMISSION_CODE = 1;
    private Uri mUri=null;
    private  String ImageUri;
    int x;
    int staticWorksEditEventid=0;


    Calendar calendar=Calendar.getInstance();
    final int YEAR=calendar.get(Calendar.YEAR);
    final int MONTH=calendar.get(Calendar.MONTH);
    int DATE=calendar.get(Calendar.DATE);

    int hour=calendar.get(Calendar.HOUR);
    int min=calendar.get(Calendar.MINUTE);

    View view;
    Button choosePhoto,createEvent;
    EditText eventName,address,description;
    ImageView imageView;
    TextView date,time,dateEnd,timeEnd;

    CreateEventWorks worksCreateFragment;
    private Spinner spinner;


    @Override
    public void onStart() {
        super.onStart();
        x=0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_create, container, false);
//
//        eventName=view.findViewById(R.id.eventnameEdittextId);
//        address=view.findViewById(R.id.addressid);
//        description=view.findViewById(R.id.descriptionId);
//
//
        spinner=view.findViewById(R.id.hostedSpinnerId);
        date=view.findViewById(R.id.dateid);
        time=view.findViewById(R.id.timeid);
        dateEnd=view.findViewById(R.id.dateEndid);
        timeEnd=view.findViewById(R.id.timeEndid);
        createEvent=view.findViewById(R.id.createEventbtnid);
        imageView=view.findViewById(R.id.imageviewid);
        choosePhoto=view.findViewById(R.id.choosephotoid);

        date.setOnClickListener(this);
        time.setOnClickListener(this);
        dateEnd.setOnClickListener(this);
        timeEnd.setOnClickListener(this);
        createEvent.setOnClickListener(this);
        choosePhoto.setOnClickListener(this);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item=spinner.getItemAtPosition(i).toString();

                if(item.equals("LUCC"))
                {
                    worksCreateFragment.spinnerSelect(item);
                }
                if(item.equals("LUPS"))
                {
                    worksCreateFragment.spinnerSelect(item);
                }
                if(item.equals("LUCuC"))
                {
                    worksCreateFragment.spinnerSelect(item);
                }
                if(item.equals("LUSSC"))
                {
                    worksCreateFragment.spinnerSelect(item);
                }
                if(item.equals("LUDC"))
                {
                    worksCreateFragment.spinnerSelect(item);
                }
                if(item.equals("Orpheus"))
                {
                    worksCreateFragment.spinnerSelect(item);
                }
                if(item.equals("BC"))
                {
                    item="Banned Community";
                    worksCreateFragment.spinnerSelect(item);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                String item="LUCC";

            }
        });

        String dates=String.format("%02d",DATE)+"/"+String.format("%02d",MONTH+1)+"/"+YEAR;
        date.setText(dates);
        dateEnd.setText(dates);

        String times=String.format("%02d:%02d",hour,min);
        time.setText(times);
        timeEnd.setText(times);

        worksCreateFragment =new CreateEventWorks(getContext(),getActivity());

        return view;
    }



    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.dateid:
                worksCreateFragment.datePicker();
                break;
            case R.id.timeid:
                worksCreateFragment.timePicker();
                break;
            case R.id.dateEndid:
                worksCreateFragment.dateEndPicker();
                break;
            case R.id.timeEndid:
                worksCreateFragment.timeEndPicker();
                break;
            case R.id.createEventbtnid:

                if(mUri!=null) worksCreateFragment.createEvent(mUri,getFileExtension(mUri));
                else worksCreateFragment.createEvent(null,"");


                break;
            case R.id.choosephotoid:

                choosePhoto();
                break;
        }

    }



    public void choosePhoto()
    {
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            Intent intent = new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);


        } else {
            requestStoragePermission();

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE_REQUEST && resultCode== Activity.RESULT_OK && data!=null && data.getData()!=null)
        {
            mUri=data.getData();

            Picasso.get().load(mUri).fit().into(imageView);
            x=1;

            //Toast.makeText(getActivity(), "Successfull", Toast.LENGTH_SHORT).show();
        }
    }

    public void requestStoragePermission()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(getActivity()),
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(getActivity())
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);


                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getFileExtension(Uri uri) {


        ContentResolver resolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(resolver.getType(uri));
    }


}
