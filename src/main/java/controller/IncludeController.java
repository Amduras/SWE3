package controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class IncludeController implements Serializable{

	private static final long serialVersionUID = 1L;

	private String page;
	
	@PostConstruct
	public void init() {
			page="start";
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		FacesContext.getCurrentInstance().getViewRoot().getViewMap().remove("roles");
		this.page = page;
	}
	
}