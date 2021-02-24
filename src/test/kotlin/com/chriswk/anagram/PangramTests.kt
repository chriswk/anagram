package com.chriswk.anagram

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PangramTests {

    @Test
    fun findsPangramsForExample() {
        val words = Anagram().pangram("pairtuy", mustContain = 'p')
        assertThat(words).contains(
            "pituitary",
            "apart",
            "apiary",
            "apparat",
            "irrupt",
            "pair",
            "papa",
            "papaya",
            "pappy",
            "papyri",
            "parity",
            "parry",
            "part",
            "partita",
            "party",
            "patty",
            "pipit",
            "pita",
            "pitapat",
            "pity",
            "prat",
            "purity",
            "purr",
            "putt",
            "putty",
            "rapt",
            "rattrap",
            "tapa",
            "tapir",
            "tarp",
            "tippy",
            "trap",
            "uppity",
            "yappy")
    }

}