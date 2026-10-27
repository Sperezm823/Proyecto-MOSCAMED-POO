package moscamed.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import moscamed.controlador.Historial;
import moscamed.modelo.Constante;
import moscamed.modelo.Escenario;
import moscamed.modelo.Resultado;

public class HistorialView extends JFrame {
    private JTable tabla;
    private DefaultTableModel modelo;
    private Historial historial;
    private JTextField txtBuscar;

    public HistorialView() {
        historial = new Historial();
        setTitle("Historial de escenarios calculados");
        setSize(1000, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelNorte.add(new JLabel("Buscar por Usuario o Nivel: "));
        txtBuscar = new JTextField(20);
        panelNorte.add(txtBuscar);
        JButton btnBuscar = new JButton("Buscar");
        JButton btnLimpiar = new JButton("Mostrar todos");
        panelNorte.add(btnBuscar);
        panelNorte.add(btnLimpiar);

        modelo = new DefaultTableModel(new Object[]{
            "ID", "Usuario", "Fecha", "Nivel (millones)", "Dieta 1 (Ton/sem)",
            "Huevo (L/día)", "Pupa hembra (L/día)", "Personal"
        }, 0) {
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

        JButton btnExportar = new JButton("Exportar a CSV");
        btnExportar.setBackground(new Color(243, 156, 18));
        btnExportar.setForeground(Color.WHITE);
        btnExportar.setFont(new Font("Arial", Font.BOLD, 13));
        btnExportar.addActionListener(e -> exportarCSV());

        JButton btnVolver = new JButton("Volver al menú");
        btnVolver.addActionListener(e -> dispose());

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnMostrar);
        panelBotones.add(btnExportar);
        panelBotones.add(btnVolver);

        btnBuscar.addActionListener(e -> {
            String filtro = txtBuscar.getText().trim().toLowerCase();
            modelo.setRowCount(0);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            
            for (Escenario esc : historial.listarEscenarios()) {
                String usuario = esc.getUsuario().toLowerCase();
                String nivel = String.valueOf((int) esc.getNivel().getMetaMoscasMillones());
                
                if (usuario.contains(filtro) || nivel.equals(filtro) || filtro.isEmpty()) {
                    modelo.addRow(new Object[]{
                        esc.getIdEscenario(), esc.getUsuario(), sdf.format(esc.getFechaHora()),
                        (int) esc.getNivel().getMetaMoscasMillones(),
                        String.format("%.2f", esc.getResultado().getDieta1SemanalTon()),
                        String.format("%.4f", esc.getResultado().getHuevoDiarioLitros()),
                        String.format("%.4f", esc.getResultado().getPupaHembraDiariaLitros()),
                        esc.getResultado().getPersonalRequerido()
                    });
                }
            }
        });

        btnLimpiar.addActionListener(e -> {
            txtBuscar.setText("");
            modelo.setRowCount(0);
            cargarTabla();
        });

        add(panelNorte, BorderLayout.NORTH);
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        for (Escenario e : historial.listarEscenarios()) {
            modelo.addRow(new Object[]{
                e.getIdEscenario(),
                e.getUsuario(),
                sdf.format(e.getFechaHora()),
                (int) e.getNivel().getMetaMoscasMillones(),
                String.format("%.2f", e.getResultado().getDieta1SemanalTon()),
                String.format("%.4f", e.getResultado().getHuevoDiarioLitros()),
                String.format("%.4f", e.getResultado().getPupaHembraDiariaLitros()),
                e.getResultado().getPersonalRequerido()
            });
        }
    }

    private void exportarCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar historial como CSV");
        fileChooser.setSelectedFile(new File("Historial_Moscamed.csv"));
        
        int seleccion = fileChooser.showSaveDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            if (!archivo.getName().toLowerCase().endsWith(".csv")) {
                archivo = new File(archivo.getParent(), archivo.getName() + ".csv");
            }

            try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
                pw.println("ID;Usuario;Fecha;Nivel (Millones);" +
                           "Dieta 1 (Ton/sem);Dieta 1 (Ton/dia);Huevo (L/dia);Pupa Hembra (L/dia);Pupa Macho (L/dia);Dieta 2 (Ton/dia);Dieta Total (Ton/dia);" +
                           "Bandejas Dieta 1;Bandejas Dieta 2;Total Bandejas;Anaqueles Dieta;Cribas Pupa;Cribas Macho;Total Cribas;Anaqueles Cribas;" +
                           "Personal");
                
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                
                for (Escenario e : historial.listarEscenarios()) {
                    Resultado r = e.getResultado();
                    pw.println(
                        e.getIdEscenario() + ";" +
                        e.getUsuario() + ";" +
                        sdf.format(e.getFechaHora()) + ";" +
                        (int) e.getNivel().getMetaMoscasMillones() + ";" +
                        
                        String.format("%.2f", r.getDieta1SemanalTon()) + ";" +
                        String.format("%.4f", r.getDieta1DiariaTon()) + ";" +
                        String.format("%.4f", r.getHuevoDiarioLitros()) + ";" +
                        String.format("%.4f", r.getPupaHembraDiariaLitros()) + ";" +
                        String.format("%.4f", r.getPupaMachoDiariaLitros()) + ";" +
                        String.format("%.4f", r.getDieta2DiariaTon()) + ";" +
                        String.format("%.4f", r.getDietaTotalDiariaTon()) + ";" +
                        
                        r.getBandejasDieta1() + ";" +
                        r.getBandejasDieta2() + ";" +
                        r.getTotalBandejas() + ";" +
                        r.getAnaquelesDieta() + ";" +
                        r.getCribasPupa() + ";" +
                        r.getCribasMacho() + ";" +
                        r.getTotalCribas() + ";" +
                        r.getAnaquelesCribas() + ";" +
                        
                        r.getPersonalRequerido()
                    );
                }
                JOptionPane.showMessageDialog(this, "Historial completo exportado exitosamente a:\n" + archivo.getAbsolutePath());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al exportar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void mostrarDetalles() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un escenario de la tabla.");
            return;
        }
        int id = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
        Escenario esc = historial.buscarPorId(id);
        if (esc == null) {
            JOptionPane.showMessageDialog(this, "No se encontró el escenario.");
            return;
        }
        mostrarDialogoDetalles(esc);
    }

    private void mostrarDialogoDetalles(Escenario esc) {
        JDialog dialogo = new JDialog(this, "Detalles del Escenario ID " + esc.getIdEscenario(), true);
        dialogo.setSize(740, 720);
        dialogo.setLocationRelativeTo(this);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Resultado r = esc.getResultado();

        StringBuilder sb = new StringBuilder();
        sb.append("=================================================================\n");
        sb.append("  DETALLES DEL ESCENARIO\n");
        sb.append("=================================================================\n\n");
        sb.append("ID: ").append(esc.getIdEscenario()).append("\n");
        sb.append("Solicitado por: ").append(esc.getUsuario()).append("\n");
        sb.append("Fecha: ").append(sdf.format(esc.getFechaHora())).append("\n");
        sb.append("Nivel de producción: ").append((int) esc.getNivel().getMetaMoscasMillones()).append(" millones\n\n");

        sb.append("=================================================================\n");
        sb.append("  INSUMOS\n");
        sb.append("=================================================================\n");
        sb.append(String.format("Dieta 1 (semanal):       %.2f Ton/semana%n", r.getDieta1SemanalTon()));
        sb.append(String.format("Dieta 1 (diaria):        %.4f Ton/día%n", r.getDieta1DiariaTon()));
        sb.append(String.format("Huevo diario:            %.4f L/día%n", r.getHuevoDiarioLitros()));
        sb.append(String.format("Pupa hembra diaria:      %.4f L/día%n", r.getPupaHembraDiariaLitros()));
        sb.append(String.format("Pupa macho diaria:       %.4f L/día%n", r.getPupaMachoDiariaLitros()));
        sb.append(String.format("Dieta 2 (diaria):        %.4f Ton/día%n", r.getDieta2DiariaTon()));
        sb.append(String.format("Dieta Total (diaria):    %.4f Ton/día%n", r.getDietaTotalDiariaTon()));
        sb.append("\n");

        sb.append("=================================================================\n");
        sb.append("  EQUIPOS\n");
        sb.append("=================================================================\n");
        sb.append(String.format("Bandejas Dieta 1:        %d bandejas/ciclo%n", r.getBandejasDieta1()));
        sb.append(String.format("Bandejas Dieta 2:        %d bandejas/ciclo%n", r.getBandejasDieta2()));
        sb.append(String.format("Total Bandejas:          %d bandejas/ciclo%n", r.getTotalBandejas()));
        sb.append(String.format("Anaqueles dieta:         %d anaqueles%n", r.getAnaquelesDieta()));
        sb.append(String.format("Cribas Pupa:             %d cribas/ciclo%n", r.getCribasPupa()));
        sb.append(String.format("Cribas Macho:            %d cribas/ciclo%n", r.getCribasMacho()));
        sb.append(String.format("Total Cribas:            %d cribas%n", r.getTotalCribas()));
        sb.append(String.format("Anaqueles cribas:        %d anaqueles%n", r.getAnaquelesCribas()));
        sb.append("\n");

        sb.append("=================================================================\n");
        sb.append("  PERSONAL\n");
        sb.append("=================================================================\n");
        sb.append(String.format("Trabajadores requeridos: %d trabajadores%n", r.getPersonalRequerido()));
        sb.append("\n");

        if (esc.getConstantesUsadas() != null && !esc.getConstantesUsadas().isEmpty()) {
            sb.append("=================================================================\n");
            sb.append("  CONSTANTES UTILIZADAS\n");
            sb.append("=================================================================\n");
            String categoriaActual = "";
            for (Constante c : esc.getConstantesUsadas()) {
                if (!c.getCategoria().equals(categoriaActual)) {
                    categoriaActual = c.getCategoria();
                    sb.append("\n[").append(categoriaActual).append("]\n");
                }
                sb.append(String.format("  %-30s = %.4f%n", c.getNombre(), c.getValor()));
            }
        }

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        area.setCaretPosition(0);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dialogo.dispose());

        JPanel panelSur = new JPanel();
        panelSur.add(btnCerrar);

        dialogo.add(new JScrollPane(area), BorderLayout.CENTER);
        dialogo.add(panelSur, BorderLayout.SOUTH);
        dialogo.setVisible(true);
    }
}