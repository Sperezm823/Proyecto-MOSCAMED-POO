package moscamed.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import moscamed.controlador.Historial;
import moscamed.modelo.Escenario;

public class HistorialView extends JFrame {
    private JTable tabla;
    private DefaultTableModel modelo;
    private Historial historial;

    public HistorialView() {
        historial = new Historial();
        setTitle("Historial de escenarios calculados");
        setSize(800, 500);
        setLocationRelativeTo(null);

        modelo = new DefaultTableModel(new Object[]{
            "ID", "Nivel (millones)", "Dieta 1 (Ton/sem)", "Huevo (L/día)",
            "Pupa hembra (L/día)", "Personal"
        }, 0);
        tabla = new JTable(modelo);

        cargarTabla();

        JButton btnEliminar = new JButton("Eliminar seleccionado");
        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                int id = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
                historial.eliminarEscenario(id);
                modelo.removeRow(fila);
            }
        });

        JButton btnVolver = new JButton("Volver al menú");
        btnVolver.addActionListener(e -> dispose());

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnEliminar);
        panelBotones.add(btnVolver);

        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarTabla() {
        for (Escenario e : historial.listarEscenarios()) {
            modelo.addRow(new Object[]{
                e.getIdEscenario(),
                (int) e.getNivel().getMetaMoscasMillones(),
                String.format("%.2f", e.getResultado().getDieta1SemanalTon()),
                String.format("%.4f", e.getResultado().getHuevoDiarioLitros()),
                String.format("%.4f", e.getResultado().getPupaHembraDiariaLitros()),
                e.getResultado().getPersonalRequerido()
            });
        }
    }
}