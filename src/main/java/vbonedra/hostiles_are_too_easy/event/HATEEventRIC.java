package vbonedra.hostiles_are_too_easy.event;

import moddedmite.rustedironcore.api.event.Handlers;
import vbonedra.hostiles_are_too_easy.event.listener.AchievementListener;

import static vbonedra.hostiles_are_too_easy.util.AchievementExtend.registerAchievements;

public class HATEEventRIC extends Handlers {
    public static void register() {
        Achievement.register(new AchievementListener());
    }

}
