import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 *  This class is an implementation of DVDUserInterface
 *  that uses JOptionPane to display the menu of command choices. 
 */

public class DVDGUI implements DVDUserInterface {
	 
	 private DVDCollection dvdlist;
	 private static JFrame frame;
	 private static JPanel panel;
	 private JList dvdJList;
	 JScrollPane dvdListScroll;
	 static String titleText = ("Title: ");
	 static String ratingText = ("Rating: ");
	 static String runTimeText = ("Runtime: ");

	 
	 public DVDGUI(DVDCollection dl)
	 {
		 dvdlist = dl;
		 createGUI();
		 loadDVDs();
	 }
	 
	 private static void createGUI() {
		 // Create initial frame + panel
		 frame = new JFrame("DVD GUI");
		 frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		 JPanel listPanel = new JPanel();
		 frame.add(listPanel);
		 Box infoBox = Box.createVerticalBox();
		 JLabel dvdTitleJLabel = new JLabel(titleText);
		 JLabel dvdRatingJLabel = new JLabel(ratingText);
		 JLabel dvdRuntimeJLabel = new JLabel(runTimeText);
		 JLabel dvdImageJLabel = new JLabel("404 Image");
		 infoBox.add(dvdTitleJLabel);
		 infoBox.add(dvdRatingJLabel);
		 infoBox.add(dvdRuntimeJLabel);
		 infoBox.add(dvdImageJLabel);
		 JButton modifyButton = new JButton("Modify DVD");
		 modifyButton.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent e) {
				 try {
					 String title = dvdlist.getDVDArray()[dvdJList.getSelectedIndex()].getTitle();
					 doAddOrModifyDVD(title);
				 }
				 catch (Exception error) {
				 } 
			 }
		 });
		 infoBox.add(modifyButton);
		 listPanel.add(infoBox);
		 
		 dvdJList = new JList();
	 }
	 
	 /* Commented out console commands
	 public void processCommands()
	 {
		 String[] commands = {"Add/Modify DVD",
				 	"Remove DVD",
				 	"Get DVDs By Rating",
				 	"Get Total Running Time",
				 	"Exit and Save"};
		 
		 int choice;
		 
		 do {
			 choice = JOptionPane.showOptionDialog(null,
					 "Select a command", 
					 "DVD Collection", 
					 JOptionPane.YES_NO_CANCEL_OPTION, 
					 JOptionPane.QUESTION_MESSAGE, 
					 null, 
					 commands,
					 commands[commands.length - 1]);
		 
			 switch (choice) {
			 	case 0: doAddOrModifyDVD(); break;
			 	case 1: doRemoveDVD(); break;
			 	case 2: doGetDVDsByRating(); break;
			 	case 3: doGetTotalRunningTime(); break;
			 	case 4: doSave(); break;
			 	default:  // do nothing
			 }
			 
		 } while (choice != commands.length-1);
		 System.exit(0);
	 }
	 */
	 
	private void doAddOrModifyDVD(String title) {
		// Request the title
		title = JOptionPane.showInputDialog("Enter title");
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
		if (time == null) ;
		}
                // Add or modify the DVD (assuming the rating and time are valid
                dvdlist.addOrModifyDVD(title, rating, time);
                refreshDVDJList();
		
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
		
	}
		
}
