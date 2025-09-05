package com.mycompany.proyectodeprueba;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ProyectoDePrueba {
    public static void main(String[] args) {
        CSVReader myReader = new CSVReader("data.csv", ",");
        ScrapearWeb scraper = new ScrapearWeb();
        double costoTotal = 0.0;
        
        try {
            List<Map<String, String>> extractedData = myReader.extractData();
            System.out.println("Cálculo de costos de materiales:\n");

            for (Map<String, String> row : extractedData) {
                String producto = row.get("nombre");
                String cantidadStr = row.get("cantidad");
                
                try {
                    double cantidad = Double.parseDouble(cantidadStr);
                    double precio = scraper.getPrecio(producto);
                    double subtotal = precio * cantidad;
                    costoTotal += subtotal;

                    System.out.printf("Producto: %s\n", producto);
                    System.out.printf("  Cantidad: %.2f\n", cantidad);
                    System.out.printf("  Precio por unidad: S/%.2f\n", precio);
                    System.out.printf("  Subtotal: S/%.2f\n\n", subtotal);

                } catch (NumberFormatException e) {
                    System.err.println("Error: La cantidad para '" + producto + "' no es un número válido.");
                } catch (IOException | ProductNotFoundException e) {
                    System.err.println("Error al obtener datos para '" + producto + "': " + e.getMessage());
                }
            }
            
            System.out.println("----------------------------------------");
            System.out.printf("Costo total estimado del proyecto: S/%.2f\n", costoTotal);
            
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        }
    }
}
