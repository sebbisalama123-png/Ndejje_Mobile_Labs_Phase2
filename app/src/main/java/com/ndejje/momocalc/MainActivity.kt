package com.ndejje.momocalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

        }
    }
}

@Composable
fun BrokenInput() {
    var amount = ""

    TextField(
        value = amount,
        onValueChange = { amount = it },
        label = { Text(stringResource(R.string.enter_amount))}
    )
}

@Composable
fun InternalStateInput() {
    // State is now properly tracked by Compose
    var amount by remember { mutableStateOf("500") }

    TextField(
        value = amount,
        onValueChange = { amount = it },
        label = { Text(stringResource(R.string.enter_amount)) }
    )
}

@Composable
fun HoistedAmountInput(
    amount: String,                    // allows state flows in
    onAmountChange: (String) -> Unit,  // events flow out
    isError: Boolean = false
) {
    Column {
        TextField(
            value = amount,
            onValueChange = onAmountChange,
            isError = isError,
            label = { Text(stringResource(R.string.enter_amount)) }
        )
        if (isError) {
            Text(
                text = stringResource(R.string.error_numbers_only),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview(showBackground = true, name = "Empty State")
@Composable
fun PreviewEmpty() {
    HoistedAmountInput(amount = "", onAmountChange = {})
}

@Preview(showBackground = true, name = "Filled State")
@Composable
fun PreviewFilled() {
    HoistedAmountInput(amount = "50000", onAmountChange = {})
}

@Preview(showBackground = true, name = "Error State")
@Composable
fun PreviewError() {
    HoistedAmountInput(amount = "sebbi", onAmountChange = {}, isError = true)
}

