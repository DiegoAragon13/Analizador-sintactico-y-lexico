/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.analizador_lexico_maven;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

public class Analizador {
      private static final Set<String> palabrasReservadas = new HashSet<>();
    private List<String> identificadores = new ArrayList<>();
    private List<String> numeros = new ArrayList<>();
    private List<String> operadoresAsignacion = new ArrayList<>();
    private List<String> operadoresRelacionales = new ArrayList<>();
    private List<String> operadoresAritmeticos = new ArrayList<>();
    private List<String> comentarios = new ArrayList<>();
    private List<String> palabrasReservadasEncontradas = new ArrayList<>();
    private List<String> errores = new ArrayList<>();

    static {
        String[] reservadas = {
            "array", "begin", "case", "const", "do",
            "else", "writeln", "readln", "elseif", "end",
            "for", "if", "loop", "module", "function",
            "exit", "not", "of", "mod", "record",
            "repeat", "return", "procedure", "by", "then",
            "to", "until", "var", "while", "with",
            "true", "false", "div", "integer", "real",
            "char", "string", "byte", "boolean"
        };
        Collections.addAll(palabrasReservadas, reservadas);
    }

     public void analizarCadenaCompleta(String texto) {
        resetearListas();
        
        // Expresión regular optimizada para reconocer todos los elementos
        String regex = "([a-zA-Z]+)|(\\d+)|(:=|=>)|(>|<|>=|<=|<>|==)|([+\\-*/])|(\\{o\\})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(texto);
        
        while (matcher.find()) {
            if (matcher.group(1) != null) {
                procesarPalabra(matcher.group(1));
            } else if (matcher.group(2) != null) {
                numeros.add(matcher.group(2));
            } else if (matcher.group(3) != null) {
                operadoresAsignacion.add(matcher.group(3));
            } else if (matcher.group(4) != null) {
                operadoresRelacionales.add(matcher.group(4));
            } else if (matcher.group(5) != null) {
                operadoresAritmeticos.add(matcher.group(5));
            } else if (matcher.group(6) != null) {
                comentarios.add(matcher.group(6));
            }
        }
    }

    private void procesarPalabra(String palabra) {
        String palabraLower = palabra.toLowerCase();
        if (palabrasReservadas.contains(palabraLower)) {
            palabrasReservadasEncontradas.add(palabra);
        } else {
            // Validar identificador según las nuevas reglas
            if (!esIdentificadorValido(palabra)) {
                errores.add("Identificador inválido: " + palabra);
                return;
            }
            
            // Si pasa la validación, procesar como antes
            for (char c : palabra.toCharArray()) {
                identificadores.add(String.valueOf(c));
            }
        }
    }
    
    private boolean esIdentificadorValido(String identificador) {
        // 1. Debe comenzar con una letra
        if (identificador.isEmpty() || !Character.isLetter(identificador.charAt(0))) {
            return false;
        }

        // 2. No permitir más de un guión bajo seguido
        if (identificador.contains("__")) {
            return false;
        }

        // 3. No debe terminar con guión bajo
        if (identificador.endsWith("_")) {
            return false;
        }

        // Validar caracteres permitidos (letras, números y guión bajo)
        for (char c : identificador.toCharArray()) {
            if (!(Character.isLetterOrDigit(c) || c == '_')) {
                return false;
            }
        }

        return true;
    }


     public String generarSemanticaCorrecta() {
        StringBuilder sb = new StringBuilder();
        
        // 1. Letras minúsculas ordenadas
        identificadores.stream()
            .filter(c -> Character.isLowerCase(c.charAt(0)))
            .sorted()
            .forEach(sb::append);
        
        // 2. Letras mayúsculas ordenadas
        identificadores.stream()
            .filter(c -> Character.isUpperCase(c.charAt(0)))
            .sorted()
            .forEach(sb::append);
        
        // 3. Operadores de asignación y relacionales
        sb.append(String.join("", operadoresAsignacion));
        sb.append(String.join("", operadoresRelacionales));
        
        // 4. Números ordenados descendente (cada dígito por separado)
        numeros.stream()
            .flatMap(num -> Stream.of(num.split("")))
            .map(Integer::parseInt)
            .sorted(Collections.reverseOrder())
            .map(String::valueOf)
            .forEach(sb::append);
        
        // 5. Palabras reservadas ordenadas (insensible a mayúsculas)
        sb.append(String.join("", palabrasReservadasEncontradas.stream()
            .sorted(Comparator.comparing(String::toLowerCase))
            .collect(Collectors.toList())));
        
        // 6. Operadores aritméticos
        sb.append(String.join("", operadoresAritmeticos));
        
        // 7. Comentarios
        sb.append(String.join("", comentarios));
        
        return sb.toString();
    }


    private void resetearListas() {
        identificadores.clear();
        numeros.clear();
        operadoresAsignacion.clear();
        operadoresRelacionales.clear();
        operadoresAritmeticos.clear();
        comentarios.clear();
        palabrasReservadasEncontradas.clear();
        errores.clear();
    }

    // Getters...
    public List<String> getIdentificadores() { return identificadores; }
    public List<String> getNumeros() { return numeros; }
    public List<String> getOperadoresAsignacion() { return operadoresAsignacion; }
    public List<String> getOperadoresRelacionales() { return operadoresRelacionales; }
    public List<String> getOperadoresAritmeticos() { return operadoresAritmeticos; }
    public List<String> getComentarios() { return comentarios; }
    public List<String> getPalabrasReservadasEncontradas() { return palabrasReservadasEncontradas; }
    public List<String> getErrores() { return errores; }
}