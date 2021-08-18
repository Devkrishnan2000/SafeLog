package com.example.safelog;

import static android.service.controls.ControlsProviderService.TAG;

import android.app.Dialog;
import android.util.Log;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class AdapterClass extends RecyclerView.Adapter<AdapterClass.ViewHolder>  {
    @NonNull

    private List<ModelClass> grplist;
    private List<PaslistClass> paslist;
    Dialog dialog; // edit dialog

    public AdapterClass(List<ModelClass> grplist ,List<PaslistClass> paslist)
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
     holder.buttontest(position);// heading value is passed from grplist to viewholder
    }

    @Override
    public int getItemCount() {
        return grplist.size();
    } // returns the item count of cards


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
            int i=0;
            for (ModelClass cls: grplist) {
                Log.d(TAG, "grplist_pos: "+cls.pos);
                Log.d(TAG, "list_pos: "+i+"\n\n");
                Log.d(TAG, "Adapter_pos: "+pos);
                i++;
            }

        }



        public void setdata(String heading) {
            Headingtxt.setText(heading);
        }
        public  void buttontest(int curcardpos)
        {
            // each linear layout for each row
            LinearLayout row1 = itemView.findViewById(R.id.bntrow1);
            LinearLayout row2 = itemView.findViewById(R.id.bntrow2);
            LinearLayout row3 = itemView.findViewById(R.id.bntrow3);
            LinearLayout currow; // stores current row in which button are stored
            List<PaslistClass> pasgrplist;  //stores all the pasword titles in the specific group specified by curcardpos
            pasgrplist = getgrpbtncnt(paslist,grplist.get(curcardpos).pos); //function to get password titles
            int pascount = pasgrplist.size();  //count of total passwords in the current group
            int col=0;      //column
            int temppascnt = pascount; //temporary copy of pascount

            /* row value is calculated in this manner
             since maximum is 3 rows we divide the total number of password by 3 so we can divide them to three groups =>1

              row will be 0 if group contains 2 or 1 passwords eg (1/3=0) .to prevent this issue row is set to 1 if pasount is >0
             this means if password list is not empty there should be alteast one row to display it ! =>2

             number of columns in each row is calculated by the following manner:

             if temppascnt -3 is >3 then this means alteast one row is fully complete (if a row has three items then its fully complete)
             note that temppascnt is reduces by 3 in every iteration

             if its false there will be an incomplete row so to get the column value of incomplete row we do tempascnt%3 this value will be
             the number of columns remaining

             but if tempcnt ==3 then 3-3>3 will be 0>3 so in next statement 3%3 will be 0 but 3 columns are there to prevent it if tempcnt%3 ==0 and tempcnt !=0
             the  col value is assigned to three.
             */
            int row =(pascount/3);

            if(pascount>0)
             {
                 if(row==0)
                     row = 1;
                 else if(pascount%3!=0)
                     row+=1;
             }

            for(int i=1;i<=row;++i)
            {
               switch (i)
               {
                   case 1:currow =row1;break;
                   case 2:currow =row2;break;
                   case 3:currow =row3;break;
                   default:currow =row1;
               }
               if(temppascnt%3==0&&temppascnt!=0)
               {
                   col=3;
               }
               else
                if(temppascnt-3>=3)
                {
                    col =3;
                    temppascnt -=3;
                }
                else
                    col = pascount%3;
               for(int j=1;j<=col;++j)
               {
                   int curpos =(((i-1)*3)+j)-1;
                   Button btn = new Button(itemView.getContext());
                   btn.setId(pasgrplist.get(curpos).pasid);
                   btn.setText(pasgrplist.get(curpos).title);
                   //  Typeface font= ResourcesCompat.getFont(itemView.getContext(), R.font.rubik_font);  NOT WORKING IN ANDROID 7
                   btn.setTextSize(itemView.getContext().getResources().getDimension(R.dimen.button_textsize));
                   btn.setBackgroundTintList(AppCompatResources.getColorStateList(itemView.getContext(),pasgrplist.get(curpos).color));
                  // btn.setTypeface(font);
                   LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT);
                   layoutParams.setMargins(5,0,5,0);
                   btn.setPadding(50,0,50,0);
                   btn.setLayoutParams(layoutParams);
                   btn.setTextColor(AppCompatResources.getColorStateList(itemView.getContext(),R.color.white));
                   currow.addView(btn);
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
            int count =0;
            for (PaslistClass po:pas)
            {
                   if(po.grpid==grp)
                       pasgrplist.add(po);
            }
            return  pasgrplist;
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
                    db.delGroup(grp.pos);                     //deletes that particular entry from database
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
                        db.renameGroup(newgrpname,grp);    //updates name in database

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
