package com.emenjivar.core.usecase

import com.emenjivar.pomodoro.data.SettingsRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class SetColorUseCaseTest {
    private lateinit var repository: com.emenjivar.pomodoro.data.SettingsRepository
    private lateinit var useCase: com.emenjivar.pomodoro.usecases.SetColorUseCase

    @Before
    fun setup() {
        repository = Mockito.mock(com.emenjivar.pomodoro.data.SettingsRepository::class.java)
        useCase = com.emenjivar.pomodoro.usecases.SetColorUseCase(repository)
    }

    @Test
    fun `setColorUseCase when color param is set should save this value`(): Unit =
        runBlocking {
            var value: Int? = null
            Mockito.`when`(repository.setColor(10)).then {
                value = 10
                it
            }
            useCase.invoke(10)
            Assert.assertEquals(10, value)
        }
}
