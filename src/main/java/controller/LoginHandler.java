package controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
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
import enums.IsActive;
import model.User;

@ManagedBean(name="loginHandler")
@SessionScoped
public class LoginHandler implements Serializable{

	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private User user = null;

	@PersistenceContext
	private EntityManager em;

	@Resource
	private UserTransaction utx;

	@PostConstruct
	public void init() {
		Query query = em.createQuery("select k from User k where k.username = :username");
		query.setParameter("username", "admin");
		@SuppressWarnings("unchecked")
		List<User> qusers = query.getResultList();
		if(qusers.size() == 0) {
			try {
				utx.begin();
			} catch (NotSupportedException | SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			em.persist(new User("admin","admin@admin.de","admin", IsActive.TRUE, AuthLvl.SGA));
			try {
				utx.commit();
			} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
					| HeuristicRollbackException | SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public String login() {
		Query query = em.createQuery("select k from User k where k.username = :username and k.password = :password ");
		query.setParameter("username", username);
		query.setParameter("password", password);
		@SuppressWarnings("unchecked")
		List<User> users = query.getResultList();
		if(users.size() == 1) {
			user = users.get(0);
			return"/main.xhtml?faces-redirect=true";
		} else { 
			return null;
		}
	}
	
	public boolean isAdmin() {
		if(user.getAuthLvl() == AuthLvl.SGA) {
			return true;
		} else {
			return false;
		}
	}
	
	public void checkLoggedIn(ComponentSystemEvent cse) {
		FacesContext context = FacesContext.getCurrentInstance();
		if (user == null) {
			context.getApplication().getNavigationHandler().handleNavigation(context, null, "/login.xhtml?faces-redirect=true");
		}
	}

	public String logout () {
		Query query = em.createQuery("UPDATE User SET lastlogin = :time where username = :username");
		query.setParameter("time", System.currentTimeMillis());
		query.setParameter("username", user.getUsername());
		try {
			utx.begin();
		} catch (NotSupportedException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		query.executeUpdate();
		try {
			utx.commit();
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
				| HeuristicRollbackException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return"/login.xhtml?faces-redirect=true";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


}