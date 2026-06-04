package com.safari.voiceassistant.ui

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.safari.voiceassistant.databinding.ActivityMainBinding
import com.safari.voiceassistant.service.VoiceAssistantService
import com.safari.voiceassistant.utils.PermissionManager
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var permissionManager: PermissionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        permissionManager = PermissionManager(this)

        setupUI()
        requestPermissions()
    }

    private fun setupUI() {
        // Listen Button
        binding.listenButton.setOnClickListener {
            if (permissionManager.hasAudioPermission()) {
                startVoiceAssistant()
            } else {
                Toast.makeText(this, "Audio permission required", Toast.LENGTH_SHORT).show()
            }
        }

        // Text Command Button
        binding.textCommandButton.setOnClickListener {
            val intent = Intent(this, CommandActivity::class.java)
            startActivity(intent)
        }

        // Settings Button
        binding.settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        // Status Display
        binding.statusText.text = "Ready to assist"
    }

    private fun startVoiceAssistant() {
        val intent = Intent(this, VoiceAssistantService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
        binding.statusText.text = "Listening..."
    }

    private fun requestPermissions() {
        lifecycleScope.launch {
            val requiredPermissions = mutableListOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requiredPermissions.add(Manifest.permission.READ_MEDIA_AUDIO)
            }

            val missingPermissions = permissionManager.getMissingPermissions(requiredPermissions)
            if (missingPermissions.isNotEmpty()) {
                permissionManager.requestPermissions(missingPermissions)
            }
        }
    }
}
