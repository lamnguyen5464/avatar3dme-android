package com.lamnguyen5464.avatar3dme.feature

interface CommonUseCase {
    fun execute()

    companion object {
        fun of(vararg useCases: CommonUseCase): CommonUseCase = object : CommonUseCase {
            override fun execute() {
                for (useCase in useCases) {
                    useCase.execute()
                }
            }
        }
    }
}