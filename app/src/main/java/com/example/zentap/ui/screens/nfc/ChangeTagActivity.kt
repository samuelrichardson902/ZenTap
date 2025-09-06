package com.example.zentap.ui.screens.nfc

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

class ChangeTagActivity : ComponentActivity() {

    private var tagId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tagId = intent.getStringExtra(PRESENTED_TAG_ID)

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
                    ChangeTagScreen(
                        onRegisterClick = {
                            tagId?.let {
                                if (NfcSettings.setRegisteredTag(this, it)){
                                    Toast.makeText(this, "Tag changed successfully!", Toast.LENGTH_SHORT).show()
                                    finish()
                                }
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
        const val PRESENTED_TAG_ID = "presented_tag_id"
    }
}

@Composable
fun ChangeTagScreen(onRegisterClick: () -> Unit, onCancelClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Change Registered Tag",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "This NFC tag isn't registered. Would you like to make it your new registered tag?",
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
