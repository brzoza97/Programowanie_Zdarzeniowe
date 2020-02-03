package MP3Player.dbConnection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DownloadBlob {

    public static String directory;
    public static File dir;

    public static void download(String title) {

        String url = "jdbc:mysql://remotemysql.com:3306/AU57qfAyJ0";
        String user = "AU57qfAyJ0";
        String password = "GaB889XfZS";
        String SQL = "SELECT patch FROM Song WHERE title=?";

        Connection conn = null;
        java.sql.PreparedStatement smt = null;
        InputStream input = null;
        FileOutputStream output = null;
        ResultSet rs = null;

        try {

            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting...");

            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connection successful..\nNow creating query...");

            smt = conn.prepareStatement(SQL);
            smt.setString(1, title);
            rs = smt.executeQuery();

            output = new FileOutputStream(new File(directory+"/"+ title + ".mp3")); // tu bedzie do wybrania sciezka
            System.out.println("Getting file please be patient..");

            while (rs.next()) {

                input = rs.getBinaryStream("patch"); //get it from col name
                int r = 0;

                while ((r = input.read()) != -1) {
                    output.write(r);

                }
            }
            System.out.println("File writing complete !");

        } catch (ClassNotFoundException e) {
            System.err.println("Class not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Connection failed!");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            System.err.println("File not found!");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("File writing error..!");
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    input.close();
                    output.flush();
                    output.close();
                    smt.close();
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Connot close connecton!");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }


    }

}