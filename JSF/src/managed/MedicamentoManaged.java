package managed;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import bean.Especie;
import bean.Laboratorio;
import bean.Medicamento;
import bean.TipoMedicamento;
import dao.MedicamentoDAO;
import util.Message;

@RequestScoped
@ManagedBean(name = "medicamentoManaged")
public class MedicamentoManaged {

	private Medicamento medicamento = new Medicamento();
	private List<Medicamento> lista = new ArrayList<Medicamento>();
	private MedicamentoDAO dao = MedicamentoDAO.getMedicamentoDAO();
	private Message message = new Message();
	private int id = 0;
	
	private List<Especie> listadoE = new ArrayList<Especie>();
	private List<Laboratorio> listadoL = new ArrayList<Laboratorio>();
	private List<TipoMedicamento> listadoTM = new ArrayList<TipoMedicamento>();

	/*Crear nuevo registro*/
	public String create(){
		try {
			if(medicamento.getNombre().equals("")){
				String msg = "Ingrese nombre";
				message.showMessage(FacesMessage.SEVERITY_WARN, "Advertencia", msg);
				
			} else if(medicamento.getEspecie().getEspecieId() == 0){
				String msg = "Seleccione especie";
				message.showMessage(FacesMessage.SEVERITY_WARN, "Advertencia", msg);
				
			} else if(medicamento.getLaboratorio().getLaboratorioId() == 0){
				String msg = "Seleccione laboratorio";
				message.showMessage(FacesMessage.SEVERITY_WARN, "Advertencia", msg);
				
			} else if(medicamento.getTipoMedicamento().getTipoMedicamentoId() == 0){
				String msg = "Seleccione tipo de medicamento";
				message.showMessage(FacesMessage.SEVERITY_WARN, "Advertencia", msg);
				
			} else if(medicamento.getPrecio() == 0){
				String msg = "Ingrese precio";
				message.showMessage(FacesMessage.SEVERITY_WARN, "Advertencia", msg);
			} else{
				int registrado = dao.create(medicamento);
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
		medicamento = dao.buscar(id);
	}
	
	/*Editar registro*/
	public String edit(){
		try {
			if(medicamento.getMedicamentoId() == 0 || medicamento.getMedicamentoId() < 0){
				String msg = "Medicamento no ha sido encontrado";
				message.showMessage(FacesMessage.SEVERITY_WARN, "Advertencia", msg);
				
			} else if(medicamento.getNombre().equals("")){
				String msg = "Ingrese nombre";
				message.showMessage(FacesMessage.SEVERITY_WARN, "Advertencia", msg);
				
			} else if(medicamento.getEspecie().getEspecieId() == 0){
				String msg = "Seleccione especie";
				message.showMessage(FacesMessage.SEVERITY_WARN, "Advertencia", msg);
				
			} else if(medicamento.getLaboratorio().getLaboratorioId() == 0){
				String msg = "Seleccione laboratorio";
				message.showMessage(FacesMessage.SEVERITY_WARN, "Advertencia", msg);
				
			} else if(medicamento.getTipoMedicamento().getTipoMedicamentoId() == 0){
				String msg = "Seleccione tipo de medicamento";
				message.showMessage(FacesMessage.SEVERITY_WARN, "Advertencia", msg);
				
			} else if(medicamento.getPrecio() == 0){
				String msg = "Ingrese precio";
				message.showMessage(FacesMessage.SEVERITY_WARN, "Advertencia", msg);
			} else{
				int editado = dao.edit(medicamento);
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
			if(medicamento.getMedicamentoId() == 0 || medicamento.getMedicamentoId() < 0){
				String msg = "Medicamento no ha sido encontrado";
				message.showMessage(FacesMessage.SEVERITY_WARN, "Advertencia", msg);
			} else{
				int eliminado = dao.delete(medicamento);
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

	/*Agregar Stock*/
	public String addstock(){
		try {
			if(medicamento.getMedicamentoId() == 0 || medicamento.getMedicamentoId() < 0){
				String msg = "Medicamento no ha sido encontrado";
				message.showMessage(FacesMessage.SEVERITY_WARN, "Advertencia", msg);
			} else{
				int agregado = dao.addstock(medicamento);
				if(agregado > 0){
					message.showMessageRedirect(FacesMessage.SEVERITY_INFO, "Exito", "Cantidad adicionada");
				}
			}
		} catch (Exception e) {
			message.showMessageRedirect(FacesMessage.SEVERITY_FATAL, "Error", e.getMessage());
			
		} finally{
			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		}
		return null;
	}
	
	public Medicamento getMedicamento() {
		return medicamento;
	}
	public void setMedicamento(Medicamento medicamento) {
		this.medicamento = medicamento;
	}
	
	public List<Medicamento> getLista() {
		lista = dao.listar();
		return lista;
	}
	public void setLista(List<Medicamento> lista) {
		this.lista = lista;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public List<Especie> getListadoE() {
		listadoE = dao.listarEspecies();
		return listadoE;
	}
	public void setListadoE(List<Especie> listadoE) {
		this.listadoE = listadoE;
	}
	
	public List<Laboratorio> getListadoL() {
		listadoL = dao.listarLaboratorios();
		return listadoL;
	}
	public void setListadoL(List<Laboratorio> listadoL) {
		this.listadoL = listadoL;
	}
	
	public List<TipoMedicamento> getListadoTM() {
		listadoTM = dao.listarTipoMedicamentos();
		return listadoTM;
	}
	public void setListadoTM(List<TipoMedicamento> listadoTM) {
		this.listadoTM = listadoTM;
	}
	
}