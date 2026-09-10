package moscamed.modelo;

import java.util.ArrayList;
import java.util.Date;

public class Escenario {
    private int idEscenario;
    private String nombre;
    private Date fechaHora;
    private NivelProduccion nivel;
    private ArrayList<Constante> constantesUsadas;
    private Resultado resultado;

    public Escenario() {
        this.fechaHora = new Date();
        this.constantesUsadas = new ArrayList<>();
    }

    public Escenario(int id, NivelProduccion nivel, ArrayList<Constante> constantes, Resultado resultado) {
        this.idEscenario = id;
        this.nombre = "Escenario " + id;
        this.fechaHora = new Date();
        this.nivel = nivel;
        this.constantesUsadas = new ArrayList<>(constantes);
        this.resultado = resultado;
    }

    public int getIdEscenario() { return idEscenario; }
    public void setIdEscenario(int id) { this.idEscenario = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Date getFechaHora() { return fechaHora; }
    public void setFechaHora(Date fechaHora) { this.fechaHora = fechaHora; }
    public NivelProduccion getNivel() { return nivel; }
    public void setNivel(NivelProduccion nivel) { this.nivel = nivel; }
    public ArrayList<Constante> getConstantesUsadas() { return constantesUsadas; }
    public void setConstantesUsadas(ArrayList<Constante> c) { this.constantesUsadas = c; }
    public Resultado getResultado() { return resultado; }
    public void setResultado(Resultado resultado) { this.resultado = resultado; }
}