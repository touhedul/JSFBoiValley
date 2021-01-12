/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import config.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author touhe
 */
@ManagedBean
@RequestScoped
public class SoldBook {

    private int id;
    private int bookId;
    private int buyerId;
    private int sellerId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String buy(int bId, int sId) {

        String loginId = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("loginId");
        if (loginId == null) {
            return "/login.xhtml?faces-redirect=true";
        } else {
             try {
            DBConnection dBConnection = new DBConnection();
            Connection connection = dBConnection.getConnection();

            PreparedStatement preparedStatement
                    = connection.prepareStatement("insert into sold_books(book_id,buyer_id,seller_id) "
                            + "value('" + bId + "','" + loginId + "','" + sId + "')");
            preparedStatement.executeUpdate();
            PreparedStatement stmt = connection.prepareStatement("update books set status = 0 where id=?");
            stmt.setInt(1, bId);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
            return "/bought_book.xhtml?faces-redirect=true";
        }

    }

    /**
     * Creates a new instance of SoldBook
     */
    public SoldBook() {
    }

}
