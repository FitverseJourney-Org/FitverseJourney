package com.example.presentation.screens.ui.community.viewmodel

//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.expect.TimerManager
//import com.example.presentation.screens.ui.community.CommunityGroup
//import com.example.presentation.screens.ui.community.CommunityUiState
//import com.example.presentation.screens.ui.community.WorkoutPost
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.launch
//
//class CommunityViewModel : ViewModel() {
//
//    private val _uiState = MutableStateFlow<CommunityUiState>(CommunityUiState.Loading)
//    val uiState: StateFlow<CommunityUiState> = _uiState.asStateFlow()
//
//    // Simulando o Banco de Dados que agora permite escrita
//    private val availableGroups = mutableListOf(
//        CommunityGroup(
//            id = "grp_001",
//            name = "Elite Hipertrofia - Divisão ABCD",
//            accessCode = "FITVERSE-ABCD",
//            description = "Foco total em ganho de massa muscular.",
//            memberCount = 12
//        )
//    )
//
//    init {
//        checkUserGroupStatus()
//    }
//
//    private fun checkUserGroupStatus() {
//        viewModelScope.launch {
//            delay(800)
//            _uiState.update { CommunityUiState.NotInGroup }
//        }
//    }
//
//    // --- LOGICA DE ENTRAR ---
//    fun joinGroup(enteredCode: String) {
//        viewModelScope.launch {
//            _uiState.update { CommunityUiState.Loading }
//            delay(1200)
//
//            val sanitizedCode = enteredCode.trim().uppercase()
//            val foundGroup = availableGroups.find { it.accessCode == sanitizedCode }
//
//            if (foundGroup != null) {
//                _uiState.update {
//                    CommunityUiState.InGroup(
//                        squadName = foundGroup.name,
//                        posts = getPostsForGroup(foundGroup.id)
//                    )
//                }
//            } else {
//                _uiState.update {
//                    CommunityUiState.Error("O código '$enteredCode' é inválido ou expirou.")
//                }
//            }
//        }
//    }
//
//    // --- NOVA LOGICA DE CRIAR ---
//    fun createGroup(name: String, description: String) {
//        viewModelScope.launch {
//            _uiState.update { CommunityUiState.Loading }
//
//            // Simula o tempo de processamento no servidor (Ktor/Firebase)
//            delay(1500)
//
//            // 1. Gera um código único estilo FIT-1234
//            val generatedCode = "FIT-${(1000..9999).random()}"
//            val newId = "grp_${TimerManager.nowMillis()}"
//
//            // 2. Cria o novo objeto de grupo
//            val newSquad = CommunityGroup(
//                id = newId,
//                name = name,
//                accessCode = generatedCode,
//                description = description,
//                memberCount = 1 // O criador é o primeiro membro
//            )
//
//            // 3. "Salva" no nosso mock de banco de dados
//            availableGroups.add(newSquad)
//
//            // 4. Coloca o usuário direto dentro do novo Squad
//            _uiState.update {
//                CommunityUiState.InGroup(
//                    squadName = newSquad.name,
//                    posts = emptyList() // Squad novo não tem posts ainda
//                )
//            }
//
//            // TODO: No futuro, disparar um evento para mostrar o código gerado ao usuário
//            println("Squad Criado com Sucesso! Código: $generatedCode")
//        }
//    }
//
//    private fun getPostsForGroup(groupId: String): List<WorkoutPost> {
//        return when (groupId) {
//            "grp_001" -> listOf(
//                WorkoutPost("p1", "Marcos", "", "Treino A (Peito e Ombro) finalizado! 💪", TimerManager.nowMillis()),
//                WorkoutPost("p2", "Juliana", "", "EAA novo aprovado! 🔥", TimerManager.nowMillis() - 3600000)
//            )
//            else -> emptyList() // Grupos novos ou outros IDs começam vazios
//        }
//    }
//
//    fun resetToJoin() {
//        _uiState.update { CommunityUiState.NotInGroup }
//    }
//
//    fun leaveGroup() {
//        viewModelScope.launch {
//            _uiState.update { CommunityUiState.Loading }
//            delay(500)
//            _uiState.update { CommunityUiState.NotInGroup }
//        }
//    }
//}