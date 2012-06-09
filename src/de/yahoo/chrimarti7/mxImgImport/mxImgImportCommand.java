package de.yahoo.chrimarti7.mxImgImport;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class mxImgImportCommand implements mxCommand {
	
	static Logger log = null;

	Location m_Loc1, m_Loc2;
	Map<Location, MatM> m_pUndoMap = new HashMap<Location, MatM>();

	public mxImgImportCommand(Location loc1, Location loc2) {
		m_Loc1 = loc1;
		m_Loc2 = loc2;
	}

	@Override
	public void Undo(CommandSender sender, Command command, String label, String[] args) {
		for(Entry<Location, MatM> cur : m_pUndoMap.entrySet()) {
			cur.getKey().getBlock().setType(cur.getValue().mat);
			cur.getKey().getBlock().setData((byte) cur.getValue().ss);
		}
	}

	@Override
	public boolean Do(CommandSender sender, Command command, String label, String[] args) {

		if (args.length < 2) {
			mxImgImport.SendHelpText(sender);
			return false;
		}
		if (sender == null) {
			if (log != null)
			log.info(ChatColor.RED + "No sender instance");
			return false;
		}
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You cannot use this command from commandline!");
			return false;
		}

		if (!m_Loc1.getWorld().getName().equals(m_Loc2.getWorld().getName())) {
			sender.sendMessage(ChatColor.RED + "The 2 Points must be in the same World.");
			return false;
		}

		int UnitX = Sign(m_Loc2.getBlockX() - m_Loc1.getBlockX());
		int UnitY = Sign(m_Loc2.getBlockY() - m_Loc1.getBlockY());
		int UnitZ = Sign(m_Loc2.getBlockZ() - m_Loc1.getBlockZ());

		if (!(UnitX == 0 || UnitY == 0 || UnitZ == 0)) {
			sender.sendMessage(ChatColor.RED + "The Selection Points have to lie on a Plane");
			return false;
		}

		int NumZero = 0;
		if (UnitX == 0) {
			NumZero += 1;
		}
		if (UnitY == 0) {
			NumZero += 1;
		}
		if (UnitZ == 0) {
			NumZero += 1;
		}

		if (NumZero != 1) {
			sender.sendMessage(ChatColor.RED + "The Selection Points have to lie on a 2D Plane");
			return false;
		}
		
		boolean allBlocks = true, scaleImage = true;
		
		if(args.length >= 3) {
			for(int i = 2; i < args.length; ++i) {
				if(args[i].toLowerCase().charAt(0) == 'w') allBlocks = false;
				else if(args[i].toLowerCase().charAt(0) == 'f') scaleImage = false;
			}
		}
		
		return ImageDrawer.Draw(sender, m_Loc1, m_Loc2,  args[1], allBlocks, scaleImage, m_pUndoMap);
	}

	private int Sign(double x) {
		if (x > 0) {
			return 1;
		} else if (x == 0) {
			return 0;
		} else {
			return -1;
		}
	}

	@Override
	public boolean CanUndo() {
		return true;
	}

	@Override
	public String GetPresentationName() {
		return "Image Import";
	}

	@Override
	public String GetUndoPresentationName() {
		return "Image Import undo";
	}

	@Override
	public String GetPermissionNodeName() {
		return "mxImgImport.Import";
	}

	@Override
	public boolean IsOPOnly() {
		return true;
	}
}
