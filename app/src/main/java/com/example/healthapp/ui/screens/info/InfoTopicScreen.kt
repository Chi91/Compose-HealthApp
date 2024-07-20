package com.example.healthapp.ui.screens.info

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.healthapp.R
import com.example.healthapp.ui.screens.scaffold.TopAppBarArrow

@Composable
fun InfoTopicScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBarArrow(navigateBack = navigateBack)
        }
    ) {
        InfoTopicBodyScreen(modifier = Modifier.padding(it))
    }
}

@Composable
fun InfoTopicBodyScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .padding(horizontal = 50.dp)
                .padding(top = 50.dp, bottom = 50.dp),
        ) {
            Text(
                text = "Diese Seite wird aktuell noch bearbeitet!",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
            )
        }
        Image(
            painter = painterResource(
                id = R.drawable.undraw_under_construction__46_pa
            ),
            contentDescription = "picture of still under construction",
            modifier = Modifier.size(300.dp)
        )
    }
}

@Preview
@Composable
fun InfoTopicScreenPreview() {
    InfoTopicScreen(navigateBack = {})
}