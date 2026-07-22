package vbonedra.hostiles_are_too_easy.mixin;

import net.minecraft.*;
import net.minecraft.server.MinecraftServer;
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
    public void injectCtor(CallbackInfo callbackInfo) {
        // TODO: add check if those are already added
        this.spawnableMonsterList.add(new SpawnListEntry(EntityHellhound.class, 20, 1, 2));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityDemonSpider.class, 20, 1, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityInfernalCreeper.class, 20, 1, 1));
    }
}