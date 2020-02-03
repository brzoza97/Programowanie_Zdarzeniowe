package MP3Player.dbConnection;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertMp3 {

    public void insert(String filePath,String author,String title){

        String url = "jdbc:mysql://remotemysql.com:3306/AU57qfAyJ0";
        String user = "AU57qfAyJ0";
        String password = "GaB889XfZS";

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            String SQL = "INSERT INTO Song (patch, title, author) values (?,?,?)";
            PreparedStatement statement = conn.prepareStatement(SQL);
            InputStream inputStream = new FileInputStream(new File(filePath));
            statement.setBlob(1, inputStream);
            statement.setString(2,title);
            statement.setString(3,author);
 
            int row = statement.executeUpdate();
            if (row > 0) {
                System.out.println("A song was inserted with mp3 blob.");
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}