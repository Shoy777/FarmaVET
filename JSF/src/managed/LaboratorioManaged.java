package managed;

import java.util.List;
import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import bean.Laboratorio;
import dao.LaboratorioDAO;
import util.Message;

@RequestScoped
@ManagedBean(name = "laboratorioManaged")
public class LaboratorioManaged {
	
	private Laboratorio laboratorio = new Laboratorio();
	private List<Laboratorio> lista = new ArrayList<Laboratorio>();
	private LaboratorioDAO dao = LaboratorioDAO.getLaboratorioDAO();
	private Message message = new Message();
	private int id = 0;
	
	/*Crear nuevo registro*/
	public String create(){
		try {
			if(laboratorio.getDescripcion().equals("")){
				String msg = "Ingrese descripcion";
				message.showMessage(FacesMessage.SEVERITY_WARN, "Advertencia", msg);
			} else{
				int registrado = dao.create(laboratorio);
				if(registrado > 0){
					message.showMessageRedirect(FacesMessage.SEVERITY_INFO, "Exito", "Registro grabado");	
				}
			}
		} catch (Exception e) {
			message.showMessageRedirect(FacesMessage.SEVERITY_FATAL, "Error", e.getMessage());
			
		} finally{
			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		}
		return null;
	}
	
	/*Cargar datos por id*/
	public void load(){
		laboratorio = dao.buscar(id);
	}
	
	/*Editar registro*/
	public String edit(){
		try {
			if(laboratorio.getLaboratorioId() == 0 || laboratorio.getLaboratorioId() < 0){
				String msg = "Laboratorio no ha sido encontrada";
				message.showMessage(FacesMessage.SEVERITY_WARN, "Advertencia", msg);
			} else if(laboratorio.getDescripcion().equals("")){
				String msg = "Ingrese descripcion";
				message.showMessage(FacesMessage.SEVERITY_WARN, "Advertencia", msg);
			} else{
				int editado = dao.edit(laboratorio);
				if(editado > 0){
					message.showMessageRedirect(FacesMessage.SEVERITY_INFO, "Exito", "Registro modificado");
				}
			}
		} catch (Exception e) {
			message.showMessageRedirect(FacesMessage.SEVERITY_FATAL, "Error", e.getMessage());
			
		} finally{
			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		}
		return null;
	}
	
	/*Eliminar registro*/
	public String delete(){
		try {
			if(laboratorio.getLaboratorioId() == 0 || laboratorio.getLaboratorioId() < 0){
				String msg = "Laboratorio no ha sido encontrada";
				message.showMessage(FacesMessage.SEVERITY_WARN, "Advertencia", msg);
			} else{
				int eliminado = dao.delete(laboratorio);
				if(eliminado > 0){
					message.showMessageRedirect(FacesMessage.SEVERITY_INFO, "Exito", "Registro eliminado");
				}
			}
		} catch (Exception e) {
			message.showMessageRedirect(FacesMessage.SEVERITY_FATAL, "Error", e.getMessage());
			
		} finally{
			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		}
		return null;
	}
	
	public Laboratorio getLaboratorio() {
		return laboratorio;
	}
	public void setEspecie(Laboratorio laboratorio) {
		this.laboratorio = laboratorio;
	}
	
	public List<Laboratorio> getLista() {
		lista = dao.listar();
		return lista;
	}
	public void setLista(List<Laboratorio> lista) {
		this.lista = lista;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}