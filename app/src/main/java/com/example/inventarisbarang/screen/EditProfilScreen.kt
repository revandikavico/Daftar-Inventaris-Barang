package com.example.inventarisbarang.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

/**
 * EditProfilScreen memungkinkan administrator untuk mengubah informasi profil mereka.
 * Data di sini masih bersifat statis (hardcoded) sesuai instruksi.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfilScreen(navController: NavHostController) {
    val context = LocalContext.current

    // States untuk form input (data awal diambil dari profil statis)
    var nama by remember { mutableStateOf("Alung Kurnia Sandi") }
    var username by remember { mutableStateOf("Alung") }
    var email by remember { mutableStateOf("23083000144@student.unmer.ac.id") }
    var telepon by remember { mutableStateOf("081234567890") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // === BAGIAN FOTO PROFIL ===
            item {
                Box(
                    contentAlignment = Alignment.BottomEnd,
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = null,
                                modifier = Modifier.size(100.dp),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                    
                    // Tombol Ganti Foto (Floating di atas foto)
                    SmallFloatingActionButton(
                        onClick = { 
                            Toast.makeText(context, "Fitur ganti foto belum tersedia", Toast.LENGTH_SHORT).show()
                        },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        shape = CircleShape
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Ganti Foto", modifier = Modifier.size(16.dp))
                    }
                }
            }

            // === FORM INPUT ===
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        EditInfoField(
                            value = nama,
                            onValueChange = { nama = it },
                            label = "Nama Lengkap",
                            icon = Icons.Default.Person
                        )
                        
                        EditInfoField(
                            value = username,
                            onValueChange = { username = it },
                            label = "Username",
                            icon = Icons.Default.Face
                        )
                        
                        EditInfoField(
                            value = email,
                            onValueChange = { email = it },
                            label = "Email",
                            icon = Icons.Default.Email
                        )
                        
                        EditInfoField(
                            value = telepon,
                            onValueChange = { telepon = it },
                            label = "Nomor Telepon",
                            icon = Icons.Default.Phone
                        )
                    }
                }
            }

            // === TOMBOL SIMPAN ===
            item {
                Button(
                    onClick = {
                        // Tampilkan pesan sukses (sesuai permintaan user)
                        Toast.makeText(context, "Perubahan profil berhasil disimpan", Toast.LENGTH_SHORT).show()
                        // Kembali ke halaman sebelumnya (Profil)
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Simpan Perubahan", fontWeight = FontWeight.Bold)
                }
            }
            
            item {
                Text(
                    text = "Catatan: Role dan Divisi hanya dapat diubah oleh Super Admin sistem.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
fun EditInfoField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline
        )
    )
}
