package com.neutron;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DB {

    final String URL = "jdbc:derby:sampleDB;create=true";
    final String USERNAME = "";
    final String PASSWORD = "";

    //Létrehozzuk a kapcsolatot (hidat)
    Connection conn = null;
    Statement createStatement = null;
    DatabaseMetaData dbmd = null;


    public DB() {
        //Megpróbáljuk életre kelteni
        try {
            conn = DriverManager.getConnection(URL);
            System.out.println("A híd létrejött");
        } catch (SQLException ex) {
            System.out.println("Valami baj van a connection (híd) létrehozásakor.");
            ex.printStackTrace();
        }

        //Ha életre kelt, csinálunk egy megpakolható teherautót
        if (conn != null){
            try {
                createStatement = conn.createStatement();
            } catch (SQLException ex) {
                System.out.println("Valami baj van van a createStatament (teherautó) létrehozásakor.");
                ex.printStackTrace();
            }
        }

        //Megnézzük, hogy üres-e az adatbázis? Megnézzük, létezik-e az adott adattábla.
        try {
            assert conn != null;
            dbmd = conn.getMetaData();
        } catch (SQLException ex) {
            System.out.println("Valami baj van a DatabaseMetaData (adatbázis leírása) létrehozásakor..");
            ex.printStackTrace();
        }

        try {
            ResultSet rs = dbmd.getTables(null, "APP", "HISTORY", null);
            if(!rs.next()) {
                createStatement.execute("create table history(id INT not null primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),link varchar(50), date varchar(30))");
            }
        } catch (SQLException ex) {
            System.out.println("Valami baj van az adattáblák létrehozásakor.");
            System.out.println(""+ex);
        }
    }


    public ArrayList<Element> getAllElements(){
        String sql = "select * from history";
        ArrayList<Element> users = null;
        try {
            ResultSet rs = createStatement.executeQuery(sql);
            users = new ArrayList<>();

            while (rs.next()){
                Element actualElement = new Element(rs.getInt("id"),rs.getString("link"),rs.getString("date"));
                users.add(actualElement);
            }
        } catch (SQLException ex) {
            System.out.println("Valami baj van a userek kiolvasásakor");
            System.out.println(""+ex);
        }
        return users;
    }

    public void addElement(Element element){
        try {
            String sql = "insert into history (link, date) values (?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, element.getLink());
            preparedStatement.setString(2, element.getDate());
            preparedStatement.execute();
        } catch (SQLException ex) {
            System.out.println("Valami baj van a Element hozzáadásakor");
            System.out.println(""+ex);
            ex.printStackTrace();
        }
    }

    public void updateHistory(Element element){
        try {
            String sql = "update history set link = ?, date = ? where id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, element.getLink());
            preparedStatement.setString(2, element.getDate());
            preparedStatement.setInt(3, Integer.parseInt(element.getId()));
            preparedStatement.execute();
        } catch (SQLException ex) {
            System.out.println("Valami baj van a Element hozzáadásakor");
            System.out.println(""+ex);
        }
    }

    public void removeElement(Element element){
        try {
            String sql = "delete from history where id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, Integer.parseInt(element.getId()));
            preparedStatement.execute();
        } catch (SQLException ex) {
            System.out.println("Valami baj van a Element törlésekor");
            System.out.println(""+ex);
        }
    }
    public void removeElement(Integer id){
        try {
            String sql = "delete from history where id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (SQLException ex) {
            System.out.println("Valami baj van az Element törlésekor");
            System.out.println(""+ex);
        }
    }

}