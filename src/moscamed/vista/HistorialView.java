package moscamed.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import moscamed.controlador.Historial;
import moscamed.modelo.Constante;
import moscamed.modelo.Escenario;
import moscamed.modelo.Resultado;

public class HistorialView extends JFrame {
    private JTable tabla;
    private DefaultTableModel modelo;
    private Historial historial;

    public HistorialView() {
        historial = new Historial();
        setTitle("Historial de escenarios calculados");
        setSize(1000, 550);
        setLocationRelativeTo(null);

        modelo = new DefaultTableModel(new Object[]{
            "ID", "Usuario", "Fecha", "Nivel (millones)", "Dieta 1 (Ton/sem)",
            "Huevo (L/día)", "Pupa hembra (L/día)", "Personal"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modelo);
        tabla.setRowHeight(25);
        tabla.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cargarTabla();

        JButton btnMostrar = new JButton("Mostrar detalles");
        btnMostrar.setBackground(new Color(46, 204, 113));
        btnMostrar.setForeground(Color.WHITE);
        btnMostrar.setFont(new Font("Arial", Font.BOLD, 13));
        btnMostrar.addActionListener(e -> mostrarDetalles());

        JButton btnVolver = new JButton("Volver al menú");
        btnVolver.addActionListener(e -> dispose());

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnMostrar);
        panelBotones.add(btnVolver);

        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    mostrarDetalles();
                }
            }
        });
    }

    private void cargarTabla() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        for (Escenario e : historial.listarEscenarios()) {
            modelo.addRow(new Object[]{
                e.getIdEscenario(),
                e.getUsuario(),
                sdf.format(e.getFechaHora()),
                (int) e.getNivel().getMetaMoscasMillones(),
                String.format("%.2f", e.getResultado().getDieta1SemanalTon()),
                String.format("%.4f", e.getResultado().getHuevoDiarioLitros()),
                String.format("%.4f", e.getResultado().getPupaHembraDiariaLitros()),
                e.getResultado().getPersonalRequerido()
            });
        }
    }

    private void mostrarDetalles() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un escenario de la tabla.");
            return;
        }
        int id = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
        Escenario esc = historial.buscarPorId(id);
        if (esc == null) {
            JOptionPane.showMessageDialog(this, "No se encontró el escenario.");
            return;
        }
        mostrarDialogoDetalles(esc);
    }

    private void mostrarDialogoDetalles(Escenario esc) {
        JDialog dialogo = new JDialog(this, "Detalles del Escenario ID " + esc.getIdEscenario(), true);
        dialogo.setSize(740, 720);
        dialogo.setLocationRelativeTo(this);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Resultado r = esc.getResultado();

        StringBuilder sb = new StringBuilder();
        sb.append("=================================================================\n");
        sb.append("  DETALLES DEL ESCENARIO\n");
        sb.append("=================================================================\n\n");
        sb.append("ID: ").append(esc.getIdEscenario()).append("\n");
        sb.append("Solicitado por: ").append(esc.getUsuario()).append("\n");
        sb.append("Fecha: ").append(sdf.format(esc.getFechaHora())).append("\n");
        sb.append("Nivel de producción: ").append((int) esc.getNivel().getMetaMoscasMillones()).append(" millones\n\n");

        sb.append("=================================================================\n");
        sb.append("  INSUMOS\n");
        sb.append("=================================================================\n");
        sb.append(String.format("Dieta 1 (semanal):       %.2f Ton/semana%n", r.getDieta1SemanalTon()));
        sb.append(String.format("Dieta 1 (diaria):        %.4f Ton/día%n", r.getDieta1DiariaTon()));
        sb.append(String.format("Huevo diario:            %.4f L/día%n", r.getHuevoDiarioLitros()));
        sb.append(String.format("Pupa hembra diaria:      %.4f L/día%n", r.getPupaHembraDiariaLitros()));
        sb.append(String.format("Pupa macho diaria:       %.4f L/día%n", r.getPupaMachoDiariaLitros()));
        sb.append(String.format("Dieta 2 (diaria):        %.4f Ton/día%n", r.getDieta2DiariaTon()));
        sb.append(String.format("Dieta Total (diaria):    %.4f Ton/día%n", r.getDietaTotalDiariaTon()));
        sb.append("\n");

        sb.append("=================================================================\n");
        sb.append("  EQUIPOS\n");
        sb.append("=================================================================\n");
        sb.append(String.format("Bandejas Dieta 1:        %d bandejas/ciclo%n", r.getBandejasDieta1()));
        sb.append(String.format("Bandejas Dieta 2:        %d bandejas/ciclo%n", r.getBandejasDieta2()));
        sb.append(String.format("Total Bandejas:          %d bandejas/ciclo%n", r.getTotalBandejas()));
        sb.append(String.format("Anaqueles dieta:         %d anaqueles%n", r.getAnaquelesDieta()));
        sb.append(String.format("Cribas Pupa:             %d cribas/ciclo%n", r.getCribasPupa()));
        sb.append(String.format("Cribas Macho:            %d cribas/ciclo%n", r.getCribasMacho()));
        sb.append(String.format("Total Cribas:            %d cribas%n", r.getTotalCribas()));
        sb.append(String.format("Anaqueles cribas:        %d anaqueles%n", r.getAnaquelesCribas()));
        sb.append("\n");

        sb.append("=================================================================\n");
        sb.append("  PERSONAL\n");
        sb.append("=================================================================\n");
        sb.append(String.format("Trabajadores requeridos: %d trabajadores%n", r.getPersonalRequerido()));
        sb.append("\n");

        if (esc.getConstantesUsadas() != null && !esc.getConstantesUsadas().isEmpty()) {
            sb.append("=================================================================\n");
            sb.append("  CONSTANTES UTILIZADAS\n");
            sb.append("=================================================================\n");
            String categoriaActual = "";
            for (Constante c : esc.getConstantesUsadas()) {
                if (!c.getCategoria().equals(categoriaActual)) {
                    categoriaActual = c.getCategoria();
                    sb.append("\n[").append(categoriaActual).append("]\n");
                }
                sb.append(String.format("  %-30s = %.4f%n", c.getNombre(), c.getValor()));
            }
        }

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        area.setCaretPosition(0);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dialogo.dispose());

        JPanel panelSur = new JPanel();
        panelSur.add(btnCerrar);

        dialogo.add(new JScrollPane(area), BorderLayout.CENTER);
        dialogo.add(panelSur, BorderLayout.SOUTH);
        dialogo.setVisible(true);
    }
}