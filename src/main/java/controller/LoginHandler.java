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
import javax.persistence.NoResultException;
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
import model.Buildable;
import model.User;
import planets.Planets_General;

@ManagedBean(name="loginHandler")
@SessionScoped
public class LoginHandler implements Serializable{

	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private UserHandler handler = new UserHandler();
	private GalaxyHandler gHandler;
	private PlanetHandler planetHandler;
	private BuildHandler buildHandler;
	

	@PersistenceContext
	private EntityManager em;

	@Resource
	private UserTransaction utx;

	@PostConstruct
	public void init() {
		gHandler = new GalaxyHandler();
		planetHandler = new PlanetHandler(em, utx, gHandler);
		buildHandler = new BuildHandler(planetHandler,em);
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
			install();
			em.persist(user);
			try {
				utx.commit();
			} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
					| HeuristicRollbackException | SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			planetHandler.createNewPlanet(user.getUserID());
		}
	}

	private void install() {
		// name		type	baseCostMetal baseCostCrystal baseCostDeut baseCostEnergy	resFactor	energyFactor	Descr	rec
		//type 0= building 1=tech 2=ship
		em.persist(new Buildable("Metallmine", 0, 40, 15, 0, 10, 1.5, 1.1, "Hauptrohstoff für den Bau tragender Strukturen von Bauwerken und Schiffen.", "test"));
		em.persist(new Buildable("Kristallmine", 0, 30, 15, 0, 10, 1.6, 1.1, "Hier wird Kristall abgebaut - der Hauptrohstoff für elektronische Bauteile und Legierungen.", "test"));
		em.persist(new Buildable("Deuterium-Synthetisierer", 0, 150, 50, 0, 20, 1.5, 1.1, "Deuterium-Synthetisierer entziehen dem Wasser eines Planeten den geringen Deuteriumanteil.", "test"));
		em.persist(new Buildable("Solarkraftwerk", 0, 50, 20, 0, 20, 1.5, 1.1, "Solarkraftwerke gewinnen aus Sonneneinstrahlung die Energie, die einige Gebäude für den Betrieb benötigen.", "test"));
		em.persist(new Buildable("Fusionskraftwerk", 0, 500, 200, 100, 30, 1.8, 1.05, "Das Fusionskraftwerk gewinnt Energie aus der Fusion von 2 schweren Wasserstoffatomen zu einem Heliumatom.", "test"));
		em.persist(new Buildable("Metallspeicher", 0, 500, 0, 0, 0, 2, 0, "Lagerstätte für rohe Metallerze, bevor sie weiter verarbeitet werden.", "test"));
		em.persist(new Buildable("Kristallspeicher", 0, 500, 250, 0, 0, 2, 0, "Lagerstätte für rohe Kristalle, bevor sie weiterverarbeitet werden.", "test"));
		em.persist(new Buildable("Deuteriumtank", 0, 500, 500, 0, 0, 2, 0, "Riesige Tanks zur Lagerung des neu gewonnenen Deuteriums.", "test"));
	}

	public String login() {
		Query query = em.createQuery("select k from User k where k.username = :username and k.password = :password ");
		query.setParameter("username", username);
		query.setParameter("password", password);
		try {
			User  user = (User) query.getSingleResult();
			handler.setUser(user);
			query = em.createQuery("select k from Planets_General k where k.userid = :userid");
			query.setParameter("userid", handler.getUser().getUserID());
			@SuppressWarnings("unchecked")
			List<Planets_General> planets = query.getResultList();
			planetHandler.init(planets);
			query = em.createQuery("select galaxy from Planets_General k where k.userid = :userid and name = :name");
			query.setParameter("userid", handler.getUser().getUserID());
			query.setParameter("name", "Heimatplanet");
			int id = (int) query.getSingleResult();
			gHandler.setGalaxyForTable(id);
			query = em.createQuery("select solarsystem from Planets_General k where k.userid = :userid and name = :name");
			query.setParameter("userid", handler.getUser().getUserID());
			query.setParameter("name", "Heimatplanet");
			id = (int) query.getSingleResult();
			gHandler.setSystemForTable(id);
			return"/main.xhtml?faces-redirect=true";
		}catch (NoResultException e) {
			System.out.println("Kein User vorhanden");
			return "/login.xhtml?faces-redirect=true";
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
			planetHandler.createNewPlanet(user.getUserID());
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
		User user = handler.getUser();
		user.setLastlogin(System.currentTimeMillis());
		try {
			utx.begin();
		} catch (NotSupportedException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		em.merge(user);
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

	public PlanetHandler getPlanetHandler() {
		return planetHandler;
	}

	public void setPlanetHandler(PlanetHandler pHandler) {
		this.planetHandler = pHandler;
	}

	public BuildHandler getBuildHandler() {
		return buildHandler;
	}

	public void setBuildHandler(BuildHandler buildHandler) {
		this.buildHandler = buildHandler;
	}

	


}