package personal;

import java.util.LinkedList;

public class Personal {
	public int age;
	public boolean sex;
	//1 - no; 3 - a lot 
	public int activityLevel;
	public int [] restrictions;
	public LinkedList <String> stationPrefs;
	int [] mealsToday;
	
	public Personal(int age, boolean sex,int activityLevel, int [] restrictions,	LinkedList <String>stationPrefs){
		this.sex = sex;
		this.activityLevel = activityLevel;
		this.age = age;
		//this.mealsToday = mealsToday;
		this.restrictions = restrictions;
		this.stationPrefs = stationPrefs;
	}
}
