package com.example.healthapp.ui.screens.info

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthapp.ui.screens.scaffold.TopAppBarArrowName


@Composable
fun InfoDrugDetailScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: InfoDetailViewModel = viewModel(factory = InfoDetailViewModel.Factory)

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBarArrowName(
                navigateBack = navigateBack,
                name = "Information"
            )
        }) { innerPadding ->
        InfoDrugDetailBody(
            modifier = Modifier.padding(innerPadding),
            viewModel = viewModel
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InfoDrugDetailBody(
    modifier: Modifier = Modifier,
    viewModel: InfoDetailViewModel,
    ) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        val pagerState = rememberPagerState(pageCount = { 2 })
        HorizontalPager(
            state = pagerState,
            pageSize = PageSize.Fixed(400.dp)
        ) {page ->
            Image(
                painter = painterResource(id = viewModel.getImage()[page]),
                contentDescription = "packing of drug",
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .aspectRatio(16f / 9f)
            )
        }
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.End)
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color.Blue else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(8.dp)
                )
            }
        }
        Text(
            text = viewModel.uiStateInfoDetail.drug.name,
            fontSize = 24.sp,
            modifier = Modifier.padding(16.dp)
        )
        Text(
            text = viewModel.uiStateInfoDetail.drug.description,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        )
    }
}


@Preview
@Composable
fun SearchDetailScreenPreview() {
    InfoDrugDetailScreen(
        navigateBack = {}
    )
}

