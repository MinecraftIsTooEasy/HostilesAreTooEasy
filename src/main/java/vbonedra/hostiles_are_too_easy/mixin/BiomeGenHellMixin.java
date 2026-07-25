package vbonedra.hostiles_are_too_easy.mixin;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BiomeGenHell.class)
public class BiomeGenHellMixin extends BiomeGenBase {
    protected BiomeGenHellMixin(int par1) {
        super(par1);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(CallbackInfo callbackInfo) {
        boolean hellhoundWasAdded = false;
        boolean demonSpiderWasAdded = false;
        boolean infernalCreeperWasAdded = false;

        for (Object obj : this.spawnableMonsterList) {
            if (obj instanceof SpawnListEntry) {
                Class entityClass = ((SpawnListEntry) obj).entityClass;
                if (entityClass == EntityHellhound.class) hellhoundWasAdded = true;
                else if (entityClass == EntityDemonSpider.class) demonSpiderWasAdded = true;
                else if (entityClass == EntityInfernalCreeper.class) infernalCreeperWasAdded = true;
            }
        }

        if (!hellhoundWasAdded) this.spawnableMonsterList.add(new SpawnListEntry(EntityHellhound.class, 10, 1, 2));
        if (!demonSpiderWasAdded) this.spawnableMonsterList.add(new SpawnListEntry(EntityDemonSpider.class, 10, 1, 4));
        if (!infernalCreeperWasAdded) this.spawnableMonsterList.add(new SpawnListEntry(EntityInfernalCreeper.class, 10, 1, 1));
    }
}
