package vbonedra.hostiles_are_too_easy.util;

public interface ICelestialType {
    int HATE$getCelestialType();
    void HATE$setCelestialType(int type);

    // NOTE: Silverfish celestialType must go below
    int celestialTypeUnset = 0;
    int celestialTypeVanilla = -1;
    int celestialTypeStartPositive = 0;
    int celestialTypeStartNegative = -1;


    // EntitySkeleton
    int celestialTypeSkeletonWithered = celestialTypeStartPositive + 1;
    // EntityCreeper
    int celestialTypeCreeperMimic = celestialTypeStartPositive + 1;
    // EntityZombie
    int celestialTypeZombiePhase = celestialTypeStartPositive + 1;
    int celestialTypeZombieTemerarious = celestialTypeStartPositive + 2;
    // EntityArachnid
    int celestialTypeArachnidWarp = celestialTypeStartPositive + 1;
    // EntityShadow
    int celestialTypeShadowGloom = celestialTypeStartPositive + 1;
    // EntityInvisibleStalker
    int celestialTypeInvisibleStalkerThief = celestialTypeStartPositive + 1;
    // EntityGhoul
    int celestialTypeGhoulVampire = celestialTypeStartPositive + 1;



}
