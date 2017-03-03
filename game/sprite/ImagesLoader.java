package game.sprite;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

public class ImagesLoader {

	private final static String IMAGE_DIR = "Images/";

	/**
	 * [Filename Prefix, ArrayList<BufferedImage>]
	 */
	private HashMap<String, ArrayList<BufferedImage>> imagesMap;

	private GraphicsConfiguration gc;

	/**
	 * The constructor: specify a file, in which detailed image information is
	 * stored.
	 * 
	 * @param filename
	 *            The filename, in which all image files information are stored.
	 */
	public ImagesLoader(String filename) {
		initLoader();
		loadImagesFile(filename);
	}

	private void initLoader() {
		imagesMap = new HashMap<String, ArrayList<BufferedImage>>();

		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
	}

	/**
	 * 
	 * The file formats:
	 * 
	 * 1. o <fnm>
	 * 
	 * a single image
	 * 
	 * 2. //
	 * 
	 * A line of comments
	 * 
	 * 3. blank lines and comment lines.
	 * 
	 * @param filename
	 *            The file name
	 */
	private void loadImagesFile(String filename) {
		String imagesFilename = IMAGE_DIR + filename;
		System.out.println("Reading file: " + imagesFilename);
		try {
			InputStream in = this.getClass()
					.getResourceAsStream(imagesFilename);
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			String line;
			char character;
			while ((line = reader.readLine()) != null) {
				// blank line
				if (line.length() == 0) {
					continue;
				}
				// comment
				if (line.startsWith("//")) {
					continue;
				}
				// a single file
				character = Character.toLowerCase(line.charAt(0));
				if (character == 'o') {
					getFileNameImage(line);
				} else {
					System.err.println("Do not recognize line: " + line);
				}

			}
			reader.close();
		} catch (IOException e) {
			System.err.println("Error reading file: " + imagesFilename);
			System.exit(1);
		}
	}

	/**
	 * Format:
	 * 
	 * o <fnm>
	 * 
	 * @param line
	 *            A line in the images file.
	 */
	private void getFileNameImage(String line) {
		StringTokenizer tokens = new StringTokenizer(line);

		if (tokens.countTokens() != 2)
			System.err.println("Wrong no. of arguments for " + line);
		else {
			// skip command label
			tokens.nextToken();
			System.out.print("o Line: ");
			loadSingleImage(tokens.nextToken());
		}
	}

	/**
	 * Load a single image.
	 * 
	 * @param fnm
	 *            The image filename.
	 * @return True is the image is loaded successfully, otherwise false.
	 */
	public boolean loadSingleImage(String fnm) {
		// get the name.
		String name = getPrefix(fnm);

		if (imagesMap.containsKey(name)) {
			System.err.println("Error: " + name + "already used");
			return false;
		}

		BufferedImage image = loadImage(fnm);
		if (image != null) {
			ArrayList<BufferedImage> imagesList = new ArrayList<BufferedImage>();
			imagesList.add(image);
			imagesMap.put(name, imagesList);
			System.out.println("  Stored " + name + "/" + fnm);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Extract the name before '.' in the filename
	 * 
	 * @param filename
	 *            The filename.
	 * @return The prefix of the filename: the name of the file.
	 */
	private String getPrefix(String filename) {
		int index;
		if ((index = filename.lastIndexOf(".")) == -1) {
			System.err.println("No prefix found for filename: " + filename);
			return filename;
		} else {
			return filename.substring(0, index);
		}
	}

	/**
	 * Get the image associated with <name>. If there are several images stored
	 * under that name, return the first one in the list.
	 * 
	 * @param name
	 *            The name of the Image.
	 * @return The BufferedImage associated with the name.
	 */
	public BufferedImage getImage(String name) {
		ArrayList<BufferedImage> imagesList = imagesMap.get(name);
		if (imagesList == null) {
			System.err.println("No image(s) stored under " + name);
			return null;
		}

		return imagesList.get(0);
	}

	/**
	 * Load the BufferedImage from the given filename.
	 * @param filename The filename
	 * @return The loaded image.
	 */
	private BufferedImage loadImage(String filename)	{
		try {
			BufferedImage image = ImageIO.read(getClass().getResource(
					IMAGE_DIR + filename));

			int transparency = image.getColorModel().getTransparency();
			BufferedImage copy = gc.createCompatibleImage(image.getWidth(),
					image.getHeight(), transparency);
			// create a graphics context
			Graphics2D g2d = copy.createGraphics();

			// copy image
			g2d.drawImage(image, 0, 0, null);
			g2d.dispose();
			return copy;
		} catch (IOException e) {
			System.err.println("Load Image error for " + IMAGE_DIR + "/" + filename
					+ ":\n" + e);
			return null;
		}
	} 

} 
