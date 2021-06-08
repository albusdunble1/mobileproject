package com.example.miniproject;

public class User {

    public String fullName, age, email, ic, phone;

    public User(){

    }

    public User(String fullName, String age, String email, String ic, String phone){
        this.fullName = fullName;
        this.age = age;
        this.email = email;
        this.ic = ic;
        this.phone = phone;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
