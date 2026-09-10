package moscamed.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import moscamed.controlador.GestorConstantes;
import moscamed.modelo.Constante;

public class CambiarConstantes extends JFrame {
    private JTable tabla;
    private DefaultTableModel modelo;
    private GestorConstantes gestor;

    public CambiarConstantes() {
        gestor = new GestorConstantes();
        setTitle("Modificar constantes del sistema");
        setSize(800, 500);
        setLocationRelativeTo(null);

        modelo = new DefaultTableModel(new Object[]{"ID", "Categoría", "Nombre", "Valor", "Unidad"}, 0);
        tabla = new JTable(modelo);

        cargarTabla();

        JButton btnGuardar = new JButton("Guardar cambios");
        btnGuardar.addActionListener(e -> guardarCambios());

        JButton btnRestablecer = new JButton("Restablecer valores por defecto");
        btnRestablecer.addActionListener(e -> {
            gestor.restablecerConstantes();
            cargarTabla();
            JOptionPane.showMessageDialog(this, "Constantes restablecidas.");
        });

        JButton btnVolver = new JButton("Volver al menú");
        btnVolver.addActionListener(e -> dispose());

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnGuardar);
        panelBotones.add(btnRestablecer);
        panelBotones.add(btnVolver);

        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        for (Constante c : gestor.listarConstantes()) {
            modelo.addRow(new Object[]{c.getId(), c.getCategoria(), c.getNombre(), c.getValor(), c.getUnidad()});
        }
    }

    private void guardarCambios() {
        try {
            for (int i = 0; i < modelo.getRowCount(); i++) {
                int id = Integer.parseInt(modelo.getValueAt(i, 0).toString());
                double nuevoValor = Double.parseDouble(modelo.getValueAt(i, 3).toString());
                gestor.modificarConstante(id, nuevoValor);
            }
            JOptionPane.showMessageDialog(this, "Cambios guardados correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
        }
    }
}