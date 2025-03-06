package com.vasconcellos.voxybases.cmd.registry;

import com.vasconcellos.voxybases.VoxyBases;
import com.vasconcellos.voxybases.cmd.base.BaseCmd;
import com.vasconcellos.voxybases.cmd.base.DefaultCmd;
import com.vasconcellos.voxybases.cmd.base.admin.CreateCmd;
import com.vasconcellos.voxybases.cmd.base.admin.DeleteCmd;
import com.vasconcellos.voxybases.cmd.base.banner.BannerSetCmd;
import com.vasconcellos.voxybases.cmd.base.gate.GateSetCmd;
import com.vasconcellos.voxybases.cmd.base.safe.SafeSetCmd;
import com.vasconcellos.voxybases.cmd.base.spawn.SpawnSetCmd;
import com.vasconcellos.voxybases.cmd.safe.SafeCmd;
import me.saiintbrisson.bukkit.command.BukkitFrame;

public class CommandRegistry {

    public static void enable(VoxyBases plugin) {
        BukkitFrame frame = new BukkitFrame(plugin);

        frame.registerCommands(
                new CreateCmd(plugin),
                new DeleteCmd(plugin),
                new BaseCmd(plugin),
                new SafeSetCmd(plugin),
                new SpawnSetCmd(plugin),
                new GateSetCmd(plugin),
                new DefaultCmd(plugin),
                new BannerSetCmd(plugin),
                new SafeCmd()
        );
    }
}