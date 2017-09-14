package com.employee.com.employee.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.ArrayList;

public class EmpInfo {

    @SerializedName("employee")
    public List<EmpData> data = new ArrayList() ;

    public class EmpData{
        @SerializedName("age")
        public Integer age;
        @SerializedName("name")
        public String name;
        @SerializedName("gender")
        public String gender;
        @SerializedName("destination")
        public String destination;
        @SerializedName("imageurl")
        public String ImgUrl;

        @Override
        public String toString() {
            return "EmpData{" +
                    "age=" + age +
                    ", name='" + name + '\'' +
                    ", gender='" + gender + '\'' +
                    ", destination='" + destination + '\'' +
                    ", ImgUrl='" + ImgUrl + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "EmpInfo{" +
                "data=" + data +
                '}';
    }
}
