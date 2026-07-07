package com.example.idletest.backend.service

import com.example.idletest.backend.dto.AbilityProgressDto
import com.example.idletest.backend.dto.AchievementProgressDto
import com.example.idletest.backend.dto.GameProgressDto
import com.example.idletest.backend.dto.UpgradeProgressDto
import com.example.idletest.backend.model.GameProgress
import com.example.idletest.backend.repository.GameProgressRepository
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service

@Service
class GameProgressService(
    private val gameProgressRepository: GameProgressRepository,
    private val objectMapper: ObjectMapper
) {

    fun getProgress(userId: Long): GameProgressDto {
        val progress = gameProgressRepository.findByUserId(userId)
            ?: gameProgressRepository.save(GameProgress(userId = userId))

        return progress.toDto()
    }

    fun saveProgress(userId: Long, dto: GameProgressDto): GameProgressDto {
        val progress = gameProgressRepository.findByUserId(userId)
            ?: GameProgress(userId = userId)

        progress.userId = userId

        progress.energy = dto.energy
        progress.globalEnergy = dto.globalEnergy
        progress.currentWave = dto.currentWave

        progress.coreHp = dto.coreHp
        progress.coreMaxHp = dto.coreMaxHp

        progress.towerDamage = dto.towerDamage
        progress.towerAttackSpeed = dto.towerAttackSpeed
        progress.towerRange = dto.towerRange

        progress.upgradesJson = writeJson(dto.upgrades)
        progress.achievementsJson = writeJson(dto.achievements)
        progress.abilitiesJson = writeJson(dto.abilities)

        return gameProgressRepository.save(progress).toDto()
    }

    private fun GameProgress.toDto(): GameProgressDto {
        return GameProgressDto(
            id = id,
            userId = userId,

            energy = energy,
            globalEnergy = globalEnergy,

            currentWave = currentWave,

            coreHp = coreHp,
            coreMaxHp = coreMaxHp,

            towerDamage = towerDamage,
            towerAttackSpeed = towerAttackSpeed,
            towerRange = towerRange,

            upgrades = readUpgrades(upgradesJson),
            achievements = readAchievements(achievementsJson),
            abilities = readAbilities(abilitiesJson)
        )
    }

    private fun writeJson(value: Any): String {
        return try {
            objectMapper.writeValueAsString(value)
        } catch (exception: Exception) {
            "[]"
        }
    }

    private fun readUpgrades(json: String?): List<UpgradeProgressDto> {
        return try {
            if (json.isNullOrBlank()) {
                emptyList()
            } else {
                objectMapper.readValue(
                    json,
                    object : TypeReference<List<UpgradeProgressDto>>() {}
                )
            }
        } catch (exception: Exception) {
            emptyList()
        }
    }

    private fun readAchievements(json: String?): List<AchievementProgressDto> {
        return try {
            if (json.isNullOrBlank()) {
                emptyList()
            } else {
                objectMapper.readValue(
                    json,
                    object : TypeReference<List<AchievementProgressDto>>() {}
                )
            }
        } catch (exception: Exception) {
            emptyList()
        }
    }

    private fun readAbilities(json: String?): List<AbilityProgressDto> {
        return try {
            if (json.isNullOrBlank()) {
                emptyList()
            } else {
                objectMapper.readValue(
                    json,
                    object : TypeReference<List<AbilityProgressDto>>() {}
                )
            }
        } catch (exception: Exception) {
            emptyList()
        }
    }
}