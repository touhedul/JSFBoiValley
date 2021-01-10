/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import config.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
//import org.apache.catalina.manager.util.SessionUtils;

/**
 *
 * @author touhe
 */
@ManagedBean
@RequestScoped
public class User {

    private int id;
    private String name;
    private String email;
    private String phone;
    private String department;
    private String password;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String changePage() {
        return "frontend.page1";
    }

    public String login() {
        boolean status = false;
        ResultSet rs = null;
        String page = "";
        try {
            DBConnection dBConnection = new DBConnection();
            Connection connection = dBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement("select * from users where email=? and password=?");
            ps.setString(1, email);
            ps.setString(2, password);

            rs = ps.executeQuery();
            status = rs.next();
            System.out.println(status);
            if (status) {
                User loginUser = new User();
                loginUser.setName(rs.getString("name"));
                loginUser.setId(rs.getInt("id"));
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loginUser", loginUser);
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loginId", rs.getString("id"));
                HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                req.setAttribute("loginUser", loginUser);

                page = "welcome?faces-redirect=true";
            } else {
                page = "login_failed?faces-redirect=true";
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return page;

    }
    
    public String logout(){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
        return "login?faces-redirect=true";
    }

    public String register() {
        int i = 0;
        try {
            DBConnection dBConnection = new DBConnection();
            Connection connection = dBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("insert into users(name,email,phone,department,password) value('" + name + "','" + email + "','" + phone + "','" + department + "','" + password + "')");
            i = preparedStatement.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }

        if (i == 1) {
            return "login?faces-redirect=true";
        } else {
            return "register_failed?faces-redirect=true";
        }
    }
    
    public String profile(){
        String loginId = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("loginId");
        User user = null;
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        try {
            DBConnection dBConnection = new DBConnection();
            Connection connection = dBConnection.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from users where id = " + (loginId));
            rs.next();
            user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setDepartment(rs.getString("department"));
            user.setPhone(rs.getString("phone"));
            sessionMap.put("editUser", user);
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return "profile?faces-redirect=true";
    }
    
    public void updateProfile(User user) {
        try {
            String loginId = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("loginId");
        
            DBConnection dBConnection = new DBConnection();
            Connection connection = dBConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement("update users set name=?,department=?,phone=? where id=?");
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getDepartment());
            stmt.setString(3, user.getPhone());
            stmt.setString(4, loginId);
            stmt.executeUpdate();
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
//        return "welcome.xhtml?faces-redirect=true";
    }

    /**
     * Creates a new instance of User
     */
    public User() {
    }

}
