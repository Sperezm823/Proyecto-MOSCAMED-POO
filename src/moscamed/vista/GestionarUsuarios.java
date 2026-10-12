package moscamed.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import moscamed.controlador.GestorUsuarios;
import moscamed.controlador.Sesion;
import moscamed.modelo.Usuario;

public class GestionarUsuarios extends JFrame {
    private JTable tabla;
    private DefaultTableModel modelo;
    private GestorUsuarios gestor;

    public GestionarUsuarios() {
        if (!Sesion.esAdministrador()) {
            JOptionPane.showMessageDialog(null, "Acceso denegado. Solo administradores.");
            dispose();
            return;
        }

        gestor = new GestorUsuarios();

        setTitle("Gestionar Usuarios");
        setSize(800, 520);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Gestión de Usuarios", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));

        modelo = new DefaultTableModel(new Object[]{"ID", "Usuario", "Contraseña", "Rol"}, 0) {
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

        JButton btnModificarPass = new JButton("Modificar contraseña");
        btnModificarPass.setBackground(new Color(52, 152, 219));
        btnModificarPass.setForeground(Color.WHITE);
        btnModificarPass.addActionListener(e -> modificarContrasena());

        JButton btnModificarRol = new JButton("Cambiar rol");
        btnModificarRol.setBackground(new Color(155, 89, 182));
        btnModificarRol.setForeground(Color.WHITE);
        btnModificarRol.addActionListener(e -> modificarRol());

        JButton btnEliminar = new JButton("Eliminar usuario");
        btnEliminar.setBackground(new Color(231, 76, 60));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.addActionListener(e -> eliminarUsuario());

        JButton btnVolver = new JButton("Volver al menú");
        btnVolver.addActionListener(e -> dispose());

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnMostrar);
        panelBotones.add(btnModificarPass);
        panelBotones.add(btnModificarRol);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnVolver);

        add(titulo, BorderLayout.NORTH);
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
        modelo.setRowCount(0);
        for (Usuario u : gestor.listarUsuarios()) {
            modelo.addRow(new Object[]{
                u.getId(), u.getNombreUsuario(), u.getContrasena(), u.getRol()
            });
        }
    }

    private void mostrarDetalles() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario de la tabla.");
            return;
        }

        int id = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
        String usuario = modelo.getValueAt(fila, 1).toString();
        String contrasena = modelo.getValueAt(fila, 2).toString();
        String rol = modelo.getValueAt(fila, 3).toString();

        Usuario u = null;
        for (Usuario us : gestor.listarUsuarios()) {
            if (us.getId() == id) {
                u = us;
                break;
            }
        }

        if (u == null) {
            JOptionPane.showMessageDialog(this, "No se encontró el usuario.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=================================================================\n");
        sb.append("  DETALLES DEL USUARIO\n");
        sb.append("=================================================================\n\n");
        sb.append("ID:              ").append(u.getId()).append("\n");
        sb.append("Usuario:         ").append(u.getNombreUsuario()).append("\n");
        sb.append("Contraseña:      ").append(u.getContrasena()).append("\n");
        sb.append("Rol:             ").append(u.getRol()).append("\n\n");
        sb.append("=================================================================\n");
        sb.append("  PERMISOS DEL ROL\n");
        sb.append("=================================================================\n");

        if (u.esAdministrador()) {
            sb.append("  ✔ Nivel de producción\n");
            sb.append("  ✔ Historial de solicitudes\n");
            sb.append("  ✔ Cambiar constantes\n");
            sb.append("  ✔ Crear nuevo usuario\n");
            sb.append("  ✔ Gestionar usuarios\n");
            sb.append("  ✔ Cerrar sesión\n");
        } else {
            sb.append("  ✔ Nivel de producción\n");
            sb.append("  ✔ Historial de solicitudes\n");
            sb.append("  ✘ Cambiar constantes\n");
            sb.append("  ✘ Crear nuevo usuario\n");
            sb.append("  ✘ Gestionar usuarios\n");
            sb.append("  ✔ Cerrar sesión\n");
        }

        if (u.getNombreUsuario().equals("Ronaldo")) {
            sb.append("\n=================================================================\n");
            sb.append("  ⚠ USUARIO PRINCIPAL DEL SISTEMA\n");
            sb.append("=================================================================\n");
            sb.append("  - No se puede eliminar\n");
            sb.append("  - No se puede cambiar su rol\n");
        }

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));
        area.setCaretPosition(0);

        JDialog dialogo = new JDialog(this, "Detalles del Usuario ID " + u.getId(), true);
        dialogo.setSize(520, 480);
        dialogo.setLocationRelativeTo(this);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dialogo.dispose());

        JPanel panelSur = new JPanel();
        panelSur.add(btnCerrar);

        dialogo.add(new JScrollPane(area), BorderLayout.CENTER);
        dialogo.add(panelSur, BorderLayout.SOUTH);
        dialogo.setVisible(true);
    }

    private void modificarContrasena() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario de la tabla.");
            return;
        }

        int id = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
        String nombre = modelo.getValueAt(fila, 1).toString();

        String nueva = JOptionPane.showInputDialog(this,
                "Nueva contraseña para '" + nombre + "':",
                "Modificar contraseña", JOptionPane.QUESTION_MESSAGE);

        if (nueva == null) return;
        nueva = nueva.trim();

        if (nueva.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La contraseña no puede estar vacía.");
            return;
        }
        if (nueva.length() < 4) {
            JOptionPane.showMessageDialog(this, "Debe tener al menos 4 caracteres.");
            return;
        }

        if (gestor.modificarContrasena(id, nueva)) {
            JOptionPane.showMessageDialog(this, "Contraseña actualizada.");
            cargarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Error al modificar la contraseña.");
        }
    }

    private void modificarRol() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario de la tabla.");
            return;
        }

        int id = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
        String nombre = modelo.getValueAt(fila, 1).toString();
        String rolActual = modelo.getValueAt(fila, 3).toString();

        if (nombre.equals("Ronaldo")) {
            JOptionPane.showMessageDialog(this, "No se puede cambiar el rol del usuario principal.");
            return;
        }

        String[] opciones = {"Administrador", "Invitado"};
        String nuevo = (String) JOptionPane.showInputDialog(this,
                "Nuevo rol para '" + nombre + "':",
                "Cambiar rol", JOptionPane.QUESTION_MESSAGE,
                null, opciones, rolActual);

        if (nuevo == null || nuevo.equals(rolActual)) return;

        if (gestor.modificarRol(id, nuevo)) {
            JOptionPane.showMessageDialog(this, "Rol actualizado a: " + nuevo);
            cargarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Error al modificar el rol.");
        }
    }

    private void eliminarUsuario() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario de la tabla.");
            return;
        }

        int id = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
        String nombre = modelo.getValueAt(fila, 1).toString();

        if (nombre.equals("Ronaldo")) {
            JOptionPane.showMessageDialog(this, "No se puede eliminar el usuario principal.");
            return;
        }

        int conf = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el usuario '" + nombre + "'?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (conf == JOptionPane.YES_OPTION) {
            if (gestor.eliminarUsuario(id)) {
                JOptionPane.showMessageDialog(this, "Usuario eliminado.");
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el usuario.");
            }
        }
    }
}