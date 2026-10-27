package de.maxhenkel.camerautils.config;

import de.maxhenkel.configbuilder.ConfigBuilder;
import de.maxhenkel.configbuilder.entry.ConfigEntry;

public class ClientConfig {

    public static final double MAX_ZOOM = 0.1D;
    public static final double MIN_ZOOM = 1.5D;

    public final ConfigEntry<Double> smoothness;
    public final ConfigEntry<Double> minSmoothValue;
    public final ConfigEntry<Double> maxSmoothValue;
    public final ConfigEntry<Double> cinematicCameraModifier;
    public final ConfigEntry<Double> zoom;
    public final ConfigEntry<Double> zoomSensitivity;
    public final ConfigEntry<ModifierKey> modifierKey;
    public final ConfigEntry<Double> guiOpacity;
    public final ConfigEntry<Double> zoomAnimationFrom;
    public final ConfigEntry<Double> zoomAnimationTo;
    public final ConfigEntry<Integer> zoomAnimationDuration;

    public static int thirdPersonCam = -1;

    // Detached Camera
    public static boolean detached;
    public static float xRot, yRot;
    public static double x, y, z;

    public ClientConfig(ConfigBuilder builder) {
        smoothness = builder.doubleEntry("smoothness", 0D, 0D, 1D);
        minSmoothValue = builder.doubleEntry("min_smoothness", 40D, 1D, 1000D);
        maxSmoothValue = builder.doubleEntry("max_smoothness", 100D, 1D, 1000D);
        cinematicCameraModifier = builder.doubleEntry("cinematic_camera_modifier", 1D, 0D, 1D);
        zoom = builder.doubleEntry("zoom", 0.1D, 0D, 2D);
        zoomSensitivity = builder.doubleEntry("zoom_sensitivity", 0.01D, 0.001D, 1D);
        modifierKey = builder.enumEntry(
                "modifier_key",
                ModifierKey.RIGHT_ALT,
                "Possible values are: LEFT_CTRL, RIGHT_CTRL, CTRL, LEFT_ALT, RIGHT_ALT, ALT, LEFT_SHIFT, RIGHT_SHIFT, SHIFT"
        );
        guiOpacity = builder.doubleEntry("gui_opacity", 1D, 0D, 1D);
        zoomAnimationFrom = builder.doubleEntry("zoom_animation_from", 1D, 0.01D, 2D);
        zoomAnimationTo = builder.doubleEntry("zoom_animation_to", 0.1D, 0.01D, 2D);
        zoomAnimationDuration = builder.integerEntry("zoom_animation_duration", 200, 1, Integer.MAX_VALUE);
    }

    public static enum ModifierKey {
        LEFT_CTRL, RIGHT_CTRL, CTRL, LEFT_ALT, RIGHT_ALT, ALT, LEFT_SHIFT, RIGHT_SHIFT, SHIFT
    }

}
