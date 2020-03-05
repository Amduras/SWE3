package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import model.User;

@ManagedBean(name="roles")
@ViewScoped
public class RoleHandler implements Serializable{

	private static final long serialVersionUID = 1L;
	private User user;
	private User selectedUser;

	private List<String> rights = new ArrayList<>( Arrays.asList("BANNED", "RESTRICTED", "USER", "GA", "SGA"));
	private String newRight; 
	
	public RoleHandler() {
	}
	
	public void onRowSelect() {
		System.out.println("Test");
		System.out.println("User: "+selectedUser.getUsername());
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<String> getRights() {
		return rights;
	}

	public void setRights(List<String> rights) {
		this.rights = rights;
	}
	
	public User getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(User selectedUser) {
		System.out.println("Gesetzt:" +selectedUser);
		this.selectedUser = selectedUser;
	}

	public String getNewRight() {
		return newRight;
	}

	public void setNewRight(String newRight) {
		this.newRight = newRight;
	}
}
