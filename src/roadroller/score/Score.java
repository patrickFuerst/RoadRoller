package roadroller.score;

import javax.microedition.rms.RecordStore;

/**
 * Responsible to save and load the score
 * 
 * A RecordStore is used to save the Score
 * @see RecordStore
 * 
 * @author patrickfuerst
 *
 */
public class Score {

/**
 * rs, the variable to access the record store. 
 */
private RecordStore rs; 

/**
 *  list of high score names  
 */
private String names[]; 
/**
 *  the list of high score values
 */
private int values[];  
/**
 * to hold the Singelton reference
 */
private static Score singleton = null;

/**
 * Contructor for the score, initializes names and values
 */
public Score(){
	names = new String[10];
	values = new int[10];
}
/**
 * Returns the singelton instance
 * @return Score
 */
public static Score getInstance() {
	if (singleton == null) {
		singleton = new Score();
	}
	return singleton;
}
/**
 * read the higscore from the recordstore
 * if there are fewer then 10 scores the other have "none" as name 
 */
private void getHighScores() { 

	int p=0; 

	for (p=0;p<10;p++) { 
		names[p]="none"; 
		values[p]=0; 
	} 

	try { 
		rs=RecordStore.openRecordStore("highScores", true); //here we open the record store, were using the db variable, with the record store name of "highScores" 
		readRecords (rs);	 //this calls a method we will make below to read the records of the recordstore 
		rs.closeRecordStore(); //we read the records, lets close the record store. 
	} catch (Exception e) {System.out.println (""+e+" getHighs");} 


} 

/**
 * write the names and values from the recordstore in the variables
 */
	public void readRecords(RecordStore recStore) { 
	
	try 
	{ 
		byte[] recData = new byte[1]; 
		int len; 



		for (int i = 1; i <= recStore.getNumRecords() && i<=10; i++) 
		{ 
			
			if (recStore.getRecordSize(i) > recData.length) 
				recData = new byte[recStore.getRecordSize(i)];

			len = recStore.getRecord(i, recData, 0); 
			String temp = new String(recData, 0, len);
			names[(i-1)] = temp.substring (0,temp.indexOf (","));
			String value = temp.substring (temp.indexOf(",")+ 1,temp.length()); 
			values[(i-1)]= Integer.parseInt(value);

		}
	}
	catch (Exception e) 
	{ 
		System.err.println(e.toString()+"reading"); 
	} 
} 

/**
 * write the variables back in to the record store
 * @param recStore
 */
	private void writeHighScore(RecordStore recStore){ 
	
		int i=1; 
		for (int p=0;p<10;p++) { 
	
			String temp=names[p]+","+values[p];
			byte[] rec = temp.getBytes(); 
	
			try 
			{ 
				if (recStore.getNextRecordID() > i) {  
	
					recStore.setRecord(i++,rec, 0, rec.length); 
	
				} 
				else 
				{ 
					recStore.addRecord (rec,0,rec.length);
					i++; 
				} 
	
			}
			catch (Exception e) { 
				System.err.println(e.toString()); 
			} 
		}
	
	}

	
/**
 * called  after the came finished  to save the score 
 * @param name of the player
 * @param score of the player 
 *
 */
	public void saveScore(String name, int score){
		
		getHighScores();
		for (int p=0; p<10; p++) {  
			
				if (values[p] < score){
				               
				    for (int t=9 ;t>p; t--) { 
				    	values[t]=values[t-1];	
				    	names[t]=names[t-1]; 
				    }
				    
				    values[p]=score; 
					names[p]= name; 
					break;
				} 
		
		}
			try { 
				rs=RecordStore.openRecordStore("highScores", true); 
				writeHighScore(rs); 
				rs.closeRecordStore(); 
			} catch (Exception e) {System.out.println (e);} 
		}

/**
 * use this to get the name array 
 * first name is first in highscore
 * @return names in the highscore
 */
	public String[] getNames(){
		getHighScores();
		return names;
	}
/**
 * use this to get the values in the highscore
 * first value belongs to the first name
 * @return scores
 */
	public int[] getValues(){
		return values;
	}
}

