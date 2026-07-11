package com.tamagotchi.code.data

data class CodingChallenge(
    val id: Int,
    val language: String,
    val type: String,
    val title: String,
    val question: String,
    val codeSnippet: String? = null,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String
)

object ChallengesData {
    val challenges = listOf(
        CodingChallenge(
            id = 1,
            language = "Kotlin",
            type = "TRIVIA",
            title = "Inmutabilidad en Kotlin",
            question = "¿Cuál es la diferencia principal entre 'val' y 'var'?",
            options = listOf(
                "val es mutable, var es inmutable",
                "val define una referencia de solo lectura (inmutable), var es mutable",
                "val es constante en tiempo de compilación, var se evalúa en ejecución",
                "No hay diferencia, son alias heredados de Java"
            ),
            correctAnswerIndex = 1,
            explanation = "En Kotlin, 'val' define una variable de solo lectura (su valor no puede ser reasignado), mientras que 'var' define una variable mutable estándar."
        ),
        CodingChallenge(
            id = 2,
            language = "Kotlin",
            type = "DEBUG",
            title = "Llamada Segura (Null Safety)",
            question = "Este código lanza un error de compilación. ¿Cómo se corrige para permitir llamadas seguras a 'length'?",
            codeSnippet = "val name: String? = null\nval len = name.length",
            options = listOf(
                "Cambiar a: val len = name?.length",
                "Cambiar a: val len = name!!?.length",
                "Cambiar a: val len = name.length()",
                "Kotlin no permite variables nulas de ningún tipo"
            ),
            correctAnswerIndex = 0,
            explanation = "Usar el operador de llamada segura '?.' (name?.length) devuelve el tamaño de la cadena si no es nula, o 'null' de lo contrario, evitando errores de puntero nulo."
        ),
        CodingChallenge(
            id = 3,
            language = "Kotlin",
            type = "TRIVIA",
            title = "Funciones de Alcance (Scope)",
            question = "¿Qué función de alcance devuelve el resultado del bloque lambda y usa 'this' como receptor?",
            options = listOf(
                "apply",
                "also",
                "run",
                "let"
            ),
            correctAnswerIndex = 2,
            explanation = "'run' ejecuta el bloque usando 'this' como receptor de llamadas y devuelve el resultado de la última expresión dentro del bloque lambda."
        ),
        CodingChallenge(
            id = 4,
            language = "JavaScript",
            type = "TRIVIA",
            title = "Tipos de Datos extraños",
            question = "¿Qué devuelve la expresión 'typeof null' en JavaScript?",
            options = listOf(
                "\"null\"",
                "\"undefined\"",
                "\"object\"",
                "\"string\""
            ),
            correctAnswerIndex = 2,
            explanation = "Históricamente en JavaScript, 'typeof null' devuelve '\"object\"'. Es considerado un error en el diseño original del lenguaje, pero no se ha cambiado por compatibilidad."
        ),
        CodingChallenge(
            id = 5,
            language = "JavaScript",
            type = "DEBUG",
            title = "Comparación Estricta",
            question = "¿Por qué la comparación estricta '0 === false' da como resultado 'false'?",
            codeSnippet = "console.log(0 == false); // true\nconsole.log(0 === false); // false",
            options = listOf(
                "Porque === hace coerción automática de tipos",
                "Porque === compara tanto el valor como el tipo sin realizar coerción",
                "Porque 0 no es un valor falsy",
                "El compilador de JS tiene un bug con números"
            ),
            correctAnswerIndex = 1,
            explanation = "El operador '==' convierte ambos lados a un tipo común (coerción), por lo que 0 y false coinciden. El operador '===' es estricto y no hace coerción, detectando que Number no es Boolean."
        ),
        CodingChallenge(
            id = 6,
            language = "JavaScript",
            type = "TRIVIA",
            title = "Clausuras (Closures)",
            question = "¿Qué es una Clausura (Closure) en JavaScript?",
            options = listOf(
                "Una función que cierra la ventana del navegador",
                "El proceso de comprimir archivos de código para producción",
                "La combinación de una función y el entorno léxico en el que fue declarada",
                "Un método reservado para destruir variables locales"
            ),
            correctAnswerIndex = 2,
            explanation = "Un closure le da a una función interna acceso al ámbito de su función externa, incluso después de que la función externa haya terminado de ejecutarse."
        ),
        CodingChallenge(
            id = 7,
            language = "PHP",
            type = "TRIVIA",
            title = "Sintaxis Básica de Variables",
            question = "¿Cuál es el prefijo obligatorio para declarar y usar cualquier variable en PHP?",
            options = listOf(
                "El signo de porcentaje (%)",
                "El signo de dólar ($)",
                "La palabra clave 'var'",
                "No hay prefijo, se define como en C++"
            ),
            correctAnswerIndex = 1,
            explanation = "En PHP, todas las variables deben comenzar con el símbolo '$' seguido del nombre de la variable."
        ),
        CodingChallenge(
            id = 8,
            language = "PHP",
            type = "DEBUG",
            title = "Fusión de Arrays",
            question = "¿Qué función nativa de PHP se utiliza para combinar dos arrays indexados preservando valores?",
            codeSnippet = "\$array1 = [1, 2];\n\$array2 = [3, 4];\n\$resultado = ???(\$array1, \$array2);",
            options = listOf(
                "array_combine",
                "array_merge",
                "array_concat",
                "implode"
            ),
            correctAnswerIndex = 1,
            explanation = "'array_merge()' combina los elementos de uno o más arrays juntos, de modo que los valores de uno se anexan al final del anterior."
        ),
        CodingChallenge(
            id = 9,
            language = "PHP",
            type = "TRIVIA",
            title = "Comparación Estricta",
            question = "¿Cuál es el resultado de '123 == \"123\"' versus '123 === \"123\"' en PHP?",
            options = listOf(
                "Ambos devuelven true",
                "Ambos devuelven false",
                "El primero da true (coerción), el segundo da false (estricto)",
                "El primero da false, el segundo da true"
            ),
            correctAnswerIndex = 2,
            explanation = "Al igual que JS, '==' en PHP hace coerción de tipos (convirtiendo el string a número para comparar), mientras que '===' valida estrictamente el tipo (int vs string)."
        ),
        CodingChallenge(
            id = 10,
            language = "Python",
            type = "TRIVIA",
            title = "Tipos de datos mutables",
            question = "¿Cuál de las siguientes estructuras de datos en Python es INMUTABLE?",
            options = listOf(
                "Lista [1, 2, 3]",
                "Diccionario {'a': 1}",
                "Tupla (1, 2, 3)",
                "Conjunto {1, 2, 3}"
            ),
            correctAnswerIndex = 2,
            explanation = "En Python, las tuplas '(1, 2, 3)' son inmutables. Una vez creadas, no se pueden modificar, añadir o quitar sus elementos."
        ),
        CodingChallenge(
            id = 11,
            language = "Python",
            type = "DEBUG",
            title = "Comprensión de Listas",
            question = "¿Cuál es el resultado de la siguiente comprensión de listas?",
            codeSnippet = "numeros = [1, 2, 3, 4]\ncuadrados = [x**2 for x in numeros if x % 2 == 0]",
            options = listOf(
                "[1, 4, 9, 16]",
                "[4, 16]",
                "[1, 9]",
                "[2, 4]"
            ),
            correctAnswerIndex = 1,
            explanation = "El bucle filtra los números pares (2 y 4) usando 'if x % 2 == 0' y luego calcula sus cuadrados (2**2 = 4 y 4**2 = 16), dando [4, 16]."
        )
    )
}
