package com.emenjivar.core.usecase

import com.emenjivar.core.model.Pomodoro
import com.emenjivar.core.repository.SettingsRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class GetPomodoroUseCaseTest {
    private lateinit var repository: SettingsRepository
    private lateinit var useCase: GetPomodoroUseCase

    @Before
    fun setup() {
        repository = Mockito.mock(SettingsRepository::class.java)
        useCase = GetPomodoroUseCase(repository)
    }

    @Test
    fun `getPomodoroUseCaseTest when repository returns pomodoro instance`(): Unit =
        runBlocking {
            val pomodoro = Pomodoro(0L, 0L)
            Mockito.`when`(repository.getPomodoro()).thenReturn(pomodoro)
            val result = useCase.invoke()
            Assert.assertEquals(pomodoro, result)
        }
}
