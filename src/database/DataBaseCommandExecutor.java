package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * NetConcurrency created by лёня on 06.05.2017.
 */

public class DataBaseCommandExecutor {
    private String url, login, password;
    private Connection con;

    public DataBaseCommandExecutor(String host, int port, String login, String pass, String dbname){
        try {
            this.url = "jdbc:mysql://"+host+":"+port+"/"+dbname;
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

    public List<Teacher> searchTeacher(String command){
        Statement stmt;
        List<Teacher> teachers = new ArrayList<> ();
        try {

            stmt = con.createStatement();
            if (stmt == null) throw new AssertionError ();
            ResultSet rs =  stmt.executeQuery("SELECT * FROM teacher WHERE teacher_name  LIKE \'%"+command+"%\'");

            while (rs.next()) {
                String name = rs.getString("teacher_name");

                String post_id = rs.getString ("teacher_post_id");
                String fac_id = rs.getString ("teacher_fac_id");

                double x = rs.getDouble ("teacher_x_coordinate");
                double y = rs.getDouble ("teacher_y_coordinate");

                Statement stmt2 = con.createStatement();
                Statement stmt3 = con.createStatement();
                Statement stmt4 = con.createStatement();

                ResultSet rs2 = stmt2.executeQuery ("SELECT  univ_name FROM universities WHERE univ_id = " +
                        "(SELECT univ_id FROM faculty WHERE fac_id = \"" + fac_id + "\"" + ")");
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

    public String checkCoord(String command){
        Statement stmt;

        try {

            stmt = con.createStatement ();
            Statement stmt2 = con.createStatement();

            ResultSet rs = stmt.executeQuery ("SELECT * FROM teacher WHERE teacher_name  LIKE \'%" + command + "%\'");
            if(!rs.isBeforeFirst ())
                throw new SQLException ();
            rs.next ();

            double x = rs.getDouble ("teacher_x_coordinate");
            double y = rs.getDouble ("teacher_y_coordinate");

            ResultSet rs2 = stmt2.executeQuery ("SELECT coord_name FROM coordinates WHERE coord_right_x > " + x + " AND coord_left_x < " + x + " AND coord_top_y > " + y + " AND coord_bot_y < " + y);

            if (!rs2.isBeforeFirst() ) {
                return "You can call teacher";
            }
            rs2.next ();

            String place = rs2.getString ("coord_name");

            rs.close();
            rs2.close();
            stmt.close();
            stmt2.close();
            return "You can't call teacher. He/she is in " + place;

        } catch (SQLException e) {
            return "No teachers with this name";
        }
    }

    private String getUrl(){
        return url;
    }

}
