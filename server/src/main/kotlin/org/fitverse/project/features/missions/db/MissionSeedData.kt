package org.fitverse.project.features.missions.db

import org.fitverse.project.features.missions.models.MissionDocument

object MissionSeedData {

    val missions: Map<String, MissionDocument> = mapOf(
        // ── Hydration ─────────────────────────────────────────────────────────
        "mission_hydration_001" to MissionDocument(
            title       = "Primeiro gole do dia",
            description = "Beba um copo de água nos primeiros 10 minutos após acordar. Pequeno hábito, grande impacto.",
            xpReward    = 20,
            type        = "HYDRATION",
            isSpecial   = false,
        ),
        "mission_hydration_002" to MissionDocument(
            title       = "Meta de hidratação",
            description = "Registre o consumo de 2L de água ao longo do dia. Seu corpo agradece a cada gole.",
            xpReward    = 35,
            type        = "HYDRATION",
            isSpecial   = false,
        ),
        "mission_hydration_003" to MissionDocument(
            title       = "Pré-treino hidratado",
            description = "Beba pelo menos 300ml de água antes de iniciar qualquer sessão de treino.",
            xpReward    = 25,
            type        = "HYDRATION",
            isSpecial   = false,
        ),
        // ── Cardio ────────────────────────────────────────────────────────────
        "mission_cardio_001" to MissionDocument(
            title       = "Mova seu corpo hoje",
            description = "Complete uma sessão de treino de qualquer modalidade. Um passo de cada vez.",
            xpReward    = 60,
            type        = "CARDIO",
            isSpecial   = false,
        ),
        "mission_cardio_002" to MissionDocument(
            title       = "30 minutos de foco",
            description = "Treine por pelo menos 30 minutos sem interrupções. Modo foco ativado.",
            xpReward    = 70,
            type        = "CARDIO",
            isSpecial   = false,
        ),
        "mission_cardio_003" to MissionDocument(
            title       = "Força e resistência",
            description = "Registre um treino com exercícios compostos — agachamento, supino ou levantamento terra.",
            xpReward    = 65,
            type        = "CARDIO",
            isSpecial   = false,
        ),
        "mission_cardio_004" to MissionDocument(
            title       = "Mobilidade em dia",
            description = "Dedique 10 minutos ao alongamento ou mobilidade articular. Recuperação também é treino.",
            xpReward    = 30,
            type        = "CARDIO",
            isSpecial   = false,
        ),
        // ── Nutrition ─────────────────────────────────────────────────────────
        "mission_nutrition_001" to MissionDocument(
            title       = "Café da manhã registrado",
            description = "Registre sua primeira refeição do dia. Começar bem é metade do caminho.",
            xpReward    = 30,
            type        = "NUTRITION",
            isSpecial   = false,
        ),
        "mission_nutrition_002" to MissionDocument(
            title       = "Proteína no prato",
            description = "Inclua uma fonte de proteína em pelo menos 2 refeições registradas hoje.",
            xpReward    = 45,
            type        = "NUTRITION",
            isSpecial   = false,
        ),
        "mission_nutrition_003" to MissionDocument(
            title       = "Recuperação nutricional",
            description = "Registre uma refeição com proteína e carboidrato até 1h após o treino.",
            xpReward    = 40,
            type        = "NUTRITION",
            isSpecial   = false,
        ),
        "mission_nutrition_004" to MissionDocument(
            title       = "3 refeições no log",
            description = "Registre o café da manhã, almoço e jantar. Consistência na nutrição é consistência nos resultados.",
            xpReward    = 50,
            type        = "NUTRITION",
            isSpecial   = false,
        ),
        // ── Steps ─────────────────────────────────────────────────────────────
        "mission_steps_001" to MissionDocument(
            title       = "Pausa ativa",
            description = "Levante e caminhe por pelo menos 5 minutos a cada 2h. Seu metabolismo precisa disso.",
            xpReward    = 25,
            type        = "STEPS",
            isSpecial   = false,
        ),
        "mission_steps_002" to MissionDocument(
            title       = "Caminhada da tarde",
            description = "Faça uma caminhada de 15 minutos no período da tarde. Luz solar + movimento = combo vencedor.",
            xpReward    = 35,
            type        = "STEPS",
            isSpecial   = false,
        ),
        "mission_steps_003" to MissionDocument(
            title       = "Rota alternativa",
            description = "Escolha as escadas em vez do elevador e registre a atividade. Pequenas escolhas, grandes resultados.",
            xpReward    = 20,
            type        = "STEPS",
            isSpecial   = false,
        ),
        "mission_steps_004" to MissionDocument(
            title       = "Explorar em movimento",
            description = "Caminhe por um percurso diferente do habitual por pelo menos 20 minutos.",
            xpReward    = 40,
            type        = "STEPS",
            isSpecial   = false,
        ),
        // ── Challenge (isSpecial = true) ──────────────────────────────────────
        "mission_challenge_001" to MissionDocument(
            title       = "Dia duplo",
            description = "Complete 2 sessões de treino distintas no mesmo dia. Apenas para os dedicados.",
            xpReward    = 150,
            type        = "CHALLENGE",
            isSpecial   = true,
        ),
        "mission_challenge_002" to MissionDocument(
            title       = "Novo recorde pessoal",
            description = "Bata um PR em qualquer exercício do seu histórico. Supere quem você era ontem.",
            xpReward    = 200,
            type        = "CHALLENGE",
            isSpecial   = true,
        ),
        "mission_challenge_003" to MissionDocument(
            title       = "Dia de nutrição perfeita",
            description = "Registre 4 refeições balanceadas com pelo menos uma fonte de proteína em cada uma.",
            xpReward    = 175,
            type        = "CHALLENGE",
            isSpecial   = true,
        ),
        "mission_challenge_004" to MissionDocument(
            title       = "Treino de elite",
            description = "Complete uma sessão contínua de no mínimo 60 minutos. Sem pausas, sem desculpas.",
            xpReward    = 120,
            type        = "CHALLENGE",
            isSpecial   = true,
        ),
        "mission_challenge_005" to MissionDocument(
            title       = "Maratona de hidratação",
            description = "Registre 3L de água consumidos em um único dia. Hidratação de atleta.",
            xpReward    = 100,
            type        = "CHALLENGE",
            isSpecial   = true,
        ),
    )
}
