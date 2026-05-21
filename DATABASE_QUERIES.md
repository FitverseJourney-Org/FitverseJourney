# Database Queries — SQLDelight Local (AppDatabase)

Todas as queries organizadas por tabela. Acessadas via `database.<table>EntityQueries`.

---

## AchievementEntity

Accessor: `database.achievementEntityQueries`

---

### `selectAllAchievementsByUser`
Retorna todas as conquistas de um usuário, ordenadas por status (UNLOCKED → IN_PROGRESS → LOCKED) e raridade (LEGENDARY → EPIC → RARE → COMMON).

```sql
selectAllAchievementsByUser:
SELECT * FROM AchievementEntity
WHERE userId = ?
ORDER BY
    CASE status WHEN 'UNLOCKED' THEN 0 WHEN 'IN_PROGRESS' THEN 1 ELSE 2 END,
    CASE rarity WHEN 'LEGENDARY' THEN 0 WHEN 'EPIC' THEN 1 WHEN 'RARE' THEN 2 ELSE 3 END;
```

**Parâmetros:** `userId: String`

---

### `selectAchievementsByCategory`
Retorna conquistas de um usuário filtradas por categoria.

```sql
selectAchievementsByCategory:
SELECT * FROM AchievementEntity
WHERE userId = ? AND category = ?
ORDER BY status, rarity;
```

**Parâmetros:** `userId: String`, `category: String`

---

### `selectAchievementById`
Busca uma conquista específica por ID e usuário.

```sql
selectAchievementById:
SELECT * FROM AchievementEntity WHERE id = ? AND userId = ?;
```

**Parâmetros:** `id: String`, `userId: String`

---

### `selectUnlockedAchievements`
Retorna conquistas desbloqueadas, ordenadas da mais recente.

```sql
selectUnlockedAchievements:
SELECT * FROM AchievementEntity
WHERE userId = ? AND status = 'UNLOCKED'
ORDER BY unlockedAt DESC;
```

**Parâmetros:** `userId: String`

---

### `insertAchievement`
Insere ou substitui uma conquista (INSERT OR REPLACE).

```sql
insertAchievement:
INSERT OR REPLACE INTO AchievementEntity(
    id, userId, icon, title, description, xpReward,
    rarity, status, category, progress, maxProgress, condition, unlockedAt
) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
```

**Parâmetros:** `id, userId, icon, title, description, xpReward, rarity, status, category, progress, maxProgress, condition, unlockedAt`

---

### `updateAchievementProgress`
Atualiza o progresso e status de uma conquista.

```sql
updateAchievementProgress:
UPDATE AchievementEntity
SET progress = ?, status = ?
WHERE id = ? AND userId = ?;
```

**Parâmetros:** `progress: Double`, `status: String`, `id: String`, `userId: String`

---

### `unlockAchievement`
Marca conquista como UNLOCKED, progresso 1.0, registra data de desbloqueio.

```sql
unlockAchievement:
UPDATE AchievementEntity
SET status = 'UNLOCKED', progress = 1.0, unlockedAt = ?
WHERE id = ? AND userId = ?;
```

**Parâmetros:** `unlockedAt: String`, `id: String`, `userId: String`

---

### `countUnlockedByUser`
Conta conquistas desbloqueadas de um usuário.

```sql
countUnlockedByUser:
SELECT COUNT(*) FROM AchievementEntity
WHERE userId = ? AND status = 'UNLOCKED';
```

**Parâmetros:** `userId: String`

---

## CatalogMissionEntity

Accessor: `database.catalogMissionEntityQueries`

---

### `selectAllCatalogMissions`
Retorna todas as missões do catálogo ordenadas por ID.

```sql
selectAllCatalogMissions:
SELECT * FROM CatalogMissionEntity ORDER BY id;
```

---

### `countCatalogMissions`
Conta o total de missões no catálogo. Usado para decidir se o seed já foi aplicado.

```sql
countCatalogMissions:
SELECT COUNT(*) FROM CatalogMissionEntity;
```

---

### `insertCatalogMission`
Insere missão no catálogo, ignorando se já existir (INSERT OR IGNORE).

```sql
insertCatalogMission:
INSERT OR IGNORE INTO CatalogMissionEntity(id, title, description, xpReward, type, isSpecial)
VALUES (?, ?, ?, ?, ?, ?);
```

**Parâmetros:** `id, title, description, xpReward, type, isSpecial`

---

## DailyMissionEntity

Accessor: `database.dailyMissionEntityQueries`

---

### `selectMissionsByUserAndDate`
Retorna missões do usuário em uma data específica. Missões CHALLENGE aparecem primeiro.

```sql
selectMissionsByUserAndDate:
SELECT * FROM DailyMissionEntity
WHERE userId = ? AND date = ?
ORDER BY
    CASE type WHEN 'CHALLENGE' THEN 0 ELSE 1 END,
    id;
```

**Parâmetros:** `userId: String`, `date: String` (YYYY-MM-DD)

---

### `selectMissionById`
Busca uma missão pelo ID.

```sql
selectMissionById:
SELECT * FROM DailyMissionEntity WHERE id = ?;
```

**Parâmetros:** `id: String`

---

### `insertMission`
Insere ou substitui uma missão diária.

```sql
insertMission:
INSERT OR REPLACE INTO DailyMissionEntity(
    id, userId, date, title, description, xpReward, type, isCompleted, completedAt
) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);
```

**Parâmetros:** `id, userId, date, title, description, xpReward, type, isCompleted, completedAt`

---

### `completeMission`
Marca uma missão como concluída com timestamp.

```sql
completeMission:
UPDATE DailyMissionEntity
SET isCompleted = 1, completedAt = ?
WHERE id = ? AND userId = ?;
```

**Parâmetros:** `completedAt: Long`, `id: String`, `userId: String`

---

### `resetMissionsForDate`
Reseta todas as missões de um usuário em uma data (isCompleted = 0).

```sql
resetMissionsForDate:
UPDATE DailyMissionEntity
SET isCompleted = 0, completedAt = NULL
WHERE userId = ? AND date = ?;
```

**Parâmetros:** `userId: String`, `date: String`

---

### `deleteMission`
Remove uma missão específica.

```sql
deleteMission:
DELETE FROM DailyMissionEntity WHERE id = ? AND userId = ?;
```

**Parâmetros:** `id: String`, `userId: String`

---

### `deleteMissionsOlderThan`
Remove missões de dias anteriores a uma data (limpeza diária).

```sql
deleteMissionsOlderThan:
DELETE FROM DailyMissionEntity WHERE userId = ? AND date < ?;
```

**Parâmetros:** `userId: String`, `date: String` (YYYY-MM-DD)

---

### `countCompletedByUserAndDate`
Conta missões concluídas de um usuário em uma data.

```sql
countCompletedByUserAndDate:
SELECT COUNT(*) FROM DailyMissionEntity
WHERE userId = ? AND date = ? AND isCompleted = 1;
```

**Parâmetros:** `userId: String`, `date: String`

---

## FoodItemEntity

Accessor: `database.foodItemEntityQueries`

---

### `selectFoodsByMeal`
Retorna todos os alimentos de uma refeição em ordem de inserção.

```sql
selectFoodsByMeal:
SELECT * FROM FoodItemEntity WHERE mealId = ? ORDER BY rowid;
```

**Parâmetros:** `mealId: String`

---

### `selectFoodById`
Busca um alimento pelo ID.

```sql
selectFoodById:
SELECT * FROM FoodItemEntity WHERE id = ?;
```

**Parâmetros:** `id: String`

---

### `insertFood`
Insere ou substitui um alimento.

```sql
insertFood:
INSERT OR REPLACE INTO FoodItemEntity(
    id, mealId, name, portion, unit, kcal, protein, carbs, fat
) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);
```

**Parâmetros:** `id, mealId, name, portion, unit, kcal, protein, carbs, fat`

---

### `updateFood`
Atualiza dados de um alimento existente.

```sql
updateFood:
UPDATE FoodItemEntity
SET name = ?, portion = ?, unit = ?, kcal = ?, protein = ?, carbs = ?, fat = ?
WHERE id = ?;
```

**Parâmetros:** `name, portion, unit, kcal, protein, carbs, fat, id`

---

### `deleteFood`
Remove um alimento pelo ID.

```sql
deleteFood:
DELETE FROM FoodItemEntity WHERE id = ?;
```

**Parâmetros:** `id: String`

---

### `deleteFoodsByMeal`
Remove todos os alimentos de uma refeição.

```sql
deleteFoodsByMeal:
DELETE FROM FoodItemEntity WHERE mealId = ?;
```

**Parâmetros:** `mealId: String`

---

### `deleteFoodsByUserBeforeDate`
Remove alimentos pertencentes a refeições de dias anteriores. Chamado antes de `deleteMealsByUserBeforeDate` para preservar integridade referencial.

```sql
deleteFoodsByUserBeforeDate:
DELETE FROM FoodItemEntity
WHERE mealId IN (SELECT id FROM MealEntryEntity WHERE userId = ? AND date < ?);
```

**Parâmetros:** `userId: String`, `date: String` (YYYY-MM-DD — exclusive lower bound)

---

### `sumMacrosByMeal`
Agrega kcal, proteína, carbs e gordura de todos os alimentos de uma refeição. Usado por `AddFoodToMealUseCase` para recalcular os totais da `MealEntryEntity`.

```sql
sumMacrosByMeal:
SELECT
    COALESCE(SUM(kcal),    0)   AS totalKcal,
    COALESCE(SUM(protein), 0.0) AS totalProtein,
    COALESCE(SUM(carbs),   0.0) AS totalCarbs,
    COALESCE(SUM(fat),     0.0) AS totalFat
FROM FoodItemEntity WHERE mealId = ?;
```

**Parâmetros:** `mealId: String`  
**Retorna:** `MealMacros(totalKcal, totalProtein, totalCarbs, totalFat)`

---

## MealEntryEntity

Accessor: `database.mealEntryEntityQueries`

---

### `selectMealsByUserAndDate`
Retorna refeições do usuário em uma data, ordenadas por hora de log. Observado como Flow em `MealsViewModel`.

```sql
selectMealsByUserAndDate:
SELECT * FROM MealEntryEntity
WHERE userId = ? AND date = ?
ORDER BY loggedAt ASC;
```

**Parâmetros:** `userId: String`, `date: String` (YYYY-MM-DD)

---

### `selectMealById`
Busca uma refeição pelo ID.

```sql
selectMealById:
SELECT * FROM MealEntryEntity WHERE id = ?;
```

**Parâmetros:** `id: String`

---

### `selectMealsByUserAndPeriod`
Retorna refeições de um usuário em um intervalo de datas.

```sql
selectMealsByUserAndPeriod:
SELECT * FROM MealEntryEntity
WHERE userId = ? AND date >= ? AND date <= ?
ORDER BY date DESC, loggedAt DESC;
```

**Parâmetros:** `userId: String`, `from: String`, `to: String`

---

### `insertMeal`
Insere ou substitui uma refeição.

```sql
insertMeal:
INSERT OR REPLACE INTO MealEntryEntity(
    id, userId, date, mealType, name,
    totalKcal, totalProtein, totalCarbs, totalFat, loggedAt
) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
```

**Parâmetros:** `id, userId, date, mealType, name, totalKcal, totalProtein, totalCarbs, totalFat, loggedAt`

---

### `updateMealTotals`
Atualiza os macros totais de uma refeição após insert/update/delete de alimentos.

```sql
updateMealTotals:
UPDATE MealEntryEntity
SET totalKcal = ?, totalProtein = ?, totalCarbs = ?, totalFat = ?
WHERE id = ? AND userId = ?;
```

**Parâmetros:** `totalKcal: Long`, `totalProtein: Double`, `totalCarbs: Double`, `totalFat: Double`, `id: String`, `userId: String`

---

### `deleteMeal`
Remove uma refeição específica.

```sql
deleteMeal:
DELETE FROM MealEntryEntity WHERE id = ? AND userId = ?;
```

**Parâmetros:** `id: String`, `userId: String`

---

### `deleteMealsByUserBeforeDate`
Remove refeições de dias anteriores (limpeza diária). Deve ser chamado após `deleteFoodsByUserBeforeDate`.

```sql
deleteMealsByUserBeforeDate:
DELETE FROM MealEntryEntity WHERE userId = ? AND date < ?;
```

**Parâmetros:** `userId: String`, `date: String` (YYYY-MM-DD)

---

### `sumMacrosByUserAndDate`
Agrega kcal, proteína, carbs e gordura de todas as refeições do usuário em um dia. Usado para exibir o resumo diário.

```sql
sumMacrosByUserAndDate:
SELECT
    COALESCE(SUM(totalKcal),    0)   AS kcal,
    COALESCE(SUM(totalProtein), 0.0) AS protein,
    COALESCE(SUM(totalCarbs),   0.0) AS carbs,
    COALESCE(SUM(totalFat),     0.0) AS fat
FROM MealEntryEntity
WHERE userId = ? AND date = ?;
```

**Parâmetros:** `userId: String`, `date: String`  
**Retorna:** `DailyMacros(kcal, protein, carbs, fat)`

---

## NotificationEntity

Accessor: `database.notificationEntityQueries`

---

### `selectNotificationsByUser`
Retorna todas as notificações do usuário, da mais recente.

```sql
selectNotificationsByUser:
SELECT * FROM NotificationEntity
WHERE userId = ?
ORDER BY createdAt DESC;
```

**Parâmetros:** `userId: String`

---

### `selectUnreadByUser`
Retorna apenas notificações não lidas.

```sql
selectUnreadByUser:
SELECT * FROM NotificationEntity
WHERE userId = ? AND isRead = 0
ORDER BY createdAt DESC;
```

**Parâmetros:** `userId: String`

---

### `selectNotificationById`
Busca uma notificação pelo ID.

```sql
selectNotificationById:
SELECT * FROM NotificationEntity WHERE id = ?;
```

**Parâmetros:** `id: String`

---

### `insertNotification`
Insere ou substitui uma notificação.

```sql
insertNotification:
INSERT OR REPLACE INTO NotificationEntity(
    id, userId, type, title, description, isRead, createdAt
) VALUES (?, ?, ?, ?, ?, ?, ?);
```

**Parâmetros:** `id, userId, type, title, description, isRead, createdAt`

---

### `markAsRead`
Marca uma notificação como lida.

```sql
markAsRead:
UPDATE NotificationEntity
SET isRead = 1
WHERE id = ? AND userId = ?;
```

**Parâmetros:** `id: String`, `userId: String`

---

### `markAllAsRead`
Marca todas as notificações do usuário como lidas.

```sql
markAllAsRead:
UPDATE NotificationEntity
SET isRead = 1
WHERE userId = ?;
```

**Parâmetros:** `userId: String`

---

### `deleteNotification`
Remove uma notificação específica.

```sql
deleteNotification:
DELETE FROM NotificationEntity WHERE id = ? AND userId = ?;
```

**Parâmetros:** `id: String`, `userId: String`

---

### `deleteOldNotifications`
Remove notificações lidas mais antigas que um timestamp. Usado para limpeza periódica.

```sql
deleteOldNotifications:
DELETE FROM NotificationEntity
WHERE userId = ? AND createdAt < ? AND isRead = 1;
```

**Parâmetros:** `userId: String`, `createdAt: Long` (epoch ms)

---

### `countUnreadByUser`
Conta notificações não lidas de um usuário.

```sql
countUnreadByUser:
SELECT COUNT(*) FROM NotificationEntity
WHERE userId = ? AND isRead = 0;
```

**Parâmetros:** `userId: String`

---

## StreakEntity

Accessor: `database.streakEntityQueries`

---

### `selectStreakByUserAndDate`
Busca o registro de streak de um usuário em uma data específica.

```sql
selectStreakByUserAndDate:
SELECT * FROM StreakEntity WHERE userId = ? AND date = ?;
```

**Parâmetros:** `userId: String`, `date: String`

---

### `selectStreakLastDays`
Retorna registros de streak desde uma data, ordenados do mais recente.

```sql
selectStreakLastDays:
SELECT * FROM StreakEntity
WHERE userId = ? AND date >= ?
ORDER BY date DESC;
```

**Parâmetros:** `userId: String`, `date: String`

---

### `selectCurrentStreak`
Retorna o streakCount mais recente do usuário.

```sql
selectCurrentStreak:
SELECT streakCount FROM StreakEntity
WHERE userId = ?
ORDER BY date DESC
LIMIT 1;
```

**Parâmetros:** `userId: String`

---

### `selectStreakWeek`
Retorna os últimos 7 registros de streak (data + check-in) para exibição semanal.

```sql
selectStreakWeek:
SELECT date, isCheckedIn FROM StreakEntity
WHERE userId = ?
ORDER BY date DESC
LIMIT 7;
```

**Parâmetros:** `userId: String`

---

### `upsertStreak`
Insere ou substitui o registro de streak de um dia.

```sql
upsertStreak:
INSERT OR REPLACE INTO StreakEntity(id, userId, date, isCheckedIn, streakCount)
VALUES (?, ?, ?, ?, ?);
```

**Parâmetros:** `id, userId, date, isCheckedIn, streakCount`

---

### `deleteStreaksOlderThan`
Remove registros de streak anteriores a uma data.

```sql
deleteStreaksOlderThan:
DELETE FROM StreakEntity WHERE userId = ? AND date < ?;
```

**Parâmetros:** `userId: String`, `date: String`

---

## UserEntity

Accessor: `database.userEntityQueries`

---

### `selectUserByUid`
Busca o perfil de um usuário pelo UID.

```sql
selectUserByUid:
SELECT * FROM UserEntity WHERE uid = ?;
```

**Parâmetros:** `uid: String`

---

### `insertUser`
Insere ou substitui o perfil completo do usuário.

```sql
insertUser:
INSERT OR REPLACE INTO UserEntity(
    uid, name, email, lastname, username, birthDate,
    gender, classType, weight, height, experienceLevel,
    goals, isPremium, targetWeight, targetCalories,
    targetProtein, targetCarbs, targetFat,
    createdAt, updatedAt
)
VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
```

**Parâmetros:** todos os campos de `UserEntity`

---

### `updateUser`
Atualiza os campos mutáveis do perfil do usuário.

```sql
updateUser:
UPDATE UserEntity SET
    name = ?, email = ?, lastname = ?, username = ?, birthDate = ?,
    gender = ?, classType = ?, weight = ?, height = ?,
    experienceLevel = ?, goals = ?, isPremium = ?,
    targetWeight = ?, targetCalories = ?,
    targetProtein = ?, targetCarbs = ?, targetFat = ?,
    updatedAt = ?
WHERE uid = ?;
```

**Parâmetros:** campos mutáveis + `updatedAt`, `uid`

---

### `deleteUser`
Remove o perfil de um usuário.

```sql
deleteUser:
DELETE FROM UserEntity WHERE uid = ?;
```

**Parâmetros:** `uid: String`

---

### `deleteAllUsers`
Remove todos os usuários (uso em testes / logout total).

```sql
deleteAllUsers:
DELETE FROM UserEntity;
```

---

## UserStatsEntity

Accessor: `database.userStatsEntityQueries`

---

### `selectStatsByUser`
Retorna as estatísticas do usuário.

```sql
selectStatsByUser:
SELECT * FROM UserStatsEntity WHERE userId = ?;
```

**Parâmetros:** `userId: String`

---

### `upsertStats`
Insere ou substitui o registro de stats completo.

```sql
upsertStats:
INSERT OR REPLACE INTO UserStatsEntity(
    userId, currentXp, currentLevel, totalXp,
    stepsToday, stepsGoal, waterGlasses, waterGoal,
    currentStreak, longestStreak, totalWorkouts, totalPRs, updatedAt
) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
```

**Parâmetros:** todos os campos de `UserStatsEntity`

---

### `addXp`
Incrementa XP atual e total do usuário atomicamente.

```sql
addXp:
UPDATE UserStatsEntity
SET currentXp = currentXp + ?, totalXp = totalXp + ?, updatedAt = ?
WHERE userId = ?;
```

**Parâmetros:** `xpAmount: Long`, `xpAmount: Long`, `updatedAt: Long`, `userId: String`

---

### `incrementWater`
Incrementa o contador de copos de água em 1.

```sql
incrementWater:
UPDATE UserStatsEntity
SET waterGlasses = waterGlasses + 1, updatedAt = ?
WHERE userId = ?;
```

**Parâmetros:** `updatedAt: Long`, `userId: String`

---

### `resetDailyStats`
Reseta passos e água para 0 (chamado na virada do dia).

```sql
resetDailyStats:
UPDATE UserStatsEntity
SET stepsToday = 0, waterGlasses = 0, updatedAt = ?
WHERE userId = ?;
```

**Parâmetros:** `updatedAt: Long`, `userId: String`

---

### `updateSteps`
Atualiza o contador de passos do dia.

```sql
updateSteps:
UPDATE UserStatsEntity
SET stepsToday = ?, updatedAt = ?
WHERE userId = ?;
```

**Parâmetros:** `stepsToday: Long`, `updatedAt: Long`, `userId: String`

---

### `incrementWorkouts`
Incrementa o total de treinos concluídos.

```sql
incrementWorkouts:
UPDATE UserStatsEntity
SET totalWorkouts = totalWorkouts + 1, updatedAt = ?
WHERE userId = ?;
```

**Parâmetros:** `updatedAt: Long`, `userId: String`

---

### `incrementPRs`
Incrementa o total de PRs pelo valor informado.

```sql
incrementPRs:
UPDATE UserStatsEntity
SET totalPRs = totalPRs + ?, updatedAt = ?
WHERE userId = ?;
```

**Parâmetros:** `count: Long`, `updatedAt: Long`, `userId: String`

---

### `updateStreak`
Atualiza streak atual e recalcula o maior streak (MAX garante que nunca decresce).

```sql
updateStreak:
UPDATE UserStatsEntity
SET currentStreak = ?,
    longestStreak = MAX(longestStreak, ?),
    updatedAt = ?
WHERE userId = ?;
```

**Parâmetros:** `currentStreak: Long`, `currentStreak: Long`, `updatedAt: Long`, `userId: String`

---

## WorkoutSessionEntity

Accessor: `database.workoutSessionEntityQueries`

---

### `selectSessionsByUser`
Retorna todas as sessões do usuário, da mais recente.

```sql
selectSessionsByUser:
SELECT * FROM WorkoutSessionEntity
WHERE userId = ?
ORDER BY startedAt DESC;
```

**Parâmetros:** `userId: String`

---

### `selectSessionsByUserAndPeriod`
Retorna sessões em um intervalo de timestamps.

```sql
selectSessionsByUserAndPeriod:
SELECT * FROM WorkoutSessionEntity
WHERE userId = ? AND startedAt >= ? AND startedAt <= ?
ORDER BY startedAt DESC;
```

**Parâmetros:** `userId: String`, `from: Long`, `to: Long` (epoch ms)

---

### `selectSessionById`
Busca uma sessão pelo ID.

```sql
selectSessionById:
SELECT * FROM WorkoutSessionEntity WHERE id = ?;
```

**Parâmetros:** `id: String`

---

### `selectRecentSessionsByUser`
Retorna as N sessões mais recentes do usuário.

```sql
selectRecentSessionsByUser:
SELECT * FROM WorkoutSessionEntity
WHERE userId = ?
ORDER BY startedAt DESC
LIMIT ?;
```

**Parâmetros:** `userId: String`, `limit: Long`

---

### `insertSession`
Insere ou substitui uma sessão de treino.

```sql
insertSession:
INSERT OR REPLACE INTO WorkoutSessionEntity(
    id, userId, workoutPlanId, workoutTitle, startedAt, completedAt,
    durationSeconds, totalVolume, totalSets, totalReps,
    xpEarned, hasPR, intensityLevel, muscleGroups, notes
) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
```

**Parâmetros:** todos os campos de `WorkoutSessionEntity`

---

### `completeSession`
Atualiza os dados de conclusão de uma sessão em andamento.

```sql
completeSession:
UPDATE WorkoutSessionEntity
SET completedAt = ?, durationSeconds = ?, totalVolume = ?,
    totalSets = ?, totalReps = ?, xpEarned = ?,
    hasPR = ?, intensityLevel = ?, muscleGroups = ?, notes = ?
WHERE id = ? AND userId = ?;
```

**Parâmetros:** `completedAt, durationSeconds, totalVolume, totalSets, totalReps, xpEarned, hasPR, intensityLevel, muscleGroups, notes, id, userId`

---

### `deleteSession`
Remove uma sessão específica.

```sql
deleteSession:
DELETE FROM WorkoutSessionEntity WHERE id = ? AND userId = ?;
```

**Parâmetros:** `id: String`, `userId: String`

---

### `countSessionsByUser`
Conta sessões concluídas do usuário.

```sql
countSessionsByUser:
SELECT COUNT(*) FROM WorkoutSessionEntity
WHERE userId = ? AND completedAt IS NOT NULL;
```

**Parâmetros:** `userId: String`

---

### `sumVolumeByUser`
Soma o volume total de treino do usuário em todas as sessões concluídas.

```sql
sumVolumeByUser:
SELECT COALESCE(SUM(totalVolume), 0.0) FROM WorkoutSessionEntity
WHERE userId = ? AND completedAt IS NOT NULL;
```

**Parâmetros:** `userId: String`

---

## WorkoutSetEntity

Accessor: `database.workoutSetEntityQueries`

---

### `selectSetsBySession`
Retorna todas as séries de uma sessão, agrupadas por exercício e número.

```sql
selectSetsBySession:
SELECT * FROM WorkoutSetEntity
WHERE sessionId = ?
ORDER BY exerciseName, setNumber;
```

**Parâmetros:** `sessionId: String`

---

### `selectSetsByExercise`
Retorna todas as séries de um exercício em ordem inversa de inserção.

```sql
selectSetsByExercise:
SELECT * FROM WorkoutSetEntity
WHERE exerciseName = ?
ORDER BY rowid DESC;
```

**Parâmetros:** `exerciseName: String`

---

### `selectPRsByExercise`
Retorna o PR (Personal Record) mais pesado de um exercício.

```sql
selectPRsByExercise:
SELECT * FROM WorkoutSetEntity
WHERE exerciseName = ? AND isPR = 1
ORDER BY weight DESC
LIMIT 1;
```

**Parâmetros:** `exerciseName: String`

---

### `selectMaxWeightByExercise`
Retorna o maior peso já levantado em um exercício.

```sql
selectMaxWeightByExercise:
SELECT MAX(weight) FROM WorkoutSetEntity WHERE exerciseName = ?;
```

**Parâmetros:** `exerciseName: String`

---

### `insertSet`
Insere ou substitui uma série.

```sql
insertSet:
INSERT OR REPLACE INTO WorkoutSetEntity(
    id, sessionId, exerciseName, muscleGroup,
    setNumber, reps, weight, isPR, rpe, notes
) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
```

**Parâmetros:** `id, sessionId, exerciseName, muscleGroup, setNumber, reps, weight, isPR, rpe, notes`

---

### `deleteSetsBySession`
Remove todas as séries de uma sessão.

```sql
deleteSetsBySession:
DELETE FROM WorkoutSetEntity WHERE sessionId = ?;
```

**Parâmetros:** `sessionId: String`

---

### `deleteSet`
Remove uma série específica.

```sql
deleteSet:
DELETE FROM WorkoutSetEntity WHERE id = ?;
```

**Parâmetros:** `id: String`

---

### `countPRsInSession`
Conta quantas séries da sessão foram PRs.

```sql
countPRsInSession:
SELECT COUNT(*) FROM WorkoutSetEntity
WHERE sessionId = ? AND isPR = 1;
```

**Parâmetros:** `sessionId: String`
