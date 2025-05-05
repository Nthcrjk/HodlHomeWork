package ru.flx.hodlhomework.ui.home

import android.widget.ProgressBar
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.example.compose.HodlHomeWorkTheme
import ru.flx.hodlhomework.ui.data.CoinTransaction
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    balanceAmount: Long?,
    amountToSend: Long?,
    addressToSend: String?,
    dialogState: DialogUiState,
    listState: MutableList<CoinTransaction> = mutableListOf<CoinTransaction>(),
    onLoadMoreTransactions: () -> Unit,
    onChangeAmountToSend: (Long?) -> Unit,
    onChangeAddressToSend: (String) -> Unit,
    onSend: () -> Unit,
    onDismissDialog: () -> Unit,
) {
    if (dialogState.isShowDialog) {
        dialogState.txId?.let {
            HomeDialog(
                it,
                {
                    onDismissDialog()
                }
            )
        }
    }
    Scaffold {
        Box(modifier = Modifier
            .padding(it)
            .fillMaxSize()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .align(Alignment.Center),
                elevation = CardDefaults.elevatedCardElevation(24.dp)
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
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = "Balance",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.height(8.dp))
                        if (balanceAmount == null) {
                            CircularProgressIndicator()
                        } else {
                            Text(
                                text = "${"%.5f".format(balanceAmount.toDouble()/100_000)} tBTC",
                                style = MaterialTheme.typography.displayMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(Modifier.height(24.dp))

                        OutlinedTextField(
                            value = addressToSend?.toString() ?: "",
                            onValueChange = {
                                onChangeAddressToSend(it)
                            },
                            label = { Text("BTC-адрес", color = Color.Gray) },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                        Spacer(Modifier.height(4.dp))
                        OutlinedTextField(
                            value = amountToSend?.toString() ?: "",
                            onValueChange = {
                                if (it.isDigitsOnly()) {
                                    onChangeAmountToSend(it.toLongOrNull())
                                }
                            },
                            label = { Text("Сумма", color = Color.Gray) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                        )
                        Spacer(Modifier.height(16.dp))
                        OutlinedButton (
                            onClick = onSend,
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
                        Spacer(Modifier.height(16.dp))
                        HorizontalDivider(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                        )
                        Spacer(Modifier.height(16.dp))
                        if (listState.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        } else {
                            LazyTransactions(
                                listState,
                                onLoadMoreTransactions
                            )
                        }

                    }
                }
            }
        }

    }
}

@Composable
fun LazyTransactions(
    state: MutableList<CoinTransaction> = mutableListOf<CoinTransaction>(),
    onLoadMoreTransactions: () -> Unit
){
    val listState = rememberLazyListState()
    LazyColumn(
        state = listState
    ) {
        items(state) {
            TransactionItem(
                it.isReceived,
                txId = it.txId,
                timestamp = it.timestamp,
                amount = it.amount
            )
        }
    }
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            val totalItems = listState.layoutInfo.totalItemsCount

            lastVisibleItem?.index ?: 0 >= totalItems - 10
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            onLoadMoreTransactions()
        }
    }
}

@Composable
fun TransactionItem(
    isReceived: Boolean,
    txId: String,
    timestamp: Long,
    amount: Long
) {
    val backgroundColor = if (isReceived) Color(0xFFDFF5E3) else Color(0xFFFFE0E0)
    val icon = if (isReceived) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown
    val iconTint = if (isReceived) Color(0xFF2E7D32) else Color(0xFFC62828)
    val amountColor = if (isReceived) Color(0xFF388E3C) else Color(0xFFD32F2F)

    val date = remember(timestamp) {
        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        sdf.format(Date(timestamp * 1000))
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isReceived) "Получено" else "Отправлено",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "TX: ${txId.take(6)}...${txId.takeLast(6)}",
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = date,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Text(
                text = "${amount} sats",
                fontWeight = FontWeight.Bold,
                color = amountColor,
                fontSize = 14.sp
            )
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HodlHomeWorkTheme {
        HomeScreen(
            750L,
            null,
            null,
            DialogUiState(),
            mutableListOf<CoinTransaction>(),
            {},
            {},
            {},
            {},
            {},
        )
    }
}