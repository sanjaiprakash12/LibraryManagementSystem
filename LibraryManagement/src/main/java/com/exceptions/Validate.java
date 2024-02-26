package com.exceptions;

public class Validate {
    public static String validateEmailValidate(String email) throws EmailException {
        if(!email.matches("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$"))
        {
            throw  new EmailException("EmailAddress is invalid format - Enter Valid Email Address");
        }
        return email;
    }
    public static String userNameValidate(String username) throws UserNameException {
        if(!username.matches("^[A-Za-z][A-Za-z0-9_]{7,29}$"))
        {
            throw new UserNameException("Username Invalid format - Enter Valid Username");
        }
        return username;
    }
    public static String numberValidate(String number) throws NumberInputException {
        if (!number.matches("\\d+"))
        {
            throw new NumberInputException("Input Invalid - Enter valid Input");
        }
        return number;
    }
    public static String alphabetValidate(String alphabet) throws AlphabetException {
        if(!alphabet.matches("^[a-zA-Z\\s]+$"))
        {
            throw new AlphabetException("Input Invalid - Enter Valid Input");
        }
        return alphabet;
    }
    public static String  passwordValidate(String password) throws PasswordException {
        if(!password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$"))
        {
            throw new PasswordException("Invalid Input Password");
        }
        return password;
    }
    public static String DateValidater(String date) throws DateValidator {
        if(!date.matches("^(?:19|20)\\d\\d/(?:0[1-9]|1[012])/(?:0[1-9]|[12]\\d|3[01])$"))
        {
            throw new DateValidator("Invalid date format");
        }
        return date;
    }
    public static String contactNumberValidate(String contactNumber) throws PhoneNumberException {
        if(!contactNumber.matches("^[9876]\\d{9}$"))
        {
            throw new PhoneNumberException("Invalid Phone Number");
        }
        return contactNumber;
    }

}
