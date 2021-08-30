package com.example.safelog;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BuildInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuildInfo extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BuildInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BuildInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static BuildInfo newInstance(String param1, String param2) {
        BuildInfo fragment = new BuildInfo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view  = inflater.inflate(R.layout.fragment_build_info, container, false);
        AppCompatButton cntwr_btn = view.findViewById(R.id.contwrisk_btn);
        AppCompatButton exit_btn = view.findViewById(R.id.exit_btn);
        TextView heading = view.findViewById(R.id.heading);

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("ALPHA\nBuild");
        spannableStringBuilder.setSpan(
                new ForegroundColorSpan(getResources().getColor(R.color.red)),0,5, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        heading.setText(spannableStringBuilder);

        cntwr_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragframe,CreatePin.class,null)
                        .commit();
            }
        });
         return view;
    }
}