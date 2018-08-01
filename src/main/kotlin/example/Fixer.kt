package example

import java.io.File
import java.nio.charset.Charset

private val userHome = System.getProperty("user.home")

private val file = File(userHome, "Documents/落格输入法/小-鹤-定制.txt")

fun main(args: Array<String>) {
    println(userHome)
    val words = file.readLines(Charset.forName("UTF-16")).filterNot { it.isEmpty() }
            .map { line ->
                val (word, code, index) = line.split("\t")
                WordCodeIndex(word, code, index.toInt())
            }

    val grouped = words.groupBy { it.code }.mapValues {
        it.value.distinctBy { it.word }
                .mapIndexed { index, wordCodeIndex ->
                    wordCodeIndex.copy(index = index + 1)
                }
    }.toList().sortedBy { it.first }

    file.bufferedWriter(Charset.forName("UTF-16")).use { writer ->
        grouped.forEach { codeGroup ->
            codeGroup.second.forEach { one ->
                writer.write("${one.word}\t${one.code}\t${one.index}")
                writer.newLine()
            }
        }
    }

    println("done")

}

private data class WordCodeIndex(val word: String, val code: String, val index: Int)