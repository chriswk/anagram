package com.chriswk.anagram

import java.util.Locale

class Anagram {
    val primes: List<Long> = listOf(31L, 37L, 41L, 43L, 47L,
            53L, 59L, 61L, 67L, 71L,
            73L, 79L, 83L, 89L, 97L,
            101L, 103L, 107L, 109L, 113L,
            127L, 131L, 137L, 139L, 149L,
            151L, 157L, 163L, 167L)
    val primeMap: Map<Char, Long> = CharRange('a', 'z').zip(primes).toMap()
    val anagramsMap: Map<Long, List<String>> by lazy {
        val sowpods = Anagram::class.java.classLoader.getResource("sowpods.dat").readText()
        sowpods.split("\n").map { it.toLowerCase(Locale.ENGLISH) }.groupBy { product(it) }
    }

    fun product(word: String): Long = word.toCharArray().map { primeMap[it] }.fold(1L) { x, y ->
        x * (y ?: 1)
    }

    fun anagramForWord(word: String): List<String> {
        return anagramsMap[product(word)] ?: emptyList()
    }

    fun anagramsFor(word: String, minChars: Int = 3): Map<String, List<String>> {
        if (word.length <= minChars) {
            return mapOf(Pair(word, anagramForWord(word)))
        } else {
            return IntRange(minChars, word.length).flatMap {
                permute(word, it).map {
                    Pair(it, anagramForWord(it))
                }
            }.toMap()
        }
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


