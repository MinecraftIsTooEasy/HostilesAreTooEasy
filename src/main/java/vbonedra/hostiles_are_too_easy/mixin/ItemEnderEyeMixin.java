package vbonedra.hostiles_are_too_easy.mixin;

import net.minecraft.EntityPlayer;
import net.minecraft.ItemEnderEye;
import net.minecraft.StatCollector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vbonedra.hostiles_are_too_easy.util.AchievementExtend;

@Mixin(ItemEnderEye.class)
public class ItemEnderEyeMixin {
    // TODO?: it doesn't need to be complex, but mayne it's better to make delay player-specific, so 2 players clicking frame won't interrupt each other, though who cares?

    @Unique private int lastSoundTick = 0;

    @Inject(method = "onItemRightClick(Lnet/minecraft/EntityPlayer;FZ)Z", at = @At("HEAD"), cancellable = true)
    private void restrictEyePlacement(EntityPlayer player, float partial_tick, boolean ctrl_is_down, CallbackInfoReturnable<Boolean> cir) {
        if (!player.worldObj.getWorldInfo().hasAchievementUnlocked(AchievementExtend.killWither)) {
            net.minecraft.RaycastCollision rc = player.getSelectedObject(partial_tick, false);

            if (rc != null && rc.isBlock() && rc.getBlockHit() == net.minecraft.Block.endPortalFrame && !net.minecraft.BlockEndPortalFrame.isEnderEyeInserted(rc.block_hit_metadata)) {
                if (player.worldObj.isRemote) {
                    this.displayActionBarMessage(StatCollector.translateToLocal("notification.end_frame_block"));

                    net.minecraft.Minecraft mc = net.minecraft.Minecraft.getMinecraft();
                    if (mc.ingameGUI != null) {
                        int currentTick = mc.ingameGUI.getUpdateCounter();
                        if (currentTick - this.lastSoundTick >= 60 || currentTick < this.lastSoundTick) {
                            this.lastSoundTick = currentTick;
                            this.playClientWitherSound(mc);
                        }
                    }
                }
                cir.setReturnValue(false);
            }
        }
    }

    @Unique private void displayActionBarMessage(String text) {
        net.minecraft.Minecraft mc = net.minecraft.Minecraft.getMinecraft();
        if (mc.ingameGUI != null) {
            mc.ingameGUI.func_110326_a(text, false);
        }
    }

    @Unique private void playClientWitherSound(net.minecraft.Minecraft mc) {
        if (mc.sndManager != null) {
            float pitch = (mc.theWorld.rand.nextFloat() - mc.theWorld.rand.nextFloat()) * 0.2F + 1.0F;
            mc.sndManager.playSoundFX("mob.wither.idle", 1.0F, pitch);
        }
    }
}
