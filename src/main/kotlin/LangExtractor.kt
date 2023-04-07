package com.salkcoding.langextractor

import com.salkcoding.langextractor.lang.EnumLang
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

fun main() {
    val versionFolder = File(System.getenv("APPDATA"), "/.minecraft/assets/indexes")
    if (!versionFolder.exists() || versionFolder.listFiles()!!.isEmpty()) {
        println("You will need to run the version of Minecraft you want at least once to extract the language files you want.")
        return
    }

    println("Version lists")
    val versionFileList = versionFolder.listFiles()!!.filter { it.extension == "json" }
    versionFileList.forEachIndexed { index, versionFile ->
        println("${index + 1}: ${versionFile.name}")
    }

    print("Select version number that you want to extract: ")
    val selectedIndex: Int
    try {
        val selected = readln().toInt()
        if (selected < 1 || selected > versionFileList.size) throw NumberFormatException("Illegal number input")
        selectedIndex = selected
    } catch (e: NumberFormatException) {
        println("Please input correct number.")
        return
    }
    println()

    val versionFile = versionFileList[selectedIndex - 1]
    print("Load hash table... ")
    EnumLang.loadHashTable(versionFile)
    println("Completed!")

    println("Extract from version ${versionFile.nameWithoutExtension}")
    val values = EnumLang.values()
    var progress = 0
    val executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()) as ThreadPoolExecutor
    val key = Object()
    values.forEach { lang ->
        executor.execute {
            lang.extractLangFile()
            lang.saveLangFile()
            synchronized(key) {
                println("Completed ${lang}.json extraction (${++progress}/${values.size})")
            }
        }
    }
    executor.shutdown()
    try {
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)
    } catch (e: InterruptedException) {
        executor.shutdownNow()
        Thread.currentThread().interrupt()
    }
    println("\nExtraction completed.")
    println("All of files are saved in: ${EnumLang.langFolder.absolutePath}")
}