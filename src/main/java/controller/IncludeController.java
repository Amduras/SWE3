package controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class IncludeController implements Serializable{

	private static final long serialVersionUID = 1L;
	private String page;
	
	@PostConstruct
	public void init() {
			page="overview";
	}

	public String getPage() {
		return page;
	}
	
	public void setPage(String page, FleetHandler fleetHandler) {
		fleetHandler.setStage(0);
		this.page = page;
	}
}