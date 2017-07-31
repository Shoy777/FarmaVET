package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import bean.Laboratorio;
import util.Message;

public class LaboratorioDAO {

	private Conexion conexion = Conexion.getInstancia();
	private PreparedStatement pstm = null;
	private Connection conn = null;
	private ResultSet rs = null;
	private Laboratorio laboratorio = null;
	private Message message = new Message();
	
	private static LaboratorioDAO dao = null;
	
	public static LaboratorioDAO getLaboratorioDAO(){
		if(dao == null){
			dao = new LaboratorioDAO();
		}
		return dao;
	}
	
	private LaboratorioDAO() {
		
	}
	
	public int create(Laboratorio laboratorio){
		int registro = 0;
		try {
			conn = conexion.getConexion();
			String sql = "insert into laboratorio(descripcion) values(?)";
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, laboratorio.getDescripcion());
			
			registro = pstm.executeUpdate();
			System.out.println(pstm);
			
		} catch (SQLServerException e) {
			if(e.getMessage().contains("UK")){
				String msg = "Ingrese una descripcion diferente";
				message.showMessageRedirect(FacesMessage.SEVERITY_FATAL, "Error", msg);
			} else{
				message.showMessageRedirect(FacesMessage.SEVERITY_FATAL, "Error", "No se pudo registar");
			}
		} catch (Exception e) {
			message.showMessage(FacesMessage.SEVERITY_FATAL, "Error", e.getMessage());
		}
		return registro;
	}
	
	public int edit(Laboratorio laboratorio){
		int editado = 0;
		try {
			conn = conexion.getConexion();
			String sql = "update laboratorio set descripcion = ? where laboratorioid = ?";
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, laboratorio.getDescripcion());
			pstm.setInt(2, laboratorio.getLaboratorioId());
			
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
	
	public int delete(Laboratorio laboratorio){
		int eliminado = 0;
		try {
			conn = conexion.getConexion();
			String sql = "delete laboratorio where laboratorioid = ?";
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, laboratorio.getLaboratorioId());
			
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
	
	public List<Laboratorio> listar(){
		List<Laboratorio> lista = new ArrayList<Laboratorio>();
		try {
			conn = conexion.getConexion();
			String sql = "select * from laboratorio";
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();
			while(rs.next()){
				laboratorio = new Laboratorio();
				laboratorio.setLaboratorioId(rs.getInt("LaboratorioId"));
				laboratorio.setDescripcion(rs.getString("Descripcion"));
				lista.add(laboratorio);
			}
		} catch (Exception e) {
			message.showMessage(FacesMessage.SEVERITY_FATAL, "Error", e.getMessage());
		}
		return lista;
	}
	
	public Laboratorio buscar(int id){
		try {
			conn = conexion.getConexion();
			String sql = "select * from laboratorio where laboratorioid = ?";
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, id);
			rs = pstm.executeQuery();
			while(rs.next()){
				laboratorio = new Laboratorio();
				laboratorio.setLaboratorioId(rs.getInt("LaboratorioId"));
				laboratorio.setDescripcion(rs.getString("Descripcion"));
			}
		} catch (Exception e) {
			message.showMessage(FacesMessage.SEVERITY_FATAL, "Error", e.getMessage());
		}
		return laboratorio;
	}
	
}