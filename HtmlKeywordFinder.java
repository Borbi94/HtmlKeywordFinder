import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;
import java.io.*;
import java.util.ArrayList;

public class HtmlKeywordFinder {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("<archivo_html> <palabra_clave>");
            return;
        }

        String archivoHTML = args[0]; 
        String palabraClave = args[1]; 
        ArrayList<Integer> posiciones = new ArrayList<>(); 

        try {

            Reader lector = new FileReader(archivoHTML);
            HTMLEditorKit.Parser parser = new ParserDelegator(); 
            parser.parse(lector, new ManejadorTexto(palabraClave, posiciones), true); 
            lector.close();
            if (posiciones.isEmpty()) {
                System.out.println("No se encontró la palabra '" + palabraClave + "' en el archivo.");
            } else {
                System.out.println("Ocurrencias de la palabra '" + palabraClave + "' encontradas en las posiciones: " + posiciones);
            }

          
            crearArchivoLog(archivoHTML, palabraClave, posiciones);

        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
    }


    private static void crearArchivoLog(String archivoHTML, String palabraClave, ArrayList<Integer> posiciones) {
        String nombreLog = "file-" + palabraClave + ".log"; 
        try (PrintWriter escritor = new PrintWriter(new FileWriter(nombreLog))) {
            escritor.println("Archivo HTML: " + archivoHTML);
            for (int posicion : posiciones) {
                escritor.println("Posición de '" + palabraClave + "': " + posicion);
            }
            System.out.println("Archivo de bitácora creado: " + nombreLog);
        } catch (IOException e) {
            System.out.println("Error al escribir el archivo de bitácora: " + e.getMessage());
        }
    }
}

class ManejadorTexto extends HTMLEditorKit.ParserCallback {
    private String palabraClave;
    private ArrayList<Integer> posiciones;
    private int posicionActual = 0;

    public ManejadorTexto(String palabraClave, ArrayList<Integer> posiciones) {
        this.palabraClave = palabraClave;
        this.posiciones = posiciones;
    }

    @Override
    public void handleText(char[] datos, int pos) {
        String texto = new String(datos); 
        int index = texto.indexOf(palabraClave); 

        while (index != -1) {
            posiciones.add(posicionActual + index); 
            index = texto.indexOf(palabraClave, index + 1); 
        }

        posicionActual += texto.length(); 
    }
}
