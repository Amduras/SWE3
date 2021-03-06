package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import controller.FleetHandler;
import planets.Planets_Buildings;
import planets.Planets_Def;
import planets.Planets_General;
import planets.Planets_Research;
import planets.Planets_Ships;

public class Fight {

	private EntityManager em;
	private UserTransaction utx;
	
	@SuppressWarnings("unused")
	private int attPlanetId;
	private int[] attTShips;
	private int[][] attShips = new int[13][6];
	private long[] booty;
	private Planets_General att_pg;
	private Planets_Research att_pr;
	
	private int defPlanetId;
	private int[][] defShips = new int[22][6];
	private Planets_General def_pg;
	private Planets_Buildings def_pb;
	private Planets_Research def_pr;
	private Planets_Ships def_ps;
	private Planets_Def def_pd;
	private FleetHandler fleetHandler;
	
	private List<Ship> shipData;
	
	public Fight(int attPlanetId, int[] attTShips, long[] booty, int defPlanetId, EntityManager em, UserTransaction utx, FleetHandler fleetHandler) {
		this.attPlanetId = attPlanetId;
		this.attTShips = attTShips;
		this.booty = booty;
		this.defPlanetId = defPlanetId;
		this.em = em;
		this.utx = utx;
		this.fleetHandler = fleetHandler;
		
		att_pg = getPg(attPlanetId);
		att_pr = getPr(attPlanetId);
		
		def_pg = getPg(defPlanetId);
		def_pr = getPr(defPlanetId);
		def_ps = getPs(defPlanetId);
		def_pd = getPd(defPlanetId);
		
		shipData = getShipData();
		initAttArray(attTShips);
		initDefArray();
		
		beginFight();
	}
	
	private void beginFight() {		
		//System.out.println("Kampf begint ------------------------------------------");
		for(int r=0;r<6;++r) {
			//System.out.println("Runde "+r+" startet------------------------------------------ \n Lade Schilde");
			//charge Shields
			chargeShields();
			
			//abwechselndes feuern, verteidiger hat ersten schuss
			for(int i=0;i<defShips.length;++i) {
				//def shot
				int dmg = (defShips[i][0]-defShips[i][5]) * defShips[i][3];
				if(dmg != 0) {
					int target = findTargetFromAtt();
					System.out.println("Def-Schiff target "+ target);
					if (target != -1) {
						int shots = RapidFire(i,target);
						
						int hulldmg = dmg*shots-attShips[target][4];
						if(hulldmg > 0)
							attShips[target][4] = 0;
						else
							attShips[target][4] -= dmg*shots;
						int losses = hulldmg / attShips[target][1];
						//System.out.println("Def-Schiff "+(i+31)+" schießt auf "+(target+31)+" mit "+shots+" Schuss für "+hulldmg+" Hüllenschaden ,"+losses+" pot. Verluste.");
						attShips[target][5] += losses;
						attShips[target][5] = Math.min(attShips[target][0], attShips[target][5]);
					}
					else {
						endFight(1,r);
						return;
					}						
				}
				//att shot
				if(i<attShips.length) {
					dmg = (attShips[i][0]-attShips[i][5]) * attShips[i][3];
					if(dmg != 0) {
						int target = findTargetFromDef();
						System.out.println("Att-Schiff target "+ target);
						if (target != -1) {
							int shots = RapidFire(i,target);
							
							int hulldmg = dmg*shots-defShips[target][4];
							if(hulldmg > 0)
								defShips[target][4] = 0;
							else
								defShips[target][4] -= dmg*shots;
							int losses = hulldmg / defShips[target][1];
							//System.out.println("Att-Schiff "+(i+31)+" schießt auf "+(target+31)+" mit "+shots+" Schuss für "+hulldmg+" Hüllenschaden ,"+losses+" Verluste.");
							defShips[target][5] += losses;
							defShips[target][5] = Math.min(defShips[target][0], defShips[target][5]);
						}
						else {
							endFight(0,r);
							return;
						}
					}
				}			
			}			
		}
		endFight(1,6);
	}
	private void endFight(int i, int round) {
		//System.out.println("Kampf ist vorbei, Runde: "+round+", Gewinner: "+i);
		calcAndApplyDebrisfield();	
		if(i == 0) { // Vicory for att
			def_pb = getPb(defPlanetId);
			resDiff();
			
			int cargoSpace = getCargoSpace();
			long maxMetal = (long)(def_pg.getMetal()/2);
			long maxCrystal = (long)(def_pg.getCrystal()/2);
			long maxDeut = (long)(def_pg.getDeut()/2);
			if((maxMetal+maxCrystal+maxDeut) <= cargoSpace) {
				booty[0] = maxMetal;
				booty[1] = maxCrystal;
				booty[2] = maxDeut;			
			}
			else {
				long min = Math.min(maxMetal, Math.min(maxCrystal, maxDeut));
				if(min*3 <= cargoSpace) {
					booty[0] = min;
					booty[1] = min;
					booty[2] = min;
				}
				else {
					booty[0] = cargoSpace/3;
					booty[1] = cargoSpace/3;
					booty[2] = cargoSpace/3;
				}
				while((booty[0]+booty[1]+booty[2])<cargoSpace) {
					booty[0] = booty[0] < maxMetal ? ++booty[0] : booty[0];
					booty[1] = booty[1] < maxCrystal ? ++booty[1] : booty[1];
					booty[2] = booty[2] < maxDeut ? ++booty[2] : booty[2];
				}		
			}
			def_pg.setMetal(def_pg.getMetal()-booty[0]);
			def_pg.setCrystal(def_pg.getCrystal()-booty[1]);
			def_pg.setDeut(def_pg.getDeut()-booty[2]);
			
			writeLog(true,true);
			writeLog(false,false);
		}
		else if(i == 1) { // Victory for def
			writeLog(false,true);
			writeLog(true,false);
		}
		else {// Draw
			writeLog(false,true);
			writeLog(false,false);
		}
		repairDef();
		applyBattleResults();		
		saveDefDataset();
	}
	private void writeLog(boolean hasWon, boolean forAtt) {
		String subject = "Angriff auf " +def_pg.getName();
		String fromUser = "Flottenadmiral";
		String toUser = "admin";
		
		int id = forAtt ? att_pg.getUserId() : def_pg.getUserId();
		//WHYYYYYYYYYYYYYYYYYY großes D????
		Query query = em.createQuery("select k from User k where k.userID = :userId");
		query.setParameter("userId", id);
		
		try {
			Object res = query.getSingleResult();
			User user = (User)res;
			toUser = user.getUsername();
		} catch(NoResultException e){	
			System.out.println("Keine user mit id "+id+" in DB - fight");
		}		
		
		String msg = hasWon ? "Wir haben gesiegt!</br>" : "Niederlage!</br>";
		msg += "Schlacht um "+def_pg.getName()+ " Am: " + new Date(System.currentTimeMillis()).toString() +"</br>"
				+"</br>"
				+ "<table id=\"buildings\">"
				+"<tr>"
				+"<td></td>"
				+"<td><img width=\"40\" height=\"40\" src=\"resources/images/res_icon/metal.png\" alt=\"Res\"></td>"
				+"<td><img width=\"40\" height=\"40\" src=\"resources/images/res_icon/crystal.png\" alt=\"Res\"></td>"
				+"<td><img width=\"40\" height=\"40\" src=\"resources/images/res_icon/deuterium.png\" alt=\"Res\"></td>"
				+"</tr>"
				+"<tr>"
				+"<td>Beute:</td>"
				+"<td>"+booty[0]+"</td>"
				+"<td>"+booty[1]+"</td>"
				+"<td>"+booty[2]+"</td>"
				+"</tr>"
				+"<tr>"
				+"<td>Trümmerfeld:</td>"
				+"<td>"+def_pg.getDebrisFieldMetal()+"</td>"
				+"<td>"+def_pg.getDebrisFieldCris()+"</td>"
				+"<td>"+def_pg.getDebrisFieldDeut()+"</td>"
				+"</tr>"
				+ "</table></br>"
				+"Angreifer:</br>"
				+"<table id=\"buildings\">"
				+"<tr>";
				for(int i=0;i<attShips.length;++i)
					msg += "<td><img width=\"42\" height=\"42\" src=\"resources/images/buildable/"+(i+31)+".jpg\" alt=\"Ship\"></td>";			
				msg +="</tr>"
				+"<tr>";
				for(int i=0;i<attShips.length;++i)
					msg += "<td>"+attShips[i][0]+"</td>";	
				msg +="</tr>"
				+"<tr>";
				for(int i=0;i<attShips.length;++i)
					msg += "<td>"+(-attShips[i][5])+"</td>";	
				msg +="</tr>"
				+"</table></br>"
				+"Verteidiger:</br>"
				+"<table id=\"buildings\">"
				+"<tr>";
				for(int i=0;i<14;++i)
					msg += "<td><img width=\"42\" height=\"42\" src=\"resources/images/buildable/"+(i+31)+".jpg\" alt=\"Ship\"></td>";			
				msg +="</tr>"
				+"<tr>";
				for(int i=0;i<14;++i)
					msg += "<td>"+defShips[i][0]+"</td>";		
				msg +="</tr>"
				+"<tr>";
				for(int i=0;i<14;++i)
					msg += "<td>"+(-defShips[i][5])+"</td>";
				msg +="</tr>"
				+"</table></br>"
				+"Verteidigungsanlagen:</br>"
				+"<table id=\"buildings\">"
				+"<tr>";
				for(int i=14;i<defShips.length;++i)
					msg += "<td><img width=\"42\" height=\"42\" src=\"resources/images/buildable/"+(i+31)+".jpg\" alt=\"Ship\"></td>";
				msg +="</tr>"
				+"<tr>";
				for(int i=14;i<defShips.length;++i)
					msg += "<td>"+defShips[i][0]+"</td>";
				msg +="</tr>"
				+"<tr>";
				for(int i=14;i<defShips.length;++i)
					msg += "<td>"+(-defShips[i][5])+"</td>";
				msg +="</tr>"
				+"</table></br>"
				+"60% der Verteidigungsanlagen wurden Repariert.";
		
		submitLog(fromUser, toUser, msg, subject);				
	}
	
	private void submitLog(String fromUser, String toUser, String msg, String subject) {
		Messages newMessage = new Messages(fromUser, toUser, msg, subject);
		try {
			utx.begin();
		} catch (NotSupportedException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		em.persist(newMessage);
		try {
			utx.commit();
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
				| HeuristicRollbackException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fleetHandler.setMissionDone("t");
	}
	private void calcAndApplyDebrisfield() {
		Query query = em.createQuery("select max(id) from WorldSettings k");
		int id =  (int)query.getSingleResult();
		query = em.createQuery("select k from WorldSettings k where k.id = :id");
		query.setParameter("id", id);
		try {
			Object res1 = query.getSingleResult();
			WorldSettings ws = (WorldSettings)res1;
			query = em.createQuery("select k from Buildable k where k.id > 30");
			@SuppressWarnings("unchecked")
			List<Buildable> res = query.getResultList();
			long metal = 0;
			long crystal = 0;
			long deut = 0;
			for(int i=0;i<defShips.length;++i) {
				metal += res.get(i).getBaseCostMetal() * defShips[i][5] * ws.getFleetToDebrisFieldRatio();
				crystal += res.get(i).getBaseCostCrystal() * defShips[i][5] * ws.getFleetToDebrisFieldRatio();
				deut += res.get(i).getBaseCostDeut() * defShips[i][5] * ws.getFleetToDebrisFieldRatio();
				if(i<attShips.length) {
					metal += res.get(i).getBaseCostMetal() * attShips[i][5] * ws.getFleetToDebrisFieldRatio();
					crystal += res.get(i).getBaseCostCrystal() * attShips[i][5] * ws.getFleetToDebrisFieldRatio();
					deut += res.get(i).getBaseCostDeut() * attShips[i][5] * ws.getFleetToDebrisFieldRatio();
				}			
			}
			def_pg.setDebrisFieldMetal(def_pg.getDebrisFieldMetal()+metal);
			def_pg.setDebrisFieldCris(def_pg.getDebrisFieldCris()+crystal);
			def_pg.setDebrisFieldDeut(def_pg.getDebrisFieldDeut()+deut);
		} catch(NoResultException e){	
			System.out.println("Keine WS in DB");
		}
	}
	private void applyBattleResults() {
		//att
		for(int i=0;i<attShips.length;++i)
			attTShips[i] = attShips[i][0]-Math.abs(attShips[i][5]);
		for(int i=0;i<defShips.length;++i)
			setById(i,defShips[i][0]-Math.abs(defShips[i][5]));
	}
	private void repairDef() {
		for(int i=14;i<defShips.length;++i) {
			defShips[i][5] *= 0.4;
		}
	}
	private int getCargoSpace() {
		int res = 0;
		for(int i=0;i<attShips.length;++i) {
			res += shipData.get(i).getCargoSpace()*(attShips[i][0]-attShips[i][5]);
		}
		return res;
	}
	private int RapidFire(int from, int to) {
		if(from<14) {
			int[] rf  = shipData.get(to).getRapidFire();
			int rfp = 100-(100/rf[from]);
			Random rng = new Random();
			int shots = 1;
			int chance = rng.nextInt(100);
			while(chance < rfp) {
				++shots;
				chance = rng.nextInt(100);
				if(shots > 1250)
					return shots;
			}
			return shots;
		}
		else {
			return 1;
		}
			
	}
	private int findTargetFromAtt() {
		List<Integer> tar = new ArrayList<Integer>();
		for(int i=0;i<attShips.length;++i) {
			if((attShips[i][0]-attShips[i][5]) > 0)
				tar.add(i);
		}
		if(tar.size() == 0)
			return -1;
		else {
			Random rng = new Random();
			return tar.get(rng.nextInt(tar.size()));
		}
	}
	private int findTargetFromDef() {
		List<Integer> tar = new ArrayList<Integer>();
		for(int i=0;i<defShips.length;++i)
			if((defShips[i][0]-defShips[i][5]) > 0)
				tar.add(i);	
		if(tar.size() == 0)
			return -1;						
		else {
			Random rng = new Random();
			return tar.get(rng.nextInt(tar.size()));
		}			
	}
	private void chargeShields() {
		for(int i=0;i<attShips.length;++i) {
			attShips[i][4] = (attShips[i][0]-attShips[i][5]) * attShips[i][2];
		}
		for(int i=0;i<defShips.length;++i) {
			defShips[i][4]= (defShips[i][0]-defShips[i][5]) * defShips[i][2];
		}
	}
	private void initAttArray(int[] attTShips) {
		for(int i=0;i<attShips.length;++i) {
			attShips[i][0] = attTShips[i];
			attShips[i][1] = shipData.get(i).getHull()  * (1 + att_pr.getArmor() / 10);
			attShips[i][2] = shipData.get(i).getShield() * (1 + att_pr.getShield() / 10);
			attShips[i][3] = shipData.get(i).getAttack()  * (1 + att_pr.getWeapon() / 10);
		}
	}
	private void initDefArray() {
		for(int i=0;i<defShips.length;++i) {
			defShips[i][0] = idToCount(i);
			defShips[i][1] = shipData.get(i).getHull()  * (1 + def_pr.getArmor() / 10);
			defShips[i][2] = shipData.get(i).getShield() * (1 + def_pr.getShield() / 10);
			defShips[i][3] = shipData.get(i).getAttack()  * (1 + def_pr.getWeapon() / 10);
		}
	}

	private Planets_General getPg(int id){
		Query query = em.createQuery("select k from Planets_General k where k.id = :id");
		query.setParameter("id", id);
		try {
			Object res = query.getSingleResult();
			Planets_General def_pg = (Planets_General)res;
			return def_pg;
		} catch(NoResultException e){	
			System.out.println("Keine def_pg in DB - fight");
		}
		return null;
	}
	private Planets_Buildings getPb(int id){
		Query query = em.createQuery("select k from Planets_Buildings k where k.id = :id");
		query.setParameter("id", id);
		try {
			Object res = query.getSingleResult();
			Planets_Buildings def_pg = (Planets_Buildings)res;
			return def_pg;
		} catch(NoResultException e){	
			System.out.println("Keine def_pb in DB - fight");
		}
		return null;
	}
	private Planets_Research getPr(int id){
		Query query = em.createQuery("select k from Planets_Research k where k.id = :id");
		query.setParameter("id", id);
		try {
			Object res = query.getSingleResult();
			Planets_Research def_pg = (Planets_Research)res;
			return def_pg;
		} catch(NoResultException e){	
			System.out.println("Keine def_pr in DB - fight");
		}
		return null;
	}
	private Planets_Ships getPs(int id){
		Query query = em.createQuery("select k from Planets_Ships k where k.id = :id");
		query.setParameter("id", id);
		try {
			Object res = query.getSingleResult();
			Planets_Ships def_pg = (Planets_Ships)res;
			return def_pg;
		} catch(NoResultException e){	
			System.out.println("Keine PS in DB - fight");
		}
		return null;
	}
	private Planets_Def getPd(int id){
		Query query = em.createQuery("select k from Planets_Def k where k.id = :id");
		query.setParameter("id", id);
		try {
			Object res = query.getSingleResult();
			Planets_Def def_pg = (Planets_Def)res;
			return def_pg;
		} catch(NoResultException e){	
			System.out.println("Keine PD in DB - fight");
		}
		return null;
	}
	private List<Ship> getShipData(){
		Query query = em.createQuery("select k from Ship k");
		@SuppressWarnings("unchecked")
		List<Ship> res = query.getResultList();
		return res;
	}
	private int idToCount(int id) {
		int res = 9999;
		switch(id+31) {
		case 31:
			res = def_ps.getLightFighter();
			break;
		case 32:
			res = def_ps.getHeavyFighter();
			break;
		case 33:
			res = def_ps.getCruiser();
			break;
		case 34:
			res = def_ps.getBattleship();
			break;
		case 35:
			res = def_ps.getBattlecruiser();
			break;
		case 36:
			res = def_ps.getBomber();
			break;
		case 37:
			res = def_ps.getDestroyer();
			break;
		case 38:
			res = def_ps.getDeathStar();
			break;
		case 39:
			res = def_ps.getSmallCargoShip();
			break;
		case 40:
			res = def_ps.getLargeCargoShip();
			break;
		case 41:
			res = def_ps.getColonyShip();
			break;
		case 42:
			res = def_ps.getRecycler();
			break;
		case 43:
			res = def_ps.getEspionageProbe();
			break;
		case 44:
			res = def_ps.getSolarSattlelite();
			break;
		case 45:
			res = def_pd.getRocketLauncher();
			break;
		case 46:
			res = def_pd.getLightLaser();
			break;
		case 47:
			res = def_pd.getHeavyLaser();
			break;
		case 48:
			res = def_pd.getGaussCannon();
			break;
		case 49:
			res = def_pd.getIonCannon();
			break;
		case 50:
			res = def_pd.getPlasmaTurret();
			break;
		case 51:
			res = def_pd.getSmallShieldDome();
			break;
		case 52:
			res = def_pd.getLargeShieldDome();
			break;
		case 53:
			res = def_pd.getAntiBallisticMissle();
			break;
		case 54:
			res = def_pd.getInterplanetaryMissle();
			break;
		}
		return res;
	}
	private void setById(int id, int val) {
		switch(id+31) {
		case 31:
			def_ps.setLightFighter(val);
			break;
		case 32:
			def_ps.setHeavyFighter(val);
			break;
		case 33:
			def_ps.setCruiser(val);
			break;
		case 34:
			def_ps.setBattleship(val);
			break;
		case 35:
			def_ps.setBattlecruiser(val);
			break;
		case 36:
			def_ps.setBomber(val);
			break;
		case 37:
			def_ps.setDestroyer(val);
			break;
		case 38:
			def_ps.setDeathStar(val);
			break;
		case 39:
			def_ps.setSmallCargoShip(val);
			break;
		case 40:
			def_ps.setLargeCargoShip(val);
			break;
		case 41:
			def_ps.setColonyShip(val);
			break;
		case 42:
			def_ps.setRecycler(val);
			break;
		case 43:
			def_ps.setEspionageProbe(val);
			break;
		case 44:
			def_ps.setSolarSattlelite(val);
			break;
		case 45:
			def_pd.setRocketLauncher(val);
			break;
		case 46:
			def_pd.setLightLaser(val);
			break;
		case 47:
			def_pd.setHeavyLaser(val);
			break;
		case 48:
			def_pd.setGaussCannon(val);
			break;
		case 49:
			def_pd.setIonCannon(val);
			break;
		case 50:
			def_pd.setPlasmaTurret(val);
			break;
		case 51:
			def_pd.setSmallShieldDome(val);
			break;
		case 52:
			def_pd.setLargeShieldDome(val);
			break;
		case 53:
			 def_pd.setAntiBallisticMissle(val);
			break;
		case 54:
			def_pd.setInterplanetaryMissle(val);
			break;
		}
	}
	public void updateRes(long seconds) {
		Query query = em.createQuery("select max(id) from WorldSettings k");
		int id =  (int)query.getSingleResult();
		query = em.createQuery("select k from WorldSettings k where k.id = :id");
		query.setParameter("id", id);
		int geologist = 1;
		float workload = 1f;
		int item = 1;
		try {
			Object res = query.getSingleResult();
			WorldSettings ws = (WorldSettings)res;
		
			double m =  def_pg.getMetal();
			double c = def_pg.getCrystal();
			double d = def_pg.getDeut();			 
			m += seconds * ((30 * def_pb.getMetalMine() * Math.pow(1.1, def_pb.getMetalMine()) * geologist * workload * item + 120) * ((100 + 1 * def_pr.getPlasma()) / 100) * ws.getGameSpeed())/3600;
			c += seconds * ((20 * def_pb.getCrystalMine() * Math.pow(1.1, def_pb.getCrystalMine()) * geologist * workload * item + 60) * ((100 + 0.66 * def_pr.getPlasma()) / 100) * ws.getGameSpeed())/3600;
			d += seconds * ((10 * def_pb.getDeutSyn() * Math.pow(1.1, def_pb.getDeutSyn()) * (1.44 - 0.004 * def_pg.getTemperature()) * geologist * workload) * ((100 + 0.33 * def_pr.getPlasma()) / 100) * ws.getGameSpeed() - Math.ceil(10 * def_pb.getFusionReactor() * Math.pow(1.1, def_pb.getFusionReactor()) * ws.getGameSpeed()))/3600;
			def_pg.setMetal(Math.min(m,Math.round(2.5 * Math.pow(Math.E,(20 * def_pb.getMetalStorage() / 33)) * 5000)));
			def_pg.setCrystal(Math.min(c,Math.round(2.5 * Math.pow(Math.E,(20 * def_pb.getCrystalStorage() / 33)) * 5000)));
			def_pg.setDeut(Math.min(d,Math.round(2.5 * Math.pow(Math.E,(20 * def_pb.getDeutTank() / 33)) * 5000)));

		} catch(NoResultException e){	
			System.out.println("Keine WS in DB");
		}		
	}
	
	private void resDiff() {
		long diff = (System.currentTimeMillis() - def_pg.getLastUpdate().getTime()) / 1000;
		updateRes(diff);
		def_pg.setLastUpdate(new Date(System.currentTimeMillis()));
	}
	private void saveDefDataset() {
		try {
			utx.begin();
		} catch (NotSupportedException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		em.merge(def_pg);
		em.merge(def_ps);
		em.merge(def_pd);
		try {
			utx.commit();
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
				| HeuristicRollbackException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
