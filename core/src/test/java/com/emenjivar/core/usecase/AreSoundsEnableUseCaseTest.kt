package com.emenjivar.core.usecase

import com.emenjivar.pomodoro.data.SettingsRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class AreSoundsEnableUseCaseTest {
    private lateinit var repository: com.emenjivar.pomodoro.data.SettingsRepository
    private lateinit var useCase: com.emenjivar.pomodoro.usecases.AreSoundsEnableUseCase

    @Before
    fun setup() {
        repository = Mockito.mock(com.emenjivar.pomodoro.data.SettingsRepository::class.java)
        useCase = com.emenjivar.pomodoro.usecases.AreSoundsEnableUseCase(repository)
    }

    @Test
    fun `areSoundsEnableUseCase when repository returns true`(): Unit =
        runBlocking {
            Mockito.`when`(repository.areSoundsEnabled()).thenReturn(true)
            val result = useCase.invoke()
            assertTrue(result)
        }

    @Test
    fun `areSoundsEnableUseCase when repository returns false`(): Unit =
        runBlocking {
            Mockito.`when`(repository.areSoundsEnabled()).thenReturn(false)
            val result = useCase.invoke()
            assertFalse(result)
        }
}
