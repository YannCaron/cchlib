package com.googlecode.cchlib.sandbox.pushingpixels.windows;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.awt.geom.AffineTransform;
//import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
//import com.googlecode.cchlib.sandbox.pushingpixels.AWTUtilitiesFrontEnd;

public class SoftClippedWindow extends JFrame {
    private static final long serialVersionUID = 1L;

    public SoftClippedWindow() {
		super("Test soft-clipped window");

		JButton open = new JButton("Open window");
		open.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
                    public void run() {
						final JWindow window = new JWindow();
						window.setLayout(new BorderLayout());

						try {
							// picture from
							// http://flickr.com/photos/petursey/366204314/
							BufferedImage avatar = ImageIO
									.read(SoftClippedWindow.class
											.getResource("backgr.jpg"));

							JPanel mainPanel = new ImagePanel(avatar);
							JButton close = new JButton("close");
							close.addActionListener(new ActionListener() {
								@Override
                                public void actionPerformed(ActionEvent e) {
									window.dispose();
								}
							});
							JPanel buttonPanel = new JPanel(new FlowLayout(
									FlowLayout.RIGHT));
							buttonPanel.add(close);
							buttonPanel.setOpaque(false);
							buttonPanel.setDoubleBuffered(false);
							mainPanel.add(buttonPanel, BorderLayout.NORTH);

							mainPanel.setDoubleBuffered(false);
							mainPanel.setOpaque(false);
							window.add(mainPanel, BorderLayout.CENTER);

							window.setSize(180, 200);
							window.setLocationRelativeTo(null);
							window.setVisible(true);
							
							//AWTUtilitiesFrontEnd.setWindowOpaque(window,false);
							window.setBackground( new Color(0, 0, 0, 0) );
						} catch (IOException ioe) {
							ioe.printStackTrace();
						}
					}
				});
			}
		});
		this.setLayout(new FlowLayout());
		this.add(open);

		this.setSize(new Dimension(400, 300));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
            public void run() {
				Window w = new SoftClippedWindow();
				w.setVisible(true);
			}
		});
	}

}
