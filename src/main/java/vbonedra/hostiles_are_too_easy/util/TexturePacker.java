package vbonedra.hostiles_are_too_easy.util;

import net.minecraft.Minecraft;
import net.minecraft.DynamicTexture;
import net.minecraft.ResourceLocation;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.List;

public class TexturePacker {

    public static ResourceLocation blendTextures(String bgPath, String fgPath, float bgFactor, float fgFactor) {

        ResourceLocation bgLocation = new ResourceLocation(bgPath);
        ResourceLocation fgLocation = new ResourceLocation(fgPath);

        try {
            Minecraft mc = Minecraft.getMinecraft();

            InputStream bgStream = mc.getResourceManager().getResource(bgLocation).getInputStream();
            BufferedImage bgImage = ImageIO.read(bgStream);
            bgStream.close();

            InputStream fgStream = mc.getResourceManager().getResource(fgLocation).getInputStream();
            BufferedImage fgImage = ImageIO.read(fgStream);
            fgStream.close();

            int width = bgImage.getWidth();
            int height = bgImage.getHeight();
            BufferedImage combinedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            float totalFactor = bgFactor + fgFactor;
            if (totalFactor <= 0.0f) {
                totalFactor = 1.0f;
            }

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int bgPixel = bgImage.getRGB(x, y);

                    int fgPixel = (x < fgImage.getWidth() && y < fgImage.getHeight()) ? fgImage.getRGB(x, y) : 0x00000000;

                    int bgA = (bgPixel >> 24) & 0xFF;
                    int bgR = (bgPixel >> 16) & 0xFF;
                    int bgG = (bgPixel >> 8) & 0xFF;
                    int bgB = bgPixel & 0xFF;

                    int fgA = (fgPixel >> 24) & 0xFF;
                    int fgR = (fgPixel >> 16) & 0xFF;
                    int fgG = (fgPixel >> 8) & 0xFF;
                    int fgB = fgPixel & 0xFF;

                    int finalA = Math.min(255, Math.max(0, (int) ((bgA * bgFactor + fgA * fgFactor) / totalFactor)));
                    int finalR = Math.min(255, Math.max(0, (int) ((bgR * bgFactor + fgR * fgFactor) / totalFactor)));
                    int finalG = Math.min(255, Math.max(0, (int) ((bgG * bgFactor + fgG * fgFactor) / totalFactor)));
                    int finalB = Math.min(255, Math.max(0, (int) ((bgB * bgFactor + fgB * fgFactor) / totalFactor)));

                    int blendedPixel = (finalA << 24) | (finalR << 16) | (finalG << 8) | finalB;
                    combinedImage.setRGB(x, y, blendedPixel);
                }
            }

            String cleanBg = bgPath.replace("/", "_").replace(".", "_");
            String cleanFg = fgPath.replace("/", "_").replace(".", "_");
            String dynamicName = "blend_" + cleanBg + "_" + cleanFg + "_" + bgFactor + "_" + fgFactor;


            DynamicTexture dynamicTexture = new DynamicTexture(combinedImage);
            return mc.getTextureManager().getDynamicTextureLocation(dynamicName, dynamicTexture);

        } catch (Exception e) {
            e.printStackTrace();
            return bgLocation;
        }
    }

    public static ResourceLocation maskTemplateWithBlockByBrightness(String blockPath, String templatePath, float bgFactor, float fgFactor, int biomeTint) {
        ResourceLocation blockLocation = new ResourceLocation(blockPath);
        ResourceLocation templateLocation = new ResourceLocation(templatePath);

        try {
            Minecraft mc = Minecraft.getMinecraft();

            InputStream blockStream = TexturePacker.class.getClassLoader().getResourceAsStream(blockPath);
            if (blockStream == null) {
                blockStream = mc.getResourceManager().getResource(blockLocation).getInputStream();
            }
            BufferedImage blockImage = ImageIO.read(blockStream);
            blockStream.close();

            InputStream templateStream = mc.getResourceManager().getResource(templateLocation).getInputStream();
            BufferedImage templateImage = ImageIO.read(templateStream);
            templateStream.close();

            int blockW = blockImage.getWidth();
            int blockH = blockImage.getHeight();
            int tempW = templateImage.getWidth();
            int tempH = templateImage.getHeight();

            float tintR = ((biomeTint >> 16) & 0xFF) / 255.0F;
            float tintG = ((biomeTint >> 8) & 0xFF) / 255.0F;
            float tintB = (biomeTint & 0xFF) / 255.0F;

            java.util.List<BlockPixelData> blockPalette = new java.util.ArrayList<>();
            for (int bY = 0; bY < blockH; bY++) {
                for (int bX = 0; bX < blockW; bX++) {
                    int rgb = blockImage.getRGB(bX, bY);
                    if (((rgb >> 24) & 0xFF) == 0) continue;

                    int r = (rgb >> 16) & 0xFF;
                    int g = (rgb >> 8) & 0xFF;
                    int b = rgb & 0xFF;

                    if (biomeTint != -1) {
                        r = (int)(r * tintR);
                        g = (int)(g * tintG);
                        b = (int)(b * tintB);
                        rgb = (0xFF << 24) | (r << 16) | (g << 8) | b;
                    }

                    double brightness = 0.2126 * r + 0.7152 * g + 0.0722 * b;
                    blockPalette.add(new BlockPixelData(rgb, brightness));
                }
            }

            if (blockPalette.isEmpty()) {
                blockPalette.add(new BlockPixelData(0xFFFFFFFF, 255.0));
            }

            blockPalette.sort(java.util.Comparator.comparingDouble(p -> p.brightness));

            java.util.List<Double> templateBrightnesses = new java.util.ArrayList<>();
            for (int tY = 0; tY < tempH; tY++) {
                for (int tX = 0; tX < tempW; tX++) {
                    int rgb = templateImage.getRGB(tX, tY);
                    if (((rgb >> 24) & 0xFF) == 0) continue;

                    int r = (rgb >> 16) & 0xFF;
                    int g = (rgb >> 8) & 0xFF;
                    int b = rgb & 0xFF;
                    templateBrightnesses.add(0.2126 * r + 0.7152 * g + 0.0722 * b);
                }
            }
            templateBrightnesses.sort(java.util.Comparator.naturalOrder());

            BufferedImage outputImage = new BufferedImage(tempW, tempH, BufferedImage.TYPE_INT_ARGB);
            float totalFactor = bgFactor + fgFactor;
            if (totalFactor <= 0.0f) totalFactor = 1.0f;

            for (int y = 0; y < tempH; y++) {
                for (int x = 0; x < tempW; x++) {
                    int templatePixel = templateImage.getRGB(x, y);
                    int templateA = (templatePixel >> 24) & 0xFF;

                    if (templateA == 0) {
                        outputImage.setRGB(x, y, 0x00000000);
                        continue;
                    }

                    int fgR = (templatePixel >> 16) & 0xFF;
                    int fgG = (templatePixel >> 8) & 0xFF;
                    int fgB = templatePixel & 0xFF;
                    double currentTemplateBrightness = 0.2126 * fgR + 0.7152 * fgG + 0.0722 * fgB;

                    int blockPixel = getBlockPixel(templateBrightnesses, currentTemplateBrightness, blockPalette);
                    int bgR = (blockPixel >> 16) & 0xFF;
                    int bgG = (blockPixel >> 8) & 0xFF;
                    int bgB = blockPixel & 0xFF;

                    int finalR = Math.min(255, Math.max(0, (int) ((bgR * bgFactor + fgR * fgFactor) / totalFactor)));
                    int finalG = Math.min(255, Math.max(0, (int) ((bgG * bgFactor + fgG * fgFactor) / totalFactor)));
                    int finalB = Math.min(255, Math.max(0, (int) ((bgB * bgFactor + fgB * fgFactor) / totalFactor)));

                    int blendedPixel = (templateA << 24) | (finalR << 16) | (finalG << 8) | finalB;
                    outputImage.setRGB(x, y, blendedPixel);
                }
            }

            String cleanBlock = blockPath.replace("/", "_").replace(".", "_");
            String cleanTemplate = templatePath.replace("/", "_").replace(".", "_");
            String dynamicName = "mask_brightness_tint_" + cleanBlock + "_" + cleanTemplate + "_" + biomeTint + "_" + bgFactor + "_" + fgFactor;

            DynamicTexture dynamicTexture = new DynamicTexture(outputImage);
            return mc.getTextureManager().getDynamicTextureLocation(dynamicName, dynamicTexture);

        } catch (Exception e) {
            e.printStackTrace();
            return templateLocation;
        }
    }

    private static int getBlockPixel(List<Double> templateBrightnesses, double currentTemplateBrightness, List<BlockPixelData> blockPalette) {
        int rankInTemplate = java.util.Collections.binarySearch(templateBrightnesses, currentTemplateBrightness);
        if (rankInTemplate < 0) rankInTemplate = -rankInTemplate - 1;

        double relativeBrightnessPct = (double) rankInTemplate / Math.max(1, templateBrightnesses.size() - 1);

        int targetBlockIdx = (int) (relativeBrightnessPct * (blockPalette.size() - 1));
        targetBlockIdx = Math.min(blockPalette.size() - 1, Math.max(0, targetBlockIdx));

        return blockPalette.get(targetBlockIdx).rgb;
    }


    private record BlockPixelData(int rgb, double brightness) {
    }

}
