
package com.groupc.saleberry.Model;

import com.groupc.saleberry.Dao.AuthDao;

public class Led34{
    public static void main(String[] args) {
        AuthDao auth = new AuthDao();
        String first_name = "test";
        String last_name = "test";
        String userId = "test121";
        String password = "java123";
        String contact_no ="9844314567";
        String confirm_password ="java123";
        RegisterModel rm = new RegisterModel(userId, password, first_name, last_name,contact_no,confirm_password);
        
        boolean check = auth.register(rm);
        if(check == true){
            System.out.println("Insert Success");
        }else{
            System.out.println("Insert Failure");
        }
        
        
    }
}
