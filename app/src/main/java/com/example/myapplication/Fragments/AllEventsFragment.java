package com.example.myapplication.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialog;
import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialogListener;
import com.example.myapplication.activities.Bottom_Navigation;
import com.example.myapplication.activities.SingleEventDetails;
import com.example.myapplication.Adapters.EventsAdapter;
import com.example.myapplication.Models.CreateModel;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */

public class AllEventsFragment extends Fragment {


    public AllEventsFragment() {
        // Required empty public constructor
    }

    private FirebaseFirestore firestore;
    private FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    private CollectionReference collectionReference;
    private Query query;

    String strMonth,timeFormat,mdayOfWeek;

    List<CreateModel> createModelList;

    //views
    private Spinner spinner;
    private EditText searchbar;


    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    ProgressBar progressBar;
    EventsAdapter adapter;

    List<CreateModel> eventDatas;
    View view;


    ProgressDialog progressDialog,progressDialog2;
    String refresh="NR";
    SharedPreferences.Editor editor;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_all_events, container, false);

        //finding Views
        recyclerView=view.findViewById(R.id.eventsrecyclerviewid);
        progressBar=view.findViewById(R.id.recyprogragbarId);
        spinner=view.findViewById(R.id.sorted_spinner_id);
        searchbar=view.findViewById(R.id.editsearchId);

        searching();

        eventDatas=new ArrayList<>();
        createModelList=new ArrayList<>();

        //firebase
        firestore=FirebaseFirestore.getInstance();
        adapter=new EventsAdapter(eventDatas,getContext());
        collectionReference=firestore.collection("Events");


        //progressDialogue
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");

        progressDialog2=new ProgressDialog(getActivity());
        progressDialog2.setMessage("Deleting..");


        //spinnerOnitem click
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item=spinner.getItemAtPosition(i).toString();

                if(item.equals("All"))
                {
                    query=collectionReference.orderBy("create_at", Query.Direction.DESCENDING);
                    setRecycler();

                }
                if(item.equals("LUCC"))
                {
                    query=collectionReference.whereEqualTo("hostedBy", item);
                    setRecycler();

                }
                if(item.equals("LUPS"))
                {
                    query=collectionReference.whereEqualTo("hostedBy", item);
                    setRecycler();
                }
                if(item.equals("LUCuC"))
                {
                    query=collectionReference.whereEqualTo("hostedBy", item);
                    setRecycler();
                }
                if(item.equals("LUSSC"))
                {
                    query=collectionReference.whereEqualTo("hostedBy", item);
                    setRecycler();
                }
                if(item.equals("LUDC"))
                {
                    query=collectionReference.whereEqualTo("hostedBy", item);
                    setRecycler();
                }
                if(item.equals("Orpheus"))
                {
                    query=collectionReference.whereEqualTo("hostedBy", item);
                    setRecycler();

                }
                if(item.equals("BC"))
                {
                    item="Banned Community";
                    query=collectionReference.whereEqualTo("hostedBy", item);
                    setRecycler();

                }
                if(item.equals("Interest"))
                {
                    query=collectionReference.orderBy("interested",Query.Direction.DESCENDING);
                    setRecycler();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        swipeRefreshLayout=view.findViewById(R.id.swipeRefreshId);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setRecycler();
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);

            }
        });

        return  view;
    }


    //
    public void setRecycler()
    {
        progressBar.setVisibility(View.VISIBLE);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful())
                {
                    eventDatas.clear();
                    for (DocumentSnapshot doc: task.getResult())
                    {

                        CreateModel data=doc.toObject(CreateModel.class);
                        eventDatas.add(data);

                    }
                    adapter=new EventsAdapter(eventDatas,getActivity());

                    onItemClick(adapter);
                    onItemLongClick(adapter);

                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());


                     recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setHasFixedSize(true);


                    recyclerView.setAdapter(adapter);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
            }
        })
        ;
    }

    private void onItemLongClick(EventsAdapter adapter) {

        adapter.setOnItemLongClickListener(new EventsAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position, final CreateModel createModel) {


                new TTFancyGifDialog.Builder(getActivity())
                        .setTitle("Delete!")
                        .setMessage("Do you want to delete the event?")
                        .setPositiveBtnText("Ok")
                        .setPositiveBtnBackground("#22b573")
                        .setNegativeBtnText("Cancel")
                        .setNegativeBtnBackground("#c1272d")
                        .setGifResource(R.drawable.update)      //pass your gif, png or jpg
                        .isCancellable(true)
                        .OnPositiveClicked(new TTFancyGifDialogListener() {
                            @Override
                            public void OnClick() {
                                //progressDialog.show();
                                progressDialog2.show();

                                firebaseFirestore.collection("Events").document(createModel.getEventId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {


                                        firebaseFirestore.collection("RegistrationInfo")
                                                .document(createModel.getEventId()).delete();

                                        Toast.makeText(getContext(),"Deleted Succesfully",Toast.LENGTH_LONG).show();
                                        getActivity().recreate();
                                        progressDialog2.cancel();

                                        // progressDialog.cancel();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });


                            }
                        })
                        .OnNegativeClicked(new TTFancyGifDialogListener() {
                            @Override
                            public void OnClick() {


                            }
                        })
                        .build();
            }
        });
    }

    //itemClick
    public void onItemClick(EventsAdapter adapter)
    {
        adapter.setOnItemClickListener(new EventsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, CreateModel createModel) {

                Intent intent=new Intent(getContext(), SingleEventDetails.class);
                intent.putExtra("eventName",createModel.getEventName());
                intent.putExtra("eventId",createModel.getEventId());
                intent.putExtra("hostedBy",createModel.getHostedBy());
                intent.putExtra("going",createModel.getInterested()+"");
                intent.putExtra("desc",createModel.getDescription());
                intent.putExtra("StartDate",createModel.getDate());
                intent.putExtra("EndDate",createModel.geteDate());
                intent.putExtra("StartYear",createModel.getYear());
                intent.putExtra("EndYear",createModel.geteYear());
                intent.putExtra("StartMonth",createModel.getMonth());
                intent.putExtra("EndMonth",createModel.geteMonth());
                intent.putExtra("StartHour",createModel.getHour());
                intent.putExtra("EndHour",createModel.geteHour());
                intent.putExtra("StartMin",createModel.getMin());
                intent.putExtra("EndMin",createModel.geteMin());
                intent.putExtra("Address",createModel.getAddress());


                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("aboutFragData", Context.MODE_PRIVATE);

                editor= sharedPreferences.edit();

                editor.putString("description", createModel.getDescription());
                editor.putString("eventName", createModel.getEventName());
                editor.putInt   ("startDate", createModel.getDate());
                editor.putInt   ("endDate", createModel.geteDate());
                editor.putInt   ("startHour", createModel.getHour());
                editor.putInt   ("endHour", createModel.geteHour());
                editor.putInt   ("startMin", createModel.getMin());
                editor.putInt   ("endMin", createModel.geteMin());
                editor.putInt   ("startMonth", createModel.getMonth());
                editor.putInt   ("endMonth", createModel.geteMonth());
                editor.putInt   ("startYear", createModel.getYear());
                editor.putInt   ("endYear", createModel.geteYear());
                editor.putString   ("address", createModel.getAddress());
                editor.putString("imageUri",createModel.getImageUri()+"");
                editor.putLong("createAt",createModel.getCreate_at());

               // editor.putString("going", String.valueOf(createModel.getInterested()));
                editor.putString("eventId",createModel.getEventId());
                editor.putString("hostedBy",createModel.getHostedBy());
                editor.putString("going",String.valueOf(createModel.getInterested()));
                //

                  refresh=sharedPreferences.getString("REFRESHED","");



                editor.apply();

                intent.putExtra("img",createModel.getImageUri());
                monthGetter(createModel);
                intent.putExtra("dateTime",mdayOfWeek+", "+strMonth+" "+ createModel.getDate()+", "+createModel.getYear()+" at "+timeFormat);
                intent.putExtra("location",createModel.getAddress());
                startActivity(intent);
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        if (refresh.equals("R")){
            getActivity().recreate();
            editor.putString("REFRESHED","NR");
        }
    }

    //searchingFilter
    public void searching()
    {
        searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

               try {

                   String mText = searchbar.getText().toString().trim();
                   List<CreateModel> mList = filter(eventDatas, mText);
                   adapter.setFilter(mList);
               }
               catch (Exception e)
               {

               }



            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private List<CreateModel> filter(List<CreateModel> createModelList, String mText) {

        mText=mText.toLowerCase().trim();

        List<CreateModel> searchString=new ArrayList<>();

        for(CreateModel createModelData : createModelList)
        {
            String eventname=createModelData.getEventName().toLowerCase().trim();

            if(eventname.contains(mText))
            {
                searchString.add(createModelData);
            }
        }
        return  searchString;

    }


    public void  monthGetter(CreateModel createModel)
    {
        if(createModel.getMonth()==1) strMonth="January";
        if(createModel.getMonth()==2) strMonth="February";
        if(createModel.getMonth()==3) strMonth="March";
        if(createModel.getMonth()==4) strMonth="April";
        if(createModel.getMonth()==5) strMonth="May";
        if(createModel.getMonth()==6) strMonth="June";
        if(createModel.getMonth()==7) strMonth="July";
        if(createModel.getMonth()==8) strMonth="August";
        if(createModel.getMonth()==9) strMonth="September";
        if(createModel.getMonth()==10) strMonth="October";
        if(createModel.getMonth()==11) strMonth="November";
        if(createModel.getMonth()==12) strMonth="December";

        //timeformat

        if (createModel.getHour()>12)
        {
            int x=createModel.getHour()-12;

            timeFormat=x+" pm";
        }
        else timeFormat=createModel.getHour()+" am";

        //dayOfweek
        GregorianCalendar date2 = new GregorianCalendar(createModel.geteYear(), createModel.geteMonth(), createModel.getDate()-4);

        int dayOfWeek=date2.get(date2.DAY_OF_WEEK);
        if(dayOfWeek==1)mdayOfWeek="Monday";
        if(dayOfWeek==2)mdayOfWeek="Tuesday";
        if(dayOfWeek==3)mdayOfWeek="Wednesday";
        if(dayOfWeek==4)mdayOfWeek="Thursday";
        if(dayOfWeek==5)mdayOfWeek="Friday";
        if(dayOfWeek==6)mdayOfWeek="Saturday";
        if(dayOfWeek==7)mdayOfWeek="Sunday";



    }
}
