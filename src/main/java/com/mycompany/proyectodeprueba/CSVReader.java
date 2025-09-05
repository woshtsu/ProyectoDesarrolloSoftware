package com.mycompany.proyectodeprueba;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVReader {
    private final String filepath;
    private final String csvDelimiter;

    private final List<String> CANTIDAD_NAMES_ACEPTED = Arrays.asList("cantidad", "cant", "quantity");
    private final List<String> PRODUCTO_NAMES_ACEPTED = Arrays.asList("nombre", "producto", "name");
    
    public CSVReader(String filePath, String delimiter) {
        this.filepath = filePath;
        this.csvDelimiter = delimiter;
    }

    public List<Map<String, String>> extractData() throws IOException {
        List<Map<String, String>> data = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(this.filepath))) {
            String line = br.readLine();
            if (line == null) {
                return data;
            }

            String[] headers = line.split(this.csvDelimiter);
            int idxNombre = getColumnIndex(headers, PRODUCTO_NAMES_ACEPTED);
            int idxCantidad = getColumnIndex(headers, CANTIDAD_NAMES_ACEPTED);

            if (idxNombre == -1 || idxCantidad == -1) {
                throw new IOException("No se encontraron las columnas 'nombre' o 'cantidad' en el CSV.");
            }

            while ((line = br.readLine()) != null) {
                String[] values = line.split(this.csvDelimiter);
                if (values.length > Math.max(idxNombre, idxCantidad)) {
                    Map<String, String> row = new HashMap<>();
                    row.put("nombre", values[idxNombre].trim());
                    row.put("cantidad", values[idxCantidad].trim());
                    data.add(row);
                }
            }

        }
        
        return data;
    }

    private int getColumnIndex(String[] headers, List<String> acceptedNames) {
        for (int i = 0; i < headers.length; i++) {
            if (acceptedNames.contains(headers[i].trim().toLowerCase())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        return "CSVReader{" + "filepath=" + filepath + ", delimiter=" + csvDelimiter + '}';
    }
}
