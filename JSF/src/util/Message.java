package util;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

public class Message {
	
	private UIViewRoot view = null;
	
	public void showMessage(Severity severity, String summary, String msg){
		FacesMessage mensaje = new FacesMessage(severity, summary, msg);
		RequestContext.getCurrentInstance().showMessageInDialog(mensaje);
	}
	
	public String showMessageRedirect(Severity severity, String summary, String msg){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary.concat(" : "), msg));
		view = FacesContext.getCurrentInstance().getViewRoot();
	    return view.getViewId() + "?faces-redirect=true";
	}
	
}