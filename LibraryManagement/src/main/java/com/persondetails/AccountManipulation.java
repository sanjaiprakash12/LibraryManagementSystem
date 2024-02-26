package com.persondetails;

import com.databaseconnection.DBConnection;
import com.primarykeyprovider.PrimaryKey;
import com.exceptions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.NumberFormatException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class AccountManipulation {
    private ArrayList<Person> collectPersonDetails() {

        try
        {
            ArrayList<Person> personListDetails = new ArrayList<>();
            String listPerson1 = "SELECT * FROM LIBRARYMANAGEMENT.Person";
            PreparedStatement listPersonStatement = DBConnection.getDBConnection().prepareStatement(listPerson1);
            ResultSet listResultSet = listPersonStatement.executeQuery();
            while (listResultSet.next())
            {
                personListDetails.add(new Person(listResultSet.getInt(1),listResultSet.getString(2),Gender.valueOf(listResultSet.getString(3)),listResultSet.getString(4), listResultSet.getString(5),listResultSet.getString(6)));
            }
            return  personListDetails;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public ArrayList<Account> collectUserAccount(){
        try
        {
            ArrayList<Account> accountsList = new ArrayList<>();
            String listAccount = "SELECT * FROM LIBRARYMANAGEMENT.Account";
            PreparedStatement listAccountStatement = DBConnection.getDBConnection().prepareStatement(listAccount);
            ResultSet resultSet = listAccountStatement.executeQuery();
            while (resultSet.next()) {
                accountsList.add(new Account(resultSet.getInt("account_id"), resultSet.getInt("person_id"),
                        resultSet.getString("username"), resultSet.getString("password"),
                        resultSet.getString("account_type")));
            }
            return accountsList;
        }
        catch (SQLException | ClassNotFoundException e)
        {
            e.getMessage();
        }
        return null;
    }

    static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    public int register(String account_type) {
        boolean flag = true;
        while (flag)
        {
            try {
                ArrayList <Person>listOfPerson = collectPersonDetails();
                System.out.print("Enter Your Name ");
                String name = Validate.alphabetValidate(input.readLine());
                System.out.print("Enter Your gender (MALE/FEMALE/TRANSGENDER)  ");
                String gender = input.readLine().toUpperCase();
                System.out.print("Enter your Email Id ");
                String email = Validate.validateEmailValidate(input.readLine());
                System.out.print("Enter your ContactNumber Number ");
                String contactNumber = input.readLine();

                for(Person person : listOfPerson)
                {
                    if(person.getEmailAddress().equals(email))
                    {
                        System.out.println("Email ID is already registered");
                        register(account_type);
                        break;
                    }
                    if(person.getContactNumber().equals(contactNumber))
                    {
                        System.out.println("Email ID is already registered");
                        register(account_type);
                        break;
                    }
                }
                System.out.print("Enter Address Ex:(Door Number,Street,City,Region-PostalCode)  ");
                String librarian_address = input.readLine();
                String username = "";
                String password = "";
                boolean innerFlag = true;
                while(innerFlag) {
                    try{
                        System.out.print("Create UserName minimum 8 Character with minimum 1 UpperCase,Underscore'_',Alphanumeric without space ");
                        username = Validate.userNameValidate(input.readLine());
                        System.out.print("Create Password minimum 8 Character include with 1 UpperCase and a Special [@$!%*#?&]");
                        password = Validate.passwordValidate(input.readLine());
                        innerFlag =false;
                    }
                    catch (UserNameException | PasswordException e)
                    {
                        System.out.println(e.getMessage());
                        innerFlag = true;
                    }
                }
                boolean insertUsername = false;
                ArrayList<Account> accountList = collectUserAccount();
                for (Account list : accountList) {
                    if (list.getUsername().equals(username)) {
                        insertUsername = true;
                        break;
                    }
                }
                int account_id = PrimaryKey.getPrimaryKey("Account");
                if (!insertUsername) {
                    int primaryKey = PrimaryKey.getPrimaryKey("Person");
                    System.out.println(primaryKey);
                    Librarian librarian = new Librarian(name, Gender.valueOf(gender), email, contactNumber, librarian_address, LocalDate.now(), LibrarianStatus.ACTIVE);
                    String addDetails = "INSERT INTO LIBRARYMANAGEMENT.Person(person_id, name, gender, email, mobileNumber, person_address) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement addDetailsStatement = DBConnection.getDBConnection().prepareStatement(addDetails);
                    addDetailsStatement.setInt(1, primaryKey);
                    addDetailsStatement.setString(2, librarian.getName());
                    addDetailsStatement.setString(3, gender.toUpperCase());
                    addDetailsStatement.setString(4, librarian.getEmailAddress());
                    addDetailsStatement.setString(5, librarian.getContactNumber());
                    addDetailsStatement.setString(6, librarian.getAddress());
                    if (addDetailsStatement.executeUpdate() > 0) {
                        System.out.println("Details Entered Successfully");
                    }
                    String addAccount = "INSERT INTO LIBRARYMANAGEMENT.Account(account_id, username, password, account_type, person_id) VALUES(?, ?, ?, ?, ?)";
                    PreparedStatement addAccountStatement = DBConnection.getDBConnection().prepareStatement(addAccount);
                    addAccountStatement.setInt(1, account_id);
                    addAccountStatement.setString(2, username);
                    addAccountStatement.setString(3, password);
                    addAccountStatement.setString(4, account_type);
                    addAccountStatement.setInt(5, primaryKey);
                    if (addAccountStatement.executeUpdate() > 0) {
                        System.out.println("Registered Success ");
                    }
                    else
                    {
                        System.out.println("Unable to register");
                    }
                }
                if (account_type.equalsIgnoreCase("MEMBER")) {
                    String status = "INSERT INTO LIBRARYMANAGEMENT.status (status_id, account_id, membership_status, member_borrowLimit, membership_startDate, membership_endDate) VALUES(?, ?, ?, ?, ?, ?)";
                    PreparedStatement addStatus = DBConnection.getDBConnection().prepareStatement(status);
                    addStatus.setInt(1, PrimaryKey.getPrimaryKey("status"));
                    addStatus.setInt(2, account_id);
                    addStatus.setString(3, String.valueOf(LibrarianStatus.ACTIVE));
                    addStatus.setInt(4, 5);
                    addStatus.setDate(5, Date.valueOf(LocalDate.now()));
                    addStatus.setDate(6, Date.valueOf(LocalDate.now().plusYears(1)));
                    addStatus.executeUpdate();
                }
                else if (account_type.equalsIgnoreCase("LIBRARIAN")) {
                    String status = "INSERT INTO LIBRARYMANAGEMENT.status (status_id, account_id, membership_status, member_borrowLimit, membership_startDate, membership_endDate) VALUES(?, ?, ?, ?, ?, ?)";
                    PreparedStatement addStatus = DBConnection.getDBConnection().prepareStatement(status);
                    addStatus.setInt(1, PrimaryKey.getPrimaryKey("status"));
                    addStatus.setInt(2, account_id);
                    addStatus.setString(3, String.valueOf(LibrarianStatus.ACTIVE));
                    addStatus.setInt(4, 20);
                    addStatus.setDate(5, Date.valueOf(LocalDate.now()));
                    addStatus.setDate(6, Date.valueOf(LocalDate.now().plusYears(5)));
                    addStatus.executeUpdate();
                }
                else if (account_type.equalsIgnoreCase("ADMIN")) {
                    String status = "INSERT INTO LIBRARYMANAGEMENT.status (status_id, account_id, membership_status, member_borrowLimit, membership_startDate, membership_endDate) VALUES(?, ?, ?, ?, ?, ?)";
                    PreparedStatement addStatus = DBConnection.getDBConnection().prepareStatement(status);
                    addStatus.setInt(1, PrimaryKey.getPrimaryKey("status"));
                    addStatus.setInt(2, account_id);
                    addStatus.setString(3, String.valueOf(LibrarianStatus.ACTIVE));
                    addStatus.setInt(4, 20);
                    addStatus.setDate(5, Date.valueOf(LocalDate.now()));
                    addStatus.setDate(6, Date.valueOf(LocalDate.now().plusYears(7)));
                    if (addStatus.executeUpdate() > 0) {
                        System.out.println("Executed");
                    }
                    else {
                        System.out.println("Not execute");
                    }
                }
                System.out.println("Enter 1 to continue or 2 to previous menu");
                int option = Integer.parseInt(input.readLine());
                if(option==1)
                {
                    flag = true;
                    return account_id;
                }
                else
                {
                    flag = false;
                }
            }
            catch (IOException | SQLException | ClassNotFoundException | IllegalArgumentException | AlphabetException |
                   EmailException e) {
                System.out.println(e.getMessage());
                flag = true;
            }
        }
        return -1;
    }
//    public int login(String account_type) throws IOException {
//        boolean flag = true;
//        while (flag) {
//            try {
//                String userName = "";
//                String password = "";
//                boolean occurenceFlag = false;
//                boolean errorFlag = true;
//                while(errorFlag)
//                {
//                    System.out.print("Enter Username ");
//                    userName = input.readLine();
//                    System.out.print("Enter Password ");
//                    password = input.readLine();
//                    ArrayList<Account> accountsList1 = collectUserAccount();
//                    for(Account list:accountsList1)
//                    {
//                        if (list.getUsername().equals(userName) && list.getAccountType().equals(account_type))
//                        {
//                            occurenceFlag = true;
//                            if (!list.getPassword().equals(password) && list.getUsername().equals(userName)) {
//                                System.out.println("Wrong Password");
//                                flag = true;
//                                break;
//                            }
//                            else if(list.getPassword().equals(password) && list.getPassword().equals(password))
//                            {
//                                errorFlag = false;
//                            }
//                        }
//                    }
//                }
//                if(!occurenceFlag)
//                {
//                    System.out.println("The user name Not yet registered");
//                    System.out.print("Enter 1 to continue 2 to previous menu:");
//                    int option = Integer.parseInt(input.readLine());
//                    if(option==1)
//                    {
//                        flag = true;
//                    }
//                    if(option==2)
//                    {
//                        break;
//                    }
//                }
//                else
//                {
//                    String checkMembership = "SELECT s.membership_status FROM LibraryManagement.Account a JOIN LibraryManagement.Status s ON a.account_id = s.account_id WHERE a.username = ? AND a.password = ?";
//                    PreparedStatement checkMembershipStatement = DBConnection.getDBConnection().prepareStatement(checkMembership);
//                    checkMembershipStatement.setString(1, userName);
//                    checkMembershipStatement.setString(2, password);
//                    ResultSet checkMembershipSet = checkMembershipStatement.executeQuery();
//                    String status = "";
//                    if (checkMembershipSet.next()) {
//                        status = checkMembershipSet.getString(1);
//                    }
//                    if (status.equals("ACTIVE") && account_type.equals(account_type)) {
//                        int account_id = -1;
//                        ArrayList<Account> accountsList1 = collectUserAccount();
//                        for (Account list : accountsList1) {
//                            if (list.getUsername().equals(userName) && list.getPassword().equals(password)) {
//                                account_id = list.account_id;
//                                System.out.println("Login successfully");
//                                return account_id;
//                            }
//                        }
//                        System.out.println("You are not still registered");
//                    }
//                    else if((status.equals("INACTIVE")||status.equals("BLOCKED") )&& account_type.equals(account_type)) {
//                        System.out.println("You were Blocked/Inactive status - Contact Admin");
//                        flag = true;
//                    }
//                }
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        }
//        return -1;
//    }
public int login(String account_type)  {
    boolean flag = true;
    while (flag) {
        try {
            System.out.print("Enter Username: ");
            String userName = input.readLine();
            System.out.print("Enter Password: ");
            String password = input.readLine();

            // Combine the two queries into one
            String checkAccountQuery = "SELECT a.account_id, s.membership_status FROM LibraryManagement.Account a JOIN LibraryManagement.Status s ON a.account_id = s.account_id WHERE a.username = ? AND a.password = ?";
            PreparedStatement checkAccountStatement = DBConnection.getDBConnection().prepareStatement(checkAccountQuery);
            checkAccountStatement.setString(1, userName);
            checkAccountStatement.setString(2, password);
            ResultSet accountResultSet = checkAccountStatement.executeQuery();

            if (accountResultSet.next()) {
                String status = accountResultSet.getString("membership_status");
                int account_id = accountResultSet.getInt("account_id");

                if (status.equals("ACTIVE") && account_type.equals(account_type)) {
                    System.out.println("Login successful");
                    return account_id;
                } else if (status.equals("INACTIVE") || status.equals("BLOCKED")) {
                    System.out.println("Your account is " + status + ". Please contact admin.");
                    flag = true;
                }
            } else {
                System.out.println("Invalid username or password.");
                System.out.print("Enter 1 to continue, 2 to previous menu: ");
                int option = Integer.parseInt(input.readLine());
                if (option == 2) {
                    break;
                }
            }
        } catch (SQLException | NumberFormatException | ClassNotFoundException |IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    return -1;
}

}
