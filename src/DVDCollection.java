import java.io.*;
import java.io.File;
import java.util.Scanner;

public class DVDCollection {
	// Data fields
	/** The current number of DVDs in the array */
	private int numdvds;
	/** The array to contain the DVDs */
	private DVD[] dvdArray;
	/** The name of the data file that contains dvd data */
	private String sourceName;
	/** Boolean flag to indicate whether the DVD collection was
	    modified since it was last saved. */
	private boolean modified;
	/**
	 *  Constructs an empty directory as an array
	 *  with an initial capacity of 7. When we try to
	 *  insert into a full array, we will double the size of
	 *  the array first.
	 */
	public DVDCollection() {
		numdvds = 0;
		dvdArray = new DVD[7]; // manually increase size if needed by 7, part of assignment
	}
	
	public String toString() {
		// Return a string containing all the DVDs in the
		// order they are stored in the array along with
		// the values for numdvds and the length of the array.
		// See homework instructions for proper format.
		
		System.out.println("numdvds = " + numdvds);
		System.out.println("dvdArray.length = " + dvdArray.length);
		
		String allDVDs = new String();
		
		allDVDs.concat("numdvds = " + numdvds + "\n");
		allDVDs.concat("dvdArray.length = " + dvdArray.length + "\n");
		
		
		for (int i = 0; i < numdvds-1; i++) {
			allDVDs.concat(dvdArray[i].toString() + "\n");
		}
		return allDVDs;
	}

	public void addOrModifyDVD(String title, String rating, String runningTime) {
		// NOTE: Be careful. Running time is a string here
		// since the user might enter non-digits when prompted.
		// If the array is full and a new DVD needs to be added,
		// double the size of the array first.
		// create new dvd instance with the parameters and put in the array

		if (numdvds == dvdArray.length) {
			DVD[] newArray = new DVD[dvdArray.length * 2];
			for (int i = 0; i < dvdArray.length; i++) {
				newArray[i] = dvdArray[i];
			}
			dvdArray = newArray;
		}
		
		if (rating == "G" || rating == "PG" || rating == "PG-13" || rating == "R" || rating == "NC-17" || rating == "NR") {
			System.out.println("Valid rating detected. \n");
			/*
			StringBuilder r = new StringBuilder();
			for (char c : runningTime.toCharArray()) {
				if (Character.isDigit(c)) {
					r.append(c);
				}
			} Idk if i can parse runningTime for digits only so enforce integers
			*/
		try {
			if (Integer.parseInt(runningTime) <= 0) {
				System.out.println("Invalid running time, must be a positive integer");
				return;
            }
		}
		 catch (NumberFormatException e) {
			 System.out.println("Please input running time as just a positive integer");
			 return;
		 }
			int compare = 0;
			for (int i = 0; i < numdvds; i++) {
				if (title == dvdArray[i].getTitle()) {
					System.out.println("DVD already exists, modifying existing DVD");
					dvdArray[i].setRating(rating);
					dvdArray[i].setRunningTime(Integer.parseInt(runningTime));
					modified = true;
					return;
				}
				compare = title.compareTo(dvdArray[i].getTitle());
				if (compare < 0) {
					DVD[] newArray = new DVD[dvdArray.length];
					for (int j = 0; j < numdvds; j++) {
						if (j == i) {
							newArray[j] = new DVD(title, rating, Integer.parseInt(runningTime));
						}
						else {
							newArray[j] = dvdArray[j];
						}
					}
					dvdArray = newArray;
					numdvds++;
					modified = true;
					return;
				}
			}
		}
	}
	
	public void removeDVD(String title) {
		for (int i = 0; i < numdvds; i++) {
			if (dvdArray[i].getTitle() == title) {
				System.out.println("Removing DVD: " + title + "\n");
				DVD[] newArray = new DVD[dvdArray.length];
				for (int j = 0; j < numdvds - 1; j++) {
					if (j == i) {
						continue;
					}
					else {
						newArray[j] = dvdArray[j];
					}
				}
				dvdArray = newArray;
				System.out.println("DVD removed: " + title + "\n");
				modified = true;
				return;
			}
		}
		System.out.println("DVD not found: " + title + "\n");
	}
	
	public String getDVDsByRating(String rating) {
		String d = new String();
		
		for (int i = 0; i < numdvds; i++) {
			if (dvdArray[i].getRating() == rating) {
				d.concat(dvdArray[i].getTitle() + "\n");
			}
		}
		if (d == "") {
			System.out.println("No DVDs found with rating: " + rating);
		}
        return d;
	}

	public int getTotalRunningTime() {
		if (numdvds == 0) {
			return 0;
		}
        int total = 0;
		for (int i = 0; i < numdvds; i++) {
			total += dvdArray[i].getRunningTime();
		}
		return total;
	}

	
	public void loadData(String filename) {
		System.out.println("Current file path: ");
		System.out.println(System.getProperty("user.dir"));
		File folder = new File(System.getProperty("user.dir"));
		File[] listOfFiles = folder.listFiles();
		
		System.out.println("Contents of " + System.getProperty("user.dir") + ":");
		for (File file : listOfFiles) {
			if (file.isFile()) {
				System.out.print(file.getName() + " and ");
			}
		}
		
		File file = new File("dvd.txt");
		if (file.exists()) {
			try {
				System.out.println("File found, loading data from " + filename);
				Scanner scanner = new Scanner(file);
				while (scanner.hasNextLine()) {
					String data = scanner.nextLine();
					String[] parts = data.split(",");
					addOrModifyDVD(parts[0], parts[1], parts[2]);
				}
				scanner.close();
				sourceName = filename;
			} catch (Exception e) {
			System.out.println("Error code encountered: " + e);
			return;
			}
		}
	}
	
	public void save() {
		System.out.println("Current file path:");
		System.out.println(System.getProperty("user.dir"));
		File folder = new File(System.getProperty("user.dir"));
		File[] listOfFiles = folder.listFiles();
		
		System.out.println("Contents of " + System.getProperty("user.dir") + ":");
		for (File file : listOfFiles) {
			if (file.isFile()) {
				System.out.println(file.getName());
			}
		}
		
		File file = new File("dvd.txt");
		
		try {
			if (file.createNewFile()) {
				FileWriter myWriter = new FileWriter("dvd.txt");
				myWriter.write(dvdArray.toString());
				myWriter.close();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		File myFile = new File("dvd.txt");

		System.out.println("DVD collection saved to " + sourceName);
		System.out.println("Contents of dvd.txt: ");
		try {
			Scanner scanner = new Scanner(myFile);
			while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                System.out.println(data);
            }
			scanner.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		modified = false;
	}

	// Additional private helper methods go here:

	public static void main (String args[]) {
		DVDCollection d = new DVDCollection();
		System.out.println(d.dvdArray.toString());
		d.addOrModifyDVD("THE MATRIX","R","136");
		System.out.println(d.dvdArray.toString());
		d.getDVDsByRating("PG");
		System.out.println(d.getTotalRunningTime());
		d.removeDVD("THE MATRIX");
		System.out.println(d.dvdArray.toString());
		d.loadData("dvd.txt");
		System.out.println(d.dvdArray.toString());
		d.save();
        return;
	}
}
