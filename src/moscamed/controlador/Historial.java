package moscamed.controlador;

import java.io.*;
import java.util.ArrayList;
import moscamed.modelo.Escenario;
import moscamed.modelo.NivelProduccion;
import moscamed.modelo.Resultado;

public class Historial {
    private ArrayList<Escenario> escenarios;
    private String rutaArchivo = "datos/historial.txt";

    public Historial() {
        crearCarpetaDatos();
        this.escenarios = new ArrayList<>();
        cargarHistorial();
    }

    private void crearCarpetaDatos() {
        File carpeta = new File("datos");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
    }

    public void agregarEscenario(Escenario e) {
        escenarios.add(e);
        guardarEscenario(e);
    }

    public ArrayList<Escenario> listarEscenarios() {
        return escenarios;
    }

    public boolean eliminarEscenario(int id) {
        for (int i = 0; i < escenarios.size(); i++) {
            if (escenarios.get(i).getIdEscenario() == id) {
                escenarios.remove(i);
                reescribirArchivo();
                return true;
            }
        }
        return false;
    }

    public Escenario buscarPorId(int id) {
        for (Escenario e : escenarios) {
            if (e.getIdEscenario() == id) return e;
        }
        return null;
    }

    public int getSiguienteId() {
        int max = 0;
        for (Escenario e : escenarios) {
            if (e.getIdEscenario() > max) max = e.getIdEscenario();
        }
        return max + 1;
    }

    private void guardarEscenario(Escenario e) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(rutaArchivo, true))) {
            pw.println(e.getIdEscenario() + ";" +
                        e.getNivel().getMetaMoscasMillones() + ";" +
                        e.getResultado().getDieta1SemanalTon() + ";" +
                        e.getResultado().getHuevoDiarioLitros() + ";" +
                        e.getResultado().getPupaHembraDiariaLitros() + ";" +
                        e.getResultado().getPersonalRequerido());
        } catch (IOException ex) {
            System.err.println("Error al guardar historial: " + ex.getMessage());
        }
    }

    private void cargarHistorial() {
        File f = new File(rutaArchivo);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] p = linea.split(";");
                if (p.length == 6) {
                    int id = Integer.parseInt(p[0]);
                    double meta = Double.parseDouble(p[1]);
                    double dieta1 = Double.parseDouble(p[2]);
                    double huevo = Double.parseDouble(p[3]);
                    double pupaHembra = Double.parseDouble(p[4]);
                    int personal = Integer.parseInt(p[5]);

                    NivelProduccion nivel = new NivelProduccion(meta, false);

                    Resultado res = new Resultado();
                    res.setDieta1SemanalTon(dieta1);
                    res.setHuevoDiarioLitros(huevo);
                    res.setPupaHembraDiariaLitros(pupaHembra);
                    res.setPersonalRequerido(personal);

                    Escenario esc = new Escenario();
                    esc.setIdEscenario(id);
                    esc.setNombre("Escenario " + id);
                    esc.setNivel(nivel);
                    esc.setResultado(res);
                    escenarios.add(esc);
                }
            }
        } catch (IOException ex) {
            System.err.println("Error al cargar historial: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            System.err.println("Error al parsear historial: " + ex.getMessage());
        }
    }

    private void reescribirArchivo() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(rutaArchivo))) {
            for (Escenario e : escenarios) {
                pw.println(e.getIdEscenario() + ";" +
                            e.getNivel().getMetaMoscasMillones() + ";" +
                            e.getResultado().getDieta1SemanalTon() + ";" +
                            e.getResultado().getHuevoDiarioLitros() + ";" +
                            e.getResultado().getPupaHembraDiariaLitros() + ";" +
                            e.getResultado().getPersonalRequerido());
            }
        } catch (IOException ex) {
            System.err.println("Error al reescribir historial: " + ex.getMessage());
        }
    }

    public void limpiarHistorial() {
        escenarios.clear();
        try (PrintWriter pw = new PrintWriter(rutaArchivo)) {
            pw.print("");
        } catch (IOException ex) {
            System.err.println("Error al limpiar: " + ex.getMessage());
        }
    }
}