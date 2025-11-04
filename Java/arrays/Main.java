public class MyClass {
  public static void main(String args[]) {
    String[] nombres = {"Juan", "Jose", "Alberto"};
    int[] edades = {15, 17, 16};
    
    System.out.println("El primer nombre es: "+nombres[0]);
    
    System.out.println("Los alumnos son: ");
    for (int i=0; i<nombres.length; i++) {
        System.out.println("Alumno: "+nombres[i]+ ", "+edades[i]);
    }
  }
}
