package com.example.hinovateste.ui.autoShops.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hinovateste.R
import com.example.hinovateste.data.remote.AutoShopDto

@Composable
fun AutoShopListItem(autoShop: AutoShopDto, onOpenDetails: (AutoShopDto) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onOpenDetails(autoShop) }
            .border(
                width = 2.dp,
                color = Color.Black,
                shape = RoundedCornerShape(50)
            ),
        shape = RoundedCornerShape(50),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = autoShop.Nome,
                style = MaterialTheme.typography.titleSmall
            )

            val isActive = autoShop.Ativo == true

            Box(
                modifier = Modifier
                    .background(
                        color = if (isActive)
                            Color(0xFF4CAF50)
                        else
                            Color(0xFFF44336),
                        shape = CircleShape
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = if (isActive)
                        stringResource(R.string.active)
                    else
                        stringResource(R.string.inactive),
                    color = Color.White,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}