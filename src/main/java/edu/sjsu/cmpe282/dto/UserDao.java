package edu.sjsu.cmpe282.dto;

import java.sql.*;

import edu.sjsu.cmpe282.domain.User;

public class UserDao {
	Connection conn = null;
	Statement stmt = null;
	String url = "jdbc:mysql://localhost/test";
	String uname = "";
	String pwd = "";
	// Constructure with JDBC connection
	public UserDao()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, uname, pwd);
		}
		catch (Exception e)
		{

			e.printStackTrace();
		}
	}

	public boolean addUser(User user)
	{
		try {
			stmt = conn.createStatement();
			String query = "INSERT INTO `users` (`firstname`, `lastname`, `email`, `password`) VALUES ('" + user.getFirstName() + "', '" + user.getLastName() + "', '" + user.getEmail() + "', '" + user.getPasswd() + "');";
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public boolean checkUser(User user){
		ResultSet rs;
		String origPasswd = null;
		try {
			stmt = conn.createStatement();
			String query = "Select password from users where email = '"+user.getEmail()+"';";
			rs = stmt.executeQuery(query);
			rs.next();
			origPasswd = rs.getString("password");
			System.out.println("Password from db : "+ origPasswd );
			System.out.println("Password entered : "+user.getPasswd());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return user.getPasswd().equals(origPasswd);
	}

}
