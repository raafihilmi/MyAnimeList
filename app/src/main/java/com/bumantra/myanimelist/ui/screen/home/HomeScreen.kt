package com.bumantra.myanimelist.ui.screen.home

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumantra.myanimelist.R
import com.bumantra.myanimelist.data.remote.response.DataItem
import com.bumantra.myanimelist.ui.common.UiState
import com.bumantra.myanimelist.ui.component.AnimeItem
import com.bumantra.myanimelist.ui.component.SearchBar
import com.bumantra.myanimelist.util.ViewModelFactory

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory.getInstance(LocalContext.current)
    ),
    navigateToDetail: (Long) -> Unit
) {
    val query by viewModel.query

    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                HomeLoading()
                viewModel.getAllListAnime()

            }

            is UiState.Success -> {
                HomeContent(
                    listAnime = uiState.data,
                    query = query,
                    onQueryChange = viewModel::search,
                    modifier = modifier,
                    navigateToDetail = navigateToDetail
                )
            }

            is UiState.Error -> {
                HomeError(modifier, uiState.errorMessage)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeContent(
    listAnime: List<DataItem>,
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    navigateToDetail: (Long) -> Unit,
) {
    val listState = rememberLazyListState()
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.testTag("AnimeList")
    ) {
        item {
            SearchBar(
                query = query,
                onQueryChange = onQueryChange,
                modifier = Modifier.fillMaxWidth()
            )
        }

        items(listAnime, key = { it.malId }) { data ->
            AnimeItem(
                image = data.images.jpg.imageUrl,
                title = data.title,
                rating = data.rating,
                modifier = Modifier
                    .clickable {
                        navigateToDetail(data.malId.toLong())
                    }
                    .animateItemPlacement()
            )
        }
    }
}

@Composable
private fun HomeLoading(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("Load"),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun HomeError(modifier: Modifier = Modifier, errorMessage: String) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("HomeError"),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current
        Text(stringResource(R.string.no_internet))
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }
}