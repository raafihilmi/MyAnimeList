package com.bumantra.myanimelist.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun FavoriteIcon(isFavorite: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val icon: ImageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder
    val contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites"

    Icon(
        imageVector = icon,
        contentDescription = contentDescription,
        tint = MaterialTheme.colorScheme.surfaceTint,
        modifier = modifier
            .size(30.dp)
            .clickable { onClick() }
    )
}