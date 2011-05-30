//---------------------------------------------------------------------------
package de.yahoo.chrimarti7.mxImgImport;

//---------------------------------------------------------------------------
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.*;

//---------------------------------------------------------------------------
public class mxImgImport extends JavaPlugin
{
	/*
	 * Version Nummer + String
	 */
	static final String m_FullVersionName = "mxImgImport v1.2";

	/*
	 * Reference to the Logfile
	 */
	private static Logger log = Logger.getLogger("Minecraft");

	/*
	 * Referenz auf den Commandmanager
	 */	
	private mxCommandManager	m_pCommandManager;

	private mxImgImportPlayerListener	m_pPlayerListener;
	
	@Override
	public void onDisable()
	{
		log.info(m_FullVersionName + " unloaded.");
	}

	@Override
	public void onEnable() 
	{
		// Erstellen
		m_pCommandManager = new mxCommandManager(this);
		m_pPlayerListener = new mxImgImportPlayerListener(this);
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_INTERACT, m_pPlayerListener,
				Event.Priority.Normal, this);
		
		log.info(m_FullVersionName + " loaded.");
	}

	static public void SendHelpText (CommandSender pPlayer)
	{
		pPlayer.sendMessage(ChatColor.GRAY+m_FullVersionName+" help ------------------------");
		pPlayer.sendMessage(ChatColor.YELLOW+"/imgimport file [Filename]" + ChatColor.GRAY + "   Imports the Image. [Filename] is the Filename");
		pPlayer.sendMessage(ChatColor.GRAY + "It is relative to the plugins/mxImgImport folder.");
		pPlayer.sendMessage(ChatColor.GRAY+"------------------------------------------------");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) 
	{
		if (label.equalsIgnoreCase("imgimport"))
		{
			// nur /imgimport
			if (args.length == 0)
			{
				SendHelpText(sender);
				return true;
			}
			else if (args[0].equalsIgnoreCase("file"))
			{
				m_pPlayerListener.m_pMap.put((Player)sender,new mxImgImportState(sender,command,label,args));
				sender.sendMessage("Please Left-Click Block number 1");
				return true;
			}
			else if (args[0].equalsIgnoreCase("undo"))
			{
				m_pCommandManager.UndoLastCommand(sender, command, label, args);
				return true;
			}
			
			else
			{
				SendHelpText(sender);
				return true;
			}
			
		}
		else
			return false;
	}
	
	mxCommandManager GetCommandManager()
	{
		return m_pCommandManager;
		
	}
	

}
