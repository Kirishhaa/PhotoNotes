package com.kirishhaa.photonotes.hash

class KeyWordsHashFunction {

    fun execute(word: Any): Long = word.hashCode().toLong()

}