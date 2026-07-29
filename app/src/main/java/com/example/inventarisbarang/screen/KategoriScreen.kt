package com.example.inventarisbarang.screen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Data class untuk merepresentasikan Kategori Barang.
 */
data class Category(
    val name: String,
    val icon: ImageVector
)

/**
 * KategoriScreen menampilkan daftar kategori barang dalam bentuk grid modern.
 *
 * @param paddingValues Padding dari Scaffold agar konten tidak tertutup TopBar/BottomBar.
 */
@Composable
fun KategoriScreen(paddingValues: PaddingValues) {
    val context = LocalContext.current

    // Daftar kategori statis sesuai permintaan.
    // Menggunakan ikon dari material-icons-core yang tersedia di project.
    val categories = remember {
        listOf(
            Category("Elektronik", Icons.Default.Build),       // Pengganti Devices
            Category("Fashion", Icons.Default.ShoppingCart),    // Pengganti Checkroom
            Category("Aksesoris", Icons.Default.Settings),     // Pengganti Watch
            Category("Alat Tulis", Icons.Default.Edit),         // Tersedia
            Category("Makanan", Icons.Default.Favorite),       // Pengganti Restaurant
            Category("Minuman", Icons.Default.Refresh),        // Pengganti LocalCafe
            Category("Peralatan Rumah", Icons.Default.Home),    // Tersedia
            Category("Olahraga", Icons.Default.Star),          // Pengganti SportsSoccer
            Category("Lainnya", Icons.Default.Menu)             // Pengganti Category
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
    ) {
        // Judul halaman: Kategori Barang
        Text(
            text = "Kategori Barang",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            ),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // Grid Kategori dengan 2 kolom
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(categories) { _, category ->
                // Animasi muncul untuk setiap item
                val visibleState = remember {
                    MutableTransitionState(false).apply {
                        targetState = true
                    }
                }

                AnimatedVisibility(
                    visibleState = visibleState,
                    enter = fadeIn(animationSpec = spring()) + 
                            slideInVertically(initialOffsetY = { it / 2 })
                ) {
                    CategoryCard(category = category) {
                        // Toast saat card diklik
                        Toast.makeText(
                            context,
                            "Kategori: ${category.name}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}

/**
 * Komponen Card untuk setiap item kategori.
 */
@Composable
fun CategoryCard(category: Category, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icon di bagian atas (40.dp)
            Icon(
                imageVector = category.icon,
                contentDescription = category.name,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Nama kategori di bawah icon
            Text(
                text = category.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
