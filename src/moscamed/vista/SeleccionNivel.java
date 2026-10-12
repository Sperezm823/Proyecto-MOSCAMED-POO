package moscamed.vista;

import javax.swing.*;
import java.awt.*;
import moscamed.controlador.GestorConstantes;
import moscamed.controlador.Historial;
import moscamed.controlador.Sesion;
import moscamed.modelo.NivelProduccion;
import moscamed.modelo.Resultado;
import moscamed.modelo.Escenario;
import moscamed.modelo.Usuario;

public class SeleccionNivel extends JFrame {
    private JTextField txtVariable;
    private GestorConstantes gestor;
    private Historial historial;
    private int contadorId;

    public SeleccionNivel() {
        gestor = new GestorConstantes();
        historial = new Historial();
        contadorId = historial.getSiguienteId();

        setTitle("Seleccione el nivel de producción (en millones)");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelNiveles = new JPanel(new GridLayout(3, 3, 10, 10));
        int[] niveles = {100, 200, 300, 400, 500, 600, 700, 800, 900};
        for (int n : niveles) {
            JButton btn = new JButton(String.valueOf(n));
            btn.setBackground(new Color(46, 204, 113));
            btn.setForeground(Color.WHITE);
            btn.addActionListener(e -> calcularYMostrar(n, false));
            panelNiveles.add(btn);
        }

        JPanel panelVariable = new JPanel(new FlowLayout());
        panelVariable.add(new JLabel("Nivel variable (millones):"));
        txtVariable = new JTextField(10);
        panelVariable.add(txtVariable);
        JButton btnVar = new JButton("Aceptar");
        btnVar.addActionListener(e -> {
            try {
                double v = Double.parseDouble(txtVariable.getText());
                if (v <= 0) {
                    JOptionPane.showMessageDialog(this, "El nivel debe ser mayor a 0.");
                    return;
                }
                calcularYMostrar(v, true);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese un número válido.");
            }
        });
        panelVariable.add(btnVar);

        JButton btnVolver = new JButton("Volver al menú");
        btnVolver.addActionListener(e -> dispose());

        add(panelNiveles, BorderLayout.CENTER);
        add(panelVariable, BorderLayout.SOUTH);
        add(btnVolver, BorderLayout.NORTH);
    }

    private void calcularYMostrar(double meta, boolean esVariable) {
        NivelProduccion nivel = new NivelProduccion(meta, esVariable);
        Resultado res = new Resultado();
        res.calcular(nivel, gestor.getConstantes());

        Escenario esc = new Escenario(contadorId++, nivel, gestor.getConstantes(), res);

        Usuario u = Sesion.getUsuario();
        if (u != null) {
            esc.setUsuario(u.getNombreUsuario());
        } else {
            esc.setUsuario("Desconocido");
        }

        historial.agregarEscenario(esc);

        new Resultados(res, nivel).setVisible(true);
    }
}