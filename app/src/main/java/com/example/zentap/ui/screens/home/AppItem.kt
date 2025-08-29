package com.example.zentap.ui.screens.home

import android.content.pm.PackageManager
import android.widget.ImageView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Replace AsyncImage with AndroidView to load the app icon
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

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = app.name,
                    fontSize = 16.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    color = Color(0xFF333333)
                )
                Text(
                    text = app.packageName,
                    fontSize = 12.sp,
                    color = Color(0xFF666666),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Switch(
                checked = app.blocked,
                onCheckedChange = { onToggle(it) }
            )
        }
    }
}
