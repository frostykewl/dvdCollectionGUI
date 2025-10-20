import java.io.*;
import java.util.Arrays;
import java.util.Comparator;

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
		
		String allDVDs = "";
		allDVDs += "numdvds = " + numdvds + '\n';
		allDVDs += "dvdArray.length = " + dvdArray.length + '\n';
		for (int i = 0; i < numdvds; i++) {
			allDVDs += "dvdArray[" + i + "] = " + dvdArray[i].toString() + '\n';
		}
		return allDVDs;
	}

	// NOTE: Be careful. Running time is a string here
			// since the user might enter non-digits when prompted.
			// If the array is full and a new DVD needs to be added,
			// double the size of the array first.
			// create new dvd instance with the parameters and put in the array
	
	public void addOrModifyDVD(String title, String rating, String runningTime) {
		if (!checkRating(rating)) {
			return;
		}
		int runTime = checkRunningTime(runningTime);
		if (runTime < 1) {
			return;
		}
		modified = true;
		title = title.toUpperCase();
		rating = rating.toUpperCase();
		// if dvd entry found, update
		// if not, add new entry
		int index = findDVD(title);
		if (index >= 0) {
			System.out.println("DVD found at index [" + index + "], updating entry: " + dvdArray[index].toString());
			dvdArray[index].setTitle(title);
			dvdArray[index].setRating(rating);
			dvdArray[index].setRunningTime(runTime);
			System.out.println("Entry [" + index + "] " + dvdArray[index].toString());
		}
		else {
			if (numdvds < dvdArray.length) {
				dvdArray[numdvds] = new DVD(title, rating, runTime);
				System.out.println("Entry Added: [" + numdvds + "] " + dvdArray[numdvds].toString());
				numdvds++;
				Arrays.sort(dvdArray, 0, numdvds, new DVDTitleSort());
			}
			else {
				System.out.println("Array full, doubling array size.");
				doubleDVDArraySize();
				System.out.println("Trying again.");
				addOrModifyDVD(title, rating, runningTime);
				System.out.println(toString());
			}
		}
		return;
	}
		
		
	public void removeDVD(String title) {
		title = title.toUpperCase();
		int index = findDVD(title);
		if (index >= 0) {
			System.out.println("Entry found, removing: " + title + " at index [" + index + "] \n");
			modified = true;
			DVD[] newArray = new DVD[dvdArray.length - 1];
			System.arraycopy(dvdArray,  0,  newArray,  0, index);
			System.arraycopy(dvdArray,  index + 1, newArray, index, dvdArray.length - index - 1);
			dvdArray = newArray;
			numdvds--;
			return;
		}
		else {
			System.out.println("DVD not found.");
		}
	}
	
	public String getDVDsByRating(String rating) {
		rating = rating.toUpperCase();
		String d = "";
		for (int i = 0; i < numdvds; i++) {
			if (dvdArray[i].getRating().equals(rating)) {
				d += (dvdArray[i].toString() + '\n');
			}
		}
		if (d.equals("")) {
			System.out.println("No DVDs found with that rating.");
		}
        return d;
	}

	public int getTotalRunningTime() {
		int total = 0;
		for (int i = 0; i < numdvds; i++) {
			total += dvdArray[i].getRunningTime();
		}
		return total;
	}
	
	public void loadData(String filename) {
		try {
		this.sourceName = filename;
		FileReader inFile = new FileReader(this.sourceName);
		BufferedReader bRead = new BufferedReader(inFile);
		String line;
			while ((line = bRead.readLine()) != null) {
				String[] dvdData = line.split(",");
				if (dvdData.length < 3) {
					System.out.println("Invalid DVD Entry in file \"" + line + "\"");
					return;
				}
				addOrModifyDVD(dvdData[0], dvdData[1], dvdData[2]);
			}
			modified = false;
		}
		catch (Exception e) {
			System.out.println(e);
			this.dvdArray = new DVD[7];
			this.numdvds = 0;
		}
	}
	
	public void save() {
		try {
			if (!modified) {
				System.out.println("No changes detected.");
				return;
			}
			FileWriter fw = new FileWriter(sourceName);
			if (numdvds > 0) {
				for (int i = 0; i < numdvds; i++) {
					fw.write(dvdArray[i].getTitle() + "," + dvdArray[i].getRating() + "," + dvdArray[i].getRunningTime());
					fw.write(System.lineSeparator());
				}
			}
			fw.close();
			System.out.println("File saved to " + sourceName + ".");
			modified = false;
		}
		catch (IOException e) {
			System.err.println("Error saving file: " + e);
		}
	}

	// Additional private helper methods go here:
	// Check if valid rating
	private boolean checkRating(String rating) {
		rating = rating.toUpperCase();
		if (rating.equals("R") || rating.equals("NC-17") || rating.equals("PG-13") || rating.equals("PG") || rating.equals("G")) {
			return true;
		}
		else {
			System.out.println("Invalid movie rating: " + rating + "\n");
			return false;
		}
	}
	
	// Check if valid runningTime
	private int checkRunningTime(String runningTime) {
		int runTime = 0;
		try {
			runTime = Integer.parseInt(runningTime);
		}
		catch (NumberFormatException e) {
			System.out.println("Invalid runtime.");
		}
		return runTime;
	}
	
	// Find index of dvd
	private int findDVD(String title) {
		if (numdvds > 0) {
			for (int i = 0; i < numdvds; i++) {
				if(dvdArray[i].getTitle().equals(title)) {
					return i;
				}
			}
		}
		return -1;
	}
	
	// Double size of dvdArray for more space
	private void doubleDVDArraySize() {
		DVD[] newArray = new DVD[dvdArray.length * 2];
		System.arraycopy(dvdArray, 0, newArray, 0, numdvds);
		dvdArray = newArray;
		return;
	}
	
	class DVDTitleSort implements Comparator<DVD> {
		public int compare(DVD a, DVD b) {
			return a.getTitle().compareTo(b.getTitle());
		}
	}
}
