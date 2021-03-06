package mchorse.blockbuster.recording.actions;

import mchorse.blockbuster.recording.data.Frame;
import mchorse.blockbuster.utils.EntityUtils;
import mchorse.metamorph.api.morphs.AbstractMorph;
import net.minecraft.entity.EntityLivingBase;

/**
 * Morph's action action
 *
 * This method is responsible for executing morph's action, if it has one.
 */
public class MorphActionAction extends Action
{
    public MorphActionAction()
    {}

    @Override
    public void apply(EntityLivingBase actor)
    {
        AbstractMorph morph = mchorse.metamorph.api.EntityUtils.getMorph(actor);

        if (morph != null)
        {
            Frame frame = EntityUtils.getRecordPlayer(actor).getCurrentFrame();

            if (frame == null) return;

            float yaw = actor.rotationYaw;
            float yawHead = actor.rotationYaw;
            float pitch = actor.rotationPitch;

            float prevYaw = actor.prevRotationYaw;
            float prevYawHead = actor.prevRotationYawHead;
            float prevPitch = actor.prevRotationPitch;

            actor.rotationYaw = actor.prevRotationYaw = frame.yaw;
            actor.rotationYawHead = actor.prevRotationYawHead = frame.yawHead;
            actor.rotationPitch = actor.prevRotationPitch = frame.pitch;

            morph.action(actor);

            actor.rotationYaw = yaw;
            actor.rotationYawHead = yawHead;
            actor.rotationPitch = pitch;

            actor.prevRotationYaw = prevYaw;
            actor.prevRotationYawHead = prevYawHead;
            actor.prevRotationPitch = prevPitch;
        }
    }
}