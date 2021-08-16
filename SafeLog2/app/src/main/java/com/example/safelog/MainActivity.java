package com.example.safelog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import net.sqlcipher.database.SQLiteDatabase;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "dev" ;

    //recycler view declarations
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    public List<ModelClass> grplist;  //list that is given to recyclerview
    public List<GroupModelClass> grpnamelist; // list of group names and id from database
    public List<PaslistClass> paslist; //list of password names and group that it belongs
    AdapterClass Adapter;

    FloatingActionButton  addbtn;
    ExtendedFloatingActionButton adpasbtn, adgrpbtn;   //Fab Declarations
    boolean isfabvisible;

    Dialog dialog;
    Button create_btn;          //group dialog Declarations
    TextView title;
    TextView name;

    Dialog pasdialog;
    Button pascanclbtn;
    Button pascreatebtn;
    List<BlockModelClass> colorlist;
    ArrayAdapter<ModelClass> grpselc_adapter; //password dialog Declarations
    BlockAdapterClass Blockadapter;
    Spinner colourspinner;
    Spinner paswordtype;
    Spinner grpselect;
    EditText paswordtitletxt;
    EditText usrnnametxt;
    EditText paswrdtxt;
    String pastype;
    int grpid;
    int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SQLiteDatabase.loadLibs(this);
        fabview();                    // initialises fab buttons (add password and add group button)
        datainit();                  // initialises data for recycler view
        recyclerinit();              // initialises recyclerview

    }

    public void datainit() {

        grplist = new ArrayList<>();
        paslist = new ArrayList<>();
        DBClass db = new DBClass(this);
        grpnamelist = db.readGroupName();//stores group names and their ids
        paslist =  db.readpasname(); // stores password title and group to paslist
        for (GroupModelClass groupModelClass:grpnamelist)
        {
            /*gets only the group name from grpnamelist since we only need group name for recycler view */
            String groupname = groupModelClass.grpname;
            int position = groupModelClass.id;
            grplist.add(new ModelClass(groupname,position));

        }
        for (PaslistClass pass:paslist) {

            Log.d(TAG, "passlist content\n");
            Log.d(TAG, pass.title);
            Log.d(TAG, String.valueOf(pass.grpid));
            Log.d(TAG, String.valueOf(pass.color));
            Log.d(TAG, String.valueOf(pass.pasid));
        }
    }


    private void recyclerinit() {

        recyclerView = findViewById(R.id.recycler);   //finds recycler in main activity
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL); //set the orientation of recyclerviwe
        Adapter = new AdapterClass(grplist);
        recyclerView.setAdapter(Adapter);

    }

    private void fabview()
    {
        adpasbtn = findViewById(R.id.addpasbtn);
        adgrpbtn = findViewById(R.id.addgrpbtn);
        addbtn = findViewById(R.id.mainaddbtn);

         /*adpasbtn and adgrpbtn are initially set to visibility GONE since they only appear if we press addbtn */
        adpasbtn.setVisibility(View.GONE);
        adgrpbtn.setVisibility(View.GONE);



        addbtn.setOnClickListener(view -> {
            if(!isfabvisible)
            {
                //once adbtn clciked they are shown
                adpasbtn.show();
                adgrpbtn.show();
                isfabvisible=true;
            }
            else
            {
                //using hide becoz i think animation is much better
                 adpasbtn.hide();
                 adgrpbtn.hide();
                 isfabvisible=false;
            }
        });

        adgrpbtn.setOnClickListener(view -> {
            if(dialog==null)
            initgrpdialog();                        //only needed to initialize group dialog once

            dialog.show();                         //shows grp dialog
            create_btn.setOnClickListener(view1 -> {
                // new group is created by user so it should be added to database
                DBClass db = new DBClass(MainActivity.this);
                GroupModelClass groupModelClass;
                String newgroupname;
                try
                {
                    groupModelClass = new GroupModelClass(-1,name.getText().toString());
                    db.insertGroup(groupModelClass);
                    newgroupname = groupModelClass.grpname;
                    updateRecyclerview(newgroupname);    // updates the recyclerview with new group
                    dialog.dismiss();        //dialog closes
                }
                catch(Exception e)
                {

                }
            });

            //once the dialog is opened these buttons need to be hide so that they are not obstructing the view
            adgrpbtn.hide();
            adpasbtn.hide();
            isfabvisible =false;    // sets flag to false to show that they are visible
        });

        adpasbtn.setOnClickListener(view -> {
            if(pasdialog==null)
                initpasdialog(); // need to only initialize password dialog once
            //once the dialog is opened these buttons need to be hide so that they are not obstructing the view
            pasdialog.show();
            adgrpbtn.hide();
            adpasbtn.hide();
            isfabvisible =false; // sets flag to false to show that they are visible
        });

    }

    private void  initgrpdialog()
    {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.editgroup_dialog); //editgroup_dialog.xml
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.dialog_background));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        title = dialog.findViewById(R.id.grouptitle);
        create_btn = dialog.findViewById(R.id.savegrpbtn);
        name =  dialog.findViewById(R.id.newgrpname);
        title.setText("Create new Group");
    }

    private void initpasdialog()
    {
        pasdialog = new Dialog(this);
        pasdialog.setContentView(R.layout.pasword_dialog); //pasword_dialog.xml
        pasdialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.dialog_background));
        pasdialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        pasdialog.getWindow().getDecorView().setFocusable(true);
        pasdialog.getWindow().getDecorView().setFocusableInTouchMode(true);

        pascanclbtn = pasdialog.findViewById(R.id.canclpasbtn);
        pascreatebtn = pasdialog.findViewById(R.id.savepasbtn);
        paswordtitletxt =pasdialog.findViewById(R.id.newpasname);
        paswrdtxt = pasdialog.findViewById(R.id.paswrd);
        usrnnametxt = pasdialog.findViewById(R.id.usrname);
        colourspinner = pasdialog.findViewById(R.id.blockspiner);
        grpselect =pasdialog.findViewById(R.id.grpselecspiner);
        paswordtype = pasdialog.findViewById(R.id.pastype);

        if(colorlist==null)
        initblocklist();
        Blockadapter = new BlockAdapterClass(this, (ArrayList<BlockModelClass>) colorlist);
        colourspinner.setAdapter(Blockadapter);

        ArrayAdapter<CharSequence> pastype_adapter = ArrayAdapter.createFromResource(this,R.array.paswrdtype,R.layout.support_simple_spinner_dropdown_item);
        pastype_adapter.setDropDownViewResource(R.layout.spinnerdropdown);
        paswordtype.setAdapter(pastype_adapter);

        grpselc_adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, grplist);
        grpselc_adapter.setDropDownViewResource(R.layout.spinnerdropdown);
        grpselect.setAdapter(grpselc_adapter);

         // spinner setonitem initialising
        colourspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                BlockModelClass colorblock = (BlockModelClass) colourspinner.getItemAtPosition(i);
                color =colorblock.colorid;
                Log.d(TAG, "colorid: "+color);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        paswordtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pastype = paswordtype.getItemAtPosition(i).toString();
                Log.d(TAG, "passtype: "+paswordtype.getItemAtPosition(i).toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        grpselect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ModelClass grp = (ModelClass) grpselect.getItemAtPosition(i);
                grpid = grp.pos;
                Log.d(TAG, "groupid: "+grpid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        pascanclbtn.setOnClickListener(view -> pasdialog.dismiss());
        pascreatebtn.setOnClickListener(view -> {
            addpass();
            pasdialog.dismiss();
        });
    }



    private void addpass()
    {
        String title;
        String usrname;
        String paswrd;
        paswordtype = pasdialog.findViewById(R.id.pastype);


        if(pasdialog!=null)
        {


            if(paswrdtxt.getText().toString().isEmpty()||usrnnametxt.getText().toString().isEmpty()|| paswordtitletxt.getText().toString().isEmpty())
            {
                Toast.makeText(this,"One or more fields are empty",Toast.LENGTH_SHORT).show();
            }
            else
            {
                title = paswordtitletxt.getText().toString();
                usrname =usrnnametxt.getText().toString();
                paswrd = paswrdtxt.getText().toString();


                DBClass db = new DBClass(this);
                PassModelClass passModelClass = new PassModelClass(title,usrname,paswrd,pastype,grpid,-1,color);
                db.insertPass(passModelClass);
            }
        }
    }

    private void updateRecyclerview(String grpname)
    {
        grplist.add(new ModelClass(grpname, grplist.size()));
        Adapter.notifyItemInserted(grplist.size());
    }



    private void initblocklist()
    {
        colorlist = new ArrayList<>();
        colorlist.add(new BlockModelClass(R.drawable.colourbox_red,R.color.red));
        colorlist.add(new BlockModelClass(R.drawable.colourbox_pink,R.color.pink));
        colorlist.add(new BlockModelClass(R.drawable.colourbox_purple,R.color.purple));
        colorlist.add(new BlockModelClass(R.drawable.colourbox_dark_purple,R.color.dark_purple));
        colorlist.add(new BlockModelClass(R.drawable.colourbox_discord_blue,R.color.discord_blue));
        colorlist.add(new BlockModelClass(R.drawable.colourbox_sky_blue,R.color.sky_blue));
        colorlist.add(new BlockModelClass(R.drawable.colourbox_dark_teal,R.color.dark_teal));
        colorlist.add(new BlockModelClass(R.drawable.colourbox_dark_green,R.color.dark_green));
        colorlist.add(new BlockModelClass(R.drawable.colourbox_leaf_green,R.color.leaf_green));
        colorlist.add(new BlockModelClass(R.drawable.colourbox_leaf_yellow,R.color.leaf_yellow));
        colorlist.add(new BlockModelClass(R.drawable.colourbox_orange,R.color.orange));
        colorlist.add(new BlockModelClass(R.drawable.colourbox_orange_red,R.color.orange_red));
        colorlist.add(new BlockModelClass(R.drawable.colourbox_brown,R.color.brown));
        colorlist.add(new BlockModelClass(R.drawable.colourbox_grey,R.color.grey));
        colorlist.add(new BlockModelClass(R.drawable.colourbox_blue_grey,R.color.blue_grey));
    }



}