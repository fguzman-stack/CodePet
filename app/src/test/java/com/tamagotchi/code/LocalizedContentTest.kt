package com.tamagotchi.code

import com.tamagotchi.code.data.ChallengesData
import com.tamagotchi.code.data.ChallengesDataEn
import com.tamagotchi.code.data.CodeCardsEn
import com.tamagotchi.code.data.CodeReviewData
import com.tamagotchi.code.data.CodeReviewDataEn
import com.tamagotchi.code.data.SpecialChallengesData
import com.tamagotchi.code.data.SpecialChallengesDataEn
import com.tamagotchi.code.data.codeCards
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LocalizedContentTest {

    @Test
    fun everyChallengeHasEnglishTranslationWithMatchingOptions() {
        val all = ChallengesData.challenges + SpecialChallengesData.challenges
        assertTrue(all.isNotEmpty())
        all.forEach { es ->
            val en = ChallengesDataEn.translations[es.id]
                ?: SpecialChallengesDataEn.translations[es.id]
            assertNotNull("Missing EN translation for challenge id=${es.id} (${es.title})", en)
            assertEquals("EN options count mismatch for id=${es.id}", es.options.size, en!!.options.size)
            assertTrue(
                "correctAnswerIndex out of range for id=${es.id}",
                es.correctAnswerIndex in en.options.indices
            )
            assertTrue("Empty EN title for id=${es.id}", en.title.isNotBlank())
            assertTrue("Empty EN explanation for id=${es.id}", en.explanation.isNotBlank())
        }
    }

    @Test
    fun everyCodeReviewSnippetHasEnglishTranslation() {
        CodeReviewData.snippets.forEach { es ->
            val en = CodeReviewDataEn.translations[es.id]
            assertNotNull("Missing EN translation for review id=${es.id}", en)
            assertTrue("Empty EN review code for id=${es.id}", en!!.code.isNotBlank())
            assertTrue("Empty EN review explanation for id=${es.id}", en.explanation.isNotBlank())
        }
    }

    @Test
    fun everyCodeCardHasEnglishTranslation() {
        codeCards.forEach { card ->
            val text = CodeCardsEn.texts[card.id]
            assertNotNull("Missing EN text for code card id=${card.id}", text)
            assertTrue("Blank EN text for code card id=${card.id}", !text.isNullOrBlank())
        }
        assertEquals(codeCards.size, CodeCardsEn.texts.size)
    }

    private fun assertNotNull(message: String, value: Any?) {
        if (value == null) throw AssertionError(message)
    }
}
