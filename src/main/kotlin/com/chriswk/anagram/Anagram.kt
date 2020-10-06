package com.chriswk.anagram

import java.util.Locale

class Anagram {
    val noLocale = Locale.forLanguageTag("no")

    val englishWords: Sequence<String> by lazy {
        readWordsFrom("sowpods.dat")
    }
    val norwegianWords: Sequence<String> by lazy {
        readWordsFrom("norsk.dat")
    }

    val usWords: Sequence<String> by lazy {
        wordNetFrom("th_en_US_new.dat")
    }

    val anagramsMapEn: Map<String, List<String>> by lazy {
        englishWords
            .map { it.toLowerCase(Locale.ENGLISH).trim() }
            .groupBy { it.asSequence().sorted().joinToString("") }
    }
    val anagramsMapNo: Map<String, List<String>> by lazy {
        norwegianWords
            .map { it.toLowerCase(noLocale).trim() }
            .groupBy { it.asSequence().sorted().joinToString("") }
    }

    val anagramsMapUs: Map<String, List<String>> by lazy {
        usWords
                .map { it.toLowerCase(Locale.ENGLISH).trim() }
                .groupBy { it.asSequence().sorted().joinToString("") }
    }
    val pangramsMapEn: Map<String, List<String>> by lazy {
        englishWords
            .map { it.toLowerCase(Locale.ENGLISH) }
            .groupBy { it.asSequence().sorted().toSet().joinToString("") }
    }
    val pangramMapUs: Map<String, List<String>> by lazy {
        usWords
                .map { it.toLowerCase(Locale.ENGLISH) }
                .groupBy { it.asSequence().sorted().toSet().joinToString("") }
    }

    val pangramMapNo: Map<String, List<String>> by lazy {
        norwegianWords
            .map { it.toLowerCase(noLocale) }
            .groupBy { it.asSequence().sorted().toSet().joinToString("") }
    }

    val maxLengthEn: Int = englishWords
            .map { it.length }
            .maxOrNull() ?: 0

    val maxLengthNo: Int = norwegianWords
            .map { it.length }
            .maxOrNull() ?: 0

    val maxLengthUs: Int = usWords
            .map { it.length }
            .maxOrNull() ?: 0

    private fun readWordsFrom(fileName: String): Sequence<String> {
        return Anagram::class.java.getResource("/$fileName")
            .readText()
            .split("\n")
            .asSequence()
    }

    private fun wordNetFrom(fileName: String): Sequence<String> {
        return readWordsFrom(fileName).filterNot { it.startsWith("(") }.map { it.split("|").first() }
    }

    fun anagramForWord(word: String, language: String = "en"): List<String> {
        return when (language) {
            "en" -> anagramsMapEn[word.sorted()] ?: emptyList()
            "no" -> anagramsMapNo[word.sorted(noLocale)] ?: emptyList()
            "us" -> anagramsMapUs[word.sorted()] ?: emptyList()
            else -> emptyList()
        }
    }

    fun pangramForWord(word: String, language: String = "en", mustContain: Char): List<String> {
        return when (language) {
            "en" -> pangramsMapEn[word.sorted().toSet().joinToString("")]?.filter { it.contains(mustContain) } ?: emptyList()
            "no" -> pangramMapNo[word.sorted().toSet().joinToString("")]?.filter { it.contains(mustContain) } ?: emptyList()
            "us" -> pangramMapUs[word.sorted().toSet().joinToString("")]?.filter { it.contains(mustContain) } ?: emptyList()
            else -> emptyList()
        }
    }

    fun pangram(letters: String, minCount: Int = 4, mustContain: Char, language: String = "en"): List<String> {
        val words = when (language) {
            "en" -> pangramsMapEn
            "no" -> pangramMapNo
            "us" -> pangramMapUs
            else -> emptyMap()
        }

        val f = generateSequence(emptyList<String>() to 1) { (p, c) ->
            (p + permute(letters, c) to c + 1)
        }.takeWhile { it.second <= letters.length + 1 }.flatMap { (w, _) -> w.asSequence().filter { it.contains(mustContain) } }.map { it.sorted() }.toSet()
        val suggestions = f.flatMap { words[it.sorted().toSet().joinToString("")] ?: emptyList() }.toList().filter { it.length >= minCount }
        return suggestions
    }

    private fun String.sorted(locale: Locale = Locale.ENGLISH): String =
        this.toLowerCase(locale).asSequence().sorted().joinToString("")

    fun anagramsFor(word: String, minChars: Int = 3, language: String = "en"): Map<Int, List<String>> {

        return when (language) {
            "en" -> if (word.length > maxLengthEn) emptyMap() else internalAnagramsFor(word, minChars, language)
            "no" -> if (word.length > maxLengthNo) emptyMap() else internalAnagramsFor(word, minChars, language)
            "us" -> if (word.length > maxLengthUs) emptyMap() else internalAnagramsFor(word, minChars, language)
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
