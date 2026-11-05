package vbonedra.hostiles_are_too_easy.util;

import net.minecraft.*;
import net.xiaoyu233.fml.reload.utils.IdUtil;
import vbonedra.achievement_lib.util.AchievementRegisterHelper;

public class AchievementExtend {
    // TODO: add achievements for new content, e.g. encounterWitheredSkeleton - when player attacks withered skeleton. issue is, idk where to put those, since vanilla map is already bloated
    public static Achievement spawnWither;
    public static Achievement killWither;
    public static Achievement normalMode;
    public static Achievement hardMode;
    public static Achievement extremeMode;
    public static Achievement legendaryMode;
    public static Achievement endgameMode;

    public static void registerAchievements() {
        spawnWither = new Achievement(
        IdUtil.getNextAchievementID(),
                "spawnWither",
                1, 16,
                Block.slowSand,
                AchievementList.blazeRod
        ).registerAchievement();
        killWither = new Achievement(
                IdUtil.getNextAchievementID(),
                "killWither",
                3, 16,
                Item.netherStar,
                spawnWither
        ).setSpecial().registerAchievement();

        // difficulty_mode
        normalMode = new Achievement(
                IdUtil.getNextAchievementID(),
                "normalMode",
                -6, 0,
                Item.slimeBall,
                null
        ).setIndependent().setSpecial().registerAchievement();
        hardMode = new Achievement(
                IdUtil.getNextAchievementID(),
                "hardMode",
                -6, -2,
                Item.slimeBall,
                normalMode
        ).setSpecial().registerAchievement();
        extremeMode = new Achievement(
                IdUtil.getNextAchievementID(),
                "extremeMode",
                -6, -4,
                Item.slimeBall,
                hardMode
        ).setSpecial().registerAchievement();
        legendaryMode = new Achievement(
                IdUtil.getNextAchievementID(),
                "legendaryMode",
                -6, -6,
                Item.slimeBall,
                extremeMode
        ).setSpecial().registerAchievement();
        endgameMode = new Achievement(
                IdUtil.getNextAchievementID(),
                "endgameMode",
                -6, -8,
                Item.slimeBall,
                legendaryMode
        ).setSpecial().registerAchievement();

        AchievementRegisterHelper.replaceAchievementItemStack(spawnWither, new ItemStack(Item.skull, 1, 1));
        AchievementRegisterHelper.replaceAchievementItemStack(normalMode, new ItemStack(Item.slimeBall, 1, 0));
        AchievementRegisterHelper.replaceAchievementItemStack(hardMode, new ItemStack(Item.slimeBall, 1, 1));
        AchievementRegisterHelper.replaceAchievementItemStack(extremeMode, new ItemStack(Item.slimeBall, 1, 2));
        AchievementRegisterHelper.replaceAchievementItemStack(legendaryMode, new ItemStack(Item.slimeBall, 1, 3));
        AchievementRegisterHelper.replaceAchievementItemStack(endgameMode, new ItemStack(Item.slimeBall, 1, 4));
    }
}
