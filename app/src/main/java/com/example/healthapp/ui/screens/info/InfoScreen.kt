package com.example.healthapp.ui.screens.info

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthapp.R
import com.example.healthapp.data.InfoCardTopic
import com.example.healthapp.data.chipTopicList
import com.example.healthapp.data.infoCardTopicLists

@Composable
fun InfoScreen(
    modifier: Modifier = Modifier,
    navigateToTopic: () -> Unit,
    navigateToDrugDetail: (String) -> Unit
) {
    val viewModel: InfoViewModel = viewModel(factory = InfoViewModel.Factory)
    Column(
        modifier = modifier
    ) {
        CustomizeTextField(navigateToDrugDetail = navigateToDrugDetail, viewModel = viewModel)
        TopicSectionChip(topics = chipTopicList)
        FeatureSection(navigateToTopic = navigateToTopic)
    }
}

@Composable
fun CustomizeTextField(
    navigateToDrugDetail: (String) -> Unit,
    viewModel: InfoViewModel,
    modifier: Modifier = Modifier
) {
    val currentContext = LocalContext.current
    OutlinedTextField(
        value = viewModel.uiStateInfo.inputField,
        onValueChange = {
            viewModel.updateInfoState(
                viewModel.uiStateInfo.copy(
                    inputField = it,
                    inputIsNotValid = false
                )
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = ""
            )
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    viewModel.scanBarcode(
                        context = currentContext,
                        navigateTo = navigateToDrugDetail
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.PhotoCamera,
                    contentDescription = null
                )
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(10.dp),
        label = { Text(text = "Suche") },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions {
            viewModel.checkInputFieldPzn(
                inputPZN = viewModel.uiStateInfo.inputField,
                navigateTo = navigateToDrugDetail
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = dimensionResource(id = R.dimen.padding_medium),
                top = dimensionResource(id = R.dimen.padding_medium),
                end = dimensionResource(id = R.dimen.padding_medium),
                bottom = dimensionResource(id = R.dimen.padding_small)
            ),
        isError = viewModel.uiStateInfo.inputIsNotValid,
        supportingText = {
            if (viewModel.uiStateInfo.inputIsNotValid) {
                Text(
                    text = "Geben Sie eine 8stellige PZN an!",
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    )
    if (viewModel.uiStateInfo.showDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.updateInfoState(viewModel.uiStateInfo.copy(showDialog = false)) },
            title = { Text("PZN existiert nicht!") },
            icon = { Icons.Default.Info },
            text = { Text("Entweder existiert die PZN nicht oder die Eingabe war nicht korrekt!") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.updateInfoState(
                        viewModel.uiStateInfo.copy(
                            showDialog = false
                        )
                    )
                }) {
                    Text("Verstanden")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.updateInfoState(
                        viewModel.uiStateInfo.copy(
                            showDialog = false
                        )
                    )
                }) {
                    Text("Abbrechen")
                }
            },
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicSectionChip(topics: List<String>) {
    var selectedTopic by remember { mutableStateOf(0) }
    LazyRow {
        items(topics.size) { index ->
            ElevatedFilterChip(
                selected = index == selectedTopic,
                onClick = { selectedTopic = index },
                label = { Text(text = topics[index], fontSize = 15.sp) },
                modifier = Modifier.padding(start = 15.dp, top = 4.dp, bottom = 4.dp),
                colors = FilterChipDefaults.elevatedFilterChipColors(containerColor = Color.White),
                elevation =
                (FilterChipDefaults.elevatedFilterChipElevation(elevation = 5.dp)),
            )
        }
    }
}


@Composable
fun FeatureSection(
    modifier: Modifier = Modifier,
    navigateToTopic: () -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Themengebiete",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(15.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(
                start = 7.5.dp,
                end = 7.5.dp,
                bottom = 10.dp
            )
        ) {
            items(items = infoCardTopicLists) {
                cardItem(
                    infoCardTopic = it,
                    navigateToTopic = navigateToTopic
                )
            }
        }

    }
}

@Composable
fun cardItem(
    infoCardTopic: InfoCardTopic,
    navigateToTopic: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(100.dp)
            .height(230.dp)
            .padding(8.dp)
            .clickable { },
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable { navigateToTopic() }
        ) {
            Image(
                painter = painterResource(id = infoCardTopic.image),
                contentDescription = null
            )
            Text(
                text = stringResource(id = infoCardTopic.title),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(10.dp)
            )
            Text(
                text = stringResource(id = infoCardTopic.description),
                fontSize = 13.sp,
                modifier = Modifier.padding(6.dp),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = Color.Gray
            )
        }
    }
}


@Composable
fun DailyTopicVideoSection(modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clip(RoundedCornerShape(10.dp))
            .shadow(elevation = 10.dp)
            .background(Color.Red)
            .padding(horizontal = 15.dp, vertical = 20.dp)
    ) {
        Column() {
            Text(
                text = "Fettstoffwechsel",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Dauer und Länge",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.Blue)
                .padding(10.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                tint = Color.White,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun cardItemPreview() {
    cardItem(
        InfoCardTopic(
            title = R.string.InfoCardTopicSport,
            image = R.drawable.sport,
            description = R.string.text_placeholder
        ), navigateToTopic = {}
    )
}

@Preview(showBackground = true)
@Composable
fun topicVideoSectionPreview() {
    DailyTopicVideoSection()
}

@Preview(showBackground = true)
@Composable
fun FeatureSectionPreview() {
    FeatureSection(navigateToTopic = {})
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    InfoScreen(navigateToTopic = {}) {
    }
}