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
import planets.Planets_General;

@ManagedBean(name="loginHandler")
@SessionScoped
public class LoginHandler implements Serializable{

	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private UserHandler handler = new UserHandler();
	private PlanetHandler pHandler;

	@PersistenceContext
	private EntityManager em;

	@Resource
	private UserTransaction utx;

	@PostConstruct
	public void init() {
		pHandler = new PlanetHandler(em);
		Query query = em.createQuery("select k from User k where k.username = :username");
		query.setParameter("username", "admin");
		@SuppressWarnings("unchecked")
		List<User> qusers = query.getResultList();
		if(qusers.size() == 0) {
			User user = new User("admin","admin@admin.de","admin", IsActive.TRUE, AuthLvl.SGA);
			try {
				utx.begin();
			} catch (NotSupportedException | SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pHandler.createNewPlanet(user);
			em.persist(user);
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
			handler.setUser(users.get(0));
			query = em.createQuery("select k from Planets_General k where k.user = :user");
			query.setParameter("user", handler.getUser());
			@SuppressWarnings("unchecked")
			List<Planets_General> planet = query.getResultList();
			pHandler.init(planet);
			return"/main.xhtml?faces-redirect=true";
		} else { 
			return null;
		}
	}
	
	public String neuerUser() {
		Query query = em.createQuery("select k from User k where k.username = :username");
		query.setParameter("username", username);
		@SuppressWarnings("unchecked")
		List<User> qusers = query.getResultList();
		if(qusers.size() == 0) {
			User user = new User();
			user.setAuthLvl(AuthLvl.USER);
			user.setIsActive(IsActive.TRUE);
			user.setUsername(username);
			user.setPassword(password);
			user.setEmail("asd@web.de");
						
			try {
				utx.begin();
			} catch (NotSupportedException | SystemException e) {
				e.printStackTrace();
			}
			user = em.merge(user);
			pHandler.createNewPlanet(user);
			em.persist(user);
			try {
				utx.commit();
			} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
					| HeuristicRollbackException | SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "login";
		} else {
			return "login";
		}
		
	}
	
	public boolean isAdmin() {
		if(handler.getUser().getAuthLvl() == AuthLvl.SGA) {
			return true;
		} else {
			return false;
		}
	}
	
	public void checkLoggedIn(ComponentSystemEvent cse) {
		FacesContext context = FacesContext.getCurrentInstance();
		if (handler.getUser() == null) {
			context.getApplication().getNavigationHandler().handleNavigation(context, null, "/login.xhtml?faces-redirect=true");
		}
	}

	public String logout () {
		Query query = em.createQuery("UPDATE User SET lastlogin = :time where username = :username");
		query.setParameter("time", System.currentTimeMillis());
		query.setParameter("username", handler.getUser().getUsername());
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

	public UserHandler getHandler() {
		return handler;
	}

	public void setHandler(UserHandler handler) {
		this.handler = handler;
	}

	


}