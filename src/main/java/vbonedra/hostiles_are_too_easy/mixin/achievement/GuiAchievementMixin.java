package vbonedra.hostiles_are_too_easy.mixin.achievement;

import net.minecraft.Achievement;
import net.minecraft.AchievementList;
import net.minecraft.GuiAchievements;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.lang.reflect.Field;
import static vbonedra.hostiles_are_too_easy.util.AchievementExtend.killWither;

@Mixin(GuiAchievements.class)
public class GuiAchievementMixin {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init_modifyAchievementStates(CallbackInfo ci) {
        try {
            Field parentAchievement = Achievement.class.getDeclaredField("parentAchievement");
            Field displayColumn = Achievement.class.getDeclaredField("displayColumn");
            Field displayRow = Achievement.class.getDeclaredField("displayRow");
            parentAchievement.setAccessible(true);
            displayColumn.setAccessible(true);
            displayRow.setAccessible(true);
            if (AchievementList.theEnd != null && killWither != null) {
                parentAchievement.set(AchievementList.theEnd, killWither);
                displayColumn.setInt(AchievementList.theEnd, 5);
                displayRow.setInt(AchievementList.theEnd, 16);
            }
            if (AchievementList.theEnd2 != null) {
                displayColumn.setInt(AchievementList.theEnd2, 7);
                displayRow.setInt(AchievementList.theEnd2, 16);
            }

        } catch (Exception e) {
            System.err.println("Failed to modify achievement's states: " + e);
        }
    }
}
