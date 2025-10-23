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
	 private JLabel imageBox;
	 private JComboBox<String> ratingFilter;

	 
	 public DVDGUI(DVDCollection dl)
	 {
		 dvdlist = dl;
		 createGUI();
		 loadDVDs();
	 }
	 
	 private void createGUI() {
		 frame = new JFrame ("DVD Manager");
		 frame.setLayout(new BorderLayout());
		 listModel = new DefaultListModel<>();
		 dvdJList = new JList<>(listModel);
		 dvdJList.addListSelectionListener(e -> displayDVDInfo());
		 JScrollPane listScrollPane = new JScrollPane(dvdJList);
		 
		 infoBox = new JTextArea();
		 infoBox.setEditable(false);
		 imageBox = new JLabel();
		 imageBox.setHorizontalAlignment(JLabel.CENTER);
		 imageBox.setPreferredSize(new Dimension(300, 200));
		 JPanel buttonPanel = new JPanel();
		 
		 JButton addButton = new JButton("Add/Modify DVD");
		 addButton.addActionListener(e -> doAddOrModifyDVD());
		 buttonPanel.add(addButton);
		 
		 JButton removeButton = new JButton("Remove Selected DVD");
		 removeButton.addActionListener(e -> doRemoveDVD());
		 buttonPanel.add(removeButton);
		 
		 JButton totalRuntimeButton = new JButton("Total Run Time");
		 totalRuntimeButton.addActionListener(e -> doGetTotalRunningTime());
		 buttonPanel.add(totalRuntimeButton);
		 
		 JButton exitButton = new JButton("Exit");
		 exitButton.addActionListener(e -> doSave());
		 buttonPanel.add(exitButton);
		 
		 ratingFilter = new JComboBox<>(new String[] {
				 "All Ratings", "PG", "PG-13", "R"
		 });
		 ratingFilter.addActionListener(e -> filterDVDRating());
		 buttonPanel.add(ratingFilter);
		 frame.add(imageBox, BorderLayout.SOUTH);
		 frame.add(buttonPanel, BorderLayout.NORTH);
		 frame.add(listScrollPane, BorderLayout.WEST);
		 frame.add(new JScrollPane(infoBox), BorderLayout.CENTER);
		 frame.setVisible(true);
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
			 infoBox.setText("Title: " + selectedDVD.getTitle() + "/n" + "Rating: " + selectedDvd.getRating() + "/n" + "Running Time: " + selectedDVD.getRunningTime() + "min");
			 
			 // Load images
			 String imagePath = selectedDVD.getImage();
			 ImageIcon dvdImage = new ImageIcon(imagePath);
			 imageBox.setIcon(dvdImage);
		 }
	 }
	 
	 private void filterDVDRating() {
		 String selectedRating = (String) ratingFilter.getSelectedItem();
		 listModel.clear();
		 for (int i = 0; i < dvdlist.getNumDVDs(); i++) {
			 DVD dvd = dvdlist.getDVD(i);
			 if (dvd.getRating().equals(selectedRating)) {
				 listModel.addElement(dvd);
			 }
		}
		 return;
	 }
		 
	/*
	 public void processCommands() {
		 
	 }
	*/
	 
	 
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
		// Request the title
		String title = JOptionPane.showInputDialog("Enter title");
		if (title == null) {
			return;		// dialog was cancelled
		}
		title = title.toUpperCase();
                // Remove the matching DVD if found
        dvdlist.removeDVD(title);
                // Display current collection to the console for debugging
        System.out.println("Removing: " + title);
        System.out.println(dvdlist);
	}
	
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

	private void doSave() {
		dvdlist.save();
		System.exit(0);
	}
		
}
