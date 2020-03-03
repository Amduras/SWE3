package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import enums.AuthLvl;
import enums.IsActive;
import planets.Planets_General;

@SuppressWarnings("serial")
@NamedQuery(name="SelectUsers", query="Select k from User k")
@Entity
public class User implements Serializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer userID;
	
	private String username;
	private String email;
	private String password;
	private IsActive isActive;
	private Long lastlogin;
	private int picture;
	private AuthLvl authLvl;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "user_id")
	private List<Planets_General> planets = new ArrayList<>();
	
	public User(){
		
	}
	
	public User(String username, String email, String password, IsActive isActive, AuthLvl authLvl) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.isActive = isActive;
		this.authLvl = authLvl;
	}
	
	public Integer getUserID() {
		return userID;
	}

	public void setUserID(Integer userID) {
		this.userID = userID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public IsActive getIsActive() {
		return isActive;
	}

	public void setIsActive(IsActive isActive) {
		this.isActive = isActive;
	}

	public Long getLastlogin() {
		return lastlogin;
	}

	public void setLastlogin(Long lastlogin) {
		this.lastlogin = lastlogin;
	}

	public int getPicture() {
		return picture;
	}

	public void setPicture(int picture) {
		this.picture = picture;
	}

	public AuthLvl getAuthLvl() {
		return authLvl;
	}

	public void setAuthLvl(AuthLvl authLvl) {
		this.authLvl = authLvl;
	}

	public List<Planets_General> getPlanets() {
		return planets;
	}

	public void setPlanets(List<Planets_General> planets) {
		this.planets = planets;
	}
	
	
}
