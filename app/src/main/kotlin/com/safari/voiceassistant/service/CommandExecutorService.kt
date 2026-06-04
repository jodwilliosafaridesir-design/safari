package com.safari.voiceassistant.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.speech.tts.TextToSpeech
import android.widget.Toast
import com.safari.voiceassistant.commands.CommandParser
import com.safari.voiceassistant.commands.CommandExecutor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class CommandExecutorService : Service(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var commandExecutor: CommandExecutor
    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        tts = TextToSpeech(this, this)
        commandExecutor = CommandExecutor(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val command = intent?.getStringExtra("command") ?: return START_STICKY

        scope.launch {
            executeCommand(command)
        }

        return START_STICKY
    }

    private fun executeCommand(command: String) {
        val parsedCommand = CommandParser.parse(command)
        
        when (parsedCommand.type) {
            "APP_LAUNCH" -> commandExecutor.launchApp(parsedCommand.param)
            "APP_CLOSE" -> commandExecutor.closeApp(parsedCommand.param)
            "SEND_SMS" -> commandExecutor.sendSMS(parsedCommand.param)
            "MAKE_CALL" -> commandExecutor.makeCall(parsedCommand.param)
            "GET_CONTACTS" -> commandExecutor.getContacts()
            "GET_CALENDAR" -> commandExecutor.getCalendarEvents()
            "OPEN_FILE" -> commandExecutor.openFile(parsedCommand.param)
            "DELETE_FILE" -> commandExecutor.deleteFile(parsedCommand.param)
            "LIST_FILES" -> commandExecutor.listFiles(parsedCommand.param)
            else -> speakResponse("Command not recognized: $command")
        }
    }

    fun speakResponse(response: String) {
        if (tts.isSpeaking) {
            tts.stop()
        }
        tts.speak(response, TextToSpeech.QUEUE_FLUSH, null)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.FRENCH
        } else {
            Toast.makeText(this, "TTS initialization failed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
    }
}
