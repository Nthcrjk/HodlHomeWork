package ru.flx.hodlhomework.ui.home

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import org.bouncycastle.crypto.params.Blake3Parameters.context

@Composable
fun HomeDialog(
    txid: String,
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )

    ) {
        (LocalView.current.parent as DialogWindowProvider).window.setDimAmount(0f)

        val shortenedTxid = remember(txid) {
            if (txid.length > 20)
                txid.take(10) + "…" + txid.takeLast(10)
            else txid
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .background(color = Color.White, shape = RoundedCornerShape(20.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Your funds have been sent!",
                style = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Your transaction ID is",
                style = TextStyle(fontSize = 16.sp, color = Color.Gray)
            )

            Spacer(modifier = Modifier.height(8.dp))
            val uriHandler = LocalUriHandler.current
            Text(
                text = shortenedTxid,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        val base = "https://mempool.space/signet/tx/"
                        uriHandler.openUri(base + txid)
                    }
                    .background(Color(0xFFE3F2FD))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                style = TextStyle(
                    fontSize = 14.sp,
                    color = Color(0xFF1A73E8),
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = TextDecoration.Underline
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onDismissRequest,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A73E8)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Send more", color = Color.White)
            }
        }
    }
}

@Composable
@Preview
fun HomeDialogPreview() {
    HomeDialog(
        "",
        {}
    )
}
