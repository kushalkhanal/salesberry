package com.groupc.saleberry.Model;
    
    public class LoginModel {
    private String userId;
    private String password;
    public LoginModel(String userId, String password){
        this.userId = userId;
        this.password = password;
    }
    
    public String getUserId(){
        return this.userId;
    }
    public String getPassword(){
        return this.password;
    } 
}

