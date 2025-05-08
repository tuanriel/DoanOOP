package bank.management.system;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import java.sql.*;

public class Connn {
    Connection connection;
    Statement statement;
    public Connn(){
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://DESKTOP-66E4JOF:1433;DatabaseName=ATM;encrypt=true;trustServerCertificate=true;";
            String username = "sa";
            String password = "123";
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public static void main(String[] args) {
        try {
            Connn conn = new Connn();
            String q = "select * from ATM_account where card_number =412341234  and  pin = 100245  ";
            ResultSet resultSet = conn.statement.executeQuery(q);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
