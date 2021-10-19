package com.example.safelog;

import static android.service.controls.ControlsProviderService.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import net.sqlcipher.database.SQLiteDatabase;

public class MainActivity extends AppCompatActivity implements PasdialogView {

    private static final String TAG = "dev" ;
    private static int cores;
    private static ExecutorService executorService;

    //recycler view declarations
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    public Allinfo allinfo;
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
    int grppos;
    int color;
    AppCompatAutoCompleteTextView search_text;  //search texiview
    Dialog pasviewdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean isfirstrun = getSharedPreferences("FIRSTRUN",MODE_PRIVATE).getBoolean("isfirstrun",true);



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!isfirstrun)
        {
            cores = Runtime.getRuntime().availableProcessors();
            executorService = Executors.newFixedThreadPool(cores+1);
            SQLiteDatabase.loadLibs(this);
            Intent intent = getIntent();
            allinfo = intent.getParcelableExtra("allinfo");
            fabview();                    // initialises fab buttons (add password and add group button)// initialises data for recycler view
            recyclerinit();              // initialises recyclerview
            search_func();
        }

    }

    public void search_func()
    {
        List<String> pasnamelist;
        pasnamelist = new ArrayList<>(50);
        PaslistClass item;

        for(int i=0;i<paslist.size();++i)
        {
            item = paslist.get(i);
            pasnamelist.add(item.title);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinnerdropdown, pasnamelist);

        search_text = findViewById(R.id.searchtext);
        search_text.setThreshold(1);
        search_text.setAdapter(adapter);
        search_text.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);


                for(int i=0;i<paslist.size();++i)
                {
                    if(item.equals(paslist.get(i).title))
                    {
                        setpasdialog();
                        getdet(paslist.get(i).pasid,paslist.get(i).title,paslist.get(i).color);
                        pasviewdialog.show();
                        search_text.setText("");
                    }
                }
            }
        });

    }



    public void exestop()
    {
        executorService.shutdown();
    }


    private void recyclerinit() {

        try{
            grplist = new ArrayList<>();
            grpnamelist =allinfo.grouplist;
            for (GroupModelClass item: grpnamelist) {
                grplist.add(new ModelClass(item.grpname,item.id));
            }
            paslist = allinfo.paslist;
        }catch (NullPointerException ex)
        {
            paslist = new ArrayList<>();
        }
            Adapter = new AdapterClass(grplist,paslist);
            recyclerView = findViewById(R.id.recycler);   //finds recycler in main activity
            linearLayoutManager = new LinearLayoutManager(MainActivity.this);
            recyclerView.setLayoutManager(linearLayoutManager);
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL); //set the orientation of recyclerviwe
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
                if(!name.getText().toString().isEmpty())
                {
                    try
                    {
                        groupModelClass = new GroupModelClass(-1,name.getText().toString());

                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                db.insertGroup(groupModelClass);
                            }
                        });
                        newgroupname = groupModelClass.grpname;
                        updateRecyclerview(newgroupname);    // updates the recyclerview with new group
                        dialog.dismiss();        //dialog closes
                    }
                    catch(Exception e)
                    {

                    }
                }
                else
                    Toast.makeText(this,"Group name Cannot Be Empty",Toast.LENGTH_SHORT).show();

            });

            //once the dialog is opened these buttons need to be hide so that they are not obstructing the view
            adgrpbtn.hide();
            adpasbtn.hide();
            isfabvisible =false;    // sets flag to false to show that they are visible
        });

        adpasbtn.setOnClickListener(view -> {
            if(!grplist.isEmpty())   //password can only be added if atleast one group is created
            {
                if(pasdialog==null)
                    initpasdialog(); // need to only initialize password dialog once
                //once the dialog is opened these buttons need to be hide so that they are not obstructing the view
                pasdialog.show();
                adgrpbtn.hide();
                adpasbtn.hide();
                isfabvisible =false; // sets flag to false to show that they are visible
            }
            else
                Toast.makeText(MainActivity.this,"Create a Group First!",Toast.LENGTH_SHORT).show();

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
                grppos =i;
                Log.d(TAG, "groupid: "+grpid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        pascanclbtn.setOnClickListener(view -> pasdialog.dismiss());
        pascreatebtn.setOnClickListener(view -> {
            addpass();
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
                if(Adapter.getgrpbtncount(grpid)>=9)
                {
                    Toast.makeText(this,"Current group is full, please select another group",Toast.LENGTH_SHORT).show();
                }
                if(paswordtitletxt.getText().length()>12)
                {
                    Toast.makeText(this,"Maximum 12 characters allowed on Title",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    title = paswordtitletxt.getText().toString();
                    usrname =usrnnametxt.getText().toString();
                    paswrd = paswrdtxt.getText().toString();


                    DBClass db = new DBClass(this);
                    PassModelClass passModelClass = new PassModelClass(title,usrname,paswrd,pastype,grpid,-1,color);

                       executorService.execute(new Runnable() {
                           @Override
                           public void run() {
                               db.insertPass(passModelClass);
                           }
                       });

                    int pasid=1;
                       try{
                           pasid= paslist.get(paslist.size()-1).pasid+1;
                       }
                       catch (ArrayIndexOutOfBoundsException ignored){}


                    PaslistClass item = new PaslistClass(pasid,title,grpid,color,pastype,usrname,paswrd);
                    Adapter.notifyItemChanged(grppos);
                    paslist.add(item);
                    pasdialog.dismiss();

                }
            }
        }

    }

    private void updateRecyclerview(String grpname)
    {
        grplist.add(new ModelClass(grpname, grplist.size()+1));
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


    // definition for the interface PasdialogView responsible for the pasviewdialog when searched item is selected;

    @Override
    public void setpasdialog() {
        pasviewdialog = new Dialog(MainActivity.this);
        pasviewdialog.setContentView(R.layout.passview_dialog);
        pasviewdialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.dialog_background));
        pasviewdialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        Button cancle_btn = pasviewdialog.findViewById(R.id.canclpasviewbtn);


        cancle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pasviewdialog.dismiss();
            }
        });
    }

    @Override
    public void getdet(int id, String title, int color) {
        PaslistClass curpas =null;
        for (PaslistClass curitem:paslist) {
            if (curitem.pasid == id)
                curpas = curitem;
        }
        if(pasviewdialog!=null)
        {
            ClipboardManager clipboard = (android.content.ClipboardManager) pasviewdialog.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            EditText username = pasviewdialog.findViewById(R.id.usernametxt);
            EditText password = pasviewdialog.findViewById(R.id.passwordtxt);
            TextView passtitle = pasviewdialog.findViewById(R.id.passviewtitle);
            MaterialCardView passcard = pasviewdialog.findViewById(R.id.passviewtitlecard);
            AppCompatImageButton paseye_btn = pasviewdialog.findViewById(R.id.paseye);
            AppCompatImageButton usercopy = pasviewdialog.findViewById(R.id.usercopy);
            AppCompatImageButton passcopy = pasviewdialog.findViewById(R.id.pascopy);
            AppCompatImageButton delbtn =  pasviewdialog.findViewById(R.id.delpasbtn);
            AppCompatButton savbtn = pasviewdialog.findViewById(R.id.savepasviewbtn);
            TextWatcher checkempty = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String pas = password.getText().toString();
                    String usr = username.getText().toString();
                    Log.d(TAG, "onTextChanged: "+usr);

                    if(pas.isEmpty()&& usr.isEmpty())
                    {
                        savbtn.setEnabled(false);
                        savbtn.setBackgroundTintList(AppCompatResources.getColorStateList(MainActivity.this,R.color.grey));
                    }
                    else if(usr.isEmpty()||pas.isEmpty())
                    {
                        savbtn.setEnabled(false);
                        savbtn.setBackgroundTintList(AppCompatResources.getColorStateList(MainActivity.this,R.color.grey));
                    }
                    else
                    {
                        savbtn.setEnabled(true);
                        savbtn.setBackgroundTintList(AppCompatResources.getColorStateList(MainActivity.this,R.color.TitleBarColour));
                    }



                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            };



            paseye_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(password.getInputType()==(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD))
                    {
                        password.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    }
                    else
                    {
                        password.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    }


                }
            });

            usercopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text",username.getText());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(pasviewdialog.getContext(),"Username Copied",Toast.LENGTH_SHORT).show();
                }
            });

            passcopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text",password.getText());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(pasviewdialog.getContext(),"Password Copied",Toast.LENGTH_SHORT).show();

                }
            });

            delbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {

                            DBClass db = new DBClass(pasviewdialog.getContext());
                            db.delpass(id);
                        }
                    });
                    int pos=0;
                    int i=0;
                    for (PaslistClass paswd:paslist) {

                        if(paswd.pasid==id)
                        {
                            pos = i;
                        }
                        i++;
                    }
                    //btn_del = true;
                    paslist.remove(pos);
                    pasviewdialog.dismiss();

                }
            });


            PaslistClass finalCurpas = curpas;
            savbtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    if(username.getText().toString().equals(finalCurpas.username)&& password.getText().toString().equals(finalCurpas.passwrd))
                    {
                        Toast.makeText(pasviewdialog.getContext(),"No changes have made in username or password !",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {

                                finalCurpas.username =username.getText().toString();
                                finalCurpas.passwrd = password.getText().toString();
                                finalCurpas.pastype = "null";
                                DBClass db = new DBClass(pasviewdialog.getContext());
                                db.editpas(id, finalCurpas);

                            }
                        });

                        Toast.makeText(pasviewdialog.getContext(),"Changes Saved",Toast.LENGTH_SHORT).show();
                    }


                }


            });

            username.setText(curpas.username);
            password.setText(curpas.passwrd);
            username.addTextChangedListener(checkempty);
            password.addTextChangedListener(checkempty);
            passtitle.setText(title);
            passcard.setBackgroundTintList(AppCompatResources.getColorStateList(MainActivity.this, color));
        }
    }
}