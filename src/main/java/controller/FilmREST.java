package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import org.apache.catalina.connector.Request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


import database.FilmDAO;
import model.Film;
import model.FilmList;

/**
 * Servlet implementation class FilmREST
 */
@WebServlet("/FilmREST")
public class FilmREST extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FilmREST() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// DATA FORMATS
		String format = request.getHeader("Accept");
		response.setHeader("Accept", format);
		
		PrintWriter out = response.getWriter();
		JAXBContext context;
		// Create an instance of FilmsDAO to use methods
		FilmDAO dao = new FilmDAO();
		// Select all contacts and assign to an array called allFilms
		ArrayList<Film> allFilms = dao.getAllFilms();

		if (format.equals("application/json")) {
			response.setContentType("application/json");
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String json = gson.toJson(allFilms);
			out.write(json);
			out.flush();
			out.close();
		} 
		else if (format.equals("application/xml")) {
			response.setContentType("application/xml");

			FilmList fl = new FilmList(allFilms);
			StringWriter sw = new StringWriter();
			try {
				context = JAXBContext.newInstance(FilmList.class);
				Marshaller m = context.createMarshaller();
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				m.marshal(fl, sw);
				System.out.println(sw.toString());
				out.write(sw.toString());
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		else if (format.equals("text/plain")) {
			response.setContentType("text/plain");
			for (int i = 0; i < allFilms.size(); i++) {
				out.write("$" + allFilms.get(i).getTitle() + "#" + 
						allFilms.get(i).getYear() + "#" +
						allFilms.get(i).getDirector() + "#" +
						allFilms.get(i).getReview() + "#" +
						allFilms.get(i).getStars() + "#" +
						allFilms.get(i).getId());
			} 
		}
		else {
			response.setContentType("application/json");
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String json = gson.toJson(allFilms);
			out.write(json);
			out.flush();
			out.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String data = request.getReader().lines().reduce("", 
				(accumulator, actual) -> accumulator + actual);
		System.out.println(data);
		// The format the user has entered the data in/how to process it
		String dataFormat = request.getHeader("Content-Type");
		JAXBContext context;
		Unmarshaller jaxbUnmarshaller;
		PrintWriter out = response.getWriter();
		Film f;
		FilmDAO dao = new FilmDAO();
		// check if user has entered json/xml/text
		if (dataFormat.equals("application/xml")) {
			try {
				context = JAXBContext.newInstance(Film.class);
				jaxbUnmarshaller = context.createUnmarshaller();
				f = (Film) jaxbUnmarshaller.unmarshal(new StringReader(data));
				try {
					dao.insertFilm(f);
					out.write("Success! Film entered by application/xml");
					System.out.println("Success! Film entered by application/xml");
				} catch (SQLException e) {
					e.printStackTrace();
				}

			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		else if (dataFormat.equals("application/json")) {
			Gson gson = new Gson();
			String jsonData = data;
			System.out.println(data);
			//Removes All Whitespace Characters and Replace with Literal Empty Space
			jsonData = jsonData.replaceAll("//s", "");
			f = gson.fromJson(jsonData, Film.class);
			try {
				dao.insertFilm(f);
				out.write(f.toString());
				out.write("Success! Film entered by application/json");
				System.out.println("Success! Film entered by application/json");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} 
		else if (dataFormat.equals("text/plain")) {
			Film fText = new Film();
			String[] splits = data.split("#");
			fText.setId(Integer.parseInt(splits[0]));
			fText.setTitle(splits[1]);
			fText.setDirector(splits[2]);
			fText.setYear(Integer.parseInt(splits[3]));
			fText.setStars(splits[4]);
			fText.setReview(splits[5]);
			try {
				dao.insertFilm(fText);
				out.write("Success! Film entered by text/plain");
				System.out.println("Success! Film entered by text/plain");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} 

	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// The format the user has entered the data in/how to process it
		String dataFormat = request.getHeader("Content-Type");
		JAXBContext context;
		Unmarshaller jaxbUnmarshaller;
		String data = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
		PrintWriter out = response.getWriter();
		FilmDAO dao = new FilmDAO();
		
		// check which data format the user has entered
		if (dataFormat.equals("application/xml")) {
			try {
				context = JAXBContext.newInstance(Film.class);
				jaxbUnmarshaller = context.createUnmarshaller();
				Film fXml = (Film) jaxbUnmarshaller.unmarshal(new StringReader(data));
				try {
					dao.updateFilm(fXml);
					out.write("Success! Film updated by application/xml");
					System.out.println("Success! Film updated by application/xml");
				} catch (SQLException e) {
					e.printStackTrace();
				}

			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		else if (dataFormat.equals("application/json")) {
			Gson gson = new Gson();
			String jsonData = data;
			//Removes All Whitespace Characters and Replace with Literal Empty Space
			jsonData = jsonData.replaceAll("//s", "");
			Film f = gson.fromJson(jsonData, Film.class);
			try {
				dao.updateFilm(f);
				out.write("Success! Film updated by application/json");
				System.out.println("Success! Film updated by application/json");
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} else if (dataFormat.equals("text/plain")) {
			Film fText = new Film();
			String[] splits = data.split("#");
			fText.setId(Integer.parseInt(splits[0]));
			fText.setTitle(splits[1]);
			fText.setDirector(splits[2]);
			fText.setYear(Integer.parseInt(splits[3]));
			fText.setStars(splits[4]);
			fText.setReview(splits[5]);
			try {
				dao.updateFilm(fText);
				out.write("Success! Film updated by text/plain");
				System.out.println("Success! Film updated by text/plain");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// The format the user has entered the data in/how to process it
		String dataFormat = request.getHeader("Content-Type");
		JAXBContext context;
		Unmarshaller jaxbUnmarshaller;
		// Data that user has entered through the body (raw data)
		String data = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
		PrintWriter out = response.getWriter();
		FilmDAO dao = new FilmDAO();
		if (dataFormat.equals("application/xml")) {
			try {
				context = JAXBContext.newInstance(Film.class);
				jaxbUnmarshaller = context.createUnmarshaller();
				Film fXml = (Film) jaxbUnmarshaller.unmarshal(new StringReader(data));
				try {
					dao.deleteFilm(fXml);
					out.write("Success! Film deleted by application/xml");
					System.out.println("Success! Film deleted by application/xml");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		else if (dataFormat.equals("application/json")) {
			Gson gson = new Gson();
			String jsonData = data;
			//Removes All Whitespace Characters and Replace with Literal Empty Space
			jsonData = jsonData.replaceAll("//s", "");
			Film f = gson.fromJson(jsonData, Film.class);
			try {
				dao.deleteFilm(f);
				out.write("Success! Film deleted by application/json");
				System.out.println("Success! Film deleted by application/json");
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} 
		else if (dataFormat.equals("text/plain")) {
			String[] splits = data.split("#");
			int deleteId = (Integer.parseInt(splits[0]));
			Film fText = dao.getFilmByID(deleteId);
			try {
				dao.deleteFilm(fText);
				out.write("Success! Film deleted by text/plain");
				System.out.println("Success! Film deleted by text/plain");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
