package vbonedra.hostiles_are_too_easy.util;

public interface ICelestialType {
    int HATE$getCelestialType();
    void HATE$setCelestialType(int type);

    // maybe not the best way to do it; yea it works, but it celestialTypeVanilla couldn't be used by any type, so Block celestialType Silverfish cant have block with id = celestialTypeVanilla
    int celestialTypeUnset = 0;
    int celestialTypeVanilla = 1;


    // EntitySkeleton
    int celestialTypeSkeletonWithered = celestialTypeVanilla + 1;
    // EntityCreeper
    int celestialTypeCreeperMimic = celestialTypeVanilla + 1;
    // EntityZombie
    int celestialTypeZombiePhase = celestialTypeVanilla + 1;
    // EntityPhaseSpider
    int celestialTypeArachnidWarp = celestialTypeVanilla + 1;

}
