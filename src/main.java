
public class main {
	public static void main(String[] args) {
		DVDCollection obj = new DVDCollection();
		obj.loadData("dvd.txt");
		System.out.println("Current DVD Array: " + obj.toString());
		System.out.println(" DVDs by Rating R: \n" + obj.getDVDsByRating("R"));
		obj.addOrModifyDVD("Zombieland", "R", "129");
		obj.addOrModifyDVD("Superbad", "R", "112");
		System.out.println("Total Run Time: " + obj.getTotalRunningTime());
		obj.removeDVD("Zombieland");
		System.out.println(obj.toString());
		return;
	}
}
