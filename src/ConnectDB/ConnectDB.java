package ConnectDB;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.sql.SQLException;
public class ConnectDB {
    private static ConnectDB instance;
    private Properties properties;
    private Connection conn;

   private ConnectDB() {
	   
	   properties = new Properties();
	   try   (InputStream input = getClass().getClassLoader().getResourceAsStream("database.properties")) {
		 if(input == null) {
			 System.out.println("Xin lỗi, không tìm thấy file database.properties");
             return;
		 }
		 
		 properties.load(input);
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	   
   }
   
   public static ConnectDB getInstance () {
	   if (instance == null) {
           instance = new ConnectDB();
       }
	   
	   return instance;
   }
   
   public Connection getConnection() throws SQLException{
	   if( conn == null || conn.isClosed()) {
		   conn =   DriverManager.getConnection(
				   properties.getProperty("db.url"),properties.getProperty("db.user"),properties.getProperty("db.password")
				   
				   
				   );
	   	}
	  
	    return conn;
   }
   
   
   
    
   
    
}