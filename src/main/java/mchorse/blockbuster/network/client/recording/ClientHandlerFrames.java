package mchorse.blockbuster.network.client.recording;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mchorse.blockbuster.common.ClientProxy;
import mchorse.blockbuster.network.client.ClientMessageHandler;
import mchorse.blockbuster.network.common.recording.PacketFramesLoad;
import mchorse.blockbuster.recording.data.Record;
import net.minecraft.client.entity.EntityPlayerSP;

/**
 * Client handler frames
 *
 * This client handler is responsible for inserting a record received from the
 * server into client's record repository (record manager).
 */
public class ClientHandlerFrames extends ClientMessageHandler<PacketFramesLoad>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketFramesLoad message)
    {
        Record record = new Record(message.filename);
        record.frames = message.frames;

        ClientProxy.manager.records.put(message.filename, record);
    }
}