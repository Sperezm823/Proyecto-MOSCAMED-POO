package moscamed.vista;

import javax.swing.*;
import java.awt.*;
import moscamed.controlador.GestorUsuarios;
import moscamed.controlador.Sesion;
import moscamed.modelo.Usuario;

public class CrearUsuario extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JPasswordField txtConfirmar;
    private JComboBox<String> cboRol;
    private GestorUsuarios gestor;

    public CrearUsuario() {
        if (!Sesion.esAdministrador()) {
            JOptionPane.showMessageDialog(null, "Acceso denegado. Solo administradores.");
            dispose();
            return;
        }

        gestor = new GestorUsuarios();

        setTitle("Crear Nuevo Usuario");
        setSize(450, 380);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Crear Nuevo Usuario", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        JPanel panelForm = new JPanel(new GridLayout(4, 1, 10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        JPanel pUser = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pUser.add(new JLabel("Usuario:        "));
        txtUsuario = new JTextField(15);
        pUser.add(txtUsuario);

        JPanel pPass = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pPass.add(new JLabel("Contraseña:   "));
        txtContrasena = new JPasswordField(15);
        pPass.add(txtContrasena);

        JPanel pConfirm = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pConfirm.add(new JLabel("Confirmar:    "));
        txtConfirmar = new JPasswordField(15);
        pConfirm.add(txtConfirmar);

        JPanel pRol = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pRol.add(new JLabel("Rol:              "));
        cboRol = new JComboBox<>(new String[]{"Invitado", "Administrador"});
        pRol.add(cboRol);

        panelForm.add(pUser);
        panelForm.add(pPass);
        panelForm.add(pConfirm);
        panelForm.add(pRol);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(46, 204, 113));
        btnGuardar.setForeground(Color.WHITE);

        JButton btnVolver = new JButton("Volver");

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnGuardar);
        panelBotones.add(btnVolver);

        btnGuardar.addActionListener(e -> guardar());
        btnVolver.addActionListener(e -> dispose());

        add(titulo, BorderLayout.NORTH);
        add(panelForm, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void guardar() {
        String user = txtUsuario.getText().trim();
        String pass = new String(txtContrasena.getPassword());
        String confirm = new String(txtConfirmar.getPassword());
        String rol = (String) cboRol.getSelectedItem();

        if (user.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return;
        }
        if (!pass.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.");
            return;
        }
        if (pass.length() < 4) {
            JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 4 caracteres.");
            return;
        }

        Usuario nuevo = new Usuario(gestor.getSiguienteId(), user, pass, rol);
        boolean ok = gestor.agregarUsuario(nuevo);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Usuario creado correctamente.");
            txtUsuario.setText("");
            txtContrasena.setText("");
            txtConfirmar.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Ya existe un usuario con ese nombre.");
        }
    }
}