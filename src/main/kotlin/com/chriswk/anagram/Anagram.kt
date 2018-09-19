package com.chriswk.anagram

import java.util.Locale

class Anagram {
    val noLocale = Locale.forLanguageTag("no")
    val primes: List<Long> = listOf(31L, 37L, 41L, 43L, 47L,
            53L, 59L, 61L, 67L, 71L,
            73L, 79L, 83L, 89L, 97L,
            101L, 103L, 107L, 109L, 113L,
            127L, 131L, 137L, 139L, 149L,
            151L, 157L, 163L, 167L)
    val primeMap: Map<Char, Long> = CharRange('a', 'z').plus(arrayOf('æ', 'ø', 'å')).zip(primes).toMap()

    val englishWords: Sequence<String> by lazy {
        readWordsFrom("sowpods.dat")
    }
    val norwegianWords: Sequence<String> by lazy {
        readWordsFrom("norsk.dat")
    }
    val anagramsMapEn: Map<String, List<String>> by lazy {
        englishWords
                .map { it.toLowerCase(Locale.ENGLISH) }
                .groupBy { it.asSequence().sorted().joinToString("") }
    }
    val anagramsMapNo: Map<String, List<String>> by lazy {
        norwegianWords
                .map { it.toLowerCase(noLocale) }
                .groupBy { it.asSequence().sorted().joinToString("") }
    }

    val maxLengthEn: Int = englishWords
            .map { it.length }
            .max() ?: 0

    val maxLengthNo: Int = norwegianWords
            .map { it.length }
            .max() ?: 0

    private fun readWordsFrom(fileName: String): Sequence<String> {
        return Anagram::class.java.classLoader.getResource(fileName)
                .readText()
                .split("\n")
                .asSequence()
    }

    fun anagramForWord(word: String, language: String = "en"): List<String> {
        return when (language) {
            "en" -> anagramsMapEn[word.sorted()] ?: emptyList()
            "no" -> anagramsMapNo[word.sorted(noLocale)] ?: emptyList()
            else -> emptyList()
        }
    }

    private fun String.sorted(locale: Locale = Locale.ENGLISH): String =
        this.toLowerCase(locale).asSequence().sorted().joinToString("")

    fun anagramsFor(word: String, minChars: Int = 3, language: String = "en"): Map<Int, List<String>> {

        return when (language) {
            "en" -> if (word.length > maxLengthEn) emptyMap() else internalAnagramsFor(word, minChars, language)
            "no" -> if (word.length > maxLengthNo) emptyMap() else internalAnagramsFor(word, minChars, language)
            else -> emptyMap()
        }
    }

    private fun internalAnagramsFor(word: String, minChars: Int, language: String): Map<Int, List<String>> {
        return IntRange(minChars, word.length).flatMap {
            permute(word, it).flatMap { w ->
                anagramForWord(w, language)
            }
        }.asSequence().distinct().groupBy { it.length }.mapValues { it.value.sorted() }
    }

    fun permute(word: String, charCount: Int): List<String> = combinations(charCount, word.toList()).map { it.joinToString(separator = "") }

    fun <T> combinations(n: Int, list: List<T>): List<List<T>> =
            if (n == 0) listOf(emptyList())
            else list.flatMapTails { subList ->
                combinations(n - 1, subList.tail()).map { (it + subList.first()) }
            }

    private fun <T> List<T>.flatMapTails(f: (List<T>) -> (List<List<T>>)): List<List<T>> =
            if (isEmpty()) emptyList()
            else f(this) + this.tail().flatMapTails(f)

    fun <T> List<T>.tail(): List<T> = drop(1)
}
