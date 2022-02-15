package de.illegalaccess.betakeysystem.utils;

import de.illegalaccess.betakeysystem.BetakeySystem;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class KeyManager {
    private static MySQL mySQL;
    public KeyManager() {
        mySQL = BetakeySystem.getMySQL();
    }

    public String create(){
        Random rand=new Random();
        String possibleLetters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ.";
        StringBuilder sb = new StringBuilder(16);
        for(int i = 0; i < 16; i++)
            sb.append(possibleLetters.charAt(rand.nextInt(possibleLetters.length())));
        String key = sb.toString();
        mySQL.update("INSERT INTO `keys` (`key`) VALUES ('" + key +"')");
        return key;
    }

    public void create(String key) {
        mySQL.update("INSERT INTO `keys` (`key`) VALUES ('" + key +"')");
    }

    public void delete(String key){
        mySQL.update("DELETE FROM `keys` WHERE `key` = '"+key+"'");
    }

    public boolean isExist(String key){
        try {
            PreparedStatement preparedStatement = MySQL.getCon().prepareStatement("SELECT * FROM `keys` WHERE `key` =?");
            preparedStatement.setString(1, key);
            preparedStatement.executeQuery();
            if(preparedStatement.getResultSet().next()) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
