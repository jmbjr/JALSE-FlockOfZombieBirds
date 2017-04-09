package flockofbirds;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class Flock {

    public static void main(final String[] args) {
	// Create window and panel
	final JFrame frame = new JFrame("JALSE-Zombies");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setLayout(new BorderLayout());
	final FlockPanel zombiesPanel = new FlockPanel();
	frame.add(zombiesPanel, BorderLayout.CENTER);
	frame.add(new ControlPanel(zombiesPanel), BorderLayout.EAST);
	frame.pack();
	frame.setResizable(false);
	frame.setLocationRelativeTo(null);
	frame.setVisible(true);
    }
}
