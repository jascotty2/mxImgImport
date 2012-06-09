package de.yahoo.chrimarti7.mxImgImport;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface mxCommand {

	void Undo(CommandSender sender, Command command,
			String label, String[] args);

	boolean Do(CommandSender sender, Command command,
			String label, String[] args);

	boolean CanUndo();

	String GetPresentationName();

	String GetUndoPresentationName();

	String GetPermissionNodeName();

	boolean IsOPOnly();
}