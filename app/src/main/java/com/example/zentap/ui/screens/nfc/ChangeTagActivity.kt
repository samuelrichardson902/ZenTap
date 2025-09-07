package com.example.zentap.ui.screens.nfc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.zentap.MainViewModel
import com.example.zentap.ui.theme.ZenTapTheme
import com.example.zentap.util.ToastManager

class ChangeTagActivity : ComponentActivity() {

    private var tagId: String? = null
    private val viewModel: MainViewModel by viewModels() // use shared ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tagId = intent.getStringExtra(PRESENTED_TAG_ID)

        if (tagId == null) {
            finish() // prevent opening without a tag
            return
        }

        setContent {
            ZenTapTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    ChangeTagScreen(
                        onRegisterClick = {
                            tagId?.let { id ->
                                if (viewModel.registerTag(this, id)) {
                                    ToastManager.showToast(this, "Tag added successfully!")
                                } else {
                                    ToastManager.showToast(this, "Tag is already registered")
                                }
                                finish() // close screen and update TagManagementScreen via ViewModel
                            }
                        },
                        onCancelClick = { finish() },
                        onBackClick = { finish() }
                    )
                }
            }
        }
    }

    companion object {
        const val PRESENTED_TAG_ID = "presented_tag_id"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeTagScreen(
    onRegisterClick: () -> Unit,
    onCancelClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopAppBar(
            title = { Text("Register New Tag") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Would you like to add this NFC tag to your account?",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
            ) {
                OutlinedButton(
                    onClick = onCancelClick,
                    modifier = Modifier.weight(1f)
                ) { Text("Cancel") }

                Button(
                    onClick = onRegisterClick,
                    modifier = Modifier.weight(1f)
                ) { Text("Register") }
            }
        }
    }
}
