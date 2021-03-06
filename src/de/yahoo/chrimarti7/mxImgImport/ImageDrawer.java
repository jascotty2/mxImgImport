package de.yahoo.chrimarti7.mxImgImport;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

/**
 *
 * @author jascotty2
 */
public class ImageDrawer {

	static Map<RGB, MatM> colorsBlockSideMap = new HashMap<RGB, MatM>(),
			colorsBlockTopMap = new HashMap<RGB, MatM>();
	static Map<RGB, MatM> woolBlockMap = new HashMap<RGB, MatM>();
	static final int[] textureIdSideMap = new int[]{
		0, 1, 3, 0, 5, 43, 0, 45, 46, 0, 0, 0, 0, 0, 0, 0,
		4, 7, 12, 13, 17, 0, 42, 41, 57, 0, 0, 0, 0, 0, 0, 0,
		14, 15, 16, 47, 48, 49, 0, 0, 0, 0, 0, 0, 61, 61, 23, 0,
		19, 0, 56, 73, 0, 0, 98, 0, 0, 0, 0, 58, 58, 62, 0, 0,
		35, 0, 80, 79, 0, 0, 0, 0, 0, 0, 84, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 17, 17, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 98, 98, 0, 87, 88, 89, 29, 33, 33, 33, 0, 0,
		0, 35, 35, 0, 17, 17, 86, 86, 91, 0, 0, 0, 0, 100, 99, 0,
		0, 35, 35, 0, 0, 0, 0, 0, 103, 0, 0, 0, 0, 100, 99, 0,
		22, 35, 35, 0, 0, 0, 0, 0, 0, 17, 0, 0, 0, 0, 0, 0,
		21, 35, 35, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 121,
		0, 35, 35, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		24, 35, 35, 0, 0, 0, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 35, 35, 123, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		112, 35, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	static final int[] textureIdTopMap = new int[]{
		0, 1, 3, 0, 5, 43, 0, 45, 0, 46, 0, 0, 0, 0, 0, 0,
		4, 7, 12, 13, 0, 17, 42, 41, 57, 0, 0, 0, 0, 0, 0, 0,
		14, 15, 16, 0, 48, 49, 0, 0, 0, 0, 0, 58, 0, 0, 0, 0,
		19, 0, 56, 73, 0, 0, 98, 0, 0, 0, 0, 0, 0, 0, 61, 0,
		35, 0, 80, 79, 0, 0, 0, 0, 0, 0, 0, 84, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 98, 98, 86, 87, 88, 89, 29, 33, 0, 33, 0, 0,
		0, 35, 35, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 100, 99, 0,
		0, 35, 35, 0, 0, 0, 0, 0, 0, 103, 0, 0, 0, 0, 0, 0,
		22, 35, 35, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		21, 35, 35, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 121,
		0, 35, 35, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		24, 35, 35, 0, 0, 0, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 35, 35, 123, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		112, 35, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	static final int[] textureDataMap = new int[]{
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 15, 7, 0, 1, 2, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0,
		0, 14, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 13, 5, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0,
		0, 12, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 11, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 10, 2, 0, 0, 0, 1, 3, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 9, 1, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	static Logger log = null;

	static void init() {

		if (woolBlockMap.isEmpty()) {
			try {
				File textureFile = new File("plugins/mxImgImport/terrain.png");
				BufferedImage texturePack = null;
				if (textureFile.exists() && textureFile.canRead()) {
					texturePack = ImageIO.read(textureFile);
				}
				int i = 0;
				for (int y = 0; y < 16; ++y) {
					for (int x = 0; x < 16; ++x) {
						if (textureIdTopMap[i] != 0) {
//							System.out.println(x + "x" + y + ":"
//									+ Material.getMaterial(textureIdTopMap[i]).name() + "x" + textureDataMap[i]);
							RGB col = averageColor(texturePack, 16 * x, 16 * y, 16, 16);
							MatM mat = new MatM(Material.getMaterial(textureIdTopMap[i]), textureDataMap[i]);
							if (textureIdTopMap[i] == 35) {
								woolBlockMap.put(col, mat);
							}
							colorsBlockTopMap.put(col, mat);
						}
						if (textureIdSideMap[i] != 0) {
							RGB col = averageColor(texturePack, 16 * x, 16 * y, 16, 16);
							MatM mat = new MatM(Material.getMaterial(textureIdSideMap[i]), textureIdSideMap[i]);
							colorsBlockSideMap.put(col, mat);
						}
						++i;
					}
				}
//				for (RGB k : colorsBlockSideMap.keySet()) {
//					if (colorsBlockSideMap.get(k).mat.getId() == 35) {
//						System.out.println("wool:" + colorsBlockSideMap.get(k).ss + " - " + k.r + ", " + k.g + ", " + k.b);
//					}
//				}
			} catch (IOException ex) {
				Logger.getLogger(ImageDrawer.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		// old mappings
		if (woolBlockMap.isEmpty()) {
			woolBlockMap.put(new RGB(255, 255, 255), new MatM(Material.WOOL, 0));
			woolBlockMap.put(new RGB(196, 86, 205), new MatM(Material.WOOL, 1));
			woolBlockMap.put(new RGB(114, 147, 215), new MatM(Material.WOOL, 2));
			woolBlockMap.put(new RGB(33, 200, 214), new MatM(Material.WOOL, 3));
			woolBlockMap.put(new RGB(210, 210, 14), new MatM(Material.WOOL, 4));
			woolBlockMap.put(new RGB(100, 230, 0), new MatM(Material.WOOL, 5));
			woolBlockMap.put(new RGB(224, 155, 173), new MatM(Material.WOOL, 6));
			woolBlockMap.put(new RGB(71, 71, 71), new MatM(Material.WOOL, 7));
			woolBlockMap.put(new RGB(173, 180, 180), new MatM(Material.WOOL, 8));
			woolBlockMap.put(new RGB(43, 129, 166), new MatM(Material.WOOL, 9));
			woolBlockMap.put(new RGB(90, 0, 90), new MatM(Material.WOOL, 10));
			woolBlockMap.put(new RGB(40, 53, 161), new MatM(Material.WOOL, 11));
			woolBlockMap.put(new RGB(93, 56, 30), new MatM(Material.WOOL, 12));
			woolBlockMap.put(new RGB(57, 78, 25), new MatM(Material.WOOL, 13));
			woolBlockMap.put(new RGB(250, 0, 0), new MatM(Material.WOOL, 14));
			woolBlockMap.put(new RGB(0, 0, 0), new MatM(Material.WOOL, 15));
		}
		if (colorsBlockSideMap.isEmpty()) {
			colorsBlockSideMap.putAll(woolBlockMap);
			colorsBlockSideMap.put(new RGB(160, 160, 160), new MatM(Material.STONE, 0));
			colorsBlockSideMap.put(new RGB(188, 152, 98), new MatM(Material.WOOD, 0));
			colorsBlockSideMap.put(new RGB(255, 255, 255), new MatM(Material.WOOL, 0));
			colorsBlockSideMap.put(new RGB(214, 207, 154), new MatM(Material.SANDSTONE, 0));
			colorsBlockSideMap.put(new RGB(150, 125, 70), new MatM(Material.LOG, 0));
			colorsBlockSideMap.put(new RGB(57, 46, 28), new MatM(Material.LOG, 1));
			colorsBlockSideMap.put(new RGB(255, 251, 93), new MatM(Material.GOLD_BLOCK, 0));
			colorsBlockSideMap.put(new RGB(213, 213, 213), new MatM(Material.IRON_BLOCK, 0));
			colorsBlockSideMap.put(new RGB(176, 176, 176), new MatM(Material.DOUBLE_STEP, 0));
			colorsBlockSideMap.put(new RGB(46, 46, 61), new MatM(Material.OBSIDIAN, 0));
			colorsBlockSideMap.put(new RGB(120, 108, 30), new MatM(Material.CHEST, 0));
			//pBlockMap.put(new RGB(35,80,35),new MatM( Material.MOSSY_COBBLESTONE, 0));
			colorsBlockSideMap.put(new RGB(160, 240, 240), new MatM(Material.DIAMOND_BLOCK, 0));
			//		colorsBlockSideMap.put(new RGB(100,56,56),new MatM( Material.NETHERRACK, 0));
			//	colorsBlockSideMap.put(new RGB(200,0,200),new MatM( Material.GLOWSTONE, 0));
			colorsBlockTopMap.putAll(colorsBlockSideMap);
		}
	}

	static RGB averageColor(BufferedImage img, int ix, int iy, int width, int height) {
		long r = 0, b = 0, g = 0, n = 0;
		for (int x = ix, i = 0; x < img.getWidth() && i < width; ++x, ++i) {
			for (int y = iy, j = 0; y < img.getHeight() && j < height; ++y, ++j) {

				int pixel = img.getRGB(x, y);
				int alpha = (pixel >> 24) & 0xff;
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;

				if (alpha != 0) {
					r += red;
					g += green;
					b += blue;
					++n;
				}
			}
		}
		System.out.println("n: " + n + " - " + r + ", " + g + ", " + b + " : "
				+ (int) ((double) r / n) + ", " + (int) ((double) g / n) + ", " + (int) ((double) b / n));
		return n > 0 ? new RGB((int) ((double) r / n), (int) ((double) g / n), (int) ((double) b / n)) : new RGB(0, 0, 0);
	}

	static float ColorDistance(RGB a, RGB b) {
		int most = 0;
		if (a.getRed() > a.getGreen() && a.getRed() > a.getBlue()) {
			most = 1;
		} else if (a.getGreen() > a.getRed() && a.getGreen() > a.getBlue()) {
			most = 2;
		} else {
			most = 3;
		}

		RGB c = new RGB(b.getRed() - a.getRed(), b.getGreen() - a.getGreen(), b.getBlue() - a.getBlue());
		float clen = c.getRed() * c.getRed() * (most == 1 ? 1 : 2) + c.getGreen() * c.getGreen() * (most == 2 ? 1 : 2) + c.getBlue() * c.getBlue() * (most == 3 ? 1 : 2);

		return clen;
	}

	static private int sign(double x) {
		if (x > 0) {
			return 1;
		} else if (x == 0) {
			return 0;
		} else {
			return -1;
		}
	}

	static boolean Draw(CommandSender sender, Location loc_1, Location loc_2, String filename, boolean allblocks, boolean scaleImage, Map<Location, MatM> m_pUndoMap) {
		filename = getPicFilename(filename);
		if (filename == null) {
			sender.sendMessage(ChatColor.RED + "File was not found.");
			return false;
		}
		File imageFile = new File("plugins/mxImgImport/" + filename);

		BufferedImage pImageFile = null;
		try {
			pImageFile = ImageIO.read(imageFile);
		} catch (IOException e) {
			sender.sendMessage(ChatColor.RED + "File was not found.");
			return false;
		}


		// Send a message to the player, telling him that the import starts.
		sender.sendMessage(ChatColor.GOLD + "Importing Image '" + imageFile.getName() + "' (" + Integer.toString(pImageFile.getWidth()) + "x" + Integer.toString(pImageFile.getHeight()) + ")");
		if (log != null) {
			log.info("Importing Image '" + imageFile.getName() + "' (" + Integer.toString(pImageFile.getWidth()) + "x" + Integer.toString(pImageFile.getHeight()) + ")");
		}

		int UnitX = sign(loc_2.getBlockX() - loc_1.getBlockX());
		int UnitY = sign(loc_2.getBlockY() - loc_1.getBlockY());
		int UnitZ = sign(loc_2.getBlockZ() - loc_1.getBlockZ());

		init();
		Map<RGB, MatM> pBlockMap = allblocks ? 
				(UnitY == 0 ? colorsBlockTopMap : colorsBlockSideMap) : woolBlockMap;

		if (m_pUndoMap != null) {
			m_pUndoMap.clear();
		}

		if (scaleImage) {
			int height, width;
			// first point is lower-left, 2nd upper-right
			if (UnitX == 0) {
				// is vertical on y-axis
				height = Math.abs(loc_2.getBlockY() - loc_1.getBlockY());
				width = Math.abs(loc_2.getBlockZ() - loc_1.getBlockZ());
			} else if (UnitY == 0) {
				// flat
				if ((UnitZ < 0 && UnitX > 0) || (UnitZ > 0 && UnitX < 0)) {
					height = Math.abs(loc_2.getBlockZ() - loc_1.getBlockZ());
					width = Math.abs(loc_2.getBlockX() - loc_1.getBlockX());
				} else {
					height = Math.abs(loc_2.getBlockX() - loc_1.getBlockX());
					width = Math.abs(loc_2.getBlockZ() - loc_1.getBlockZ());
				}
			} else {
				// vertical on x-axis
				height = Math.abs(loc_2.getBlockY() - loc_1.getBlockY());
				width = Math.abs(loc_2.getBlockX() - loc_1.getBlockX());
			}

			double scaleY = (double) height / pImageFile.getHeight();
			double scaleX = (double) width / pImageFile.getWidth();
			double scale = scaleY > scaleX ? scaleX : scaleY;

			int w = pImageFile.getWidth();
			int h = pImageFile.getHeight();
			BufferedImage bi = new BufferedImage((int) (w * scale), (int) (h * scale),
					BufferedImage.TYPE_INT_ARGB);

			Graphics2D grph = (Graphics2D) bi.getGraphics();
			grph.scale(scale, scale);

			// everything drawn with grph from now on will get scaled.

			grph.drawImage(pImageFile, 0, 0, null);
			grph.dispose();
			pImageFile = bi;

			// Send a message to the player, telling him that the import starts.
			sender.sendMessage(ChatColor.GOLD + "Scaled to " + Integer.toString(pImageFile.getWidth()) + "x" + Integer.toString(pImageFile.getHeight()) + "");
			if (log != null) {
				log.info("Scaled to " + Integer.toString(pImageFile.getWidth()) + "x" + Integer.toString(pImageFile.getHeight()) + "");
			}
		} else if (pImageFile.getWidth() > 100 || pImageFile.getHeight() > 100) {
			sender.sendMessage(ChatColor.RED + "Image is to big");
			return false;
		}

		Set<Map.Entry<RGB, MatM>> pSet = pBlockMap.entrySet();
		Location curpos = loc_1.clone();

		// Iterate through the pixels
		for (int y = 0; y < pImageFile.getHeight(); ++y) {
			for (int x = 0; x < pImageFile.getWidth(); ++x) {

				if (UnitX == 0) {
					curpos.setZ(loc_1.getZ() + UnitZ * x);
					curpos.setY(loc_1.getY() + UnitY * y);
				} else if (UnitY == 0) {
					if ((UnitZ < 0 && UnitX > 0) || (UnitZ > 0 && UnitX < 0)) {
						curpos.setZ(loc_1.getZ() + UnitZ * y);
						curpos.setX(loc_1.getX() + UnitX * x);
					} else {
						curpos.setZ(loc_1.getZ() + UnitZ * x);
						curpos.setX(loc_1.getX() + UnitX * y);
					}
				} else {
					curpos.setX(loc_1.getX() + UnitX * x);
					curpos.setY(loc_1.getY() + UnitY * y);
				}

				int pixel = pImageFile.getRGB(x, pImageFile.getHeight() - y - 1);
				int alpha = (pixel >> 24) & 0xff;
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;

				if (alpha != 0 && curpos.getBlock() != null) {
					RGB c = new RGB(red, green, blue);

					float bestDist = 100000000;
					RGB col = null;

					Iterator<Map.Entry<RGB, MatM>> It = pSet.iterator();
					while (It.hasNext()) {
						Map.Entry<RGB, MatM> Current = It.next();
						float dist = ColorDistance(c, (Current.getKey()));

						if (dist < bestDist) {
							bestDist = dist;
							col = Current.getKey();
						}
					}

					if (col != null) {
						if (m_pUndoMap != null) {
							m_pUndoMap.put(curpos.clone(), new MatM(curpos.getBlock().getType(), curpos.getBlock().getData()));
						}
						curpos.getBlock().setType(pBlockMap.get(col).mat);
						curpos.getBlock().setData((byte) pBlockMap.get(col).ss);
					} else {
						//log.info("null color at " + x + "," + y + ": " + red + "," + green + "," + blue + " - " + bestDist + "/" + pSet.size());
					}

				}
			}
		}
		return true;
	}

	public static String getPicFilename(String filename) {
		File imageFile = new File("plugins/mxImgImport/" + filename);
		if (!imageFile.exists() || !imageFile.canRead()) {
			if (!(filename.length() > 4 && filename.substring(filename.length() - 5, filename.length() - 1).contains("."))) {
				// look for a close match
				imageFile = new File("plugins/mxImgImport/");
				File[] search = imageFile.listFiles(new ImageSearchFilter(filename)); //for(String ext : new String[]{""}) { }
				imageFile = null;
				if (search.length > 0) {
					imageFile = search[0];
					if (search.length > 1) {
						int len = imageFile.getName().length();
						for (int i = 1; i < search.length; ++i) {
							if (search[i].getName().length() < len) {
								imageFile = search[i];
								len = imageFile.getName().length();
							}
						}
					}
				}
			}
			if (imageFile == null || !imageFile.exists() || !imageFile.canRead()) {
				return null;
			}
		}
		return imageFile.getName();
	}
}

class ImageSearchFilter implements FileFilter {

	String searchFilename;
	private static final String IMAGE_PATTERN =
			"([^\\s]+(\\.(?i)(png|gif|bmp|jpg|jpeg|wbmp|gif))$)";
	private static Pattern pattern = Pattern.compile(IMAGE_PATTERN);

	public ImageSearchFilter(String searchFilename) {
		this.searchFilename = searchFilename.toLowerCase();
	}

	@Override
	public boolean accept(File pathname) {
		if (pathname.getName().toLowerCase().startsWith(searchFilename)) {
			return pattern.matcher(pathname.getName().toLowerCase()).matches();
		}
		return false;
	}
}

class MatM {

	MatM(Material m, int s) {
		mat = m;
		ss = s;
	}
	Material mat;
	int ss;
}

class RGB {

	int r;
	int g;
	int b;

	RGB(Color a) {
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

	int getRed() {
		return r;
	}

	int getGreen() {
		return g;
	}

	int getBlue() {
		return b;
	}
}