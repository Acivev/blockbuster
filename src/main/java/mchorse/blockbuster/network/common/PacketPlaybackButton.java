package mchorse.blockbuster.network.common;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class PacketPlaybackButton implements IMessage
{
    public int mode;
    public String profile;

    public PacketPlaybackButton()
    {}

    public PacketPlaybackButton(int mode, String profile)
    {
        this.mode = mode;
        this.profile = profile;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.mode = buf.readInt();
        this.profile = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.mode);
        ByteBufUtils.writeUTF8String(buf, this.profile);
    }
}
