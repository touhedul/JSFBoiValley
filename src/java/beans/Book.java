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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author touhe
 */
@ManagedBean
@RequestScoped
public class Book {

    private int id;
    private String name;
    private String authorName;
    private String description;
    private int price;
    private String department;
    private String year;
    private int userId;

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

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String add() {
        System.out.println(name + " " + userId);
        String loginId = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("loginId");
        
        int i = 0;
        try {
            DBConnection dBConnection = new DBConnection();
            Connection connection = dBConnection.getConnection();
            
            PreparedStatement preparedStatement
                    = connection.prepareStatement("insert into books(name,author_name,description,price,department,year,user_id) "
                            + "value('" + name + "','" + authorName + "','" + description + "','" + price + "','" + department + "','" + year + "','" + loginId + "')");
            i = preparedStatement.executeUpdate();
            System.out.println("Success.");
        } catch (Exception e) {
            System.out.println(e);
        }
        if (i == 1) {
            return "books?faces-redirect=true";
        } else {
            return "add_book?faces-redirect=true";
        }
    }

    public ArrayList allBooks() {
        ArrayList bookList = new ArrayList();
        try {
            DBConnection dBConnection = new DBConnection();
            Connection connection = dBConnection.getConnection();
            Statement stmt = connection.createStatement();
            String loginId = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("loginId");
        
            ResultSet rs = stmt.executeQuery("select * from books where user_id = "+loginId);
            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setName(rs.getString("name"));
                book.setAuthorName(rs.getString("author_name"));
                book.setDepartment(rs.getString("department"));
                book.setDescription(rs.getString("description"));
                book.setYear(rs.getString("year"));
                book.setPrice(rs.getInt("price"));
                bookList.add(book);
            }
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return bookList;
    }

    public String edit(int id) {

        Book book = null;
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        try {
            DBConnection dBConnection = new DBConnection();
            Connection connection = dBConnection.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from books where id = " + (id));
            rs.next();
            book = new Book();
            book.setId(rs.getInt("id"));
            book.setName(rs.getString("name"));
            book.setAuthorName(rs.getString("author_name"));
            book.setDepartment(rs.getString("department"));
            book.setDescription(rs.getString("description"));
            book.setYear(rs.getString("year"));
            book.setPrice(rs.getInt("price"));
            sessionMap.put("editBook", book);
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return "/edit_book.xhtml?faces-redirect=true";
    }

    public String update(Book book) {
        try {
            DBConnection dBConnection = new DBConnection();
            Connection connection = dBConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement("update books set name=?,author_name=?,description=?,department=?,year=?,price=? where id=?");
            stmt.setString(1, book.getName());
            stmt.setString(2, book.getAuthorName());
            stmt.setString(3, book.getDescription());
            stmt.setString(4, book.getDepartment());
            stmt.setString(5, book.getYear());
            stmt.setInt(6, book.getPrice());
            stmt.setInt(7, book.getId());
            stmt.executeUpdate();
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return "books.xhtml?faces-redirect=true";
    }

    public void delete(int id) {
        try {
            DBConnection dBConnection = new DBConnection();
            Connection connection = dBConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement("delete from books where id = " + id);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Creates a new instance of Book
     */
    public Book() {
    }

}
