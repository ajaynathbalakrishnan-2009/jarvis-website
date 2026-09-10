package com.example.jarvis

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.app.role.RoleManager
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { JarvisScreen() }
    }

    private fun JarvisScreen() {
        var status by remember { mutableStateOf("Ready. Say: call John") }
        var pendingCall by remember { mutableStateOf<PhoneTarget?>(null) }
        val permissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { grants ->
            if (grants[Manifest.permission.RECORD_AUDIO] == true) {
                listenForCommand { command ->
                    pendingCall = resolveCallTarget(command)
                    status = pendingCall?.let { "Call ${it.label}?" } ?: "Try saying: call John"
                }
            }
        }

        MaterialTheme {
            Surface(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("JARVIS", style = MaterialTheme.typography.headlineLarge)
                    Text(status)
                    Button(onClick = {
                        permissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.READ_CONTACTS,
                                Manifest.permission.CALL_PHONE
                            )
                        )
                    }) { Text("Speak") }
                    Button(onClick = {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                            val roleManager = getSystemService(RoleManager::class.java)
                            startActivityForResult(
                                roleManager.createRequestRoleIntent(RoleManager.ROLE_ASSISTANT),
                                1001
                            )
                        }
                    }) { Text("Enable hands-free assistant") }
                    pendingCall?.let { target ->
                        Button(onClick = {
                            placeCall(target.number)
                            pendingCall = null
                            status = "Calling ${target.label}"
                        }) { Text("Confirm call") }
                    }
                }
            }
        }
    }

    private fun listenForCommand(onResult: (String) -> Unit) {
        if (!SpeechRecognizer.isRecognitionAvailable(this)) return
        val recognizer = SpeechRecognizer.createSpeechRecognizer(this)
        recognizer.setRecognitionListener(SimpleRecognitionListener { text ->
            recognizer.destroy()
            onResult(text)
        })
        recognizer.startListening(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Say call followed by a contact or number")
        })
    }

    private fun resolveCallTarget(command: String): PhoneTarget? {
        val phrase = command.lowercase().trim()
        if (!phrase.startsWith("call ") && !phrase.startsWith("dial ")) return null
        val spokenTarget = phrase.substringAfter(' ').trim()
        val digits = spokenTarget.filter { it.isDigit() || it == '+' }
        if (digits.length >= 3) return PhoneTarget(digits, digits)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) return null
        contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER),
            "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} LIKE ?",
            arrayOf("%$spokenTarget%"),
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                return PhoneTarget(
                    cursor.getString(1),
                    cursor.getString(0)
                )
            }
        }
        return null
    }

    private fun placeCall(number: String) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:${Uri.encode(number)}")))
        } else {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${Uri.encode(number)}")))
        }
    }

    private data class PhoneTarget(val number: String, val label: String)
}
