package vbonedra.hostiles_are_too_easy.register;

import huix.glacier.api.entrypoint.IGameRegistry;

import static vbonedra.hostiles_are_too_easy.util.AchievementExtend.registerAchievements;

public class HostilesAreTooEasyRegistryInit implements IGameRegistry {
    @Override
	public void onGameRegistry() {
        // TODO: add AchievementLib tab for achievements related to celestial types
        //  for example achievement for killing celestial type of every mob with some descriptions on their mechanics,
        //  so player would be able to know what exactly is happening. maybe it could be just short description though
        registerAchievements();
	}
}
