package database;

import netutils.MessageHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * NetConcurrency created by лёня on 06.05.2017.
 */

public class DataBaseCommandExecutor {
    private String url, login, password;
    private Connection con;
    private MessageHandler classMH;
    public DataBaseCommandExecutor(String host, int port, String login, String pass, String dbname, MessageHandler cmh){
        try {
            this.url = "jdbc:mysql://"+host+":"+port+"/"+dbname;
            this.classMH = cmh;
//           this.url = "jdbc:mysql://localhost:3306/teachers";
            this.login = login;
            this.password = pass;
            con = DriverManager.getConnection(url, this.login, password);
        } catch (SQLException e) {
            e.printStackTrace ();
        }
    }

    public void close(){
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace ();
        }
    }

    public List<Teacher> execute(String command){
        Statement stmt;
        List<Teacher> teachers = new ArrayList<> ();
        try {

            stmt = con.createStatement();
            if (stmt == null) throw new AssertionError ();
            ResultSet rs =  stmt.executeQuery("SELECT * FROM teacher WHERE teacher_name  LIKE \'%"+command+"%\'");

            while (rs.next()) {
                String name = rs.getString("teacher_name");

                String univ_id = rs.getString ("teacher_univ_id");
                String post_id = rs.getString ("teacher_post_id");
                String fac_id = rs.getString ("teacher_fac_id");

                double x = Double.parseDouble (rs.getString ("teacher_x_coordinate"));
                double y = Double.parseDouble (rs.getString ("teacher_y_coordinate"));

                Statement stmt2 = con.createStatement();
                Statement stmt3 = con.createStatement();
                Statement stmt4 = con.createStatement();

                ResultSet rs2 = stmt2.executeQuery ("SELECT univ_name FROM universities WHERE univ_id = \""
                        +univ_id+"\"");
                rs2.next();

                ResultSet rs3 = stmt3.executeQuery ("SELECT fac_name FROM faculty WHERE fac_id = \""
                        +fac_id+"\"");
                rs3.next();
                ResultSet rs4 = stmt4.executeQuery ("SELECT post_name FROM post WHERE post_id = \""
                        +post_id+"\"");
                rs4.next();

                String post = rs4.getString ("post_name");
                String univ = rs2.getString ("univ_name");
                String fac = rs3.getString ("fac_name");

                teachers.add (new Teacher(name, post, fac, univ, x, y));

                rs2.close();
                rs3.close();
                rs4.close();

                stmt2.close();
                stmt3.close();
                stmt4.close();
            }
            stmt.close();
            rs.close ();
        } catch (SQLException e) {
            e.printStackTrace ();
        }
        return teachers;
    }

    public String getUrl(){
        return url;
    }

}
