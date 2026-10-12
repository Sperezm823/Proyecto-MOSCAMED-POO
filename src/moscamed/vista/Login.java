package moscamed.vista;

import javax.swing.*;
import java.awt.*;
import moscamed.controlador.GestorUsuarios;
import moscamed.controlador.Sesion;
import moscamed.modelo.Usuario;

public class Login extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JComboBox<String> cboTipo;
    private GestorUsuarios gestor;

    public Login() {
        gestor = new GestorUsuarios();

        setTitle("MOSCAMED - Iniciar Sesión");
        setSize(450, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("MOSCAMED - El Pino", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 5, 10));

        JLabel subtitulo = new JLabel("Sistema de Análisis de Escenarios", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 13));

        JPanel panelTitulo = new JPanel(new GridLayout(2, 1));
        panelTitulo.add(titulo);
        panelTitulo.add(subtitulo);

        JPanel panelForm = new JPanel(new GridLayout(3, 1, 10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        JPanel pTipo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pTipo.add(new JLabel("Tipo de usuario:"));
        cboTipo = new JComboBox<>(new String[]{"Administrador", "Invitado"});
        pTipo.add(cboTipo);

        JPanel pUser = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pUser.add(new JLabel("Usuario:           "));
        txtUsuario = new JTextField(15);
        pUser.add(txtUsuario);

        JPanel pPass = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pPass.add(new JLabel("Contraseña:    "));
        txtContrasena = new JPasswordField(15);
        pPass.add(txtContrasena);

        panelForm.add(pTipo);
        panelForm.add(pUser);
        panelForm.add(pPass);

        JButton btnIngresar = new JButton("Ingresar");
        btnIngresar.setBackground(new Color(46, 204, 113));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFont(new Font("Arial", Font.BOLD, 14));

        JButton btnSalir = new JButton("Salir");

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnIngresar);
        panelBotones.add(btnSalir);

        btnIngresar.addActionListener(e -> ingresar());
        btnSalir.addActionListener(e -> System.exit(0));

        add(panelTitulo, BorderLayout.NORTH);
        add(panelForm, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(btnIngresar);
    }

    private void ingresar() {
        String tipo = (String) cboTipo.getSelectedItem();
        String user = txtUsuario.getText().trim();
        String pass = new String(txtContrasena.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese usuario y contraseña.");
            return;
        }

        Usuario u = gestor.autenticar(user, pass);
        if (u == null) {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.");
            return;
        }

        if (!u.getRol().equalsIgnoreCase(tipo)) {
            JOptionPane.showMessageDialog(this,
                "Este usuario no es de tipo " + tipo + ".\nRol real: " + u.getRol());
            return;
        }

        Sesion.iniciar(u);
        new MenuPrincipal().setVisible(true);
        dispose();
    }
}