/*
 * Author: Alexander Anderson
 * Spring 2014
 */
package a09;

import javax.swing.ImageIcon;

/*
 *holds the objective information along with the images for the missions.
 */
public enum ObjectivesEnum {
	GOLDENEYE("Encryption Chip Retrieval", "Rescue the hostages and recover any enemy intel you can find.",
			43, 10, 132, 0, "Vladivostok, Russia", 5, "AM", "GOLDENEYE", "images/Russia Mission Map.jpg"), 
	GHOST("Observe the Unobserved", "Spy on the HYDRA meeting and find where their next target is.", 
			35, 40, 139, 45, "Tokyo, Japan", 12, "PM", "GHOST", "images/Tokyo Mission Map.jpg"), 
	JUGGERNAUT("Tell No Tales", "Assassinate Strucker", 
			50, 7, 8, 41, "Frankfurt, Germany", 9, "AM", "JUGGERNAUT", "images/Germany Mission.jpg"), 
	TIGER("Catch the Jackrabbit", "Capture the rogue agent ALIVE!", 
			22, 57, 43, 12, "Rio de Janeiro, Brazil", 10, "AM", "TIGER", "images/Rio Mission.jpg"), 
	RABBITHOLE("Need More Allies", "Break our informant out of Federal Prison.", 
			41, 30, 75, 25, "Canaan, PA, USA", 4, "PM", "RABBITHOLE", "images/Canaan Bars.jpg");

	private String title, obj, mornEve, operation, location;
	private int longDegrees, longMinutes, latDegrees, latMinutes, time;
	private ImageIcon img;

	private ObjectivesEnum(String title, String obj, int longDegrees,
			int longMinutes, int latDegrees, int latMinutes, String location, int time, String mornEve, 
			String operation, String imagePath) {
		this.title = title;
		this.obj = obj;
		this.longDegrees = longDegrees;
		this.longMinutes = longMinutes;
		this.latDegrees = latDegrees;
		this.latMinutes = latMinutes;
		this.location = location;
		this.time = time;
		this.mornEve = mornEve;
		this.operation = operation;
		setImg(new ImageIcon(getClass().getResource(imagePath)));
	}

	public String getTitle() {
		return title;
	}

	public String getObj() {
		return obj;
	}

	public int getLongDegrees() {
		return longDegrees;
	}

	public int getLongMinutes() {
		return longMinutes;
	}

	public int getLatDegrees() {
		return latDegrees;
	}

	public int getLatMinutes() {
		return latMinutes;
	}
	public String getMornEve(){
		return mornEve;
	}

	public String getLocation() {
		return location;
	}

	public int getTime() {
		return time;
	}

	public String getOperation(){
		return operation;
	}

	public ImageIcon getImg() {
		return img;
	}

	public void setImg(ImageIcon img) {
		this.img = img;
	}

	public String toString(){
		return this.name() + ": " + title + ": " + "Objective: " + obj + "\n"
				+ "Location: " + location
				+ " Longitude: " + longDegrees + "\u00B0" + " " + longMinutes + "\u0027"
				+ " Latitude: " + latDegrees + "\u00B0" + " " + latMinutes + "\u0027\n"
				+ "Time: " + time + ":00 " + mornEve;
	}

	public static String enumInformationForGui(ObjectivesEnum e) {
		return "<html>Location: " + e.getLocation() + "<br>" 
				+ "Long: " + e.getLongDegrees() + "\u00B0 " + e.getLongMinutes() + "\u0027  "
				+ "Lat: " + e.getLatDegrees() + "\u00B0" + e.getLatMinutes() + "\u0027";
	}

	public static String sideObjective(ObjectivesEnum e) {
		StringBuilder tempStr = new StringBuilder();

		tempStr.append("<html>" + e.getObj() + "<br>" + "Starting Time: " + e.getTime() + ":00 " 
				+ e.getMornEve() + "<br><br>");
		if (e == ObjectivesEnum.GOLDENEYE) {
			tempStr.append("<html>\u2022 Rescue all 15 hostages<br>");
			tempStr.append("<html>\u2022 Recover any enemy intel<br>");
			tempStr.append("<html>\u2022 Suppressive fire only<br>");
			tempStr.append("<html>\u2022 Recover the Encryption Chip<br>");
			tempStr.append("<html>\u2022 No enemy Detection<br>");
		}
		if (e == ObjectivesEnum.GHOST) {
			tempStr.append("<html>\u2022 Use the equipment we give you to break into the building<br>");
			tempStr.append("<html>\u2022 Make note of what HYDRA plans to do next<br>");
			tempStr.append("<html>\u2022 No lethal force, use tranquilizer darts only<br>");
			tempStr.append("<html>\u2022 Get out of there, if things get too hot<br>");
			tempStr.append("<html>\u2022 Don't get Caught!<br>");
		}
		if (e == ObjectivesEnum.JUGGERNAUT) {
			tempStr.append("<html>\u2022 Intercept the limo escorting Strucker to airport<br>");
			tempStr.append("<html>\u2022 Don't let him out of your sight<br>");
			tempStr.append("<html>\u2022 Use the specialized sniper rifle we provide for you<br>");
			tempStr.append("<html>\u2022 Assassinate Strucker<br>");
			tempStr.append("<html>\u2022 Rendezvous at the extraction point<br>");
		}
		if (e == ObjectivesEnum.TIGER) {
			tempStr.append("<html>\u2022 Locate the rogue agent Ward<br>");
			tempStr.append("<html>\u2022 No collateral damage encouraged<br>");
			tempStr.append("<html>\u2022 Use guns for intent to injure ONLY<br>");
			tempStr.append("<html>\u2022 Capture Ward ALIVE!<br>");
			tempStr.append("<html>\u2022 Bring him back to US soil for punishment<br>");
		}
		if (e == ObjectivesEnum.RABBITHOLE) {
			tempStr.append("<html>\u2022 Air drop in on top of the building<br>");
			tempStr.append("<html>\u2022 Knock out security<br>");
			tempStr.append("<html>\u2022 Use lethal force as last resort<br>");
			tempStr.append("<html>\u2022 Plant the charges we give you to bust open the side wall<br>");
			tempStr.append("<html>\u2022 Get on the chopper to extract you - limited time window<br>");
		}
		return tempStr.toString();
	}
}
