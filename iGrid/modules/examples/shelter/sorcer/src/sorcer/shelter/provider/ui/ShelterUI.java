package sorcer.shelter.provider.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.jini.core.lookup.ServiceItem;
import net.jini.lookup.entry.UIDescriptor;
import net.jini.lookup.ui.MainUI;
import sorcer.shelter.provider.Animal;
import sorcer.shelter.provider.Shelter;
import sorcer.core.provider.ServiceProvider;
import sorcer.ui.serviceui.UIComponentFactory;
import sorcer.ui.serviceui.UIDescriptorFactory;
import sorcer.util.Sorcer;
import javax.swing.*;
import javax.swing.DefaultListModel.*;
import java.util.*;

public class ShelterUI extends JPanel {

	private static final long serialVersionUID = -3171243785170712405L;

	private JList<Animal> animalsInShelterList;

	private JTextField removeTextField;
	private JTextField addTextField;
	private DefaultListModel model = new DefaultListModel();
	private Shelter shelter;

	private ServiceItem item;

	private final static Logger logger = Logger.getLogger(ShelterUI.class
			.getName());

	public ShelterUI(Object provider) {
		super();
		getAccessibleContext().setAccessibleName("Shelter Tester");
		item = (ServiceItem) provider;

		if (item.service instanceof Shelter) {
			shelter = (Shelter) item.service;
			createUI();
		}

	}

	protected void createUI() {
		setLayout(new BorderLayout());
		add(buildShelterPanel(), BorderLayout.CENTER);
		resetAnimalsInShelterField();
	}

    private void resetAnimalsInShelterField() {
        try {
            LinkedList<Animal> animalsInShelterList = shelter.getAnimalsInShelter();
            model.clear();
			for (Animal element : animalsInShelterList)
			{
				model.addElement(element);
			}
			
			
        } catch (Exception e) {
            logger.info("Error occurred while getting animals in shelter\n" + e);
			logger.throwing(getClass().getName(), "resetAnimalsInShelterField", e);
        }
    }
	
	private JPanel buildShelterPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		
		JPanel actionPanel = new JPanel(new GridLayout(2, 3));
		
		JPanel actionPanel1 = new JPanel(new GridLayout(3, 1));
		
		actionPanel1.add(new JLabel("Current Animals in shelter: "));
		animalsInShelterList = new JList<Animal>(model);
		animalsInShelterList.setEnabled(false);
		actionPanel1.add(animalsInShelterList);
	

		
		actionPanel.add(new JLabel("Removing from shelter"));
		removeTextField = new JTextField();
		actionPanel.add(removeTextField);
		JButton removeButton = new JButton("Remove me");
		removeButton.addActionListener(new RemoveAction());
		actionPanel.add(removeButton);
		
		actionPanel.add(new JLabel("Adding to shelter"));
		addTextField = new JTextField();
		actionPanel.add(addTextField);
		JButton addButton = new JButton("Add me");
		addButton.addActionListener(new AddAction());
		actionPanel.add(addButton);
		
		panel.add(actionPanel1);
		panel.add(actionPanel);
		return panel;
	}

	private Animal readTextField(JTextField animalNameField) {
        try {
            String name = animalNameField.getText().toLowerCase();
            return new Animal(name);
        } catch (Exception e) {
            logger.info("Field doesn't contain a valid value");
        }
        return null;
    }
	
	

    private class RemoveAction implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            try {
                
                Animal animalToRemove = readTextField(removeTextField);
                shelter.deleteAnimal(animalToRemove);
                removeTextField.setText("");
                resetAnimalsInShelterField();
                
			} catch (Exception exception) {
				logger.info("Couldn't talk to shelter. Error was " + exception);
				logger.throwing(getClass().getName(), "actionPerformed",
						exception);
			}
		}
	}

    private class AddAction implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            try {
              
                Animal animalToAdd = readTextField(addTextField);
                shelter.addAnimal(animalToAdd);
                addTextField.setText("");
                resetAnimalsInShelterField();
			} catch (Exception exception) {
				logger.info("Couldn't talk to shelter. Error was \n"
						+ exception);
				logger.throwing(getClass().getName(), "actionPerformed",
						exception);
			}
		}
	}

	/**
	 * Returns a service UI descriptorfor this service. Usally this method is
	 * used as an entry in provider configuration files when smart proxies are
	 * deployed with a standard off the shelf {@link ServiceProvider}.
	 * 
	 * @return service UI descriptor
	 */
	public static UIDescriptor getUIDescriptor() {
		UIDescriptor uiDesc = null;
		try {
			uiDesc = UIDescriptorFactory.getUIDescriptor(MainUI.ROLE,
					new UIComponentFactory(new URL[] { new URL(Sorcer
							.getWebsterUrl()
							+ "/shelter-ui.jar") }, ShelterUI.class.getName()));
		} catch (Exception ex) {
			logger.throwing(ShelterUI.class.getName(), "getUIDescriptor", ex);
		}
		return uiDesc;
	}
}
