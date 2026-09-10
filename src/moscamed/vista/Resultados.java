package moscamed.vista;

import javax.swing.*;
import java.awt.*;
import moscamed.modelo.NivelProduccion;
import moscamed.modelo.Resultado;

public class Resultados extends JFrame {

    public Resultados(Resultado r, NivelProduccion nivel) {
        setTitle("Plan para " + (int) nivel.getMetaMoscasMillones() + " millones de moscas/semana");
        setSize(700, 600);
        setLocationRelativeTo(null);

        JTextArea area = new JTextArea(r.generarResumen());
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));

        JButton btnVolver = new JButton("Volver al menú");
        btnVolver.addActionListener(e -> dispose());

        add(new JScrollPane(area), BorderLayout.CENTER);
        add(btnVolver, BorderLayout.SOUTH);
    }
}