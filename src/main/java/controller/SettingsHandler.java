package controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import model.WorldSettings;


@ManagedBean(name="settingsHandler")
@SessionScoped
public class SettingsHandler implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EntityManager em;
	private UserTransaction utx;
	private WorldSettings settings;
	private int settingsForTable;
	
	public SettingsHandler() {
		
	}

	
	public SettingsHandler(EntityManager em, UserTransaction utx) {
		this.em = em;
		this.utx = utx;
		loadSettings();
	}

	private void startSettings(String name, int gameSpeed, int fleetSpeed, int startingPlanetSize,
			double fleetToDebrisFieldRatio, double defToDebrisFieldRatio, double jumpgateCooldown, int protection) {
		settings = new WorldSettings(name, gameSpeed, fleetSpeed, startingPlanetSize, fleetToDebrisFieldRatio, 
				defToDebrisFieldRatio, jumpgateCooldown, protection);
		try {
			utx.begin();
		} catch (NotSupportedException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		em.persist(settings);
		try {
			utx.commit();
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
				| HeuristicRollbackException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void loadSettings() {
		Query query = em.createQuery("select k from WorldSettings k");
		try {
			settings = (WorldSettings) query.getSingleResult();
		} catch(NoResultException e) {
			startSettings("Electra", 1, 1, 193, 0.1, 0.1 , 5, 10);
		}
	}
	
	public void changeSettings(boolean left) {
		if(left) {
			int min = getMinSettings();
			if(getSettingsForTable() - 1 < min) {
				setSettingsForTable(min);
			} else {
				setSettingsForTable(getSettingsForTable()-1);
			}
		}else {
			int max = getMaxSettings();
			if(getSettingsForTable() + 1 > max) {
				setSettingsForTable(max);
			} else {
				setSettingsForTable(getSettingsForTable()+1);
			}
		}
		
	}
	
	private int getMinSettings() {
		Query query = em.createQuery("select min(id) from WorldSettings k");
		int min = (int) query.getSingleResult();
		return min;
	}

	private int getMaxSettings() {
		Query query = em.createQuery("select max(id) from WorldSettings k");
		int max = (int) query.getSingleResult();
		return max;
	}

	public void updateSettings() {			
		try {
			utx.begin();
		} catch (NotSupportedException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		em.merge(settings);
		try {
			utx.commit();
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
				| HeuristicRollbackException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public WorldSettings getSettings() {
		return settings;
	}

	public void setSettings(WorldSettings settings) {
		this.settings = settings;
	}


	public int getSettingsForTable() {
		return settingsForTable;
	}


	public void setSettingsForTable(int settingsForTable) {
		this.settingsForTable = settingsForTable;
	}
	
	
}
