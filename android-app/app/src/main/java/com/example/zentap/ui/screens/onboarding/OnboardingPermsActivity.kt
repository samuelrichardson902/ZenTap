package com.example.zentap.ui.screens.onboarding

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.ClickableText
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zentap.BaseActivity
import com.example.zentap.R
import com.example.zentap.ui.screens.privacy.PrivacyPolicyActivity
import com.example.zentap.ui.theme.ZenTapTheme

class OnboardingPermsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZenTapTheme {
                OnboardingPermsScreen(
                    onGrantPermission = {
                        openAccessibilitySettings()
                    }
                )
            }
        }
    }

    private fun openAccessibilitySettings() {
        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        finish()
    }
}

@Composable
fun OnboardingPermsScreen(
    onGrantPermission: () -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(64.dp))

        Icon(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = stringResource(R.string.app_name),
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Permissions Required for Zentap",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        val annotatedString = buildAnnotatedString {
            append("To help you focus and block distracting apps, Zentap needs the following permissions:\n\n")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("• Accessibility Service:")
            }
            append(" To monitor the app you are currently using, so we can show the blocking screen at the right time.\n")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("• Query All Packages:")
            }
            append(" To get a list of all your installed apps, so you can choose which ones to block.\n")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("• Display Over Other Apps:")
            }
            append(" To show the blocking screen on top of the apps you have chosen to block.\n")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("• Alarms & Reminders:")
            }
            append(" To schedule automatic blocking sessions.\n\n")
            append("We take your privacy seriously. All data is stored locally on your device and is never shared. For more details, please review our ")
            pushStringAnnotation(tag = "PrivacyPolicy", annotation = "PrivacyPolicy")
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)) {
                append("Privacy Policy")
            }
            pop()
            append(".")
        }

        ClickableText(
            text = annotatedString,
            onClick = { offset ->
                annotatedString.getStringAnnotations(tag = "PrivacyPolicy", start = offset, end = offset)
                    .firstOrNull()?.let {
                        context.startActivity(Intent(context, PrivacyPolicyActivity::class.java))
                    }
            },
            style = MaterialTheme.typography.bodyLarge.copy(
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onGrantPermission,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            Text("Agree & Continue", fontSize = 16.sp)
        }
    }
}