package com.emenjivar.core.usecase

import com.emenjivar.core.repository.SettingsRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class IsNightModeUseCaseTest {
    private lateinit var repository: SettingsRepository
    private lateinit var useCase: IsNightModeUseCase

    @Before
    fun setup() {
        repository = Mockito.mock(SettingsRepository::class.java)
        useCase = IsNightModeUseCase(repository)
    }

    @Test
    fun `isNightModeUseCase when repository returns true`(): Unit =
        runBlocking {
            Mockito.`when`(repository.isNightMode()).thenReturn(true)
            val result = useCase.invoke()
            assertTrue(result)
        }

    @Test
    fun `isNightModeUseCase when repository returns false`(): Unit =
        runBlocking {
            Mockito.`when`(repository.isNightMode()).thenReturn(false)
            val result = useCase.invoke()
            assertFalse(result)
        }
}
