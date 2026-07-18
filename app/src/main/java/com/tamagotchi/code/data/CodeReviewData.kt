package com.tamagotchi.code.data

data class CodeReviewSnippet(
    val id: Int,
    val code: String,
    val hasBug: Boolean,
    val explanation: String,
    val language: String = "Kotlin"
)

object CodeReviewData {
    val snippets = listOf(
        CodeReviewSnippet(
            1,
            "fun sum(a: Int, b: Int): Int {\n    return a - b\n}",
            true,
            "La función debería sumar, pero está restando.",
            "Kotlin"
        ),
        CodeReviewSnippet(
            2,
            "val list = listOf(1, 2, 3)\nval item = list[3]",
            true,
            "IndexOutOfBoundsException: El índice 3 no existe en una lista de 3 elementos.",
            "Kotlin"
        ),
        CodeReviewSnippet(
            3,
            "fun greet(name: String?) {\n    println(\"Hello \" + name.length)\n}",
            true,
            "NullPointerException: Intentando acceder a .length en un String que puede ser nulo.",
            "Kotlin"
        ),
        CodeReviewSnippet(
            4,
            "val x: Int = 10\nif (x == 10) {\n    println(\"Es diez\")\n}",
            false,
            "El código es correcto.",
            "Kotlin"
        ),
        CodeReviewSnippet(
            5,
            "for (i in 0..10) {\n    println(i)\n}",
            false,
            "El bucle recorre del 0 al 10 correctamente.",
            "Kotlin"
        ),
        CodeReviewSnippet(
            6,
            "fun divide(a: Int, b: Int): Int {\n    return a / b\n}",
            true,
            "Falta manejar la división por cero si b es 0.",
            "Kotlin"
        ),
        CodeReviewSnippet(
            7,
            "val map = mutableMapOf(\"a\" to 1)\nmap[\"b\"] = 2",
            false,
            "Inserción correcta en un mapa mutable.",
            "Kotlin"
        ),
        CodeReviewSnippet(
            8,
            "val name = \"Codey\"\nname = \"Buggy\"",
            true,
            "Val no puede ser reasignada.",
            "Kotlin"
        ),
        CodeReviewSnippet(
            9,
            "fun printAll(items: List<String>) {\n    items.forEach { println(it) }\n}",
            false,
            "Uso correcto de forEach.",
            "Kotlin"
        ),
        CodeReviewSnippet(
            10,
            "lateinit var user: User\nprintln(user.name)",
            true,
            "UninitializedPropertyAccessException si se accede antes de inicializar.",
            "Kotlin"
        )
    )
}
