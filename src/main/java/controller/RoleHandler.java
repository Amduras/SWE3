package controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

import enums.AuthLvl;
import model.User;

@ManagedBean(name="roles")
@SessionScoped
public class RoleHandler implements Serializable{

	private static final long serialVersionUID = 1L;
	private User user;
	private User selectedUser;

	private AuthLvl[] rights = {AuthLvl.BANNED, AuthLvl.RESTRICTED, AuthLvl.USER, AuthLvl.GA, AuthLvl.SGA};
	private AuthLvl[] userRights = new AuthLvl[5];
	
	public RoleHandler() {
		System.out.println("Role");
	}
	
	public void onRowSelect() {
		System.out.println("läuft");
//		user = (User) event.getObject();
//		System.out.println(user.getUsername());
	}
	
	public void test() {
		System.out.println("Test");
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public AuthLvl[] getRights() {
		return rights;
	}

	public void setRights(AuthLvl[] rights) {
		this.rights = rights;
	}

	public AuthLvl[] getUserRights() {
		return userRights;
	}

	public void setUserRights(AuthLvl[] userRights) {
		this.userRights = userRights;
	}
	
	public User getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(User selectedUser) {
		System.out.println("Gesetzt");
		this.selectedUser = selectedUser;
	}
}
