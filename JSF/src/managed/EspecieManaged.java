package managed;

import java.util.List;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import bean.Especie;
import dao.EspecieDAO;
import util.Message;

@ViewScoped
@ManagedBean(name = "especieManaged")
public class EspecieManaged {
	
	private Especie especie = new Especie();
	private List<Especie> lista = new ArrayList<Especie>();
	private EspecieDAO dao = EspecieDAO.getEspecieDAO();
	private Message message = new Message();
	private int id = 0;
	
	/*Crear nuevo registro*/
	public String create(){
		try {
			if(especie.getDescripcion().equals("")){
				String msg = "Ingrese descripcion";
				message.showMessage(FacesMessage.SEVERITY_WARN, "Advertencia", msg);
			} else{
				int registro = dao.create(especie);
				if(registro > 0){
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
		especie = dao.buscar(id);
	}
	
	/*Editar registro*/
	public String edit(){
		try {
			if(especie.getEspecieId() == 0 || especie.getEspecieId() < 0){
				String msg = "Especie no ha sido encontrada";
				message.showMessage(FacesMessage.SEVERITY_WARN, "Advertencia", msg);
			} else if(especie.getDescripcion().equals("")){
				String msg = "Ingrese descripcion";
				message.showMessage(FacesMessage.SEVERITY_WARN, "Advertencia", msg);
			} else{
				int editado = dao.edit(especie);
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
			if(especie.getEspecieId() == 0 || especie.getEspecieId() < 0){
				String msg = "Especie no ha sido encontrada";
				message.showMessage(FacesMessage.SEVERITY_WARN, "Advertencia", msg);
			} else{
				int eliminado = dao.delete(especie);
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
	
	public Especie getEspecie() {
		return especie;
	}
	public void setEspecie(Especie especie) {
		this.especie = especie;
	}
	
	public List<Especie> getLista() {
		lista = dao.listar();
		return lista;
	}
	public void setLista(List<Especie> lista) {
		this.lista = lista;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}