package vbonedra.hostiles_are_too_easy.difficulty_mode;

import net.minecraft.World;

import static vbonedra.hostiles_are_too_easy.util.AchievementExtend.*;

public class DifficultyMode {

    public static int get_difficulty_level(World worldObj) {
        int difficulty_level = 0;
        if (worldObj.getWorldInfo().hasAchievementUnlocked(hardMode)) difficulty_level = 1;
        if (worldObj.getWorldInfo().hasAchievementUnlocked(extremeMode)) difficulty_level = 2;
        if (worldObj.getWorldInfo().hasAchievementUnlocked(legendaryMode)) difficulty_level = 3;
        if (worldObj.getWorldInfo().hasAchievementUnlocked(endgameMode)) difficulty_level = 4;
        return  difficulty_level;
    }

}
