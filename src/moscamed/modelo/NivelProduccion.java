package moscamed.modelo;

public class NivelProduccion {
    private double metaMoscasMillones;
    private boolean esVariable;
    private String descripcion;

    public NivelProduccion() {}

    public NivelProduccion(double meta, boolean esVariable) {
        this.metaMoscasMillones = meta;
        this.esVariable = esVariable;
        this.descripcion = (int) meta + " millones" + (esVariable ? " (variable)" : "");
    }

    public double getMetaMoscasMillones() { return metaMoscasMillones; }
    public void setMetaMoscasMillones(double meta) { this.metaMoscasMillones = meta; }

    public boolean isEsVariable() { return esVariable; }
    public void setEsVariable(boolean esVariable) { this.esVariable = esVariable; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() { return descripcion; }
}