package ABMCalumnos;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

/**
 *
 * @author Pablo Acevedo - pablo@xeven.com.ar
 */
public class ABMCalumnos {
    private static final Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        String op;
        while(1==1){
            System.out.println("Sistema ABC (Altas Bajas Consultas) de alumnos");
            System.out.println("1) Buscar y cargar alumnos desde archivo.");
            System.out.println("2) Buscar y eliminar alumnos.");
            System.out.println("3) Buscar y mostrar alumnos.");
            System.out.println("4) Buscar y actualizar alumnos.");
            System.out.println("5) Salir.");
            System.out.print("\n Elija una opción: ");
            op = sc.nextLine(); 
            switch(op){
                case "1":
                    buscarInsertar();
                    break;
                case "2":
                    buscarEliminar();
                    break;
                case "3":
                    buscarMostrar();
                    break;
                case "4":
                    buscarActualizar();
                    break;
                default:
                    System.exit(0);
            }
        }
    }
    public static void buscarInsertar(){
        LinkedHashMap<String, Alumno> mapa = new LinkedHashMap();
        try {
            FileReader fr = new FileReader(Alumno.class.getResource("alumnos.csv").getPath());
            BufferedReader reader = new BufferedReader(fr);
            String linea=reader.readLine();//lee los encabezados de columna (no los usamos aca)
            linea=reader.readLine();// esta es la primer fila con datos reales
            while(linea != null){
                Alumno alu = new Alumno(linea);
                mapa.put(alu.getLegajo(), alu);
                linea = reader.readLine();// leo la siguiente linea
            }
            reader.close();
        }catch(FileNotFoundException e) {
            System.out.println("no se encontró el fichero");
        }catch(IOException e) {
            System.out.println("algo fue mal al leer o cerrar el fichero");
        }
        String query = buscar();
        ArrayList<Alumno> losAlumnos = new ArrayList();
        for(Alumno alumno: mapa.values()){
            if(alumno.toString().toLowerCase().contains(query)){
                System.out.println("La persona buscada es: "+alumno.getNombre()+" ("+alumno.getLegajo()+")");
                System.out.print("¿Está seguro que desea insertarlo en la db? (y/n) ");
                if(sc.nextLine().toLowerCase().equals("y"))
                    losAlumnos.add(alumno);
            }
        }
        for(Alumno alumno: losAlumnos)
            alumno.insertar();
    }

    public static void buscarEliminar() {
        String query = buscar();
        ArrayList<Alumno> losAlumnos = Alumno.buscar(query);
        for(Alumno alumno: losAlumnos){
            alumno.mostrar();
            System.out.print("¿Está seguro que desea eliminarlo de la db? (y/n) ");
            if(sc.nextLine().toLowerCase().equals("y"))
                alumno.eliminar();
        }
    }

    public static void buscarMostrar() {
        String query = buscar();
        ArrayList<Alumno> losAlumnos = Alumno.buscar(query);
        losAlumnos.forEach((alumno) -> alumno.mostrar());
    }
    public static void buscarActualizar() {
        String query = buscar();
        ArrayList<Alumno> losAlumnos = Alumno.buscar(query);
        losAlumnos.forEach((alumno) -> {
            alumno.mostrar();
            System.out.println("Ingrese los nuevos valores: ");
            System.out.print("Nombre: ");
            String nombre = sc.nextLine();
            System.out.print("DNI: ");
            String dni = sc.nextLine();
            System.out.print("Mail: ");
            String mail = sc.nextLine();
            alumno.setNombre(nombre);
            alumno.setDni(dni);
            alumno.setMail(mail);
            System.out.println("Se guardarán los siguientes datos:");
            System.out.println(nombre+", DNI: "+dni+", Mail: "+mail);
            System.out.print("¿Está seguro que desea actualizarlo en la db? (y/n) ");
            if(sc.nextLine().toLowerCase().equals("y"))
                alumno.actualizar();
        });
    }
    public static String buscar(){
        System.out.print("Buscar alumno: ");
        return sc.nextLine().toLowerCase();
    }
}
