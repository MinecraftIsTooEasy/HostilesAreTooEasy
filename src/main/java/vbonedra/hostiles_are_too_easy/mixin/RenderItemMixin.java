package vbonedra.hostiles_are_too_easy.mixin;

import net.minecraft.Minecraft;
import net.minecraft.GuiAchievements;
import net.minecraft.RenderItem;
import net.minecraft.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderItem.class)
public class RenderItemMixin {

    @Redirect(
            method = "renderItemAndEffectIntoGUI(Lnet/minecraft/FontRenderer;Lnet/minecraft/TextureManager;Lnet/minecraft/ItemStack;II)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/ItemStack;hasEffect()Z"
            )
    )
    private boolean suppressGlintLogicInGuiAchievements(ItemStack itemStack) {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc != null && mc.currentScreen instanceof GuiAchievements) {
            return false;
        }

        return itemStack.hasEffect();
    }
}
