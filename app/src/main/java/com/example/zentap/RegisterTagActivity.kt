package com.example.zentap

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.zentap.data.NfcSettings
import com.example.zentap.ui.theme.ZenTapTheme

class RegisterTagActivity : ComponentActivity() {

    private var tagId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tagId = intent.getStringExtra(EXTRA_TAG_ID)

        if (tagId == null) {
            // No tag ID passed, so we can't register anything.
            finish()
            return
        }

        setContent {
            ZenTapTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RegisterTagScreen(
                        onRegisterClick = {
                            tagId?.let {
                                NfcSettings.addRegisteredTag(this, it)
                                Toast.makeText(this, "Tag registered successfully!", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        },
                        onCancelClick = {
                            finish()
                        }
                    )
                }
            }
        }
    }

    companion object {
        const val EXTRA_TAG_ID = "extra_tag_id"
    }
}

@Composable
fun RegisterTagScreen(onRegisterClick: () -> Unit, onCancelClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Register New Tag",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "This NFC tag isn't registered. Would you like to add it?",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = onCancelClick) {
                Text("Cancel")
            }
            Button(onClick = onRegisterClick) {
                Text("Register")
            }
        }
    }
}
