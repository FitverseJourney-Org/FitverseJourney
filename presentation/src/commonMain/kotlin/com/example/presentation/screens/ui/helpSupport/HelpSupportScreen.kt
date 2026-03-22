package com.example.presentation.screens.ui.helpSupport

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.screens.widgets.FitVerseSpacer

data class SupportCategory(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val color: Color
)

data class FAQItem(
    val question: String,
    val answer: String
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun HelpSupportScreen(onBack: () -> Unit) {
    var searchQuery by remember { mutableStateOf("") }

    // Lista de categorias (Cards superiores)
    val categories = listOf(
        SupportCategory(
            "Começando",
            "Aprenda o básico do app",
            Icons.Default.RocketLaunch,
            Color(0xFFFFEBEE)
        ),
        SupportCategory("Segurança", "Proteja sua conta", Icons.Default.Shield, Color(0xFFE8F5E9)),
        SupportCategory(
            "Assinaturas",
            "Planos e pagamentos",
            Icons.Default.CreditCard,
            Color(0xFFE3F2FD)
        )
    )

    // Lista de FAQs
    val faqs = listOf(
        FAQItem(
            "Como editar meu perfil?",
            "Vá em Configurações > Perfil e clique no ícone de lápis."
        ),
        FAQItem("Configurar notificações", "Você pode gerenciar alertas na aba de Preferências."),
        FAQItem(
            "Problemas de login?",
            "Tente redefinir sua senha clicando em 'Esqueci minha senha'."
        ),
        FAQItem("Privacidade de dados", "Saiba como protegemos suas informações em nossa política.")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Centro de Ajuda", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                windowInsets = WindowInsets(0, 0, 0, 0)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // --- CAMPO DE PESQUISA ---
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Pesquisar tópicos de ajuda...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors()
                )
            }

            // --- SEÇÃO: PRIMEIROS PASSOS ---
            item {
                Column {
                    SectionHeader("Primeiros Passos")
                    FitVerseSpacer(vertical = true, value = 12.dp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        categories.forEach { category ->
                            SupportCategoryCard(
                                category = category,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // --- SEÇÃO: TÓPICOS FREQUENTES ---
            item { SectionHeader("Tópicos Frequentes") }

            items(faqs) { faq ->
                FAQListItem(faq)
            }

            // --- FOOTER: AINDA PRECISA DE AJUDA? ---
            item {
                HelpFooter()
            }
        }
    }
}

@Composable
fun SupportCategoryCard(category: SupportCategory, modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier.height(160.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = CircleShape,
                color = category.color,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    category.icon,
                    contentDescription = null,
                    tint = Color.DarkGray,
                    modifier = Modifier.padding(12.dp)
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                category.title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            Text(
                category.subtitle,
                fontSize = 11.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                lineHeight = 14.sp
            )
        }
    }
}

@Composable
fun FAQListItem(faq: FAQItem) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded }) {
        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.HelpOutline,
                null,
                modifier = Modifier.size(20.dp),
                tint = Color.Gray
            )
            Spacer(Modifier.width(16.dp))
            Text(
                faq.question,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge
            )
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null
            )
        }
        AnimatedVisibility(visible = expanded) {
            Text(
                text = faq.answer,
                modifier = Modifier.padding(start = 36.dp, bottom = 16.dp),
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Divider(thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.5f))
    }
}

@Composable
fun HelpFooter() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Ainda precisa de ajuda?", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Button(
            onClick = {
                /**** Abrir Chat ****/
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Fale Conosco")
        }
        OutlinedButton(
            onClick = { /* Abrir Feedback */ },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Enviar Feedback")
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold
    )
}