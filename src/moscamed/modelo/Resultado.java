package moscamed.modelo;

import java.util.ArrayList;

public class Resultado {
    private double dieta1SemanalTon;
    private double dieta1DiariaTon;
    private double huevoDiarioLitros;
    private double pupaHembraDiariaLitros;
    private double pupaMachoDiariaLitros;
    private double dieta2DiariaTon;
    private double dietaTotalDiariaTon;
    private double bandejasDieta1;
    private double bandejasDieta2;
    private double totalBandejas;
    private double anaquelesDieta;
    private double cribasPupa;
    private double cribasMacho;
    private double totalCribas;
    private double anaquelesCribas;
    private int personalRequerido;
    private String fechaCalculo;

    public Resultado() {}

    public void calcular(NivelProduccion nivel, ArrayList<Constante> constantes) {
        double millonesTonDieta = getValor(constantes, "millones/ton dieta");
        double litrosHuevoTon = getValor(constantes, "litros huevo/ton");
        double litrosHuevoJaula = getValor(constantes, "litros huevo/jaula");
        double pupaHembraJaula = getValor(constantes, "l pupa hembra/jaula");
        double hembrasMacho = getValor(constantes, "hembras/macho");
        double hembrasTon = getValor(constantes, "l hembras/ton");
        double cicloDieta1 = getValor(constantes, "Ciclo dieta 1 (días)");
        double cicloDieta2 = getValor(constantes, "Ciclo dieta 2 (días)");
        double kgBandeja = getValor(constantes, "Kg/bandeja");
        double lMillon = getValor(constantes, "L/millón");
        double lCriba = getValor(constantes, "L/criba");
        double millonesTrabajador = getValor(constantes, "millones/trabajador");

        double nivelMillones = nivel.getMetaMoscasMillones();

        dieta1SemanalTon = nivelMillones / millonesTonDieta;
        dieta1DiariaTon = dieta1SemanalTon / 7;
        huevoDiarioLitros = dieta1DiariaTon * litrosHuevoTon;
        double jaulasDiarias = huevoDiarioLitros / litrosHuevoJaula;
        pupaHembraDiariaLitros = jaulasDiarias * pupaHembraJaula;
        pupaMachoDiariaLitros = pupaHembraDiariaLitros / hembrasMacho;
        dieta2DiariaTon = pupaHembraDiariaLitros / hembrasTon;
        dietaTotalDiariaTon = dieta1DiariaTon + dieta2DiariaTon;

        bandejasDieta1 = dieta1DiariaTon * cicloDieta1 * 1000 / kgBandeja;
        bandejasDieta2 = dieta2DiariaTon * cicloDieta2 * 1000 / kgBandeja;
        totalBandejas = bandejasDieta1 + bandejasDieta2;
        anaquelesDieta = totalBandejas / 60;

        cribasPupa = nivelMillones * lMillon / lCriba;
        cribasMacho = (pupaHembraDiariaLitros + pupaMachoDiariaLitros) * lMillon / lCriba;
        totalCribas = cribasPupa + cribasMacho;
        anaquelesCribas = totalCribas / 40;

        personalRequerido = (int) Math.ceil(nivelMillones / millonesTrabajador);
        fechaCalculo = new java.util.Date().toString();
    }

    private double getValor(ArrayList<Constante> constantes, String nombre) {
        for (Constante c : constantes) {
            if (c.getNombre().equalsIgnoreCase(nombre)) return c.getValor();
        }
        return 0;
    }

    public String generarResumen() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== INSUMOS ===\n");
        sb.append("Dieta 1 (semanal): ").append(String.format("%.2f", dieta1SemanalTon)).append(" Ton/semana\n");
        sb.append("Dieta 1 (diaria): ").append(String.format("%.4f", dieta1DiariaTon)).append(" Ton/día\n");
        sb.append("Huevo diario: ").append(String.format("%.4f", huevoDiarioLitros)).append(" L/día\n");
        sb.append("Pupa hembra diaria: ").append(String.format("%.4f", pupaHembraDiariaLitros)).append(" L/día\n");
        sb.append("Pupa macho diaria: ").append(String.format("%.4f", pupaMachoDiariaLitros)).append(" L/día\n");
        sb.append("Dieta Total diaria: ").append(String.format("%.4f", dietaTotalDiariaTon)).append(" Ton/día\n");
        sb.append("\n=== EQUIPOS ===\n");
        sb.append("Bandejas Dieta 1: ").append(String.format("%.2f", bandejasDieta1)).append(" bandejas/ciclo\n");
        sb.append("Bandejas Dieta 2: ").append(String.format("%.2f", bandejasDieta2)).append(" bandejas/ciclo\n");
        sb.append("Total Bandejas: ").append(String.format("%.2f", totalBandejas)).append(" bandejas/ciclo\n");
        sb.append("Anaqueles dieta: ").append(String.format("%.2f", anaquelesDieta)).append(" anaqueles\n");
        sb.append("Cribas Pupa: ").append(String.format("%.2f", cribasPupa)).append(" cribas/ciclo\n");
        sb.append("Cribas Macho: ").append(String.format("%.2f", cribasMacho)).append(" cribas/ciclo\n");
        sb.append("Total Cribas: ").append(String.format("%.2f", totalCribas)).append(" cribas\n");
        sb.append("Anaqueles cribas: ").append(String.format("%.2f", anaquelesCribas)).append(" anaqueles\n");
        sb.append("\n=== PERSONAL ===\n");
        sb.append("Trabajadores requeridos: ").append(personalRequerido).append(" trabajadores\n");
        return sb.toString();
    }

    public double getDieta1SemanalTon() { return dieta1SemanalTon; }
    public void setDieta1SemanalTon(double v) { this.dieta1SemanalTon = v; }
    public double getDieta1DiariaTon() { return dieta1DiariaTon; }
    public void setDieta1DiariaTon(double v) { this.dieta1DiariaTon = v; }
    public double getHuevoDiarioLitros() { return huevoDiarioLitros; }
    public void setHuevoDiarioLitros(double v) { this.huevoDiarioLitros = v; }
    public double getPupaHembraDiariaLitros() { return pupaHembraDiariaLitros; }
    public void setPupaHembraDiariaLitros(double v) { this.pupaHembraDiariaLitros = v; }
    public double getPupaMachoDiariaLitros() { return pupaMachoDiariaLitros; }
    public void setPupaMachoDiariaLitros(double v) { this.pupaMachoDiariaLitros = v; }
    public double getDieta2DiariaTon() { return dieta2DiariaTon; }
    public void setDieta2DiariaTon(double v) { this.dieta2DiariaTon = v; }
    public double getDietaTotalDiariaTon() { return dietaTotalDiariaTon; }
    public void setDietaTotalDiariaTon(double v) { this.dietaTotalDiariaTon = v; }
    public double getBandejasDieta1() { return bandejasDieta1; }
    public void setBandejasDieta1(double v) { this.bandejasDieta1 = v; }
    public double getBandejasDieta2() { return bandejasDieta2; }
    public void setBandejasDieta2(double v) { this.bandejasDieta2 = v; }
    public double getTotalBandejas() { return totalBandejas; }
    public void setTotalBandejas(double v) { this.totalBandejas = v; }
    public double getAnaquelesDieta() { return anaquelesDieta; }
    public void setAnaquelesDieta(double v) { this.anaquelesDieta = v; }
    public double getCribasPupa() { return cribasPupa; }
    public void setCribasPupa(double v) { this.cribasPupa = v; }
    public double getCribasMacho() { return cribasMacho; }
    public void setCribasMacho(double v) { this.cribasMacho = v; }
    public double getTotalCribas() { return totalCribas; }
    public void setTotalCribas(double v) { this.totalCribas = v; }
    public double getAnaquelesCribas() { return anaquelesCribas; }
    public void setAnaquelesCribas(double v) { this.anaquelesCribas = v; }
    public int getPersonalRequerido() { return personalRequerido; }
    public void setPersonalRequerido(int v) { this.personalRequerido = v; }
    public String getFechaCalculo() { return fechaCalculo; }
    public void setFechaCalculo(String v) { this.fechaCalculo = v; }
}