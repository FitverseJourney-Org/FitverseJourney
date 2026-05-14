package com.example.domain.usecase.missions

import com.example.domain.repository.dbLocal.sqldelight.catalog.CatalogMissionRecord

internal object CatalogMissionSeeds {

    val all: List<CatalogMissionRecord> = listOf(
        // ── Hydration ─────────────────────────────────────────────────────────
        CatalogMissionRecord("mission_hydration_001", "Primeiro gole do dia",       "Beba um copo de água nos primeiros 10 minutos após acordar. Pequeno hábito, grande impacto.",                    20,  "HYDRATION", false),
        CatalogMissionRecord("mission_hydration_002", "Meta de hidratação",          "Registre o consumo de 2L de água ao longo do dia. Seu corpo agradece a cada gole.",                              35,  "HYDRATION", false),
        CatalogMissionRecord("mission_hydration_003", "Pré-treino hidratado",        "Beba pelo menos 300ml de água antes de iniciar qualquer sessão de treino.",                                      25,  "HYDRATION", false),
        // ── Cardio ────────────────────────────────────────────────────────────
        CatalogMissionRecord("mission_cardio_001",    "Mova seu corpo hoje",         "Complete uma sessão de treino de qualquer modalidade. Um passo de cada vez.",                                    60,  "CARDIO",    false),
        CatalogMissionRecord("mission_cardio_002",    "30 minutos de foco",          "Treine por pelo menos 30 minutos sem interrupções. Modo foco ativado.",                                          70,  "CARDIO",    false),
        CatalogMissionRecord("mission_cardio_003",    "Força e resistência",         "Registre um treino com exercícios compostos — agachamento, supino ou levantamento terra.",                       65,  "CARDIO",    false),
        CatalogMissionRecord("mission_cardio_004",    "Mobilidade em dia",           "Dedique 10 minutos ao alongamento ou mobilidade articular. Recuperação também é treino.",                        30,  "CARDIO",    false),
        // ── Nutrition ─────────────────────────────────────────────────────────
        CatalogMissionRecord("mission_nutrition_001", "Café da manhã registrado",    "Registre sua primeira refeição do dia. Começar bem é metade do caminho.",                                        30,  "NUTRITION", false),
        CatalogMissionRecord("mission_nutrition_002", "Proteína no prato",           "Inclua uma fonte de proteína em pelo menos 2 refeições registradas hoje.",                                       45,  "NUTRITION", false),
        CatalogMissionRecord("mission_nutrition_003", "Recuperação nutricional",     "Registre uma refeição com proteína e carboidrato até 1h após o treino.",                                         40,  "NUTRITION", false),
        CatalogMissionRecord("mission_nutrition_004", "3 refeições no log",          "Registre o café da manhã, almoço e jantar. Consistência na nutrição é consistência nos resultados.",             50,  "NUTRITION", false),
        // ── Steps ─────────────────────────────────────────────────────────────
        CatalogMissionRecord("mission_steps_001",     "Pausa ativa",                 "Levante e caminhe por pelo menos 5 minutos a cada 2h. Seu metabolismo precisa disso.",                           25,  "STEPS",     false),
        CatalogMissionRecord("mission_steps_002",     "Caminhada da tarde",          "Faça uma caminhada de 15 minutos no período da tarde. Luz solar + movimento = combo vencedor.",                  35,  "STEPS",     false),
        CatalogMissionRecord("mission_steps_003",     "Rota alternativa",            "Escolha as escadas em vez do elevador e registre a atividade. Pequenas escolhas, grandes resultados.",           20,  "STEPS",     false),
        CatalogMissionRecord("mission_steps_004",     "Explorar em movimento",       "Caminhe por um percurso diferente do habitual por pelo menos 20 minutos.",                                       40,  "STEPS",     false),
        // ── Challenge (isSpecial = true) ──────────────────────────────────────
        CatalogMissionRecord("mission_challenge_001", "Dia duplo",                   "Complete 2 sessões de treino distintas no mesmo dia. Apenas para os dedicados.",                                150, "CHALLENGE", true),
        CatalogMissionRecord("mission_challenge_002", "Novo recorde pessoal",        "Bata um PR em qualquer exercício do seu histórico. Supere quem você era ontem.",                                200, "CHALLENGE", true),
        CatalogMissionRecord("mission_challenge_003", "Dia de nutrição perfeita",    "Registre 4 refeições balanceadas com pelo menos uma fonte de proteína em cada uma.",                            175, "CHALLENGE", true),
        CatalogMissionRecord("mission_challenge_004", "Treino de elite",             "Complete uma sessão contínua de no mínimo 60 minutos. Sem pausas, sem desculpas.",                              120, "CHALLENGE", true),
        CatalogMissionRecord("mission_challenge_005", "Maratona de hidratação",      "Registre 3L de água consumidos em um único dia. Hidratação de atleta.",                                         100, "CHALLENGE", true),
    )
}
