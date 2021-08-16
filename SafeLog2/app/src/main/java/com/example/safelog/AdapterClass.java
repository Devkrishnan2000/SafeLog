package com.example.safelog;

import android.app.Dialog;
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

import java.util.List;
import java.util.Random;


public class AdapterClass extends RecyclerView.Adapter<AdapterClass.ViewHolder>  {
    @NonNull

    private List<ModelClass> passlist;
    Dialog dialog; // edit dialog

    public AdapterClass(List<ModelClass> passlist)
    {
        this.passlist = passlist;
    }  // constructor for adapter class
    public AdapterClass.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pass_container,parent,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterClass.ViewHolder holder, int position) {
     String heading = passlist.get(position).Heading(); // gets the heading text of current card
     holder.setdata(heading);                         // heading value is passed from passlist to viewholder
    }

    @Override
    public int getItemCount() {
        return passlist.size();
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
           // buttontest(); // for testing purpose
        }



        public void setdata(String heading) {
            Headingtxt.setText(heading);
        }
        public  void buttontest()
        {
            LinearLayout row1 = itemView.findViewById(R.id.bntrow1);
            LinearLayout row2 = itemView.findViewById(R.id.bntrow2);
            LinearLayout row3 = itemView.findViewById(R.id.bntrow3);
            LinearLayout currow;
         //  Typeface font= ResourcesCompat.getFont(itemView.getContext(), R.font.rubik_font);  NOT WORKING IN ANDROID 7

            for(int i=1;i<=3;++i)
            {
               switch (i)
               {
                   case 1:currow =row1;break;
                   case 2:currow =row2;break;
                   case 3:currow =row3;break;
                   default:currow =row1;
               }
               for(int j=1;j<=3;++j)
               {
                   Random r = new Random();
                   int i1 = r.nextInt( 14 - 1) +1;
                   Button btn = new Button(itemView.getContext());
                   btn.setId((i*3)+j);
                   btn.setText("Whatsapp");
                   btn.setTextSize(itemView.getContext().getResources().getDimension(R.dimen.button_textsize));
                  // btn.setTypeface(font);

                   switch (i1)
                   {
                       case 1: btn.setBackgroundTintList(AppCompatResources.getColorStateList(itemView.getContext(),R.color.pink)); break;
                       case 2:btn.setBackgroundTintList(AppCompatResources.getColorStateList(itemView.getContext(),R.color.red)); break;
                       case 3:btn.setBackgroundTintList(AppCompatResources.getColorStateList(itemView.getContext(),R.color.purple)); break;
                       case 4:btn.setBackgroundTintList(AppCompatResources.getColorStateList(itemView.getContext(),R.color.dark_purple)); break;
                       case 5:btn.setBackgroundTintList(AppCompatResources.getColorStateList(itemView.getContext(),R.color.discord_blue)); break;
                       case 6: btn.setBackgroundTintList(AppCompatResources.getColorStateList(itemView.getContext(),R.color.sky_blue)); break;
                       case 7:btn.setBackgroundTintList(AppCompatResources.getColorStateList(itemView.getContext(),R.color.teal)); break;
                       case 8:btn.setBackgroundTintList(AppCompatResources.getColorStateList(itemView.getContext(),R.color.dark_teal)); break;
                       case 9:btn.setBackgroundTintList(AppCompatResources.getColorStateList(itemView.getContext(),R.color.dark_green)); break;
                       case 10:btn.setBackgroundTintList(AppCompatResources.getColorStateList(itemView.getContext(),R.color.orange)); break;
                       case 11:btn.setBackgroundTintList(AppCompatResources.getColorStateList(itemView.getContext(),R.color.orange_red)); break;
                       case 12:btn.setBackgroundTintList(AppCompatResources.getColorStateList(itemView.getContext(),R.color.brown)); break;
                       case 13:btn.setBackgroundTintList(AppCompatResources.getColorStateList(itemView.getContext(),R.color.grey)); break;
                       default: btn.setBackgroundTintList(AppCompatResources.getColorStateList(itemView.getContext(),R.color.black));
                   }
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
                    ModelClass grp = passlist.get(getAdapterPosition());  //gets the card's position in which edit button is clicked
                    db.delGroup(grp.pos);                     //deletes that particular entry from database
                    passlist.remove(getAdapterPosition());    // removes that particular card from recycler view
                    notifyItemRemoved(getAdapterPosition());

                  //  Toast.makeText(itemView.getContext(),"Group '"+curgrpname+"' has been deleted",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();                           //closes dialog
                }
            });

            create_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ModelClass grp = passlist.get(getAdapterPosition());
                    String newgrpname = name.getText().toString(); //gets new name from edittext
                    if(!newgrpname.isEmpty())
                    {
                        db.renameGroup(newgrpname,grp);    //updates name in database

                        grp.heading = newgrpname;           //changes name in recyclerview  card
                        passlist.set(getAdapterPosition(),grp);
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
