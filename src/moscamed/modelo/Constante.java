package moscamed.modelo;

public class Constante {
    private int id;
    private String categoria;
    private String nombre;
    private double valor;
    private String unidad;

    public Constante() {}

    public Constante(int id, String categoria, String nombre, double valor, String unidad) {
        this.id = id;
        this.categoria = categoria;
        this.nombre = nombre;
        this.valor = valor;
        this.unidad = unidad;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public String getUnidad() { return unidad; }
    public void setUnidad(String unidad) { this.unidad = unidad; }

    public boolean validar() {
        return valor > 0;
    }

    @Override
    public String toString() {
        return categoria + " | " + nombre + " = " + valor + " " + unidad;
    }
}   