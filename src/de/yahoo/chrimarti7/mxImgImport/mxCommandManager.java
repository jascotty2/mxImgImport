package de.yahoo.chrimarti7.mxImgImport;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class mxCommandManager 
{
	/*
	 * Referenz auf den Logger
	 */
	private static Logger log = Logger.getLogger("Minecraft");
	
	private HashMap<CommandSender, LinkedList<mxCommand>> m_pPreviousCommands;
		
	private mxImgImport m_pPlugin;
	
	static PermissionHandler m_pPermissionHandler;
	
	public mxCommandManager(mxImgImport pPlugin) 
	{
		m_pPlugin = pPlugin;
		m_pPreviousCommands = new HashMap<CommandSender, LinkedList<mxCommand>>();
		m_pPermissionHandler = null;
		Plugin permissionsPlugin = m_pPlugin.getServer().getPluginManager().getPlugin("Permissions");

	      if (m_pPermissionHandler == null) {
	          if (permissionsPlugin != null) {
	              m_pPermissionHandler = ((Permissions) permissionsPlugin).getHandler();
	          } else {
	              log.info("Permission system not detected, defaulting to OP");
	          }
	      }
	}
	
	LinkedList<mxCommand> GetPlayerCommandlist (CommandSender pSender)
	{
		LinkedList<mxCommand> ret = null;
	
		ret = m_pPreviousCommands.get(pSender);
		if (ret == null)
		{
			LinkedList<mxCommand> nList = new LinkedList<mxCommand>();
			m_pPreviousCommands.put(pSender, nList);
			ret = m_pPreviousCommands.get(pSender);
		}
	
		
		return ret;
		
	}
	
	void ExecuteCommand (mxCommand pCommand, CommandSender sender, Command command,
			String label, String[] args)
	{
		if (pCommand == null)
		{
			return;
		}
		if (m_pPermissionHandler != null)
		{
			if (!m_pPermissionHandler.has((Player) sender, pCommand.GetPermissionNodeName()))
			{
				sender.sendMessage(ChatColor.RED + "You don't have Permissions to do that.");
				return;
			}
		}
		else
		{
			if (!((Player)sender).isOp() && pCommand.IsOPOnly())
			{
				sender.sendMessage(ChatColor.RED + "This command is OP only.");
				return;
			}
		}
		
		
		log.info("Executing Command " + pCommand.GetPresentationName());
		// Kommando zur Liste hinzufügen
		if (pCommand.Do(sender, command, label, args))
			GetPlayerCommandlist(sender).add(pCommand);
		
		
	}
	
	void UndoLastCommand(CommandSender sender, Command command,
			String label, String[] args)
	{	
		if (GetPlayerCommandlist(sender).isEmpty())
		{
			sender.sendMessage("There is nothing to undo.");
			return;
		}
		
	
		if (GetPlayerCommandlist(sender).peekLast().CanUndo())
		{
			log.info("Executing Command " + GetPlayerCommandlist(sender).peekLast().GetPresentationName());
			GetPlayerCommandlist(sender).peekLast().Undo(sender, command, label, args);
			GetPlayerCommandlist(sender).removeLast();
		}
		else
			sender.sendMessage("The Command " + GetPlayerCommandlist(sender).element().GetPresentationName() + " is not undoable");
	}
}
