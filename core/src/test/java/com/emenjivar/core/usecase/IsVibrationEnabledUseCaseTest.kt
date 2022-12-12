package com.emenjivar.core.usecase

import com.emenjivar.pomodoro.data.SettingsRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class IsVibrationEnabledUseCaseTest {
    private lateinit var repository: com.emenjivar.pomodoro.data.SettingsRepository
    private lateinit var useCase: com.emenjivar.pomodoro.usecases.IsVibrationEnabledUseCase

    @Before
    fun setup() {
        repository = Mockito.mock(com.emenjivar.pomodoro.data.SettingsRepository::class.java)
        useCase = com.emenjivar.pomodoro.usecases.IsVibrationEnabledUseCase(repository)
    }

    @Test
    fun `isVibrationEnabledUseCase when repository returns true`(): Unit =
        runBlocking {
            Mockito.`when`(repository.isVibrationEnabled()).thenReturn(true)
            val result = useCase.invoke()
            Assert.assertTrue(result)
        }

    @Test
    fun `isVibrationEnabledUseCase when repository returns false`(): Unit =
        runBlocking {
            Mockito.`when`(repository.isVibrationEnabled()).thenReturn(false)
            val result = useCase.invoke()
            Assert.assertFalse(result)
        }
}
