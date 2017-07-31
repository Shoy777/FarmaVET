package bean;

import java.util.Date;

public  class Boleta
{
	
	private int BoletaId;
	private String Cliente;
	private double MontoTotal;
	private int CantidadItems;
	private Date FechaVenta;
	
	public Boleta()
	{
		
	}

	public int getBoletaId() {
		return BoletaId;
	}
	public void setBoletaId(int boletaId) {
		BoletaId = boletaId;
	}

	public String getCliente() {
		return Cliente;
	}
	public void setCliente(String cliente) {
		Cliente = cliente;
	}

	public double getMontoTotal() {
		return MontoTotal;
	}
	public void setMontoTotal(double montoTotal) {
		MontoTotal = montoTotal;
	}

	public int getCantidadItems() {
		return CantidadItems;
	}
	public void setCantidadItems(int cantidadItems) {
		CantidadItems = cantidadItems;
	}

	public Date getFechaVenta() {
		return FechaVenta;
	}
	public void setFechaVenta(Date fechaVenta) {
		FechaVenta = fechaVenta;
	}
	
}