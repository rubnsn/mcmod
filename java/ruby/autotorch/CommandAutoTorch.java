package ruby.autotorch;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandAutoTorch extends CommandBase
{
    private TickHandler tick_handler;
    private List options;
    CommandAutoTorch(TickHandler tick_handler)
    {
        this.tick_handler = tick_handler;
        options = new ArrayList();
        options.add("start");
        options.add("stop");
    }

    @Override
    public String getCommandName()
    {
        return "autotorch";
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2)
    {
        if (var2.length > 0)
        {
            if (var2[0].equalsIgnoreCase("start"))
            {
                tick_handler.setPlayer(MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(var1.getCommandSenderName()));
                notifyAdmins(var1, 1, "commands.autotorch.success", "start");
            }
            else if (var2[0].equalsIgnoreCase("stop"))
            {
                tick_handler.setPlayer(null);
                notifyAdmins(var1, 1, "commands.autotorch.success", "stop");
            }
        }
    }
    @Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }
    @Override
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        return par2ArrayOfStr.length == 1 ? options : null;
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender)
    {
        return "AutoTorch";
    }
}
