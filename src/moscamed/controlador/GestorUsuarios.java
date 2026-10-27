package moscamed.controlador;

import java.io.*;
import java.util.ArrayList;
import moscamed.modelo.Usuario;

public class GestorUsuarios {
    private ArrayList<Usuario> usuarios;
    private String rutaArchivo = "datos/usuarios.txt";

    public GestorUsuarios() {
        crearCarpetaDatos();
        this.usuarios = new ArrayList<>();
        cargarDesdeArchivo();
        if (usuarios.isEmpty()) {
            usuarios.add(new Usuario(1, "Ronaldo", "Ronaldo 123", "Administrador"));
            guardar();
        }
    }

    private void crearCarpetaDatos() {
        File carpeta = new File("datos");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
    }

    public Usuario autenticar(String usuario, String contrasena) {
        for (Usuario u : usuarios) {
            if (u.getNombreUsuario().equals(usuario) && u.getContrasena().equals(contrasena)) {
                return u;
            }
        }
        return null;
    }

    public boolean agregarUsuario(Usuario u) {
        for (Usuario existente : usuarios) {
            if (existente.getNombreUsuario().equalsIgnoreCase(u.getNombreUsuario())) {
                return false;
            }
        }
        usuarios.add(u);
        guardar();
        return true;
    }

    public boolean eliminarUsuario(int id) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId() == id) {
                if (usuarios.get(i).getNombreUsuario().equals("Ronaldo")) {
                    return false;
                }
                usuarios.remove(i);
                guardar();
                return true;
            }
        }
        return false;
    }

    public boolean modificarContrasena(int id, String nuevaContrasena) {
        for (Usuario u : usuarios) {
            if (u.getId() == id) {
                u.setContrasena(nuevaContrasena);
                guardar();
                return true;
            }
        }
        return false;
    }

    public boolean modificarRol(int id, String nuevoRol) {
        for (Usuario u : usuarios) {
            if (u.getId() == id) {
                if (u.getNombreUsuario().equals("Ronaldo")) {
                    return false;
                }
                u.setRol(nuevoRol);
                guardar();
                return true;
            }
        }
        return false;
    }

    public boolean modificarNombreUsuario(int id, String nuevoNombre) {
        for (Usuario u : usuarios) {
            if (u.getId() != id && u.getNombreUsuario().equalsIgnoreCase(nuevoNombre)) {
                return false;
            }
        }
        for (Usuario u : usuarios) {
            if (u.getId() == id) {
                if (u.getNombreUsuario().equals("Ronaldo")) {
                    return false;
                }
                u.setNombreUsuario(nuevoNombre);
                guardar();
                return true;
            }
        }
        return false;
    }

    public ArrayList<Usuario> listarUsuarios() {
        return usuarios;
    }

    public int getSiguienteId() {
        int max = 0;
        for (Usuario u : usuarios) {
            if (u.getId() > max) max = u.getId();
        }
        return max + 1;
    }

    public void guardar() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(rutaArchivo))) {
            for (Usuario u : usuarios) {
                pw.println(u.getId() + ";" + u.getNombreUsuario() + ";" + u.getContrasena() + ";" + u.getRol());
            }
        } catch (IOException e) {
            System.err.println("Error al guardar usuarios: " + e.getMessage());
        }
    }

    private void cargarDesdeArchivo() {
        File f = new File(rutaArchivo);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] p = linea.split(";", -1);
                if (p.length == 4) {
                    usuarios.add(new Usuario(
                        Integer.parseInt(p[0]), p[1], p[2], p[3]));
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar usuarios: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error al parsear usuarios: " + e.getMessage());
        }
    }
}