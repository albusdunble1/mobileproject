package com.example.miniproject;

public class AdminData {
    private String Username;
    private String Phone;
    private String Email;
    private String Password;
    private String Ic;
    public String image;
    //private Integer Image;

    public AdminData(String image) {
        this.image= image;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getIc() {
        return Ic;
    }

    public void setIc(String ic) {
        this.Ic = ic;
    }

    public String getImage() {
        return image;
    }


//    public Integer getImage() {
//        return Image;
//    }
//
//    public void setImage(Integer image) {
//        Image = image;
//    }
}
