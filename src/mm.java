import java.util.ArrayList;
import java.util.Scanner;

/**
 * 
 * @author Xiaoning Guo
 * 
 * List generating code inspired[but not copied] from: http://www.geeksforgeeks.org/print-all-combinations-of-given-length/
 */

public class mm {
	//Full Color Set is the largest set of colors that is allowed (to reduce maximum complexity). 
	final static String[] FULL_COLOR_SET = {"Red", "Orange", "Yellow", "Green", "Blue", "Purple", "Pink", "Silver", "Brown", "Gold"};
	static boolean running = true;
	static Scanner sc = new Scanner(System.in);
	int guessNumber, positions, rounds;
	String[] tokenColors, codeArray;
	ArrayList<String> allCombs;
	
	
	public mm(String[] tokenColors, int positions) {
		guessNumber = 1;
		rounds = 0;
		this.positions = positions;
		this.tokenColors = tokenColors;
		codeArray = new String[positions];
		sc.nextLine();
		newGame();									
    }
	
	
	public void response(int colorsRightPositionWrong, int positionsAndColorRight){
		
		//Winning condition
		if(positionsAndColorRight == positions){
			System.out.print("\nI win with " + (guessNumber-1) + " guesses!\nDo you want to play again [Y/N]?\n");
			promptGameContinue();
			return;
		}

		//User error condition
		if(positionsAndColorRight<positions && allCombs.size()==1 || (allCombs.size()==0)){
			System.out.print("\nI think you told me false information at some point...\nDo you want to play again [Y/N]?\n");
			promptGameContinue();
			return;
		}
		
		//If the guess is wrong... Do all this
		String currentGuess = allCombs.get(0);
		allCombs.remove(0); //Always remove the base guess if it is wrong
		
		//Elimination based on assuming the test string is correct
		for(int i=0; i<allCombs.size(); i++){
			int pwTest = 0; //Only color right
			int prTest = 0; //Position and color right
		
			//Using arraylist to isolate pwTest and prTest
			ArrayList<String> test = new ArrayList<>();
			for(int k = 0; k < positions; k++){
				test.add(allCombs.get(i).substring(k,k+1));
			}
			ArrayList<String> base = new ArrayList<>();
			for(int k1 = 0; k1 < positions; k1++){
				base.add(currentGuess.substring(k1,k1+1));
			}
			
			//Counts the position and color right
			for(int j = 0; j<test.size(); j++){
				if(test.get(j).equals(base.get(j))){
					prTest++;
					test.remove(j);
					base.remove(j);
					j--;
				}
			}
				
			//Counts the color right but position wrong
			for(int j1 = 0; j1<base.size(); j1++){
				for(int j2 = 0; j2<test.size(); j2++){
					if(base.get(j1).equals(test.get(j2))){
						test.remove(j2);
						j2--;
						pwTest++;
					}
				}
			}
			
			//Remove the test case that does not yield the base 
			if(colorsRightPositionWrong!=pwTest || positionsAndColorRight!=prTest){
				allCombs.remove(i);
				i--;
			}
		}
	}

	public void newGame(){
		guessNumber = 1;
		rounds++;
		allCombs = new ArrayList<String>(); //Wipes the full list of possible combinations
		generatePossibilities(numeralColorSet(positions), "", positions);  //Creates a new list
		
		System.out.println("Now, choose your code sequence. You may select colors from the following list: " + stringForm(tokenColors));
		//User inputs the code created with the prompted constraints
		for(int i=0; i<positions; i++){	
			System.out.printf("Enter the color of position %d:\n", i+1);
			String choice = sc.nextLine();
			boolean isAColor = false; //Prevents the user from misstyping colors
			for(String color: tokenColors){
				if(color.equals(choice)){
					isAColor = true;
				}
			}
			if(!isAColor){ //Error checking
				System.out.println("Try again. Enter colors from the following list: " + stringForm(tokenColors) + "\n");
				i--;
			}else{
				codeArray[i] = choice;
			}	
		}
		System.out.printf("\nRound %d. Okay, press enter to begin.\n",rounds);
		sc.nextLine();
	} 
	
	
	public String [] nextMove() {
		guessNumber++;
		String[] temp = new String[positions];
		String colorCode = allCombs.get(0);
		for(int i=0; i<positions; i++){
			int index = Integer.parseInt(colorCode.substring(i,i+1));
			temp[i] = FULL_COLOR_SET[index];
		}
		return temp; // return the next guess
	
	} 
	
	//Takes the color set ("Red", "Blue", "Green", "Red") and converts it into something like 1421
	public String[] numeralColorSet(int positions){
		String[] temp = new String[positions];
		
		for(int i=0; i<positions;i++){
			temp[i] = i+"";
		}
		return temp;
	}
	
	/* 
	 * Code inspired from: http://www.geeksforgeeks.org/print-all-combinations-of-given-length/
	 * Generates the full set of combinations given number of colors and number of positions
	 */
    public void generatePossibilities(String[] colorSet, String string, int positions) {
         
        //Print the final string when everything is finished
        if (positions == 0) {
            allCombs.add(string);
            return; //Do not want to continue after positions is down to 0
        }
 
        //Accumulate the characters 
        for (int i = 0; i<colorSet.length; i++) {
        	generatePossibilities(colorSet, string+colorSet[i], positions-1); 
        }
    }
	
	public void promptGameContinue(){
		String input = sc.nextLine();
		input = sc.nextLine();
		if(input.equals("y") || input.equals("Y")){
			System.out.println();
			newGame();
		}else{
			System.out.println("Sorry to hear that, see you again!");
			running = false;
		}
	}
	
	public static void main(String args[]){
		gameLoop();
	}

	
	public static void gameLoop(){
		int numColors, numPositions;
		String[] colorArray;
		
		System.out.print("Welcome to MasterMinds. I am the codebreaker.");
		System.out.print("\nYou are the codemaker. I will ask you what conditions you want to play with. \nPress enter to continue.");
		sc.nextLine();
		
		//User chooses the color options. Loop is used incase input is not within limits
		while(true){
			System.out.println("\nHow many colors do you want to choose from? [No more than 10]");
			numColors = sc.nextInt();
			
			if(numColors > 10){
				System.out.println("Enter a valid number next time.");
				continue;
			}
			colorArray = colorPalette(numColors);
			break;
		}
		
		//User chooses the number of positions. Loop is used incase input is not within limits
		while(true){
			System.out.println("How many positions do you want? [No more than 10]");
			numPositions = sc.nextInt();
			
			if(numPositions > 10){
				System.out.println("Enter a valid number next time.");
				continue;
			}
			break;		
		}
		
		//Instantiation of the game object
		mm game = new mm(colorArray, numPositions);
		
		while(running){
			System.out.print("[You peek at your code sequence: " + stringForm(game.codeArray) + "]"); 
			System.out.print("\n[Guess number: " + game.guessNumber + "] " + "My guess is: " + stringForm(game.nextMove()));
			
			while(true){
				System.out.print("\nEnter the number of correct colors that are in the right position.\n");
				int positionRight = sc.nextInt();
				System.out.print("Enter the number of correct colors that are not in the right position.\n");
				int positionWrong = sc.nextInt();
				if(positionRight+positionWrong>5){
					System.out.println("These numbers don't add up... Try again.");
					continue;
				}
				game.response(positionWrong, positionRight);	
				break;
			}
		}
	}
	
	
	//Helper method that returns the correct color list given the number of color options
	public static String[] colorPalette(int numColors){
		String[] temp = new String[numColors];
		for(int i = 0; i<numColors; i++){
			temp[i] = FULL_COLOR_SET[i];
		}
		return temp;
	}

	
	public static String stringForm(String[] s){
		String temp = "";
		for(String string: s){
			temp += string + " ";
		}
		return temp;
	}
	
	
	public static String stringForm(ArrayList<String> s){
		String temp = "";
		for(String string: s){
			temp += string + " ";
		}
		return temp;
	}
}
