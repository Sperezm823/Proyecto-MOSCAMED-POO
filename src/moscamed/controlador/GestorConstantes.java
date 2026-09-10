package moscamed.controlador;

import java.io.File;
import java.util.ArrayList;
import moscamed.modelo.Constante;
import moscamed.util.ArchivoUtil;

public class GestorConstantes {
    private ArrayList<Constante> constantes;
    private String rutaArchivo = "datos/constantes.txt";
    private ArrayList<Constante> valoresPorDefecto;

    public GestorConstantes() {
        crearCarpetaDatos();
        this.constantes = new ArrayList<>();
        this.valoresPorDefecto = new ArrayList<>();
        cargarPorDefecto();
        cargarDesdeArchivo();
    }

    private void crearCarpetaDatos() {
        File carpeta = new File("datos");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
    }

    private void cargarPorDefecto() {
        valoresPorDefecto.add(new Constante(1, "INSUMOS", "millones/ton dieta", 10, "millones/ton"));
        valoresPorDefecto.add(new Constante(2, "INSUMOS", "litros huevo/ton", 1.25, "L/ton"));
        valoresPorDefecto.add(new Constante(3, "INSUMOS", "litros huevo/jaula", 4, "L/jaula"));
        valoresPorDefecto.add(new Constante(4, "INSUMOS", "l pupa hembra/jaula", 10, "L/jaula"));
        valoresPorDefecto.add(new Constante(5, "INSUMOS", "hembras/macho", 4, "hembras/macho"));
        valoresPorDefecto.add(new Constante(6, "INSUMOS", "l hembras/ton", 30, "L/ton"));
        valoresPorDefecto.add(new Constante(7, "EQUIPOS", "Bandejas dieta/ton", 200, "bandejas/ton"));
        valoresPorDefecto.add(new Constante(8, "EQUIPOS", "Ciclo dieta 1 (días)", 9, "días"));
        valoresPorDefecto.add(new Constante(9, "EQUIPOS", "Ciclo dieta 2 (días)", 14, "días"));
        valoresPorDefecto.add(new Constante(10, "EQUIPOS", "Kg/bandeja", 5, "kg/bandeja"));
        valoresPorDefecto.add(new Constante(11, "EQUIPOS", "L/millón", 16, "L/millón"));
        valoresPorDefecto.add(new Constante(12, "EQUIPOS", "L/criba", 2, "L/criba"));
        valoresPorDefecto.add(new Constante(13, "PERSONAL", "millones/trabajador", 2.5, "millones/trabajador"));
    }

    public void cargarDesdeArchivo() {
        ArrayList<Constante> cargadas = ArchivoUtil.cargarConstantes(rutaArchivo);
        if (cargadas.isEmpty()) {
            constantes = new ArrayList<>(valoresPorDefecto);
            guardar();
        } else {
            constantes = cargadas;
        }
    }

    public void agregarConstante(Constante c) {
        constantes.add(c);
        guardar();
    }

    public boolean modificarConstante(int id, double nuevoValor) {
        for (Constante c : constantes) {
            if (c.getId() == id) {
                c.setValor(nuevoValor);
                guardar();
                return true;
            }
        }
        return false;
    }

    public boolean eliminarConstante(int id) {
        for (int i = 0; i < constantes.size(); i++) {
            if (constantes.get(i).getId() == id) {
                constantes.remove(i);
                guardar();
                return true;
            }
        }
        return false;
    }

    public ArrayList<Constante> listarConstantes() {
        return constantes;
    }

    public ArrayList<Constante> listarPorCategoria(String categoria) {
        ArrayList<Constante> res = new ArrayList<>();
        for (Constante c : constantes) {
            if (c.getCategoria().equalsIgnoreCase(categoria)) res.add(c);
        }
        return res;
    }

    public void restablecerConstantes() {
        constantes = new ArrayList<>();
        for (Constante c : valoresPorDefecto) {
            constantes.add(new Constante(c.getId(), c.getCategoria(), c.getNombre(), c.getValor(), c.getUnidad()));
        }
        guardar();
    }

    public void guardar() {
        ArchivoUtil.guardarConstantes(constantes, rutaArchivo);
    }

    public ArrayList<Constante> getConstantes() {
        return constantes;
    }
}