package managed;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.swing.JOptionPane;

import bean.Boleta;
import bean.DetalleBoleta;
import bean.Medicamento;
import dao.MedicamentoDAO;
import dao.VentaDAO;
import util.Message;

@ViewScoped
@ManagedBean(name = "ventaManaged")
public class VentaManaged {
	
	private Boleta boleta = new Boleta();
	private List<DetalleBoleta> ventadetails = new ArrayList<DetalleBoleta>();
	//private VentaDAO dao = VentaDAO.getVentaDAO();
	private Message message = new Message();
	
	@SuppressWarnings("unchecked")
	public void addDetalle(){
		
		DetalleBoleta det = new DetalleBoleta();
		//det.getBoletaId();
		
		det.getMedicamento().setMedicamentoId(medicamento.getMedicamentoId());
		
		MedicamentoDAO daoM = MedicamentoDAO.getMedicamentoDAO();
		medicamento = daoM.buscar(medicamento.getMedicamentoId());
		det.getMedicamento().setNombre(medicamento.getNombre());
		det.getMedicamento().getEspecie().setDescripcion(medicamento.getEspecie().getDescripcion());
		det.getMedicamento().getLaboratorio().setDescripcion(medicamento.getLaboratorio().getDescripcion());
		det.getMedicamento().getTipoMedicamento().setDescripcion(medicamento.getTipoMedicamento().getDescripcion());
		det.setPrecio(medicamento.getPrecio());
		det.setCantidad(cantidad);
		subtotal = medicamento.getPrecio() * cantidad;
		det.setSubTotal(subtotal);
		/*
		JOptionPane.showMessageDialog(null, "ID: "+ medicamento.getMedicamentoId());
		JOptionPane.showMessageDialog(null, "Precio: "+ medicamento.getPrecio());
		JOptionPane.showMessageDialog(null, "Cantidad: "+ cantidad);
		JOptionPane.showMessageDialog(null, "Subtotal: "+ subtotal);
		
		message.showMessage(FacesMessage.SEVERITY_INFO, "Info", "ID: "+ medicamento.getMedicamentoId());
		message.showMessage(FacesMessage.SEVERITY_INFO, "Info", "Precio: "+ medicamento.getPrecio());
		message.showMessage(FacesMessage.SEVERITY_INFO, "Info", "Cantidad: "+ cantidad);
		message.showMessage(FacesMessage.SEVERITY_INFO, "Info", "subtotal: "+ subtotal);
		*/
		Map<String, Object> map = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		if(map.get("ventadetails")!= null){
			ventadetails = (ArrayList<DetalleBoleta>)map.get("ventadetails");
		}
		if(ventadetails.size() == 0){
			ventadetails.add(det);
			message.showMessage(FacesMessage.SEVERITY_INFO, "Info", "Medicamento agregado 1");
			
		} else{
			for(DetalleBoleta x : ventadetails){
				if(x.getMedicamento().getMedicamentoId() == medicamento.getMedicamentoId()){
					det = x;
				}
			}
			if(det.getMedicamento().getMedicamentoId() != ){
				ventadetails.add(det);
				message.showMessage(FacesMessage.SEVERITY_INFO, "Info", "Medicamento agregado 2");
			} else{
				int can = det.getCantidad();
				det.setCantidad(can+=cantidad);
				double subT = det.getSubTotal();
				det.setSubTotal(subT+=det.getPrecio() * cantidad);
				message.showMessage(FacesMessage.SEVERITY_INFO, "Info", cantidad +" und adicionadas a "+det.getMedicamento().getDescripcion());
			}
			//ventadetails.add(det);
			//message.showMessage(FacesMessage.SEVERITY_INFO, "Info", "Medicamento agregado 2");
		}
		map.put("ventadetails", ventadetails);
	}
	
	public Boleta getBoleta() {
		return boleta;
	}
	public void setBoleta(Boleta boleta) {
		this.boleta = boleta;
	}
	
	public List<DetalleBoleta> getVentadetails() {
		return ventadetails;
	}
	public void setVentadetails(List<DetalleBoleta> ventadetails) {
		this.ventadetails = ventadetails;
	}
	
	private Medicamento medicamento = new Medicamento();
	private int cantidad;
	private double subtotal;
	private double montototal;
	
	public Medicamento getMedicamento() {
		return medicamento;
	}
	public void setMedicamento(Medicamento medicamento) {
		this.medicamento = medicamento;
	}
	
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	
	public double getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public double getMontototal() {
		return montototal;
	}
	public void setMontototal(double montototal) {
		this.montototal = montototal;
	}
	
}