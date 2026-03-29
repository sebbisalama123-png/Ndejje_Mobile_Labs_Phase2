package com.ndejje.momocalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ndejje.momocalc.ui.theme.MoMoTypography

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme(typography = MoMoTypography) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MoMoCalcScreen()
                }
            }
        }
    }
}

@Composable
fun MoMoCalcScreen() {
    var amountInput by remember { mutableStateOf("") }

    val numericAmount = amountInput.toDoubleOrNull()
    val isError = amountInput.isNotEmpty() && numericAmount == null
    val fee = (numericAmount ?: 0.0) * 0.03
    val formattedFee = "UGX %,.0f".format(fee)

    Column(
        modifier = Modifier
            .fillMaxSize()// occupy full screen — centering needs space
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,// vertical middle
        horizontalAlignment = Alignment.CenterHorizontally // horizontal centre
    ) {
        Text(
            text = stringResource(R.string.app_title),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center  // centres text within its own bounding box
        )
        Spacer(modifier = Modifier.height(24.dp))

        HoistedAmountInput(
            amount = amountInput,
            onAmountChange = { amountInput = it },
            isError = isError
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.fee_label, formattedFee),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

// This function was missing from your original code
@Composable
fun HoistedAmountInput(
    amount: String,
    onAmountChange: (String) -> Unit,
    isError: Boolean = false,
    modifier: Modifier = Modifier   // ← new parameter with safe default
) {
    Column(modifier = modifier) {        // ← modifier applied to outer Column
    OutlinedTextField(
        value = amount,
        onValueChange = onAmountChange,
        label = { Text(stringResource(R.string.enter_amount))},
        isError = isError,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true
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

@Preview(showBackground = true)
@Composable
fun MoMoCalcPreview() {
    MaterialTheme { MoMoCalcScreen() }
}