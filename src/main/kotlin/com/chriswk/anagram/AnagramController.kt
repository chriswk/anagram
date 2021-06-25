package com.chriswk.anagram

import java.lang.IllegalArgumentException
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class AnagramController {
    val anagram: Anagram = Anagram()

    @PostMapping("/anagram", produces = ["application/json"])
    @ResponseBody
    fun postAnagramsFor(@RequestBody anagramRequest: AnagramRequest): Map<Int, List<String>> {
        return anagram.anagramsFor(anagramRequest.word, anagramRequest.minChars)
    }

    @PostMapping("/pangram")
    fun postPangramsFor(@ModelAttribute("pangram") pangramRequest: PangramForm, model: Model): String {
        model.addAttribute("pangrams", anagram.pangram(letters = pangramRequest.letters, mustContain = pangramRequest.mustcontain[0], language = "us").sortedByDescending { it.length })
        return "pangram"
    }

    @GetMapping("/pangram", produces = ["text/html"])
    @CrossOrigin
    fun getPangramsForHtml(
        @RequestParam("letters") letters: String,
        @RequestParam("language", defaultValue = "en", required = false) language: String,
        @RequestParam("mustcontain") mustContain: Char,
        model: ModelMap
    ): String {
        model["pangrams"] = anagram.pangram(letters = letters.lowercase(), mustContain = mustContain.lowercaseChar(), language = language).sortedByDescending { it.length }
        return "pangram"
    }

    @GetMapping("/anagram", produces = ["application/json"])
    @CrossOrigin()
    @ResponseBody
    fun getAnagramsForApi(
        @RequestParam("word") word: String,
        @RequestParam("minCount", required = false, defaultValue = "3") minCount: Int,
        @RequestParam("language", required = false, defaultValue = "en") language: String
    ): Map<String, List<String>> {
        if (word.length > 20) {
            throw IllegalArgumentException("Word is too long")
        }
        return mapOf(Pair("anagrams", anagram.anagramsFor(word, minCount, language).values.flatten()))
    }

    @GetMapping("/pangram", produces = ["application/json"])
    @CrossOrigin
    @ResponseBody
    fun getPangramsForApi(
        @RequestParam("letters") letters: String,
        @RequestParam("language", defaultValue = "en", required = false) language: String,
        @RequestParam("mustcontain") mustContain: Char
    ): Map<String, List<String>> {
        return mapOf(Pair("pangrams", anagram.pangram(letters, mustContain = mustContain, language = language).sortedByDescending { it.length }))
    }

    @GetMapping("/")
    fun redirectToFrontend(pangramForm: PangramForm = PangramForm("", "")): String {
        return "index"
    }
}
