package com.employee.main;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employee.adapter.EmployeeAdapter;
import com.employee.adapter.EmployeeViewAdapter;
import com.employee.com.employee.retrofit.APIClient;
import com.employee.com.employee.retrofit.APIInterface;
import com.employee.com.employee.retrofit.EmpInfo;
import com.employee.fragment.EmployeeDetialFrag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import main.employee.com.employeeleavemanagement.R;
import retrofit2.Call;
import retrofit2.Callback;
//import retrofit2.Response;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.textViewHead)
    TextView textViewHead;
    @BindView(R.id.recycleEmployee)
    RecyclerView recycleEmployee;

    /*Note : To store Employee List*/
    LinkedList<EmployeeAdapter> linkedList;
    /*Note : Internet Permission code*/
    final int INTERNET_PERMISSION = 112;
    ProgressDialog progressDialog;
    /*Note : To check how much time taking using different network connection*/
    private static int timeTookConnection;

    /*Note : JSON link server URL*/
    public static final String API_BASE_URL = "https://api.myjson.com/bins/jf7t1";
    APIInterface apiInterface;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Note : Used Butterknife library */
        ButterKnife.bind(this);
         /*Note : For Log */
       /* if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree() {
                @Override
                protected String createStackElementTag(StackTraceElement element) {
                    return super.createStackElementTag(element) + " : " + element.getLineNumber();
                }
            });
        }*/
        /*Note : Setting Recycler Adapter*/
        setRecycleAdapter();

        /*Note : For Getting RUntime permission to avoid conflict in above Android M version*/
        getPermission();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int idItem = item.getItemId();

        if (idItem == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    /*Note : setting Recycler adapter and on item touch listener*/
    private void setRecycleAdapter() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleEmployee.setLayoutManager(linearLayoutManager);

        recycleEmployee.addOnItemTouchListener(
                new EmployeeViewAdapter(getApplicationContext(), new EmployeeViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click

                        EmployeeDetialFrag employeeDetialFrag = new EmployeeDetialFrag();
                        EmployeeDetialFrag.employeeAdapterDetails = linkedList.get(position);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frameLayoutFrag, employeeDetialFrag);
                        fragmentTransaction.addToBackStack("MainActivity");
                        fragmentTransaction.commit();
                    }
                })
        );
    }

    /*NOte : Network communication using VOlley Library */
    private void fetchDataFromVolley() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //String API_BASE_URL= "https://api.myjson.com/bins/jf7t1";
        String conToServerText = getResources().getString(R.string.ConnectingTOServer);
        String Pleasemessage = getResources().getString(R.string.pleaseWait);
        progressDialog = ProgressDialog.show(this, conToServerText, Pleasemessage, true);
        /*Note : Start time*/
        final int time1 = (int) ((System.currentTimeMillis() / 1000) % 3600);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API_BASE_URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                /*Note : Complete time*/
                int time2 = (int) ((System.currentTimeMillis() / 1000) % 3600);
                /*NOte : Total time duration*/
                timeTookConnection = (3600 + time2 - time1) % 3600;
                /*Note : Response*/
                fetchJSONData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }

    /*Note : Fetch Json from Response*/
    private void fetchJSONData(String response) {
        linkedList = new LinkedList<EmployeeAdapter>();
        try {
            /*Note : Display how long duration network connection took*/
            Toast.makeText(this, "Connection took time : " + timeTookConnection + " sec", Toast.LENGTH_LONG).show();
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("employee");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String strName = object.getString("name");
                String strImageURL = object.getString("imageurl");
                int age = object.getInt("age");
                String strGender = object.getString("gender");
                String strDest = object.getString("destination");
                EmployeeAdapter employeeAdapter = new EmployeeAdapter(strName, strImageURL, age, strGender, strDest);
                linkedList.add(employeeAdapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        EmployeeViewAdapter employeeViewAdapter = new EmployeeViewAdapter(this, linkedList);
        recycleEmployee.setAdapter(employeeViewAdapter);
    }


    /*Note : Permission for taking Admin mail id from playstore*/
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getPermission() {

        if (requestGET_INTERNETPermission()) {

            /*Note : For Calling Volley connection*/
            fetchDataFromVolley();

             /*Note : For Calling Retrofit connection*/
            //fetchDataFromRetrofit();

            /*Note : For Calling HttpConnection*/
            /*try {
                fetchDataFromHttpConn();
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            return;
        }
    }

    /*Note : Taking permission from user , if the user deny the permission it will ask again*/
    private boolean requestGET_INTERNETPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle("Permission necessary");
                alertBuilder.setMessage("Oops you just denied the permission, Please allow the permission in next screen");
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, INTERNET_PERMISSION);

                    }
                });

                AlertDialog alert = alertBuilder.create();
                alert.show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, INTERNET_PERMISSION);
            }
            return false;
        } else {
            return true;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == INTERNET_PERMISSION) {
            for (String permission : permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    /*Note : Permission denied*/
                    requestGET_INTERNETPermission();
                } else {
                    /*NOte : Permission granted*/
                    if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                        /*Note : For Calling Volley connection*/
                        fetchDataFromVolley();

                        /*Note : For Calling Retrofit connection*/
                        //fetchDataFromRetrofit();

                        /*Note : For Calling HttpConnection*/
                     /*try {
                            fetchDataFromHttpConn();
                        } catch (IOException e) {
                         e.printStackTrace();
                         }*/
                    } else {
                        /*NOte :set to never ask again*/
                        requestGET_INTERNETPermission();
                    }
                }
            }
        }
    }

    /*Note : For Time complexity checking in HttpConnection*/
    private void fetchDataFromHttpConn() throws IOException {
        new EmpAsyncTask(this).execute();
    }


    /*Note : For Time complexity checking in HttpConnection in AsyncTask*/
    private class EmpAsyncTask extends AsyncTask<Void, Void, String> {

        Context contextAsyncTask;
        ProgressDialog progressDialog;

        public EmpAsyncTask(Context context) {
            this.contextAsyncTask = context;
            progressDialog = new ProgressDialog(contextAsyncTask);
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage(contextAsyncTask.getResources().getString(R.string.pleaseWait));
            // if /*(!((Activity) contextAsyncTask)*/(contextAsyncTask.isFinishing()) {
            progressDialog.show();
            // }
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            final int time1 = (int) ((System.currentTimeMillis() / 1000) % 3600);
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String responseEmp = null;

            try {
                URL url = new URL(API_BASE_URL);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                int time2 = (int) ((System.currentTimeMillis() / 1000) % 3600);
                timeTookConnection = (3600 + time2 - time1) % 3600;
                responseEmp = buffer.toString();
                return responseEmp;
            } catch (IOException e) {
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            fetchJSONData(s);
            progressDialog.cancel();
        }
    }


    /*Note : Network communication in Retrofit*/
   /* private void fetchDataFromRetrofit(){
            apiInterface = APIClient.getClient().create(APIInterface.class);
            *//*Note : Start time*//*
            final int time1 = (int) ((System.currentTimeMillis() / 1000) % 3600);
            Call<EmpInfo> call = apiInterface.doGetListResources();
            call.enqueue(new Callback<EmpInfo>() {
                @Override
                public void onResponse(Call<EmpInfo> call, Response<EmpInfo> response) {
                    Timber.d("TAG",response.code()+""+response.body().toString());
                    String displayResponse = "";
                    EmpInfo empInfo = response.body();
                    Timber.d(empInfo.toString());

                    List<EmpInfo.EmpData> datumList = empInfo.data;
                    int time2 = (int) ((System.currentTimeMillis() / 1000) % 3600);
                   *//*NOte : Total time duration*//*
                    timeTookConnection = (3600 + time2 - time1) % 3600;
                    Timber.d(datumList.toString());
                }

                @Override
                public void onFailure(Call<EmpInfo> call, Throwable t) {
                    call.cancel();
                }
            });
    }*/
}
