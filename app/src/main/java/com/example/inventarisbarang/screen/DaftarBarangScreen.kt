package com.example.inventarisbarang.screen

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.inventarisbarang.R
import com.example.inventarisbarang.data.BarangRepository
import com.example.inventarisbarang.model.Barang
import java.text.NumberFormat
import java.util.Locale

/**
 * DaftarBarangScreen (StockFlow Edition)
 * Menampilkan barang dalam format Grid 2 kolom yang modern.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaftarBarangScreen(navController: NavHostController) {
    val context = LocalContext.current
    val repository = remember { BarangRepository(context) }

    var daftarBarang by remember { mutableStateOf(repository.getSemuaBarang()) }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedTab by remember { mutableIntStateOf(0) }

    val filteredBarang = if (searchQuery.isEmpty()) {
        daftarBarang
    } else {
        daftarBarang.filter {
            it.nama.contains(searchQuery, ignoreCase = true) ||
                    it.kategori.contains(searchQuery, ignoreCase = true)
        }
    }

    LaunchedEffect(navController.currentBackStackEntry) {
        daftarBarang = repository.getSemuaBarang()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    if (isSearchActive) {
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Cari stok...") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().padding(end = 16.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent
                            )
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.app_name),
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        isSearchActive = !isSearchActive
                        if (!isSearchActive) searchQuery = ""
                    }) {
                        Icon(
                            imageVector = if (isSearchActive) Icons.Default.Close else Icons.Default.Search,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = { navController.navigate("tambah") },
                containerColor = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(24.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(32.dp))
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(if (selectedTab == 0) Icons.Filled.Home else Icons.Outlined.Home, null) },
                    label = { Text("Stok") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.Search, null) },
                    label = { Text("Kategori") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(if (selectedTab == 2) Icons.Filled.Person else Icons.Outlined.Person, null) },
                    label = { Text("Profil") }
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        when (selectedTab) {
            1 -> KategoriScreen(paddingValues = paddingValues)
            2 -> ProfilScreen(paddingValues, navController, daftarBarang)
            else -> {
                if (filteredBarang.isEmpty()) {
                    Box(Modifier.fillMaxSize().padding(paddingValues), Alignment.Center) {
                        Text("Stok kosong", color = MaterialTheme.colorScheme.outline)
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize().padding(paddingValues),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(filteredBarang, key = { it.id }) { barang ->
                            StockCard(barang = barang) {
                                navController.navigate("detail/${barang.id}")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StockCard(barang: Barang, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(140.dp)) {
                if (barang.gambarUri.isNotEmpty()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(Uri.parse(barang.gambarUri))
                            .crossfade(true).build(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primaryContainer),
                        Alignment.Center
                    ) {
                        Text(barang.nama.take(1), fontSize = 48.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                }
                
                // Stock Badge
                Surface(
                    modifier = Modifier.padding(12.dp).align(Alignment.TopEnd),
                    shape = RoundedCornerShape(12.dp),
                    color = if (barang.stok <= 5) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary,
                    tonalElevation = 4.dp
                ) {
                    Text(
                        text = "${barang.stok}",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(Modifier.padding(16.dp)) {
                Text(barang.nama, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(barang.kategori, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(8.dp))
                Text(formatRupiah(barang.harga), fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}

fun formatRupiah(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    format.maximumFractionDigits = 0
    return format.format(amount)
}
