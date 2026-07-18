package com.tamagotchi.code.data

data class CodeCard(
    val id: Int,
    val text: String,
    val type: CardType
)

enum class CardType {
    FACT, JOKE, TIP
}

val codeCards = listOf(
    CodeCard(1, "El primer bug de la historia fue una polilla real atrapada en un relé del Harvard Mark II en 1947.", CardType.FACT),
    CodeCard(2, "¿Sabías que el lenguaje de programación más caro del mundo es APL? Se necesitaba un teclado especial.", CardType.FACT),
    CodeCard(3, "Tip: Usa nombres descriptivos para tus variables. Tu yo del futuro te lo agradecerá.", CardType.TIP),
    CodeCard(4, "El término 'debugging' viene de quitar polillas (bugs) de los ordenadores.", CardType.FACT),
    CodeCard(5, "¿Cuál es el lenguaje de programación favorito de los gatos? Scratch.", CardType.JOKE),
    CodeCard(6, "En 1960 existían más de 2000 lenguajes de programación. Hoy sobreviven unos 250.", CardType.FACT),
    CodeCard(7, "El primer compilador fue escrito por Grace Hopper en 1952. Ella también popularizó el término 'bug'.", CardType.FACT),
    CodeCard(8, "Tip: Un buen código no necesita comentarios. Un excelente código los tiene claros.", CardType.TIP),
    CodeCard(9, "¿Sabías que Python debe su nombre a los Monty Python? No a la serpiente.", CardType.FACT),
    CodeCard(10, "El símbolo {} se llama 'llave' en español, 'curly brace' en inglés, y 'accolade' en francés.", CardType.FACT),
    CodeCard(11, "¿Por qué los programadores confunden Halloween con Navidad? Porque Oct 31 == Dec 25.", CardType.JOKE),
    CodeCard(12, "Hay 10 tipos de personas: las que entienden binario y las que no.", CardType.JOKE),
    CodeCard(13, "Tip: Escribe tests antes del código. Tu yo del futuro (y tu equipo) te lo agradecerán.", CardType.TIP),
    CodeCard(14, "El lenguaje de programación más antiguo en uso hoy es Fortran (1957).", CardType.FACT),
    CodeCard(15, "¿Qué le dijo un bit al otro? 'Nos vemos en el bus'.", CardType.JOKE),
    CodeCard(16, "Tip: Un commit por cambio lógico. No hagas 'fixed stuff' en un solo commit gigante.", CardType.TIP),
    CodeCard(17, "El primer virus informático fue creado en 1971 y se llamaba 'Creeper'.", CardType.FACT),
    CodeCard(18, "¿Cómo sale un programador de la ducha? Con 'shower -exit'.", CardType.JOKE),
    CodeCard(19, "Tip: Aprende un nuevo atajo de teclado cada semana. Pequeños cambios, gran impacto.", CardType.TIP),
    CodeCard(20, "Java originalmente se llamaba 'Oak'. Lo renombraron porque ya existía una marca registrada.", CardType.FACT),
    CodeCard(21, "¿Cuántos programadores hacen falta para cambiar una bombilla? Ninguno, es un problema de hardware.", CardType.JOKE),
    CodeCard(22, "Tip: El código se escribe para personas, no para máquinas. La legibilidad importa.", CardType.TIP),
    CodeCard(23, "El primer sitio web de la historia todavía está online: info.cern.ch", CardType.FACT),
    CodeCard(24, "¿Qué hace un pez cuando se cae de un árbol? Nada. Igual que tu variable no inicializada.", CardType.JOKE),
    CodeCard(25, "Tip: Si tu código funciona pero no sabes por qué, eso se llama 'programación por coincidencia'.", CardType.TIP),
    CodeCard(26, "El símbolo # se llama 'numeral', 'hash' o 'gato'. En programación es 'sharp' o 'hashtag'.", CardType.FACT),
    CodeCard(27, "¿Cuál es la bebida favorita de los programadores? Java.", CardType.JOKE),
    CodeCard(28, "Tip: La deuda técnica se paga con intereses. Mejor refactoriza hoy.", CardType.TIP),
    CodeCard(29, "El CD-ROM fue inventado en 1982. Podía almacenar 650 MB, lo que hoy cabe en una foto.", CardType.FACT),
    CodeCard(30, "¿Por qué los programadores odian la naturaleza? Tiene demasiados bugs.", CardType.JOKE),
    CodeCard(31, "Tip: No optimices prematuramente. Primero que funcione, luego que sea rápido.", CardType.TIP),
    CodeCard(32, "Linux tiene más de 27 millones de líneas de código. Y sigue creciendo.", CardType.FACT),
    CodeCard(33, "¿Qué es un algoritmo? Una palabra que usan los programadores para no decir 'no sé cómo hacerlo'.", CardType.JOKE),
    CodeCard(34, "Tip: Usa control de versiones aunque trabajes solo. Tu yo del pasado te lo agradecerá.", CardType.TIP),
    CodeCard(35, "La primera programadora de la historia fue Ada Lovelace, en 1843.", CardType.FACT),
    CodeCard(36, "¿Qué hace un programador cuando tiene frío? Cierra todas las ventanas menos la terminal.", CardType.JOKE),
    CodeCard(37, "Tip: La mejor documentación es el código mismo. El código miente menos que los comentarios.", CardType.TIP),
    CodeCard(38, "El protocolo HTTP tiene un código 418: 'I'm a teapot' (Soy una tetera).", CardType.FACT),
    CodeCard(39, "¿Cuál es el animal favorito de los programadores? La polilla (por los bugs).", CardType.JOKE),
    CodeCard(40, "Tip: Divide y vencerás. Un problema grande son varios problemas pequeños.", CardType.TIP)
)
