package com.ndejje.momocalc

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.ndejje.momocalc.ui.MoMoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoMoAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Scaffold(
                        topBar = { MoMoTopBar() }
                    ) { innerPadding ->
                        MoMoCalcScreen(
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
} // End of MainActivity Class

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoMoTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_title),
                style = MaterialTheme.typography.headlineMedium
            )
        },
        navigationIcon = {
            Image(
                painter = painterResource(id = R.drawable.ic_momo_logo),
                contentDescription = "MoMo Logo",
                modifier = Modifier
                    .padding(start = dimensionResource(R.dimen.spacing_medium))
                    .height(dimensionResource(R.dimen.spacing_large))
                    .wrapContentWidth(),
                contentScale = ContentScale.Fit
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Composable
fun MoMoCalcScreen(modifier: Modifier = Modifier) {
    //state to the input initialized as empty
    var amountInput by remember { mutableStateOf("") }
    //convert input to double safely
    val numericAmount = amountInput.toDoubleOrNull()
    //error state: true if not empty but not a valid number
    val isError = amountInput.isNotEmpty() && numericAmount == null
    //calculate a 3% and 1.5% fee (if null/error, treat as 0.0)
    val fee = if ((numericAmount ?: 0.0) < 2500000) {
        (numericAmount ?: 0.0) * 0.03
    } else {
        (numericAmount ?: 0.0) * 0.015
    }
    //Format to UGX with thousand separators
    val formattedFee = "UGX %,.0f".format(fee)

    Column(
        modifier = modifier
            .fillMaxSize()              // occupy full screen — centering needs space
            .padding(dimensionResource(R.dimen.screen_padding)),
        verticalArrangement = Arrangement.Center,         // vertical middle
        horizontalAlignment = Alignment.CenterHorizontally // horizontal centre
    ) {
        Text(
            text = stringResource(R.string.app_title),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center  // centres text within its own bounding box
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))

        HoistedAmountInput(
            amount = amountInput,
            onAmountChange = { amountInput = it },//This called a lambda
            isError = isError
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))

        // --- ADDED STEP D3 HERE ---
        // Wrapping the fee display in a themed, shaped Surface
        Surface(
            shape = MaterialTheme.shapes.medium, // Pulls the corner radius from your Shape.kt
            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f), // Using a theme-aware color
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.fee_label, formattedFee),
                style = MaterialTheme.typography.headlineSmall, // Made slightly larger for visibility
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(dimensionResource(R.dimen.spacing_medium))
            )
        }
    }
}

@Composable
fun HoistedAmountInput(
    amount: String,//allows state to flow in
    onAmountChange: (String) -> Unit,//allows events to flow out
    isError: Boolean = false,
    modifier: Modifier = Modifier   // ← new parameter with safe default
) {
    Column(modifier = modifier) {        // ← modifier applied to outer Column
        OutlinedTextField(
            value = amount,
            onValueChange = onAmountChange,
            label = { Text(stringResource(R.string.enter_amount)) },
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
    MoMoAppTheme {
        MoMoCalcScreen()
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Composable
fun PreviewLight() {
    MoMoAppTheme(darkTheme = false) {
        MoMoCalcScreen()
    }
}

@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PreviewDark() {
    MoMoAppTheme(darkTheme = true) {
        MoMoCalcScreen()
    }
}