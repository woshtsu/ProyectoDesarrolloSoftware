package com.mycompany.proyectodeprueba;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ProyectoDePrueba {

    public static void main(String[] args) {
        CSVReader myReader = new CSVReader("data.csv", ",");
        
        try {
            List<Map<String, String>> extractedData = myReader.extractData();
            System.out.println("Datos extraídos:");
            for (Map<String, String> row : extractedData) {
                System.out.println(row);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        }
    }
}
