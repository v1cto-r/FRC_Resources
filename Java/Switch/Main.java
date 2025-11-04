// 1 siendo Lunes y 7 siendo domingo
public class MyClass {
  public static void main(String args[]) {
    int diaDeLaSemana = 3;
    
    switch (diaDeLaSemana) {
        case 6:
            System.out.println("Es sábado");
            break;
        case 7:
            System.out.println("Es domingo");
            break;
        default:
            System.out.println("Es entre semana");
    }
  }
}
