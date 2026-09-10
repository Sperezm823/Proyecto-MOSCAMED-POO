package moscamed.controlador;

import java.io.*;
import java.util.ArrayList;
import moscamed.modelo.Constante;
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
            pw.println(serializar(e));
        } catch (IOException ex) {
            System.err.println("Error al guardar historial: " + ex.getMessage());
        }
    }

    private String serializar(Escenario e) {
        StringBuilder sb = new StringBuilder();
        sb.append(e.getIdEscenario()).append(";");
        sb.append(e.getNivel().getMetaMoscasMillones()).append(";");
        sb.append(e.getFechaHora().getTime()).append(";");

        StringBuilder consts = new StringBuilder();
        ArrayList<Constante> cs = e.getConstantesUsadas();
        for (int i = 0; i < cs.size(); i++) {
            Constante c = cs.get(i);
            if (i > 0) consts.append("#");
            consts.append(c.getCategoria()).append("@")
                 .append(c.getNombre()).append("=")
                 .append(c.getValor());
        }
        sb.append(consts.toString()).append(";");

        Resultado r = e.getResultado();
        sb.append(r.getDieta1SemanalTon()).append(";");
        sb.append(r.getDieta1DiariaTon()).append(";");
        sb.append(r.getHuevoDiarioLitros()).append(";");
        sb.append(r.getPupaHembraDiariaLitros()).append(";");
        sb.append(r.getPupaMachoDiariaLitros()).append(";");
        sb.append(r.getDieta2DiariaTon()).append(";");
        sb.append(r.getDietaTotalDiariaTon()).append(";");
        sb.append(r.getBandejasDieta1()).append(";");
        sb.append(r.getBandejasDieta2()).append(";");
        sb.append(r.getTotalBandejas()).append(";");
        sb.append(r.getAnaquelesDieta()).append(";");
        sb.append(r.getCribasPupa()).append(";");
        sb.append(r.getCribasMacho()).append(";");
        sb.append(r.getTotalCribas()).append(";");
        sb.append(r.getAnaquelesCribas()).append(";");
        sb.append(r.getPersonalRequerido());
        return sb.toString();
    }

    private void cargarHistorial() {
        File f = new File(rutaArchivo);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] p = linea.split(";", -1);
                if (p.length < 20) continue;

                try {
                    int id = Integer.parseInt(p[0]);
                    double meta = Double.parseDouble(p[1]);
                    long fecha = Long.parseLong(p[2]);

                    ArrayList<Constante> consts = new ArrayList<>();
                    if (!p[3].isEmpty()) {
                        String[] items = p[3].split("#");
                        for (int i = 0; i < items.length; i++) {
                            String[] kv = items[i].split("=");
                            if (kv.length == 2) {
                                String[] cat = kv[0].split("@");
                                String categoria = cat.length > 0 ? cat[0] : "";
                                String nombre = cat.length > 1 ? cat[1] : kv[0];
                                consts.add(new Constante(i + 1, categoria, nombre,
                                        Double.parseDouble(kv[1]), ""));
                            }
                        }
                    }

                    NivelProduccion nivel = new NivelProduccion(meta, false);

                    Resultado r = new Resultado();
                    r.setDieta1SemanalTon(Double.parseDouble(p[4]));
                    r.setDieta1DiariaTon(Double.parseDouble(p[5]));
                    r.setHuevoDiarioLitros(Double.parseDouble(p[6]));
                    r.setPupaHembraDiariaLitros(Double.parseDouble(p[7]));
                    r.setPupaMachoDiariaLitros(Double.parseDouble(p[8]));
                    r.setDieta2DiariaTon(Double.parseDouble(p[9]));
                    r.setDietaTotalDiariaTon(Double.parseDouble(p[10]));
                    r.setBandejasDieta1(Double.parseDouble(p[11]));
                    r.setBandejasDieta2(Double.parseDouble(p[12]));
                    r.setTotalBandejas(Double.parseDouble(p[13]));
                    r.setAnaquelesDieta(Double.parseDouble(p[14]));
                    r.setCribasPupa(Double.parseDouble(p[15]));
                    r.setCribasMacho(Double.parseDouble(p[16]));
                    r.setTotalCribas(Double.parseDouble(p[17]));
                    r.setAnaquelesCribas(Double.parseDouble(p[18]));
                    r.setPersonalRequerido(Integer.parseInt(p[19]));

                    Escenario esc = new Escenario();
                    esc.setIdEscenario(id);
                    esc.setNombre("Escenario " + id);
                    esc.setFechaHora(new java.util.Date(fecha));
                    esc.setNivel(nivel);
                    esc.setConstantesUsadas(consts);
                    esc.setResultado(r);

                    escenarios.add(esc);
                } catch (NumberFormatException ex) {
                    System.err.println("Error al parsear línea: " + ex.getMessage());
                }
            }
        } catch (IOException ex) {
            System.err.println("Error al cargar historial: " + ex.getMessage());
        }
    }

    private void reescribirArchivo() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(rutaArchivo))) {
            for (Escenario e : escenarios) {
                pw.println(serializar(e));
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