package com.bumantra.myanimelist.ui.screen.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.bumantra.myanimelist.R
import com.bumantra.myanimelist.ui.component.RowDescription
import com.bumantra.myanimelist.ui.component.TitleScreen
import com.bumantra.myanimelist.ui.theme.MyAnimeListTheme

@Composable
fun AboutScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    )
    {
        TitleScreen(
            title = stringResource(R.string.about_me),
        )
        Box(modifier = modifier, contentAlignment = Alignment.BottomCenter) {
            AsyncImage(
                model = stringResource(R.string.about_img),
                contentDescription = stringResource(R.string.foto_profile),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(300.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Text(
                text = stringResource(R.string.my_name),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .width(300.dp)
                    .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
        RowDescription(
            imageVector = Icons.Default.Email,
            title = stringResource(R.string.my_email)
        )
        RowDescription(
            imageVector = Icons.Default.LocationOn,
            title = stringResource(R.string.my_city)
        )

    }
}

@Preview(showBackground = true)
@Composable
fun AboutPrev() {
    MyAnimeListTheme {
        AboutScreen()
    }
}