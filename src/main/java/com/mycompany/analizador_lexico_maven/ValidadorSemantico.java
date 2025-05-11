/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.analizador_lexico_maven;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 *
 * @author jorge
 */

public class ValidadorSemantico {
    private final Analizador analizador;

    public ValidadorSemantico(Analizador analizador) {
        this.analizador = analizador;
    }

    public ResultadoValidacion validar(String codigo) {
        analizador.analizarCadenaCompleta(codigo);
        String semanticaCorrecta = analizador.generarSemanticaCorrecta();
        
        List<String> errores = new ArrayList<>();
        errores.addAll(analizador.getErrores());
        
        // Validar posición de comentarios
        if (!analizador.getComentarios().isEmpty() && 
            !codigo.trim().endsWith(analizador.getComentarios().get(analizador.getComentarios().size()-1))) {
            errores.add("Los comentarios deben estar al final");
        }
        
        return new ResultadoValidacion(errores.isEmpty(), semanticaCorrecta, errores);
    }

    public static class ResultadoValidacion {
        public final boolean valido;
        public final String semanticaCorrecta;
        public final List<String> errores;
        
        public ResultadoValidacion(boolean valido, String semanticaCorrecta, List<String> errores) {
            this.valido = valido;
            this.semanticaCorrecta = semanticaCorrecta;
            this.errores = errores;
        }
    }
}