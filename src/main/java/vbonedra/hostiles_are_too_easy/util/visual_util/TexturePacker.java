package vbonedra.hostiles_are_too_easy.util.visual_util;

import net.minecraft.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static vbonedra.hostiles_are_too_easy.HostilesAreTooEasyMod.HATE_LOGGER;

public class TexturePacker {
    public static final Map<String, ResourceLocation> cacheResourceLocation = new HashMap<>();
    public static final Map<String, BufferedImage> cacheBufferedImage = new HashMap<>();

    private static int getPixelSourcePixel(List<Double> templateBrightnesses, double currentTemplateBrightness, List<PixelSourcePixelData> pixelSourcePalette, boolean reverseBrightness) {
        int rankInTemplate = java.util.Collections.binarySearch(templateBrightnesses, currentTemplateBrightness);
        if (rankInTemplate < 0) rankInTemplate = -rankInTemplate - 1;

        double relativeBrightnessPct = (double) rankInTemplate / Math.max(1, templateBrightnesses.size() - 1);

        if (reverseBrightness) {
            relativeBrightnessPct = 1.0 - relativeBrightnessPct;
        }

        int targetPixelSourceIdx = (int) (relativeBrightnessPct * (pixelSourcePalette.size() - 1));
        targetPixelSourceIdx = Math.min(pixelSourcePalette.size() - 1, Math.max(0, targetPixelSourceIdx));

        return pixelSourcePalette.get(targetPixelSourceIdx).rgb;
    }

    private record PixelSourcePixelData(int rgb, double brightness) {}

    // paints one texture over another with factor: longdead + wither skeleton = withered longdead
    public static BufferedImage blendForegroundOverBackgroundWithFactor(BufferedImage bgImage, BufferedImage fgImage,
                                                                        float bgFactor, float fgFactor) {
        try {
            // make new texture, paint all on bgImage
            int width = bgImage.getWidth();
            int height = bgImage.getHeight();

            float factorSum = bgFactor + fgFactor;
            if (factorSum <= 0.0f) {
                factorSum = 1.0f;
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

                    int finalA = Math.min(255, Math.max(0, (int) ((bgA * bgFactor + fgA * fgFactor) / factorSum)));
                    int finalR = Math.min(255, Math.max(0, (int) ((bgR * bgFactor + fgR * fgFactor) / factorSum)));
                    int finalG = Math.min(255, Math.max(0, (int) ((bgG * bgFactor + fgG * fgFactor) / factorSum)));
                    int finalB = Math.min(255, Math.max(0, (int) ((bgB * bgFactor + fgB * fgFactor) / factorSum)));

                    int blendedPixel = (finalA << 24) | (finalR << 16) | (finalG << 8) | finalB;
                    bgImage.setRGB(x, y, blendedPixel);
                }
            }
        } catch (Exception e) {
            System.err.println("blendForegroundOverBackgroundWithFactor caught error: " + e);
        }


        return bgImage;
    }

    // takes pixels from source and applies them on template with factor - make zombie use phase spider's color pallet
    public static BufferedImage maskTemplateWithPixelSourceByBrightness(String pixelSourceImage, String templateImage,
                                                                        float sourceFactor, float templateFactor, int biomeTint, boolean reverseBrightness) throws IOException {
        Minecraft mc = Minecraft.getMinecraft();
        return maskTemplateWithPixelSourceByBrightness(
                ImageIO.read(mc.getResourceManager().getResource(new ResourceLocation(pixelSourceImage)).getInputStream()),
                ImageIO.read(mc.getResourceManager().getResource(new ResourceLocation(templateImage)).getInputStream()),
                sourceFactor, templateFactor, biomeTint, reverseBrightness);
    }
    public static BufferedImage maskTemplateWithPixelSourceByBrightness(BufferedImage pixelSourceImage, BufferedImage templateImage,
                                                                        float sourceFactor, float templateFactor, int biomeTint, boolean reverseBrightness) {
        try {
            int pixelSourceW = pixelSourceImage.getWidth();
            int pixelSourceH = pixelSourceImage.getHeight();
            int tempW = templateImage.getWidth();
            int tempH = templateImage.getHeight();

            float tintR = ((biomeTint >> 16) & 0xFF) / 255.0F;
            float tintG = ((biomeTint >> 8) & 0xFF) / 255.0F;
            float tintB = (biomeTint & 0xFF) / 255.0F;

            java.util.List<PixelSourcePixelData> pixelSourcePalette = new java.util.ArrayList<>();
            for (int bY = 0; bY < pixelSourceH; bY++) {
                for (int bX = 0; bX < pixelSourceW; bX++) {
                    int rgb = pixelSourceImage.getRGB(bX, bY);
                    if (((rgb >> 24) & 0xFF) == 0) continue;

                    int r = (rgb >> 16) & 0xFF;
                    int g = (rgb >> 8) & 0xFF;
                    int b = rgb & 0xFF;

                    if (biomeTint >= 0) {
                        r = (int)(r * tintR);
                        g = (int)(g * tintG);
                        b = (int)(b * tintB);
                        rgb = (0xFF << 24) | (r << 16) | (g << 8) | b;
                    }

                    double brightness = 0.2126 * r + 0.7152 * g + 0.0722 * b;
                    pixelSourcePalette.add(new PixelSourcePixelData(rgb, brightness));
                }
            }

            if (pixelSourcePalette.isEmpty()) {
                pixelSourcePalette.add(new PixelSourcePixelData(0xFFFFFFFF, 255.0));
            }

            pixelSourcePalette.sort(java.util.Comparator.comparingDouble(p -> p.brightness));

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
            float totalFactor = sourceFactor + templateFactor;
            if (totalFactor < 1.0f) totalFactor = 1.0f;

            for (int y = 0; y < tempH; y++) {
                for (int x = 0; x < tempW; x++) {
                    int templatePixel = templateImage.getRGB(x, y);
                    int templateA = (templatePixel >> 24) & 0xFF;

                    if (templateA == 0) {
                        outputImage.setRGB(x, y, 0x00000000);
                        continue;
                    }

                    int templateR = (templatePixel >> 16) & 0xFF;
                    int templateG = (templatePixel >> 8) & 0xFF;
                    int templateB = templatePixel & 0xFF;
                    double currentTemplateBrightness = 0.2126 * templateR + 0.7152 * templateG + 0.0722 * templateB;

                    int pixelSourcePixel = getPixelSourcePixel(templateBrightnesses, currentTemplateBrightness, pixelSourcePalette, reverseBrightness);
                    int sourceR = (pixelSourcePixel >> 16) & 0xFF;
                    int sourceG = (pixelSourcePixel >> 8) & 0xFF;
                    int sourceB = pixelSourcePixel & 0xFF;

                    int finalR = Math.min(255, Math.max(0, (int) ((sourceR * sourceFactor + templateR * templateFactor) / totalFactor)));
                    int finalG = Math.min(255, Math.max(0, (int) ((sourceG * sourceFactor + templateG * templateFactor) / totalFactor)));
                    int finalB = Math.min(255, Math.max(0, (int) ((sourceB * sourceFactor + templateB * templateFactor) / totalFactor)));

                    int blendedPixel = (templateA << 24) | (finalR << 16) | (finalG << 8) | finalB;
                    outputImage.setRGB(x, y, blendedPixel);
                }
            }

            return outputImage;

        } catch (Exception e) {
            System.err.println("maskTemplateWithPixelSourceByBrightness failed masking: " + e);
            return templateImage;
        }
    }


    // puts source over template with factor over and over again - silverfish using ore texture
    public static BufferedImage maskTemplateWithPixelSource(String pixelSourceImage, String templateImage,
                                                            float sourceFactor, float templateFactor, int biomeTint) throws IOException {
        Minecraft mc = Minecraft.getMinecraft();
        return maskTemplateWithPixelSource(
                ImageIO.read(mc.getResourceManager().getResource(new ResourceLocation(pixelSourceImage)).getInputStream()),
                ImageIO.read(mc.getResourceManager().getResource(new ResourceLocation(templateImage)).getInputStream()),
                sourceFactor,
                templateFactor,
                biomeTint
        );
    }
    public static BufferedImage maskTemplateWithPixelSource(BufferedImage pixelSourceImage, BufferedImage templateImage,
                                                            float sourceFactor, float templateFactor, int biomeTint) {
        try {
            int pixelSourceW = pixelSourceImage.getWidth();
            int pixelSourceH = pixelSourceImage.getHeight();
            int tempW = templateImage.getWidth();
            int tempH = templateImage.getHeight();

            float tintR = ((biomeTint >> 16) & 0xFF) / 255.0F;
            float tintG = ((biomeTint >> 8) & 0xFF) / 255.0F;
            float tintB = (biomeTint & 0xFF) / 255.0F;

            BufferedImage outputImage = new BufferedImage(tempW, tempH, BufferedImage.TYPE_INT_ARGB);
            float totalFactor = sourceFactor + templateFactor;
            if (totalFactor <= 0.0f) totalFactor = 1.0f;

            for (int y = 0; y < tempH; y++) {
                for (int x = 0; x < tempW; x++) {
                    int templatePixel = templateImage.getRGB(x, y);
                    int templateA = (templatePixel >> 24) & 0xFF;

                    if (templateA == 0) {
                        outputImage.setRGB(x, y, 0x00000000);
                        continue;
                    }

                    int pixelSourceX = x % pixelSourceW;
                    int pixelSourceY = y % pixelSourceH;
                    int pixelSourcePixel = pixelSourceImage.getRGB(pixelSourceX, pixelSourceY);

                    int sourceR = (pixelSourcePixel >> 16) & 0xFF;
                    int sourceG = (pixelSourcePixel >> 8) & 0xFF;
                    int sourceB = pixelSourcePixel & 0xFF;

                    if (biomeTint != -1) {
                        sourceR = (int) (sourceR * tintR);
                        sourceG = (int) (sourceG * tintG);
                        sourceB = (int) (sourceB * tintB);
                    }

                    int templateR = (templatePixel >> 16) & 0xFF;
                    int templateG = (templatePixel >> 8) & 0xFF;
                    int templateB = templatePixel & 0xFF;

                    int finalR = Math.min(255, Math.max(0, (int) ((sourceR * sourceFactor + templateR * templateFactor) / totalFactor)));
                    int finalG = Math.min(255, Math.max(0, (int) ((sourceG * sourceFactor + templateG * templateFactor) / totalFactor)));
                    int finalB = Math.min(255, Math.max(0, (int) ((sourceB * sourceFactor + templateB * templateFactor) / totalFactor)));

                    int blendedPixel = (templateA << 24) | (finalR << 16) | (finalG << 8) | finalB;
                    outputImage.setRGB(x, y, blendedPixel);
                }
            }

            return outputImage;

        } catch (Exception e) {
            System.err.println("maskTemplateWithPixelSource failed masking: " + e);
            return templateImage;
        }
    }

    // puts random pixels of color with factor - add red pimples to infected zombie
    public static BufferedImage maskTemplateWithPixelSourceByPattern(BufferedImage pixelSourceImage, BufferedImage templateImage,
                                                                     float sourceFactor, float templateFactor, byte patternId, float amountFactor) {
        try {
            int pixelSourceW = pixelSourceImage.getWidth();
            int pixelSourceH = pixelSourceImage.getHeight();
            int tempW = templateImage.getWidth();
            int tempH = templateImage.getHeight();

            java.util.List<Integer> pixelSourcePalette = new java.util.ArrayList<>();
            for (int bY = 0; bY < pixelSourceH; bY++) {
                for (int bX = 0; bX < pixelSourceW; bX++) {
                    int rgb = pixelSourceImage.getRGB(bX, bY);
                    if (((rgb >> 24) & 0xFF) != 0) {
                        pixelSourcePalette.add(rgb);
                    }
                }
            }

            if (pixelSourcePalette.isEmpty()) {
                pixelSourcePalette.add(0xFFFFFFFF);
            }

            java.util.Random random = new java.util.Random(patternId);
            BufferedImage outputImage = new BufferedImage(tempW, tempH, BufferedImage.TYPE_INT_ARGB);
            float totalFactor = sourceFactor + templateFactor;
            if (totalFactor <= 0.0f) totalFactor = 1.0f;

            for (int y = 0; y < tempH; y++) {
                for (int x = 0; x < tempW; x++) {
                    int templatePixel = templateImage.getRGB(x, y);
                    int templateA = (templatePixel >> 24) & 0xFF;

                    if (templateA == 0) {
                        outputImage.setRGB(x, y, 0x00000000);
                        continue;
                    }

                    int templateR = (templatePixel >> 16) & 0xFF;
                    int templateG = (templatePixel >> 8) & 0xFF;
                    int templateB = templatePixel & 0xFF;

                    int finalR = templateR;
                    int finalG = templateG;
                    int finalB = templateB;

                    if (random.nextFloat() < amountFactor) {
                        int pixelSourcePixel = pixelSourcePalette.get(random.nextInt(pixelSourcePalette.size()));
                        int sourceR = (pixelSourcePixel >> 16) & 0xFF;
                        int sourceG = (pixelSourcePixel >> 8) & 0xFF;
                        int sourceB = pixelSourcePixel & 0xFF;

                        finalR = Math.min(255, Math.max(0, (int) ((sourceR * sourceFactor + templateR * templateFactor) / totalFactor)));
                        finalG = Math.min(255, Math.max(0, (int) ((sourceG * sourceFactor + templateG * templateFactor) / totalFactor)));
                        finalB = Math.min(255, Math.max(0, (int) ((sourceB * sourceFactor + templateB * templateFactor) / totalFactor)));
                    }

                    int blendedPixel = (templateA << 24) | (finalR << 16) | (finalG << 8) | finalB;
                    outputImage.setRGB(x, y, blendedPixel);
                }
            }

            return outputImage;

        } catch (Exception e) {
            System.err.println("maskTemplateWithPixelSourceByPattern caught error: " + e);
            return templateImage;
        }
    }



    // TODO: modded blocks might save block textures differently, maybe its better to change how it gets texture path
    // takes block and its metadata to generate its block path
    public static String getBlockTexturePath(Block block, int metadata, String nameForError) {
        if (block == null) return "textures/blocks/dirt.png";
        try {
            Icon blockIcon = block.getIcon(1, metadata);
            if (blockIcon != null && blockIcon.getIconName() != null) {
                return "textures/blocks/" + blockIcon.getIconName() + ".png";
            }
        } catch (Exception e) {
            System.err.println(nameForError + " has hard time getting blockIcon / texture path: " + e);
        }
        return "textures/blocks/dirt.png";
    }


    public static void saveToCache(BufferedImage bufferedImage, String cacheKey) {
        DynamicTexture dynamicTexture = new DynamicTexture(bufferedImage);
        ResourceLocation dynamicLocation = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(cacheKey, dynamicTexture);
        cacheResourceLocation.put(cacheKey, dynamicLocation);
    }



    @FunctionalInterface
    public interface TextureProcessor {
        BufferedImage process(BufferedImage base) throws Exception;
    }

    public static ResourceLocation getOrCreateInMemoryTexture(String templateTexture, String cacheKeyFinal, TextureProcessor processor) {
        if (cacheResourceLocation.containsKey(cacheKeyFinal)) {
            return cacheResourceLocation.get(cacheKeyFinal);
        }

        try {
            Minecraft mc = Minecraft.getMinecraft();

            BufferedImage baseImg = cacheBufferedImage.get(templateTexture);
            if (baseImg == null) {
                baseImg = ImageIO.read(mc.getResourceManager().getResource(new ResourceLocation(templateTexture)).getInputStream());
                cacheBufferedImage.put(templateTexture, baseImg);
            }

            BufferedImage resultImg = processor.process(baseImg);

            saveToCache(resultImg, cacheKeyFinal);

            return cacheResourceLocation.get(cacheKeyFinal);

        } catch (Exception e) {
            HATE_LOGGER.error(e);;
            return new ResourceLocation(templateTexture);
        }
    }


}
