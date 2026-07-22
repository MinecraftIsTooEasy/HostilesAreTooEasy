package vbonedra.hostiles_are_too_easy.event.listener;

import moddedmite.rustedironcore.api.event.listener.IAchievementListener;
import net.minecraft.*;
import vbonedra.hostiles_are_too_easy.util.AchievementExtend;

public class AchievementListener implements IAchievementListener {
    @Override
    public void onItemCrafted(EntityPlayer player, ItemStack par1ItemStack) {
    }

    @Override
    public void onItemSmelt(EntityPlayer player, ItemStack itemStack) {
    }

    @Override
    public void onItemPickUp(EntityPlayer player, ItemStack itemStack) {
    }

    @Override
    public void onDimensionTravel(EntityPlayer player, int currentDimension, int destinationDimension) {
        if (destinationDimension == -2) {
            if (AchievementExtend.hardMode != null) {
                player.addStat(AchievementExtend.hardMode, 1);
            }
        } else if (destinationDimension == -1) {
            if (AchievementExtend.extremeMode != null) {
                player.addStat(AchievementExtend.extremeMode, 1);
            }
        }
    }
}
