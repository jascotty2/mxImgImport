package de.yahoo.chrimarti7.mxImgImport;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class mxImgImportState {

	CommandSender cs;
	Command c;
	String Label;
	String[]args;
	Location loc1;
	Location loc2;
	
	mxImgImportState(CommandSender ccs, Command cc, String cLabel, String[]cargs)
	{
		cs = ccs;
		c = cc;
		Label = cLabel;
		args = cargs;
		loc1 = null;
		loc2 = null;
		
	}
}
