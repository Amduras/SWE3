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

import planets.Planets_Buildings;
import planets.Planets_Def;
import planets.Planets_General;
import planets.Planets_Research;
import planets.Planets_Ships;

public class Fight {

	private EntityManager em;
	private UserTransaction utx;
	
	private int attPlanetId;
	private int[][] attShips = new int[13][6];
	private long[] booty;
	private Planets_Research att_pr;
	
	private int defPlanetId;
	private int[][] defShips = new int[20][6];
	private Planets_General def_pg;
	private Planets_Buildings def_pb;
	private Planets_Research def_pr;
	private Planets_Ships def_ps;
	private Planets_Def def_pd;
	
	private List<Ship> shipData;
	
	public Fight(int attPlanetId, int[] attTShips, long[] booty, int defPlanetId, EntityManager em, UserTransaction utx) {
		this.attPlanetId = attPlanetId;
		this.booty = booty;
		this.defPlanetId = defPlanetId;
		this.em = em;
		this.utx = utx;
		
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
		for(int r=0;r<6;++r) {
			//charge Shields
			chargeShields();
			
			//abwechselndes feuern, verteidiger hat ersten schuss
			for(int i=0;i<defShips.length;++i) {
				//def shot
				int dmg = defShips[i][0] * defShips[i][3];
				if(dmg != 0) {
					int target = findTargetFromAtt();
					if (target != -1) {
						int shots = RapidFire(i,target);
						
						int hulldmg = dmg*shots-attShips[target][4];
						if(hulldmg > 0)
							attShips[target][4] = 0;
						else
							attShips[target][4] -= dmg*shots;
						int losses = hulldmg / attShips[target][1];
						attShips[target][5] += losses;
						if((attShips[target][0] - losses) < 0)
							attShips[target][0] = 0;
						else
							attShips[target][0] -= losses;	
					}
					else	
						endFight(1,r);
						return;
				}
				//att shot
				if(i<attShips.length) {
					dmg = attShips[i][0] * attShips[i][3];
					if(dmg != 0) {
						int target = findTargetFromDef();
						if (target != -1) {
							int shots = RapidFire(i,target);
							
							int hulldmg = dmg*shots-defShips[target][4];
							if(hulldmg > 0)
								defShips[target][4] = 0;
							else
								defShips[target][4] -= dmg*shots;
							int losses = hulldmg / defShips[target][1];
							defShips[target][5] += losses;
							if((defShips[target][0] - losses) < 0)
								defShips[target][0] = 0;
							else
								defShips[target][0] -= losses;
						}
						else	
							endFight(0,r);
							return;
					}
				}			
			}			
		}
		endFight(2,6);
	}
	private void endFight(int i, int round) {
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
				while((booty[0]+booty[1]+booty[2])<cargoSpace) {
					booty[0] = booty[0] < maxMetal ? ++booty[0] : booty[0];
					booty[1] = booty[1] < maxCrystal ? ++booty[1] : booty[1];
					booty[2] = booty[2] < maxDeut ? ++booty[2] : booty[2];
				}
			}
			def_pg.setMetal(def_pg.getMetal()-booty[0]);
			def_pg.setCrystal(def_pg.getCrystal()-booty[1]);
			def_pg.setDeut(def_pg.getDeut()-booty[2]);
		}
		else if(i == 1) { // Victory for def
			
		}
		else {// Draw
		}
		repairDef();
		saveDefDataset();
	}
	
	private void repairDef() {
		for(int i=14;i<defShips.length;++i) {
			defShips[i][0] += defShips[i][5] * 0.6;
		}
	}
	private int getCargoSpace() {
		int res = 0;
		for(int i=0;i<attShips.length;++i) {
			res += shipData.get(i).getCargoSpace()*attShips[i][0];
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
			if(attShips[i][0] > 0)
				tar.add(i);
		}
		if(tar.size() == 0)
			return -1;
		else {
			Random rng = new Random();
			return tar.indexOf(rng.nextInt(tar.size()));
		}
	}
	private int findTargetFromDef() {
		List<Integer> tar = new ArrayList<Integer>();
		for(int i=0;i<defShips.length;++i) {
			if(defShips[i][0] > 0)
				tar.add(i);
		}
		if(tar.size() == 0)
			return -1;
		else {
			Random rng = new Random();
			return tar.indexOf(rng.nextInt(tar.size()));
		}			
	}
	private void chargeShields() {
		for(int i=0;i<attShips.length;++i) {
			attShips[i][4] = attShips[i][0] * attShips[i][2];
		}
		for(int i=0;i<defShips.length;++i) {
			defShips[i][4]= defShips[i][0] * defShips[i][2];
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
