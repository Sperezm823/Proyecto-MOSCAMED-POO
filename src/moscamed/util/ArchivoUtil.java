package moscamed.util;

import java.io.*;
import java.util.ArrayList;
import moscamed.modelo.Constante;

public class ArchivoUtil {

    private static void crearCarpetaSiNoExiste(String ruta) {
        File archivo = new File(ruta);
        File carpeta = archivo.getParentFile();
        if (carpeta != null && !carpeta.exists()) {
            carpeta.mkdirs();
        }
    }

    public static void guardarConstantes(ArrayList<Constante> constantes, String ruta) {
        crearCarpetaSiNoExiste(ruta);
        try (PrintWriter pw = new PrintWriter(new FileWriter(ruta))) {
            for (Constante c : constantes) {
                pw.println(c.getId() + ";" + c.getCategoria() + ";" + c.getNombre() + ";" + c.getValor() + ";" + c.getUnidad());
            }
        } catch (IOException e) {
            System.err.println("Error al guardar constantes: " + e.getMessage());
        }
    }

    public static ArrayList<Constante> cargarConstantes(String ruta) {
        ArrayList<Constante> lista = new ArrayList<>();
        File f = new File(ruta);
        if (!f.exists()) return lista;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] p = linea.split(";");
                if (p.length == 5) {
                    lista.add(new Constante(Integer.parseInt(p[0]), p[1], p[2], Double.parseDouble(p[3]), p[4]));
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar constantes: " + e.getMessage());
        }
        return lista;
    }

    public static void guardarLinea(String ruta, String linea) {
        crearCarpetaSiNoExiste(ruta);
        try (PrintWriter pw = new PrintWriter(new FileWriter(ruta, true))) {
            pw.println(linea);
        } catch (IOException e) {
            System.err.println("Error al escribir: " + e.getMessage());
        }
    }
}