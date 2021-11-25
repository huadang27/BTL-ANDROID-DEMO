package com.hqdang.baitaplonversion1.Model;

public class Departments {
    String nameDepartment;
    String dateCreate;
    String AdminCreate;

    public Departments(String nameDepartment, String dateCreate, String adminCreate) {
        this.nameDepartment = nameDepartment;
        this.dateCreate = dateCreate;
        AdminCreate = adminCreate;
    }

    public Departments() {
    }

    public String getNameDepartment() {
        return nameDepartment;
    }

    public void setNameDepartment(String nameDepartment) {
        this.nameDepartment = nameDepartment;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getAdminCreate() {
        return AdminCreate;
    }

    public void setAdminCreate(String adminCreate) {
        AdminCreate = adminCreate;
    }
}
