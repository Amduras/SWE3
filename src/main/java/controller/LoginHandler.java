package controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.event.Event;
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
import model.Messages;
import model.Ship;
import model.User;
import planets.Planets_Buildings;
import planets.Planets_Def;
import planets.Planets_General;
import planets.Planets_Research;
import planets.Planets_Ships;

@ManagedBean(name="loginHandler")
@SessionScoped
public class LoginHandler implements Serializable{

	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private UserHandler handler;
	private GalaxyHandler gHandler;
	private PlanetHandler planetHandler;
	private BuildHandler buildHandler;
	private RoleHandler roleHandler;
	private SettingsHandler settingsHandler;
	private MessageHandler messageHandler;
	private FleetHandler fleetHandler;
	private boolean test = true;


	@PersistenceContext
	private EntityManager em;

	@Resource
	private UserTransaction utx;

	@PostConstruct
	public void init() {
		handler = new UserHandler(em, utx);
		gHandler = new GalaxyHandler(em, utx);
		planetHandler = new PlanetHandler(em, utx, gHandler);
		buildHandler = new BuildHandler(planetHandler,em,utx);
		setFleetHandler(new FleetHandler(planetHandler,em,utx));
		roleHandler = new RoleHandler(em, utx);
		messageHandler = new MessageHandler(em, utx);
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
			install();
			try {
				utx.commit();
			} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
					| HeuristicRollbackException | SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		settingsHandler = new SettingsHandler(em, utx);
	}
	
	private void genTestMsg(User user) {
		em.persist(new Messages("1","admin", "Das issen Test du lappen", "Test"));
		em.persist(new Messages("1","admin", "Das issen Test2 du lappen", "Test2"));
		em.persist(new Messages("1","admin", "Das issen Test3 du lappen", "Test3"));
		em.persist(new Messages("1","admin", "Das issen Test4 du lappen", "Test4"));
	}
	
	private void install() {
		// name		type	baseCostMetal baseCostCrystal baseCostDeut baseCostEnergy	resFactor	energyFactor	Descr	rec
		//type 0= building 1=tech 2=ship/def
		em.persist(new Buildable("Metallmine", 0, 40, 10, 0, 10, 1.5, 1.1, "Hauptrohstoff für den Bau tragender Strukturen von Bauwerken und Schiffen.", ""));
		em.persist(new Buildable("Kristallmine", 0, 30, 15, 0, 10, 1.6, 1.1, "Hier wird Kristall abgebaut - der Hauptrohstoff für elektronische Bauteile und Legierungen.", ""));
		em.persist(new Buildable("Deuterium-Synthetisierer", 0, 150, 50, 0, 20, 1.5, 1.1, "Deuterium-Synthetisierer entziehen dem Wasser eines Planeten den geringen Deuteriumanteil.", ""));
		em.persist(new Buildable("Solarkraftwerk", 0, 50, 20, 0, 20, 1.5, 1.1, "Solarkraftwerke gewinnen aus Sonneneinstrahlung die Energie, die einige Gebäude für den Betrieb benötigen.", ""));
		em.persist(new Buildable("Fusionskraftwerk", 0, 500, 200, 100, 30, 1.8, 1.05, "Das Fusionskraftwerk gewinnt Energie aus der Fusion von 2 schweren Wasserstoffatomen zu einem Heliumatom.", "Deuterium-Synthetisierer:5<br/>Energytechnik:3"));
		em.persist(new Buildable("Metallspeicher", 0, 500, 0, 0, 0, 2, 0, "Lagerstätte für rohe Metallerze, bevor sie weiter verarbeitet werden.", ""));
		em.persist(new Buildable("Kristallspeicher", 0, 500, 250, 0, 0, 2, 0, "Lagerstätte für rohe Kristalle, bevor sie weiterverarbeitet werden.", ""));
		em.persist(new Buildable("Deuteriumtank", 0, 500, 500, 0, 0, 2, 0, "Riesige Tanks zur Lagerung des neu gewonnenen Deuteriums.", ""));
		em.persist(new Buildable("Roboterfabrik", 0, 200, 60, 100, 0, 2, 0, "Roboterfabriken stellen einfache Arbeitskräfte zur Verfügung, die beim Bau der planetaren Infrastruktur eingesetzt werden können. Jede Stufe erhöht damit die Geschwindigkeit des Ausbaus von Gebäuden.", ""));
		em.persist(new Buildable("Raumschiffswerft", 0, 200, 100, 50, 0, 2, 0, "In der planetaren Werft werden alle Arten von Schiffen und Verteidigungsanlagen gebaut.", "Roboterfabrik:2"));
		em.persist(new Buildable("Forschungslabor", 0, 100, 200, 100, 0, 2, 0, "Um neue Technologien zu erforschen, ist der Betrieb einer Forschungsstation notwendig.", ""));
		em.persist(new Buildable("Allianzdepot", 0, 10000, 20000, 0, 0, 2, 0, "Das Allianzdepot bietet dir die Möglichkeit, befreundete Flotten, die bei der Verteidigung helfen und in deinem Orbit stehen, mit Treibstoff zu versorgen.", ""));
		em.persist(new Buildable("Raketensilo", 0, 10000, 10000, 500, 0, 2, 0, "Raketensilos dienen zum Einlagern von Raketen.", ""));
		em.persist(new Buildable("Nanitenfabrik", 0, 500000, 250000, 50000, 0, 2, 0, "Dies ist die Krönung der Robotertechnik. Jede Stufe halbiert die Bauzeit von Gebäuden, Schiffen und Verteidigung.", "Roboterfabrik:10<br/>Computertechnik:10"));
		em.persist(new Buildable("Terraformer", 0, 0, 25000, 50000, 500, 2, 2, "Terraformer vergrößern die nutzbare Fläche auf einem Planeten.", "Nanitenfabrik:1<br/>Energytechnik:12"));
		em.persist(new Buildable("Energietechnik", 1, 0, 400, 200, 0, 2, 0, "Die Beherrschung der unterschiedlichen Arten von Energie ist für viele neue Technologien notwendig.", "Forschungslabor:1"));
		em.persist(new Buildable("Lasertechnik", 1, 100, 400, 50, 0, 2, 0, "Durch Bündelung des Lichts entsteht ein Strahl, der beim Auftreffen auf ein Objekt Schaden anrichtet.", "Forschungslabor:1<br/>Energietechnik:2"));
		em.persist(new Buildable("Ionentechnik", 1, 500, 150, 50, 0, 2, 0, "Durch die Konzentration von Ionen können Geschütze gebaut werden, die beträchtlichen Schaden anrichten können und die Abrisskosten von Gebäuden pro Stufe um 4% verringern.", "Forschungslabor:4<br/>Energietechnik:2<br/>Lastertechnik:5"));
		em.persist(new Buildable("Hyperraumtechnik", 1, 0, 2000, 1000, 0, 2, 0, "Durch die Einbindung der vierten und fünften Dimension ist es nun möglich, einen neuartigen Antrieb zu erforschen, der sparsamer und leistungsfähiger ist.", "Forschungslabor:7<br/>Lasertechnik:5<br/>Schildtechnik:5"));
		em.persist(new Buildable("Plasmatechnik", 1, 1000, 2000, 500, 0, 2, 0, "Eine Weiterentwicklung der Ionentechnik, die hochenergetisches Plasma beschleunigt, das verheerende Schäden anrichten kann, und die Produktion von Metall, Kristall und Deuterium (1%/0,66%/0,33% pro Stufe) optimiert.", "Forschungslabor:4<br/>Energietechnik:8<br/>LAsertechnik:10<br/>Ionentechnik:5"));
		em.persist(new Buildable("Verbrennungstriebwerk", 1, 200, 0, 300, 0, 2, 0, "Die Weiterentwicklung dieser Triebwerke macht einige Schiffe schneller. Jede Stufe steigert die Geschwindigkeit um 10% des Grundwertes.", "Forschungslabor:1<br/>Energietechnik:1"));
		em.persist(new Buildable("Impulstriebwerk", 1, 1000, 2000, 300, 0, 2, 0, "Das Impulstriebwerk basiert auf dem Rückstoßprinzip. Die Weiterentwicklung dieser Triebwerke steigert die Geschwindigkeit einiger Schiffe um 20% des Grundwertes.", "Forschungslabor:2<br/>Energietechnik:2"));
		em.persist(new Buildable("Hyperraumantrieb", 1, 5000, 10000, 3000, 0, 2, 0, "Die Hyperraumtechnologie ist die gezielte Krümmung des Raums durch die Einbindung der vierten und fünften Dimension. Jede Stufe dieser Technologie beschleunigt deine Schiffe um 30% des Grundwertes.", "Forschungslabor:7<br/>Energietechnik:5<br/>Hyperraumtechnik:3<br/>Schildtechnik:6"));
		em.persist(new Buildable("Spionagetechnik", 1, 100, 500, 100, 0, 2, 0, "Die Beherrschung der unterschiedlichen Arten von Energie ist für viele neue Technologien notwendig.", "Forschungslabor:3"));
		em.persist(new Buildable("Computertechnik", 1, 0, 200, 300, 0, 2, 0, "Mit der Erhöhung der Computerkapazitäten lassen sich immer mehr Flotten befehligen. Jede Stufe Computertechnik erhöht dabei die maximale Flottenanzahl um eins.", "Forschungslabor:1"));
		em.persist(new Buildable("Astrophysik", 1, 0, 100, 100, 100, 2.75, 0, "Schiffe mit einem Forschungsmodul können weite Expeditionen unternehmen. Für zwei neue Stufen dieser Technologie kann ein weiterer Planet kolonisiert werden.", "Forschungslabor:3<br/>Energietechnik:1<br/>Impulsantrieb:3<br/>Spionagetechnik:4"));
		em.persist(new Buildable("Gravitonforschung", 1, 0, 0, 0, 100000, 0, 3, "Durch den Abschuss einer konzentrierten Ladung von Gravitonpartikeln kann ein künstliches Gravitationsfeld errichtet werden. Es hat das Potenzial, sogar große Schiffe oder ganze Monde zu vernichten.", "Forschungslabor:12"));
		em.persist(new Buildable("Waffentechnik", 1, 400, 100, 0, 0, 2, 0, "Die Waffentechnik steigert die Leistung sämtlicher Waffensysteme, so dass die Schusskraft jeder Einheit pro Stufe auf 10% über den Grundwert ansteigt.", "Forschungslabor:4"));
		em.persist(new Buildable("Schildtechnik", 1, 100, 300, 0, 0, 2, 0, "Schildtechnik macht die Schilde der Schiffe und Verteidigungsanlagen effizienter. Jede Stufe steigert die Effizienz um 10% des Grundwertes.", "Forschungslabor:6<br/>Energietechnik:3"));
		em.persist(new Buildable("Raumschiffpanzerung", 1, 0, 400, 200, 0, 2, 0, "Spezielle Legierungen verbessern die Panzerung der Raumschiffe stetig. Die Wirksamkeit der Panzerung kann so pro Stufe um 10% gesteigert werden.", "Forschungslabor:2"));
		em.persist(new Buildable("Leichter Jäger", 2, 3000, 1000, 0, 0, 1, 0, "Der leichte Jäger ist ein wendiges Schiff, das man in dieser Form auf fast jedem Planeten vorfinden kann. Er ist zwar günstig, in seinen Fähigkeiten allerdings auch recht begrenzt.", "Schiffswerft:1<br/>Verbrennungstriebwerk:1"));
		em.persist(new Buildable("Schwerer Jäger", 2, 6000, 4000, 0, 0, 1, 0, "Diese Weiterentwicklung des leichten Jägers ist besser gepanzert und hat eine höhere Angriffsstärke.", "Schiffswerft:3<br/>Raumschiffpanzerung:2<br/>Impulsantrieb:2"));
		em.persist(new Buildable("Kreuzer", 2, 20000, 7000, 2000, 0, 1, 0, "Kreuzer sind fast dreimal so stark gepanzert wie schwere Jäger und verfügen über mehr als die doppelte Schusskraft. Zudem sind sie sehr schnell.", "Schiffswerft:5<br/>Impulstriebwerk:4<br/>Ionentechnik:2"));
		em.persist(new Buildable("Schlachtschiff", 2, 45000, 15000, 0, 0, 1, 0, "Diese Weiterentwicklung des leichten Jägers ist besser gepanzert und hat eine höhere Angriffsstärke.", "Schiffswerft:7<br/>Hyperraumantrieb:4"));
		em.persist(new Buildable("Schlachtkreuzer", 2, 30000, 40000, 15000, 0, 1, 0, "Der Schlachtkreuzer ist auf das Abfangen feindlicher Flotten spezialisiert.", "Schiffswerft:8<br/>Hyperraumantrieb:5<br/>Hyperraumtechnik:5<br/>Lasertechnik:12"));
		em.persist(new Buildable("Bomber", 2, 50000, 25000, 15000, 0, 1, 0, "Der Bomber wurde speziell entwickelt, um die Verteidigung eines Planeten zu zerstören.", "Schiffswerft:8<br/>Impulsantrieb:6<br/>Plasmatechnik:5"));
		em.persist(new Buildable("Zerstörer", 2, 60000, 50000, 15000, 0, 1, 0, "Der Zerstörer ist der König unter den Kriegsschiffen.", "Schiffswerft:9<br/>Hyperraumantrieb:6<br/>Hyperraumtechnik:5"));
		em.persist(new Buildable("Todesstern", 2, 5000000, 4000000, 1000000, 0, 1, 0, "Die Zerstörungskraft des Todessterns ist unübertroffen.", "Schiffswerft:12<br/>Hyperraumantrieb:7<br/>Hyperraumtechnik:6<br/>Gravitonforschung:1"));
		em.persist(new Buildable("Kleiner Transporter", 2, 2000, 2000, 0, 0, 1, 0, "Der kleine Transporter ist ein wendiges Schiff, welches Rohstoffe schnell zu anderen Planeten transportieren kann.", "Schiffswerft:2<br/>Verbrennungstriebwerk:2"));
		em.persist(new Buildable("Großer Transporter", 2, 6000, 6000, 0, 0, 1, 0, "Die Weiterentwicklung des kleinen Transporters hat ein größeres Ladevermögen und ist dank seines verbesserten Antriebs auch schneller.", "Schiffswerft:4<br/>Verbrennungstriebwerk:6"));
		em.persist(new Buildable("Kolonieschiff", 2, 10000, 20000, 10000, 0, 1, 0, "Mit diesem Schiff können fremde Planeten kolonisiert werden.", "Schiffswerft:4<br/>Impulsantrieb:3"));
		em.persist(new Buildable("Recycler", 2, 10000, 6000, 2000, 0, 1, 0, "Mit dem Recycler lassen sich Rohstoffe aus Trümmerfeldern gewinnen.", "Schiffswerft:4<br/>Verbrennungstriebwerk:6<br/>Schildtechnik:2"));
		em.persist(new Buildable("Spionagesonde", 2, 0, 1000, 0, 0, 1, 0, "Spionagesonden sind kleine wendige Drohnen, die über weite Entfernungen Daten zu Flotten und Planeten liefern.", "Schiffswerft:3<br/>Verbrennungstriebwerk:3<br/>Spionagetechnik:2"));
		em.persist(new Buildable("Solarsatellit", 2, 0, 2000, 500, 0, 1, 0, "Solarsatelliten sind einfache Plattformen aus Solarzellen, die sich in einem hohen stationären Orbit befinden. Sie sammeln das Sonnenlicht und geben es per Laser an die Bodenstation weiter. Ein Solarsatellit erzeugt auf diesem Planeten 32 Energie.", "Schiffswerft:1"));
		em.persist(new Buildable("Raketenwerfer", 2, 2000, 0, 0, 0, 1, 0, "Der Raketenwerfer ist eine einfache und kostengünstige Verteidigungsmöglichkeit.", "Schiffswerft:1"));
		em.persist(new Buildable("Leichtes Lasergeschütz", 2, 1500, 500, 0, 0, 1, 0, "Durch den konzentrierten Beschuss eines Ziels mit Photonen kann wesentlich größerer Schaden erzielt werden als mit gewöhnlichen ballistischen Waffen.", "Schiffswerft:2<br/>Energytechnik:1<br/>Lasertechnik:3"));
		em.persist(new Buildable("Schweres Lasergeschütz", 2, 6000, 4000, 0, 0, 1, 0, "Der schwere Laser ist eine konsequente Weiterentwicklung des leichten Lasers.", "Schiffswerft:4<br/>Energytechnik:3<br/>Lasertechnik:6"));
		em.persist(new Buildable("Gaußkanone", 2, 20000, 15000, 2000, 0, 1, 0, "Die Gaußkanone beschleunigt tonnenschwere Geschosse unter gigantischem Energieaufwand.", "Schiffswerft:6<br/>Energytechnik:6<br/>Waffentechnik:3<br/>Schildtechnik:1"));
		em.persist(new Buildable("Ionengeschütz", 2, 5000, 3000, 0, 0, 1, 0, "Das Ionengeschütz schleudert eine Welle von Ionen, die ihrem Ziel erheblichen Schaden zufügt.", "Schiffswerft:4<br/>Ionentechnik:4<br/>"));
		em.persist(new Buildable("Plasmawerfer", 2, 50000, 50000, 30000, 0, 1, 0, "Plasmageschütze setzen die Kraft einer Sonneneruption frei und übertreffen in ihrer vernichtenden Wirkung sogar den Zerstörer.", "Schiffswerft:8<br/>Plasmatechnik:7"));
		em.persist(new Buildable("Kleine Schildkuppel", 2, 10000, 10000, 0, 0, 1, 0, "Die kleine Schildkuppel umhüllt den ganzen Planeten mit einem Feld, das ungeheure Energiemengen absorbieren kann.", "Schiffswerft:1<br/>Schildtechnik:2"));
		em.persist(new Buildable("Große Schildkuppel", 2, 50000, 50000, 0, 0, 1, 0, "Die Weiterentwicklung der kleinen Schildkuppel kann wesentlich mehr Energie einsetzen, um Angriffe abzuwehren.", "Schiffswerft:6<br/>Schildtechnik:6"));
		em.persist(new Buildable("Abfangrakete", 2, 8000, 2000, 0, 0, 1, 0, "Abfangraketen zerstören angreifende Interplanetarraketen.", "Schiffswerft:1<br/>Raketensilo:2"));
		em.persist(new Buildable("Interplanetarrakete", 2, 12500, 2500, 10000, 0, 1, 0, "Interplanetarraketen zerstören die gegnerische Verteidigung.", "Schiffswerft:1<br/>Raketensilo:4<br/>Impulsantrieb:1"));

		em.persist(new Ship(31,400,	10,50,50,12500,20,21,new int[] {1,1,6,1,1,1,1,200,1,1,1,1,1,1}));
		em.persist(new Ship(32,1000,25,150,100,10000,75,22,new int[] {1,1,1,1,4,1,1,100,1,1,1,1,1,1}));
		em.persist(new Ship(33,2700,50,400,800,15000,300,22,new int[] {1,1,1,1,4,1,1,33,1,1,1,1,1,1}));
		em.persist(new Ship(34,6000,200,1000,1500,10000,500,23, new int[] {1,1,1,7,1,1,1,30,1,1,1,1,1,1}));
		em.persist(new Ship(35,7000,400,700,750,10000,250,23,new int[] {1,1,1,1,1,1,2,15,1,1,1,1,1,1}));
		em.persist(new Ship(36,7500,500,1000,500,9000,1000,23,new int[] {1,1,1,1,1,1,1,25,1,1,1,1,1,1}));
		em.persist(new Ship(37,11000,500,2000,2000,5000,1000,23,new int[] {1,1,1,1,1,1,1,5,1,1,1,1,1,1}));
		em.persist(new Ship(38,900000,50000,200000,1000000,100,1,23,new int[] {1,1,1,1,1,1,1,1,1,1,1,1,1,1}));
		em.persist(new Ship(39,400,10,5,5000,12500,20,22,new int[] {1,3,1,1,3,1,1,250,1,1,1,1,1,1}));
		em.persist(new Ship(40,1200,250,5,25000,7500,50,21,new int[] {1,1,1,3,1,1,1,250,1,1,1,1,1,1}));
		em.persist(new Ship(41,3000,100,50,7500,2500,1000,22,new int[] {1,1,1,1,1,1,1,250,1,1,1,1,1,1}));
		em.persist(new Ship(42,1600,10,1,20000,2000,1000,21,new int[] {1,1,1,1,1,1,1,250,1,1,1,1,1,1}));
		em.persist(new Ship(43,100,1,1,5,100000000,1,21,new int[] {5,5,5,5,5,5,5,1250,5,5,5,5,1,1}));
		em.persist(new Ship(44,200,1,1,0,0,0,21,new int[] {5,5,5,5,5,5,5,1250,5,5,5,5,1,1}));
		
		User noober = new User("noober","admin@admin.de","noober", IsActive.TRUE, AuthLvl.USER);
		User casual = new User("casual","admin@admin.de","casual", IsActive.TRUE, AuthLvl.USER);
		User pro = new User("pro","admin@admin.de","pro", IsActive.TRUE, AuthLvl.USER);
		User toxic = new User("toxic","admin@admin.de","toxic", IsActive.TRUE, AuthLvl.RESTRICTED);
		User badGuy = new User("bad","admin@admin.de","admin", IsActive.TRUE, AuthLvl.BANNED);
		User admin = new User("admin","admin@admin.de","admin", IsActive.TRUE, AuthLvl.SGA);
		
		em.persist(noober);
		em.persist(casual);
		em.persist(pro);
		em.persist(toxic);
		em.persist(badGuy);
		em.persist(admin);
				
		Planets_General noober_pg = new Planets_General(1,1,4, null, 0, 0, 0, 193, 0, 50, 1000, 500, 0, 0, 0, "Hey what a great game",noober.getUserID());
		Planets_General casual_pg = new Planets_General(1,1,5, null, 0, 0, 0, 193, 0, 50, 6000000, 4000000, 2500000, 0, 0, "Endor",casual.getUserID());
		Planets_General pro_pg = new Planets_General(1,1,9, null, 0, 0, 0, 193, 0, 50, 10000000, 5000000, 5000000, 0, 0, "Draktar",pro.getUserID());
		Planets_General toxic_pg = new Planets_General(1,1,10, null, 0, 0, 0, 193, 0, 50, 1000, 500, 0, 0, 0, "get on my level",toxic.getUserID());
		Planets_General badGuy_pg = new Planets_General(1,1,12, null, 0, 0, 0, 193, 0, 50, 1000, 500, 0, 0, 0, "exploits are a feature",badGuy.getUserID());
		Planets_General admin_pg = new Planets_General( 1, 1, 7, null, 0, 0, 0, 193, 0, 500, 200, 200, 0, 0, 0, "Heimatplanet", admin.getUserID());

		em.persist(noober_pg);
		em.persist(casual_pg);
		em.persist(pro_pg);
		em.persist(toxic_pg);
		em.persist(badGuy_pg);
		
		Planets_Buildings noober_pb = new Planets_Buildings(noober_pg.getPlanetId(), 8, 6, 5, 12, 0, 10, 10, 10, 0, 0, 0, 5, 5, 3, 0, 0, 0, 0, 0, 0, 0, 0);
		Planets_Def noober_pd = new Planets_Def(noober_pg.getPlanetId(), 20, 10, 0, 0, 0, 0, 0, 0, 0, 0);
		Planets_Research noober_pr = new Planets_Research(noober_pg.getPlanetId(), 5, 5, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 2, 2);
		Planets_Ships noober_ps = new Planets_Ships(noober_pg.getPlanetId(), 50, 20, 10, 0, 0, 0, 0, 0, 10, 0, 0, 0, 10, 0);
		
		em.persist(noober_pb);
		em.persist(noober_pd);
		em.persist(noober_pr);
		em.persist(noober_ps);
		
		Planets_Buildings casual_pb = new Planets_Buildings(casual_pg.getPlanetId(), 23, 20, 18, 24, 0, 20, 20, 20, 0, 0, 0, 10, 12, 12, 0, 0, 0, 0, 0, 0, 0, 0);
		Planets_Def casual_pd = new Planets_Def(casual_pg.getPlanetId(), 500, 250, 100, 50, 0, 0, 1, 1, 0, 0);
		Planets_Research casual_pr = new Planets_Research(casual_pg.getPlanetId(), 10, 12, 10, 10, 10, 10, 10, 10, 12, 10, 10, 0, 12, 12, 12);
		Planets_Ships casual_ps = new Planets_Ships(casual_pg.getPlanetId(), 500, 500, 300, 200, 50, 0, 50, 0, 50, 100, 0, 0, 200, 0);
		
		em.persist(casual_pb);
		em.persist(casual_pd);
		em.persist(casual_pr);
		em.persist(casual_ps);
		
		Planets_Buildings pro_pb = new Planets_Buildings(pro_pg.getPlanetId(), 50, 36, 50, 90, 0, 40, 40, 40, 0, 0, 0, 20, 20, 20, 0, 0, 10, 0, 0, 0, 0, 0);
		Planets_Def pro_pd = new Planets_Def(pro_pg.getPlanetId(), 10000, 10000, 5000, 500, 0, 500, 1, 1, 0, 0);
		Planets_Research pro_pr = new Planets_Research(pro_pg.getPlanetId(), 10, 12, 10, 10, 10, 10, 10, 10, 12, 10, 10, 0, 24, 24, 24);
		Planets_Ships pro_ps = new Planets_Ships(pro_pg.getPlanetId(), 5000, 5000, 3000, 2000, 500, 1000, 500, 10, 500, 1000, 0, 0, 1000, 0);
		
		em.persist(pro_pb);
		em.persist(pro_pd);
		em.persist(pro_pr);
		em.persist(pro_ps);
		
		Planets_Buildings toxic_pb = new Planets_Buildings(toxic_pg.getPlanetId(), 8, 6, 5, 12, 0, 10, 10, 10, 0, 0, 0, 5, 5, 3, 0, 0, 0, 0, 0, 0, 0, 0);
		Planets_Def toxic_pd = new Planets_Def(toxic_pg.getPlanetId(), 20, 10, 0, 0, 0, 0, 0, 0, 0, 0);
		Planets_Research toxic_pr = new Planets_Research(toxic_pg.getPlanetId(), 5, 5, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 2, 2);
		Planets_Ships toxic_ps = new Planets_Ships(toxic_pg.getPlanetId(), 50, 20, 10, 0, 0, 0, 0, 0, 10, 0, 0, 0, 10, 0);
		
		em.persist(toxic_pb);
		em.persist(toxic_pd);
		em.persist(toxic_pr);
		em.persist(toxic_ps);
		
		Planets_Buildings badGuy_pb = new Planets_Buildings(badGuy_pg.getPlanetId(), 8, 6, 5, 12, 0, 10, 10, 10, 0, 0, 0, 5, 5, 3, 0, 0, 0, 0, 0, 0, 0, 0);
		Planets_Def badGuy_pd = new Planets_Def(badGuy_pg.getPlanetId(), 20, 10, 0, 0, 0, 0, 0, 0, 0, 0);
		Planets_Research badGuy_pr = new Planets_Research(badGuy_pg.getPlanetId(), 5, 5, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 2, 2);
		Planets_Ships badGuy_ps = new Planets_Ships(badGuy_pg.getPlanetId(), 50, 20, 10, 0, 0, 0, 0, 0, 10, 0, 0, 0, 10, 0);
		
		em.persist(badGuy_pb);
		em.persist(badGuy_pd);
		em.persist(badGuy_pr);
		em.persist(badGuy_ps);

		Planets_Buildings admin_pb = new Planets_Buildings(admin_pg.getPlanetId(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		Planets_Def admin_pd = new Planets_Def(admin_pg.getPlanetId(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		Planets_Research admin_pr = new Planets_Research(admin_pg.getPlanetId(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		Planets_Ships admin_ps = new Planets_Ships(admin_pg.getPlanetId(), 50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

		em.persist(admin_pb);
		em.persist(admin_pd);
		em.persist(admin_pr);
		em.persist(admin_ps);
		
		genTestMsg(admin);
		
		//planetHandler.createNewPlanet(1);
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
			buildHandler.setUserId(user.getUserID());
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
			messageHandler.setUser(user);
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
			em.persist(user);
			try {
				utx.commit();
			} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
					| HeuristicRollbackException | SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			planetHandler.createNewPlanet(user.getUserID());
			return "login";
		} else {
			return "login";
		}

	}

	public boolean isAdmin() {
		Query query = em.createQuery("select k from User k where k.username = :username and k.password = :password ");
		query.setParameter("username", username);
		query.setParameter("password", password);
		User  user = (User) query.getSingleResult();
		handler.setUser(user);
		if(handler.getUser().getAuthLvl() == AuthLvl.SGA) {
			return true;
		} else {
			return false;
		}
	}
	
	public void isBanned() {
		FacesContext context = FacesContext.getCurrentInstance();
		if(handler.getUser().getAuthLvl() == AuthLvl.BANNED) {
			context.getApplication().getNavigationHandler().handleNavigation(context, null, "/login.xhtml?faces-redirect=true");
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
		Planets_General pg = planetHandler.getPg();
		try {
			utx.begin();
		} catch (NotSupportedException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		em.merge(pg);
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

	public RoleHandler getRoleHandler() {
		return roleHandler;
	}

	public void setRoleHandler(RoleHandler roleHandler) {
		this.roleHandler = roleHandler;
	}

	public SettingsHandler getSettingsHandler() {
		return settingsHandler;
	}

	public void setSettingsHandler(SettingsHandler settingsHandler) {
		this.settingsHandler = settingsHandler;
	}

	public MessageHandler getMessageHandler() {
		return messageHandler;
	}

	public void setMessageHandler(MessageHandler messageHandler) {
		this.messageHandler = messageHandler;
	}

	public FleetHandler getFleetHandler() {
		return fleetHandler;
	}

	public void setFleetHandler(FleetHandler fleetHandler) {
		this.fleetHandler = fleetHandler;
	}
}