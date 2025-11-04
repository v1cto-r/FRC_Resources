public class MyClass {
  public static void main(String args[]) {
    int calificacionMate = 8, calificacionEspanol = 9, calificacionGeografia = 9, calificacionHistoria = 8;
    double promedio;
    
    promedio = (double) ( calificacionMate + calificacionEspanol + calificacionGeografia + calificacionHistoria) / 4;
    System.out.println(promedio > 8.0);
  }
}
