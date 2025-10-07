package com.example.hinovateste.ui.principal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.hinovateste.data.remote.AutoShopDto
import androidx.compose.ui.res.stringResource
import com.example.hinovateste.R
import com.example.hinovateste.ui.autoShops.AutoShopViewModel
import com.example.hinovateste.ui.autoShops.AutoShopsTab
import com.example.hinovateste.ui.recommendation.RecommendationTab

@Composable
fun PrincipalScreen(
    autoShopViewModel: AutoShopViewModel,
    onOpenDetails: (AutoShopDto) -> Unit
) {
    val tabIndex = remember { mutableStateOf(0) }
    val tabs = listOf(
        stringResource(R.string.auto_shops),
        stringResource(R.string.recommendation)
    )
    Column(modifier = Modifier.fillMaxSize()
        .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())) {
        TabRow(
            selectedTabIndex = tabIndex.value,
            containerColor = Color.Black,
            contentColor = Color.White,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    color = Color.White,
                    modifier = Modifier.tabIndicatorOffset(tabPositions[tabIndex.value])
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = tabIndex.value == index,
                    onClick = { tabIndex.value = index }
                ) {
                    Text(title, modifier = Modifier.padding(16.dp))
                }
            }
        }

        when (tabIndex.value) {
            0 -> AutoShopsTab(autoShopViewModel, onOpenDetails)
            1 -> RecommendationTab()
        }
    }

}