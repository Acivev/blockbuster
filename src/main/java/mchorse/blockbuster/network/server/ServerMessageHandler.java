package mchorse.blockbuster.network.server;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import mchorse.blockbuster.network.AbstractMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * This class passes operation from Netty to Minecraft (Server) Thread. This
 * class will prevent the client-side message handling method from appearing in
 * server message handler classes.
 *
 * @author Ernio (Ernest Sadowski)
 */
public abstract class ServerMessageHandler<T extends IMessage> extends AbstractMessageHandler<T>
{
    public abstract void run(final EntityPlayerMP player, final T message);

    @Override
    public IMessage handleServerMessage(final EntityPlayerMP player, final T message)
    {
        this.run(player, message);

        return null;
    }

    @Override
    public final IMessage handleClientMessage(final T message)
    {
        return null;
    }
}