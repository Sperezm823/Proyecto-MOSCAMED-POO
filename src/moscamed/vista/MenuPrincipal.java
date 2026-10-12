package moscamed.vista;

import javax.swing.*;
import java.awt.*;
import moscamed.controlador.Sesion;
import moscamed.modelo.Usuario;

public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        Usuario u = Sesion.getUsuario();

        setTitle("Sistema de Análisis de Escenarios - MOSCAMED");
        setSize(520, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("MOSCAMED - El Pino", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));

        String textoBienvenida = (u != null)
                ? "Bienvenido: " + u.getNombreUsuario() + " (" + u.getRol() + ")"
                : "Bienvenido";

        JLabel lblBienvenida = new JLabel(textoBienvenida, SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Arial", Font.PLAIN, 13));
        lblBienvenida.setForeground(new Color(80, 80, 80));

        JPanel panelTitulo = new JPanel(new GridLayout(2, 1));
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        panelTitulo.add(titulo);
        panelTitulo.add(lblBienvenida);

        JButton btnNivel = crearBoton("Nivel de producción", new Color(46, 204, 113));
        JButton btnHistorial = crearBoton("Historial de solicitudes", new Color(52, 152, 219));
        JButton btnConstantes = crearBoton("Cambiar constantes", new Color(46, 204, 113));
        JButton btnCrearUsuario = crearBoton("Crear nuevo usuario", new Color(155, 89, 182));
        JButton btnGestionarUsuarios = crearBoton("Gestionar usuarios", new Color(155, 89, 182));
        JButton btnCerrarSesion = crearBoton("Cerrar sesión", new Color(231, 76, 60));

        btnNivel.addActionListener(e -> new SeleccionNivel().setVisible(true));
        btnHistorial.addActionListener(e -> new HistorialView().setVisible(true));

        if (Sesion.esAdministrador()) {
            btnConstantes.addActionListener(e -> new CambiarConstantes().setVisible(true));
            btnCrearUsuario.addActionListener(e -> new CrearUsuario().setVisible(true));
            btnGestionarUsuarios.addActionListener(e -> new GestionarUsuarios().setVisible(true));
        }

        btnCerrarSesion.addActionListener(e -> {
            int conf = JOptionPane.showConfirmDialog(this,
                    "¿Cerrar sesión?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                Sesion.cerrar();
                for (Window w : Window.getWindows()) {
                    w.dispose();
                }
                new Login().setVisible(true);
            }
        });

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(0, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 60, 30, 60));
        panelBotones.add(btnNivel);
        panelBotones.add(btnHistorial);

        if (Sesion.esAdministrador()) {
            panelBotones.add(btnConstantes);
            panelBotones.add(btnCrearUsuario);
            panelBotones.add(btnGestionarUsuarios);
        }

        panelBotones.add(btnCerrarSesion);

        add(panelTitulo, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.CENTER);
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        return btn;
    }
}