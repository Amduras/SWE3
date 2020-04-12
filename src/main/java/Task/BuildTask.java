package Task;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import controller.BuildHandler;
import controller.PlanetHandler;
import controller.QHandler;
import planets.Planets_Buildings;
import planets.Planets_Def;
import planets.Planets_Research;
import planets.Planets_Ships;

public class BuildTask implements Task, Serializable{
	
	private EntityManager em;
	
	private UserTransaction utx;
	
	private static final long serialVersionUID = 1L;
	private int type;
	private Date time;
	private int upgradeId;
	private int planet;
	private PlanetHandler planetHandler;
	private BuildHandler buildHandler;
	
	public BuildTask(int type, Date time, int upgradeId, int planet, EntityManager em, UserTransaction utx, PlanetHandler p, BuildHandler buildHandler) {
		this.type = type;
		this.time = time;
		this.upgradeId = upgradeId;
		this.planet = planet;
		this.em = em;
		this.utx = utx;
		this.planetHandler = p;
		this.buildHandler = buildHandler;
		/** Add to queue for schedule **/
		QHandler.queued.add(this);
	}
	
	public int getUpgradeId() {
		return upgradeId;
	}

	public void setUpgradeId(int upgradeId) {
		this.upgradeId = upgradeId;
	}

	@Override
	public int getType() {
		return type;
	}

	@Override
	public Date getTime() {
		return time;
	}

	@Override
	public void executeTask() {
		System.out.println("---------------------------> EX BT " + type);
		if(type == 0) {
			Query query = em.createQuery("select k from Planets_Buildings k where k.planetId = :id");
			query.setParameter("id", planet);
			try {
				Object res = query.getSingleResult();
				Planets_Buildings b = (Planets_Buildings)res;
				idToFieldB(b,upgradeId);
				b.setTask(-1);
				try {
					utx.begin();
				} catch (NotSupportedException | SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				em.merge(b);
				try {
					utx.commit();
				} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
						| HeuristicRollbackException | SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}catch(NoResultException e){	
				System.out.println("Keine Werte in DB");
			}	
		}
		else if(type == 1) {
			Query query = em.createQuery("select k from Planets_Research k where k.planetId = :id");
			query.setParameter("id", planet);
			try {
				Object res = query.getSingleResult();
				Planets_Research b = (Planets_Research)res;
				idToFieldR(b,upgradeId);
				b.setTask(-1);
				try {
					utx.begin();
				} catch (NotSupportedException | SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				em.merge(b);
				try {
					utx.commit();
				} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
						| HeuristicRollbackException | SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}catch(NoResultException e){	
				System.out.println("Keine Werte in DB");
			}	
		}
		else if(type == 2 && upgradeId < 45) {
			Query query = em.createQuery("select k from Planets_Ships k where k.planetId = :id");
			query.setParameter("id", planet);
			try {
				Object res = query.getSingleResult();
				Planets_Ships b = (Planets_Ships)res;
				idToFieldS(b,upgradeId);
				b.setTask(-1);
//				b.removeTask(time);
				try {
					utx.begin();
				} catch (NotSupportedException | SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				em.merge(b);
				try {
					utx.commit();
				} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
						| HeuristicRollbackException | SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}catch(NoResultException e){	
				System.out.println("Keine Werte in DB");
			}	
		}
		else {
			Query query = em.createQuery("select k from Planets_Def k where k.planetId = :id");
			query.setParameter("id", planet);
			try {
				Object res = query.getSingleResult();
				Planets_Def b = (Planets_Def)res;
				idToFieldD(b,upgradeId);
				b.setTask(-1);
//				b.removeTask(time);
				try {
					utx.begin();
				} catch (NotSupportedException | SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				em.merge(b);
				try {
					utx.commit();
				} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
						| HeuristicRollbackException | SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}catch(NoResultException e){	
				System.out.println("Keine Werte in DB");
			}
		}
		planetHandler.updateDataset();
		planetHandler.save();
		buildHandler.setBuildDone("t");
	}
	
	private void idToFieldB(Planets_Buildings b, int id) {
		switch(id) {
			case 1:
				b.setMetalMine(b.getMetalMine()+1);
				break;
			case 2:
				b.setCrystalMine(b.getCrystalMine()+1);
				break;
			case 3:
				b.setDeutSyn(b.getDeutSyn()+1);
				break;
			case 4:
				b.setSolarPlant(b.getSolarPlant()+1);
				break;
			case 5:
				b.setFusionReactor(b.getFusionReactor()+1);
				break;
			case 6:
				b.setMetalStorage(b.getMetalStorage()+1);
				break;
			case 7:
				b.setCrystalStorage(b.getCrystalStorage()+1);
				break;
			case 8:
				b.setDeutTank(b.getDeutTank()+1);
				break;
			case 9:
				b.setRoboticFactory(b.getRoboticFactory()+1);
				break;
			case 10:
				b.setShipyard(b.getShipyard()+1);
				break;
			case 11:
				b.setResearchlab(b.getResearchlab()+1);
				break;
			case 12:
				b.setAlliancedepot(b.getAlliancedepot()+1);
				break;
			case 13:
				b.setMissleSilo(b.getMissleSilo()+1);
				break;
			case 14:
				b.setNaniteFactory(b.getNaniteFactory()+1);
				break;
			case 15:
				b.setTerraformer(b.getTerraformer());
				break;
		}
	}
	private void idToFieldR(Planets_Research b, int id) {
		switch(id) {
			case 16:
				b.setEnergy(b.getEnergy()+1);
				break;
			case 17:
				b.setLaser(b.getLaser()+1);
				break;
			case 18:
				b.setIon(b.getIon());
				break;
			case 19:
				b.setHyperspacetech(b.getHyperspacetech()+1);
				break;
			case 20:
				b.setPlasma(b.getPlasma()+1);
				break;
			case 21:
				b.setCombustion(b.getCombustion()+1);
				break;
			case 22:
				b.setImpulse(b.getImpulse()+1);
				break;
			case 23:
				b.setHyperspace(b.getHyperspace()+1);
				break;
			case 24:
				b.setEspionage(b.getEspionage()+1);
				break;
			case 25:
				b.setComputer(b.getComputer()+1);
				break;
			case 26:
				b.setAstrophysics(b.getAstrophysics()+1);
				break;
			case 27:
				b.setGravitation(b.getGravitation()+1);
				break;
			case 28:
				b.setWeapon(b.getWeapon()+1);
				break;
			case 29:
				b.setShield(b.getShield()+1);;
				break;
			case 30:
				b.setArmor(b.getArmor()+1);;
				break;
		}
	}
	private void idToFieldS(Planets_Ships b, int id) {
		switch(id) {
			case 31:
				b.setLightFighter(b.getLightFighter()+1);
				break;
			case 32:
				b.setHeavyFighter(b.getHeavyFighter()+1);
				break;
			case 33:
				b.setCruiser(b.getCruiser()+1);
				break;
			case 34:
				b.setBattleship(b.getBattleship()+1);
				break;
			case 35:
				b.setBattlecruiser(b.getBattlecruiser()+1);
				break;
			case 36:
				b.setBomber(b.getBomber()+1);
				break;
			case 37:
				b.setDestroyer(b.getDestroyer()+1);
				break;
			case 38:
				b.setDeathStar(b.getDeathStar()+1);
				break;
			case 39:
				b.setSmallCargoShip(b.getSmallCargoShip()+1);
				break;
			case 40:
				b.setLargeCargoShip(b.getLargeCargoShip()+1);
				break;
			case 41:
				b.setColonyShip(b.getColonyShip()+1);
				break;
			case 42:
				b.setRecycler(b.getRecycler()+1);
				break;
			case 43:
				b.setEspionageProbe(b.getEspionageProbe()+1);
				break;
			case 44:
				b.setSolarSattlelite(b.getSolarSattlelite()+1);
				break;
		}
	}
	private void idToFieldD(Planets_Def b, int id) {
		switch(id) {
			case 45:
				b.setRocketLauncher(b.getRocketLauncher()+1);
				break;
			case 46:				
				b.setLightLaser(b.getLightLaser()+1);
				break;
			case 47:
				b.setHeavyLaser(b.getHeavyLaser()+1);
				break;
			case 48:
				b.setGaussCannon(b.getGaussCannon()+1);
				break;
			case 49:
				b.setIonCannon(b.getIonCannon()+1);
				break;
			case 50:
				b.setPlasmaTurret(b.getPlasmaTurret()+1);
				break;
			case 51:
				b.setSmallShieldDome(1);
				break;
			case 52:
				b.setLargeShieldDome(1);
				break;
			case 53:
				b.setAntiBallisticMissle(b.getAntiBallisticMissle()+1);
				break;
			case 54:
				b.setInterplanetaryMissle(b.getInterplanetaryMissle()+1);
				break;
		}
	}
	@Override
	public void saveToDB(int id) {
		if(type == 0) {
			planetHandler.getPb().setTask(id);
		}
		else if(type == 1) 
			planetHandler.getPr().setTask(id);
		else if(type == 2 && upgradeId < 45)
			planetHandler.getPs().setTask(id);
		else
			planetHandler.getPd().setTask(id);
		try {
			utx.begin();
		} catch (NotSupportedException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(type == 0)
			em.merge(planetHandler.getPb());
		else if(type == 1)
			em.merge(planetHandler.getPr());
		else if(type == 2 && upgradeId < 45)
			em.merge(planetHandler.getPs());
		else
			em.merge(planetHandler.getPd());
		
		try {
			utx.commit();
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
				| HeuristicRollbackException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
}
