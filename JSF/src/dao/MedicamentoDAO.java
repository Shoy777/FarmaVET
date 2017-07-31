package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.swing.JOptionPane;

import com.microsoft.sqlserver.jdbc.SQLServerException;

import bean.Especie;
import bean.Laboratorio;
import bean.Medicamento;
import bean.TipoMedicamento;
import util.Message;

public class MedicamentoDAO {
	
	private Conexion conexion = Conexion.getInstancia();
	private PreparedStatement pstm = null;
	private Connection conn = null;
	private ResultSet rs = null;
	private Medicamento medicamento = null;
	private Especie especie = null;
	private Laboratorio laboratorio = null;
	private TipoMedicamento tipomedicamento = null;
	private Message message = new Message();
	
	private static MedicamentoDAO dao = null;
	
	public static MedicamentoDAO getMedicamentoDAO(){
		if(dao == null){
			dao = new MedicamentoDAO();
		}
		return dao;
	}
	
	private MedicamentoDAO() {
		
	}
	
	/*Crear nuevo registro*/
	public int create(Medicamento medicamento){
		
		int registro = 0;
		try {
			conn = conexion.getConexion();
			String sql = "exec SP_RegistrarMedicamento ?,?,?,?,?,?";
			
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, medicamento.getNombre());
			pstm.setInt(2, medicamento.getEspecie().getEspecieId());
			pstm.setInt(3, medicamento.getTipoMedicamento().getTipoMedicamentoId());
			pstm.setInt(4, medicamento.getLaboratorio().getLaboratorioId());
			pstm.setString(5, medicamento.getDescripcion());
			pstm.setDouble(6, medicamento.getPrecio());
			
			registro = pstm.executeUpdate();
			System.out.println(pstm);
			
		} catch (SQLServerException e) {
			if(e.getMessage().contains("UK")){
				String msg = "Ya existe medicamento con mismo nombre y especie";
				message.showMessageRedirect(FacesMessage.SEVERITY_FATAL, "Error", msg);
				JOptionPane.showMessageDialog(null, "1: "+ e.getMessage());
			} else if(e.getMessage().contains("FK")){
				String msg = "No se olvide de seleccionar Especie, Laboratorio y Tipo Medicamento";
				message.showMessageRedirect(FacesMessage.SEVERITY_FATAL, "Error", msg);
				JOptionPane.showMessageDialog(null, "2: "+ e.getMessage());
			} else{
				message.showMessageRedirect(FacesMessage.SEVERITY_FATAL, "Error", "No se pudo registar");
				JOptionPane.showMessageDialog(null, "3: "+ e.getMessage());
			}
		} catch (Exception e) {
			message.showMessageRedirect(FacesMessage.SEVERITY_FATAL, "Error", e.getMessage());
		}
		return registro;
	}
	
	/*Editar registro*/
	public int edit(Medicamento medicamento){
		
		int editado = 0;
		try {
			conn = conexion.getConexion();
			String sql = "exec SP_ModificarMedicamento ?,?,?,?,?,?,?";
			
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, medicamento.getMedicamentoId());
			pstm.setString(2, medicamento.getNombre());
			pstm.setInt(3, medicamento.getEspecie().getEspecieId());
			pstm.setInt(4, medicamento.getTipoMedicamento().getTipoMedicamentoId());
			pstm.setInt(5, medicamento.getLaboratorio().getLaboratorioId());
			pstm.setString(6, medicamento.getDescripcion());
			pstm.setDouble(7, medicamento.getPrecio());
			
			editado = pstm.executeUpdate();
			System.out.println(pstm);
			
		} catch (SQLServerException e) {
			if(e.getMessage().contains("UK")){
				String msg = "Ya existe medicamento con mismo nombre y especie";
				message.showMessageRedirect(FacesMessage.SEVERITY_FATAL, "Error", msg);
				JOptionPane.showMessageDialog(null, "1: "+ e.getMessage());
			} else if(e.getMessage().contains("FK")){
				String msg = "No se olvide de seleccionar Especie, Laboratorio y Tipo Medicamento";
				message.showMessageRedirect(FacesMessage.SEVERITY_FATAL, "Error", msg);
				JOptionPane.showMessageDialog(null, "2: "+ e.getMessage());
			} else{
				message.showMessageRedirect(FacesMessage.SEVERITY_FATAL, "Error", "No se pudo registar");
				JOptionPane.showMessageDialog(null, "3: "+ e.getMessage());
			}
		} catch (Exception e) {
			message.showMessageRedirect(FacesMessage.SEVERITY_FATAL, "Error", e.getMessage());
		}
		return editado;
	}
	
	/*Listar Medicamentos*/
	public List<Medicamento> listar(){
		List<Medicamento> lista = new ArrayList<Medicamento>();
		try {
			conn = conexion.getConexion();
			String sql = 
					"select m.MedicamentoId, m.Nombre, e.Descripcion as DescripcionE, " +
					"l.Descripcion as DescripcionL, tm.Descripcion as DescripcionTM, " +
					"m.Descripcion, m.Precio, m.Stock, m.Estado from Medicamento m " +
					"inner join Especie e on m.EspecieId = e.EspecieId " +
					"inner join TipoMedicamento tm on m.TipoMedicamentoId = tm.TipoMedicamentoId " +
					"inner join Laboratorio l on m.LaboratorioId = l.LaboratorioId";
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();
			while(rs.next()){
				medicamento = new Medicamento();
				medicamento.setMedicamentoId(rs.getInt("MedicamentoId"));
				medicamento.setNombre(rs.getString("Nombre"));
				medicamento.getEspecie().setDescripcion(rs.getString("DescripcionE"));
				medicamento.getLaboratorio().setDescripcion(rs.getString("DescripcionL"));
				medicamento.getTipoMedicamento().setDescripcion(rs.getString("DescripcionTM"));
				medicamento.setDescripcion(rs.getString("Descripcion"));
				medicamento.setPrecio(rs.getDouble("Precio"));
				medicamento.setStock(rs.getInt("Stock"));
				medicamento.setEstado(rs.getByte("Estado"));
				lista.add(medicamento);
			}
		} catch (Exception e) {
			message.showMessage(FacesMessage.SEVERITY_FATAL, "Error", e.getMessage());
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return lista;
	}
	
	/*Buscar Medicamento*/
	public Medicamento buscar(int id){
		try {
			conn = conexion.getConexion();
			String sql = 
				"select m.MedicamentoId, m.Nombre, e.EspecieId, e.Descripcion as DescripcionE, " +
				"l.LaboratorioId, l.Descripcion as DescripcionL, " +
				"tm.TipoMedicamentoId, tm.Descripcion as DescripcionTM, " +
				"m.Descripcion, m.Precio, m.Stock, m.Estado from Medicamento m " +
				"inner join Especie e on m.EspecieId = e.EspecieId " +
				"inner join TipoMedicamento tm on m.TipoMedicamentoId = tm.TipoMedicamentoId " +
				"inner join Laboratorio l on m.LaboratorioId = l.LaboratorioId " +
				"where m.medicamentoid = ?";
			
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, id);
			rs = pstm.executeQuery();
			
			while(rs.next()){
				medicamento = new Medicamento();
				medicamento.setMedicamentoId(rs.getInt("MedicamentoId"));
				medicamento.setNombre(rs.getString("Nombre"));
				medicamento.getEspecie().setEspecieId(rs.getInt("EspecieId"));
				medicamento.getEspecie().setDescripcion(rs.getString("DescripcionE"));
				medicamento.getLaboratorio().setDescripcion(rs.getString("DescripcionL"));
				medicamento.getLaboratorio().setLaboratorioId(rs.getInt("LaboratorioId"));
				medicamento.getTipoMedicamento().setDescripcion(rs.getString("DescripcionTM"));
				medicamento.getTipoMedicamento().setTipoMedicamentoId(rs.getInt("TipoMedicamentoId"));
				medicamento.setDescripcion(rs.getString("Descripcion"));
				medicamento.setPrecio(rs.getDouble("Precio"));
				medicamento.setStock(rs.getInt("Stock"));
				medicamento.setEstado(rs.getByte("Estado"));
			}
		} catch (Exception e) {
			message.showMessage(FacesMessage.SEVERITY_FATAL, "Error", e.getMessage());
		}
		return medicamento;
	}
	
	/*Eliminar registro*/
	public int delete(Medicamento medicamento){
		int eliminado = 0;
		try {
			conn = conexion.getConexion();
			String sql = "delete medicamento where medicamentoid = ?";
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, medicamento.getMedicamentoId());
			
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
	
	/*Eliminar registro*/
	public int addstock(Medicamento medicamento){
		int agregado = 0;
		try {
			conn = conexion.getConexion();
			String sql = "exec SP_AgregarStock ?,?";
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, medicamento.getMedicamentoId());
			pstm.setInt(2, medicamento.getCantidad());
			
			agregado = pstm.executeUpdate();
			System.out.println(pstm);
			
		} catch (Exception e) {
			message.showMessageRedirect(FacesMessage.SEVERITY_FATAL, "Error", e.getMessage());
		}
		return agregado;
	}
	
	/*Listar Especies*/
	public List<Especie> listarEspecies(){
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
	
	/*Listar Laboratorios*/
	public List<Laboratorio> listarLaboratorios(){
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
	
	/*Listar Tipo Medicamentos*/
	public List<TipoMedicamento> listarTipoMedicamentos(){
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
	
}