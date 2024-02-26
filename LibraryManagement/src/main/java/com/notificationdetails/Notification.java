package com.notificationdetails;

import java.sql.SQLException;
import java.util.Date;

abstract class Notification {
    public abstract void Notify() throws SQLException, ClassNotFoundException;
}