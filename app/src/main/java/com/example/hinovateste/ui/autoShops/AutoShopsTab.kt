package com.example.hinovateste.ui.autoShops

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.hinovateste.R
import com.example.hinovateste.data.remote.AutoShopDto
import com.example.hinovateste.ui.autoShops.components.AutoShopListItem
import com.google.android.gms.location.LocationServices

@Composable
fun AutoShopsTab(vm: AutoShopViewModel, onOpenDetails: (AutoShopDto)->Unit) {
    val loading by vm.loading.collectAsState()
    var onlyActive by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val defaultLocationText = stringResource(R.string.location_default)
    var locationText by remember { mutableStateOf(defaultLocationText) }
    val locationFormat = stringResource(R.string.location_format)
    val locationUnavailable = stringResource(R.string.location_unavailable)
    val permissionRequired = stringResource(R.string.permission_required)
    val permissionDenied = stringResource(R.string.permission_denied)

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted) {

                val fused = LocationServices.getFusedLocationProviderClient(context)
                try {
                    fused.lastLocation.addOnSuccessListener { loc ->
                        if (loc != null) {
                            locationText = String.format(locationFormat, loc.latitude, loc.longitude)
                        } else {
                            locationText = locationUnavailable
                        }
                    }
                } catch (e: SecurityException) {
                    locationText = permissionRequired
                }
            } else {
                locationText = permissionDenied
            }
        }
    )

    LaunchedEffect(Unit) {
        vm.loadOficinas()

        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.active))
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = onlyActive,
                    onCheckedChange = {
                        onlyActive = it
                        vm.showOnlyActive.value = it
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        uncheckedThumbColor = Color.Gray,
                        checkedTrackColor = Color.Black,
                        uncheckedTrackColor = Color.LightGray
                    )
                )
            }
        }

        if (loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            val list = vm.filteredList()
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(list) { autoShop ->
                    AutoShopListItem(autoShop, onOpenDetails)
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(
                    color = Color.Black,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(12.dp)
        ) {
            Text(
                text = locationText,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}