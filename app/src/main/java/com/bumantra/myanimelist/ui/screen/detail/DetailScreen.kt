package com.bumantra.myanimelist.ui.screen.detail

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.bumantra.myanimelist.R
import com.bumantra.myanimelist.data.local.entity.FavoriteAnime
import com.bumantra.myanimelist.data.remote.response.Images
import com.bumantra.myanimelist.di.Injection
import com.bumantra.myanimelist.ui.common.UiState
import com.bumantra.myanimelist.ui.component.FavoriteIcon
import com.bumantra.myanimelist.util.ViewModelFactory

@Composable
fun DetailScreen(
    animeId: Long, viewModel: DetailViewModel = viewModel(
        factory = ViewModelFactory(
            Injection.provideRepository(LocalContext.current)
        )
    ), navigateBack: () -> Unit
) {
    val current = LocalContext.current

    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.getDetailById(animeId)
            }

            is UiState.Success -> {
                val data = uiState.data
                val favoriteAnime = FavoriteAnime(
                    id = data.malId,
                    image = data.images.jpg.imageUrl,
                    title = data.title,
                    rating = data.rating
                )
                val isFavoriteAnime = viewModel.isFavoriteAnime(data.malId)

                DetailContent(
                    data.images,
                    data.title,
                    data.rating,
                    data.synopsis,
                    onBackClick = navigateBack,
                    isFavorite = isFavoriteAnime,
                    onFavoriteClick = { viewModel.toggleFavoriteStatus(favoriteAnime) },
                )
            }

            is UiState.Error -> {
                Toast.makeText(current, uiState.errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun DetailContent(
    image: Images,
    title: String,
    rating: String,
    synopsis: String,
    onBackClick: () -> Unit,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Box {
                AsyncImage(model = image.jpg.imageUrl,
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .drawWithCache {
                            val gradient = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.LightGray),
                                startY = size.height / 5,
                                endY = size.height
                            )
                            onDrawWithContent {
                                drawContent()
                                drawRect(gradient, blendMode = BlendMode.Multiply)
                            }
                        })
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back_to_home),
                    modifier = Modifier
                        .padding(16.dp)
                        .width(32.dp)
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = MaterialTheme.shapes.extraLarge
                        )
                        .clickable { onBackClick() }
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-32).dp)
                    .background(
                        MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                    )
                    .padding(start = 16.dp, top = 24.dp, end = 16.dp)
            ) {
                FavoriteIcon(
                    isFavorite = isFavorite,
                    onClick = onFavoriteClick
                )

                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                    ),
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = rating, style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Medium
                    ), color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = synopsis,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.padding(
                        top = 12.dp, start = 16.dp, end = 16.dp, bottom = 12.dp
                    )
                )
            }
        }
    }
}