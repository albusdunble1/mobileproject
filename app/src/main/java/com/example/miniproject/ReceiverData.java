package com.example.miniproject;

public class ReceiverData {

    private String receiverId;
    private String receiverName;
    private String receiverDesc;
    private String receiverPhone;
    private String receiverEmail;
    private String receiverLocation;
    private Integer receiverImage;
    private String imageURL;

    public ReceiverData(String receiverId, String receiverName, String receiverDesc, String receiverPhone,
                        String receiverEmail, String receiverLocation, Integer receiverImage,
                        String imageURL) {
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.receiverDesc = receiverDesc;
        this.receiverPhone = receiverPhone;
        this.receiverEmail = receiverEmail;
        this.receiverLocation = receiverLocation;
        this.receiverImage = receiverImage;
        this.imageURL = imageURL;
    }

    public ReceiverData() {}

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }


    public String getReceiverDesc() {
        return receiverDesc;
    }

    public void setReceiverDesc(String receiverDesc) {
        this.receiverDesc = receiverDesc;
    }


    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }


    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }


    public String getReceiverLocation() {
        return receiverLocation;
    }

    public void setReceiverLocation(String receiverLocation) {
        this.receiverLocation = receiverLocation;
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
}
