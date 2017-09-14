package com.employee.com.employee.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIInterface {
    @GET("/bins/jf7t1")
    Call<EmpInfo> doGetListResources();
}
