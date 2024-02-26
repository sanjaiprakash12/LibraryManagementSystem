package com.mainpackage;

import com.exceptions.NumberInputException;
import com.exceptions.Validate;
import com.persondetails.AccountManipulation;
import com.persondetails.Admin;
import com.persondetails.Person;
import com.persondetails.Librarian;
import com.persondetails.ReportManipulation;
import com.searchdetails.BookArchive;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import static com.persondetails.ReportManipulation.explore;


public class DriverClass {
    static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) {
        try {
            System.out.println("------WELCOME TO ETERNALS LIBRARY--------");
            mainMenu();
        }
        catch (SQLException | ClassNotFoundException | IOException | NumberInputException e) {
            System.out.println(e.getMessage());;
        }
    }

    private static void mainMenu() throws IOException, NumberInputException, SQLException, ClassNotFoundException {
        boolean flag = true;
        while (flag)
        {
            try {
                System.out.println("\t+---------------------------------------+");
                System.out.println("\t|             Main Menu                 |");
                System.out.println("\t+---------------------------------------+");
                System.out.println("\t| 1. Admin                              |");
                System.out.println("\t| 2. Librarian                          |");
                System.out.println("\t| 3. Member                             |");
                System.out.println("\t| 4. Explore                            |");
                System.out.println("\t| 5. Exit                               |");
                System.out.println("\t+---------------------------------------+");
                int option = Integer.parseInt(input.readLine());
                switch (option) {
                    case 1:
                        adminLoginMenu();
                        break;
                    case 2:
                        librarianLoginMenu();
                        break;
                    case 3:
                        memberLoginMenu();
                        break;
                    case 4:
                        explore();
                        break;
                    case 5:
                        exitmenu();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Enter Valid Option.");
                        break;
                }

            }
            catch (NumberFormatException e)
            {
                System.out.println("Enter the Valid Input");
            }
        }
    }

    private static void adminLoginMenu(){
        boolean flag = true;
        while(flag)
        try {
            indexMenuLayout();
            int option = Integer.parseInt(Validate.numberValidate(input.readLine()));
            AccountManipulation account = new AccountManipulation();
            switch (option)
            {
                case 1:
                {
                    int account_id = account.login("ADMIN");
                    if(account_id!=-1)
                    {
                        adminMenu(account_id);
                    }
                    break;
                }
                case 2:
                {
                    int account_id = account.register("ADMIN");
                    if(account_id!=-1)
                    {
                        adminMenu(account_id);
                    }
                    break;
                }
                case 3:
                    mainMenu();
                    break;
                case 4:

                    exitmenu();
                    flag = false;
                    break;
                default:
                    System.out.println("Enter Valid Option");
                    break;
            }
        }
        catch (IOException | NumberInputException | SQLException | ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
            flag = true;
        }

    }

    private static void adminMenu(int account_id) throws IOException, NumberInputException, SQLException, ClassNotFoundException {
        boolean flag = true;
        while(flag) {
            try {
                System.out.println("1.Add Books\n2.Remove Books\n3.Borrow Book\n4.Reserve\n5.Block Member\n6.UnBlock Member\n7.Other Reports");
                int option = Integer.parseInt(Validate.numberValidate(input.readLine()));
                Librarian librarian = new Librarian();
                Person person = new Person();
                Admin admin = new Admin();
                switch (option) {
                    case 1:
                        librarian.addBooks();
                        break;
                    case 2:
                        librarian.removeBookByTitle();
                        break;
                    case 3:
                        person.setBorrow(account_id);
                        break;
                    case 4:
                        person.setReserve(account_id);
                        break;
                    case 5:
                        admin.blockMember();
                        break;
                    case 6:
                        admin.unblockMember();
                        break;
                    case 7: {
                        otherReports(account_id);
                        break;
                    }
                    default:
                        System.out.println("Enter Valid Option");
                        break;
                }
            } catch (IOException | NumberInputException | SQLException e) {
                System.out.println(e.getMessage());
                flag = true;
            }
        }
    }
    private static void otherReports(int account_id) {
        boolean flag = true;
        while (flag) {
            try {
                System.out.println("\t+-----------------------------------+");
                System.out.println("\t| 1. Books and Availability        |");
                System.out.println("\t| 2. Patron Borrowed and Fine List |");
                System.out.println("\t| 3. Show All Patron Details       |");
                System.out.println("\t| 4. High Rated Books              |");
                System.out.println("\t| 5. Back                          |");
                System.out.println("\t| 6. Exit                          |");
                System.out.println("\t+-----------------------------------+");

                int option = Integer.parseInt(Validate.numberValidate(input.readLine()));
                switch (option) {
                    case 1:
                        ReportManipulation.bookAvailability();
                        break;
                    case 2:
                        ReportManipulation.borrowedFine();
                        break;
                    case 3:
                        ReportManipulation.allPatronList();
                        break;
                    case 4:
                        ReportManipulation.highlyRatedBooks();
                        break;
                    case 5:
                        adminMenu(account_id);
                        break;
                    case 6:
                        exitmenu();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Enter Valid Option");
                        break;
                }
            } catch (IOException | NumberInputException | SQLException | ClassNotFoundException e) {
                System.out.println(e.getMessage());
                flag = true;
            }
        }
    }
    public static void librarianLoginMenu() {
        boolean flag = true;
        while (flag) {
            try {
                indexMenuLayout();
                int option = Integer.parseInt(Validate.numberValidate(input.readLine()));
                AccountManipulation account = new AccountManipulation();
                int account_id = -1;
                switch (option) {
                    case 1: {
                        account_id = account.login("LIBRARIAN");
                        if(account_id!=-1)
                        {
                            librarianMenu(account_id);
                        }
                        break;
                    }
                    case 2:
                        account_id =account.register("LIBRARIAN");
                        if(account_id!=-1)
                        {
                            librarianMenu(account_id);
                        }
                        break;
                    case 3:
                        mainMenu();
                        break;
                    case 4:
                        exitmenu();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Enter Valid Option");
                        break;
                }
            } catch (SQLException | NumberInputException | IOException | ClassNotFoundException e) {
                System.out.println(e.getMessage());
                flag = true;
            }
        }
    }
    private static void librarianMenu(int account_id) {
        boolean flag = true;
        while (flag) {
            try {
                System.out.println("\t+---------------------------------+");
                System.out.println("\t| 1. Add Books                    |");
                System.out.println("\t| 2. Remove Books                 |");
                System.out.println("\t| 3. Borrow Book                  |");
                System.out.println("\t| 4. Reserve Book                 |");
                System.out.println("\t| 5. Other Report                 |");
                System.out.println("\t| 6. Back                         |");
                System.out.println("\t| 7. Exit                         |");
                System.out.println("\t+---------------------------------+");
                Librarian librarian = new Librarian();
                Person person = new Person();
                int option = Integer.parseInt(Validate.numberValidate(input.readLine()));
                switch (option) {
                    case 1:
                        librarian.addBooks();
                        break;
                    case 2:
                        librarian.removeBookByTitle();
                        break;
                    case 3:
                        person.setBorrow(account_id);
                        break;
                    case 4:
                        person.setReserve(account_id);
                        break;
                    case 5:
                        otherReports(account_id);
                        break;
                    case 6 :
                        librarianLoginMenu();
                        break;
                    case 7:
                        exitmenu();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Enter Valid Option");
                        break;
                }
            } catch (IOException | NumberInputException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    private static void memberMenu(int account_id) throws IOException {
        boolean flag = true;
        while(flag)
        try {
            System.out.println("\t+------------------------+");
            System.out.println("\t| 1. View Books          |");
            System.out.println("\t| 2. Borrow Books        |");
            System.out.println("\t| 3. Reserve Books       |");
            System.out.println("\t| 4. Borrowed Books      |");
            System.out.println("\t| 5. Return Book         |");
            System.out.println("\t| 5. Previous Menu       |");
            System.out.println("\t| 6. Exit                |");
            System.out.println("\t+------------------------+");

            Person person = new Person();
            int option = Integer.parseInt(Validate.numberValidate(input.readLine()));
            switch (option)
            {
                case 1:
                    viewBooks(account_id);
                    break;
                case 2:
                    person.setBorrow(account_id);
                    break;
                case 3:
                    person.setReserve(account_id);
                    break;
                case 4:
                    person.borrowedDetails(account_id);
                    break;
                case 5:
                    memberLoginMenu();
                    break;
                case 6:
                    exitmenu();
                    break;
                default:
                    System.out.println("Enter Valid Input");
                    break;
            }
        }
        catch (NumberInputException e) {
            throw new RuntimeException(e);
        }
    }

    private static void viewBooks(int account_id) {
        BookArchive bookArchive = new BookArchive();
        boolean flag = true;
        while(flag)
        {
            try
            {
                System.out.println("\t+---------------------+");
                System.out.println("\t| 1. Sort by Author   |");
                System.out.println("\t| 2. Sort by Rating   |");
                System.out.println("\t| 3. Sort by Genre    |");
                System.out.println("\t| 4. Previous         |");
                System.out.println("\t+---------------------+");
                int option = Integer.parseInt(Validate.numberValidate(input.readLine()));
                switch (option)
                {
                    case 1:
                        bookArchive.sortByAuthor();
                        break;
                    case 2:
                        bookArchive.sortByRating();
                        break;
                    case 3:
                        bookArchive.sortByGenre();
                        break;
                    case 4:
                        memberMenu(account_id);
                        break;
                    default:
                        System.out.println("Enter Valid Input");
                        break;
                }
            }
            catch (IOException | NumberInputException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void memberLoginMenu() {
        try {
            indexMenuLayout();
            int option = Integer.parseInt(Validate.numberValidate(input.readLine()));
            AccountManipulation account = new AccountManipulation();
            switch (option)
            {
                case 1:
                {
                    int account_id = account.login("MEMBER");
                    if(account_id!=-1)
                    {
                        memberMenu(account_id);
                    }
                    break;
                }
                case 2:
                {
                    int account_id = account.register("MEMBER");
                    if(account_id!=-1)
                    {
                        memberMenu(account_id);
                    }
                    break;
                }
                case 3:
                    mainMenu();
                    break;
                case 4:
                    exitmenu();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Enter Valid Option");
                    break;
            }
        }
        catch (IOException | NumberInputException | SQLException | ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
    }
    public static void exitmenu()
    {
        System.out.println("------------------ Thank You -------------------");
    }
    public static void indexMenuLayout()
    {
        System.out.println("\t+--------------------------------+");
        System.out.println("\t| 1. Login                       |");
        System.out.println("\t| 2. Register                    |");
        System.out.println("\t| 3. Back                        |");
        System.out.println("\t| 4. Exit                        |");
        System.out.println("\t+--------------------------------+");
    }
}
