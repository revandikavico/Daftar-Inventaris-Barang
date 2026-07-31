package com.example.inventarisbarang.screen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.inventarisbarang.model.Barang

@Composable
fun ProfilScreen(
    paddingValues: PaddingValues,
    navController: NavHostController,
    daftarBarang: List<Barang>
) {
    val context = LocalContext.current
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { isVisible = true }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(tween(600)) + slideInVertically(initialOffsetY = { 50 }, animationSpec = tween(600))
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().height(200.dp).background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(bottomStart = 48.dp, bottomEnd = 48.dp)
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        modifier = Modifier.size(100.dp).clip(CircleShape),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.padding(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("StockFlow Manager", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text("Inventory Control System", color = MaterialTheme.colorScheme.outline)
                    
                    Spacer(Modifier.height(24.dp))
                    
                    ProfileCard {
                        ProfileInfoItem(Icons.Default.AccountBox, "Full Name", "Admin StockFlow")
                        ProfileInfoItem(Icons.Default.Email, "Email Address", "admin@stockflow.com")
                        ProfileInfoItem(Icons.Default.Phone, "Contact", "0812-0000-0000")
                    }

                    Spacer(Modifier.height(16.dp))

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        StatBox(Modifier.weight(1f), "Items", daftarBarang.size.toString(), Icons.Default.ShoppingCart)
                        StatBox(Modifier.weight(1f), "Groups", "9", Icons.Default.Menu)
                    }

                    Spacer(Modifier.height(32.dp))

                    Button(
                        onClick = { navController.navigate("edit_profil") },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Update Profile Details")
                    }

                    TextButton(
                        onClick = { Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show() },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Sign Out")
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            content()
        }
    }
}

@Composable
fun ProfileInfoItem(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, Modifier.size(24.dp), tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.width(16.dp))
        Column {
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
            Text(value, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun StatBox(modifier: Modifier, label: String, value: String, icon: ImageVector) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
    ) {
        Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.labelSmall)
        }
    }
}
