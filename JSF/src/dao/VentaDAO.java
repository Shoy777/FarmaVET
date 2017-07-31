package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.faces.application.FacesMessage;

import com.microsoft.sqlserver.jdbc.SQLServerException;

import bean.DetalleBoleta;
import bean.TipoMedicamento;
import util.Message;

public class VentaDAO {
	
	private Conexion conexion = Conexion.getInstancia();
	private PreparedStatement pstm = null;
	private Connection conn = null;
	private ResultSet rs = null;
	private TipoMedicamento tipomedicamento = null;
	private Message message = new Message();
	
	private static VentaDAO dao = null;
	
	public static VentaDAO getVentaDAO(){
		if(dao == null){
			dao = new VentaDAO();
		}
		return dao;
	}
	
	private VentaDAO() {
		
	}
	
	public int Venta(){
		int registro =0;
		try {
			conn = conexion.getConexion();
			String sql = "insert into tipomedicamento(descripcion) values(?)";
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, tipomedicamento.getDescripcion());
			
			registro = pstm.executeUpdate();
			System.out.println(pstm);
			
		} catch (SQLServerException e) {
			if(e.getMessage().contains("UK")){
				String msg = "Ingrese una descripcion diferente";
				message.showMessageRedirect(FacesMessage.SEVERITY_FATAL, "Error", msg);
			} else{
				message.showMessageRedirect(FacesMessage.SEVERITY_FATAL, "Error", "No se pudo registrar");
			}
		} catch (Exception e) {
			message.showMessage(FacesMessage.SEVERITY_FATAL, "Error", e.getMessage());
		}
		return registro;
	}
	
}