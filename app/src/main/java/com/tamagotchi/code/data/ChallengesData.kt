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
        // ========== KOTLIN ==========
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
            id = 12,
            language = "Kotlin",
            type = "TRIVIA",
            title = "Data Class",
            question = "¿Qué genera automáticamente la palabra clave 'data' en una clase?",
            options = listOf(
                "Solo getters y setters",
                "equals(), hashCode(), toString(), copy() y componentN()",
                "Un constructor sin parámetros",
                "Implementación de Serializable"
            ),
            correctAnswerIndex = 1,
            explanation = "Las 'data class' generan automáticamente equals(), hashCode(), toString(), copy(), y funciones componentN() para desestructuración."
        ),
        CodingChallenge(
            id = 13,
            language = "Kotlin",
            type = "DEBUG",
            title = "Elvis Operator",
            question = "¿Qué imprime este código?",
            codeSnippet = "val name: String? = null\nprintln(name ?: \"Invitado\")",
            options = listOf(
                "null",
                "Invitado",
                "name",
                "Error de compilación"
            ),
            correctAnswerIndex = 1,
            explanation = "El operador Elvis '?:' devuelve el lado izquierdo si no es nulo, o el derecho si el izquierdo es null. Aquí name es null, así que imprime 'Invitado'."
        ),
        CodingChallenge(
            id = 14,
            language = "Kotlin",
            type = "TRIVIA",
            title = "Funciones de Extensión",
            question = "¿Qué permite hacer una función de extensión en Kotlin?",
            options = listOf(
                "Modificar una clase sellada desde otra clase",
                "Añadir nuevas funcionalidades a una clase sin heredar de ella",
                "Crear una función que solo se ejecuta en extensiones de archivo .kt",
                "Extender el tiempo de ejecución de una función recursiva"
            ),
            correctAnswerIndex = 1,
            explanation = "Las funciones de extensión permiten agregar nuevos métodos a una clase existente sin modificar su código fuente ni heredar de ella."
        ),
        CodingChallenge(
            id = 15,
            language = "Kotlin",
            type = "DEBUG",
            title = "Null Safety en Listas",
            question = "¿Cómo obtienes el primer elemento de una lista nullable de forma segura?",
            codeSnippet = "val items: List<String>? = null\nval first = ???",
            options = listOf(
                "val first = items?.firstOrNull()",
                "val first = items!!.first()",
                "val first = items.first()",
                "val first = items?.get(0)"
            ),
            correctAnswerIndex = 0,
            explanation = "'items?.firstOrNull()' es la forma más segura: si items es null, firstOrNull() ni se llama y el resultado es null. Si no es null, devuelve el primer elemento o null si la lista está vacía."
        ),
        CodingChallenge(
            id = 16,
            language = "Kotlin",
            type = "TRIVIA",
            title = "Corrutinas - Dispatchers",
            question = "¿Cuál es el propósito de Dispatchers.IO en corrutinas de Kotlin?",
            options = listOf(
                "Ejecutar tareas de actualización de UI",
                "Ejecutar operaciones de E/S en segundo plano (archivos, red, BD)",
                "Ejecutar código de forma secuencial en el hilo principal",
                "No es un dispatcher real, solo un concepto teórico"
            ),
            correctAnswerIndex = 1,
            explanation = "Dispatchers.IO está optimizado para operaciones de entrada/salida como lecturas de archivos, llamadas de red o consultas a bases de datos."
        ),
        CodingChallenge(
            id = 17,
            language = "Kotlin",
            type = "TRIVIA",
            title = "When Expression",
            question = "¿En qué se diferencia 'when' de 'switch' en Java?",
            options = listOf(
                "when solo funciona con números enteros",
                "when puede usarse como expresión (devuelve valor) y no necesita break",
                "when requiere un bloque default obligatorio",
                "when solo funciona con tipos String"
            ),
            correctAnswerIndex = 1,
            explanation = "'when' en Kotlin es más potente que 'switch': puede usarse como expresión que devuelve valores, no necesita break, y admite cualquier tipo de condición."
        ),
        CodingChallenge(
            id = 18,
            language = "Kotlin",
            type = "TRIVIA",
            title = "Sealed Class",
            question = "¿Para qué sirve una clase sellada (sealed class) en Kotlin?",
            options = listOf(
                "Para ocultar los detalles de implementación de una clase",
                "Para restringir la jerarquía de herencia a un conjunto fijo de subtipos conocidos",
                "Para evitar que se pueda crear ninguna instancia de la clase",
                "Para marcar una clase como obsoleta y no recomendada"
            ),
            correctAnswerIndex = 1,
            explanation = "Las 'sealed class' definen una jerarquía de herencia restringida: todos los subtipos directos deben declararse en el mismo archivo, lo que permite when exhaustivo."
        ),
        CodingChallenge(
            id = 19,
            language = "Kotlin",
            type = "DEBUG",
            title = "Smart Cast",
            question = "¿Por qué este código compila sin necesidad de un cast explícito?",
            codeSnippet = "fun length(obj: Any): Int {\n    if (obj is String) return obj.length\n    return 0\n}",
            options = listOf(
                "Porque Kotlin infiere que obj es Object y Object tiene length",
                "Porque Kotlin aplica smart cast: tras verificar 'is String', trata obj como String",
                "Porque el compilador convierte Any a String automáticamente",
                "No compila, necesita (obj as String).length"
            ),
            correctAnswerIndex = 1,
            explanation = "Kotlin realiza 'smart cast': cuando verificas que una variable es de cierto tipo con 'is', el compilador la trata automáticamente como ese tipo dentro del bloque."
        ),
        CodingChallenge(
            id = 20,
            language = "Kotlin",
            type = "TRIVIA",
            title = "Flow vs LiveData",
            question = "¿Qué ventaja tiene un Flow de Kotlin sobre LiveData?",
            options = listOf(
                "Flow está atado al ciclo de vida de la Activity",
                "Flow es reactivo sin importar dependencias de Android y tiene operadores como map, filter",
                "Flow solo funciona en Java",
                "LiveData ya no se puede usar en Kotlin"
            ),
            correctAnswerIndex = 1,
            explanation = "Flow es parte de Kotlin (no de Android), por lo que es agnóstico de plataforma y ofrece operadores funcionales como map, filter, transform, etc."
        ),
        CodingChallenge(
            id = 21,
            language = "Kotlin",
            type = "TRIVIA",
            title = "Colecciones Inmutables",
            question = "¿Cuál es la diferencia entre listOf() y mutableListOf()?",
            options = listOf(
                "Ambas crean listas iguales, solo cambia el nombre",
                "listOf() crea una lista de solo lectura, mutableListOf() permite modificar elementos",
                "listOf() lanza error si la lista tiene más de 10 elementos",
                "mutableListOf() no permite elementos nulos"
            ),
            correctAnswerIndex = 1,
            explanation = "listOf() devuelve una List inmutable (solo lectura), mientras mutableListOf() devuelve una MutableList que permite agregar, quitar y modificar elementos."
        ),
        CodingChallenge(
            id = 22,
            language = "Kotlin",
            type = "TRIVIA",
            title = "Inline Functions",
            question = "¿Qué beneficio principal tienen las funciones 'inline' en Kotlin?",
            options = listOf(
                "Permiten usar herencia múltiple",
                "Eliminan la sobrecarga de crear objetos lambda al copiar el código en el punto de llamada",
                "Hacen que las funciones se ejecuten en un hilo separado",
                "Permiten que las funciones sean visibles globalmente"
            ),
            correctAnswerIndex = 1,
            explanation = "'inline' copia el cuerpo de la función directamente en el lugar de la llamada, eliminando la creación de objetos lambda y reduciendo la sobrecarga de memoria."
        ),
        CodingChallenge(
            id = 23,
            language = "Kotlin",
            type = "DEBUG",
            title = "Try como Expresión",
            question = "¿Qué valor imprime este código?",
            codeSnippet = "val result = try { \"10\".toInt() } catch (e: Exception) { 0 }\nprintln(result)",
            options = listOf(
                "Error de compilación",
                "10",
                "0",
                "\"10\""
            ),
            correctAnswerIndex = 1,
            explanation = "'try' en Kotlin es una expresión y devuelve el valor del último bloque ejecutado. '10'.toInt() tiene éxito, result = 10."
        ),
        CodingChallenge(
            id = 24,
            language = "Kotlin",
            type = "TRIVIA",
            title = "Operator Overloading",
            question = "¿Cómo se sobrecarga el operador '+' para una clase en Kotlin?",
            options = listOf(
                "Definiendo un método llamado add()",
                "Definiendo una función 'operator fun plus()'",
                "Usando la anotación @Overload",
                "No se puede sobrecargar operadores en Kotlin"
            ),
            correctAnswerIndex = 1,
            explanation = "En Kotlin los operadores se sobrecargan implementando funciones con 'operator': plus() para '+', minus() para '-', times() para '*', etc."
        ),
        CodingChallenge(
            id = 25,
            language = "Kotlin",
            type = "TRIVIA",
            title = "Scope Functions - also",
            question = "¿Qué hace diferente a 'also' respecto a 'apply'?",
            options = listOf(
                "also usa 'this', apply usa 'it'",
                "also usa 'it' como receptor, apply usa 'this'",
                "also es sincrónico, apply es asíncrono",
                "No hay diferencia, son alias"
            ),
            correctAnswerIndex = 1,
            explanation = "'apply' utiliza 'this' (receptor implícito) para configurar objetos, mientras que 'also' utiliza 'it' (parámetro explícito) y normalmente se usa para efectos secundarios."
        ),
        CodingChallenge(
            id = 86,
            language = "Kotlin",
            type = "TRIVIA",
            title = "Object Keyword",
            question = "¿Para qué sirve la palabra clave 'object' en Kotlin?",
            options = listOf(
                "Solo para crear nuevos objetos de una clase existente",
                "Para declarar una clase singleton (única instancia global)",
                "Es el equivalente de 'new' en Java",
                "Para marcar un método como obsoleto"
            ),
            correctAnswerIndex = 1,
            explanation = "'object' declara una clase singleton: solo existe una instancia de ella, creada de forma perezosa y segura al primer acceso."
        ),
        CodingChallenge(
            id = 87,
            language = "Kotlin",
            type = "DEBUG",
            title = "Lambda y fold",
            question = "¿Qué resultado produce esta operación?",
            codeSnippet = "val nums = listOf(1, 2, 3, 4)\nval sum = nums.fold(0) { acc, i -> acc + i }\nprintln(sum)",
            options = listOf(
                "10",
                "0",
                "1234",
                "Error de compilación"
            ),
            correctAnswerIndex = 0,
            explanation = "fold(0) itera con valor inicial 0: 0+1=1, 1+2=3, 3+3=6, 6+4=10. El resultado es la suma total 10."
        ),

        // ========== JAVASCRIPT ==========
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
            id = 26,
            language = "JavaScript",
            type = "TRIVIA",
            title = "let vs var",
            question = "¿Cuál es la diferencia principal entre 'let' y 'var'?",
            options = listOf(
                "No hay diferencia, let es solo una alternativa moderna",
                "let tiene ámbito de bloque, var tiene ámbito de función",
                "var no puede usarse en navegadores modernos",
                "let solo funciona dentro de objetos"
            ),
            correctAnswerIndex = 1,
            explanation = "'let' tiene ámbito de bloque (solo existe dentro de {}), mientras que 'var' tiene ámbito de función y sufre hoisting."
        ),
        CodingChallenge(
            id = 27,
            language = "JavaScript",
            type = "DEBUG",
            title = "Promesas - then",
            question = "¿Qué imprime el siguiente código?",
            codeSnippet = "Promise.resolve(1)\n  .then(x => x + 1)\n  .then(x => console.log(x))",
            options = listOf(
                "1",
                "2",
                "undefined",
                "Promise {<pending>}"
            ),
            correctAnswerIndex = 1,
            explanation = "Promise.resolve(1) crea una promesa con valor 1. El primer then suma 1 (resultado 2). El segundo then imprime 2."
        ),
        CodingChallenge(
            id = 28,
            language = "JavaScript",
            type = "TRIVIA",
            title = "Spread Operator",
            question = "¿Qué hace el operador '...' (spread) en un array?",
            options = listOf(
                "Concatena todos los arrays de la aplicación",
                "Expande un array en sus elementos individuales",
                "Elimina el último elemento del array",
                "Crea una copia vacía del array"
            ),
            correctAnswerIndex = 1,
            explanation = "El spread operator '...' expande un iterable (como un array) en sus elementos individuales, útil para copiar o combinar arrays."
        ),
        CodingChallenge(
            id = 29,
            language = "JavaScript",
            type = "TRIVIA",
            title = "Template Literals",
            question = "¿Cómo se interpola una variable en un string con template literals?",
            options = listOf(
                "\"Hola \" + nombre + \"!\"",
                "`Hola \${nombre}!`",
                "'Hola {nombre}!'",
                "\"Hola %nombre%!\""
            ),
            correctAnswerIndex = 1,
            explanation = "Los template literals usan backticks ` y la sintaxis \${} para interpolar variables o expresiones directamente en el string."
        ),
        CodingChallenge(
            id = 30,
            language = "JavaScript",
            type = "TRIVIA",
            title = "Arrow Functions",
            question = "¿Qué diferencia a una arrow function de una función tradicional?",
            options = listOf(
                "No puede tener parámetros",
                "No tiene su propio 'this', hereda del ámbito padre",
                "Solo funciona con números",
                "No puede devolver valores"
            ),
            correctAnswerIndex = 1,
            explanation = "Las arrow functions no tienen su propio 'this', heredándolo del ámbito circundante. Tampoco tienen 'arguments' ni pueden usarse como constructoras."
        ),
        CodingChallenge(
            id = 31,
            language = "JavaScript",
            type = "DEBUG",
            title = "Hoisting en var",
            question = "¿Qué devuelve este código?",
            codeSnippet = "console.log(x)\nvar x = 5",
            options = listOf(
                "5",
                "undefined",
                "ReferenceError: x is not defined",
                "null"
            ),
            correctAnswerIndex = 1,
            explanation = "Con 'var', la declaración se eleva (hoisting) al inicio, pero la asignación no. Al llegar al console.log, x está declarada pero su valor es undefined."
        ),
        CodingChallenge(
            id = 32,
            language = "JavaScript",
            type = "TRIVIA",
            title = "Destructuring Assignment",
            question = "¿Qué hace la desestructuración (destructuring) en arrays?",
            options = listOf(
                "Elimina elementos no deseados del array",
                "Extrae valores del array en variables individuales con una sintaxis concisa",
                "Cifra los datos del array para seguridad",
                "Convierte el array en un objeto"
            ),
            correctAnswerIndex = 1,
            explanation = "La desestructuración permite extraer valores de arrays u objetos en variables individuales: const [a, b] = [1, 2] asigna 1 a 'a' y 2 a 'b'."
        ),
        CodingChallenge(
            id = 33,
            language = "JavaScript",
            type = "TRIVIA",
            title = "Método map",
            question = "¿Qué devuelve el método 'map' de un array?",
            options = listOf(
                "Un nuevo array con el mismo número de elementos transformados",
                "Un nuevo array solo con los elementos que cumplen una condición",
                "El primer elemento que cumple una condición",
                "Un booleano indicando si todos los elementos cumplen la condición"
            ),
            correctAnswerIndex = 0,
            explanation = "'map' itera sobre cada elemento y aplica una función, devolviendo un nuevo array de la misma longitud con los valores transformados."
        ),
        CodingChallenge(
            id = 34,
            language = "JavaScript",
            type = "DEBUG",
            title = "Event Loop",
            question = "¿En qué orden se imprimen estos mensajes?",
            codeSnippet = "console.log('A')\nsetTimeout(() => console.log('B'), 0)\nconsole.log('C')",
            options = listOf(
                "A, B, C",
                "B, A, C",
                "A, C, B",
                "C, B, A"
            ),
            correctAnswerIndex = 2,
            explanation = "A y C son síncronos (se ejecutan primero). setTimeout, con delay 0, pasa a la cola de tareas y se ejecuta después de completar el stack síncrono."
        ),
        CodingChallenge(
            id = 35,
            language = "JavaScript",
            type = "TRIVIA",
            title = "falsy Values",
            question = "¿Cuántos de estos valores son falsy en JavaScript? 0, '', false, null, undefined, NaN, []",
            options = listOf(
                "Todos son falsy",
                "6 excepto [] (los arrays vacíos son truthy)",
                "4: 0, '', false, null",
                "Solo 0 y false"
            ),
            correctAnswerIndex = 1,
            explanation = "Los valores falsy son: 0, '' (string vacío), false, null, undefined, NaN (6 en total). [] (array vacío) es truthy."
        ),
        CodingChallenge(
            id = 36,
            language = "JavaScript",
            type = "TRIVIA",
            title = "Fetch API",
            question = "¿Cómo se maneja la respuesta de una llamada fetch?",
            options = listOf(
                "fetch devuelve directamente el JSON",
                "fetch devuelve una Promise que resuelve a un objeto Response",
                "fetch es síncrono y bloquea el hilo",
                "fetch solo funciona con archivos locales"
            ),
            correctAnswerIndex = 1,
            explanation = "fetch() devuelve una Promise que resuelve a un objeto Response. Para obtener el JSON, debes llamar a response.json() (también una Promise)."
        ),
        CodingChallenge(
            id = 37,
            language = "JavaScript",
            type = "TRIVIA",
            title = "Coerción Implícita",
            question = "¿Qué devuelve '5' - 3 en JavaScript?",
            options = listOf(
                "\"53\"",
                "2",
                "\"5-3\"",
                "Error TypeError"
            ),
            correctAnswerIndex = 1,
            explanation = "Con el operador '-', JavaScript convierte el string '5' a número (5) y resta 3, dando 2. En cambio, '5' + 3 daría \"53\" por concatenación."
        ),
        CodingChallenge(
            id = 38,
            language = "JavaScript",
            type = "DEBUG",
            title = "Objeto this en función",
            question = "¿Qué imprime este código (en el navegador)?",
            codeSnippet = "function foo() {\n  console.log(this)\n}\nfoo()",
            options = listOf(
                "undefined",
                "El objeto global (window)",
                "null",
                "El objeto foo"
            ),
            correctAnswerIndex = 1,
            explanation = "En una función normal no estricta, 'this' dentro de la función hace referencia al objeto global (window en navegador). En strict mode sería undefined."
        ),
        CodingChallenge(
            id = 39,
            language = "JavaScript",
            type = "TRIVIA",
            title = "JSON.stringify",
            question = "¿Qué hace JSON.stringify()?",
            options = listOf(
                "Convierte un string JSON en un objeto JavaScript",
                "Convierte un objeto JavaScript en un string JSON",
                "Valida si un string es JSON válido",
                "Minifica un string quitando espacios"
            ),
            correctAnswerIndex = 1,
            explanation = "JSON.stringify() serializa un objeto JavaScript a su representación en string JSON. Lo opuesto es JSON.parse()."
        ),
        CodingChallenge(
            id = 40,
            language = "JavaScript",
            type = "TRIVIA",
            title = "null vs undefined",
            question = "¿Cuál es la diferencia entre null y undefined?",
            options = listOf(
                "Son exactamente iguales, sinónimos",
                "undefined significa 'no asignado', null significa 'vacío intencional'",
                "null solo existe en strict mode",
                "undefined es un string, null es un objeto"
            ),
            correctAnswerIndex = 1,
            explanation = "undefined indica que una variable se declaró pero no se le asignó valor. null es un valor asignado intencionalmente para indicar 'sin valor'."
        ),

        // ========== PHP ==========
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
            id = 41,
            language = "PHP",
            type = "TRIVIA",
            title = "Variables Superglobales",
            question = "¿Qué superglobal contiene los datos enviados por un formulario con método POST?",
            options = listOf(
                "\$_GET",
                "\$_POST",
                "\$_SERVER",
                "\$_REQUEST"
            ),
            correctAnswerIndex = 1,
            explanation = "\$_POST contiene los datos de formularios enviados con method='post', incluyendo archivos (combinado con \$_FILES)."
        ),
        CodingChallenge(
            id = 42,
            language = "PHP",
            type = "TRIVIA",
            title = "foreach con Arrays",
            question = "¿Cuál es la sintaxis correcta para iterar un array asociativo?",
            options = listOf(
                "foreach(\$array as \$value)",
                "foreach(\$array as \$key => \$value)",
                "for(\$i = 0; \$i < count(\$array); \$i++)",
                "while(\$item = next(\$array))"
            ),
            correctAnswerIndex = 1,
            explanation = "La sintaxis foreach(\$array as \$key => \$value) itera sobre arrays asociativos dando acceso tanto a la clave como al valor."
        ),
        CodingChallenge(
            id = 43,
            language = "PHP",
            type = "DEBUG",
            title = "isset vs empty",
            question = "¿Qué función devuelve true si una variable existe y no es null?",
            codeSnippet = "\$var = 0;\nvar_dump(isset(\$var)); // true\nvar_dump(empty(\$var)); // ?",
            options = listOf(
                "true porque 0 es un valor válido",
                "false porque 0 se considera vacío",
                "true porque empty solo revisa si existe",
                "Lanza un error de tipo"
            ),
            correctAnswerIndex = 1,
            explanation = "empty(\$var) devuelve true para valores 'vacíos': \"\", 0, \"0\", null, false, array(), y variables no definidas. Como \$var = 0, empty() devuelve true."
        ),
        CodingChallenge(
            id = 44,
            language = "PHP",
            type = "TRIVIA",
            title = "Clases y Objetos",
            question = "¿Cómo se define una clase en PHP?",
            options = listOf(
                "class Persona { }",
                "new class Persona() { }",
                "function Persona() { }",
                "type Persona struct { }"
            ),
            correctAnswerIndex = 0,
            explanation = "En PHP las clases se definen con 'class NombreClase { }', similar a otros lenguajes como Java o C++."
        ),
        CodingChallenge(
            id = 45,
            language = "PHP",
            type = "TRIVIA",
            title = "Herencia en PHP",
            question = "¿Qué palabra clave se usa para heredar de una clase padre?",
            options = listOf(
                "implements",
                "extends",
                "inherits",
                "parent"
            ),
            correctAnswerIndex = 1,
            explanation = "PHP usa 'extends' para la herencia de clases: class Hijo extends Padre { }. Para interfaces se usa 'implements'."
        ),
        CodingChallenge(
            id = 46,
            language = "PHP",
            type = "DEBUG",
            title = "echo vs print_r",
            question = "¿Qué función es más adecuada para inspeccionar el contenido de un array?",
            codeSnippet = "\$datos = ['a' => 1, 'b' => 2];\necho \$datos; // Error\n???",
            options = listOf(
                "echo solo sirve para strings, usa print_r(\$datos) o var_dump(\$datos)",
                "Usa echo \$datos con comillas dobles",
                "Los arrays no se pueden inspeccionar en PHP",
                "Usa console.log(\$datos)"
            ),
            correctAnswerIndex = 0,
            explanation = "echo solo imprime strings. Para ver el contenido de un array u objeto se usa print_r() o var_dump(), que muestran la estructura completa."
        ),
        CodingChallenge(
            id = 47,
            language = "PHP",
            type = "TRIVIA",
            title = "Sesiones en PHP",
            question = "¿Qué función inicia o reanuda una sesión en PHP?",
            options = listOf(
                "start_session()",
                "session_start()",
                "begin_session()",
                "init_session()"
            ),
            correctAnswerIndex = 1,
            explanation = "session_start() inicia una nueva sesión o reanuda la existente basada en el ID de sesión (en cookie o URL)."
        ),
        CodingChallenge(
            id = 48,
            language = "PHP",
            type = "TRIVIA",
            title = "include vs require",
            question = "¿Cuál es la diferencia entre include y require?",
            options = listOf(
                "include incluye el archivo, require lo ejecuta",
                "require lanza un error fatal si el archivo no existe, include solo una advertencia",
                "No hay diferencia, son alias",
                "include carga el archivo de forma asíncrona"
            ),
            correctAnswerIndex = 1,
            explanation = "Ambos incluyen archivos, pero require produce un error fatal (E_COMPILE_ERROR) si falla, deteniendo el script. include solo emite una advertencia (E_WARNING)."
        ),
        CodingChallenge(
            id = 49,
            language = "PHP",
            type = "TRIVIA",
            title = "Arrays Asociativos",
            question = "¿Cómo se accede al valor con clave 'nombre' en un array asociativo?",
            options = listOf(
                "\$persona->nombre",
                "\$persona['nombre']",
                "\$persona{nombre}",
                "\$persona::nombre"
            ),
            correctAnswerIndex = 1,
            explanation = "Los arrays asociativos en PHP usan corchetes con la clave entre comillas: \$persona['nombre']. La flecha '->' se usa para propiedades de objetos."
        ),
        CodingChallenge(
            id = 50,
            language = "PHP",
            type = "DEBUG",
            title = "Concatenación de Strings",
            question = "¿Cuál es el operador de concatenación en PHP?",
            codeSnippet = "\$a = 'Hola ';\n\$b = 'Mundo';\n\$c = ???",
            options = listOf(
                "\$a + \$b",
                "\$a . \$b",
                "\$a & \$b",
                "\$a :: \$b"
            ),
            correctAnswerIndex = 1,
            explanation = "PHP usa el punto (.) como operador de concatenación de strings: 'Hola ' . 'Mundo' produce 'Hola Mundo'."
        ),
        CodingChallenge(
            id = 51,
            language = "PHP",
            type = "TRIVIA",
            title = "Funciones Variables",
            question = "¿Cómo se llama una función cuyo nombre está en una variable?",
            options = listOf(
                "\$nombreFuncion()",
                "call(\$nombreFuncion)",
                "invoke(\$nombreFuncion)",
                "No es posible en PHP"
            ),
            correctAnswerIndex = 0,
            explanation = "PHP permite llamar a funciones con nombre dinámico: si \$nombreFuncion = 'strlen', entonces \$nombreFuncion('hola') llama a strlen('hola')."
        ),
        CodingChallenge(
            id = 52,
            language = "PHP",
            type = "TRIVIA",
            title = "Métodos Estáticos",
            question = "¿Cómo se accede a un método estático desde dentro de la misma clase?",
            options = listOf(
                "\$this->metodo()",
                "self::metodo()",
                "static.metodo()",
                "class::metodo()"
            ),
            correctAnswerIndex = 1,
            explanation = "Dentro de una clase, los métodos y propiedades estáticos se acceden con self:: o static:: (late static binding)."
        ),
        CodingChallenge(
            id = 53,
            language = "PHP",
            type = "TRIVIA",
            title = "PDO y Prepared Statements",
            question = "¿Qué beneficio principal tienen los prepared statements en PDO?",
            options = listOf(
                "Son más rápidos que las consultas normales",
                "Previenen la inyección SQL separando la estructura SQL de los datos",
                "Permiten conectar múltiples bases de datos a la vez",
                "No requieren conexión a la base de datos"
            ),
            correctAnswerIndex = 1,
            explanation = "Los prepared statements envían la estructura de la consulta por separado de los datos, evitando que datos maliciosos alteren la intención de la SQL."
        ),
        CodingChallenge(
            id = 54,
            language = "PHP",
            type = "DEBUG",
            title = "Cookies en PHP",
            question = "¿Qué función establece una cookie en PHP?",
            codeSnippet = "// ¿Cuál es la función correcta?\n???('usuario', 'juan', time() + 3600)",
            options = listOf(
                "set_cookie()",
                "setcookie()",
                "cookie_set()",
                "http_set_cookie()"
            ),
            correctAnswerIndex = 1,
            explanation = "setcookie() es la función nativa de PHP para establecer cookies. Debe llamarse antes de cualquier salida HTML."
        ),
        CodingChallenge(
            id = 55,
            language = "PHP",
            type = "TRIVIA",
            title = "Operador Ternario",
            question = "¿Cuál es la sintaxis del operador ternario en PHP?",
            options = listOf(
                "cond ? if_true : if_false",
                "cond :: if_true :: if_false",
                "if cond then if_true else if_false",
                "cond -> if_true -> if_false"
            ),
            correctAnswerIndex = 0,
            explanation = "PHP usa la misma sintaxis que C/Java: condición ? valor_si_verdadero : valor_si_falso."
        ),

        // ========== PYTHON ==========
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
        ),
        CodingChallenge(
            id = 56,
            language = "Python",
            type = "TRIVIA",
            title = "PEP 8 - Nombres",
            question = "¿Cuál es la convención PEP 8 para nombrar funciones y variables en Python?",
            options = listOf(
                "camelCase",
                "snake_case",
                "PascalCase",
                "kebab-case"
            ),
            correctAnswerIndex = 1,
            explanation = "PEP 8 recomienda usar snake_case (palabras_separadas_por_guiones_bajos) para funciones, variables y métodos. PascalCase para clases."
        ),
        CodingChallenge(
            id = 57,
            language = "Python",
            type = "TRIVIA",
            title = "Listas vs Tuplas",
            question = "¿Por qué las tuplas son más rápidas que las listas en ciertos contextos?",
            options = listOf(
                "Las tuplas se almacenan en disco, no en memoria",
                "Las tuplas son inmutables, Python puede optimizar su almacenamiento y acceso",
                "Las tuplas no tienen métodos, solo funciones globales",
                "Las tuplas se compilan a código máquina"
            ),
            correctAnswerIndex = 1,
            explanation = "Al ser inmutables, Python puede hacer optimizaciones en la memoria de las tuplas. No necesitan espacio adicional para posibles modificaciones."
        ),
        CodingChallenge(
            id = 58,
            language = "Python",
            type = "TRIVIA",
            title = "with Statement",
            question = "¿Para qué sirve la sentencia 'with' en Python?",
            options = listOf(
                "Para ejecutar bloques de código en paralelo",
                "Para manejar recursos (archivos, conexiones) asegurando su cierre automático",
                "Para declarar variables dentro de un ámbito temporal",
                "Es sinónimo de 'if' en versiones modernas"
            ),
            correctAnswerIndex = 1,
            explanation = "'with' usa context managers para asegurar que los recursos se limpien correctamente al salir del bloque, como cerrar archivos automáticamente."
        ),
        CodingChallenge(
            id = 59,
            language = "Python",
            type = "DEBUG",
            title = "Mutable como default",
            question = "¿Qué problema tiene esta función?",
            codeSnippet = "def add_item(item, lista=[]):\n    lista.append(item)\n    return lista",
            options = listOf(
                "No hay problema, funciona correctamente",
                "El argumento default mutable se comparte entre todas las llamadas",
                "append no existe para listas",
                "No se puede asignar un default a un parámetro"
            ),
            correctAnswerIndex = 1,
            explanation = "Los argumentos default se evalúan una vez al definir la función. Si el default es mutable, todas las llamadas comparten la misma lista."
        ),
        CodingChallenge(
            id = 60,
            language = "Python",
            type = "TRIVIA",
            title = "args y kwargs",
            question = "¿Qué hace *args en una función de Python?",
            options = listOf(
                "Convierte los argumentos en un array de NumPy",
                "Permite pasar un número variable de argumentos posicionales como una tupla",
                "Indica que la función es privada",
                "Multiplica los valores de los argumentos"
            ),
            correctAnswerIndex = 1,
            explanation = "*args recoge los argumentos posicionales extra en una tupla. **kwargs recoge argumentos con nombre en un diccionario."
        ),
        CodingChallenge(
            id = 61,
            language = "Python",
            type = "TRIVIA",
            title = "Decoradores",
            question = "¿Qué es un decorador en Python?",
            options = listOf(
                "Una función que modifica el comportamiento de otra función",
                "Un método especial como __init__",
                "Una clase que extiende funcionalidades del sistema",
                "Una sintaxis para declarar variables constantes"
            ),
            correctAnswerIndex = 0,
            explanation = "Un decorador es una función que recibe otra función y extiende su comportamiento sin modificar su código fuente, usando @nombre_decorador."
        ),
        CodingChallenge(
            id = 62,
            language = "Python",
            type = "DEBUG",
            title = "range en bucles",
            question = "¿Qué imprime este código?",
            codeSnippet = "for i in range(3):\n    print(i, end=' ')",
            options = listOf(
                "1 2 3",
                "0 1 2",
                "0 1 2 3",
                "1 2"
            ),
            correctAnswerIndex = 1,
            explanation = "range(3) genera los números 0, 1, 2 (stop exclusivo). Por defecto empieza en 0. Imprime '0 1 2'."
        ),
        CodingChallenge(
            id = 63,
            language = "Python",
            type = "TRIVIA",
            title = "Diccionarios",
            question = "¿Cómo se obtiene un valor de un diccionario de forma segura (sin KeyError)?",
            options = listOf(
                "dict.valor('clave')",
                "dict.get('clave', default)",
                "dict['clave'] con try-except obligatorio",
                "dict.fetch('clave')"
            ),
            correctAnswerIndex = 1,
            explanation = "dict.get('clave', default) devuelve el valor si la clave existe, o el default (None si no se especifica) sin lanzar excepción."
        ),
        CodingChallenge(
            id = 64,
            language = "Python",
            type = "TRIVIA",
            title = "F-Strings",
            question = "¿Cuál es la sintaxis correcta de un f-string?",
            options = listOf(
                "f'Hola {nombre}'",
                "F('Hola %s', nombre)",
                "'Hola {nombre}'.format(nombre)",
                "printf('Hola', nombre)"
            ),
            correctAnswerIndex = 0,
            explanation = "Los f-strings (Python 3.6+) se escriben con prefijo 'f' o 'F' y usan { } para interpolar variables o expresiones."
        ),
        CodingChallenge(
            id = 65,
            language = "Python",
            type = "TRIVIA",
            title = "Excepciones",
            question = "¿Cuál es la sintaxis correcta para capturar una excepción?",
            options = listOf(
                "try { } catch (e) { }",
                "try: ... except Exception as e: ...",
                "try: ... catch (e): ...",
                "attempt: ... except: ..."
            ),
            correctAnswerIndex = 1,
            explanation = "Python usa 'try: ... except TipoError as e: ...' para el manejo de excepciones. No usa llaves, sino indentación."
        ),
        CodingChallenge(
            id = 66,
            language = "Python",
            type = "TRIVIA",
            title = "Herencia Múltiple",
            question = "¿Python soporta herencia múltiple?",
            options = listOf(
                "No, Python no permite herencia múltiple",
                "Sí, class Hijo(Padre1, Padre2):",
                "Solo si se usa el decorador @multiple",
                "Sí, pero solo con clases abstractas"
            ),
            correctAnswerIndex = 1,
            explanation = "Python soporta herencia múltiple: class Hijo(Padre1, Padre2):. El MRO (Method Resolution Order) determina el orden de búsqueda."
        ),
        CodingChallenge(
            id = 67,
            language = "Python",
            type = "TRIVIA",
            title = "Generadores y yield",
            question = "¿Qué diferencia a un generador de una función normal?",
            options = listOf(
                "Los generadores no pueden tener parámetros",
                "Los generadores usan yield y devuelven un iterador que produce valores bajo demanda",
                "Los generadores solo funcionan con números enteros",
                "No hay diferencia, son lo mismo"
            ),
            correctAnswerIndex = 1,
            explanation = "Un generador usa 'yield' en lugar de 'return' y produce una secuencia de valores sobre la que se puede iterar, manteniendo su estado entre llamadas."
        ),
        CodingChallenge(
            id = 68,
            language = "Python",
            type = "DEBUG",
            title = "Copias superficiales",
            question = "¿Qué sucede al modificar una lista dentro de una copia con list()?",
            codeSnippet = "original = [[1, 2], [3, 4]]\ncopia = list(original)\ncopia[0][0] = 99\nprint(original[0][0])",
            options = listOf(
                "1 (la copia es independiente)",
                "99 (list() hace copia superficial, los elementos internos se comparten)",
                "Error: las listas anidadas no se pueden copiar",
                "None"
            ),
            correctAnswerIndex = 1,
            explanation = "list() hace una copia superficial (shallow copy). Las listas internas son los mismos objetos, por lo que modificar una afecta a la otra."
        ),
        CodingChallenge(
            id = 69,
            language = "Python",
            type = "TRIVIA",
            title = "Sets en Python",
            question = "¿Qué característica define a un set (conjunto) en Python?",
            options = listOf(
                "Mantiene el orden de inserción y permite duplicados",
                "No permite elementos duplicados y no tiene orden definido",
                "Solo puede contener strings",
                "Es lo mismo que una lista pero con métodos adicionales"
            ),
            correctAnswerIndex = 1,
            explanation = "Un set es una colección no ordenada de elementos únicos. Se usa para pruebas de pertenencia y operaciones de conjuntos (unión, intersección)."
        ),
        CodingChallenge(
            id = 70,
            language = "Python",
            type = "TRIVIA",
            title = "Módulos y Paquetes",
            question = "¿Qué archivo necesita un directorio para ser considerado un paquete en Python?",
            options = listOf(
                "package.json",
                "__init__.py",
                "main.py",
                "setup.py"
            ),
            correctAnswerIndex = 1,
            explanation = "El archivo __init__.py (que puede estar vacío) indica que un directorio es un paquete de Python, permitiendo importar sus módulos."
        ),
        CodingChallenge(
            id = 71,
            language = "Python",
            type = "DEBUG",
            title = "División en Python 3",
            question = "¿Cuál es el resultado de 5 / 2 en Python 3?",
            codeSnippet = "resultado = 5 / 2\nprint(resultado)",
            options = listOf(
                "2",
                "2.5",
                "2.0",
                "Error: división no permitida"
            ),
            correctAnswerIndex = 1,
            explanation = "En Python 3, '/' siempre devuelve un float (2.5). Para división entera se usa '//' (5 // 2 = 2)."
        ),
        CodingChallenge(
            id = 72,
            language = "Python",
            type = "TRIVIA",
            title = "Métodos de Clase",
            question = "¿Cómo se define un método de clase en Python?",
            options = listOf(
                "Con el decorador @staticmethod",
                "Con el decorador @classmethod y 'cls' como primer parámetro",
                "Con la palabra clave 'class' dentro del método",
                "No se pueden definir métodos de clase"
            ),
            correctAnswerIndex = 1,
            explanation = "@classmethod recibe la clase (cls) como primer argumento, a diferencia de @staticmethod que no recibe ni cls ni self."
        ),
        CodingChallenge(
            id = 73,
            language = "Python",
            type = "TRIVIA",
            title = "zip function",
            question = "¿Qué hace la función zip() en Python?",
            options = listOf(
                "Comprime archivos en formato ZIP",
                "Combina varios iterables en tuplas, paralelamente",
                "Ordena los elementos de una lista",
                "Convierte strings a números"
            ),
            correctAnswerIndex = 1,
            explanation = "zip() toma varios iterables y devuelve un iterador de tuplas, donde la i-ésima tupla contiene el i-ésimo elemento de cada iterable."
        ),
        CodingChallenge(
            id = 74,
            language = "Python",
            type = "TRIVIA",
            title = "Variables Globales",
            question = "¿Cómo se modifica una variable global dentro de una función?",
            options = listOf(
                "Simplemente asignándole un nuevo valor",
                "Usando la palabra clave 'global' seguida del nombre de la variable",
                "Usando 'var' delante del nombre",
                "No se puede modificar variables globales desde funciones"
            ),
            correctAnswerIndex = 1,
            explanation = "Para modificar una variable global dentro de una función, debe declararse con 'global nombre_variable' antes de asignarle un valor."
        ),
        CodingChallenge(
            id = 75,
            language = "Python",
            type = "TRIVIA",
            title = "Comprensión de Diccionarios",
            question = "¿Cuál es la sintaxis de una comprensión de diccionarios?",
            options = listOf(
                "{k.upper(): v for k, v in dict.items()}",
                "[k: v for k, v in dict.items()]",
                "dict(k.upper(): v for k, v in dict.items())",
                "for k, v in dict.items(): {k: v}"
            ),
            correctAnswerIndex = 0,
            explanation = "La comprensión de diccionarios usa llaves {} con clave: valor: {clave: valor for elemento in iterable}. Similar a listas pero con :."
        ),
        CodingChallenge(
            id = 76,
            language = "Python",
            type = "TRIVIA",
            title = "lambda",
            question = "¿Qué es una función lambda en Python?",
            options = listOf(
                "Una función anónima de una sola expresión",
                "Una función que puede tener múltiples líneas y decoradores",
                "El operador de la división de enteros",
                "Una función asíncrona con await"
            ),
            correctAnswerIndex = 0,
            explanation = "lambda crea funciones anónimas de una sola línea: lambda args: expresión. Equivale a una función pequeña y descartable."
        ),
        CodingChallenge(
            id = 77,
            language = "Python",
            type = "TRIVIA",
            title = "re.match vs re.search",
            question = "¿Cuál es la diferencia entre re.match() y re.search()?",
            options = listOf(
                "match busca en todo el string, search busca solo al inicio",
                "match solo busca al inicio del string, search busca en todo el string",
                "Ambos hacen lo mismo, match es obsoleto",
                "match usa regex, search usa coincidencia exacta"
            ),
            correctAnswerIndex = 1,
            explanation = "re.match() verifica solo desde el principio del string; re.search() busca en todo el string la primera coincidencia."
        ),
        CodingChallenge(
            id = 78,
            language = "Python",
            type = "TRIVIA",
            title = "assert en Python",
            question = "¿Qué hace la sentencia 'assert'?",
            options = listOf(
                "Detiene la ejecución si la condición es verdadera",
                "Lanza AssertionError si la condición es falsa, usado para depuración",
                "Afirma que una variable existe en el ámbito global",
                "Declara una variable como constante"
            ),
            correctAnswerIndex = 1,
            explanation = "assert condición, mensaje lanza AssertionError con el mensaje si la condición es falsa. Se usa para validar invariantes en tiempo de desarrollo."
        ),
        CodingChallenge(
            id = 79,
            language = "Python",
            type = "TRIVIA",
            title = "enumerate en Bucles",
            question = "¿Qué hace la función enumerate() en un bucle for?",
            options = listOf(
                "Devuelve el índice y el valor de cada elemento",
                "Cuenta cuántos elementos hay en el iterable",
                "Asigna un número único a cada elemento",
                "Ordena los elementos numéricamente"
            ),
            correctAnswerIndex = 0,
            explanation = "enumerate() devuelve tuplas (índice, valor) para cada elemento, evitando tener que usar una variable contadora manual."
        ),
        CodingChallenge(
            id = 80,
            language = "Python",
            type = "DEBUG",
            title = "Mutable vs Inmutable",
            question = "¿Qué imprime este código?",
            codeSnippet = "a = [1, 2, 3]\nb = a\na.append(4)\nprint(b)",
            options = listOf(
                "[1, 2, 3]",
                "[1, 2, 3, 4]",
                "[4]",
                "Error: las listas no se pueden asignar"
            ),
            correctAnswerIndex = 1,
            explanation = "b = a no copia la lista, sino que b referencia la misma lista que a. Modificar a afecta a b. Para copiar se usa a.copy() o list(a)."
        ),
        CodingChallenge(
            id = 81,
            language = "Python",
            type = "TRIVIA",
            title = "sys.argv",
            question = "¿Qué contiene sys.argv?",
            options = listOf(
                "Los argumentos de línea de comandos pasados al script",
                "Las variables de entorno del sistema",
                "Los módulos importados actualmente",
                "Las rutas de búsqueda de Python"
            ),
            correctAnswerIndex = 0,
            explanation = "sys.argv es una lista con los argumentos de línea de comandos. sys.argv[0] es el nombre del script, sys.argv[1:] son los argumentos."
        ),
        CodingChallenge(
            id = 82,
            language = "Python",
            type = "TRIVIA",
            title = "Python y JSON",
            question = "¿Qué función convierte un string JSON a un objeto Python?",
            options = listOf(
                "json.stringify()",
                "json.loads()",
                "json.parse()",
                "json.decode()"
            ),
            correctAnswerIndex = 1,
            explanation = "json.loads() (load string) convierte un string JSON en un objeto Python. json.dumps() hace la operación inversa (objeto a string)."
        ),
        CodingChallenge(
            id = 83,
            language = "Python",
            type = "TRIVIA",
            title = "Herencia y super()",
            question = "¿Para qué sirve super() en Python?",
            options = listOf(
                "Para llamar a la versión de la clase padre de un método",
                "Para declarar una clase como superior en jerarquía",
                "Para mejorar el rendimiento de los métodos",
                "No existe super() en Python"
            ),
            correctAnswerIndex = 0,
            explanation = "super() devuelve un objeto proxy que delega las llamadas de métodos a la clase padre, siguiendo el MRO (Method Resolution Order)."
        ),
        CodingChallenge(
            id = 84,
            language = "Python",
            type = "TRIVIA",
            title = "Virtualenv",
            question = "¿Cuál es el propósito de un entorno virtual (virtualenv)?",
            options = listOf(
                "Crear una máquina virtual para ejecutar Python",
                "Aislar las dependencias de un proyecto Python del sistema global",
                "Simular un entorno de producción en local",
                "Acelerar la ejecución del código Python"
            ),
            correctAnswerIndex = 1,
            explanation = "Un entorno virtual aísla las dependencias de Python de cada proyecto, evitando conflictos entre versiones de bibliotecas."
        ),
        CodingChallenge(
            id = 85,
            language = "Python",
            type = "TRIVIA",
            title = "Type Hints",
            question = "¿Cómo se indica el tipo de retorno de una función con type hints?",
            options = listOf(
                "def suma(a: int, b: int) -> int:",
                "def suma(int a, int b) returns int:",
                "def suma(a: int, b: int) :: int",
                "@typedef suma(a: int, b: int) -> int"
            ),
            correctAnswerIndex = 0,
            explanation = "Los type hints usan ': tipo' para parámetros y '-> tipo' para el retorno: def suma(a: int, b: int) -> int:"
        ),
        CodingChallenge(
            id = 88,
            language = "Python",
            type = "DEBUG",
            title = "Excepción en división",
            question = "¿Qué excepción lanza Python al dividir por cero?",
            codeSnippet = "resultado = 10 / 0",
            options = listOf(
                "ValueError",
                "ZeroDivisionError",
                "TypeError",
                "ArithmeticError (genérica)"
            ),
            correctAnswerIndex = 1,
            explanation = "Python lanza ZeroDivisionError cuando se intenta dividir por cero. Es un subtipo de ArithmeticError."
        ),
        CodingChallenge(
            id = 89,
            language = "Python",
            type = "TRIVIA",
            title = "Paquetes con pip",
            question = "¿Qué comando instala un paquete desde PyPI?",
            options = listOf(
                "python get package_name",
                "pip install package_name",
                "pip download package_name",
                "python install package_name"
            ),
            correctAnswerIndex = 1,
            explanation = "pip install nombre_paquete descarga e instala el paquete desde PyPI (Python Package Index)."
        ),
        CodingChallenge(
            id = 90,
            language = "Python",
            type = "TRIVIA",
            title = "asyncio",
            question = "¿Qué palabra clave se usa para definir una función asíncrona en Python?",
            options = listOf(
                "async def",
                "async function",
                "coroutine def",
                "def async"
            ),
            correctAnswerIndex = 0,
            explanation = "Las funciones asíncronas se definen con 'async def'. Dentro de ellas se usa 'await' para llamar a otras funciones asíncronas."
        ),
        // ========== GO ==========
        CodingChallenge(
            id = 211,
            language = "Go",
            type = "TRIVIA",
            title = "Arrays vs Slices",
            question = "¿Cuál es la diferencia principal entre un array [3]int y un slice []int en Go?",
            options = listOf(
                "Son sinónimos, el compilador los trata igual",
                "El array tiene longitud fija y se copia por valor; el slice es una vista dinámica sobre un array subyacente",
                "El slice solo puede contener enteros, el array cualquier tipo",
                "Los arrays se asignan en el heap y los slices en el stack"
            ),
            correctAnswerIndex = 1,
            explanation = "En Go, [3]int es un valor de longitud fija que se copia completo al pasarlo a una función. []int es un slice: una estructura (puntero, longitud, capacidad) que apunta a un array subyacente y puede crecer con append()."
        ),
        CodingChallenge(
            id = 212,
            language = "Go",
            type = "PRINT",
            title = "Longitud tras append",
            question = "¿Qué imprime este código?",
            codeSnippet = "package main\n\nimport \"fmt\"\n\nfunc main() {\n\ts := []int{1, 2, 3}\n\tt := append(s, 4)\n\tfmt.Println(len(s), len(t))\n}",
            options = listOf(
                "3 4",
                "4 4",
                "3 3",
                "4 3"
            ),
            correctAnswerIndex = 0,
            explanation = "append puede reutilizar la capacidad del array subyacente, pero la variable 's' conserva su longitud original (3). El slice devuelto 't' tiene 4 elementos. Salida: '3 4'."
        ),
        CodingChallenge(
            id = 213,
            language = "Go",
            type = "DEBUG",
            title = "Variable sin usar",
            question = "Este código no compila en Go. ¿Por qué?",
            codeSnippet = "package main\n\nimport \"fmt\"\n\nfunc main() {\n\tx := 5\n\tfmt.Println(\"listo\")\n}",
            options = listOf(
                "Falta inicializar x con var",
                "Go no permite variables declaradas y nunca usadas; hay que eliminar x o usarla (o asignarla a _)",
                "import \"fmt\" debe ir después del main",
                ":= solo es válido con variables de paquete"
            ),
            correctAnswerIndex = 1,
            explanation = "Go es estricto: 'declared and not used: x' es un error de compilación, no un warning. Soluciones típicas: borrar la variable, usarla, o asignarla al identificador en blanco _."
        ),
        CodingChallenge(
            id = 214,
            language = "Go",
            type = "TRIVIA",
            title = "Esperar goroutines",
            question = "¿Cuál es la forma idiomática de esperar a que un grupo de goroutines termine antes de salir del main?",
            options = listOf(
                "Dormir con time.Sleep(10 * time.Second)",
                "Usar sync.WaitGroup: wg.Add(1) antes de lanzar y wg.Done() dentro de cada goroutine, luego wg.Wait()",
                "Declarar main como func() error",
                "Usar runtime.Goexit() en cada goroutine"
            ),
            correctAnswerIndex = 1,
            explanation = "El main termina cuando acaba su función: si lanza goroutines sin sincronizar, pueden no ejecutarse nunca. sync.WaitGroup cuenta las goroutines activas y Wait() bloquea hasta que todas llamen a Done(). Sleep es un antipadrón: depende del tiempo, no del trabajo real."
        ),
        CodingChallenge(
            id = 215,
            language = "Go",
            type = "PRINT",
            title = "Orden de defer",
            question = "¿Qué imprime este código?",
            codeSnippet = "package main\n\nimport \"fmt\"\n\nfunc main() {\n\tdefer fmt.Println(\"a\")\n\tfmt.Println(\"b\")\n}",
            options = listOf(
                "a luego b",
                "b luego a",
                "Solamente b",
                "Solamente a"
            ),
            correctAnswerIndex = 1,
            explanation = "'defer' pospone la llamada hasta el final de la función circundante: primero se ejecuta Println(\"b\") y, al retornar main, se ejecuta la diferida 'a'. Si hay varios defer, se ejecutan en orden LIFO (pila)."
        ),
        CodingChallenge(
            id = 216,
            language = "Go",
            type = "TRIVIA",
            title = "Visibilidad en Go",
            question = "En Go, ¿cómo se hace que un identificador (función, struct, campo) sea público/exportado fuera de su paquete?",
            options = listOf(
                "Añadiendo la palabra clave public",
                "Comenzando el nombre con letra mayúscula",
                "Marcándolo con el comentario //export",
                "Declarándolo dentro de un bloque exported{}"
            ),
            correctAnswerIndex = 1,
            explanation = "Go no tiene modificadores public/private: la visibilidad la decide la primera letra. 'Sumar' es accesible desde otros paquetes; 'sumar' es privado del paquete. Aplica a funciones, tipos, campos y métodos."
        ),
        CodingChallenge(
            id = 217,
            language = "Go",
            type = "PRINT",
            title = "Longitud y capacidad",
            question = "¿Qué imprime este código?",
            codeSnippet = "package main\n\nimport \"fmt\"\n\nfunc main() {\n\ts := []int{1, 2, 3, 4}\n\tp := s[1:3]\n\tfmt.Println(len(p), cap(p))\n}",
            options = listOf(
                "2 2",
                "2 3",
                "3 3",
                "2 4"
            ),
            correctAnswerIndex = 1,
            explanation = "p = s[1:3] toma los elementos {2, 3}: longitud 2. Pero la capacidad llega hasta el final del array subyacente: desde el índice 1 hay 3 elementos (2, 3, 4), así que cap = 3. Por eso append a p puede modificar s[3]."
        ),
        CodingChallenge(
            id = 218,
            language = "Go",
            type = "TRIVIA",
            title = "Manejo de errores",
            question = "¿Cuál es la convención idiomatica de manejo de errores en Go?",
            options = listOf(
                "Lanzar excepciones con try/catch",
                "Retornar el error como último valor y comprobar 'if err != nil' en la llamada",
                "Usar códigos de retorno negativos",
                "Registrar el error en un log global y continuar"
            ),
            correctAnswerIndex = 1,
            explanation = "Go no usa excepciones para errores recuperables: las funciones devuelven (valor, error) y el llamador inspecciona 'if err != nil { ... }'. Existe panic/recover, pero se reserva para fallos irrecuperables del programa."
        ),
        CodingChallenge(
            id = 219,
            language = "Go",
            type = "PRINT",
            title = "Sombras dentro de un bloque",
            question = "¿Qué imprime este código?",
            codeSnippet = "package main\n\nimport \"fmt\"\n\nfunc main() {\n\tx := 1\n\tif x > 0 {\n\t\tx := 2\n\t\tfmt.Println(x)\n\t}\n\tfmt.Println(x)\n}",
            options = listOf(
                "1 luego 1",
                "2 luego 2",
                "2 luego 1",
                "Error: redeclaración de x"
            ),
            correctAnswerIndex = 2,
            explanation = "'x := 2' dentro del if crea una NUEVA variable que 'sombrea' (shadows) a la exterior solo dentro de ese bloque: imprime 2 dentro y 1 fuera. Ojo con este bug clásico al reusar := en lugar de =."
        ),
        CodingChallenge(
            id = 220,
            language = "Go",
            type = "PRINT",
            title = "defer con argumentos",
            question = "¿Qué imprime este código? (sin salto de línea, es un Print)",
            codeSnippet = "package main\n\nimport \"fmt\"\n\nfunc main() {\n\tfor i := 0; i < 3; i++ {\n\t\tdefer fmt.Print(i)\n\t}\n}",
            options = listOf(
                "012",
                "210",
                "222",
                "000"
            ),
            correctAnswerIndex = 1,
            explanation = "Los argumentos de una llamada diferida se evalúan EN EL MOMENTO del defer (i vale 0, 1, 2), pero las llamadas se ejecutan al final en orden LIFO: 2, 1, 0 → imprime '210'."
        ),
    )
}
