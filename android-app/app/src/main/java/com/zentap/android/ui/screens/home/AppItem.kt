package com.zentap.android.ui.screens.home

import android.content.pm.PackageManager
import android.widget.ImageView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat

data class AppUiModel(
    val name: String,
    val packageName: String,
    val blocked: Boolean
)

@Composable
fun AppItem(
    app: AppUiModel,
    onToggle: (Boolean) -> Unit,
) {
    val context = LocalContext.current
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // App icon loaded via AndroidView
            AndroidView(
                factory = { ctx ->
                    ImageView(ctx).apply {
                        try {
                            val icon = ctx.packageManager.getApplicationIcon(app.packageName)
                            setImageDrawable(icon)
                        } catch (e: PackageManager.NameNotFoundException) {
                            e.printStackTrace()
                            setImageDrawable(
                                ContextCompat.getDrawable(
                                    ctx,
                                    android.R.drawable.sym_def_app_icon
                                )
                            )
                        }
                    }
                },
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = app.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.onSurface
                )
            }

            Switch(
                checked = app.blocked,
                onCheckedChange = { onToggle(it) },
                colors = SwitchDefaults.colors(
                    // --- MODIFICATION START ---
                    // Use onPrimary for the thumb when checked, so it contrasts with the primary track color.
                    checkedThumbColor = colors.onPrimary,
                    checkedTrackColor = colors.primary,
                    checkedBorderColor = colors.primary,

                    // Define unchecked colors for consistency
                    uncheckedThumbColor = colors.onSurfaceVariant,
                    uncheckedTrackColor = colors.surfaceVariant, // or colors.surfaceContainerHighest
                    uncheckedBorderColor = colors.outline
                    // --- MODIFICATION END ---
                )
            )
        }
    }
}