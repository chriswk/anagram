package com.chriswk.anagram

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AnagramTests {
    @Test
    fun aTooLongWordGivesEmpty() {
        val a = Anagram()
        assertThat(a.anagramsFor("zzzzzzzzzzzzzzzzzzzzzzzzz")).isEmpty()
    }

    @Test
    fun shouldFindAllAnagramsFromSowpods() {
        val a = Anagram()
        val anagrams: Map<Int, List<String>> = a.anagramsFor("retail")
        assertThat(anagrams[3]).hasSize(35)
        assertThat(anagrams[4]).hasSize(46)
        assertThat(anagrams[5]).hasSize(19)
        assertThat(anagrams[6]).hasSize(3)
        assertThat(anagrams.values.flatten()).hasSize(103)
    }

    @Test
    fun aNonExistentWordShouldResultInAnEmptyList() {
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

    @Test
    fun shouldEliminateDuplicates() {
        val a = Anagram()
        val anagrams = a.anagramsFor("application", 10)
        assertThat(anagrams[11]).containsOnly("application")
        assertThat(anagrams[10]).containsOnly("capitolian", "panoptical")
        assertThat(anagrams.values.flatten()).hasSize(3)
    }

    @Test
    fun shouldBeSortedAlphabetically() {
        val a = Anagram()
        val anagrams = a.anagramsFor("application", 9)
        assertThat(anagrams[11]).containsExactly("application")
        assertThat(anagrams[10]).containsExactly("capitolian", "panoptical")
        assertThat(anagrams[9]).containsExactly("ancipital", "applicant", "pactional", "palinopia", "palpation", "placation", "plication")
        assertThat(anagrams.values.flatten()).hasSize(10)
    }

    @Test
    fun shouldSupportNorwegianAsWell() {
        val a = Anagram()
        val anagrams = a.anagramsFor("harepus", 3, language = "no")
        assertThat(anagrams[7]).containsExactly("harepus")
        assertThat(anagrams[6]).containsExactly("harpes", "hasper", "hauser", "hurpes", "pauser", "pusher", "sherpa")
        assertThat(anagrams[5]).hasSize(29)
        assertThat(anagrams[4]).hasSize(55)
        assertThat(anagrams[3]).hasSize(44)
        assertThat(anagrams).doesNotContainKey(2)
        assertThat(anagrams.values.flatten()).hasSize(136)
    }

    @Test
    fun shouldHandleNorwegianSpecialChars() {
        val a = Anagram()
        val anagrams = a.anagramsFor("fårepølse", 3, language = "no")
        assertThat(anagrams[9]).containsExactly("fårepølse")
        assertThat(anagrams).doesNotContainKey(8)
        assertThat(anagrams[7]).containsExactly("felåser", "leprøse", "påføres")
        assertThat(anagrams[6]).hasSize(26)
        assertThat(anagrams[5]).hasSize(61)
        assertThat(anagrams[4]).hasSize(82)
        assertThat(anagrams[3]).hasSize(55)
        assertThat(anagrams).doesNotContainKey(2)
        assertThat(anagrams.values.flatten()).hasSize(228)
    }
}
