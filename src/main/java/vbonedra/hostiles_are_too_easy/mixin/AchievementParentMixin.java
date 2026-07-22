package vbonedra.hostiles_are_too_easy.mixin;

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
public class AchievementParentMixin {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onGuiInit(CallbackInfo ci) {
        try {
            Field parent = Achievement.class.getDeclaredField("parentAchievement");
            Field column = Achievement.class.getDeclaredField("displayColumn");
            Field row = Achievement.class.getDeclaredField("displayRow");
            parent.setAccessible(true);
            column.setAccessible(true);
            row.setAccessible(true);
            if (AchievementList.theEnd != null && killWither != null) {
                parent.set(AchievementList.theEnd, killWither);
                column.setInt(AchievementList.theEnd, 5);
                row.setInt(AchievementList.theEnd, 16);
            }
            if (AchievementList.theEnd2 != null) {
                column.setInt(AchievementList.theEnd2, 7);
                row.setInt(AchievementList.theEnd2, 16);
            }

        } catch (Exception e) {
            System.err.println("Failed to modify achievement's states: " + e);
        }
    }
}
