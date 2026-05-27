package org.fitverse.presentation.theme

import androidx.compose.ui.graphics.Color

val transparent = Color.Transparent

// =============================================================================
//  FitColors — fonte canônica de todas as cores do FitverseJourney
//
//  Organizado em blocos semânticos. Cada token documenta:
//    • Papel visual que cumpre no design system
//    • Features / telas onde é utilizado
//
//  HIERARQUIA DE SUPERFÍCIES (do mais fundo ao mais elevado):
//    Bg → Surface → Surface2 → Surface3 → SurfaceModal
//
//  REGRA DE USO:
//    Código novo → sempre referenciar FitColors.*
//    Código legado → usa os delegates abaixo (zero breaking change)
// =============================================================================
object FitColors {

    // =========================================================================
    // BACKGROUNDS & SURFACES
    // =========================================================================

    /** Fundo principal do app — OLED-friendly near-black com tint azul.
     *  Usado em: scaffold background de TODAS as telas. */
    val Bg            = Color(0xFF0A0A0F)

    /** Superfície primária — cards principais, bottom sheets, nav bars.
     *  Usado em: DashboardScreen, WorkoutScreen, AuthScreens, NotificationScreen. */
    val Surface       = Color(0xFF111118)

    /** Superfície secundária — containers internos, listas dentro de cards.
     *  Usado em: MealsScreen, WikiScreen, ProfileScreen, TasksScreen. */
    val Surface2      = Color(0xFF16161E)

    /** Superfície terciária — itens de lista elevados, chips, tag backgrounds.
     *  Usado em: LeaderboardScreen, HistoricScreen, rest-day circles (WorkoutPlan). */
    val Surface3      = Color(0xFF1E1E28)

    /** Superfície de modal — dialogs de confirmação, alert sheets.
     *  Usado em: DeleteDialog (WorkoutPlan), modais genéricos, reward sheets. */
    val SurfaceModal  = Color(0xFF1C1C26)

    // =========================================================================
    // BORDERS & DIVIDERS
    // =========================================================================

    /** Borda sutíl para cards gerais (7% white alpha).
     *  Usado em: WikiScreen cards, MealsScreen cards, AchievementsScreen cards. */
    val Border        = Color(0x12FFFFFF)

    /** Borda visível para inputs e estados selecionados (12% white alpha).
     *  Usado em: TextFields, chips selecionados, itens ativos de listas. */
    val Border2       = Color(0x1FFFFFFF)

    /** Contorno sólido — divisores de seção, separadores e bordas de inputs.
     *  Usado em: AuthScreen (inputs), separadores de listas, WorkoutPlan (inativo). */
    val Outline       = Color(0xFF2A2A35)

    // =========================================================================
    // PRIMARY BRAND — Neon Lime  #C8FF00
    // =========================================================================

    /** Cor primária da marca — Neon Lime.
     *  Usado em: CTAs principais, FABs, ícone ativo da bottom nav, logo,
     *            barras de progresso XP, day-training circles (WorkoutPlan),
     *            cursor de busca (Wiki), streaks ativos, tags Força (Wiki). */
    val Accent        = Color(0xFFC8FF00)

    /** Container de Accent — fundo de chips/tags/ghost buttons (12% alpha).
     *  Usado em: notificação CONQUISTA background, AccentDim em chip selecionado. */
    val AccentDim     = Color(0x1FC8FF00)

    /** Tint mínimo de Accent (6% alpha) — hover/focus sutil.
     *  Usado em: fundo de card hover com accent, estado de foco em inputs. */
    val AccentDim2    = Color(0x0FC8FF00)

    /** Container escuro para primaryContainer do MaterialTheme.
     *  Usado em: MaterialTheme.colorScheme.primaryContainer (Theme.kt),
     *            containers de seleção, hover states, Quick Commands bg. */
    val AccentContainer = Color(0xFF1A2600)

    // =========================================================================
    // SECONDARY BRAND — Neon Aqua  #00FFB2
    // =========================================================================

    /** Cor secundária da marca — Neon Aqua.
     *  Usado em: Trial plan badge, barra de hidratação, indicadores de stamina,
     *            subtítulos do plano (WorkoutPlanScreen), streak reward state. */
    val Secondary     = Color(0xFF00FFB2)

    // =========================================================================
    // GREEN — Success / Active / Training  #00E87A
    // =========================================================================

    /** Verde semântico — sucesso, ativo, treino concluído.
     *  Usado em: missões concluídas (Dashboard), atividade Caminhada (Activity/Workout),
     *            tag Treino (Wiki), notificação TREINO, plano ativo (WorkoutPlan). */
    val Green         = Color(0xFF00E87A)
    val GreenDim      = Color(0x1F00E87A)

    val GreenDark = Color(0xFF181A17)

    val GreenSemiDark = Color(0xFF252B15)

    // =========================================================================
    // ORANGE — Warning / Running / Cardio / Streak  #FF6B35
    // =========================================================================

    /** Laranja semântico — alerta, corrida, cardio, streak em risco, energia.
     *  Usado em: atividade Corrida (Workout/Activity), tag Cardio (Wiki),
     *            notificação STREAK, Energy button background (DashboardHeader). */
    val Orange        = Color(0xFFFF6B35)
    val OrangeDim     = Color(0x26FF6B35)

    // =========================================================================
    // PURPLE — AI / Nutrition / Macro Fat  #9D6FFF
    // =========================================================================

    /** Roxo semântico — IA generativa, nutrição, macro gordura, bicicleta.
     *  Usado em: AI plan badge (WorkoutPlan), tag Nutrição (Wiki),
     *            notificação XP, macro fat (Meals), atividade Bike. */
    val Purple        = Color(0xFF9D6FFF)
    val PurpleDim     = Color(0x269D6FFF)

    // =========================================================================
    // BLUE — Info / Comments / Macro Carbs  #3B82F6
    // =========================================================================

    /** Azul semântico — informação, comentários, macro carboidrato.
     *  Usado em: notificação COMENTÁRIO, macro carbs (Meals),
     *            atividade Bike (indicador secundário), itens informativos. */
    val Blue          = Color(0xFF3B82F6)
    val BlueDim       = Color(0x1F3B82F6)

    val DarkBlue = Color(0xFF090C6F)

    // =========================================================================
    // RED — Danger / Error / Recovery  #FF4757
    // =========================================================================

    /** Vermelho semântico — perigo, exclusão, erro, recovery.
     *  Usado em: botão delete (WorkoutPlan dialog), tag Recovery (Wiki),
     *            estados de erro em formulários, dialogs destrutivos. */
    val Red           = Color(0xFFFF4757)
    val RedDim        = Color(0x1FFF4757)

    // =========================================================================
    // TEAL — Challenges / Social  #00D4B4
    // =========================================================================

    /** Teal semântico — desafios, conquistas sociais.
     *  Usado em: notificação DESAFIO (NotificationsScreen),
     *            indicadores de challenge em progresso. */
    val Teal          = Color(0xFF00D4B4)
    val TealDim       = Color(0x2600D4B4)

    // =========================================================================
    // AMBER — Ranking / Premium / Energy  #FFB830
    // =========================================================================

    /** Âmbar semântico — ranking, premium, energia, conquistas especiais.
     *  Usado em: notificação RANKING, highlights do LeaderboardScreen,
     *            Energy accent do DashboardHeader (ícone raio). */
    val Amber         = Color(0xFFFFB830)
    val AmberDim      = Color(0x26FFB830)

    // =========================================================================
    // GRAY — System / Neutral / Inactive  #8890A4
    // =========================================================================

    /** Cinza semântico — sistema, inativo, neutro, rest days.
     *  Usado em: notificação SISTEMA, ícones inativos da bottom nav,
     *            rest-day icon (WorkoutPlan), estados desabilitados. */
    val Gray          = Color(0xFF8890A4)
    val GrayDim       = Color(0x1F8890A4)

    // =========================================================================
    // TEXT HIERARCHY
    // =========================================================================

    /** Texto primário — headlines, títulos, rótulos principais.
     *  Usado em: todas as telas como cor de texto dominante. */
    val TextPrimary   = Color(0xFFF0F0F8)

    /** Texto secundário — captions, meta-info, unidades (BPM, kg, kcal).
     *  Usado em: subtítulos de cards, timestamps, placeholders preenchidos. */
    val TextMuted     = Color(0xFF888899)

    /** Texto terciário — placeholders vazios, itens bloqueados, ghost labels.
     *  Usado em: inputs vazios, conquistas bloqueadas, estados ghost. */
    val TextDisabled  = Color(0xFF555566)

    // =========================================================================
    // GAMIFICATION — XP / Level  #7C6FFF
    // =========================================================================

    /** Violeta de XP — barra de experiência, nível do jogador.
     *  Usado em: ContainerLevel (Dashboard XP bar), barra de progressão. */
    val Xp            = Color(0xFF7C6FFF)
    val XpDim         = Color(0x267C6FFF)

    /** Superfície escura para cards de tema IA / XP (purple AI card bg).
     *  Usado em: WorkoutPlan AI card background, containers de IA. */
    val XpSurface     = Color(0xFF191534)
    val XpBorder      = Color(0xFF3D3580)

    val PurpleDark = Color(0xFF242140)

    val bgIconIa = Color(0xFF28264A)

    // =========================================================================
    // RANKING TIERS — Gold / Silver / Bronze / Platinum
    // Usado em: PlayerProfileCard (Dashboard), LeaderboardScreen
    // =========================================================================

    val Gold            = Color(0xFFFFD700)
    val GoldSurface     = Color(0xFF1E1800)
    val GoldBorder      = Color(0xFF7A6200)

    val Silver          = Color(0xFFC0C0D0)
    val SilverSurface   = Color(0xFF222235)
    val SilverBorder    = Color(0xFF6655BB)

    val Bronze          = Color(0xFFCD7F32)
    val BronzeSurface   = Color(0xFF1E1510)
    val BronzeBorder    = Color(0xFF8B5A2B)

    /** Platinum — tier máximo, destaque ciano.
     *  Usado em: PlayerProfileCard (Dashboard) rank platinum. */
    val Platinum        = Color(0xFF00E8E8)
    val PlatinumSurface = Color(0xFF0A2020)
    val PlatinumBorder  = Color(0xFF00D4D4)

    // =========================================================================
    // MACROS — Nutritional indicators  (MealsScreen)
    // =========================================================================

    /** Indicadores de macronutrientes na tela de Refeições.
     *  Proteína → Lime, Carboidrato → Blue, Gordura → Purple. */
    val MacroProtein  = Accent
    val MacroCarbs    = Blue
    val MacroFat      = Purple

    // =========================================================================
    // WIKI CONTENT TAGS
    // Usado em: chips de categoria nos cards da WikiScreen
    // =========================================================================

    val TagForca      = Accent
    val TagTreino     = Green
    val TagNutricao   = Purple
    val TagRecovery   = Red
    val TagCardio     = Orange

    // =========================================================================
    // ACTIVITY TYPES
    // Usado em: WorkoutScreen, ActivityScreen, WorkoutSessionScreen
    // =========================================================================

    val ActivityRunning = Orange
    val ActivityWalking = Green
    val ActivityBike    = Blue
    val ActivityOther   = Purple

    // =========================================================================
    // ACHIEVEMENT RARITY
    // Usado em: AchievementsScreen — brilho, borda e badge de raridade
    // =========================================================================

    val RarityCommon    = Accent   // COMMON  → Lime
    val RarityRare      = Xp       // RARE    → Viola
    val RarityLegendary = Red      // LEGENDARY → Red/Fire

    // =========================================================================
    // NOTIFICATION TYPES
    // Usado em: NotificationsScreen — fundo do ícone (Dim) + dot indicator
    // =========================================================================

    val NotifXp              = Purple
    val NotifXpDim           = PurpleDim

    val NotifStreak          = Orange
    val NotifStreakDim       = OrangeDim

    val NotifLike            = Red
    val NotifLikeDim         = RedDim

    val NotifComment         = Blue
    val NotifCommentDim      = BlueDim

    val NotifWorkout         = Green
    val NotifWorkoutDim      = GreenDim

    val NotifAchievement     = Accent
    val NotifAchievementDim  = AccentDim

    val NotifChallenge       = Teal
    val NotifChallengeDim    = TealDim

    val NotifRanking         = Amber
    val NotifRankingDim      = AmberDim

    val NotifSystem          = Gray
    val NotifSystemDim       = GrayDim

    // =========================================================================
    // OVERLAYS & SCRIM
    // =========================================================================

    /** Overlay escuro para modais e bottom sheets (80% black).
     *  Usado em: WorkoutPlan dialog scrim, bottom sheet backdrop. */
    val Scrim         = Color(0xCC000000)
}
