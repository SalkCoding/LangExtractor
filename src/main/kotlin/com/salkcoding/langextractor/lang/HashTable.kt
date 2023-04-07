package com.salkcoding.langextractor.lang

import com.google.gson.JsonParser
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class HashTable {

    val table = HashMap<String, String>()
    fun loadHashTable(versionFile: File) {
        BufferedReader(FileReader(versionFile)).use { reader ->
            val json = JsonParser.parseReader(reader).asJsonObject["objects"].asJsonObject
            json.entrySet().forEach { (key, value) ->
                if (!key.startsWith("minecraft/lang/")) return@forEach
                val lang = key.split("/").last().split(".")[0]
                table[lang] = value.asJsonObject["hash"].asString
            }
        }
    }
}