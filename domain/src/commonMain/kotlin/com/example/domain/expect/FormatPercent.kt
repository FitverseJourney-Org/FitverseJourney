package org.fitverse.domain.expect

expect fun Double.formatPercent(): String

// Se você usa Float no seu changePercent, adicione esta também:
expect fun Float.formatPercent(): String