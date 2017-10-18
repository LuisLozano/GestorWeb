package es.salazaryasociados.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="Configuracion")
@NamedQuery(name="Configuracion.findAll", query="SELECT c FROM Configuracion c")
public class Configuracion implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7584660624283056508L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="Id")
	private Integer id;

	@Column(name="theme")
	private String theme;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}
}
