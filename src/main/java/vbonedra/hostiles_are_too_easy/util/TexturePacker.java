package vbonedra.hostiles_are_too_easy.util;

import net.minecraft.Minecraft;
import net.minecraft.DynamicTexture;
import net.minecraft.ResourceLocation;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.List;

public class TexturePacker {
    private static int getPixelSourcePixel(List<Double> templateBrightnesses, double currentTemplateBrightness, List<PixelSourcePixelData> pixelSourcePalette) {
        return getPixelSourcePixel(templateBrightnesses, currentTemplateBrightness, pixelSourcePalette, false);
    }
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
            System.err.println("blendTextures failed blending: " + e);
            return bgLocation;
        }
    }
    public static ResourceLocation blendColorOnTexture(String bgPath, float targetR, float targetG, float targetB, float targetA, float bgFactor, float colorFactor) {
        ResourceLocation bgLocation = new ResourceLocation(bgPath);

        try {
            Minecraft mc = Minecraft.getMinecraft();

            InputStream bgStream = mc.getResourceManager().getResource(bgLocation).getInputStream();
            BufferedImage bgImage = ImageIO.read(bgStream);
            bgStream.close();

            int width = bgImage.getWidth();
            int height = bgImage.getHeight();
            BufferedImage combinedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            float totalFactor = bgFactor + colorFactor;
            if (totalFactor <= 0.0f) {
                totalFactor = 1.0f;
            }

            int fgR = Math.min(255, Math.max(0, (int) (targetR * 255.0f)));
            int fgG = Math.min(255, Math.max(0, (int) (targetG * 255.0f)));
            int fgB = Math.min(255, Math.max(0, (int) (targetB * 255.0f)));
            int fgA = Math.min(255, Math.max(0, (int) (targetA * 255.0f)));

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int bgPixel = bgImage.getRGB(x, y);

                    int bgA = (bgPixel >> 24) & 0xFF;
                    int bgR = (bgPixel >> 16) & 0xFF;
                    int bgG = (bgPixel >> 8) & 0xFF;
                    int bgB = bgPixel & 0xFF;

                    int finalA;
                    if (bgA > 0) {
                        finalA = Math.min(255, Math.max(0, (int) ((bgA * bgFactor + fgA * colorFactor) / totalFactor)));
                    } else {
                        finalA = 0;
                    }

                    int finalR = Math.min(255, Math.max(0, (int) ((bgR * bgFactor + fgR * colorFactor) / totalFactor)));
                    int finalG = Math.min(255, Math.max(0, (int) ((bgG * bgFactor + fgG * colorFactor) / totalFactor)));
                    int finalB = Math.min(255, Math.max(0, (int) ((bgB * bgFactor + fgB * colorFactor) / totalFactor)));

                    int blendedPixel = (finalA << 24) | (finalR << 16) | (finalG << 8) | finalB;
                    combinedImage.setRGB(x, y, blendedPixel);
                }
            }

            String cleanBg = bgPath.replace("/", "_").replace(".", "_");
            String dynamicName = "colorblend_" + cleanBg + "_" + fgR + "_" + fgG + "_" + fgB + "_" + fgA + "_" + bgFactor + "_" + colorFactor;

            DynamicTexture dynamicTexture = new DynamicTexture(combinedImage);
            return mc.getTextureManager().getDynamicTextureLocation(dynamicName, dynamicTexture);

        } catch (Exception e) {
            System.err.println("blendColorOnTexture failed blending: " + e);
            return bgLocation;
        }
    }
    public static ResourceLocation blendHexColorOnTexture(String bgPath, String hexColor, float bgFactor, float colorFactor) {
        ResourceLocation bgLocation = new ResourceLocation(bgPath);

        try {
            Minecraft mc = Minecraft.getMinecraft();

            InputStream bgStream = mc.getResourceManager().getResource(bgLocation).getInputStream();
            BufferedImage bgImage = ImageIO.read(bgStream);
            bgStream.close();

            int width = bgImage.getWidth();
            int height = bgImage.getHeight();
            BufferedImage combinedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            float totalFactor = bgFactor + colorFactor;
            if (totalFactor <= 0.0f) {
                totalFactor = 1.0f;
            }

            String cleanHex = hexColor.replace("#", "");
            int colorInt = Integer.parseInt(cleanHex, 16);

            int fgR = (colorInt >> 16) & 0xFF;
            int fgG = (colorInt >> 8) & 0xFF;
            int fgB = colorInt & 0xFF;
            int fgA = 255;

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int bgPixel = bgImage.getRGB(x, y);

                    int bgA = (bgPixel >> 24) & 0xFF;
                    int bgR = (bgPixel >> 16) & 0xFF;
                    int bgG = (bgPixel >> 8) & 0xFF;
                    int bgB = bgPixel & 0xFF;

                    int finalA = (bgA > 0) ? Math.min(255, Math.max(0, (int) ((bgA * bgFactor + fgA * colorFactor) / totalFactor))) : 0;
                    int finalR = Math.min(255, Math.max(0, (int) ((bgR * bgFactor + fgR * colorFactor) / totalFactor)));
                    int finalG = Math.min(255, Math.max(0, (int) ((bgG * bgFactor + fgG * colorFactor) / totalFactor)));
                    int finalB = Math.min(255, Math.max(0, (int) ((bgB * bgFactor + fgB * colorFactor) / totalFactor)));

                    int blendedPixel = (finalA << 24) | (finalR << 16) | (finalG << 8) | finalB;
                    combinedImage.setRGB(x, y, blendedPixel);
                }
            }

            String cleanBg = bgPath.replace("/", "_").replace(".", "_");
            String dynamicName = "hexblend_" + cleanBg + "_" + cleanHex + "_" + bgFactor + "_" + colorFactor;

            DynamicTexture dynamicTexture = new DynamicTexture(combinedImage);
            return mc.getTextureManager().getDynamicTextureLocation(dynamicName, dynamicTexture);

        } catch (Exception e) {
            System.err.println("blendHexColorOnTexture failed blending: " + e);
            return bgLocation;
        }
    }


    public static ResourceLocation maskTemplateWithPixelSourceByBrightness(String pixelSourcePath, String templatePath) {
        return maskTemplateWithPixelSourceByBrightness(pixelSourcePath, templatePath, 1F, 0F, -1, false);
    }
    public static ResourceLocation maskTemplateWithPixelSourceByBrightness(String pixelSourcePath, String templatePath, float bgFactor, float fgFactor) {
        return maskTemplateWithPixelSourceByBrightness(pixelSourcePath, templatePath, bgFactor, fgFactor, -1, false);
    }
    public static ResourceLocation maskTemplateWithPixelSourceByBrightness(String pixelSourcePath, String templatePath, float bgFactor, float fgFactor, int biomeTint) {
        return maskTemplateWithPixelSourceByBrightness(pixelSourcePath, templatePath, bgFactor, fgFactor, biomeTint, false);
    }
    public static ResourceLocation maskTemplateWithPixelSourceByBrightness(String pixelSourcePath, String templatePath, boolean reverseBrightness) {
        return maskTemplateWithPixelSourceByBrightness(pixelSourcePath, templatePath, 1F, 0F, -1, reverseBrightness);
    }
    public static ResourceLocation maskTemplateWithPixelSourceByBrightness(String pixelSourcePath, String templatePath, float bgFactor, float fgFactor, boolean reverseBrightness) {
        return maskTemplateWithPixelSourceByBrightness(pixelSourcePath, templatePath, bgFactor, fgFactor, -1, reverseBrightness);
    }
    public static ResourceLocation maskTemplateWithPixelSourceByBrightness(String pixelSourcePath, String templatePath, float bgFactor, float fgFactor, int biomeTint, boolean reverseBrightness) {
        ResourceLocation pixelSourceLocation = new ResourceLocation(pixelSourcePath);
        ResourceLocation templateLocation = new ResourceLocation(templatePath);

        try {
            Minecraft mc = Minecraft.getMinecraft();

            InputStream pixelSourceStream = TexturePacker.class.getClassLoader().getResourceAsStream(pixelSourcePath);
            if (pixelSourceStream == null) {
                pixelSourceStream = mc.getResourceManager().getResource(pixelSourceLocation).getInputStream();
            }
            BufferedImage pixelSourceImage = ImageIO.read(pixelSourceStream);
            pixelSourceStream.close();

            InputStream templateStream = mc.getResourceManager().getResource(templateLocation).getInputStream();
            BufferedImage templateImage = ImageIO.read(templateStream);
            templateStream.close();

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

                    if (biomeTint != -1) {
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

                    int pixelSourcePixel = getPixelSourcePixel(templateBrightnesses, currentTemplateBrightness, pixelSourcePalette, reverseBrightness);
                    int bgR = (pixelSourcePixel >> 16) & 0xFF;
                    int bgG = (pixelSourcePixel >> 8) & 0xFF;
                    int bgB = pixelSourcePixel & 0xFF;

                    int finalR = Math.min(255, Math.max(0, (int) ((bgR * bgFactor + fgR * fgFactor) / totalFactor)));
                    int finalG = Math.min(255, Math.max(0, (int) ((bgG * bgFactor + fgG * fgFactor) / totalFactor)));
                    int finalB = Math.min(255, Math.max(0, (int) ((bgB * bgFactor + fgB * fgFactor) / totalFactor)));

                    int blendedPixel = (templateA << 24) | (finalR << 16) | (finalG << 8) | finalB;
                    outputImage.setRGB(x, y, blendedPixel);
                }
            }

            String pixelSourceClean = pixelSourcePath.replace("/", "_").replace(".", "_");
            String cleanTemplate = templatePath.replace("/", "_").replace(".", "_");
            String dynamicName = "mask_brightness_tint_" + pixelSourceClean + "_" + cleanTemplate + "_" + biomeTint + "_" + bgFactor + "_" + fgFactor + "_" + reverseBrightness;

            DynamicTexture dynamicTexture = new DynamicTexture(outputImage);
            return mc.getTextureManager().getDynamicTextureLocation(dynamicName, dynamicTexture);

        } catch (Exception e) {
            System.err.println("maskTemplateWithPixelSourceByBrightness failed masking: " + e);
            return templateLocation;
        }
    }


    public static ResourceLocation maskTemplateWithPixelSource(String pixelSourcePath, String templatePath, float bgFactor, float fgFactor) {
        return maskTemplateWithPixelSource(pixelSourcePath, templatePath, bgFactor, fgFactor, -1);
    }
    public static ResourceLocation maskTemplateWithPixelSource(String pixelSourcePath, String templatePath, float bgFactor, float fgFactor, int biomeTint) {
        ResourceLocation pixelSourceLocation = new ResourceLocation(pixelSourcePath);
        ResourceLocation templateLocation = new ResourceLocation(templatePath);

        try {
            Minecraft mc = Minecraft.getMinecraft();

            InputStream pixelSourceStream = TexturePacker.class.getClassLoader().getResourceAsStream(pixelSourcePath);
            if (pixelSourceStream == null) {
                pixelSourceStream = mc.getResourceManager().getResource(pixelSourceLocation).getInputStream();
            }
            BufferedImage pixelSourceImage = ImageIO.read(pixelSourceStream);
            pixelSourceStream.close();

            InputStream templateStream = mc.getResourceManager().getResource(templateLocation).getInputStream();
            BufferedImage templateImage = ImageIO.read(templateStream);
            templateStream.close();

            int pixelSourceW = pixelSourceImage.getWidth();
            int pixelSourceH = pixelSourceImage.getHeight();
            int tempW = templateImage.getWidth();
            int tempH = templateImage.getHeight();

            float tintR = ((biomeTint >> 16) & 0xFF) / 255.0F;
            float tintG = ((biomeTint >> 8) & 0xFF) / 255.0F;
            float tintB = (biomeTint & 0xFF) / 255.0F;

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

                    int pixelSourceX = x % pixelSourceW;
                    int pixelSourceY = y % pixelSourceH;
                    int pixelSourcePixel = pixelSourceImage.getRGB(pixelSourceX, pixelSourceY);

                    int bgR = (pixelSourcePixel >> 16) & 0xFF;
                    int bgG = (pixelSourcePixel >> 8) & 0xFF;
                    int bgB = pixelSourcePixel & 0xFF;

                    if (biomeTint != -1) {
                        bgR = (int) (bgR * tintR);
                        bgG = (int) (bgG * tintG);
                        bgB = (int) (bgB * tintB);
                    }

                    int fgR = (templatePixel >> 16) & 0xFF;
                    int fgG = (templatePixel >> 8) & 0xFF;
                    int fgB = templatePixel & 0xFF;

                    int finalR = Math.min(255, Math.max(0, (int) ((bgR * bgFactor + fgR * fgFactor) / totalFactor)));
                    int finalG = Math.min(255, Math.max(0, (int) ((bgG * bgFactor + fgG * fgFactor) / totalFactor)));
                    int finalB = Math.min(255, Math.max(0, (int) ((bgB * bgFactor + fgB * fgFactor) / totalFactor)));

                    int blendedPixel = (templateA << 24) | (finalR << 16) | (finalG << 8) | finalB;
                    outputImage.setRGB(x, y, blendedPixel);
                }
            }

            String pixelSourceClean = pixelSourcePath.replace("/", "_").replace(".", "_");
            String cleanTemplate = templatePath.replace("/", "_").replace(".", "_");
            String dynamicName = "mask_tiled_tint_" + pixelSourceClean + "_" + cleanTemplate + "_" + biomeTint + "_" + bgFactor + "_" + fgFactor;

            DynamicTexture dynamicTexture = new DynamicTexture(outputImage);
            return mc.getTextureManager().getDynamicTextureLocation(dynamicName, dynamicTexture);

        } catch (Exception e) {
            System.err.println("maskTemplateWithPixelSource failed masking: " + e);
            return templateLocation;
        }
    }


    public static ResourceLocation getEmptyTransparentTexture() {
        try {
            Minecraft mc = Minecraft.getMinecraft();
            BufferedImage outputImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            outputImage.setRGB(0, 0, 0x00FFFFFF);
            String dynamicName = "empty_transparent_pixel_1x1";
            DynamicTexture dynamicTexture = new DynamicTexture(outputImage);
            return mc.getTextureManager().getDynamicTextureLocation(dynamicName, dynamicTexture);
        } catch (Exception e) {
            System.err.println("Failed creating empty transparent texture: " + e);
            return new ResourceLocation("missingno");
        }
    }


}
