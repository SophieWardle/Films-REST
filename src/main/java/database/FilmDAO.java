package database;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.*;

import model.Film;
public class FilmDAO {
	
	Film oneFilm = null;
	Connection conn = null;
    Statement stmt = null;
	String user = "wardleso";
    String password = "pUshgrep9";
    // Note none default port used, 6306 not 3306
    String url = "jdbc:mysql://mudfoot.doc.stu.mmu.ac.uk:6306/"+user;

	public FilmDAO() {}

	
	private Statement openConnection(){
		// loading jdbc driver for mysql
		try{
		    Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch(Exception e) { System.out.println(e); }

		// connecting to database
		try{
			// connection string for demos database, username demos, password demos
 			conn = DriverManager.getConnection(url, user, password);
		    stmt = conn.createStatement();
		} catch(SQLException se) { System.out.println(se); }
		return stmt;
    }
	private void closeConnection(){
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Film getNextFilm(ResultSet rs){
    	Film thisFilm=null;
		try {
			thisFilm = new Film(
					rs.getInt("id"),
					rs.getString("title"),
					rs.getInt("year"),
					rs.getString("director"),
					rs.getString("stars"),
					rs.getString("review"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return thisFilm;		
	}
	
	
	
   public ArrayList<Film> getAllFilms(){
	   
		ArrayList<Film> allFilms = new ArrayList<Film>();
		openConnection();
		
	    // Create select statement and execute it
		try{
		    String selectSQL = "SELECT * FROM films";
		    System.out.print(selectSQL);
		    ResultSet rs1 = stmt.executeQuery(selectSQL);
	    // Retrieve the results
		    while(rs1.next()){
		    	oneFilm = getNextFilm(rs1);
		    	allFilms.add(oneFilm);
		   }

		    stmt.close();
		    closeConnection();
		} catch(SQLException se) { System.out.println(se); }

	   return allFilms;
   }

   public Film getFilmByID(int id){
	   
		openConnection();
		oneFilm=null;
	    // Create select statement and execute it
		try{
		    String selectSQL = "select * from films where id="+id;
		    ResultSet rs1 = stmt.executeQuery(selectSQL);
	    // Retrieve the results
		    while(rs1.next()){
		    	oneFilm = getNextFilm(rs1);
		    }

		    stmt.close();
		    closeConnection();
		} catch(SQLException se) { System.out.println(se); }

	   return oneFilm;
   }
   
   public ArrayList<Film> getAllFilmsByTitle(String searchname) {
	   
	    ArrayList<Film> allFilmsByTitle = new ArrayList<Film>();
		openConnection();
		// Create select statement and execute it
		try{
			String selectSQL = "SELECT * FROM films WHERE title LIKE '%"+searchname+" %' ";
			ResultSet rs1 = stmt.executeQuery(selectSQL);
		//Retrieve the results
			while(rs1.next()) {
				oneFilm = getNextFilm(rs1);
				allFilmsByTitle.add(oneFilm);
			}
			stmt.close();
			closeConnection();
		} catch(SQLException se) { System.out.println(se); }
		return allFilmsByTitle;
   }
   
   public boolean insertFilm(Film f) throws SQLException {
	   boolean b = false;
	   
	   try {
			String sql = "INSERT INTO films (title, year, director, stars, review) VALUES ('" + f.getTitle() + "','" + f.getYear() + "','" + f.getDirector() + "','" + f.getStars() + "','" + f.getReview() +"');";
			System.out.println(sql);
			b = openConnection().execute(sql);
			closeConnection();
			b = true;
		} catch (SQLException s) {
			throw new SQLException("Film Not Added");
		}
		return b; 
   }
   
   public boolean deleteFilm(Film f) throws SQLException {
	   boolean b = false;
	   try {
		   String sql = "DELETE FROM films WHERE ID = '"+f.getId()+"';";
		   System.out.print(sql);
		   b = openConnection().execute(sql);
		   closeConnection();
		   b = true;
	   } catch (SQLException s) {
		   throw new SQLException("Film Not Deleted");
	   }
	   return b;
   }
   
   public boolean updateFilm(Film f) throws SQLException {
	   boolean b = false;
	   try {
		   String sql = "UPDATE films SET title ='"+ f.getTitle() +"', year = '"+f.getYear()+"', director = '"+f.getDirector()+"', stars = '"+f.getStars()+"', review = '"+f.getReview()+"' WHERE id = '" + f.getId() +"';";
		   System.out.print(sql);
		   b = openConnection().execute(sql);
		   closeConnection();
		   b = true;
	   } catch (SQLException s) {
		   throw new SQLException("Film Not Added");
	   }
	   return b;
   }
   
   public ArrayList<Film> searchFilms(String searchOption, String userInput) {
	   ArrayList<Film> filmArray = new ArrayList<Film>();
	   try {
		   String sql = "SELECT * FROM films WHERE " + searchOption + " LIKE '%" + userInput +"%';";
		   System.out.println(sql);
		   ResultSet rs = openConnection().executeQuery(sql);
		   if (rs != null) {
			   while (rs.next()) {
				   Film film = new Film();
				   try {
					   film.setId(rs.getInt("id"));
					   film.setTitle(rs.getString("title"));
					   film.setYear(rs.getInt("year"));
					   film.setDirector(rs.getString("director"));
					   film.setStars(rs.getString("stars"));
					   film.setReview(rs.getString("review"));
				   } catch (SQLException s) {
					   s.printStackTrace();
				   }
				   filmArray.add(film);
			   }
			   rs.close();
		   }
		   } catch (SQLException s) {
			   System.out.println(s);
		   }
		   closeConnection();
		   return filmArray;
	   }
   
   	//Search films but for int (doesn't work with the 'single quotes')
	   public ArrayList<Film> searchFilmsInt(String searchOption, String userInput) {
		   ArrayList<Film> filmArray = new ArrayList<Film>();
		   try {
			   String sql = "SELECT * FROM films WHERE " + searchOption + " = '" + userInput +"';";
			   System.out.println(sql);
			   ResultSet rs = openConnection().executeQuery(sql);
			   if (rs != null) {
				   while (rs.next()) {
					   Film film = new Film();
					   try {
						   film.setId(rs.getInt("id"));
						   film.setTitle(rs.getString("title"));
						   film.setYear(rs.getInt("year"));
						   film.setDirector(rs.getString("director"));
						   film.setStars(rs.getString("stars"));
						   film.setReview(rs.getString("review"));
					   } catch (SQLException s) {
						   s.printStackTrace();
					   }
					   filmArray.add(film);
				   }
				   rs.close();
			   }
			   } catch (SQLException s) {
				   System.out.println(s);
			   }
			   closeConnection();
			   return filmArray;
		   }
   
   }

