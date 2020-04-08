package model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@SuppressWarnings("serial")
@NamedQuery(name="SelectMessages", query="Select k from Messages k")
@Entity
public class Messages implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int messagesId;
	
	private String fromUser;
	private String toUser;
	private Date dateSend;
	@Column(columnDefinition = "TEXT")
	private String content;
	private boolean read;
	private String subject;
	
	public Messages(){
		
	}
	
	public Messages(String fromUser, String toUser, String content, String subject){
		this.fromUser = fromUser;
		this.toUser = toUser;
		this.dateSend = new Date();
		this.content = content;
		this.read = false;
		this.subject = subject;
	}
	
	public int getMessagesId() {
		return messagesId;
	}

	public void setMessagesId(int messagesId) {
		this.messagesId = messagesId;
	}

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public Date getDateSend() {
		return dateSend;
	}

	public void setDateSend(Date dateSend) {
		this.dateSend = dateSend;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public String getToUser() {
		return toUser;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	
}
