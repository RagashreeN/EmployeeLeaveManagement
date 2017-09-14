package com.employee.adapter;

import java.util.LinkedList;

/**
 * Created by Srisht on 13-09-2017.
 */

public class EmployeeAdapter {

    String empName,empImgLink,empGender,empDest;
    int empAge;
    EmployeeAdapter employeeAdapter;

    public EmployeeAdapter(String employeeName,String employeeImg){
        this.empName = employeeName;
        this.empImgLink = employeeImg;
    }
    public EmployeeAdapter(String employeeName,String employeeImg,int age,String gender,String designation){
        this.empName = employeeName;
        this.empImgLink = employeeImg;
        this.empAge = age;
        this.empGender = gender;
        this.empDest = designation;
    }
    public EmployeeAdapter(){
    }


    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpImgLink() {
        return empImgLink;
    }

    public void setEmpImgLink(String empImgLink) {
        this.empImgLink = empImgLink;
    }

    public String getEmpGender() {
        return empGender;
    }

    public void setEmpGender(String empGender) {
        this.empGender = empGender;
    }

    public String getEmpDest() {
        return empDest;
    }

    public void setEmpDest(String empDest) {
        this.empDest = empDest;
    }

    public int getAge() {
        return empAge;
    }

    public void setAge(int age) {
        this.empAge = age;
    }

}
