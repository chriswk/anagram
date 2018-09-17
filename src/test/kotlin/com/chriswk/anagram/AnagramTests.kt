package com.chriswk.anagram

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class AnagramTests {

    @Test
    fun productOfTwoWordsShouldBeEqualIfTheyAreAnagrams() {
        val a = Anagram()
        assertThat(a.product("retail")).isEqualTo(a.product("tailer"))
    }

    @Test
    fun productOfTwoDifferentWordsShouldNotBeEqual() {
        val a = Anagram()
        assertThat(a.product("retail")).isNotEqualTo("ttailer")
        assertThat(a.product("late")).isNotEqualTo("eat")
    }

    @Test
    fun shouldFindAllAnagramsFromSowpods() {
        val a = Anagram()
        val anagrams: Map<String, List<String>> = a.anagramsFor("retail")
        assertThat(anagrams).hasSize(34)
        assertThat(anagrams["aer"]).containsAll(listOf("are", "ear", "era"))
    }

    @Test
    fun aNonExistantWordShouldResultInAnEmptyList() {
        val a = Anagram()
        val anagrams = a.anagramsFor("lter", 4)
        assertThat(anagrams).isEmpty()
    }
    @Test
    fun shouldPermuteStringCorrectly() {
        val a = Anagram()
        val perms: List<String> = a.permute("ret", 2)
        assertThat(perms).hasSize(3)
    }

    @Test
    fun permutationShouldBeNoopIfSizeIsSameAsString() {
        val a = Anagram()
        assertThat(a.permute("retail", 6)).hasSize(1)
    }
}