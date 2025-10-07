package com.example.hinovateste.ui.autoShopDetails

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.hinovateste.R
import com.example.hinovateste.data.remote.AutoShopDto

@Composable
fun AutoShopDetailsScreen(
    autoShop: AutoShopDto?,
    navController: NavController
) {
    val context = LocalContext.current

    if (autoShop == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(stringResource(R.string.auto_shop_not_found))
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                backgroundColor = Color.White,
                elevation = 0.dp,
                modifier = Modifier.systemBarsPadding()
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {

                autoShop.Foto?.let { base64Str ->
                    val imageBitmap = remember(base64Str) {
                        try {
                            val bytes =
                                android.util.Base64.decode(base64Str, android.util.Base64.DEFAULT)
                            val options = BitmapFactory.Options().apply {
                                inPreferredConfig = android.graphics.Bitmap.Config.ARGB_8888
                            }
                            BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
                        } catch (e: Exception) {
                            null
                        }
                    }

                    if (imageBitmap != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(imageBitmap.width.toFloat() / imageBitmap.height.toFloat())
                                .clip(RoundedCornerShape(16.dp))
                                .border(2.dp, Color.Black, RoundedCornerShape(16.dp))
                        ) {
                            Image(
                                bitmap = imageBitmap.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.FillWidth
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                val valueUnavailable = stringResource(R.string.value_unavailable)

                Text(
                    text = autoShop.Nome ?: "",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 4.dp)
                    )
                    Text(
                        text = autoShop.Endereco ?: valueUnavailable,
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = autoShop.Descricao?.replace("\\n", "\n") ?: "",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(stringResource(R.string.label_phone, autoShop.Telefone1 ?: valueUnavailable))

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 4.dp)
                    )
                    Text(
                        text = autoShop.Email ?: valueUnavailable,
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(modifier = Modifier.height(100.dp)) // espaço extra para não cobrir conteúdo
            }
        }

        val context = LocalContext.current

        val takePictureLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { /* não é necessário tratar imagem */ }

        val cameraPermissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                takePictureLauncher.launch(intent)
            } else {
                Toast.makeText(context, "Permissão de câmera negada", Toast.LENGTH_SHORT).show()
            }
        }

        Button(
            onClick = {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    takePictureLauncher.launch(intent)
                } else {
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(stringResource(R.string.qr_code_reader))
        }
    }
}
