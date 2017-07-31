package bean;

public class DetalleBoleta
{

	private int BoletaId;
	private Medicamento medicamento;
	private double Precio;
	private int Cantidad;
	private double SubTotal;
    
    public DetalleBoleta() {
    	medicamento = new Medicamento();
	}
    
	public int getBoletaId() {
		return BoletaId;
	}
	public void setBoletaId(int boletaId) {
		BoletaId = boletaId;
	}
	
	public Medicamento getMedicamento() {
		return medicamento;
	}
	public void setMedicamento(Medicamento medicamento) {
		this.medicamento = medicamento;
	}
	
	public double getPrecio() {
		return Precio;
	}
	public void setPrecio(double precio) {
		Precio = precio;
	}
	
	public int getCantidad() {
		return Cantidad;
	}
	public void setCantidad(int cantidad) {
		Cantidad = cantidad;
	}
	
	public double getSubTotal() {
		return SubTotal;
	}
	public void setSubTotal(double subTotal) {
		SubTotal = subTotal;
	}
    
}