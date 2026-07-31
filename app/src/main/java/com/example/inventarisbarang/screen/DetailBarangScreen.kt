package com.example.inventarisbarang.screen

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.inventarisbarang.data.BarangRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailBarangScreen(navController: NavHostController, barangId: Long) {
    val context = LocalContext.current
    val repository = remember { BarangRepository(context) }
    var barang by remember { mutableStateOf(repository.getBarangById(barangId)) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(navController.currentBackStackEntry) {
        barang = repository.getBarangById(barangId)
    }

    if (barang == null) {
        Box(Modifier.fillMaxSize(), Alignment.Center) {
            Button(onClick = { navController.popBackStack() }) { Text("Go Back") }
        }
        return
    }

    val item = barang!!

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Overview", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("edit/${item.id}") }) {
                        Icon(Icons.Default.Edit, null)
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())) {
            // Hero Image
            Box(modifier = Modifier.fillMaxWidth().height(300.dp).padding(16.dp)) {
                if (item.gambarUri.isNotEmpty()) {
                    AsyncImage(
                        model = ImageRequest.Builder(context).data(Uri.parse(item.gambarUri)).crossfade(true).build(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(32.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(32.dp)),
                        Alignment.Center
                    ) {
                        Text(item.nama.take(1), fontSize = 80.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                }
            }

            Column(Modifier.padding(24.dp)) {
                Text(item.nama, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
                Text(item.kategori, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                
                Spacer(Modifier.height(32.dp))

                InfoSection("Pricing", formatRupiah(item.harga))
                InfoSection("Inventory Level", "${item.stok} units available")
                
                if (item.deskripsi.isNotEmpty()) {
                    Spacer(Modifier.height(24.dp))
                    Text("Description", fontWeight = FontWeight.Bold)
                    Text(item.deskripsi, color = MaterialTheme.colorScheme.outline, lineHeight = 20.sp)
                }

                Spacer(Modifier.height(48.dp))

                Button(
                    onClick = { navController.navigate("edit/${item.id}") },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Edit, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Modify Details")
                }

                TextButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Icon(Icons.Default.Delete, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Remove from Inventory")
                }
            }
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Item?") },
                text = { Text("Are you sure you want to remove this item? This action is permanent.") },
                confirmButton = {
                    TextButton(onClick = {
                        repository.hapusBarang(item.id)
                        showDeleteDialog = false
                        navController.popBackStack()
                    }, colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
                }
            )
        }
    }
}

@Composable
fun InfoSection(label: String, value: String) {
    Column(Modifier.padding(vertical = 8.dp)) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
        Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
    }
}
