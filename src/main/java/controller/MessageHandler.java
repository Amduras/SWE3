package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import model.Messages;
import model.User;

@ManagedBean(name = "messageHandler")
@SessionScoped
public class MessageHandler implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Messages> messages = new ArrayList<>();
	private User user;
	private boolean unread;
	private EntityManager em;
	private UserTransaction utx;
	private Messages message;
	private String newMessageUser;
	private String newMessageSubject;
	private String newMessageContent;

	public MessageHandler() {

	}

	public MessageHandler(EntityManager em, UserTransaction utx) {
		this.em = em;
		this.utx = utx;
	}

	@SuppressWarnings("unchecked")
	private void createMessageList() {
		Query query = em.createQuery("select k from Messages k where k.toUser = :user");
		query.setParameter("user", user.getUsername());
		messages = query.getResultList();
	}

	public List<Messages> getMessages() {
		createMessageList();
		return messages;
	}
	public void setMessages(List<Messages> messages) {
		this.messages = messages;
	}
	public boolean isUnread() {
		return unread;
	}
	public void setUnread(boolean unread) {
		this.unread = unread;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Messages getMessage() {
		return message;
	}

	public void setMessage(int i, boolean galaxy) {
		if(i != -1) {
			if(!galaxy) {
				this.message = messages.get(i);
				message.setRead(true);
				try {
					utx.begin();
				} catch (NotSupportedException | SystemException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				em.merge(message);
				try {
					utx.commit();
				} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
						| HeuristicRollbackException | SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				setNewMessageUser(message.getFromUser());
				setNewMessageSubject("Re:"+message.getSubject());
			}
			
		}else {
			this.message = new Messages();
			this.message.setContent(null);
			this.newMessageUser = null;
			this.newMessageSubject = null;
			this.newMessageContent = null;
		}
	}
	
	private void toGrowl(String msg) {
		FacesContext context = FacesContext.getCurrentInstance();
		if(context != null) {
			context.addMessage(null, new FacesMessage(msg));
		} else {
			System.out.println("context: null");
		}
	}
	
	public void submit() {
		Query query = em.createQuery("select k from User k where k.username = :name");
		query.setParameter("name", newMessageUser);
		try {
			Object res = query.getSingleResult();
		} catch(NoResultException e) {
			toGrowl("Nutzer "+newMessageUser+" nicht vorhanden.");
		}
		Messages newMessage = new Messages(user.getUsername(), newMessageUser, newMessageContent, newMessageSubject);
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
		toGrowl("Nachricht verschickt.");
//		include.setPage("messages", fleet);
	}

	public void remove(int i) {
		Query query = em.createQuery("delete from Messages k where k.messagesId = :id");
		query.setParameter("id", messages.get(i).getMessagesId());
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
	}

	public String getNewMessageUser() {
		return newMessageUser;
	}

	public void setNewMessageUser(String newMessageUser) {
		this.newMessageUser = newMessageUser;
	}

	public String getNewMessageSubject() {
		return newMessageSubject;
	}

	public void setNewMessageSubject(String newMessageSubject) {
		this.newMessageSubject = newMessageSubject;
	}

	public String getNewMessageContent() {
		return newMessageContent;
	}

	public void setNewMessageContent(String newMessageContent) {
		this.newMessageContent = newMessageContent;
	}
}
