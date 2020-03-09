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
		gHandler = new GalaxyHandler(em, utx);
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
		em.persist(new Buildable("Metallmine", 0, 40, 10, 0, 10, 1.5, 1.1, "Hauptrohstoff für den Bau tragender Strukturen von Bauwerken und Schiffen.", "test"));
		em.persist(new Buildable("Kristallmine", 0, 30, 15, 0, 10, 1.6, 1.1, "Hier wird Kristall abgebaut - der Hauptrohstoff für elektronische Bauteile und Legierungen.", "test"));
		em.persist(new Buildable("Deuterium-Synthetisierer", 0, 150, 50, 0, 20, 1.5, 1.1, "Deuterium-Synthetisierer entziehen dem Wasser eines Planeten den geringen Deuteriumanteil.", "test"));
		em.persist(new Buildable("Solarkraftwerk", 0, 50, 20, 0, 20, 1.5, 1.1, "Solarkraftwerke gewinnen aus Sonneneinstrahlung die Energie, die einige Gebäude für den Betrieb benötigen.", "test"));
		em.persist(new Buildable("Fusionskraftwerk", 0, 500, 200, 100, 30, 1.8, 1.05, "Das Fusionskraftwerk gewinnt Energie aus der Fusion von 2 schweren Wasserstoffatomen zu einem Heliumatom.", "test"));
		em.persist(new Buildable("Metallspeicher", 0, 500, 0, 0, 0, 2, 0, "Lagerstätte für rohe Metallerze, bevor sie weiter verarbeitet werden.", "test"));
		em.persist(new Buildable("Kristallspeicher", 0, 500, 250, 0, 0, 2, 0, "Lagerstätte für rohe Kristalle, bevor sie weiterverarbeitet werden.", "test"));
		em.persist(new Buildable("Deuteriumtank", 0, 500, 500, 0, 0, 2, 0, "Riesige Tanks zur Lagerung des neu gewonnenen Deuteriums.", "test"));
		em.persist(new Buildable("Roboterfabrik", 0, 200, 60, 100, 0, 2, 0, "Roboterfabriken stellen einfache Arbeitskräfte zur Verfügung, die beim Bau der planetaren Infrastruktur eingesetzt werden können. Jede Stufe erhöht damit die Geschwindigkeit des Ausbaus von Gebäuden.", "test"));
		em.persist(new Buildable("Raumschiffswerft", 0, 200, 100, 50, 0, 2, 0, "In der planetaren Werft werden alle Arten von Schiffen und Verteidigungsanlagen gebaut.", "test"));
		em.persist(new Buildable("Forschungslabor", 0, 100, 200, 100, 0, 2, 0, "Um neue Technologien zu erforschen, ist der Betrieb einer Forschungsstation notwendig.", "test"));
		em.persist(new Buildable("Allianzdepot", 0, 10000, 20000, 0, 0, 2, 0, "Das Allianzdepot bietet dir die Möglichkeit, befreundete Flotten, die bei der Verteidigung helfen und in deinem Orbit stehen, mit Treibstoff zu versorgen.", "test"));
		em.persist(new Buildable("Raketensilo", 0, 10000, 10000, 500, 0, 2, 0, "Raketensilos dienen zum Einlagern von Raketen.", "test"));
		em.persist(new Buildable("Nanitenfabrik", 0, 500000, 250000, 50000, 0, 2, 0, "Dies ist die Krönung der Robotertechnik. Jede Stufe halbiert die Bauzeit von Gebäuden, Schiffen und Verteidigung.", "test"));
		em.persist(new Buildable("Terraformer", 0, 0, 25000, 50000, 500, 2, 2, "Terraformer vergrößern die nutzbare Fläche auf einem Planeten.", "test"));
		em.persist(new Buildable("Energietechnik", 1, 0, 400, 200, 0, 2, 0, "Die Beherrschung der unterschiedlichen Arten von Energie ist für viele neue Technologien notwendig.", "test"));
		em.persist(new Buildable("Lasertechnik", 1, 100, 400, 50, 0, 2, 0, "Durch Bündelung des Lichts entsteht ein Strahl, der beim Auftreffen auf ein Objekt Schaden anrichtet.", "test"));
		em.persist(new Buildable("Ionentechnik", 1, 500, 150, 50, 0, 2, 0, "Durch die Konzentration von Ionen können Geschütze gebaut werden, die beträchtlichen Schaden anrichten können und die Abrisskosten von Gebäuden pro Stufe um 4% verringern.", "test"));
		em.persist(new Buildable("Hyperraumtechnik", 1, 0, 2000, 1000, 0, 2, 0, "Durch die Einbindung der vierten und fünften Dimension ist es nun möglich, einen neuartigen Antrieb zu erforschen, der sparsamer und leistungsfähiger ist.", "test"));
		em.persist(new Buildable("Plasmatechnik", 1, 1000, 2000, 500, 0, 2, 0, "Eine Weiterentwicklung der Ionentechnik, die hochenergetisches Plasma beschleunigt, das verheerende Schäden anrichten kann, und die Produktion von Metall, Kristall und Deuterium (1%/0,66%/0,33% pro Stufe) optimiert.", "test"));
		em.persist(new Buildable("Verbrennungstriebwerk", 1, 200, 0, 300, 0, 2, 0, "Die Weiterentwicklung dieser Triebwerke macht einige Schiffe schneller. Jede Stufe steigert die Geschwindigkeit um 10% des Grundwertes.", "test"));
		em.persist(new Buildable("Impulstriebwerk", 1, 1000, 2000, 300, 0, 2, 0, "Das Impulstriebwerk basiert auf dem Rückstoßprinzip. Die Weiterentwicklung dieser Triebwerke steigert die Geschwindigkeit einiger Schiffe um 20% des Grundwertes.", "test"));
		em.persist(new Buildable("Hyperraumantrieb", 1, 5000, 10000, 3000, 0, 2, 0, "Die Hyperraumtechnologie ist die gezielte Krümmung des Raums durch die Einbindung der vierten und fünften Dimension. Jede Stufe dieser Technologie beschleunigt deine Schiffe um 30% des Grundwertes.", "test"));
		em.persist(new Buildable("Spionagetechnik", 1, 100, 500, 100, 0, 2, 0, "Die Beherrschung der unterschiedlichen Arten von Energie ist für viele neue Technologien notwendig.", "test"));
		em.persist(new Buildable("Computertechnik", 1, 0, 200, 300, 0, 2, 0, "Mit der Erhöhung der Computerkapazitäten lassen sich immer mehr Flotten befehligen. Jede Stufe Computertechnik erhöht dabei die maximale Flottenanzahl um eins.", "test"));
		em.persist(new Buildable("Astrophysik", 1, 0, 100, 100, 100, 2.75, 0, "Schiffe mit einem Forschungsmodul können weite Expeditionen unternehmen. Für zwei neue Stufen dieser Technologie kann ein weiterer Planet kolonisiert werden.", "test"));
		em.persist(new Buildable("Gravitonforschung", 1, 0, 0, 0, 100000, 0, 3, "Durch den Abschuss einer konzentrierten Ladung von Gravitonpartikeln kann ein künstliches Gravitationsfeld errichtet werden. Es hat das Potenzial, sogar große Schiffe oder ganze Monde zu vernichten.", "test"));
		em.persist(new Buildable("Waffentechnik", 1, 400, 100, 0, 0, 2, 0, "Die Waffentechnik steigert die Leistung sämtlicher Waffensysteme, so dass die Schusskraft jeder Einheit pro Stufe auf 10% über den Grundwert ansteigt.", "test"));
		em.persist(new Buildable("Schildtechnik", 1, 100, 300, 0, 0, 2, 0, "Schildtechnik macht die Schilde der Schiffe und Verteidigungsanlagen effizienter. Jede Stufe steigert die Effizienz um 10% des Grundwertes.", "test"));
		em.persist(new Buildable("Raumschiffpanzerung", 1, 0, 400, 200, 0, 2, 0, "Spezielle Legierungen verbessern die Panzerung der Raumschiffe stetig. Die Wirksamkeit der Panzerung kann so pro Stufe um 10% gesteigert werden.", "test"));
		em.persist(new Buildable("Leichter Jäger", 2, 3000, 1000, 0, 0, 1, 0, "Der leichte Jäger ist ein wendiges Schiff, das man in dieser Form auf fast jedem Planeten vorfinden kann. Er ist zwar günstig, in seinen Fähigkeiten allerdings auch recht begrenzt.", "test"));
		em.persist(new Buildable("Schwerer Jäger", 2, 6000, 4000, 0, 0, 1, 0, "Diese Weiterentwicklung des leichten Jägers ist besser gepanzert und hat eine höhere Angriffsstärke.", "test"));
		em.persist(new Buildable("Kreuzer", 2, 20000, 7000, 2000, 0, 1, 0, "Kreuzer sind fast dreimal so stark gepanzert wie schwere Jäger und verfügen über mehr als die doppelte Schusskraft. Zudem sind sie sehr schnell.", "test"));
		em.persist(new Buildable("Schlachtschiff", 2, 45000, 15000, 0, 0, 1, 0, "Diese Weiterentwicklung des leichten Jägers ist besser gepanzert und hat eine höhere Angriffsstärke.", "test"));
		em.persist(new Buildable("Schlachtkreuzer", 2, 30000, 40000, 15000, 0, 1, 0, "Der Schlachtkreuzer ist auf das Abfangen feindlicher Flotten spezialisiert.", "test"));
		em.persist(new Buildable("Bomber", 2, 50000, 25000, 15000, 0, 1, 0, "Der Bomber wurde speziell entwickelt, um die Verteidigung eines Planeten zu zerstören.", "test"));
		em.persist(new Buildable("Zerstörer", 2, 60000, 50000, 15000, 0, 1, 0, "Der Zerstörer ist der König unter den Kriegsschiffen.", "test"));
		em.persist(new Buildable("Todesstern", 2, 5000000, 4000000, 1000000, 0, 1, 0, "Die Zerstörungskraft des Todessterns ist unübertroffen.", "test"));
		em.persist(new Buildable("Kleiner Transporter", 2, 2000, 2000, 0, 0, 1, 0, "Der kleine Transporter ist ein wendiges Schiff, welches Rohstoffe schnell zu anderen Planeten transportieren kann.", "test"));
		em.persist(new Buildable("Großer Transporter", 2, 6000, 6000, 0, 0, 1, 0, "Die Weiterentwicklung des kleinen Transporters hat ein größeres Ladevermögen und ist dank seines verbesserten Antriebs auch schneller.", "test"));
		em.persist(new Buildable("Kolonieschiff", 2, 10000, 20000, 10000, 0, 1, 0, "Mit diesem Schiff können fremde Planeten kolonisiert werden.", "test"));
		em.persist(new Buildable("Recycler", 2, 10000, 6000, 2000, 0, 1, 0, "Mit dem Recycler lassen sich Rohstoffe aus Trümmerfeldern gewinnen.", "test"));
		em.persist(new Buildable("Spionagesonde", 2, 0, 1000, 0, 0, 1, 0, "Spionagesonden sind kleine wendige Drohnen, die über weite Entfernungen Daten zu Flotten und Planeten liefern.", "test"));
		em.persist(new Buildable("Solarsatellit", 2, 0, 2000, 500, 0, 1, 0, "Solarsatelliten sind einfache Plattformen aus Solarzellen, die sich in einem hohen stationären Orbit befinden. Sie sammeln das Sonnenlicht und geben es per Laser an die Bodenstation weiter. Ein Solarsatellit erzeugt auf diesem Planeten 32 Energie.", "test"));
		em.persist(new Buildable("Raketenwerfer", 2, 2000, 0, 0, 0, 1, 0, "Der Raketenwerfer ist eine einfache und kostengünstige Verteidigungsmöglichkeit.", "test"));
		em.persist(new Buildable("Leichtes Lasergeschütz", 2, 1500, 500, 0, 0, 1, 0, "Durch den konzentrierten Beschuss eines Ziels mit Photonen kann wesentlich größerer Schaden erzielt werden als mit gewöhnlichen ballistischen Waffen.", "test"));
		em.persist(new Buildable("Schweres Lasergeschütz", 2, 6000, 4000, 0, 0, 1, 0, "Der schwere Laser ist eine konsequente Weiterentwicklung des leichten Lasers.", "test"));
		em.persist(new Buildable("Gaußkanone", 2, 20000, 15000, 2000, 0, 1, 0, "Die Gaußkanone beschleunigt tonnenschwere Geschosse unter gigantischem Energieaufwand.", "test"));
		em.persist(new Buildable("Ionengeschütz", 2, 5000, 3000, 0, 0, 1, 0, "Das Ionengeschütz schleudert eine Welle von Ionen, die ihrem Ziel erheblichen Schaden zufügt.", "test"));
		em.persist(new Buildable("Plasmawerfer", 2, 50000, 50000, 30000, 0, 1, 0, "Plasmageschütze setzen die Kraft einer Sonneneruption frei und übertreffen in ihrer vernichtenden Wirkung sogar den Zerstörer.", "test"));
		em.persist(new Buildable("Kleine Schildkuppel", 2, 10000, 10000, 0, 0, 1, 0, "Die kleine Schildkuppel umhüllt den ganzen Planeten mit einem Feld, das ungeheure Energiemengen absorbieren kann.", "test"));
		em.persist(new Buildable("Große Schildkuppel", 2, 50000, 50000, 0, 0, 1, 0, "Die Weiterentwicklung der kleinen Schildkuppel kann wesentlich mehr Energie einsetzen, um Angriffe abzuwehren.", "test"));
		em.persist(new Buildable("Abfangrakete", 2, 8000, 2000, 0, 0, 1, 0, "Abfangraketen zerstören angreifende Interplanetarraketen.", "test"));
		em.persist(new Buildable("Interplanetarrakete", 2, 12500, 2500, 10000, 0, 1, 0, "Interplanetarraketen zerstören die gegnerische Verteidigung.", "test"));		
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
			gHandler.setUser(user);
			gHandler.createPlanetList();
			return"/main.xhtml?faces-redirect=true";
		}catch (NoResultException e) {
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

	public GalaxyHandler getgHandler() {
		return gHandler;
	}

	public void setgHandler(GalaxyHandler gHandler) {
		this.gHandler = gHandler;
	}

	


}