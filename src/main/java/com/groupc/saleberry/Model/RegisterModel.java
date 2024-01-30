
package com.groupc.saleberry.Model;

public class RegisterModel {
    private String userId;
    private String password;
    private String first_name;
    private String last_name;
    private String contact_no;
    private String confirm_password;
    public RegisterModel(String userId, String contact_no, String first_name,String last_name, String password,String confirm_password){
        this.userId = userId;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.contact_no = contact_no;
        this.confirm_password = confirm_password;
    }
    
    public String getUserId(){
        return this.userId;
    }
    public String getPassword(){
        return this.password;
    } 
    
    public String getFirst_name(){
        return this.first_name;
    }
    
    public String getLast_name(){
        return this.last_name;
    }
    
    public String getContact_no(){
        return this.contact_no;
    }     
    
    public String getConfirm_password(){
        return this.confirm_password;
    }
    
    
}
