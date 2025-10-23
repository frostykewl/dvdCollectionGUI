import java.awt.*;
import javax.swing.*;

/**
 *  This class is an implementation of DVDUserInterface
 *  that uses JOptionPane to display the menu of command choices. 
 */

public class DVDGUI implements DVDUserInterface {
	 
	 private DVDCollection dvdlist;
	 private JFrame frame;
	 private JList<DVD> dvdJList;
	 private DefaultListModel<DVD> listModel;
	 private JTextArea infoBox;
	 private JComboBox<String> ratingFilter;
	 private JLabel dvdImg;
	 
	 public DVDGUI(DVDCollection dl)
	 {
		 dvdlist = dl;
		 createGUI();
		 loadDVDs();
	 }
	 
	 private void createGUI() {
		 // Initialize GUI
		 frame = new JFrame ("DVD Manager");
		 frame.setLayout(new BorderLayout());
		 frame.setSize(800, 500);
		 frame.setTitle("DVD List");
		 listModel = new DefaultListModel<>();
		 dvdJList = new JList<>(listModel);
		 dvdJList.addListSelectionListener(e -> displayDVDInfo());
		 JScrollPane listScrollPane = new JScrollPane(dvdJList);
		 
		 infoBox = new JTextArea();
		 infoBox.setEditable(false);
		 
		 JPanel buttonPanel = new JPanel();
		 
		 // Add each button and map each action
		 JButton addButton = new JButton("Add/Modify DVD");
		 addButton.addActionListener(e -> doAddOrModifyDVD());
		 buttonPanel.add(addButton);
		 
		 JButton removeButton = new JButton("Remove Selected DVD");
		 removeButton.addActionListener(e -> doRemoveDVD());
		 buttonPanel.add(removeButton);
		 
		 JButton totalRuntimeButton = new JButton("Total Run Time");
		 totalRuntimeButton.addActionListener(e -> doGetTotalRunningTime());
		 buttonPanel.add(totalRuntimeButton);
		 
		 JButton saveButton = new JButton("Save");
		 saveButton.addActionListener(e -> doSave());
		 buttonPanel.add(saveButton);
		 
		 JButton exitButton = new JButton("Exit");
		 exitButton.addActionListener(e -> doExit());
		 buttonPanel.add(exitButton);
		 
		 dvdImg = new JLabel();
		 buttonPanel.add(dvdImg);
		 dvdImg.setPreferredSize(new java.awt.Dimension(100, 200));
		 dvdImg.setIcon(null);
		 dvdImg.setText("Image goes here.");
		 
		 
		 ratingFilter = new JComboBox<>(new String[] {
				 "All Ratings", "PG", "PG-13", "R"
		 });
		 ratingFilter.addActionListener(e -> filterDVDRating());
		 buttonPanel.add(ratingFilter);
		 frame.add(dvdImg, BorderLayout.EAST);
		 frame.add(buttonPanel, BorderLayout.NORTH);
		 frame.add(listScrollPane, BorderLayout.WEST);
		 frame.add(new JScrollPane(infoBox), BorderLayout.CENTER);
		 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 frame.setVisible(true);
	 }
	 
	 private void setDVDImage(String title) {
		// To-do add in movie images
		
	}

	 private void loadDVDs() {
		 listModel.clear();
		 for (int i = 0; i < dvdlist.getNumDVDs(); i++) {
			 listModel.addElement(dvdlist.getDVD(i));
		 }
	 }
	 
	 private void displayDVDInfo() {
		 DVD selectedDVD = dvdJList.getSelectedValue();
		 if (selectedDVD != null) {
			 infoBox.setText("Title: " + selectedDVD.getTitle() + "\n" + "Rating: " + selectedDVD.getRating() + "\n" + "Running Time: " + selectedDVD.getRunningTime() + "min");
		 }
	 }
	 
	 private void filterDVDRating() {
		 String selectedRating = (String) ratingFilter.getSelectedItem();
		 listModel.clear();
		 for (int i = 0; i < dvdlist.getNumDVDs(); i++) {
			 DVD dvd = dvdlist.getDVD(i);
			 if (selectedRating.equals("All Ratings") || dvd.getRating().equals(selectedRating)) {
				 listModel.addElement(dvd);
			 }
		}
		 return;
	 }
		 
	 
	private void doGetTotalRunningTime() {
		int total = dvdlist.getTotalRunningTime();
		JOptionPane.showMessageDialog(frame, "Total Running Time of DVDS: " + total + " min");
	}
	 
	private void doAddOrModifyDVD() {
		// Request the title
		String title = JOptionPane.showInputDialog("Enter title");
		if (title == null) {
			return;		// dialog was cancelled
		}
		title = title.toUpperCase();
		
		// Request the rating
		String rating = JOptionPane.showInputDialog("Enter rating for " + title);
		if (rating == null) {
			return;		// dialog was cancelled
		}
		rating = rating.toUpperCase();
		
		// Request the running time
		String time = JOptionPane.showInputDialog("Enter running time for " + title);
		if (time == null) {
			return;
		}
		try {
			// Add or modify the DVD (assuming the rating and time are valid
			dvdlist.addOrModifyDVD(title, rating, time);
			loadDVDs();
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(frame, "Invalid running time.");
		}
	}
	
	private void doRemoveDVD() {
		DVD selectedDVD = dvdJList.getSelectedValue();
		if (selectedDVD != null) {
			dvdlist.removeDVD(selectedDVD.getTitle());
			loadDVDs();
		} else {
			JOptionPane.showMessageDialog(frame,  "No DVD Selected.");
		}
	}
	/* Commented out Console Commands
	private void doGetDVDsByRating() {

		// Request the rating
		String rating = JOptionPane.showInputDialog("Enter rating");
		if (rating == null) {
			return;		// dialog was cancelled
		}
		rating = rating.toUpperCase();
                String results = dvdlist.getDVDsByRating(rating);
                System.out.println("DVDs with rating " + rating);
                System.out.println(results);
	}

        private void doGetTotalRunningTime() {
                int total = dvdlist.getTotalRunningTime();
                System.out.println("Total Running Time of DVDs: ");
                System.out.println(total);
        }
	*/
	
	private void doSave() {
		dvdlist.save();
	}
	
	private void doExit() {
		System.exit(0);	}

	@Override
	public void processCommands() {
		// TODO Auto-generated method stub
	}
}
