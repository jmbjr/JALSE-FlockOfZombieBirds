package flockofbirds;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class Flock {

    public static void main(final String[] args) {
	// Create window and panel
	final JFrame frame = new JFrame("JALSE-FlockOfBirds");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setLayout(new BorderLayout());
	final FlockPanel flockPanel = new FlockPanel();
	frame.add(flockPanel, BorderLayout.CENTER);
	frame.add(new ControlPanel(flockPanel), BorderLayout.EAST);
	frame.pack();
	frame.setResizable(false);
	frame.setLocationRelativeTo(null);
	frame.setVisible(true);
    }
}
