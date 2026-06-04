package com.safari.voiceassistant.commands

data class ParsedCommand(
    val type: String,
    val param: String = ""
)

object CommandParser {

    fun parse(command: String): ParsedCommand {
        val lowerCommand = command.lowercase().trim()

        return when {
            // App Control Commands
            lowerCommand.startsWith("ouvre") || lowerCommand.startsWith("lance") -> {
                val appName = extractParam(lowerCommand, listOf("ouvre", "lance"))
                ParsedCommand("APP_LAUNCH", appName)
            }
            lowerCommand.startsWith("ferme") -> {
                val appName = extractParam(lowerCommand, listOf("ferme"))
                ParsedCommand("APP_CLOSE", appName)
            }

            // SMS Commands
            lowerCommand.startsWith("envoie") && lowerCommand.contains("sms") -> {
                val params = extractParams(lowerCommand, "à", ":")
                ParsedCommand("SEND_SMS", params)
            }

            // Call Commands
            lowerCommand.startsWith("appelle") -> {
                val contact = extractParam(lowerCommand, listOf("appelle"))
                ParsedCommand("MAKE_CALL", contact)
            }

            // Contacts
            lowerCommand.startsWith("affiche") && lowerCommand.contains("contact") -> {
                ParsedCommand("GET_CONTACTS")
            }

            // Calendar
            lowerCommand.startsWith("affiche") && lowerCommand.contains("calendrier") -> {
                ParsedCommand("GET_CALENDAR")
            }

            // File Management
            lowerCommand.startsWith("ouvre") && lowerCommand.contains("fichier") -> {
                val fileName = extractParam(lowerCommand, listOf("fichier"))
                ParsedCommand("OPEN_FILE", fileName)
            }
            lowerCommand.startsWith("supprime") && lowerCommand.contains("fichier") -> {
                val fileName = extractParam(lowerCommand, listOf("fichier"))
                ParsedCommand("DELETE_FILE", fileName)
            }
            lowerCommand.startsWith("liste") -> {
                val path = extractParam(lowerCommand, listOf("liste"))
                ParsedCommand("LIST_FILES", path)
            }

            else -> ParsedCommand("UNKNOWN", command)
        }
    }

    private fun extractParam(command: String, keywords: List<String>): String {
        for (keyword in keywords) {
            val index = command.indexOf(keyword)
            if (index != -1) {
                return command.substring(index + keyword.length).trim()
            }
        }
        return ""
    }

    private fun extractParams(command: String, vararg separators: String): String {
        for (separator in separators) {
            if (command.contains(separator)) {
                return command.split(separator)[1].trim()
            }
        }
        return ""
    }
}
