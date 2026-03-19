package com.example.presentation.screens.ui.community.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expect.DateTimeManager
import com.example.presentation.screens.ui.community.CommunityGroup
import com.example.presentation.screens.ui.community.CommunityUiState
import com.example.presentation.screens.ui.community.WorkoutPost
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CommunityViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<CommunityUiState>(CommunityUiState.Loading)
    val uiState: StateFlow<CommunityUiState> = _uiState.asStateFlow()

    // ---------------------------------------------------------
    // MOCK DO BACKEND (Simulando o banco de dados do servidor)
    // ---------------------------------------------------------
    private val availableGroups = listOf(
        CommunityGroup(
            id = "grp_001",
            name = "Elite Hipertrofia - Divisão ABCD",
            accessCode = "FITVERSE-ABCD", // <-- O CÓDIGO ÚNICO AQUI
            description = "Foco total em ganho de massa muscular, estruturação de treinos e rotina de suplementação.",
            memberCount = 12
        )
    )

    init {
        checkUserGroupStatus()
    }

    private fun checkUserGroupStatus() {
        viewModelScope.launch {
            delay(800)
            // Inicia a tela pedindo o código
            _uiState.update { CommunityUiState.NotInGroup }
        }
    }

    // Função chamada quando o usuário clica em "Entrar no Grupo"
    fun joinGroup(enteredCode: String) {
        viewModelScope.launch {
            // Coloca a tela em estado de carregamento
            _uiState.update { CommunityUiState.Loading }

            // Simula o tempo de requisição para o servidor Ktor
            delay(1500)

            // Remove espaços e ignora maiúsculas/minúsculas para facilitar para o usuário
            val sanitizedCode = enteredCode.trim().uppercase()

            // Busca no "banco de dados" se o código existe
            val foundGroup = availableGroups.find { it.accessCode == sanitizedCode }

            if (foundGroup != null) {
                // Sucesso! Usuário entrou no grupo. Atualiza o estado com os dados do grupo.
                _uiState.update {
                    CommunityUiState.InGroup(
                        squadName = foundGroup.name,
                        posts = getPostsForGroup(foundGroup.id)
                    )
                }
            } else {
                // Erro: Código não encontrado
                _uiState.update {
                    CommunityUiState.Error("O código '$enteredCode' é inválido ou expirou. Verifique e tente novamente.")
                }
            }
        }
    }

    // Simula a busca do feed de postagens baseado no ID do grupo
    private fun getPostsForGroup(groupId: String): List<WorkoutPost> {
        return if (groupId == "grp_001") {
            listOf(
                WorkoutPost(
                    id = "p1",
                    userName = "Marcos",
                    imageUrl = "",
                    description = "Treino A (Peito e Ombro) finalizado! Pump absurdo hoje. 💪",
                    timestamp = DateTimeManager.nowMillis()
                ),
                WorkoutPost(
                    id = "p2",
                    userName = "Juliana",
                    imageUrl = "",
                    description = "Testando aquele EAA novo intra-treino no dia de braço, o rendimento bateu no teto! 🔥",
                    timestamp = DateTimeManager.nowMillis() // 1 hora atrás
                )
            )
        } else {
            emptyList()
        }
    }

    fun resetToJoin() {
        _uiState.update { CommunityUiState.NotInGroup }
    }
    fun leaveGroup() {
        viewModelScope.launch {
            _uiState.update { CommunityUiState.Loading }
            delay(500) // Simulação de rede
            _uiState.update { CommunityUiState.NotInGroup }
        }
    }
}