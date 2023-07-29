package model;

import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "films")
public class FilmList {

	@XmlElement(name = "film")
	private List<Film> filmsList;
	public FilmList() {}
	
	public FilmList(List<Film> filmsList) {
		this.filmsList = filmsList;
	}
	
	public List<Film> getFilmsList(){
		return filmsList;
	}
	
	public void setFilmsList(List<Film> filmsList) {
		this.setFilmsList(filmsList);
	}
}
