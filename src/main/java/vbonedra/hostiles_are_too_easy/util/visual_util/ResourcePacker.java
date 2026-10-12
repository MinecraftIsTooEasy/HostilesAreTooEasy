package vbonedra.hostiles_are_too_easy.util.visual_util;

import net.minecraft.*;

import java.awt.image.BufferedImage;

public class ResourcePacker {
    // oneBlackTransparentPixel used to remove texture
    public static String oneBlackTransparentPixelName = "oneBlackTransparentPixel";
    public static BufferedImage oneBlackTransparentPixelTexture = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    public static DynamicTexture oneBlackTransparentPixelDynamicTexture = new DynamicTexture(oneBlackTransparentPixelTexture);
    public static ResourceLocation oneBlackTransparentPixelResourceLocation = new ResourceLocation("oneBlackTransparentPixelName");

    // needs bug test, might call minecraft when it isn't ready
    private ResourcePacker() {
        oneBlackTransparentPixelTexture.setRGB(0, 0, 0x00FFFFFF);

        // is it even required?
        // TODO: maybe replace getDynamicTextureLocation with manual linking texture to location
        Minecraft mc = Minecraft.getMinecraft();
        oneBlackTransparentPixelResourceLocation = mc.getTextureManager().getDynamicTextureLocation(oneBlackTransparentPixelName, oneBlackTransparentPixelDynamicTexture);
    }


    // this must take texture data, change it and return
    // each method must do its own thing, no combined functions
    // then in any use case code must be changed to save texture data into dynamic texture and do whatever it did
    // maybe prebake all textures at register and not runtime? some random and environment textures would still be made in runtime



}
