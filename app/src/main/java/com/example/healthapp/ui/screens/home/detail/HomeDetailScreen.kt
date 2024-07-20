package com.example.healthapp.ui.screens.home.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthapp.ui.screens.scaffold.FloatingButtonEdit
import com.example.healthapp.ui.screens.scaffold.TopAppBarItem
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeDetailScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    navigateTo: (String) -> Unit,
    navigateHome: () -> Unit,
) {
    val viewModel: HomeDetailViewModel = viewModel(factory = HomeDetailViewModel.Factory)

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBarItem(
                navigateBack = navigateBack,
                name = viewModel.uiStateHomeDetail.drugDetail.name,
                deleteItem = { viewModel.setDeleteDialogTrue() },
                scrollBehavior = scrollBehavior
            )
        }, floatingActionButton = {
            FloatingButtonEdit(
                navigateTo = navigateTo, pzn = viewModel.uiStateHomeDetail.drugDetail.pzn
            )
        }) { innerPadding ->
        HomeDetailBody(
            modifier = Modifier.padding(innerPadding),
            viewModel = viewModel,
            navigateHome = navigateHome
        )
    }
}

@Composable
fun HomeDetailBody(
    modifier: Modifier = Modifier,
    viewModel: HomeDetailViewModel,
    navigateHome: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 30.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = viewModel.getImage()),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .aspectRatio(16f / 9f)
                .padding(bottom = 25.dp)
        )
        if (viewModel.uiStateHomeDetail.deleteDialog) {
            DeleteDialog(viewModel = viewModel, navigateHome = navigateHome)
        }
        val topicPair =
            listOf(Pair<String, @Composable () -> Unit>("Allgemeiner Hinweis") {
                GeneralCard(
                    viewModel = viewModel
                )
            },
                Pair<String, @Composable () -> Unit>("Einnahmezeiten") {
                    DosageInstructionCard(
                        viewModel = viewModel
                    )
                },
                Pair<String, @Composable () -> Unit>("Eigene Notizen") { NoteCard(viewModel = viewModel) })
        for ((time, customizeComposable) in topicPair) {
            TopicSection(title = time) {
                customizeComposable()
            }
        }
    }
}

@Composable
fun DeleteDialog(
    viewModel: HomeDetailViewModel,
    navigateHome: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    AlertDialog(
        onDismissRequest = { viewModel.setDeleteDialogFalse() },
        title = { Text("Arzneimittel löschen?") },
        icon = { Icons.Default.Info },
        text = {
            Text(
                text = "Das Arzneimittel wird aus Ihrer Liste entfernt. Sind Sie sich sicher?",
                textAlign = TextAlign.Justify
            )
        },
        confirmButton = {
            TextButton(onClick = {
                scope.launch {
                    viewModel.deleteDrug(navigateHome = navigateHome)
                }
            }) {
                Text("Löschen")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                viewModel.setDeleteDialogFalse()
            }) {
                Text("Abbrechen")
            }
        },
    )
}

@Composable
fun TopicSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier.padding(start = 25.dp, end = 25.dp, bottom = 25.dp)
    ) {
        Text(
            text = title, fontSize = 20.sp, modifier = Modifier.padding(10.dp)
        )
        Divider()
        content()
    }
}

@Composable
fun GeneralCard(viewModel: HomeDetailViewModel, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(10.dp)) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp),
        ) {
            Text(text = "Wirkstoff", modifier = Modifier.padding(5.dp))
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = viewModel.uiStateHomeDetail.drugDetail.activeIngredient,
                modifier = Modifier.padding(5.dp)
            )
        }
        Row(
            modifier = Modifier.padding(horizontal = 10.dp),
        ) {
            Text(text = "PZN", modifier = Modifier.padding(5.dp))
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = viewModel.uiStateHomeDetail.drugDetail.pzn,
                modifier = Modifier.padding(5.dp)
            )
        }
        Row(
            modifier = Modifier.padding(horizontal = 10.dp),
        ) {
            Text(text = "Teilbar", modifier = Modifier.padding(5.dp))
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = viewModel.uiStateHomeDetail.drugDetail.divisible,
                modifier = Modifier.padding(5.dp)
            )
        }
        Row(
            modifier = Modifier.padding(horizontal = 10.dp),
        ) {
            Text(text = "Mahlzeit", modifier = Modifier.padding(5.dp))
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = viewModel.uiStateHomeDetail.drugDetail.dosageInstruction,
                modifier = Modifier.padding(5.dp)
            )
        }
    }
}

@Composable
fun DosageInstructionCard(modifier: Modifier = Modifier, viewModel: HomeDetailViewModel) {
    Column(modifier = modifier.padding(vertical = 10.dp)) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
        ) {
            Text(
                text = "6:00 Uhr",
                modifier = Modifier.padding(5.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${viewModel.uiStateHomeDetail.drugDetail.morning.takeIf { it.isNotBlank() } ?: "0"} Tablette(n)",
                modifier = Modifier.padding(5.dp)
            )
        }
        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
        ) {
            Text(
                text = "12:00 Uhr",
                modifier = Modifier.padding(5.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${viewModel.uiStateHomeDetail.drugDetail.noon.takeIf { it.isNotBlank() } ?: "0"} Tablette(n)",
                modifier = Modifier.padding(5.dp)
            )
        }
        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
        ) {
            Text(
                text = "18:00 Uhr",
                modifier = Modifier.padding(5.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${viewModel.uiStateHomeDetail.drugDetail.evening.takeIf { it.isNotBlank() } ?: "0"} Tablette(n)",
                modifier = Modifier.padding(5.dp)
            )
        }
        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
        ) {
            Text(
                text = "22:00 Uhr",
                modifier = Modifier.padding(5.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${viewModel.uiStateHomeDetail.drugDetail.night.takeIf { it.isNotBlank() } ?: "0"} Tablette(n)",
                modifier = Modifier.padding(5.dp)
            )
        }

    }
}

@Composable
fun NoteCard(modifier: Modifier = Modifier, viewModel: HomeDetailViewModel) {
    Column(modifier = modifier.padding(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "${viewModel.uiStateHomeDetail.drugDetail.notes.takeIf { it.isNotBlank() } ?: "Sie haben keine Hinweise hinterlassen"} ",
                textAlign = TextAlign.Justify,
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeDetailPreview() {
    HomeDetailScreen(navigateBack = {}, navigateTo = {}, navigateHome = {})
}