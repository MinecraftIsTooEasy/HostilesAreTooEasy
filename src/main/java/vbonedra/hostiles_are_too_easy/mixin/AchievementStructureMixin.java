package vbonedra.hostiles_are_too_easy.mixin;

import net.minecraft.Achievement;
import net.minecraft.AchievementList;
import net.minecraft.GuiAchievements;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.lang.reflect.Field;
import java.util.*;

@Mixin(GuiAchievements.class)
public class AchievementStructureMixin { // TODO: doesn't fit mod idea, must be enhanced and used in some AchievementEnhanced mod


    @Redirect(method = "drawScreen(IIF)V", at = @At(value = "FIELD", target = "Lnet/minecraft/GuiAchievements;guiMapTop:I"))
    private int redirectDrawMapTop() {
        return 0;
    }

    @Redirect(method = "drawScreen(IIF)V", at = @At(value = "FIELD", target = "Lnet/minecraft/GuiAchievements;guiMapLeft:I"))
    private int redirectDrawMapLeft() {
        return 0;
    }

    @Redirect(method = "drawScreen(IIF)V", at = @At(value = "FIELD", target = "Lnet/minecraft/GuiAchievements;guiMapBottom:I"))
    private int redirectDrawMapBottom() {
        return 8000;
    }

    @Redirect(method = "drawScreen(IIF)V", at = @At(value = "FIELD", target = "Lnet/minecraft/GuiAchievements;guiMapRight:I"))
    private int redirectDrawMapRight() {
        return 8000;
    }

    @Redirect(method = "genAchievementBackground(IIF)V", at = @At(value = "FIELD", target = "Lnet/minecraft/GuiAchievements;guiMapTop:I"))
    private int redirectBgMapTop() {
        return 0;
    }

    @Redirect(method = "genAchievementBackground(IIF)V", at = @At(value = "FIELD", target = "Lnet/minecraft/GuiAchievements;guiMapLeft:I"))
    private int redirectBgMapLeft() {
        return 0;
    }

    @Redirect(method = "genAchievementBackground(IIF)V", at = @At(value = "FIELD", target = "Lnet/minecraft/GuiAchievements;guiMapBottom:I"))
    private int redirectBgMapBottom() {
        return 8000;
    }

    @Redirect(method = "genAchievementBackground(IIF)V", at = @At(value = "FIELD", target = "Lnet/minecraft/GuiAchievements;guiMapRight:I"))
    private int redirectBgMapRight() {
        return 8000;
    }
    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        List<Achievement> achievements = (List<Achievement>) AchievementList.achievementList;
        if (achievements == null || achievements.isEmpty()) return;

        Map<Achievement, List<Achievement>> tree = new HashMap<>();
        List<Achievement> roots = new ArrayList<>();

        for (Achievement a : achievements) {
            if (a.parentAchievement == null) { roots.add(a); }
            else { tree.computeIfAbsent(a.parentAchievement, k -> new ArrayList<>()).add(a); }
        }

        Map<Achievement, int[]> finalCoords = new HashMap<>();
        int currentGlobalXOffset = 0;

        for (Achievement root : roots) {
            Map<Achievement, Double> xMap = new HashMap<>();
            Map<Achievement, Double> yMap = new HashMap<>();
            Map<Achievement, Double> modMap = new HashMap<>();
            Map<Achievement, Achievement> ancMap = new HashMap<>();
            Map<Achievement, Achievement> predMap = new HashMap<>();
            Map<Achievement, Achievement> threadMap = new HashMap<>();
            Map<Achievement, Double> changeMap = new HashMap<>();
            Map<Achievement, Double> shiftMap = new HashMap<>();

            initTreeState(root, tree, xMap, yMap, modMap, ancMap, predMap, threadMap, changeMap, shiftMap);
            setPredsFlat(root, tree, predMap);
            walk1Flat(root, tree, 1.0, xMap, yMap, modMap, ancMap, predMap, threadMap, changeMap, shiftMap);
            walk2Flat(root, tree, 0.0, yMap, modMap);

            double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
            for (Achievement a : xMap.keySet()) {
                double cx = xMap.get(a), cy = yMap.get(a);
                if (cx < minX) minX = cx;
                if (cy < minY) minY = cy;
            }

            int treeMaxX = 0;
            for (Achievement a : xMap.keySet()) {
                int nx = (int) (xMap.get(a) - minX);
                int ny = (int) (yMap.get(a) - minY);
                finalCoords.put(a, new int[]{(currentGlobalXOffset + nx) * 2, ny});
                if (nx > treeMaxX) treeMaxX = nx;
            }
            currentGlobalXOffset += treeMaxX + 1;
        }

        try {
            Field colField = Achievement.class.getField("displayColumn");
            Field rowField = Achievement.class.getField("displayRow");
            colField.setAccessible(true);
            rowField.setAccessible(true);
            for (Map.Entry<Achievement, int[]> entry : finalCoords.entrySet()) {
                Achievement a = entry.getKey(); int[] c = entry.getValue();
                colField.setInt(a, c[0]);
                rowField.setInt(a, c[1]);
            }
        } catch (Exception e) {
            System.err.println("Failed to modify achievement structure: " + e);
        }
    }

    @Unique private void initTreeState(Achievement v, Map<Achievement, List<Achievement>> tree,
                               Map<Achievement, Double> xMap, Map<Achievement, Double> yMap, Map<Achievement, Double> modMap,
                               Map<Achievement, Achievement> ancMap, Map<Achievement, Achievement> predMap, Map<Achievement, Achievement> threadMap,
                               Map<Achievement, Double> changeMap, Map<Achievement, Double> shiftMap) {
        xMap.put(v, 0.0); yMap.put(v, 0.0); modMap.put(v, 0.0); ancMap.put(v, v);
        predMap.put(v, null); threadMap.put(v, null); changeMap.put(v, 0.0); shiftMap.put(v, 0.0);
        List<Achievement> children = tree.get(v);
        if (children != null) {
            for (Achievement c : children) {
                initTreeState(c, tree, xMap, yMap, modMap, ancMap, predMap, threadMap, changeMap, shiftMap);
            }
        }
    }

    @Unique private void setPredsFlat(Achievement v, Map<Achievement, List<Achievement>> tree, Map<Achievement, Achievement> predMap) {
        List<Achievement> children = tree.get(v);
        if (children != null) {
            Achievement left = null;
            for (Achievement c : children) {
                predMap.put(c, left); left = c;
                setPredsFlat(c, tree, predMap);
            }
        }
    }

    @Unique private void walk1Flat(Achievement v, Map<Achievement, List<Achievement>> tree, double d,
                           Map<Achievement, Double> xMap, Map<Achievement, Double> yMap, Map<Achievement, Double> modMap,
                           Map<Achievement, Achievement> ancMap, Map<Achievement, Achievement> predMap, Map<Achievement, Achievement> threadMap,
                           Map<Achievement, Double> changeMap, Map<Achievement, Double> shiftMap) {
        List<Achievement> children = tree.get(v);
        if (children == null || children.isEmpty()) {
            Achievement pred = predMap.get(v);
            yMap.put(v, (pred != null) ? yMap.get(pred) + d : 0.0);
        } else {
            Achievement dAnc = children.get(0);
            for (Achievement c : children) {
                xMap.put(c, xMap.get(v) + 1);
                walk1Flat(c, tree, d, xMap, yMap, modMap, ancMap, predMap, threadMap, changeMap, shiftMap);
                dAnc = apporFlat(c, dAnc, d, tree, yMap, modMap, ancMap, predMap, threadMap, changeMap, shiftMap);
            }
            shiftTreeFlat(v, tree, yMap, modMap, changeMap, shiftMap);
            double mid = (yMap.get(children.get(0)) + yMap.get(children.get(children.size() - 1))) / 2.0;
            Achievement pred = predMap.get(v);
            if (pred != null) {
                yMap.put(v, yMap.get(pred) + d); modMap.put(v, yMap.get(v) - mid);
            } else { yMap.put(v, mid); }
        }
    }

    @Unique private Achievement apporFlat(Achievement v, Achievement dAnc, double d, Map<Achievement, List<Achievement>> tree,
                                  Map<Achievement, Double> yMap, Map<Achievement, Double> modMap, Map<Achievement, Achievement> ancMap,
                                  Map<Achievement, Achievement> predMap, Map<Achievement, Achievement> threadMap,
                                  Map<Achievement, Double> changeMap, Map<Achievement, Double> shiftMap) {
        Achievement w = predMap.get(v);
        if (w != null) {
            Achievement vip = v, vop = v, vim = w, vom = tree.get(v.parentAchievement).get(0);
            double sip = modMap.get(vip), sop = modMap.get(vop), sim = modMap.get(vim), som = modMap.get(vom);
            while (nextRightFlat(vim, tree, threadMap) != null && nextLeftFlat(vip, tree, threadMap) != null) {
                vim = nextRightFlat(vim, tree, threadMap); vip = nextLeftFlat(vip, tree, threadMap);
                vom = nextLeftFlat(vom, tree, threadMap); vop = nextRightFlat(vop, tree, threadMap);
                ancMap.put(vop, v);
                double sh = (yMap.get(vim) + sim) - (yMap.get(vip) + sip) + d;
                if (sh > 0) {
                    moveSubtreeFlat(ancNodeFlat(vim, v, dAnc, tree, ancMap), v, sh, tree, yMap, modMap, changeMap, shiftMap);
                    sip += sh; sop += sh;
                }
                sim += modMap.get(vim); sip += modMap.get(vip); som += modMap.get(vom); sop += modMap.get(vop);
            }
            if (nextRightFlat(vim, tree, threadMap) != null && nextRightFlat(vop, tree, threadMap) == null) {
                threadMap.put(vop, nextRightFlat(vim, tree, threadMap));
                modMap.put(vop, modMap.get(vop) + (sim - sop));
            }
            if (nextLeftFlat(vip, tree, threadMap) != null && nextLeftFlat(vom, tree, threadMap) == null) {
                threadMap.put(vom, nextLeftFlat(vip, tree, threadMap));
                modMap.put(vom, modMap.get(vom) + (sip - som)); dAnc = v;
            }
        }
        return dAnc;
    }

    @Unique private Achievement nextLeftFlat(Achievement v, Map<Achievement, List<Achievement>> tree, Map<Achievement, Achievement> threadMap) {
        List<Achievement> children = tree.get(v);
        return (children != null && !children.isEmpty()) ? children.get(0) : threadMap.get(v);
    }

    @Unique private void walk2Flat(Achievement v, Map<Achievement, List<Achievement>> tree, double m, Map<Achievement, Double> yMap, Map<Achievement, Double> modMap) {
        yMap.put(v, yMap.get(v) + m);
        List<Achievement> children = tree.get(v);
        if (children != null) {
            for (Achievement c : children) { walk2Flat(c, tree, m + modMap.get(v), yMap, modMap); }
        }
    }

    @Unique private Achievement nextRightFlat(Achievement v, Map<Achievement, List<Achievement>> tree, Map<Achievement, Achievement> threadMap) {
        List<Achievement> children = tree.get(v);
        return (children != null && !children.isEmpty()) ? children.get(children.size() - 1) : threadMap.get(v);
    }

    @Unique private void moveSubtreeFlat(Achievement wm, Achievement wp, double sh, Map<Achievement, List<Achievement>> tree,
                                 Map<Achievement, Double> yMap, Map<Achievement, Double> modMap, Map<Achievement, Double> changeMap, Map<Achievement, Double> shiftMap) {
        List<Achievement> siblings = tree.get(wp.parentAchievement);
        int sub = siblings.indexOf(wp) - siblings.indexOf(wm);
        changeMap.put(wp, changeMap.get(wp) - (sh / sub)); shiftMap.put(wp, shiftMap.get(wp) + sh);
        changeMap.put(wm, changeMap.get(wm) + (sh / sub)); yMap.put(wp, yMap.get(wp) + sh); modMap.put(wp, modMap.get(wp) + sh);
    }

    @Unique private void shiftTreeFlat(Achievement v, Map<Achievement, List<Achievement>> tree,
                               Map<Achievement, Double> yMap, Map<Achievement, Double> modMap, Map<Achievement, Double> changeMap, Map<Achievement, Double> shiftMap) {
        List<Achievement> children = tree.get(v);
        if (children == null) return;
        double sh = 0, ch = 0;
        for (int i = children.size() - 1; i >= 0; i--) {
            Achievement c = children.get(i);
            yMap.put(c, yMap.get(c) + sh); modMap.put(c, modMap.get(c) + sh);
            ch += changeMap.get(c); sh += shiftMap.get(c) + ch;
        }
    }

    @Unique private Achievement ancNodeFlat(Achievement vim, Achievement v, Achievement dAnc, Map<Achievement, List<Achievement>> tree, Map<Achievement, Achievement> ancMap) {
        return tree.get(v.parentAchievement).contains(ancMap.get(vim)) ? ancMap.get(vim) : dAnc;
    }
}
