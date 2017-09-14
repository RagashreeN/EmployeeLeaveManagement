package com.employee.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.employee.adapter.EmployeeAdapter;
import com.employee.main.MainActivity;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import main.employee.com.employeeleavemanagement.R;

/**
 * Created by Srisht on 13-09-2017.
 */

public class EmployeeDetialFrag extends Fragment {

    View viewEmpDetails;
    public static EmployeeAdapter employeeAdapterDetails;
    TextView tvNameAns,tvGenderAns,tvAge,tvDesignAns,tvSanctionedLeave;
    Spinner spinnerLeave;
    Button btnSubmit;
    ImageView imageViewProfile;
    final String SHARED_PREF_NAME="SHARED_PREF_NAME";
    SharedPreferences sharedPreferences;
    private float leaveProvide=0f;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewEmpDetails = inflater.inflate(R.layout.employee_details, container, false);

        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        setFragDetailsUI();
        //unbinder = ButterKnife.bind(this, viewEmpDetails);
        ButterKnife.bind(this, viewEmpDetails);
        return viewEmpDetails;
    }

    private void setFragDetailsUI() {
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageViewProfile = (ImageView) viewEmpDetails.findViewById(R.id.imgViewEmpDetail);
        tvNameAns = (TextView) viewEmpDetails.findViewById(R.id.tvNameAns);
        tvGenderAns = (TextView) viewEmpDetails.findViewById(R.id.tvGenderAns);
        tvAge = (TextView) viewEmpDetails.findViewById(R.id.tvAgeAns);
        tvDesignAns = (TextView) viewEmpDetails.findViewById(R.id.tvDesignAns);
        tvSanctionedLeave = (TextView) viewEmpDetails.findViewById(R.id.tvSancLeaveAns);
        spinnerLeave = (Spinner) viewEmpDetails.findViewById(R.id.spinnerLeaveProv);
        btnSubmit = (Button) viewEmpDetails.findViewById(R.id.btnSubmit);
        Picasso.with((MainActivity) getActivity()).load(employeeAdapterDetails.getEmpImgLink()).into(imageViewProfile);
        tvNameAns.setText(employeeAdapterDetails.getEmpName());
        tvGenderAns.setText(employeeAdapterDetails.getEmpGender());
        tvAge.setText(String.valueOf(employeeAdapterDetails.getAge()));
        tvDesignAns.setText(employeeAdapterDetails.getEmpDest());


        float sanctionedLeave = sharedPreferences.getFloat(employeeAdapterDetails.getEmpName(),0);
        if(sanctionedLeave!=0){
            tvSanctionedLeave.setText(String.valueOf(sanctionedLeave));
        }else{
            tvSanctionedLeave.setText("0");
        }
        spinnerLeave.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if(selectedItem.equals("One day"))
                {
                    leaveProvide = 1f;
                }else if(selectedItem.equals("Half day")){
                    leaveProvide = 0.5f;
                }else{
                    leaveProvide = 2f;
                }
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveLeaveDetails(employeeAdapterDetails.getEmpName(),leaveProvide);
                //((Activity) getActivity()).invalidateOptionsMenu();
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle(getResources().getString(R.string.Message));
                    alertDialogBuilder.setMessage(getResources().getString(R.string.leaveSanctioned));
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setNegativeButton(getResources().getString(R.string.Ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            float sanctionedLeave = sharedPreferences.getFloat(employeeAdapterDetails.getEmpName(),0);
                            tvSanctionedLeave.setText(String.valueOf(sanctionedLeave));
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                }
        });

    }

    private void saveLeaveDetails(String empName,float empLeave){

        float sanctionedLeave = sharedPreferences.getFloat(employeeAdapterDetails.getEmpName(),0);

        sanctionedLeave = sanctionedLeave+empLeave;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(empName,sanctionedLeave);
        editor.commit();
    }

    @Override
    public void onDestroyView() {
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        super.onDestroyView();
        //unbinder.unbind();
    }
}
