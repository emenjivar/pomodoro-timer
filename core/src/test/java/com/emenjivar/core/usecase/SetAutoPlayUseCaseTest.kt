package com.emenjivar.core.usecase

import com.emenjivar.core.repository.SettingsRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class SetAutoPlayUseCaseTest {
    private lateinit var repository: SettingsRepository
    private lateinit var useCase: SetAutoPlayUseCase

    @Before
    fun setup() {
        repository = Mockito.mock(SettingsRepository::class.java)
        useCase = SetAutoPlayUseCase(repository)
    }

    @Test
    fun `setAutoPlayUseCase when autoPlay param is true`(): Unit =
        runBlocking {
            var value: Boolean? = null
            Mockito.`when`(repository.setAutoPlay(true)).then {
                value = true
                it
            }
            useCase.invoke(true)
            assertTrue(value ?: false)
        }

    @Test
    fun `setAutoPlayUseCase when autoPlay param is false`(): Unit =
        runBlocking {
            var value: Boolean? = null
            Mockito.`when`(repository.setAutoPlay(false)).then {
                value = false
                it
            }
            useCase.invoke(false)
            assertFalse(value ?: true)
        }
}
