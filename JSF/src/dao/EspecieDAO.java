package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import bean.Especie;
import util.Message;

public class EspecieDAO {

	private Conexion conexion = Conexion.getInstancia();
	private PreparedStatement pstm = null;
	private Connection conn = null;
	private ResultSet rs = null;
	private Especie especie = null;
	private Message message = new Message();
	
	private static EspecieDAO dao = null;
	
	public static EspecieDAO getEspecieDAO(){
		if(dao == null){
			dao = new EspecieDAO();
		}
		return dao;
	}
	
	private EspecieDAO() {
		
	}
	
	public int create(Especie especie){
		int registro =0;
		try {
			conn = conexion.getConexion();
			String sql = "insert into especie(descripcion) values(?)";
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, especie.getDescripcion());
			
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
	
	public int edit(Especie especie){
		int editado = 0;
		try {
			conn = conexion.getConexion();
			String sql = "update especie set descripcion = ? where especieid = ?";
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, especie.getDescripcion());
			pstm.setInt(2, especie.getEspecieId());
			
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
	
	public int delete(Especie especie){
		int eliminado = 0;
		try {
			conn = conexion.getConexion();
			String sql = "delete especie where especieid = ?";
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, especie.getEspecieId());
			
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
	
	public List<Especie> listar(){
		List<Especie> lista = new ArrayList<Especie>();
		try {
			conn = conexion.getConexion();
			String sql = "select * from especie";
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();
			while(rs.next()){
				especie = new Especie();
				especie.setEspecieId(rs.getInt("EspecieId"));
				especie.setDescripcion(rs.getString("Descripcion"));
				lista.add(especie);
			}
		} catch (Exception e) {
			message.showMessage(FacesMessage.SEVERITY_FATAL, "Error", e.getMessage());
		}
		return lista;
	}
	
	public Especie buscar(int id){
		try {
			conn = conexion.getConexion();
			String sql = "select * from especie where especieid = ?";
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, id);
			rs = pstm.executeQuery();
			while(rs.next()){
				especie = new Especie();
				especie.setEspecieId(rs.getInt("EspecieId"));
				especie.setDescripcion(rs.getString("Descripcion"));
			}
		} catch (Exception e) {
			message.showMessage(FacesMessage.SEVERITY_FATAL, "Error", e.getMessage());
		}
		return especie;
	}
	
}