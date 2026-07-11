package com.tamagotchi.code.data

object SpecialChallengesData {
    val challenges = listOf(
        CodingChallenge(
            id = 101,
            language = "Lógica Especial",
            type = "ALGORITHM",
            title = "Encontrar el Único",
            question = "Dado un array de enteros donde cada elemento aparece tres veces excepto uno que aparece exactamente una vez. Encuentra ese elemento único usando O(1) de memoria extra y O(N) de tiempo.",
            options = listOf(
                "Usar una tabla hash (Hash Map)",
                "Ordenar el array y buscar linealmente",
                "Usar conteo de bits módulo 3 en cada posición",
                "XOR de todos los elementos"
            ),
            correctAnswerIndex = 2,
            explanation = "La forma óptima de resolver esto es contar el número de bits seteados en cada posición i para todos los números. Si aplicamos módulo 3 a esta suma, obtendremos el bit en la posición i del número único."
        ),
        CodingChallenge(
            id = 102,
            language = "Lógica Especial",
            type = "ALGORITHM",
            title = "Problema del Mochilero",
            question = "¿Cuál es el enfoque clásico para resolver el Problema de la Mochila (Knapsack) 0/1 con precisión?",
            options = listOf(
                "Algoritmo Codicioso (Greedy) tomando por mayor ratio valor/peso",
                "Programación Dinámica con una tabla 2D o vector 1D",
                "Búsqueda Binaria",
                "Ordenamiento Topológico"
            ),
            correctAnswerIndex = 1,
            explanation = "El Knapsack 0/1 no se puede resolver con Greedy con precisión garantizada. Requiere Programación Dinámica evaluando subproblemas de capacidades menores y objetos disponibles."
        ),
        CodingChallenge(
            id = 103,
            language = "Lógica Especial",
            type = "ALGORITHM",
            title = "Invertir una Lista Enlazada",
            question = "¿Cuántos punteros se necesitan normalmente (como mínimo) para invertir de forma iterativa una lista enlazada simple in-place?",
            options = listOf("1", "2", "3", "4"),
            correctAnswerIndex = 2,
            explanation = "Se necesitan 3 punteros: prev (para apuntar al nodo anterior procesado), current (el nodo actual) y next (para no perder el resto de la lista al romper el enlace)."
        ),
        CodingChallenge(
            id = 104,
            language = "Lógica Especial",
            type = "ARCHITECTURE",
            title = "Teorema CAP",
            question = "En sistemas distribuidos, el Teorema CAP dice que solo puedes garantizar simultáneamente 2 de 3 propiedades. ¿Cuáles son?",
            options = listOf(
                "Consistencia, Asincronía, Partición",
                "Consistencia, Disponibilidad (Availability), Tolerancia a Particiones",
                "Control, Accesibilidad, Precisión",
                "Caché, API, Persistencia"
            ),
            correctAnswerIndex = 1,
            explanation = "CAP significa Consistency, Availability, y Partition Tolerance. Debido a que las particiones de red (P) son inevitables, un sistema debe elegir entre C y A."
        ),
        CodingChallenge(
            id = 105,
            language = "Lógica Especial",
            type = "ALGORITHM",
            title = "Detectar Ciclo",
            question = "¿Qué algoritmo de dos punteros (uno rápido y uno lento) se usa para detectar si hay un ciclo en una lista enlazada?",
            options = listOf(
                "Algoritmo de Dijkstra",
                "Algoritmo de la Tortuga y la Liebre (Floyd)",
                "Búsqueda en Profundidad (DFS)",
                "A* Search"
            ),
            correctAnswerIndex = 1,
            explanation = "El algoritmo de detección de ciclos de Floyd (Tortuga y Liebre) utiliza dos punteros avanzando a distintas velocidades. Si hay un ciclo, eventualmente se encontrarán."
        ),
        CodingChallenge(
            id = 106,
            language = "Lógica Especial",
            type = "ALGORITHM",
            title = "Árboles AVL",
            question = "¿Qué condición debe cumplirse siempre en un Árbol AVL para estar balanceado?",
            options = listOf(
                "Todos los nodos hoja deben estar en el mismo nivel",
                "La diferencia de alturas entre los subárboles izquierdo y derecho de cualquier nodo debe ser como máximo 1",
                "El subárbol izquierdo debe tener siempre más nodos que el derecho",
                "El color de los nodos no debe repetir rojo en secuencia"
            ),
            correctAnswerIndex = 1,
            explanation = "El factor de balanceo en un AVL es estrictamente la diferencia de altura, la cual no puede exceder 1 ni ser menor a -1."
        )
    )
}
