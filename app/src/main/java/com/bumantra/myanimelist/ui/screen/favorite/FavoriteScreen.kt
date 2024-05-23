package com.bumantra.myanimelist.ui.screen.favorite

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumantra.myanimelist.R
import com.bumantra.myanimelist.data.local.entity.FavoriteAnime
import com.bumantra.myanimelist.ui.component.AnimeItem
import com.bumantra.myanimelist.ui.component.TitleScreen
import com.bumantra.myanimelist.util.ViewModelFactory

@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier, viewModel: FavoriteViewModel = viewModel(
        factory = ViewModelFactory.getInstance(LocalContext.current)
    ), navigateToDetail: (Long) -> Unit
) {
    val favoriteAnimeList by viewModel.getFavoriteList.collectAsState(emptyList())

    if (favoriteAnimeList.isEmpty()) {
        FavoriteEmpty()
    } else {
        FavoriteContent(anime = favoriteAnimeList, modifier, navigateToDetail = navigateToDetail)
    }
}

@Composable
fun FavoriteContent(
    anime: List<FavoriteAnime>, modifier: Modifier = Modifier, navigateToDetail: (Long) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    ) {
        item {
            TitleScreen(title = stringResource(R.string.favorite_anime))
        }
        items(anime, key = { it.id }) { data ->
            AnimeItem(image = data.image,
                title = data.title,
                rating = data.rating,
                modifier.clickable { navigateToDetail(data.id.toLong()) })
        }
    }
}

@Composable
fun FavoriteEmpty(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(R.string.no_favorite),
        )
    }
}