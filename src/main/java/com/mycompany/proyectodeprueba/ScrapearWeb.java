package com.mycompany.proyectodeprueba;


import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ScrapearWeb {
    private static final String URL_BASE = "https://sodimac.falabella.com.pe/sodimac-pe/search?Ntt=";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";
    private static final int TIMEOUT = 10000;

    /**
     * Obtiene el precio de un producto de la web de Sodimac.
     * @param producto El nombre del producto a buscar.
     * @return El precio del producto como un double.
     * @throws IOException Si ocurre un error de conexión o lectura.
     * @throws ProductNotFoundException Si no se encuentra el producto o el precio.
     */
    public double getPrecio(String producto) throws IOException, ProductNotFoundException {
        Document doc = Jsoup.connect(URL_BASE + producto.replace(" ", "%20"))
                            .userAgent(USER_AGENT)
                            .timeout(TIMEOUT)
                            .get();

        // Buscar todos los elementos que representan productos
        Elements items = doc.select("a.jsx-682461353.jsx-3390574944.pod.pod-4_GRID.pod-link");

        if (items.isEmpty()) {
            throw new ProductNotFoundException("No se encontraron resultados para el producto: " + producto);
        }

        // Obtener el primer resultado
        Element firstItem = items.first();
        
        // Seleccionar el elemento del precio dentro del primer resultado
        Elements precios = firstItem.select("span.copy10.primary.medium.jsx-233704000.normal.line-height-22");
        
        if (precios.isEmpty()) {
            throw new ProductNotFoundException("Se encontró el producto pero no se pudo extraer el precio. La estructura de la página puede haber cambiado.");
        }
        
        String precioTexto = precios.first().text().trim();
        
        // Limpiar el texto para que solo contenga números y un punto decimal
        precioTexto = precioTexto.replaceAll("[^\\d.]", ""); 
        
        try {
            return Double.parseDouble(precioTexto);
        } catch (NumberFormatException e) {
            throw new ProductNotFoundException("Error al convertir el precio '" + precioTexto + "' a un número.");
        }
    }
}
