package moscamed.controlador;

import moscamed.modelo.Usuario;

public class Sesion {
    private static Usuario usuarioActual;

    public static void iniciar(Usuario u) { usuarioActual = u; }
    public static void cerrar() { usuarioActual = null; }
    public static Usuario getUsuario() { return usuarioActual; }

    public static boolean esAdministrador() {
        return usuarioActual != null && usuarioActual.esAdministrador();
    }

    public static boolean esInvitado() {
        return usuarioActual != null && usuarioActual.esInvitado();
    }

    public static boolean haySesion() {
        return usuarioActual != null;
    }
}