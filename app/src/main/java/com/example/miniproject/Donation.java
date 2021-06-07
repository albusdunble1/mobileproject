package com.example.miniproject;

public class Donation {
    private String Name;
    private String Phone;
    private String IC;
    private String Food;
    private Double Amount;
    private String PaymentID;
    private String PaymentStatus;
    private String PaymentDate;
    private String receiverName;
    private Integer receiverImage;
    private String imageURL;
    private String ReceiverID;

    public Donation(){
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getIC() {
        return IC;
    }

    public void setIC(String IC) {
        this.IC = IC;
    }

    public String getFood() {
        return Food;
    }

    public void setFood(String food) {
        Food = food;
    }

    public Double getAmount() {
        return Amount;
    }

    public void setAmount(Double amount) {
        Amount = amount;
    }

    public String getPaymentID() {
        return PaymentID;
    }

    public void setPaymentID(String paymentID) {
        PaymentID = paymentID;
    }

    public String getPaymentStatus() {
        return PaymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        PaymentStatus = paymentStatus;
    }

    public String getPaymentDate() {
        return PaymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        PaymentDate = paymentDate;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public Integer getReceiverImage() {
        return receiverImage;
    }

    public void setReceiverImage(Integer receiverImage) {
        this.receiverImage = receiverImage;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getReceiverID() {
        return ReceiverID;
    }

    public void setReceiverID(String receiverID) {
        this.ReceiverID = receiverID;
    }
}
