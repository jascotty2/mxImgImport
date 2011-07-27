package de.yahoo.chrimarti7.mxImgImport;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

public class mxImgImportPlayerListener extends PlayerListener{

	HashMap<Player, mxImgImportState> m_pMap;
	mxImgImport						  m_pPlugin;
	
	public mxImgImportPlayerListener(mxImgImport pPlugin)
	{
		m_pMap = new HashMap<Player, mxImgImportState>();
		m_pPlugin = pPlugin;
	}
	
	@Override
	public void onPlayerInteract(PlayerInteractEvent event) 
	{
		if (event.getAction() == Action.LEFT_CLICK_BLOCK)
		{
			if (!m_pMap.containsKey(event.getPlayer()))
			{
				
			}
			else
			{
				if (m_pMap.get(event.getPlayer()).loc1 == null)
				{
					m_pMap.get(event.getPlayer()).loc1 = event.getClickedBlock().getLocation();
					event.getPlayer().sendMessage("Please Left-Click Block number 2");
					return;
				}
				else if (m_pMap.get(event.getPlayer()).loc2 == null)
				{
					m_pMap.get(event.getPlayer()).loc2 = event.getClickedBlock().getLocation();
					mxImgImportState s= m_pMap.get(event.getPlayer());
					m_pPlugin.GetCommandManager().ExecuteCommand(new mxImgImportCommand(s.loc1,s.loc2),s.cs,s.c,s.Label,s.args);
					
				}
			}
		}

	}




}
