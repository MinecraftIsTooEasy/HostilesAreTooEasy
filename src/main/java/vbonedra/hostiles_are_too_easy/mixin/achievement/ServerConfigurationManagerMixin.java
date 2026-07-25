package vbonedra.hostiles_are_too_easy.mixin.achievement;

import net.minecraft.ServerPlayer;
import net.minecraft.ServerConfigurationManager;
import vbonedra.hostiles_are_too_easy.util.AchievementExtend;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerConfigurationManager.class)
public class ServerConfigurationManagerMixin {

    @Inject(method = "playerLoggedIn(Lnet/minecraft/ServerPlayer;)V", at = @At("RETURN"), remap = false)
    private void playerLoggedIn_grantAchievement(ServerPlayer player, CallbackInfo ci) {
        if (player != null && AchievementExtend.normalMode != null) {
            player.addStat(AchievementExtend.normalMode, 1);
        }
    }
}
