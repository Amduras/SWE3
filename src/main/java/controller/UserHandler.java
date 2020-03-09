package controller;


import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import model.User;


@ManagedBean(name="userHandler")
@SessionScoped
public class UserHandler {

	@PersistenceContext
	private EntityManager em;
	
	@Resource
	private UserTransaction utx;

	private DataModel<User> users = new ListDataModel<User>();
	private User user = null;
	
	public UserHandler() {
		
	}
	
	public UserHandler(EntityManager em, UserTransaction utx) {
		this.em = em;
		this.utx = utx;
	}

	public void userList() {
		try {
			utx.begin();
		} catch (NotSupportedException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		users=new ListDataModel<User>();
		users.setWrappedData(em.createNamedQuery("SelectUsers").getResultList());
		try {
			utx.commit();
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
				| HeuristicRollbackException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public DataModel<User> getUsers() {
		userList();
		return users;
	}

	public void setUsers(DataModel<User> users) {
		this.users = users;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


}
