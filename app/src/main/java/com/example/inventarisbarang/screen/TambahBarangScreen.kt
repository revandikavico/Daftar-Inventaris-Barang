package com.example.inventarisbarang.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.inventarisbarang.data.BarangRepository
import com.example.inventarisbarang.model.Barang
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahBarangScreen(navController: NavHostController, editBarangId: Long? = null) {
    val context = LocalContext.current
    val repository = remember { BarangRepository(context) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val isEditMode = editBarangId != null
    val existingBarang = if (isEditMode) remember { repository.getBarangById(editBarangId!!) } else null

    var nama by remember { mutableStateOf(existingBarang?.nama ?: "") }
    var kategori by remember { mutableStateOf(existingBarang?.kategori ?: "") }
    var harga by remember { mutableStateOf(if (existingBarang != null && existingBarang.harga > 0) existingBarang.harga.toLong().toString() else "") }
    var stok by remember { mutableStateOf(if (existingBarang != null && existingBarang.stok > 0) existingBarang.stok.toString() else "") }
    var sku by remember { mutableStateOf(existingBarang?.sku ?: "") }
    var berat by remember { mutableStateOf(if (existingBarang != null && existingBarang.berat > 0) existingBarang.berat.toString() else "") }
    var deskripsi by remember { mutableStateOf(existingBarang?.deskripsi ?: "") }
    var gambarUri by remember { mutableStateOf(existingBarang?.gambarUri ?: "") }
    var expandedKategori by remember { mutableStateOf(false) }

    val daftarKategori = listOf("Gadgets", "Fashion", "Tools", "Books", "Health", "Service", "Living", "Hobby", "Others")

    val imagePickerLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            try {
                context.contentResolver.takePersistableUriPermission(it, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } catch (e: Exception) { e.printStackTrace() }
            gambarUri = it.toString()
        }
    }

    fun save() {
        if (nama.isBlank() || kategori.isBlank()) {
            scope.launch { snackbarHostState.showSnackbar("Please fill required fields") }
            return
        }
        val item = Barang(
            id = existingBarang?.id ?: System.currentTimeMillis(),
            nama = nama.trim(), kategori = kategori,
            harga = harga.toDoubleOrNull() ?: 0.0, stok = stok.toIntOrNull() ?: 0,
            sku = sku.trim(), berat = berat.toDoubleOrNull() ?: 0.0,
            deskripsi = deskripsi.trim(), gambarUri = gambarUri
        )
        if (isEditMode) repository.updateBarang(item) else repository.tambahBarang(item)
        navController.popBackStack()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (isEditMode) "Edit Entry" else "New Entry", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                actions = {
                    IconButton(onClick = { save() }) {
                        Icon(Icons.Default.Check, null, tint = MaterialTheme.colorScheme.primary)
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(24.dp)) {
            // Photo Area
            Box(
                modifier = Modifier.fillMaxWidth().height(160.dp).clip(RoundedCornerShape(32.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (gambarUri.isNotEmpty()) {
                    AsyncImage(
                        model = ImageRequest.Builder(context).data(Uri.parse(gambarUri)).crossfade(true).build(),
                        contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Add, null, modifier = Modifier.size(40.dp), tint = MaterialTheme.colorScheme.primary)
                        Text("Add Visual Preview", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            OutlinedTextField(
                value = nama, onValueChange = { nama = it }, label = { Text("Product Name") },
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)
            )

            Spacer(Modifier.height(16.dp))

            ExposedDropdownMenuBox(expanded = expandedKategori, onExpandedChange = { expandedKategori = !expandedKategori }) {
                OutlinedTextField(
                    value = kategori, onValueChange = {}, readOnly = true, label = { Text("Category") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedKategori) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(), shape = RoundedCornerShape(16.dp)
                )
                ExposedDropdownMenu(expanded = expandedKategori, onDismissRequest = { expandedKategori = false }) {
                    daftarKategori.forEach { item ->
                        DropdownMenuItem(text = { Text(item) }, onClick = { kategori = item; expandedKategori = false })
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = harga, onValueChange = { harga = it }, label = { Text("Price (Rp)") },
                    modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = stok, onValueChange = { stok = it }, label = { Text("Stock") },
                    modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = deskripsi, onValueChange = { deskripsi = it }, label = { Text("Brief Description") },
                modifier = Modifier.fillMaxWidth().height(120.dp), shape = RoundedCornerShape(16.dp)
            )

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = { save() }, modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(if (isEditMode) "Confirm Changes" else "Finalize Listing")
            }
        }
    }
}
