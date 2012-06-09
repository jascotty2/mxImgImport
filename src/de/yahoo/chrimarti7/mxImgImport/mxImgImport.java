//---------------------------------------------------------------------------
package de.yahoo.chrimarti7.mxImgImport;

//---------------------------------------------------------------------------
import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.*;

//---------------------------------------------------------------------------
public class mxImgImport extends JavaPlugin {

	static String m_FullVersionName = "mxImgImport v1.4";
	private mxCommandManager m_pCommandManager;
	private mxImgImportPlayerListener m_pPlayerListener;

	@Override
	public void onEnable() {
		m_FullVersionName = super.getDescription().getFullName();

		m_pCommandManager = new mxCommandManager(this);
		m_pPlayerListener = new mxImgImportPlayerListener(this);

		getServer().getPluginManager().registerEvents(m_pPlayerListener, this);

		File pFile = new File("plugins/mxImgImport/");
		if (!pFile.exists()) {
			pFile.mkdir();
		}

		ImageDrawer.log = 
				mxCommandManager.log = 
				mxImgImportCommand.log = getLogger();
	}

	@Override
	public void onDisable() {
	}

	static public void SendHelpText(CommandSender pPlayer) {
		pPlayer.sendMessage(ChatColor.GRAY + m_FullVersionName + " help ------------------------");
		pPlayer.sendMessage(ChatColor.YELLOW + "/imgimport file [Filename]" + ChatColor.GRAY + "   Imports the Image.");
		pPlayer.sendMessage(ChatColor.GRAY + "(file relative to the plugins/mxImgImport folder)");
		pPlayer.sendMessage(ChatColor.YELLOW + "/imgimport file [Filename] w" + ChatColor.GRAY + "   Use only wool blocks.");
		pPlayer.sendMessage(ChatColor.YELLOW + "/imgimport file [Filename] f" + ChatColor.GRAY + "   Import at file resolution.");
		//pPlayer.sendMessage(ChatColor.GRAY + "Infos at the other parameters see above.");
		pPlayer.sendMessage(ChatColor.YELLOW + "/imgimport undo" + ChatColor.GRAY + "    Undoes your last import.");
		//pPlayer.sendMessage(ChatColor.GRAY + "You can undo an import until the next time the Server is reloaded.");
		pPlayer.sendMessage(ChatColor.GRAY + "------------------------------------------------");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (label.equalsIgnoreCase("imgimport")) {
			// nur /imgimport
			if (args.length == 0 ) {
				SendHelpText(sender);
			} else if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "Must use as a Player");
			} else if (args[0].equalsIgnoreCase("file")) {

				String pic = ImageDrawer.getPicFilename(args[1]);
				
				if (pic == null) {
					sender.sendMessage(ChatColor.RED + "File \"" + args[1] + "\" not found in mxImgImport folder");
				} 
//				else if (pic.substring(pic.length() - 4).equalsIgnoreCase(".jpg")
//						|| pic.substring(pic.length() - 4).equalsIgnoreCase(".jpeg")) {
//					sender.sendMessage(ChatColor.RED + "Due to high compression artifacts, the JPG-Format isnt Supported.");
//				} 
				else {
					m_pPlayerListener.m_pMap.put((Player) sender, new mxImgImportState(sender, command, label, args));
					sender.sendMessage("Please Left-Click Block number 1");
				}
			} else if (args[0].equalsIgnoreCase("undo")) {
				m_pCommandManager.UndoLastCommand(sender, command, label, args);
			} else {
				String pic = ImageDrawer.getPicFilename(args[0]);
				if (pic == null) {
					sender.sendMessage(ChatColor.RED + "File \"" + args[1] + "\" not found in mxImgImport folder");
					SendHelpText(sender);
				} else {
					m_pPlayerListener.m_pMap.put((Player) sender, new mxImgImportState(sender, command, label, args));
					sender.sendMessage("Please Left-Click Block number 1");
				}
			}

			return true;
		} else {
			return false;
		}
	}

	mxCommandManager GetCommandManager() {
		return m_pCommandManager;
	}
}
