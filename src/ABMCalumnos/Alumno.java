package ABMCalumnos;

import ar.com.xeven.utils.XEVEN;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pablo Acevedo - pablo@xeven.com.ar
 */
public class Alumno {
    private String legajo;
    private String nombre;
    private String dni;
    private String mail;

    public Alumno(String legajo, String nombre, String dni, String mail) {
        this.legajo = legajo;
        this.nombre = nombre;
        this.dni = dni;
        this.mail = mail;
    }

    Alumno(String linea) {
        int comas = linea.length() - linea.replace(",", "").length();
        String[] campos = (comas==3||comas==4)?linea.split(","):linea.split(";");//separo por , o ; segun la version de CSV usada
        this.legajo = campos[0];
        this.dni = campos[1];
        this.nombre = campos[2];
        this.mail = campos[3];
    }
    
    public void insertar(){
        if(yaExiste()){
            actualizar();
            return;
        }
        Connection c = XEVEN.getConnection();
        PreparedStatement pstmt = null;
        String sql = "INSERT INTO alumnos (legajo, nombre, dni, mail) VALUES(?,?,?,?);";
        try {
            pstmt = c.prepareStatement(sql);
            pstmt.setString(1, this.getLegajo());
            pstmt.setString(2, this.getNombre());
            pstmt.setString(3, this.getDni());
            pstmt.setString(4, this.getMail());
            pstmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(Alumno.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try { pstmt.close(); } catch (SQLException ex) {}
        }
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getLegajo() {
        return legajo;
    }

    public void setLegajo(String legajo) {
        this.legajo = legajo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }
    @Override
    public String toString() {
        return legajo + "*" + nombre + "*" + dni + "*" + mail;
    }
    public void mostrar(){
        System.out.println("Alumno: "+this.nombre+" ("+this.legajo+")");
    }
    public static ArrayList<Alumno> buscar(String query) {
        ArrayList<Alumno> alumnos = new ArrayList();
        Connection c = XEVEN.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM alumnos WHERE "+
                "legajo LIKE '%"+query+"%' OR "+
                "nombre LIKE '%"+query+"%' OR "+
                "dni LIKE '%"+query+"%' OR "+
                "mail LIKE '%"+query+"%';";
        try {
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next())
                alumnos.add(new Alumno(
                    rs.getString("legajo"),
                    rs.getString("nombre"),
                    rs.getString("dni"),
                    rs.getString("mail")
            ));
        } catch (SQLException ex) {
            Logger.getLogger(Alumno.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try { stmt.close(); } catch (SQLException ex) {}
            try { rs.close(); } catch (SQLException ex) {}
        }
        return alumnos;
    }
    private boolean yaExiste() {
        boolean existe = false;
        Connection c = XEVEN.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT id FROM alumnos WHERE legajo='"+this.legajo+"';";
        try {
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            if(rs.next())
                existe=true;
        } catch (SQLException ex) {
            Logger.getLogger(Alumno.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try { stmt.close(); } catch (SQLException ex) {}
            try { rs.close(); } catch (SQLException ex) {}
        }
        return existe;
    }

    public void actualizar() {
        Connection c = XEVEN.getConnection();
        Statement stmt = null;
        String sql = "UPDATE alumnos SET legajo='"+this.legajo+"', nombre='"+this.nombre+"', dni='"+this.dni+"', mail='"+this.mail+"'  WHERE legajo='"+this.legajo+"';";
        try {
            stmt = c.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(Alumno.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try { stmt.close(); } catch (SQLException ex) {}
        }
    }
    public void eliminar() {
        Connection c = XEVEN.getConnection();
        Statement stmt = null;
        String sql = "DELETE FROM alumnos WHERE legajo='"+this.legajo+"';";
        try {
            stmt = c.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(Alumno.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try { stmt.close(); } catch (SQLException ex) {}
        }
    }
}
