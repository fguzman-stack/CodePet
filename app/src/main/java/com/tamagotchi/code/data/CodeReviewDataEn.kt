package com.tamagotchi.code.data

// English translations keyed by CodeReviewSnippet.id. Code semantics kept identical.
object CodeReviewDataEn {
    val translations: Map<Int, ReviewTextsEn> = mapOf(
        1 to ReviewTextsEn(
            code = "fun sum(a: Int, b: Int): Int {\n    return a - b\n}",
            explanation = "The function should add, but it's subtracting."
        ),
        2 to ReviewTextsEn(
            code = "val list = listOf(1, 2, 3)\nval item = list[3]",
            explanation = "IndexOutOfBoundsException: Index 3 does not exist in a list of 3 elements."
        ),
        3 to ReviewTextsEn(
            code = "fun greet(name: String?) {\n    println(\"Hello \" + name.length)\n}",
            explanation = "NullPointerException: Trying to access .length on a String that can be null."
        ),
        4 to ReviewTextsEn(
            code = "val x: Int = 10\nif (x == 10) {\n    println(\"It's ten\")\n}",
            explanation = "The code is correct."
        ),
        5 to ReviewTextsEn(
            code = "for (i in 0..10) {\n    println(i)\n}",
            explanation = "The loop iterates from 0 to 10 correctly."
        ),
        6 to ReviewTextsEn(
            code = "fun divide(a: Int, b: Int): Int {\n    return a / b\n}",
            explanation = "Missing handling of division by zero if b is 0."
        ),
        7 to ReviewTextsEn(
            code = "val map = mutableMapOf(\"a\" to 1)\nmap[\"b\"] = 2",
            explanation = "Correct insertion into a mutable map."
        ),
        8 to ReviewTextsEn(
            code = "val name = \"Codey\"\nname = \"Buggy\"",
            explanation = "A val cannot be reassigned."
        ),
        9 to ReviewTextsEn(
            code = "fun printAll(items: List<String>) {\n    items.forEach { println(it) }\n}",
            explanation = "Correct use of forEach."
        ),
        10 to ReviewTextsEn(
            code = "lateinit var user: User\nprintln(user.name)",
            explanation = "UninitializedPropertyAccessException if accessed before initialization."
        )
    )
}
