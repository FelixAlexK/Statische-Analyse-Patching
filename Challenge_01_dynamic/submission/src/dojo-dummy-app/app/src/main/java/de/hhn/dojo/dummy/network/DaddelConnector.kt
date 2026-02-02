package de.hhn.dojo.dummy.network

import android.util.Log
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket

private const val SERVER_HOST = "10.0.2.2"
private const val SERVER_PORT = 13377

sealed class ServerResult {
    data class Success(val message: String, val rawFlag: String? = null) : ServerResult()
    data class Error(val message: String) : ServerResult()
}

object DaddelConnector {
    fun send(
        payloadMap: Map<String, String>? = null,
        targetVerifierNameForPayload: String? = null,
        command: String? = null
    ): ServerResult {
        var clientSocket: Socket? = null
        return try {
            val messageToSend: String = command
                ?: if (payloadMap != null) {
                    val jsonPayload = JSONObject()
                    targetVerifierNameForPayload?.let {
                        jsonPayload.put("verifier_target_name", it)
                    }
                    for ((key, value) in payloadMap) {
                        jsonPayload.put(key, value)
                    }
                    jsonPayload.toString()
                } else {
                    return ServerResult.Error("Internal error: No payload or command specified.")
                }

            Log.d("ServerCommunicator", "Sending to server: $messageToSend")
            clientSocket = Socket(SERVER_HOST, SERVER_PORT)
            clientSocket.soTimeout = 5000

            OutputStreamWriter(clientSocket.getOutputStream()).use { writer ->
                writer.write(messageToSend + "\n")
                writer.flush()

                BufferedReader(InputStreamReader(clientSocket.getInputStream())).use { reader ->
                    val serverMessage = reader.readLine() ?: ""
                    Log.d("ServerCommunicator", "Received from server: $serverMessage")

                    return if (serverMessage.startsWith("ERROR:")) {
                        ServerResult.Error(serverMessage)
                    } else if (command == "getFlag" && !serverMessage.startsWith("OK:")) {
                        ServerResult.Success("Flag received.", rawFlag = serverMessage)
                    } else {
                        ServerResult.Success(serverMessage)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("ServerCommunicator", "Error communicating with server: ${e.message}")
            ServerResult.Error("Connection error: ${e.message}")
        } finally {
            try {
                clientSocket?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

