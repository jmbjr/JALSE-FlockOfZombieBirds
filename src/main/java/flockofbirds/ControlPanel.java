package flockofbirds;

import java.awt.Component;
import java.util.EventObject;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeListener;

import flockofbirds.entities.Loner;
import flockofbirds.entities.Flocker;

@SuppressWarnings("serial")
public class ControlPanel extends JPanel {

    private static JLabel newLabel(final String text) {
	final JLabel label = new JLabel(text, SwingConstants.CENTER);
	label.setAlignmentX(Component.CENTER_ALIGNMENT);
	return label;
    }

    private static JSlider newSlider(final int min, final int max, final int ticks, final int initial,
	    final ChangeListener listener) {
	final JSlider slider = new JSlider(min, max, initial);
	slider.setMajorTickSpacing(ticks);
	slider.setPaintLabels(true);
	slider.setAlignmentX(Component.CENTER_ALIGNMENT);
	slider.addChangeListener(listener);
	return slider;
    }

    private static int sliderValue(final EventObject e) {
	return ((JSlider) e.getSource()).getValue();
    }

    public ControlPanel(final FlockPanel zombiesPanel) {
	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

	// Population
	add(newLabel("Population"));
	add(newSlider(0, 200, 50, FlockProperties.getPopulation(), e -> {
	    FlockProperties.setPopulation(sliderValue(e));
	    zombiesPanel.adjustPopulation();
	}));
	add(Box.createVerticalGlue());

	// Infected speed
	add(newLabel("Infected Relative Speed (%)"));
	add(newSlider(50, 200, 50,
		(int) (100 * FlockProperties.getSpeed(Flocker.class) / FlockProperties.getSpeed(Loner.class)),
		e -> {
		    FlockProperties.setInfectedRelativeSpeed(sliderValue(e));
		    zombiesPanel.adjustInfectedSpeed();
		}));
	add(Box.createVerticalGlue());

	// Healthy sight
	add(newLabel("Healthy Sight Range"));
	add(newSlider(0, 200, 50, FlockProperties.getSightRange(Loner.class), e -> {
	    FlockProperties.setHealthySightRange(sliderValue(e));
	    zombiesPanel.adjustSightRange(Loner.class);
	}));
	add(Box.createVerticalGlue());

	// Infected sight
	add(newLabel("Infected Sight Range"));
	add(newSlider(0, 200, 50, FlockProperties.getSightRange(Flocker.class), e -> {
	    FlockProperties.setInfectedSightRange(sliderValue(e));
	    zombiesPanel.adjustSightRange(Flocker.class);
	}));
	add(Box.createVerticalGlue());

	// Infection time
	add(newLabel("Infection Time (s.)"));
	add(newSlider(0, 30, 5, (int) FlockProperties.getInfectionTime(),
		e -> FlockProperties.setInfectionTime(sliderValue(e))));
	add(Box.createVerticalGlue());

	// Starvation time
	add(newLabel("Starvation Time (s.)"));
	add(newSlider(0, 30, 5, (int) FlockProperties.getStarveTime(),
		e -> FlockProperties.setStarveTime(sliderValue(e))));
	add(Box.createVerticalGlue());

	// Reset
	final JButton resetButton = new JButton("Reset Simulation");
	resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	resetButton.addActionListener(e -> zombiesPanel.reset());
	add(resetButton);
    }
}
