package de.maxhenkel.camerautils.mixin;

import de.maxhenkel.camerautils.CameraUtils;
import de.maxhenkel.camerautils.config.ClientConfig;
import net.minecraft.client.Camera;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow
    private float xRot;
    @Shadow
    private float yRot;

    @Redirect(method = "alignWithEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;move(FFF)V", ordinal = 0))
    private void move(Camera instance, float f, float g, float h) {
        if (ClientConfig.detached) {
            //Don't move with detached cam
        } else {
            move(f, g, h);
        }
    }



    @Inject(method = "alignWithEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setPosition(DDD)V", shift = At.Shift.AFTER))
    private void setPosition(CallbackInfo ci) {
        if (ClientConfig.detached) {
            setRotation(ClientConfig.yRot, ClientConfig.xRot);
            setPosition(ClientConfig.x, ClientConfig.y, ClientConfig.z);
        }
    }

    @Shadow
    protected abstract void move(float d, float e, float f);

    @Shadow
    protected abstract void setRotation(float f, float g);

    @Shadow
    protected abstract void setPosition(double d, double e, double f);

}
