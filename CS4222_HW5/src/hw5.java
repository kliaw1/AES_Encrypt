import java.util.*;
public class hw5 {
	static String[][] AES = new String[][] 
			{{"63", "7C", "77", "7B", "F2", "6B", "6F", "C5", "30", "01", "67", "2B", "FE", "D7", "AB", "76"},
			{"CA", "82", "C9", "7D", "FA", "59", "47", "F0", "AD", "D4", "A2", "AF", "9C", "A4", "72", "C0"}, 
			{"B7", "FD", "93", "26", "36", "3F", "F7", "CC", "34", "A5", "E5", "F1", "71", "D8", "31", "15"}, 
			{"04", "C7", "23", "C3", "18", "96", "05", "9A", "07", "12", "80", "E2", "EB", "27", "B2", "75"}, 
			{"09", "83", "2C", "1A", "1B", "6E", "5A", "A0", "52", "3B", "D6", "B3", "29", "E3", "2F", "84"}, 
			{"53", "D1", "00", "ED", "20", "FC", "B1", "5B", "6A", "CB", "BE", "39", "4A", "4C", "58", "CF"}, 
			{"D0", "EF", "AA", "FB", "43", "4D", "33", "85", "45", "F9", "02", "7F", "50", "3C", "9F", "A8"},  
			{"51", "A3", "40", "8F", "92", "9D", "38", "F5", "BC", "B6", "DA", "21", "10", "FF", "F3", "D2"},  
			{"CD", "0C", "13", "EC", "5F", "97", "44", "17", "C4", "A7", "7E", "3D", "64", "5D", "19", "73"},  
			{"60", "81", "4F", "DC", "22", "2A", "90", "88", "46", "EE", "B8", "14", "DE", "5E", "0B", "DB"}, 
			{"E0", "32", "3A", "0A", "49", "06", "24", "5C", "C2", "D3", "AC", "62", "91", "95", "E4", "79"},  
			{"E7", "C8", "37", "6D", "8D", "D5", "4E", "A9", "6C", "56", "F4", "EA", "65", "7A", "AE", "08"},  
			{"BA", "78", "25", "2E", "1C", "A6", "B4", "C6", "E8", "DD", "74", "1F", "4B", "BD", "8B", "8A"},  
			{"70", "3E", "B5", "66", "48", "03", "F6", "0E", "61", "35", "57", "B9", "86", "C1", "1D", "9E"},  
			{"E1", "F8", "98", "11", "69", "D9", "8E", "94", "9B", "1E", "87", "E9", "CE", "55", "28", "DF"},  
			{"8C", "A1", "89", "0D", "BF", "E6", "42", "68", "41", "99", "2D", "0F", "B0", "54", "BB", "16"} };
	
	static String[][] RCON = new String[][]
		{
		{"01", "00", "00", "00"},
		{"02", "00", "00", "00"},
		{"04", "00", "00", "00"},
		{"08", "00", "00", "00"},
		{"10", "00", "00", "00"},
		{"20", "00", "00", "00"},
		{"40", "00", "00", "00"},
		{"80", "00", "00", "00"},
		{"1B", "00", "00", "00"},
		{"36", "00", "00", "00"},
		};
	static String[][] MixConstant = 
		{
		 {"02", "03", "01", "01"},
		 {"01", "02", "03", "01"},
		 {"01", "01", "02", "03"},
		 {"03", "01", "01", "02"}
		};
	static String[][] roundKeySet = new String[11][16];
	static String[][] roundOutputSet = new String[11][16];
	static String [] roundKey = new String[16];
	static String [] roundOut = new String[16];
	static String [][] roundOutTemp = new String[4][4];
	static String [][] roundOutMult = new String [4][4];
	static String [] gW3 = new String[4];
	static int roundCon = 0;
	public static void main(String[] args) {
//PART 1 KEY GENERATION
		//change key to hex
		//String key = "Thats my Kung Fu";
		//String key = "This is Too Hard";
		String key = "Wooww I Finished";
		int length = key.length();
		String[] hex0 = new String[length];
		for(int i = 0; i<length; i++) {
			hex0[i] = String.format("%02x", (int)key.charAt(i)); //change the plaintext to hex
		}
		String[] round = hex0;
		System.out.println("Round key number 0");
		
		for(int i = 0; i<16; i++) {
			round[i] = hex0[i];
			roundKeySet[0][i] = hex0[i].toUpperCase();
			if(i<=14) {
				System.out.print(round[i].toUpperCase() + ", ");
			}else {
				System.out.print(round[i].toUpperCase());
			}
		}
		System.out.println();
		System.out.println();
		for(int i = 1; i<11; i++) {
			System.out.println("Round key number " + (i+1));
			getRoundKey(round);
			
			for(int j = 0; j<16; j++) {
				round[j]=roundKey[j];
				if(i!=0) {
					roundKeySet[i][j] = roundKey[j];
				}
			}
			System.out.println();
			System.out.println();
			roundCon++;
		}
		for(int i = 0; i<11; i++) {
			for(int j = 0; j<16; j++) {
				if(j<15) {
					System.out.print(roundKeySet[i][j] + ", ");
				}else {
					System.out.print(roundKeySet[i][j]);
				}
			}
			System.out.println();
		}
		System.out.println();
			
//PART 2 Round Outputs
		roundCon = 0;
		//String plaintext = "Two One Nine Two";
		//String plaintext = "Who made this HW";
		String plaintext="This took Forevr";
		length = plaintext.length();
		for(int i = 0; i<length; i++) {
			hex0[i]=String.format("%02x", (int)plaintext.charAt(i)).toUpperCase();
		}
		//FOR PLAINTEXT, SET IN MATRIX
		for(int i=0;i<16;i++) {
			roundOutputSet[0][i]=hex0[i];
			roundOut[i]=hex0[i];	
		}
		//FOR FIRST ROUND OUTPUT, SET IN MATRIX
		roundOutStateMat(roundOut, roundCon);
		getRoundOut(roundOut, roundCon);
			
		for(int j=0; j<16; j++) {
			roundOutputSet[1][j]=roundOut[j];
		}
		roundCon++;
		//FOR ROUND OUTPUTS 2 to 9, SET IN MATRIX
		for(int i = 2; i<10; i++) {
			byteSub(roundOut);
			getRoundOut(roundOut, roundCon);
			for(int j=0; j<16; j++) {
				roundOutputSet[i][j]=roundOut[j];
			}
			roundCon++;
		}
		byteSub(roundOut);
		cipherText(roundOut, roundCon);
		roundCon++;
		for(int j=0; j<16; j++) {
			roundOutputSet[roundCon][j]=roundOut[j];
		}
		System.out.println();	
		for(int i =0; i<11;i++) {
			for(int j=0; j<16;j++) {
				if(j<15) {
					System.out.print(roundOutputSet[i][j] + ", ");
				}else {
					System.out.print(roundOutputSet[i][j]);
				}
			}
			System.out.println();
		}
		System.out.println();
		System.out.print("Final CIPHERTEXT: ");
		for(int i = 0; i<16; i++) {
			if(i<15) {
				System.out.print(roundOutputSet[10][i] + ", ");
			}else {
				System.out.print(roundOutputSet[10][i]);
			}
		}
		System.out.println();
	}
	
	public static void getRoundKey(String[]key) {
		//separate the key first
		String[][]w = new String[4][4]; //2d array for the original w[] from the key
		int count = 0; //set count to 0
		for(int i = 0; i<4; i++) { //for the rows of w
			for(int j = 0; j<4; j++) {// for the columns of w
				w[i][j] = key[count]; //set index of 0 to whatever value from the key array
					count++; //increase count to run through entirety of the key
				}
			}
		getGW3(w);
		String [] tempW = gW3;
		for(int i = 0; i<4; i++){
			findNextRound(w, i, tempW);
			for(int j = 0; j<4; j++) {
				tempW[j] = roundKey[(i*4)+j];
			}
		}
		for(int i = 0; i<16; i++) {
			if(i<=14) {
				System.out.print(roundKey[i] + ", ");
			}
			else {
				System.out.print( roundKey[i]);
			}		
		}	
	}
	public static void findNextRound(String w[][], int current, String prevW[]) {
		
		for(int i = 0; i<4; i++) {
			String setW1 = Character.toString(w[current][i].charAt(0));
			String setW2 = Character.toString(w[current][i].charAt(1));
			
			String prevW1 = Character.toString(prevW[i].charAt(0));
			String prevW2 = Character.toString(prevW[i].charAt(1));
			
			String newW1 = Integer.toHexString( (Integer.parseInt(prevW1, 16))^(Integer.parseInt(setW1, 16)) );
			String newW2 = Integer.toHexString( (Integer.parseInt(prevW2, 16))^(Integer.parseInt(setW2, 16)) );
			
			newW1 = newW1.toUpperCase();
			newW2 = newW2.toUpperCase();	
			roundKey[(current*4)+i]=newW1+newW2;
		}
		
	}
	public static void getGW3(String[][]w) { //g(w[3]) for key generation (part 1)
		//PERFORM g(w[3])
		//byte shift left w[3]
		String [] temp = new String[4]; //temporary string array to hold shift for g(w[3])
		for(int i = 0; i<4; i++) { 
			//shift all values one to the left, assign the first index value of w to the last index of temp
			if((i+1)==4) {	
				temp[i]=w[3][0];
			}else {			
				temp[i]=w[3][i+1]; 
			}
		}
		//reassign the values to the respective index back into w
		for(int i = 0; i<4; i++) { 
			//System.out.print(temp[i] + " ");
			gW3[i] = temp[i];
		}
		//Byte substitution
		for(int i=0; i<4; i++) {
			String hold = gW3[i]; //temporary string variable to get value of w[3] at the index
			String firstVar = Character.toString(hold.charAt(0)); //get the first Hex value from the hold string
			String secondVar = Character.toString(hold.charAt(1)); //get the second hex value from the hold string
			int row = Integer.parseInt(firstVar, 16); //get the respective row depending on the first hex value
			int col = Integer.parseInt(secondVar, 16); //get the respective column depending on the first hex value
			gW3[i] = AES[row][col]; //get the substitute value of the w[3] value and assign to the temp w[3] array
		}
		//Adding round constant to g(w[3])
		for(int i=0; i<4; i++) {
			String holdw3 = gW3[i];
			
			String w3part1 = Character.toString(holdw3.charAt(0));
			String w3part2 = Character.toString(holdw3.charAt(1));
			
			//System.out.print(w3part2 + " ");
			//w3part1 = Integer.toBinaryString((Integer.parseInt(w3part1, 16)));
			//w3part2 = Integer.toBinaryString((Integer.parseInt(w3part2, 16)));
			
			String holdRound = RCON[roundCon][i]; //get the round to be added to get the corresponding array from the RCON array
			
			String holdRound1 = Character.toString(holdRound.charAt(0)); //get the first hex character of the RCON value
			String holdRound2 = Character.toString(holdRound.charAt(1)); //get the second hex character of the RCON value
			
			//System.out.println(holdRound2);
			//xor the first hex character of RCON and w[3]
			String part1 = Integer.toHexString(Integer.parseInt(holdRound1, 16) ^ Integer.parseInt(w3part1, 16)); //xor the first hex character of RCON and w[3]
			String part2 = Integer.toHexString(Integer.parseInt(holdRound2, 16) ^ Integer.parseInt(w3part2, 16)); //xor the second hex character of RCON and w[3]
			w3part1 = part1.toUpperCase();
			w3part2 = part2.toUpperCase(); 
			gW3[i] = w3part1+w3part2; //insert the new value into the g(w[3]) array
			//roundCon++; 
		}		
	}
	
	public static void roundOutStateMat(String[]key, int num) {
		for(int i = 0; i<16; i++) {			
			String inPart1 = Character.toString(key[i].charAt(0)); //take first Hex value from key
			String inPart2 = Character.toString(key[i].charAt(1)); //take second hex value from key 
			
			String rkeyPart1 = Character.toString(roundKeySet[num][i].charAt(0)); //take first hex val from the corresponding round key
			String rkeyPart2 = Character.toString(roundKeySet[num][i].charAt(1)); //take the second hex val from the corresponding round key
			
			String concParts1 = Integer.toHexString(Integer.parseInt(inPart1, 16) ^ Integer.parseInt(rkeyPart1, 16)).toUpperCase(); //xor first hex values for key and round key
			String concParts2 = Integer.toHexString(Integer.parseInt(inPart2, 16) ^ Integer.parseInt(rkeyPart2, 16)).toUpperCase(); //xor second hex value for key and round
			
			roundOut[i] = concParts1 + concParts2; //concatonate parts
			//perform AES 
			rkeyPart1 = Character.toString(roundOut[i].charAt(0)); //get first hex value of XOR
			rkeyPart2 = Character.toString(roundOut[i].charAt(1));  //get second hex value of XOR
			
			int row = Integer.parseInt(rkeyPart1, 16); //find the value of the row
			int col = Integer.parseInt(rkeyPart2, 16); //find the value of the column
			
			roundOut[i] = AES[row][col]; //reassign to the array final AES
			//System.out.print(roundOut[i] + " "); //check
		//	System.out.println();
			//System.out.println();
			
		}
	}
	public static void byteSub(String[]key) {
		for(int i =0; i<16; i++) {
		//	System.out.println("value passed in " + key[i]);
			String inPart1 = Character.toString(key[i].charAt(0)); //take first Hex value from key
			String inPart2 = Character.toString(key[i].charAt(1)); //take second hex value from key 
			int row = Integer.parseInt(inPart1, 16); //find the value of the row
			int col = Integer.parseInt(inPart2, 16); //find the value of the column
			roundOut[i] = AES[row][col];
			
		}
	}
	public static void getRoundOut(String[]key, int num) {
		//assign 2d array to column order
		for(int i = 0; i<4; i++) {
			for(int j = 0; j<4; j++) {
				roundOutTemp[i][j] = roundOut[i+(j*4)];
			}
		}
		//perform offset shifts
		String temp[] = new String[4];
		int toShift = 0;
		int count = 0;
		//System.out.println("Offset matrix");
		for(int i = 0; i<4; i++) { 
			toShift=i; //match the number to be shifted by the row number
			if(i!=0) { //if the number to shift is not 0, shift accordingly
				for(int shift = 0; shift<i; shift++) { 
					temp[shift]=roundOutTemp[i][shift]; //assign the values in the temporary array according to where they are going to be shifted
				}
				while(count<(4-toShift)) {
					//System.out.println(count);
					roundOutTemp[i][count] = roundOutTemp[i][count+toShift]; //reassign the shifted values in their new index
					count++;
				}
				int index = 4-count; 
				for(int in = 0; in<index; in++) {
					roundOutTemp[i][count] = temp[in]; //reassign the values that were held in the temp array to their new index
					count++; //increase the count to get the correct index
				}	
				count = 0;
			}
		}
		//Multiply mix columns
		String matmult[] = new String[4];
		String roNum = "";
		String multiplier = "";
		for(int i = 0; i < 4; i++) { //run through the mix column matrix rows
			for(int rows = 0; rows<4; rows++) { //move through all values of each row in mix column
				//go through row of multiplier * column of round
				for(int col = 0; col<4; col ++) { //goes through all values in each column of the roundOutTemp matrix 
					 roNum = roundOutTemp[col][rows]; //get the current value in round out column row
					 multiplier= MixConstant[i][col]; //get current value of the mix column row
					 if(multiplier == "03") { //follow the logic when multiplying by 03, multiply the number by 02 and 01 then xor the result
						 String temp02 = Integer.toHexString(Integer.parseInt("02", 16)*Integer.parseInt(roNum,16));
						 if(temp02.length()==3) { //if the length of the resulting 02 multiplication is 3 hex characters, drop leading character and then multiply by given constant
							 temp02=twoSplitter(temp02);
						 }
						 String temp01 = Integer.toHexString(Integer.parseInt("01", 16)*Integer.parseInt(roNum,16));
						 matmult[col] = Integer.toHexString(Integer.parseInt(temp02, 16)^Integer.parseInt(temp01, 16)).toUpperCase();			 
					 }else if(multiplier == "02") {  //if the length of the resulting 02 multiplication is 3 hex characters, drop leading character and then multiply by given constant
						 String tempHold= Integer.toHexString(Integer.parseInt(multiplier,16)*Integer.parseInt(roNum,16)).toUpperCase();
						 if(tempHold.length()==3) {
							 matmult[col] = twoSplitter(tempHold);
						 }else {
							 matmult[col]=tempHold;
						 }
						 
					 }else if (multiplier =="01"){
						 matmult[col]= Integer.toHexString(Integer.parseInt(multiplier,16)*Integer.parseInt(roNum,16)).toUpperCase();
					 }
				}
				roundOutMult[i][rows]= multMatXor(matmult);
			}
		}
		int counter = 0;
		int hold = 0;
		
		//put roundout numbers in a 1d array
		for(int i =0; i<4; i++) {
			hold = i;
			for(int j = 0; j<4; j++) {
			
				roundOut[counter]=roundOutMult[j][hold];
				counter++;
				//hold++;
			}
			//System.out.println();
		}	
		counter =0;
		System.out.println();
		
		//add corresponding RoundKey by XOR
		for(int i=0;i<16; i++) {
			roundOut[i]=Integer.toHexString( Integer.parseInt(roundOut[i], 16) ^ Integer.parseInt(roundKeySet[num+1][i], 16) ).toUpperCase();
			if(roundOut[i].length()!=2) {
				roundOut[i]= "0"+roundOut[i];
			}
		}
	}
	public static String twoSplitter(String adjust) {
		adjust = adjust.substring(1);
		adjust = Integer.toHexString(Integer.parseInt(adjust,16)^Integer.parseInt("1B",16)); //perform the xor operation and return the result
		return adjust;
	}
	public static String multMatXor(String matmult[]) { //perform the xor operation on the products of the mix column*roundouttemp values
		String finalRound = matmult[0];
		for(int i = 1; i <4;i++) {
			finalRound = Integer.toHexString(Integer.parseInt(finalRound, 16)^Integer.parseInt(matmult[i],16)).toUpperCase();	
		}
		if(finalRound.length()!=2) { //add a 0 to any value that doesn't have a length of 2
			finalRound="0"+finalRound;
		}
		return finalRound;
	}
	public static void cipherText(String [] key, int num) {//performs the same as the getRoundOut method but doesn't perform mix column multiplication
		//assign 2d array to column order
		for(int i = 0; i<4; i++) {
			for(int j = 0; j<4; j++) {
				roundOutTemp[i][j] = roundOut[i+(j*4)];
				}
		}
		//perform offset shifts
		String temp[] = new String[4];
		int toShift = 0;
		int count = 0;
		//System.out.println("Offset matrix");
		for(int i = 0; i<4; i++) {
			toShift=i;
			if(i!=0) {
				for(int shift = 0; shift<i; shift++) {
					temp[shift]=roundOutTemp[i][shift];
				}
				while(count<(4-toShift)) {
				//System.out.println(count);
					roundOutTemp[i][count] = roundOutTemp[i][count+toShift];
					count++;
				}
				int index = 4-count;
				for(int in = 0; in<index; in++) {
					roundOutTemp[i][count] = temp[in];
					count++;
				}				
				count = 0;
			}
		}
		int counter = 0;
		int hold = 0;
		for(int i =0; i<4; i++) {
			hold = i;
			for(int j = 0; j<4; j++) {
				roundOut[counter]=roundOutTemp[j][hold];
				counter++;
			}
		}	
		counter =0;
		for(int i=0;i<16; i++) {
			roundOut[i]=Integer.toHexString( Integer.parseInt(roundOut[i], 16) ^ Integer.parseInt(roundKeySet[num+1][i], 16) ).toUpperCase();
			if(roundOut[i].length()!=2) {
				roundOut[i]= "0"+roundOut[i];
			}
		}
	}
}
