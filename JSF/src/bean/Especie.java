package bean;

public class Especie {
	
	private int EspecieId;
	private String Descripcion;
	
	public Especie()
	{
		
	}
	
	public int getEspecieId() {
		return EspecieId;
	}
	public void setEspecieId(int especieId) {
		EspecieId = especieId;
	}

	public String getDescripcion() {
		return Descripcion;
	}
	public void setDescripcion(String descripcion) {
		Descripcion = descripcion;
	}
	
}