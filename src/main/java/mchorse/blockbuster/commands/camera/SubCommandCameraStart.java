package mchorse.blockbuster.commands.camera;

import mchorse.blockbuster.common.ClientProxy;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Camera's sub-command /camera start
 *
 * This sub-command is responsible for starting current camera profile.
 */
public class SubCommandCameraStart extends CommandBase
{
    @Override
    public String getCommandName()
    {
        return "start";
    }

    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "blockbuster.commands.camera.start";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        ClientProxy.profileRunner.start();
        sender.addChatMessage(new TextComponentTranslation("blockbuster.info.profile.start"));
    }
}
