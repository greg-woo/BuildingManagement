
/*
Created by: GREG WOO
Program: Manage buildings using a tree structure
*/

public class Building {

	OneBuilding data;
	Building older;
	Building same;
	Building younger;

	public Building(OneBuilding data){
		this.data = data;
		this.older = null;
		this.same = null;
		this.younger = null;
	}

	public String toString(){
		String result = this.data.toString() + "\n";
		if (this.older != null){
			result += "older than " + this.data.toString() + " :\n";
			result += this.older.toString();
		}
		if (this.same != null){
			result += "same age as " + this.data.toString() + " :\n";
			result += this.same.toString();
		}
		if (this.younger != null){
			result += "younger than " + this.data.toString() + " :\n";
			result += this.younger.toString();
		}
		return result;
	}

	public Building addBuilding (OneBuilding b){

		if(b == null) {
			return this;
		}

		Building building_new = new Building(b);

		// b older
		if(building_new.data.yearOfConstruction < this.data.yearOfConstruction) {

			if(this.older == null){
				this.older = building_new;

			} else {
				this.older = this.older.addBuilding(b);
			}

			// younger
		} else if (building_new.data.yearOfConstruction > this.data.yearOfConstruction) {

			if(this.younger == null){
				this.younger = building_new;

			} else {
				this.younger = this.younger.addBuilding(b);
			}

			//same
		} else {

			if(this.data.height < building_new.data.height) {

				building_new.younger = this.younger;
				building_new.older = this.older;

				this.older = null;
				this.younger = null;

				building_new.same = this;

				return building_new;

			} else {

				if(this.same == null){
					this.same = building_new;

				} else {
					this.same = this.same.addBuilding(b);
				}
			}
		}

		return this;
	}

	public Building addBuildings (Building b){

		if(b == null) {
			return this;
		}

		Building temp = this;
		temp = temp.addBuilding(b.data);

		if(b.older != null) {
			temp = temp.addBuildings(b.older);
		}

		if(b.same != null) {
			temp = temp.addBuildings(b.same);
		}
		if(b.younger != null) {
			temp = temp.addBuildings(b.younger);
		}

		return temp;
	}

	public Building removeBuilding (OneBuilding b){

		if(this.data.equals(b)) {

			if(this.same != null) {

				if(this.younger != null) {
					this.same.addBuildings(this.younger);
				}
				if(this.older != null) {
					this.same.addBuildings(this.older);
				}

				return this.same;

			} else if ( this.older != null ) {
				this.older.addBuildings(this.younger);
				return this.older;

			} else {
				return this.younger;
			}

		} else {


			if( this.older != null ) {

				if(this.older.data.equals(b)) {

					this.older = this.older.removeBuilding(b);
					return this;

				} else {

					this.older = this.older.removeBuilding(b);
				}
			}

			if ( this.same != null ) {

				if(this.same.data.equals(b)) {
					this.same = this.same.removeBuilding(b);
					return this;

				} else {
					this.same = this.same.removeBuilding(b);
				}
			}

			if ( this.younger != null ) {

				if(this.younger.data.equals(b)) {
					this.younger = this.younger.removeBuilding(b);
					return this;

				} else {
					this.younger = this.younger.removeBuilding(b);
				}
			}
		}

		return this;
	}

	public int oldest(){
		// ADD YOUR CODE HERE

		if(this.older != null) {
			return this.older.oldest();

		} else {
			return this.data.yearOfConstruction;

		}
	}

	public int highest(){

		int highLocal = this.data.height;

		if (this.older == null && this.younger == null) {
			return this.data.height;
		}

		if (this.older != null) {
			if (this.older.highest() > this.data.height) {
				highLocal = this.older.highest();
			}
		}

		if (this.younger != null) {
			if (this.younger.highest() > highLocal) {
				highLocal = this.younger.highest();
			}
		}
		return highLocal;

	}

	public OneBuilding highestFromYear (int year){

		if(this.data.yearOfConstruction != year) {

			if(this.data.yearOfConstruction > year) {
				// look in older

				if((year > this.older.data.yearOfConstruction) && (this.older.younger == null)) {
					return null;

				} else {
					return this.older.highestFromYear(year);
				}

			} else {
				//look in younger

				if((year < this.younger.data.yearOfConstruction) && (this.younger.older == null)) {
					return null;

				} else {
					return this.younger.highestFromYear(year);
				}
			}

		} else {

			if(this.data.height == this.highest()) {
				return this.data;

			} else {

				if( this.same != null ) {
					return this.same.highestFromYear(year);

				} else {
					return null;
				}
			}
		}
	}

	public int numberFromYears (int yearMin, int yearMax){

		int counter = 0;

		if(yearMin > yearMax) {
			return 0;

		} else {
			// 3 parts (older, same and younger)

			if( (yearMin <= this.data.yearOfConstruction) && (yearMax >= this.data.yearOfConstruction) ) {
				//this means that this is in the interval

				counter ++;

				if( this.same != null ) {
					counter += this.same.numberFromYears(yearMin, yearMax);
				}

				if( this.older != null ) {
					counter += this.older.numberFromYears(yearMin, yearMax);
				}

				if( this.younger != null ) {
					counter += this.younger.numberFromYears(yearMin, yearMax);
				}

			} else {
				// not in interval

				if( this.older != null ) {
					counter += this.older.numberFromYears(yearMin, yearMax);
				}

				if( this.younger != null ) {
					counter += this.younger.numberFromYears(yearMin, yearMax);
				}
			}
		}
		return counter;
	}

	public int[] costPlanning (int nbYears){

		if(nbYears < 0) {
			return null;

		} else {

			// initialize the array
			int[] totalCost = new int[nbYears];

			for(int i = 0; i < nbYears; i++) {
				totalCost[i] = 0;

			}

			int yearDifference = this.data.yearForRepair - 2018;

			if( (yearDifference < nbYears) && (yearDifference >= 0) ) {
				totalCost[yearDifference] += this.data.costForRepair;
			}

			if( this.same != null ) {

				yearDifference = this.same.data.yearForRepair - 2018;
				int[] temp = this.same.costPlanning(nbYears);

				for(int j = 0; j < nbYears; j++) {

					if(temp[j] != 0) {
						totalCost[j] += temp[j];
					}
				}
			}

			if( this.older != null ) {

				yearDifference = this.older.data.yearForRepair - 2018;
				int[] temp = this.older.costPlanning(nbYears);

				for(int j = 0; j < nbYears; j++) {

					if(temp[j] != 0) {
						totalCost[j] += temp[j];
					}
				}
			}

			if( this.younger != null ) {

				yearDifference = this.younger.data.yearForRepair - 2018;
				int[] temp = this.younger.costPlanning(nbYears);

				for(int j = 0; j < nbYears; j++) {

					if(temp[j] != 0) {
						totalCost[j] += temp[j];
					}
				}
			}

			return totalCost;
		}
	}

}
