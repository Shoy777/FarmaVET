package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import bean.TipoMedicamento;
import util.Message;

public class TipoMedicamentoDAO {

	private Conexion conexion = Conexion.getInstancia();
	private PreparedStatement pstm = null;
	private Connection conn = null;
	private ResultSet rs = null;
	private TipoMedicamento tipomedicamento = null;
	private Message message = new Message();
	
	private static TipoMedicamentoDAO dao = null;
	
	public static TipoMedicamentoDAO getTipoMedicamentoDAO(){
		if(dao == null){
			dao = new TipoMedicamentoDAO();
		}
		return dao;
	}
	
	private TipoMedicamentoDAO() {
		
	}
	
	public int create(TipoMedicamento tipomedicamento){
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
	
	public int edit(TipoMedicamento tipomedicamento){
		int editado = 0;
		try {
			conn = conexion.getConexion();
			String sql = "update tipomedicamento set descripcion = ? where tipomedicamentoid = ?";
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, tipomedicamento.getDescripcion());
			pstm.setInt(2, tipomedicamento.getTipoMedicamentoId());
			
			editado = pstm.executeUpdate();
			System.out.println(pstm);
			
		} catch (SQLServerException e) {
			if(e.getMessage().contains("UK")){
				String msg = "No puedes editar el nombre a uno existente!";
				message.showMessageRedirect(FacesMessage.SEVERITY_FATAL, "Error", msg);
			} else{
				message.showMessageRedirect(FacesMessage.SEVERITY_FATAL, "Error", "No se pudo editar");
			}
		} catch (Exception e) {
			message.showMessage(FacesMessage.SEVERITY_FATAL, "Error", e.getMessage());
		}
		return editado;
	}
	
	public int delete(TipoMedicamento tipomedicamento){
		int eliminado = 0;
		try {
			conn = conexion.getConexion();
			String sql = "delete tipomedicamento where tipomedicamentoid = ?";
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, tipomedicamento.getTipoMedicamentoId());
			
			eliminado = pstm.executeUpdate();
			System.out.println(pstm);
			
		} catch (SQLServerException e) {
			if(e.getMessage().contains("FK")){
				String msg = "No se puede eliminar porque hay registros enlazados";
				message.showMessageRedirect(FacesMessage.SEVERITY_FATAL, "Error", msg);
			} else{
				message.showMessageRedirect(FacesMessage.SEVERITY_FATAL, "Error", "No se puedo eliminar");
			}
		} catch (Exception e) {
			message.showMessage(FacesMessage.SEVERITY_FATAL, "Error", e.getMessage());
		}
		return eliminado;
	}
	
	public List<TipoMedicamento> listar(){
		List<TipoMedicamento> lista = new ArrayList<TipoMedicamento>();
		try {
			conn = conexion.getConexion();
			String sql = "select * from tipomedicamento";
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();
			while(rs.next()){
				tipomedicamento = new TipoMedicamento();
				tipomedicamento.setTipoMedicamentoId(rs.getInt("tipomedicamentoId"));
				tipomedicamento.setDescripcion(rs.getString("Descripcion"));
				lista.add(tipomedicamento);
			}
		} catch (Exception e) {
			message.showMessage(FacesMessage.SEVERITY_FATAL, "Error", e.getMessage());
		}
		return lista;
	}
	
	public TipoMedicamento buscar(int id){
		try {
			conn = conexion.getConexion();
			String sql = "select * from tipomedicamento where tipomedicamentoid = ?";
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, id);
			rs = pstm.executeQuery();
			while(rs.next()){
				tipomedicamento = new TipoMedicamento();
				tipomedicamento.setTipoMedicamentoId(rs.getInt("tipomedicamentoId"));
				tipomedicamento.setDescripcion(rs.getString("Descripcion"));
			}
		} catch (Exception e) {
			message.showMessage(FacesMessage.SEVERITY_FATAL, "Error", e.getMessage());
		}
		return tipomedicamento;
	}
	
}