package bean;

public class Medicamento {
	
	private int MedicamentoId;
	private String Nombre;
	private Especie especie;
	private TipoMedicamento tipoMedicamento;
	private Laboratorio laboratorio;
	private String Descripcion;
	private double Precio;
	private int Stock;
	private byte Estado;
	private int Cantidad;
	
	public Medicamento()
	{
		especie = new Especie();
		laboratorio = new Laboratorio();
		tipoMedicamento = new TipoMedicamento();
	}
	
	public int getMedicamentoId() {
		return MedicamentoId;
	}
	public void setMedicamentoId(int medicamentoId) {
		MedicamentoId = medicamentoId;
	}

	public String getNombre() {
		return Nombre;
	}
	public void setNombre(String nombre) {
		Nombre = nombre;
	}
	
	public Especie getEspecie() {
		return especie;
	}
	public void setEspecie(Especie especie) {
		this.especie = especie;
	}
	
	public TipoMedicamento getTipoMedicamento() {
		return tipoMedicamento;
	}
	public void setTipoMedicamento(TipoMedicamento tipoMedicamento) {
		this.tipoMedicamento = tipoMedicamento;
	}
	
	public Laboratorio getLaboratorio() {
		return laboratorio;
	}
	public void setLaboratorio(Laboratorio laboratorio) {
		this.laboratorio = laboratorio;
	}
	
	public String getDescripcion() {
		return Descripcion;
	}
	public void setDescripcion(String descripcion) {
		Descripcion = descripcion;
	}

	public double getPrecio() {
		return Precio;
	}
	public void setPrecio(double precio) {
		Precio = precio;
	}

	public int getStock() {
		return Stock;
	}
	public void setStock(int stock) {
		Stock = stock;
	}

	public byte getEstado() {
		return Estado;
	}
	public void setEstado(byte estado) {
		Estado = estado;
	}
	
	public int getCantidad() {
		return Cantidad;
	}
	public void setCantidad(int cantidad) {
		Cantidad = cantidad;
	}
}