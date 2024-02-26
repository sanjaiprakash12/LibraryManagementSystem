package com.notificationdetails;

import java.sql.SQLException;

import static com.persondetails.Person.memberShipExpired;

public class EmailNotification extends Notification{
    @Override
    public void Notify() throws SQLException, ClassNotFoundException {
        memberShipExpired();
    }
}
