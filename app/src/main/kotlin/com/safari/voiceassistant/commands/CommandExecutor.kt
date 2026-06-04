package com.safari.voiceassistant.commands

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.CalendarContract
import android.provider.ContactsContract
import android.telecom.TelecomManager
import android.telephony.SmsManager
import android.widget.Toast
import com.safari.voiceassistant.service.CommandExecutorService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class CommandExecutor(private val context: Context) {

    private val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

    // App Control
    fun launchApp(appName: String) {
        val packageName = getPackageNameFromAppName(appName)
        if (packageName.isNotEmpty()) {
            val intent = context.packageManager.getLaunchIntentForPackage(packageName)
            if (intent != null) {
                context.startActivity(intent)
                speakResponse("Ouverture de $appName")
            } else {
                speakResponse("Impossible de lancer $appName")
            }
        } else {
            speakResponse("Application $appName non trouvée")
        }
    }

    fun closeApp(appName: String) {
        val packageName = getPackageNameFromAppName(appName)
        if (packageName.isNotEmpty()) {
            activityManager.killBackgroundProcesses(packageName)
            speakResponse("$appName fermée")
        }
    }

    // SMS Management
    fun sendSMS(recipient: String) {
        try {
            val smsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                context.getSystemService(SmsManager::class.java)
            } else {
                @Suppress("DEPRECATION")
                SmsManager.getDefault()
            }
            
            smsManager?.sendTextMessage(recipient, null, "Message automatique", null, null)
            speakResponse("SMS envoyé à $recipient")
        } catch (e: Exception) {
            speakResponse("Erreur lors de l'envoi du SMS: ${e.message}")
        }
    }

    // Phone Calls
    fun makeCall(contact: String) {
        try {
            val intent = Intent(Intent.ACTION_CALL).apply {
                data = Uri.parse("tel:$contact")
            }
            context.startActivity(intent)
            speakResponse("Appel à $contact")
        } catch (e: Exception) {
            speakResponse("Impossible d'appeler: ${e.message}")
        }
    }

    // Contacts
    fun getContacts() {
        try {
            val cursor = context.contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                null
            )
            val contactCount = cursor?.count ?: 0
            cursor?.close()
            speakResponse("Vous avez $contactCount contacts")
        } catch (e: Exception) {
            speakResponse("Erreur lors de la lecture des contacts: ${e.message}")
        }
    }

    // Calendar
    fun getCalendarEvents() {
        try {
            val cursor = context.contentResolver.query(
                CalendarContract.Events.CONTENT_URI,
                null,
                null,
                null,
                null
            )
            val eventCount = cursor?.count ?: 0
            cursor?.close()
            speakResponse("Vous avez $eventCount événements")
        } catch (e: Exception) {
            speakResponse("Erreur lors de la lecture du calendrier: ${e.message}")
        }
    }

    // File Management
    fun openFile(fileName: String) {
        try {
            val file = File(fileName)
            if (file.exists()) {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.fromFile(file)
                }
                context.startActivity(intent)
                speakResponse("Ouverture de $fileName")
            } else {
                speakResponse("Fichier non trouvé: $fileName")
            }
        } catch (e: Exception) {
            speakResponse("Erreur: ${e.message}")
        }
    }

    fun deleteFile(fileName: String) {
        try {
            val file = File(fileName)
            if (file.exists()) {
                file.delete()
                speakResponse("Fichier $fileName supprimé")
            } else {
                speakResponse("Fichier non trouvé: $fileName")
            }
        } catch (e: Exception) {
            speakResponse("Erreur lors de la suppression: ${e.message}")
        }
    }

    fun listFiles(path: String) {
        try {
            val directory = File(path.ifEmpty { "/sdcard/" })
            if (directory.isDirectory) {
                val files = directory.listFiles() ?: emptyArray()
                val fileCount = files.size
                speakResponse("$fileCount fichiers trouvés")
            } else {
                speakResponse("Chemin invalide: $path")
            }
        } catch (e: Exception) {
            speakResponse("Erreur: ${e.message}")
        }
    }

    private fun getPackageNameFromAppName(appName: String): String {
        val commonApps = mapOf(
            "chrome" to "com.android.chrome",
            "gmail" to "com.google.android.gm",
            "maps" to "com.google.android.apps.maps",
            "youtube" to "com.google.android.youtube",
            "facebook" to "com.facebook.katana",
            "whatsapp" to "com.whatsapp",
            "instagram" to "com.instagram.android",
            "twitter" to "com.twitter.android"
        )
        return commonApps[appName.lowercase()] ?: ""
    }

    private fun speakResponse(response: String) {
        if (context is CommandExecutorService) {
            context.speakResponse(response)
        }
    }
}
