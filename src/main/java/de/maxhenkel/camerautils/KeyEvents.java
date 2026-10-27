package de.maxhenkel.camerautils;

import com.mojang.blaze3d.platform.InputConstants;
import de.maxhenkel.camerautils.config.ClientConfig;
import de.maxhenkel.camerautils.gui.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.lwjgl.sdl.SDLScancode;

public class KeyEvents {

    private static final Minecraft mc = Minecraft.getInstance();

    public KeyEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (mc.player == null) {
                return;
            }
            if (CameraUtils.ZOOM.consumeClick() && isModifierDown()) {
                openZoomSettings();
            }
            if (CameraUtils.DETACH_CAMERA.consumeClick()) {
                toggleDetachCamera();
            }
            if (CameraUtils.ZOOM_ANIMATION.consumeClick()) {
                if (isModifierDown()) {
                    openZoomAnimationSettings();
                } else {
                    if (CameraUtils.ZOOM_TRACK == null) {
                        CameraUtils.ZOOM_TRACK = new ZoomTrack(CameraUtils.CLIENT_CONFIG.zoomAnimationFrom.get().floatValue(), CameraUtils.CLIENT_CONFIG.zoomAnimationTo.get().floatValue(), CameraUtils.CLIENT_CONFIG.zoomAnimationDuration.get());
                    } else {
                        CameraUtils.ZOOM_TRACK = null;
                    }
                }
            }

            if (CameraUtils.CINEMATIC_CAMERA_GUI.consumeClick()) {
                openCinematicCameraSettings();
            }
            if (CameraUtils.ZOOM_GUI.consumeClick()) {
                openZoomSettings();
            }
            if (CameraUtils.ZOOM_ANIMATION_GUI.consumeClick()) {
                openZoomAnimationSettings();
            }
        });
    }

    public boolean onScroll(double amount) {
        if (CameraUtils.ZOOM.isDown()) {
            double zoom = CameraUtils.CLIENT_CONFIG.zoom.get();
            double zoomSensitivity = CameraUtils.CLIENT_CONFIG.zoomSensitivity.get();
            zoom = Math.max(0D, Math.min(2D, zoom + (-amount * zoomSensitivity)));
            CameraUtils.CLIENT_CONFIG.zoom.set(zoom);
            CameraUtils.CLIENT_CONFIG.zoom.save();
            mc.player.sendOverlayMessage(Component.translatable("message.camerautils.zoom", Math.round((1D - zoom) * 100D)));
            return true;
        }
        return false;
    }

    public boolean onSmoothCameraClick() {
        if (isModifierDown()) {
            openCinematicCameraSettings();
            return true;
        }

        return false;
    }

    private void toggleDetachCamera() {
        ClientConfig.detached = !ClientConfig.detached;

        if (ClientConfig.detached) {
            ClientConfig.thirdPersonCam = -1;
            mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);

            if (!isModifierDown() || Math.sqrt(mc.player.distanceToSqr(ClientConfig.x, ClientConfig.y, ClientConfig.z)) > 100F) {
                ClientConfig.xRot = mc.player.getViewXRot(0F);
                ClientConfig.yRot = mc.player.getViewYRot(0F);
                ClientConfig.x = mc.player.getX();
                ClientConfig.y = mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose());
                ClientConfig.z = mc.player.getZ();
            }
        } else {
            mc.options.setCameraType(CameraType.FIRST_PERSON);
        }
        checkPostEffect();
    }

    public void checkPostEffect() {
        mc.gameRenderer.checkEntityPostEffect(mc.options.getCameraType().isFirstPerson() ? mc.getCameraEntity() : null);
    }

    public void openCinematicCameraSettings() {
        mc.gui.setScreen(new CinematicCameraScreen());
    }


    public void openZoomSettings() {
        mc.gui.setScreen(new ZoomScreen());
    }

    public void openZoomAnimationSettings() {
        mc.gui.setScreen(new ZoomAnimationScreen());
    }

    public static boolean isModifierDown() {
        ClientConfig.ModifierKey modifierKey = CameraUtils.CLIENT_CONFIG.modifierKey.get();
        return switch (modifierKey) {
            case LEFT_CTRL -> InputConstants.isKeyDown(SDLScancode.SDL_SCANCODE_LCTRL);
            case RIGHT_CTRL -> InputConstants.isKeyDown(SDLScancode.SDL_SCANCODE_RCTRL);
            case CTRL -> mc.hasControlDown();
            case LEFT_ALT -> InputConstants.isKeyDown(SDLScancode.SDL_SCANCODE_LALT);
            case RIGHT_ALT -> InputConstants.isKeyDown(SDLScancode.SDL_SCANCODE_RALT);
            case ALT -> mc.hasAltDown();
            case LEFT_SHIFT -> InputConstants.isKeyDown(SDLScancode.SDL_SCANCODE_LSHIFT);
            case RIGHT_SHIFT -> InputConstants.isKeyDown(SDLScancode.SDL_SCANCODE_RSHIFT);
            case SHIFT -> mc.hasShiftDown();
        };
    }

}
