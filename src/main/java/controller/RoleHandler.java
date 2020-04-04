package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import enums.AuthLvl;
import model.User;

@ManagedBean(name="roles")
@SessionScoped
public class RoleHandler implements Serializable{

	private static final long serialVersionUID = 1L;
	private User user;
	private User selectedUser;

	private List<String> rights = new ArrayList<>( Arrays.asList("BANNED", "RESTRICTED", "USER", "GA", "SGA"));
	private String newRight;

	private EntityManager em;

	private UserTransaction utx;

	public RoleHandler() {
	}

	public RoleHandler(EntityManager em, UserTransaction utx) {
		this.em = em;
		this.utx = utx;
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
		this.selectedUser = selectedUser;
	}

	public String getNewRight() {
		return newRight;
	}

	public void setNewRight(String newRight) {
		if(selectedUser != null) {
			if(!selectedUser.getUsername().equals("admin")) {
				if(!newRight.toLowerCase().equals(selectedUser.getAuthLvl().getLabel())) {
					this.newRight = newRight;
					selectedUser.setAuthLvl(AuthLvl.valueOf(newRight));
					try {
						utx.begin();
					} catch (NotSupportedException | SystemException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					em.merge(selectedUser);
					//				query.executeUpdate();
					try {
						utx.commit();
					} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
							| HeuristicRollbackException | SystemException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}
	}
}