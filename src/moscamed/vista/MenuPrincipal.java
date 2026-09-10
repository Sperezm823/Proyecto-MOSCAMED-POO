package moscamed.vista;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        setTitle("Sistema de Análisis de Escenarios - MOSCAMED");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1, 20, 20));

        JLabel titulo = new JLabel("MOSCAMED - El Pino", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));

        JButton btnNivel = new JButton("Nivel de producción");
        btnNivel.setBackground(new Color(46, 204, 113));
        btnNivel.setForeground(Color.WHITE);
        btnNivel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton btnConstantes = new JButton("Cambiar constantes");
        btnConstantes.setBackground(new Color(46, 204, 113));
        btnConstantes.setForeground(Color.WHITE);
        btnConstantes.setFont(new Font("Arial", Font.BOLD, 16));

        JButton btnHistorial = new JButton("Historial de solicitudes");
        btnHistorial.setBackground(new Color(52, 152, 219));
        btnHistorial.setForeground(Color.WHITE);
        btnHistorial.setFont(new Font("Arial", Font.BOLD, 14));

        btnNivel.addActionListener(e -> new SeleccionNivel().setVisible(true));
        btnConstantes.addActionListener(e -> new CambiarConstantes().setVisible(true));
        btnHistorial.addActionListener(e -> new HistorialView().setVisible(true));

        add(titulo);
        add(btnNivel);
        add(btnConstantes);
        add(btnHistorial);
    }
}