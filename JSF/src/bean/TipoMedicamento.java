package bean;

public class TipoMedicamento {

	private int TipoMedicamentoId;
	private String Descripcion;
	
	public TipoMedicamento()
	{
		
	}

	public int getTipoMedicamentoId() {
		return TipoMedicamentoId;
	}

	public void setTipoMedicamentoId(int tipoMedicamentoId) {
		TipoMedicamentoId = tipoMedicamentoId;
	}

	public String getDescripcion() {
		return Descripcion;
	}

	public void setDescripcion(String descripcion) {
		Descripcion = descripcion;
	}
	
}