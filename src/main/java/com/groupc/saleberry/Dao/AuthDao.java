
package com.groupc.saleberry.Dao;

import com.groupc.saleberry.Database.MySqlConnection;
import com.groupc.saleberry.Model.Delete;
import com.groupc.saleberry.Model.IconImage;
import com.groupc.saleberry.Model.Inventory;
import com.groupc.saleberry.Model.LoginModel;
import com.groupc.saleberry.Model.RegisterModel;
import com.groupc.saleberry.Model.SalesModel;
import com.groupc.saleberry.Model.Search;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthDao extends MySqlConnection{
    public boolean register(RegisterModel user){
        try{
            PreparedStatement ps =null;
            Connection conn = openConnection();
            String sql = "INSERT INTO user(first_name,last_name,user_id,contact_no,password,confirm_password) VALUES(?,?,?,?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getFirst_name());
            ps.setString(2, user.getLast_name());
            ps.setString(3, user.getUserId());
            ps.setString(4, user.getContact_no());
            ps.setString(5, user.getPassword());
            ps.setString(6, user.getConfirm_password());
            
            int result = ps.executeUpdate();
            if(result == -1){
                return false;
            }else{
                return true;
            }
            
        }catch(Exception e){
            System.out.println(e);
            return false;
        }
    }
    public RegisterModel Login(LoginModel login){
        try{
          PreparedStatement ps=null;
          Connection conn=openConnection();
          String sql="SELECT * FROM user WHERE user_id=? and password=?";
          ps=conn.prepareStatement(sql);
          ps.setString(1,login.getUserId());
          ps.setString(2,login.getPassword());

          ResultSet result=ps.executeQuery();
          if(result!=null && result.next()!=false){
                String userId=result.getString("user_id");
                String password=result.getString("password");
                String firstName=result.getString("first_name");
                String lastName=result.getString("last_name");
                String contact=result.getString("contact_no");
                String passwordConfirm=result.getString("confirm_password");
                RegisterModel user=new RegisterModel(userId,password,firstName,lastName,contact,passwordConfirm);
                return user;
          }else{ 
                return null;
          }
         }catch (Exception exception){
            System.out.println("Database error");
    }
        return null;
        
}   
    public boolean productInventory(Inventory products){
        try{
            PreparedStatement ps =null;
            Connection conn = openConnection();
            String sql = "INSERT INTO inventory(category_id,category_name,product_id,product_name,price) VALUES(?,?,?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, products.getCategory_id());
            ps.setString(2, products.getCategory_name());
            ps.setInt(3, products.getProduct_id());
            ps.setString(4, products.getProduct_name());
            ps.setInt(5, products.getPrice());
            
            int result = ps.executeUpdate();
            if(result == -1){
                return false;
            }else{
                return true;
            }
            
        }catch(Exception e){
            System.out.println(e);
            return false;
        }
    }
    
    public boolean inventoryUpdate(Inventory update){
        try{
            PreparedStatement ps =null;
            Connection conn = openConnection();
            String sql = "update inventory set category_id=?, category_name=?,product_id=?,product_name=?,price=? where category_id=? and product_id=?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, update.getCategory_id());
            ps.setString(2, update.getCategory_name());
            ps.setInt(3, update.getProduct_id());
            ps.setString(4, update.getProduct_name());
            ps.setInt(5, update.getPrice());
            ps.setInt(6, update.getCategory_id());
            ps.setInt(7, update.getProduct_id());
            int result = ps.executeUpdate();
            if(result == -1){
                return false;
            }else{
                return true;
            }
            
        }catch(Exception e){
            System.out.println(e);
            return false;
        }
    }
    
    public boolean inventoryDelete(Delete delete){
        try{
            PreparedStatement ps =null;
            Connection conn = openConnection();
            String sql = "delete from inventory where category_id=? and product_id=?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, delete.getCategory_id());
            ps.setInt(2, delete.getProduct_id());
            int result = ps.executeUpdate();
            if(result == -1){
                return false;
            }else{
                return true;
            }     
        }catch(Exception e){
            System.out.println(e);
            return false;
        }
    }
    
     public boolean inventorySearch(Delete search){
        try{
            PreparedStatement ps =null;
            Connection conn = openConnection();
            String sql = "select * from inventory where category_id=? and product_id=?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, search.getCategory_id());
            ps.setInt(2, search.getProduct_id());
            int result = ps.executeUpdate();
            if(result == -1){
                return false;
            }else{
                return true;
            }     
        }catch(Exception e){
            System.out.println(e);
            return false;
        }
    }
     
    public Search searchInventory(int categoryId, int productId) {
    Search searchResult = null;
    Connection conn = openConnection();
    String query = "SELECT * FROM inventory WHERE category_id=? AND product_id=?";
    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setInt(1, categoryId);
        pstmt.setInt(2, productId);
        try (ResultSet resultSet = pstmt.executeQuery()) {
            if (resultSet.next()) {
                searchResult = new Search();
                searchResult.setCategory_id(resultSet.getInt("category_id"));
                searchResult.setCategory_name(resultSet.getString("category_name"));
                searchResult.setProduct_id(resultSet.getInt("product_id"));
                searchResult.setProduct_name(resultSet.getString("product_name"));
                searchResult.setPrice(resultSet.getInt("price"));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return searchResult;
}
    public boolean salesProduct(SalesModel sales){
        try{
            PreparedStatement ps =null;
            Connection conn = openConnection();
            String sql = "INSERT INTO sellProducts(category_id,category_name,product_id,product_name,price,quantity,total_price) VALUES(?,?,?,?,?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, sales.getCategory_id());
            ps.setString(2, sales.getCategory_name());
            ps.setInt(3, sales.getProduct_id());
            ps.setString(4, sales.getProduct_name());
            ps.setInt(5, sales.getPrice());
            ps.setInt(6, sales.getQuantity());
            ps.setInt(7, sales.getTotalPrice());
            int result = ps.executeUpdate();
            if(result == -1){
                return false;
            }else{
                return true;
            }
            
        }catch(Exception e){
            System.out.println(e);
            return false;
        }
    }
        public boolean imageInsert(IconImage image){
        try{
            PreparedStatement ps =null;
            Connection conn = openConnection();
            String sql = "INSERT INTO imageStore(images) VALUES(?)";
            ps = conn.prepareStatement(sql);
            ps.setBytes(1, image.getPicture());
            int result = ps.executeUpdate();
            if(result == -1){
                return false;
            }else{
                return true;
            }
            
        }catch(Exception e){
            System.out.println(e);
            return false;
        }
    }
        
   
}
     
    




 
