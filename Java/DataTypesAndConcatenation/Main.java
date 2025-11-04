public class MyClass {
  public static void main(String args[]) {
    final String name = "Juan"; // Podemos tener texto con el tipo de dato String, se pone entre comillas dobles ""
                                // Usamos final para definir que el valor no se puede cambiar
    int edad = 17; // Int almacena numeros enteros, sin el punto decimal, tampoco nos permite almacenar numeros tan grandes
    float calificacion = 8.9f; // Float nos permite almacenar numeros decimales al igual que nmeros más grandes

    var altura = 1.78 // Usando var, JAVA automaticamente va a interpretar el valor y asignarle un tipo
                      // Se recomienda como sea ser especifico desde el incio de el tipo de dato que se usa

    // En un print, podemos unir varios valores para mostrarlos, lo hacemos mediante " + " por ejemplo:
    System.out.println("Mi nombre es: "+ name +", tengo " + edad + " años y mi calificación es de: " + calificacion);
  }
}
