package com.coding.LojoFundrasing.Models;

import javax.imageio.ImageIO;
import javax.persistence.Entity;
import javax.servlet.http.HttpSession;
import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTMLEditorKit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.coding.LojoFundrasing.Controllers.LojoController;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;



public class HtmlImageGenerator extends HTMLEditorKit{
	private JEditorPane editorPane;
	static final Dimension DEFAULT_SIZE = new Dimension(800, 800);
	

	public HtmlImageGenerator() {
		editorPane = createJEditorPane();
	}

	public ComponentOrientation getOrientation() {
		return editorPane.getComponentOrientation();
	}

	public void setOrientation(ComponentOrientation orientation) {
		editorPane.setComponentOrientation(orientation);
	}

	public Dimension getSize() {
		return editorPane.getSize();
	}

	public void setSize(Dimension dimension) {
		editorPane.setSize(dimension);
	}

	public void loadUrl(URL url) {
		try {
			editorPane.setPage(url);
		} catch (IOException e) {
			throw new RuntimeException(String.format("Exception while loading %s", url), e);
		}
	}

	public void loadUrl(String url) {
		try {
			editorPane.setPage(url);
			System.out.println("page url set " + url);
		} catch (IOException e) {
			throw new RuntimeException(String.format("Exception while loading %s", url), e);
		}
	}

	public void loadHtml(String html) {
		editorPane.setText(html);
		onDocumentLoad();
	}

	/*public String getLinksMapMarkup(String mapName) {
		final StringBuilder markup = new StringBuilder();
		markup.append("<map name=\"").append(mapName).append("\">\n");
		for (LinkInfo link : getLinks()) {
			final List<Rectangle> bounds = link.getBounds();
			for (Rectangle bound : bounds) {
				final int x1 = (int) bound.getX();
				final int y1 = (int) bound.getY();
				final int x2 = (int) (x1 + bound.getWidth());
				final int y2 = (int) (y1 + bound.getHeight());
				markup.append(String.format("<area coords=\"%s,%s,%s,%s\" shape=\"rect\"", x1, y1, x2, y2));
				for (Map.Entry<String, String> entry : link.getAttributes().entrySet()) {
					String attName = entry.getKey();
					String value = entry.getValue();
					markup.append(" ").append(attName).append("=\"").append(value.replace("\"", "&quot;")).append("\"");
				}
				markup.append(">\n");
			}
		}
		markup.append("</map>\n");
		return markup.toString();
	}*/

	/*public List<LinkInfo> getLinks() {
		final LinkHarvester harvester = new LinkHarvester(editorPane);
		return harvester.getLinks();
	}*/

	public void saveAsHtmlWithMap(String file, String imageUrl) {
		saveAsHtmlWithMap(new File(file), imageUrl);
	}

	public void saveAsHtmlWithMap(File file, String imageUrl) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(file);
			writer.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n");
			writer.append("<html>\n<head></head>\n");
			writer.append("<body style=\"margin: 0; padding: 0; text-align: center;\">\n");
			//final String htmlMap = getLinksMapMarkup("map");
			//writer.write(htmlMap);
			writer.append("<img border=\"0\" usemap=\"#map\" src=\"");
			writer.append(imageUrl);
			writer.append("\"/>\n");
			//writer.append("</body>\n</html>");
		} catch (IOException e) {
			throw new RuntimeException(String.format("Exception while saving '%s' html file", file), e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException ignore) {
				}
			}
		}
		System.out.println("file writer " + writer);

	}

	/*public void saveAsImage(String file) {
		saveAsImage(new File(file));
	}*/

	public File saveAsImage(String html) throws IOException {
		//System.out.println("file " + file.getAbsoluteFile());
		File file = new File("html.jpeg");
		BufferedImage img = getBufferedImage();
		try {
			//final String formatName = FormatNameUtil.formatForFilename(file.getName());
			ImageIO.write(img, "html.jpeg", file);
			System.out.println("img write" + img);
		} catch (IOException e) {
			throw new RuntimeException(String.format("Exception while saving '%s' image", file), e);
		}
		System.out.println("file " + file.getTotalSpace());
		return file;
	}

	protected void onDocumentLoad() {
	}

	public Dimension getDefaultSize() {
		return DEFAULT_SIZE;
	}

	public BufferedImage getBufferedImage() throws IOException {
		Dimension prefSize = editorPane.getPreferredSize();
		BufferedImage img = new BufferedImage(prefSize.width, editorPane.getPreferredSize().height, BufferedImage.TYPE_INT_ARGB);
		//BufferedImage img = ImageIO.read(image);
		Graphics graphics = img.createGraphics();
		editorPane.setSize(prefSize);
		editorPane.paint(graphics);
		return img;
	}

	protected JEditorPane createJEditorPane() {
		System.out.println("editor 1");
		final JEditorPane editorPane = new JEditorPane();
		editorPane.setSize(getDefaultSize());
		editorPane.setEditable(false);
		//final SynchronousHTMLEditorKit kit = new SynchronousHTMLEditorKit();
		//editorPane.setEditorKitForContentType("text/html", kit);
		editorPane.setContentType("text/html");
		editorPane.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("page")) {
					onDocumentLoad();
				}
			}
		});
		System.out.println("editor 2");
		return editorPane;
	}

	/*public HtmlImageGenerator() {
		editorPane = new JEditorPane();
	}

	public ComponentOrientation getOrientation() {
		return editorPane.getComponentOrientation();
	}

	public void setOrientation(ComponentOrientation orientation) {
		editorPane.setComponentOrientation(orientation);
	}

	public Dimension getSize() {
		return editorPane.getSize();
	}

	public void setSize(Dimension dimension) {
		editorPane.setSize(dimension);
	}

	public void loadUrl(URL html) {
		try {
			editorPane.setPage(html);
		} catch (IOException e) {
			throw new RuntimeException(String.format("Exception while loading %s", html), e);
		}
	}

	public void loadUrl(String url) {
		try {
			editorPane.setPage(url);
		} catch (IOException e) {
			throw new RuntimeException(String.format("Exception while loading %s", url), e);
		}
	}

	public void loadHtml(String html) {
		editorPane.setText(html);
		onDocumentLoad();
	}

	/*public String getLinksMapMarkup(String mapName) {
		final StringBuilder markup = new StringBuilder();
		markup.append("<map name=\"").append(mapName).append("\">\n");
		for (Link link : email.getLink()) {
			final List<Rectangle> bounds = link.getBounds();
			for (Rectangle bound : bounds) {
				final int x1 = (int) bound.getX();
				final int y1 = (int) bound.getY();
				final int x2 = (int) (x1 + bound.getWidth());
				final int y2 = (int) (y1 + bound.getHeight());
				markup.append(String.format("<area coords=\"%s,%s,%s,%s\" shape=\"rect\"", x1, y1, x2, y2));
				for (Map.Entry<String, String> entry : link.getAttributes().entrySet()) {
					String attName = entry.getKey();
					String value = entry.getValue();
					markup.append(" ").append(attName).append("=\"").append(value.replace("\"", "&quot;")).append("\"");
				}
				markup.append(">\n");
			}
		}
		markup.append("</map>\n");
		return markup.toString();
	}*/

	/*public List<LinkInfo> getLinks() {
		final LinkHarvester harvester = new LinkHarvester(editorPane);
		return harvester.getLinks();
	}*/

/*	public void saveAsHtmlWithMap(String file, String imageUrl) {
		saveAsHtmlWithMap(new File(file), imageUrl);
	}

	public void saveAsHtmlWithMap(File file, String imageUrl) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(file);
			//writer.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n");
			//writer.append("<html>\n<head></head>\n");
			//writer.append("<body style=\"margin: 0; padding: 0; text-align: center;\">\n");
			//final String htmlMap = getLinksMapMarkup("map");
			//writer.write(htmlMap);
			//writer.append("<img border=\"0\" usemap=\"#map\" src=\"");
			writer.append(imageUrl);
			//writer.append("\"/>\n");
			//writer.append("</body>\n</html>");
		} catch (IOException e) {
			throw new RuntimeException(String.format("Exception while saving '%s' html file", file), e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException ignore) {
				}
			}
		}

	}

	/*public void saveAsImage(String file) {
		saveAsImage(new File(file));
	}*/

	/*public void saveAsImage(File file, String html) {

		BufferedImage img = getBufferedImage();
		try {
			final String formatName = "image";
		ImageIO.write(img, formatName, file);
		} catch (IOException e) {
			throw new RuntimeException(String.format("Exception while saving '%s' image", file), e);
		}
	}
	//@Produces("image/png")
	/*public void getImage(String html) {

		BufferedImage img = getBufferedImage();
		try {
			final String formatName = "image";
			ImageIO.write(img, formatName, html);
		} catch (IOException e) {
			throw new RuntimeException(String.format("Exception while saving '%s' image", file), e);
		}
		return img;
	}*/

	/*protected void onDocumentLoad() {
	}

	public Dimension getDefaultSize() {
		return DEFAULT_SIZE;
	}

	public BufferedImage getBufferedImage() {
		Dimension prefSize = editorPane.getPreferredSize();
		BufferedImage img = new BufferedImage(prefSize.width, editorPane.getPreferredSize().height, BufferedImage.TYPE_INT_ARGB);
		Graphics graphics = img.getGraphics();
		editorPane.setSize(prefSize);
		editorPane.paint(graphics);
		return img;
	}
	
    public void renderHTML(String html, File image) throws IOException {
    	System.out.println(html);
    	int width = 650;
    	int height = 2000;
    	
    	
    	//Java2DRenderer renderer = new Java2DRenderer(file, width, height);
    	loadUrl(html);
    	//BufferedImage img = ImageIO.read(image);;

   

    	// write  as uncompressed JPEG (the 1f parameter)
    	
    	image.write(image, "/home/parveen/Desktop/full.jpg");

    	// now we compress it this is using by ImageIO utilities
    	imagWriter = FSImageWriter.newJpegWriter(0.76f);
    	imagWriter.write(image, "/home/parveen/Desktop/threeqtr.jpg");

    	// we can use the same writer, but at a different compression rate
    	imagWriter.setWriteCompressionQuality(0.8f);
    	imagWriter.write(image, "/home/parveen/Desktop/ninety.jpg");

    	// now we scale it ,ScalingOptions lets us control some quality options and pass in rendering hints
    	ScalingOptions scalingOption = new ScalingOptions (BufferedImage.TYPE_INT_ARGB , DownscaleQuality.LOW_QUALITY ,
    	RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR );

    	//target size -:you can reuse the option instance for different sizes
    	scalingOption.setTargetDimensions(new Dimension(200, 200));
    	Image scaled = ImageUtil.getScaledInstance(scalingOptions, image);

    	// we can also scale multiple dimensions at once (well, not at once, but.....)
    	// be careful because quality setting in the options instance can affect performance.

    	List dimensions = new ArrayList();
    	dimensions.add(new Dimension(150, 150)); 
    	dimensions.add(new Dimension(200, 200)); 
    	dimensions.add(new Dimension(600, 600));
    	dimensions.add(new Dimension(760, 760)); 
    	List images = ImageUtil.scaleMultiple(scalingOptions, image, dimensions);

    	
    	//BufferedImage image = GraphicsEnvironment.getLocalGraphicsEnvironment()
    		   // .getDefaultScreenDevice().getDefaultConfiguration()
    		  //  .createCompatibleImage(width, height);
    	//BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    	/*BufferedImage img = ImageIO.read(image);
    		Graphics graphics = img.createGraphics();

    		JEditorPane jep = new JEditorPane("text/html", html);
    		jep.setSize(width, height);
    		jep.print(graphics);
    		
    	
    		
    		ImageIO.write(img, "jpeg", image);
    		return image;*/
    	/*JEditorPane pane2 = createJEditorPane();
    	pane2.setContentType("text/html");
    	pane2.setText(html);
    	//pane.setText(html);
    	JScrollPane pane = new JScrollPane(pane2);
    	   int w = 800;
           int h = 650;
    	/*JFrame frame=new JFrame();
    	frame.add(pane);
    	frame.setSize(200,200);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setVisible(true);*/
    	/*HTMLEditorKit kit = new HTMLEditorKit();
    	pane.setEditorKit(kit);
    	Document doc = kit.createDefaultDocument();
    	pane.setDocument(doc);*/
           
           
    	//File file = null;
    	// BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    	  //pane2.paint(image.getGraphics());
    	 // Graphics2D g2 = image.createGraphics();
    	 /* pane2.paint(g2);
    	  g2.dispose();
    	  
    	  try {
			ImageIO.write(image, "jpeg", output);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		      pane2.setContentType("text/html");
		      pane2.setText("<html>Connection issues!</html>");
		}*/
    	   /* JFrame frame=new JFrame();
    	    frame.getContentPane().add(pane2);
    	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	    frame.setBounds(0,0,w,h);
    	    frame.setLocationRelativeTo(null);
    	    frame.setVisible(true);*/
    	  
    	 //return output;
    	// now add it to a scroll pane
    	//JScrollPane scrollPane = new JScrollPane(pane);
    	//pane.setText(html);
    	//System.out.println(pane.getText());
    	//File file2 = new File(ViewFactory);
    	//jEditorPane.setText(htmlString);
    	//return frame;
    	/*com.aspose.html.HTMLDocument document = new com.aspose.html.HTMLDocument("document.html");
    	try {
    	    // Initialize ImageSaveOptions
    	    com.aspose.html.saving.ImageSaveOptions options = new com.aspose.html.saving.ImageSaveOptions(com.aspose.html.rendering.image.ImageFormat.Jpeg);

    	    // Convert HTML to output JPG image
    	    com.aspose.html.converters.Converter.convertHTML(document, options, "output.jpg");
    	    System.out.println(document);
    	} finally {
    	    if (document != null) {
    	        document.dispose();
    	    }
    	}*/
    	
   /* }
	protected JEditorPane createJEditorPane() {
		final JEditorPane editorPane = new JEditorPane();
		editorPane.setSize(getDefaultSize());
		editorPane.setEditable(false);
		//final SynchronousHTMLEditorKit kit = new SynchronousHTMLEditorKit();
		//editorPane.setEditorKitForContentType("text/html", kit);
		editorPane.setContentType("text/html");
		editorPane.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("page")) {
					onDocumentLoad();
				}
			}
		});
		return editorPane;
	}*/
}
