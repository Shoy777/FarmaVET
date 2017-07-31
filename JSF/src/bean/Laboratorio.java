package bean;

public class Laboratorio {
	
	private int LaboratorioId;
	private String Descripcion;
	
	public Laboratorio()
	{
		
	}

	public int getLaboratorioId() {
		return LaboratorioId;
	}
	public void setLaboratorioId(int laboratorioId) {
		LaboratorioId = laboratorioId;
	}

	public String getDescripcion() {
		return Descripcion;
	}
	public void setDescripcion(String descripcion) {
		Descripcion = descripcion;
	}
	
}