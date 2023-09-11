package Clases;

import Formularios.frmCarro;
import Formularios.frmCarta;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import javax.swing.JOptionPane;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
public class Conectar {
    Connection conectar=null;
    
    String usuario ="anthony";
    String contrasenia ="123";
    String bd="KFC";
    String ip="localhost";
    String puerto="1433";
   public static  String precio;
   public static  String cantidad;
   public static  String total;
   public static  String compra;
   public static  String fin;
   public static  String desc;
   public static  String Usuario;
   public static  String Producto;
    String cadena = "jdbc:sqlserver://"+ip+":"+puerto+"/"+bd;

    public   Connection establecerConexion(){
        try{
           String cadena="jdbc:sqlserver://localhost:"+puerto+";"+"databaseName="+bd;
            conectar= DriverManager.getConnection(cadena,usuario,contrasenia);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error al conectarse a la base de datos, error"+e.toString());
        }
        return conectar;
    }
    public int ValidarUsuario(String usuario, String contrasenia)
           
   {
       conectar= establecerConexion();

       String consulta="Select usu_nombre, usu_contra from tbl_usuario where usu_nombre='"+usuario+"' and usu_contra='"+contrasenia+"'";
       
       
       try {
           Statement st = conectar.createStatement();
          ResultSet rs = st.executeQuery(consulta);
          
          
          if(rs.next()==true)
          {
          return 1;
          }
          else
              {
              return 0;
              }
           
       } catch (Exception e) {
       }
  
     return 0;
               
   }
    public void validarEdad(JTextField a){
        a.addKeyListener(new KeyAdapter(){
            public void keyTyped(KeyEvent e){
                char c=e.getKeyChar();
                if(!Character.isDigit(c)){
                    e.consume();
                }
            }
        });
    }
    public void OptenerIdUsu(String usuario, String contrasenia){
        conectar= establecerConexion();
        String consulta="SELECT id_usu from tbl_usuario where usu_nombre='"+usuario+"' and usu_contra='"+contrasenia+"'";
        try{
            PreparedStatement ps;
            Statement st = conectar.createStatement();
            ResultSet rs2 = st.executeQuery(consulta);
            while(rs2.next()==true){
                Usuario=(rs2.getString(1));
            }
        }catch(Exception e){}
        
    };
    public void OptenerIdPro( JComboBox cbxPlato){
        conectar= establecerConexion();
        String consulta="SELECT id_pro from tbl_producto where pro_plato='"+cbxPlato.getSelectedItem()+"'";
        try{
            PreparedStatement ps;
            Statement st = conectar.createStatement();
            ResultSet rs2 = st.executeQuery(consulta);
            while(rs2.next()==true){
                Producto=(rs2.getString(1));
            }
        }catch(Exception e){}
        
    };
    public void RegistroCliente(String nombre,String contra,String correo){
        conectar= establecerConexion();
        String consulta="Insert into tbl_usuario(usu_nombre,usu_contra,correo) values ('"+nombre+"','"+contra+"','"+correo+"')";
        try {
           PreparedStatement ps;
           ps=conectar.prepareStatement(consulta);
           ps.execute();
           JOptionPane.showMessageDialog(null, "Usuario registrado");
           
       } catch (Exception e) {
            
        }
    
    }
    public void ConectarCategorias(JComboBox cbxCat){
            conectar= establecerConexion();
            String consulta;
        consulta = "Select cat_categoria from tbl_categoria";
        try{
            PreparedStatement ps;
            Statement st = conectar.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            while(rs.next()==true){
                cbxCat.addItem(rs.getString(1));
            }
        }catch(Exception e){}
    }
    public void ConectarPlatos(JComboBox cbxCate,JComboBox cbxPlato){
            conectar= establecerConexion();
            String consulta;
            consulta = "Select p.pro_plato,p.pro_precio from tbl_producto as p inner join tbl_categoria as c on p.id_cat=c.id_cat where cat_categoria='"+cbxPlato.getSelectedItem().toString()+"'";
            try{
            PreparedStatement ps;
            Statement st = conectar.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            
            while(rs.next()==true){
                cbxCate.addItem(rs.getString(1));
            }
            
        }catch(Exception e){}
    }
    public  void ConectarPrecios(JComboBox tipoPlato){
            conectar= establecerConexion();
            String consulta;
        consulta = "Select pro_precio from tbl_producto where pro_plato ='"+tipoPlato.getSelectedItem()+"'";
        try{
            PreparedStatement ps;
            Statement st = conectar.createStatement();
            ResultSet rs2 = st.executeQuery(consulta);
            while(rs2.next()==true){
                precio=(rs2.getString(1));
            }
        }catch(Exception e){}
    }
    public  void MostrarDesc(JComboBox tipoPlato){
            conectar= establecerConexion();
            String consulta;
        consulta = "Select pro_descripcion from tbl_producto where pro_plato ='"+tipoPlato.getSelectedItem()+"'";
        try{
            PreparedStatement ps;
            Statement st = conectar.createStatement();
            ResultSet rs2 = st.executeQuery(consulta);
            while(rs2.next()==true){
                desc=(rs2.getString(1));
            }
        }catch(Exception e){}
    } 
    public  void AgregarPedido(){
            conectar= establecerConexion();
            String consulta;
            consulta="Insert into tbl_pedido(id_pro,id_usu) values ('"+Integer.parseInt(Producto)+"','"+Integer.parseInt(Usuario)+"')";
        try{
            PreparedStatement ps;
            Statement st = conectar.createStatement();
            ResultSet rs2 = st.executeQuery(consulta);
            while(rs2.next()==true){
                desc=(rs2.getString(1));
            }
            Producto="";
        }catch(Exception e){}
    }
    public void CargarProductos(JTable tabla){
        DefaultTableModel modelo=(DefaultTableModel)tabla.getModel();
        modelo.setRowCount(0);
        conectar= establecerConexion();
            String consulta;
        consulta = "Select ped.id_ped,u.usu_nombre,pro_plato,p.pro_precio from tbl_pedido as ped\n" +
        "inner join tbl_producto as p on p.id_pro=ped.id_pro\n" +
        "inner join tbl_usuario as u on u.id_usu = ped.id_usu \n" +
        "where u.id_usu="+Usuario+"";
        try{
            PreparedStatement ps;
            Statement st = conectar.createStatement();
            ResultSet rs2 = st.executeQuery(consulta);
            while(rs2.next()==true){
                Vector v= new Vector();
                v.add(rs2.getString(1));
                v.add(rs2.getString(2));
                v.add(rs2.getString(3));
                v.add(rs2.getString(4));
                modelo.addRow(v);
                tabla.setModel(modelo);
                
            }
        }catch(Exception e){}
    }
    public  void MostrarTotal(JTable tabla){
        CargarProductos(tabla);
        int contar =tabla.getRowCount();
        float suma=0;
       
        for(int i=0;i<contar;i++){
            suma=suma+Float.parseFloat(tabla.getValueAt(i, 3).toString());
            
        }
        total=String.valueOf(suma);
        
    }
    public void Eliminar(JTable tabla){
        conectar= establecerConexion();
        int fila =tabla.getSelectedRow();
        String valor=tabla.getValueAt(fila, 0).toString();
        String cosulta ="Delete from tbl_pedido where id_ped='"+valor+"'";
        CargarProductos(tabla);
        try {
            PreparedStatement elimi = conectar.prepareStatement(cosulta);
            elimi.executeUpdate();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"No se elimino el registro");
        }
    }
    public  void Finalizar(){
            conectar= establecerConexion();
            String consulta;
            consulta="Delete from tbl_pedido where id_usu='"+Integer.parseInt(Usuario)+"'";
        try{
            PreparedStatement ps;
            Statement st = conectar.createStatement();
            ResultSet rs2 = st.executeQuery(consulta);
            while(rs2.next()==true){
                fin=(rs2.getString(1));
            }
        }catch(Exception e){}
    }
    public  void cantidaCarro(){
            conectar= establecerConexion();
            String consulta;
            consulta="Select count(pro_id) from tbl_pruducto";
        try{
            PreparedStatement ps;
            Statement st = conectar.createStatement();
            ResultSet rs2 = st.executeQuery(consulta);
            while(rs2.next()==true){
                cantidad=(rs2.getString(1));
            }
            
        }catch(Exception e){}
    }
}
