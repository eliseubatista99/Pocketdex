package com.eliseubatista.pocketdex.utils

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun writeStringToFile(context: Context, data: String, fileName: String): Boolean {
    return try {
        val streamWriter =
            OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE))
        streamWriter.write(data)
        streamWriter.close()

        true
    } catch (e: Exception) {
        Log.e("Exception", e.toString())
        false
    }
}

fun readStringFromFile(context: Context, fileName: String): String {
    try {
        val inputStream = context.openFileInput(fileName)

        if (inputStream == null) {
            return ""
        }

        val inputStreamReader = InputStreamReader(inputStream)
        val bufferedReader = BufferedReader(inputStreamReader)
        var receivedString = ""
        val stringBuilder = StringBuilder()

        while (true) {
            receivedString = bufferedReader.readLine()

            if (receivedString == null) {
                break
            }

            stringBuilder.append(receivedString)
        }

        inputStream.close()
        return stringBuilder.toString()
    } catch (e: Exception) {
        Log.e("Exception", e.toString())
        return ""
    }
}