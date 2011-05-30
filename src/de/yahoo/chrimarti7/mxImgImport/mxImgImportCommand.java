package de.yahoo.chrimarti7.mxImgImport;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class mxImgImportCommand implements mxCommand
{
	/*
	 * Reference to the Logfile
	 */
	private static Logger log = Logger.getLogger("Minecraft");

	class MatM
	{
		MatM(Material m, int s)
		{
			mat = m;
			ss = s;
		}
		
		Material mat;
		int ss;
	}
	
	class RGB
	{
		
		RGB(Color a)
		{
			r = a.getRed();
			g = a.getGreen();
			b = a.getBlue();
		}
		
		public RGB(int red, int green, int blue) {
			// TODO Auto-generated constructor stub
			r = red;
			g = green;
			b = blue;
		}

		int getRed(){
			return r;
		}
		
		int getGreen(){
			return g;
		}
		
		int getBlue(){
			return b;
		}
		
		int r;
		int g;
		int b;
	}

	Location m_Loc1;
	Location m_Loc2;
	
	HashMap<Location,MatM> m_pUndoMap;
	
	public mxImgImportCommand(Location loc1, Location loc2) 
	{
		m_Loc1 = loc1;
		m_Loc2 = loc2;
	}

	@Override
	public void Undo(CommandSender sender, Command command, String label,
			String[] args) 
	{
		Iterator<Entry<Location, MatM>> It = m_pUndoMap.entrySet().iterator();
		
		while (It.hasNext())
		{
			Entry<Location, MatM> cur = It.next();
			cur.getKey().getBlock().setType(cur.getValue().mat);
			cur.getKey().getBlock().setData((byte) cur.getValue().ss);
		}
		
		
		
		// 
		log.info("undo");
	}

	float ColorDistance(RGB a, RGB b)
	{
		int most = 0;
		if (a.getRed() > a.getGreen() && a.getRed()>a.getBlue())
			most = 1;
		else if (a.getGreen() > a.getRed() && a.getGreen()>a.getBlue())
			most = 2;
		else	most = 3;
		
		RGB c = new RGB(b.getRed()-a.getRed(),b.getGreen()-a.getGreen(), b.getBlue()-a.getBlue());
		float clen = c.getRed()*c.getRed()*(most ==1?1:2)+c.getGreen()*c.getGreen()*(most ==2?1:2)+c.getBlue()*c.getBlue()*(most ==3?1:2);
	
		return clen;
	}
	
	@Override
	public boolean Do(CommandSender sender, Command command, String label,
			String[] args) {

		if (args.length != 2)
		{
			mxImgImport.SendHelpText(sender);
			return false;
		}
		if (sender == null)
		{
			log.info(ChatColor.RED + "No sender instance");
			return false;
		}
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + "You cannot use this command from commandline!");
			return false;
		}
		
		String Filename = args[1];
		
		if (Filename.substring(Filename.length()-4).equalsIgnoreCase(".jpg")||
				Filename.substring(Filename.length()-4).equalsIgnoreCase(".JPG"))
		{
			sender.sendMessage(ChatColor.RED + "Due to high compression artifacts, the JPG-Format isnt Supported.");
			return false;
		}
		
		if (m_Loc1.getWorld().getName() != m_Loc2.getWorld().getName())
		{
			sender.sendMessage(ChatColor.RED + "The 2 Points must be in the same World.");
			return false;
		}

		Location loc3 = new Location (m_Loc1.getWorld(), m_Loc2.getX()-m_Loc1.getX(), m_Loc2.getY()-m_Loc1.getY(),m_Loc2.getZ()-m_Loc1.getBlockZ());
		int UnitX = Sign(loc3.getX());
		int UnitY = Sign(loc3.getY());
		int UnitZ = Sign(loc3.getZ());
		
		if (!(UnitX == 0 || UnitY == 0 || UnitZ == 0))
		{
			sender.sendMessage(ChatColor.RED + "The Selection Points have to lie on a Plane");
			return false;
		}
		
		int NumZero = 0;
		if (UnitX == 0)
			NumZero += 1;
		if (UnitY == 0)
			NumZero += 1;
		if (UnitZ == 0)
			NumZero += 1;
		
		if (NumZero != 1)
		{
			sender.sendMessage(ChatColor.RED + "The Selection Points have to lie on a 2D Plane");
			return false;
		}
		
	
		BufferedImage pImageFile = null;

		try 
		{
			pImageFile = ImageIO.read(new File("plugins/mxImgImport/"+Filename));
		} 
		catch (IOException e) 
		{
			sender.sendMessage(ChatColor.RED + "File was not found.");
			return false;
		}
		
		HashMap<RGB, MatM> pBlockMap = new HashMap<RGB, MatM>();
		pBlockMap.put(new RGB(160,160,160), new MatM(Material.STONE, 0));
		pBlockMap.put(new RGB(188,152,98), new MatM(Material.WOOD, 0));
		pBlockMap.put(new RGB(255,255,255), new MatM(Material.WOOL, 0));
		pBlockMap.put(new RGB(214,207,154),new MatM( Material.SANDSTONE, 0));
		pBlockMap.put(new RGB(196,86,205), new MatM(Material.WOOL,1));
		pBlockMap.put(new RGB(114,147,215), new MatM(Material.WOOL, 2));
		pBlockMap.put(new RGB(33,200,214), new MatM(Material.WOOL, 3));
		pBlockMap.put(new RGB(210,210,14),new MatM( Material.WOOL, 4));
		pBlockMap.put(new RGB(100,230,0),new MatM( Material.WOOL, 5));
		pBlockMap.put(new RGB(224,155,173),new MatM( Material.WOOL, 6));
		pBlockMap.put(new RGB(71,71,71),new MatM( Material.WOOL, 7));
		pBlockMap.put(new RGB(173,180,180),new MatM( Material.WOOL, 8));
		pBlockMap.put(new RGB(43,129,166),new MatM( Material.WOOL, 9));
		pBlockMap.put(new RGB(90,0,90),new MatM( Material.WOOL, 10));
		pBlockMap.put(new RGB(40,53,161),new MatM( Material.WOOL, 11));
		pBlockMap.put(new RGB(93,56,30),new MatM( Material.WOOL, 12));
		pBlockMap.put(new RGB(57,78,25),new MatM( Material.WOOL, 13));
		pBlockMap.put(new RGB(250,0,0),new MatM( Material.WOOL, 14));
		pBlockMap.put(new RGB(0,0,0),new MatM( Material.WOOL, 15));
		pBlockMap.put(new RGB(150,125,70),new MatM( Material.LOG, 0));
		pBlockMap.put(new RGB(57,46,28),new MatM( Material.LOG, 1));
		pBlockMap.put(new RGB(255,251,93),new MatM( Material.GOLD_BLOCK, 0));
		pBlockMap.put(new RGB(213,213,213),new MatM( Material.IRON_BLOCK, 0));
		pBlockMap.put(new RGB(176,176,176),new MatM( Material.DOUBLE_STEP, 0));
		pBlockMap.put(new RGB(46,46,61),new MatM( Material.OBSIDIAN, 0));
		pBlockMap.put(new RGB(120,108,30), new MatM(Material.CHEST, 0));
		//pBlockMap.put(new RGB(35,80,35),new MatM( Material.MOSSY_COBBLESTONE, 0));
		pBlockMap.put(new RGB(160,240,240),new MatM( Material.DIAMOND_BLOCK, 0));
//		pBlockMap.put(new RGB(100,56,56),new MatM( Material.NETHERRACK, 0));
	//	pBlockMap.put(new RGB(200,0,200),new MatM( Material.GLOWSTONE, 0));
		
		
		if (pImageFile.getWidth() > 100 || pImageFile.getHeight() > 100)
		{
			sender.sendMessage(ChatColor.RED + "Image is to big");
			return false;
		}
		
		// Send a message to the player, telling him, that the import starts.
		sender.sendMessage(ChatColor.GOLD+"Importing Image '"+Filename+"' (" + Integer.toString(pImageFile.getWidth())+"x"+Integer.toString(pImageFile.getHeight())+")");
		log.info("Importing Image '"+Filename+"' (" + Integer.toString(pImageFile.getWidth())+"x"+Integer.toString(pImageFile.getHeight())+")");

		m_pUndoMap = new HashMap<Location,MatM>();
		
		// Iterate through the pixels
		for (int y = 0; y<pImageFile.getHeight(); y++)
		{
			for (int x = 0; x<pImageFile.getWidth(); x++)
			{
				Location curpos = m_Loc1.clone();

				if (UnitX == 0)
				{
					curpos.setZ(curpos.getZ()+UnitZ*x);
					curpos.setY(curpos.getY()+UnitY*y);
				}
				else if (UnitY == 0)
				{
					curpos.setZ(curpos.getZ()+UnitZ * y);
					curpos.setX(curpos.getX()+UnitX * x);
				}
				else
				{
					curpos.setX(curpos.getX()+UnitX*x);
					curpos.setY(curpos.getY()+UnitY*y);
				}
				// 
				int pixel = pImageFile.getRGB(pImageFile.getWidth()-x-1, pImageFile.getHeight()-y-1);
				int alpha = (pixel >> 24) & 0xff;
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;

				if (alpha != 0 && curpos.getBlock() != null)
				{
					RGB c = new RGB(red, green, blue);

					Set<Entry<RGB, MatM>> pSet = pBlockMap.entrySet();
					Iterator<Entry<RGB, MatM>> It = pSet.iterator();

					float bestDist = 100000000;
					RGB col = null;

					while(It.hasNext())
					{
						Entry<RGB, MatM> Current = It.next();
						float dist = ColorDistance(c,(Current.getKey()));

						if (dist < bestDist)
						{
							bestDist = dist;
							col = Current.getKey();
						}
					}
					
					m_pUndoMap.put(curpos,new MatM(curpos.getBlock().getType(), curpos.getBlock().getData()));

					curpos.getBlock().setType(pBlockMap.get(col).mat);
					curpos.getBlock().setData((byte)pBlockMap.get(col).ss);
					
					
				}
				else
				{
					
				}
			}
		}

		return true;
	}

	private int Sign(double x)
	{
		if (x > 0)
			return 1;
		else if (x == 0)
			return 0;
		else
			return -1;
	}

	@Override
	public boolean CanUndo()
	{
		return true;
	}

	@Override
	public String GetPresentationName() 
	{
		return "Image Import";
	}

	@Override
	public String GetUndoPresentationName() 
	{
		return "Image Import undo";
	}

	public String GetPermissionNodeName()
	{
		return "mxImgImport.Import";
	}
	
	public boolean IsOPOnly()
	{
		return true;
	}
}
