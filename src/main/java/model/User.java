/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author PC
 */
public class User {
    private int id;
          
    private String name, email, tel;
    private String password;

    
//  constructor
    public User(int id, String name, String email, String tel, String password){
        this.id = id;
        this.name = name;
        this.password = password;
        this.tel = tel;
        this.email = email;
        
    }
//    constructor sem id para novos usuarios!
    public User(String name, String password, String tel, String email) {
        this.name = name;
        this.password = password;

        this.tel = tel;
        this.email = email;
    }

    
    
    
    
    
    public int getId() {
        return id;
    }

    
    
    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }


}
