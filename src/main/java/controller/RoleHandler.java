package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import enums.AuthLvl;
import model.User;

@ManagedBean(name="roles")
@ViewScoped
public class RoleHandler implements Serializable{

	private static final long serialVersionUID = 1L;
	private User user;
	private User selectedUser;

	private List<String> rights = new ArrayList<>( Arrays.asList("BANNED", "RESTRICTED", "USER", "GA", "SGA"));
	private String newRight;
	
	@PersistenceContext
	private EntityManager em;

	@Resource
	private UserTransaction utx;
	
	public RoleHandler() {
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
		System.out.println("Gesetzt:" +selectedUser.getUsername());
		this.selectedUser = selectedUser;
	}

	public String getNewRight() {
		return newRight;
	}

	public void setNewRight(String newRight) {
		if(selectedUser != null) {
			if(!newRight.toLowerCase().equals(selectedUser.getAuthLvl().getLabel())) {
				this.newRight = newRight;
				Query query = em.createQuery("update User set authlvl = :authlvl where username = :username");
				query.setParameter("authlvl", (int) AuthLvl.valueOf(newRight).ordinal());
				query.setParameter("username", selectedUser.getUsername());
				try {
					utx.begin();
				} catch (NotSupportedException | SystemException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				query.executeUpdate();
				try {
					utx.commit();
				} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
						| HeuristicRollbackException | SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				FacesContext.getCurrentInstance().getViewRoot().getViewMap().remove("roles");
			} else {
				System.out.println("Nimm ne andere rolle trottel!");
			}
		} else {
			System.out.println("Kein User gewählt");
		}
		
	}
}
