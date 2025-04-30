package ru.flx.hodlhomework.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.HodlHomeWorkTheme

@Composable
fun HomeScreen(
    balanceAmount: Double,
    amountToSend: Double,
    addressToSend: String,
    onChangeAmountToSend: () -> Unit,
    onChangeAddressToSend: () -> Unit,
    onSend: () -> Unit
) {
    Scaffold {
        Box(modifier = Modifier
            .padding(it)
            .fillMaxSize()
        ) {
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Center),
                elevation = CardDefaults.elevatedCardElevation(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(16.dp),
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Bitcoin wallet",
                            style = MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        Spacer(Modifier.height(24.dp))
                        Text(
                            text = "Balance",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "0.750 " + "BTC",
                            style = MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.height(24.dp))
                        TextField(
                            value = "",
                            onValueChange = {},

                        )
                        TextField(
                            value = "",
                            onValueChange = {},
                        )
                        Spacer(Modifier.height(48.dp))
                        OutlinedButton (
                            onClick = {},
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            shape = RoundedCornerShape(4.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color.Transparent,
                            )
                        ) {
                            Text(
                                text = "Send",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HodlHomeWorkTheme {
        HomeScreen(
            0.750,
            0.0,
            "",
            {},
            {},
            {}
        )
    }
}