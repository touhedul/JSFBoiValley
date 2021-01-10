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
import java.util.ArrayList;
import java.util.Map;  
import javax.faces.bean.ManagedBean;  
import javax.faces.bean.RequestScoped;  
import javax.faces.context.FacesContext;  

/**
 *
 * @author touhe
 */
@ManagedBean
@RequestScoped
public class Department {

    private int id;
    private String shortName;
    private String fullName;
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Creates a new instance of Department
     */
    public Department() {
    }

    public void add() {
        try {
            DBConnection dBConnection = new DBConnection();
            Connection connection = dBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("insert into departments(short_name,full_name) value('" + shortName + "','" + fullName + "')");
            preparedStatement.executeUpdate();
            System.out.println("Success.");

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public ArrayList allDepartments() {
        ArrayList departmentList = new ArrayList();
        try {
            DBConnection dBConnection = new DBConnection();
            Connection connection = dBConnection.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from departments");
            while (rs.next()) {
                Department department = new Department();
                department.setId(rs.getInt("id"));
                department.setFullName(rs.getString("full_name"));
                department.setShortName(rs.getString("short_name"));
                departmentList.add(department);
            }
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return departmentList;
    }

    public String edit(int id) {

        Department department = null;
        Map<String,Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();  
        try {
            DBConnection dBConnection = new DBConnection();
            Connection connection = dBConnection.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from departments where id = " + (id));
            rs.next();
            department = new Department();
            department.setId(rs.getInt("id"));
            department.setFullName(rs.getString("full_name"));
            department.setShortName(rs.getString("short_name"));
            sessionMap.put("editDepartment", department);  
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return "/edit_department.xhtml?faces-redirect=true";
    }
    
    public String update(Department department){
        try {
            DBConnection dBConnection = new DBConnection();
            Connection connection = dBConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement("update departments set short_name=?,full_name=? where id=?");
            stmt.setString(1,department.getShortName()); 
            stmt.setString(2,department.getFullName()); 
            stmt.setInt(3,department.getId()); 
            stmt.executeUpdate();
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return "index.xhtml?faces-redirect=true";
    }

    public void delete(int id) {
        try {
            DBConnection dBConnection = new DBConnection();
            Connection connection = dBConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement("delete from departments where id = " + id);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
