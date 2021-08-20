package com.example.safelog;

import static android.service.controls.ControlsProviderService.TAG;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class AdapterClass extends RecyclerView.Adapter<AdapterClass.ViewHolder>  {
    @NonNull

    private List<ModelClass> grplist;
    public List<PaslistClass> paslist;
    static int cores =Runtime.getRuntime().availableProcessors();
    private  static ExecutorService adapter_executor = Executors.newFixedThreadPool(cores+1);
    Dialog dialog; // edit dialog

    public AdapterClass(List<ModelClass> grplist , List<PaslistClass> paslist)
    {
        this.grplist = grplist;
        this.paslist = paslist;
    }  // constructor for adapter class
    public AdapterClass.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pass_container,parent,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterClass.ViewHolder holder, int position) {
     String heading = grplist.get(position).Heading(); // gets the heading text of current card
     holder.setdata(heading);
     holder.btngen(position);// heading value is passed from grplist to viewholder
    }

    @Override
    public int getItemCount() {
        return grplist.size();
    } // returns the item count of cards

    public int getgrpbtncount(int grp)
    {
        int count =0;
        for (PaslistClass po:paslist)
        {
            if(po.grpid==grp)
                count++;
        }
        return  count;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView Headingtxt;
        public TextView Createtxt;
        public ImageButton  Editbtn;
        public ImageButton Addbtn;
        int pos =0;
        FragmentManager fragmentManager = ((FragmentActivity) itemView.getContext()).getSupportFragmentManager();


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Headingtxt = itemView.findViewById(R.id.heading);
            Editbtn = itemView.findViewById(R.id.edit_btn);   // initialising components inside pass container.xml
            Createtxt = itemView.findViewById(R.id.createtxtview);
            Addbtn = itemView.findViewById(R.id.plusimg);
            editgrpfun(); //function of edit buttton in the card
        }

        public void setdata(String heading) {
            Headingtxt.setText(heading);
        }

        @SuppressLint("RestrictedApi")
        public  void btngen(int curcardpos)
        {

            LinearLayout row1 = itemView.findViewById(R.id.bntrow1);
            LinearLayout row2 = itemView.findViewById(R.id.bntrow2);
            LinearLayout row3 = itemView.findViewById(R.id.bntrow3);
            LinearLayout currow = null;
            List<PaslistClass> pasgrplist;  //stores all the pasword titles in the specific group specified by curcardpos

                pasgrplist = getgrpbtncnt(paslist, grplist.get(curcardpos).pos); //function to get password titles
                int pascount = pasgrplist.size();  //count of total passwords in the current group

                if (pascount != 0) {
                    int fixed_col = 3;   // maximum number of columns of buttons (its not recommended to go beyond three coz can cause issue in 18:9 diplay)
                    int remain_col = 0; // remaining columns
                    int cur_col;
                    int row;
                    int pending;
                    int curpos;
                    row = pascount / fixed_col;     //to determine how many rows are required for n number of items for eg 12 items needs 4 rows (12/3)=4)
                    pending = pascount % fixed_col;  // some times items may not be perfectly divisible by 3 so pending stores remaining columns for eg  if
                                                     // no of items is 4 then 4/3 =1 so one row but one row can only contain 3 items so remaining 1 item is send to pending
                    if (pascount % fixed_col == 0)    //this is to prevent adding extra columns if for eg if items is 3 3/3 =1 but we additional add one row in loop so to balance we use this operation
                        row--;

                    if (pending > 0)                   //if pending is more than 0 there are items to be inserted in the next row
                        remain_col = pending;
                    curpos = 0;                        //this is used to find the current buttons id
                    for (int i = 0; i < row + 1; ++i) {
                        switch (i) {
                            case 0:
                                row1.removeAllViews();   //removeallview is used to remove previously added buttons to remove duplication of buttons
                                currow = row1;
                                break;
                            case 1:
                                row2.removeAllViews();
                                currow = row2;
                                break;
                            case 2:
                                row3.removeAllViews();
                                currow = row3;
                                break;
                            default:
                                currow = row1;
                        }
                        if (i == row && remain_col != 0)
                            cur_col = remain_col;                //if we are on the last row and there is pending items we add those pending items to current row
                        else
                            cur_col = fixed_col;
                        for (int j = 0; j < cur_col; ++j) {
                            AppCompatButton btn = new AppCompatButton(itemView.getContext());

                            btn.setId(pasgrplist.get(curpos).pasid);
                            btn.setText(pasgrplist.get(curpos).title);
                            btn.setTextSize(itemView.getContext().getResources().getDimension(R.dimen.button_textsize));
                            btn.setMinimumWidth((int) itemView.getContext().getResources().getDimension(R.dimen.button_width));
                            btn.setBackgroundTintList(AppCompatResources.getColorStateList(itemView.getContext(), pasgrplist.get(curpos).color));
                            //  Typeface font= ResourcesCompat.getFont(itemView.getContext(), R.font.rubik_font);  NOT WORKING IN ANDROID 7
                            // btn.setTypeface(font);
                            // btn.setPadding(50, 0, 50, 0); not needed right now padding looks good i guess
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                            layoutParams.setMargins(5, 0, 5, 0);
                            btn.setLayoutParams(layoutParams);
                            btn.setAutoSizeTextTypeUniformWithConfiguration(12,15,2, TypedValue.COMPLEX_UNIT_DIP);
                            btn.setTextColor(AppCompatResources.getColorStateList(itemView.getContext(), R.color.white));
                            currow.addView(btn);
                            curpos++;
                        }
                    }

                }

        }

        private void editgrpfun()
        {
            Editbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setdialog();      //initialises the editgroupdialog.xml
                    dialog.show();     //displays dialog;
                }
            });
        }

        private List<PaslistClass> getgrpbtncnt(List<PaslistClass> pas, int grp)
        {
            List<PaslistClass> pasgrplist= new ArrayList<>();
            for (PaslistClass po:pas)
            {
                   if(po.grpid==grp)
                       pasgrplist.add(po);
            }
            return  pasgrplist;
        }
        private int getgrpbtncnt(List<PaslistClass> pas, int grp, boolean cnt)
        {
           int count =0;
            for (PaslistClass po:pas)
            {
                if(po.grpid==grp)
                    count++;
            }
            return count;
        }

        private void setdialog()
        {
            dialog = new Dialog(itemView.getContext());
            dialog.setContentView(R.layout.editgroup_dialog);
            dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.dialog_background));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

            TextView title = dialog.findViewById(R.id.grouptitle);
            Button create_btn = dialog.findViewById(R.id.savegrpbtn); // initialses components inside editgroupdialog.xml
            TextView name =  dialog.findViewById(R.id.newgrpname);
            ImageButton delbtn = dialog.findViewById(R.id.delgrpbtn);
            title.setText("Edit Group");

            DBClass db = new DBClass(itemView.getContext());  //create an DBclass obj to delete entries from daatabase

            delbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ModelClass grp = grplist.get(getAdapterPosition());  //gets the card's position in which edit button is clicked
                    adapter_executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            db.delGroup(grp.pos);
                            db.deletegrppass(grp.pos);
                        }
                    });
                                        //deletes that particular entry from database
                    grplist.remove(getAdapterPosition());    // removes that particular card from recycler view
                    notifyItemRemoved(getAdapterPosition());

                  //  Toast.makeText(itemView.getContext(),"Group '"+curgrpname+"' has been deleted",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();                           //closes dialog
                }
            });

            create_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ModelClass grp = grplist.get(getAdapterPosition());
                    String newgrpname = name.getText().toString(); //gets new name from edittext
                    if(!newgrpname.isEmpty())
                    {
                        adapter_executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                db.renameGroup(newgrpname,grp);  //updates name in database
                            }
                        });


                        grp.heading = newgrpname;           //changes name in recyclerview  card
                        grplist.set(getAdapterPosition(),grp);
                        notifyItemChanged(getAdapterPosition());
                        dialog.dismiss();
                    }
                    else
                    {
                        Toast.makeText(itemView.getContext(),"Group name cannot be empty",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}
