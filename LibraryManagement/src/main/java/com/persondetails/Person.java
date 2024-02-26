package com.persondetails;

import com.borrowdetails.Borrow;
import com.borrowdetails.Reservation;
import com.databaseconnection.DBConnection;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Person {
    public Account account;
    public int Person_id;
    private String name;
    private Gender gender;
    private String emailAddress;
    private String ContactNumber;
    private String address;
    public Person()
    {
        name = null;
        gender = null;
        emailAddress = null;
        ContactNumber = null;
        address = null;
    }

    public Person(int person_id, String name, Gender gender, String emailAddress, String contactNumber, String address) {
        Person_id = person_id;
        this.name = name;
        this.gender = gender;
        this.emailAddress = emailAddress;
        ContactNumber = contactNumber;
        this.address = address;
    }

    public Person(String name, Gender gender, String emailAddress, String mobileNumber, String address) {
        this.name = name;
        this.gender = gender;
        this.emailAddress = emailAddress;
        this.ContactNumber = mobileNumber;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public Gender getGender() {
        return gender;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setBorrow(int account_id){
        try
        {
            Borrow borrow = new Borrow();
            borrow.borrowBooks(account_id);
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
    public void setReserve(int account_id){
            Reservation reserve = new Reservation();
            reserve.reservation(account_id);
    }
    public void setReturn(int account_id) throws IOException {
            Borrow.returnBooks(account_id);
    }

    public Account getAccount() {
        return account;
    }

    public int getPerson_id() {
        return Person_id;
    }

    public void borrowedDetails(int accountId) {
        try
        {
            String borrwedDetails = "SELECT b.book_id, b.title,br.BORROWED_DATE,br.DUE_DATE, p.fine FROM LibraryManagement.Books b JOIN LibraryManagement.Borrow br ON b.book_id = br.book_id JOIN LibraryManagement.Payment p ON br.borrow_id = p.borrow_id WHERE br.account_id = ? ;";
            PreparedStatement borrowedStatement = DBConnection.getDBConnection().prepareStatement(borrwedDetails);
            borrowedStatement.setInt(1,accountId);
            ResultSet borrowedSet = borrowedStatement.executeQuery();
            while(borrowedSet.next())
            {
                System.out.println("\t"+borrowedSet.getInt(1)+"\t"+borrowedSet.getString(2)+"\t"+borrowedSet.getDate(3)+"\t"+borrowedSet.getDate(4)+"\t"+borrowedSet.getDouble(5));
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }
    public static void memberShipExpired() throws SQLException, ClassNotFoundException {
        String  membershipExpiredQuery = "UPDATE LibraryManagement.Status SET membership_status = 'INACTIVE' WHERE account_id IN (SELECT account_id FROM Status WHERE membership_endDate < TRUNC(SYSDATE));";
        PreparedStatement membershipStatement = DBConnection.getDBConnection().prepareStatement(membershipExpiredQuery);
        if(membershipStatement.executeUpdate()>0)
        {
            System.out.println("Your Account is Inactivated");
        }
    }
}

