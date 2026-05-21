package org.fitverse.domain.usecase

import app.cash.turbine.test
import org.fitverse.domain.fakes.FakeAppLanguageRepository
import org.fitverse.domain.usecase.db.datastore.language.ChangeAppLanguageUseCase
import org.fitverse.domain.usecase.db.datastore.language.GetAppLanguageUseCase
import org.fitverse.domain.usecase.db.datastore.language.GetCurrentLanguageUseCase
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetAppLanguageUseCaseTest {

    private val repo = FakeAppLanguageRepository(initialCode = "pt")
    private val getLanguage = GetAppLanguageUseCase(repo)
    private val getCurrentLanguage = GetCurrentLanguageUseCase(repo)
    private val changeLanguage = ChangeAppLanguageUseCase(repo)

    @Test
    fun `invoke emite o idioma inicial`() = runTest {
        getLanguage().test {
            assertEquals("pt", awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `invoke emite novo idioma apos mudanca`() = runTest {
        getLanguage().test {
            assertEquals("pt", awaitItem())
            changeLanguage("en")
            assertEquals("en", awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getCurrentLanguage retorna valor atual sem flow`() = runTest {
        assertEquals("pt", getCurrentLanguage())
    }

    @Test
    fun `changeLanguage persiste novo codigo`() = runTest {
        changeLanguage("de")
        assertEquals("de", getCurrentLanguage())
    }
}
