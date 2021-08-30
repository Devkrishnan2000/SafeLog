package com.example.safelog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import net.sqlcipher.database.SQLiteDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreatePin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreatePin extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreatePin.
     */
    // TODO: Rename and change types and number of parameters
    public static CreatePin newInstance(String param1, String param2) {
        CreatePin fragment = new CreatePin();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CreatePin() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SQLiteDatabase.loadLibs(getActivity());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_pin, container, false);

        AppCompatButton contbtn = view.findViewById(R.id.contpin_btn);
        EditText pintext = view.findViewById(R.id.enterpin);
        EditText repintext =view.findViewById(R.id.reenterpin);
        ImageButton entereye = view.findViewById(R.id.entereye);
        ImageButton rentereye = view.findViewById(R.id.rentereye);

        pintext.setInputType((InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD));
        repintext.setInputType((InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD));

        entereye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pintext.getInputType()== (InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD))
                {
                    pintext.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                else
                {
                    pintext.setInputType((InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD));
                }

            }
        });

        rentereye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(repintext.getInputType()== (InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD))
                {
                    repintext.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                else
                {
                    repintext.setInputType((InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD));
                }
            }
        });
        contbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(pintext.getText().toString().equals(repintext.getText().toString()))
                {
                    Keyclass.setKey(pintext.getText().toString());
                    DBClass db = new DBClass(getActivity());
                    db.setpasword();
                    view.getContext().getSharedPreferences("FIRSTRUN", Context.MODE_PRIVATE).edit().putBoolean("isfirstrun",false).apply();
                    view.getContext().startActivity(new Intent(view.getContext(),MainActivity.class));
                }
                else
                {
                    Toast.makeText(view.getContext(),"Passwords Doesnt Match",Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }
}