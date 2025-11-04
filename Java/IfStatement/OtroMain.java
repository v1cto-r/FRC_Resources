public class Main {
  public static void main(String args[]) {
    boolean esAdministrador = true;
    int codigo = 1234;
    
    if (esAdministrador && (codigo == 1234)) {
        System.out.println("Acceso permitido");
    } else {
        System.out.println("Acceso negado");
    }
    
  }
}
