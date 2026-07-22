package vbonedra.hostiles_are_too_easy.util;

import net.minecraft.*;
import net.xiaoyu233.fml.reload.utils.IdUtil;
import java.lang.reflect.Field;

public class AchievementExtend {
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
                1,
                16,
                Block.slowSand,
                AchievementList.blazeRod
        ).registerAchievement();
        killWither = new Achievement(
                IdUtil.getNextAchievementID(),
                "killWither",
                3,
                16,
                Item.netherStar,
                spawnWither
        ).setSpecial().registerAchievement();

        try {
            Field stackField = Achievement.class.getDeclaredField("theItemStack");
            stackField.setAccessible(true);
            stackField.set(spawnWither, new net.minecraft.ItemStack(net.minecraft.Item.skull, 1, 1));
        } catch (Exception e) {
            System.err.println("Failed to modify subtype on achievement's item: " + e);
        }

        // difficulty_mode
        normalMode = new Achievement(
                IdUtil.getNextAchievementID(),
                "normalMode",
                -6,
                0,
                Item.slimeBall,
                null
        ).setIndependent().setSpecial().registerAchievement();
        hardMode = new Achievement(
                IdUtil.getNextAchievementID(),
                "hardMode",
                -6,
                -2,
                Item.slimeBall,
                normalMode
        ).setSpecial().registerAchievement();
        extremeMode = new Achievement(
                IdUtil.getNextAchievementID(),
                "extremeMode",
                -6,
                -4,
                Item.slimeBall,
                hardMode
        ).setSpecial().registerAchievement();
        legendaryMode = new Achievement(
                IdUtil.getNextAchievementID(),
                "legendaryMode",
                -6,
                -6,
                Item.slimeBall,
                extremeMode
        ).setSpecial().registerAchievement();
        endgameMode = new Achievement(
                IdUtil.getNextAchievementID(),
                "endgameMode",
                -6,
                -8,
                Item.slimeBall,
                legendaryMode
        ).setSpecial().registerAchievement();

        try {
            Field sF = Achievement.class.getDeclaredField("theItemStack");
            sF.setAccessible(true);
            sF.set(normalMode, new ItemStack(Item.slimeBall, 1, 0));
            sF.set(hardMode, new ItemStack(Item.slimeBall, 1, 1));
            sF.set(extremeMode, new ItemStack(Item.slimeBall, 1, 2));
            sF.set(legendaryMode, new ItemStack(Item.slimeBall, 1, 3));
            sF.set(endgameMode, new ItemStack(Item.slimeBall, 1, 4));
        } catch (Exception e) {
            System.err.println("Failed to modify subtype on difficulty items: " + e);
        }
    }
}
