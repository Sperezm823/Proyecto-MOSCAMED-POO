package moscamed.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import moscamed.controlador.GestorConstantes;
import moscamed.modelo.Constante;

public class CambiarConstantes extends JFrame {
    private JTable tabla;
    private DefaultTableModel modelo;
    private GestorConstantes gestor;

    public CambiarConstantes() {
        if (!moscamed.controlador.Sesion.esAdministrador()) {
            JOptionPane.showMessageDialog(null, "Acceso denegado. Solo administradores.");
            dispose();
            return;
        }

        gestor = new GestorConstantes();
        setTitle("Modificar constantes del sistema");
        setSize(800, 500);
        setLocationRelativeTo(null);

        modelo = new DefaultTableModel(new Object[]{"ID", "Categoría", "Nombre", "Valor", "Unidad"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };
        tabla = new JTable(modelo);
        tabla.setRowHeight(25);

        cargarTabla();

        JButton btnGuardar = new JButton("Guardar cambios");
        btnGuardar.addActionListener(e -> guardarCambios());

        JButton btnRestablecer = new JButton("Restablecer valores por defecto");
        btnRestablecer.addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de restablecer todas las constantes a sus valores por defecto?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                gestor.restablecerConstantes();
                cargarTabla();
                JOptionPane.showMessageDialog(this, "Constantes restablecidas.");
            }
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
        if (tabla.isEditing()) {
            tabla.getCellEditor().stopCellEditing();
        }

        try {
            int cambios = 0;
            for (int i = 0; i < modelo.getRowCount(); i++) {
                int id = Integer.parseInt(modelo.getValueAt(i, 0).toString());
                
                String valorStr = modelo.getValueAt(i, 3).toString().trim();
                if (valorStr.isEmpty()) {
                    throw new NumberFormatException("El valor no puede estar vacío.");
                }
                
                double nuevoValor = Double.parseDouble(valorStr);
                
                if (nuevoValor <= 0) {
                    JOptionPane.showMessageDialog(this, 
                        "Error en la fila " + (i+1) + ": El valor debe ser mayor a 0.\n" +
                        "Constante: " + modelo.getValueAt(i, 2).toString(), 
                        "Validación", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double valorAnterior = gestor.listarConstantes().get(i).getValor();
                if (nuevoValor != valorAnterior) {
                    gestor.modificarConstante(id, nuevoValor);
                    cambios++;
                }
            }

            if (cambios > 0) {
                JOptionPane.showMessageDialog(this, "Se guardaron " + cambios + " cambio(s) correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "No se detectaron cambios. Recuerda presionar Enter después de editar una celda.");
            }
            cargarTabla();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Error de formato: Ingrese solo números válidos.\n" + ex.getMessage(), 
                "Error de Datos", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage());
        }
    }
}