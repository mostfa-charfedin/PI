package org.example;

import utils.MyDb;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        Connection connection = MyDb.getInstance().getConnection();
    }
}