package managed;

import java.util.List;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import bean.TipoMedicamento;
import dao.TipoMedicamentoDAO;
import util.Message;

@ViewScoped
@ManagedBean(name = "tipomedicamentoManaged")
public class TipoMedicamentoManaged {
	
	private TipoMedicamento tipomedicamento = new TipoMedicamento();
	private List<TipoMedicamento> lista = new ArrayList<TipoMedicamento>();
	private TipoMedicamentoDAO dao = TipoMedicamentoDAO.getTipoMedicamentoDAO();
	private Message message = new Message();
	private int id = 0;
	
	/*Crear nuevo registro*/
	public String create(){
		try {
			if(tipomedicamento.getDescripcion().equals("")){
				String msg = "Ingrese descripcion";
				message.showMessage(FacesMessage.SEVERITY_WARN, "Advertencia", msg);
			} else{
				int registro = dao.create(tipomedicamento);
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
	public void loadTipoMedicamento(){
		tipomedicamento = dao.buscar(id);
	}
	
	/*Editar registro*/
	public String edit(){
		try {
			if(tipomedicamento.getTipoMedicamentoId() == 0 || tipomedicamento.getTipoMedicamentoId() < 0){
				String msg = "Tipo Medicamento no ha sido encontrada";
				message.showMessage(FacesMessage.SEVERITY_WARN, "Advertencia", msg);
			} else if(tipomedicamento.getDescripcion().equals("")){
				String msg = "Ingrese descripcion";
				message.showMessage(FacesMessage.SEVERITY_WARN, "Advertencia", msg);
			} else{
				dao.edit(tipomedicamento);
				message.showMessageRedirect(FacesMessage.SEVERITY_INFO, "Exito", "Registro modificado");
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
			if(tipomedicamento.getTipoMedicamentoId() == 0 || tipomedicamento.getTipoMedicamentoId() < 0){
				String msg = "Tipo Medicamento no ha sido encontrada";
				message.showMessage(FacesMessage.SEVERITY_WARN, "Advertencia", msg);
			} else{
				dao.delete(tipomedicamento);
				message.showMessageRedirect(FacesMessage.SEVERITY_INFO, "Exito", "Registro eliminado");
			}
		} catch (Exception e) {
			message.showMessageRedirect(FacesMessage.SEVERITY_FATAL, "Error", e.getMessage());
			
		} finally{
			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		}
		return null;
	}
	
	public TipoMedicamento getTipoMedicamento() {
		return tipomedicamento;
	}
	public void setTipoMedicamento(TipoMedicamento tipomedicamento) {
		this.tipomedicamento = tipomedicamento;
	}
	
	public List<TipoMedicamento> getLista() {
		lista = dao.listar();
		return lista;
	}
	public void setLista(List<TipoMedicamento> lista) {
		this.lista = lista;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}