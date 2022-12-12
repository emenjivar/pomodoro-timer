package com.emenjivar.core.usecase

import com.emenjivar.pomodoro.data.SettingsRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class SetNighModeUseCaseTest {
    private lateinit var repository: com.emenjivar.pomodoro.data.SettingsRepository
    private lateinit var useCase: com.emenjivar.pomodoro.usecases.SetNighModeUseCase

    @Before
    fun setup() {
        repository = Mockito.mock(com.emenjivar.pomodoro.data.SettingsRepository::class.java)
        useCase = com.emenjivar.pomodoro.usecases.SetNighModeUseCase(repository)
    }

    @Test
    fun `setNighModeUseCase when param is true`(): Unit =
        runBlocking {
            var value: Boolean? = null
            Mockito.`when`(repository.setNightMode(true)).then {
                value = true
                it
            }
            useCase.invoke(true)
            Assert.assertTrue(value ?: false)
        }

    @Test
    fun `setNighModeUseCase when param is false`(): Unit =
        runBlocking {
            var value: Boolean? = null
            Mockito.`when`(repository.setNightMode(false)).then {
                value = false
                it
            }
            useCase.invoke(false)
            Assert.assertFalse(value ?: true)
        }
}
