package es.salazaryasociados.db.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="User", schema = "public",
	uniqueConstraints={
		   @UniqueConstraint(columnNames={"username"})
		})
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6598228681756418854L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="Id")
	private Integer id;
	
	@NotNull(message="USER_SHORT_NAME")
	@Size(max=10, min=4,message="USER_SHORT_NAME")
	@Column(name="Username", nullable=false)	
	private String username;

	@Column(name="Password", nullable=false)
	@NotNull(message="USER_SHORT_PASSWORD")
	@Size(min=4,message="USER_SHORT_PASSWORD")
	private String password;
		
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
		name="RoleVSUser"
		, joinColumns={@JoinColumn(name="uid", referencedColumnName="id")}
		, inverseJoinColumns={@JoinColumn(name="role", referencedColumnName="id")}
		)
	private Set<Role> roles = new HashSet<Role>();	

	public String getPassword() {
		return password;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}
	
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (roles == null) {
			if (other.roles != null)
				return false;
		} 
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}	
}
