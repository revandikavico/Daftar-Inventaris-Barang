package com.example.inventarisbarang.screen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
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

data class Category(val name: String, val icon: ImageVector)

@Composable
fun KategoriScreen(paddingValues: PaddingValues) {
    val context = LocalContext.current
    val categories = remember {
        listOf(
            Category("Gadgets", Icons.Default.Info),
            Category("Fashion", Icons.Default.ShoppingCart),
            Category("Tools", Icons.Default.Build),
            Category("Books", Icons.Default.Edit),
            Category("Health", Icons.Default.Favorite),
            Category("Service", Icons.Default.Settings),
            Category("Living", Icons.Default.Home),
            Category("Hobby", Icons.Default.Star),
            Category("Others", Icons.Default.Menu)
        )
    }

    Box(modifier = Modifier.fillMaxSize().padding(paddingValues).background(MaterialTheme.colorScheme.surface)) {
        Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
            Text(
                text = "Classification",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Organize your inventory effectively",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(categories) { _, category ->
                    val state = remember { MutableTransitionState(false).apply { targetState = true } }
                    AnimatedVisibility(visibleState = state, enter = fadeIn() + scaleIn()) {
                        CategoryItem(category) {
                            Toast.makeText(context, category.name, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryItem(category: Category, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier.aspectRatio(1f).clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    category.icon,
                    null,
                    modifier = Modifier.padding(8.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = category.name,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}
