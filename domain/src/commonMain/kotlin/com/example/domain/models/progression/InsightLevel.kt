package org.fitverse.domain.models.progression

enum class InsightLevel {
    EXCELLENT,  // ≥ 8% de ganho no período
    GOOD,       // ≥ 2% de ganho no período
    NEUTRAL,    // < 2% de ganho no período
    NO_DATA,    // Dados insuficientes (< 2 sessões)
}