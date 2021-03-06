package domain;
import java.io.BufferedReader;


import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;


import exceptions.InvalidBoardException;

public class Board {
	static boolean specialCards=false; // for the cards only in grading criteria,set true
	public Bank bank;
	public int pool = 0;
	public int totalPlayer;
	public static int gameStatus = -1;
	public ArrayList<Player> players = new ArrayList<Player>();
	public Player currentPlayer;
	public Square[][] squares = new Square[3][56];
	private GUI gui;
	public String[][] names = {{"SQUEEZE PLAY","THE EMBARCADERO","FISHERMAN'S WHARF","TELEPHONE COMPANY","COMMUNITY CHEST","BEACON STREET","BONUS","BOYLSTON STREET","NEWBURY STREET","PENNSYLVANIA RAILROAD","FIFTH AVENUE","MADISON AVENUE","STOCK EXCHANGE","WALL STREET","TAX REFUND","GAS COMPANY","CHANCE","FLORIDA AVENUE","HOLLAND TUNNEL","MIAMI AVENUE","BISCAYNE AVENUE","SHORT LINE","REVERSE DIRECTION","LOMBARD STREET","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"GO","MEDITERRANEAN AVENUE","COMMUNITY CHEST","BALTIC AVENUE","INCOME TAX","READING RAILROAD","ORIENTAL AVENUE","CHANCE","VERMONT AVENUE","CONNECTICUT AVENUE","IN JAIL","ST. CHARLES PLACE","ELECTRIC COMPANY","STATES AVENUE","VIRGINIA AVENUE","PENNSYLVANIA RAILROAD","ST. JAMES PLACE","COMMUNITY CHEST","TENNESSE AVENUE","NEW YORK AVENUE","FREE PARKING","KENTUCKY AVENUE","CHANCE","INDIANA AVENUE","ILLINOIS AVENUE","B&O RAILROAD","ATLANTIC AVENUE","VENTNOR AVENUE","WATER WORKS","MARVIN GARDENS","ROLL ONCE","PACIFIC AVENUE","NORTH CAROLINA AVENUE","COMMUNITY CHEST","PENNSYLVANIA AVENUE","SHORT LINE","CHANCE","PARK PLACE","LUXURY TAX","BOARDWALK","","","","","","","","","","","","","","","",""},
			{"SUBWAY","LAKE STREET","COMMUNITY CHEST", "NICOLLET AVENUE","HENNEPIN AVENUE", "BUS TICKET","CHECKER CAB CO.", "READING RAILROAD","ESPLANADE AVENUE","CANAL STREET","CHANCE","CABLE COMPANY","MAGAZINE STREET","BOURBON STREET","HOLLAND TUNNEL","AUCTION","KATY FREEWAY","WESTHEIMER ROAD","INTERNET SERVICE PROVIDER","KIRBY DRIVE","CULLEN BOULEVARD","CHANCE","BLACK & WHITE CAB CO.","DEKALB AVENUE","COMMUNITY CHEST","ANDREW YOUNG INTL BOULEVARD","DECATUR STREET","PEACHTREE STREET","PAY DAY","RANDOLPH STREET","CHANCE","LAKE SHORE DRIVE","WACKER DRIVE","MICHIGAN AVENUE","YELLOW CAB CO.","B&O RAILROAD","COMMUNITY CHEST","SOUTH TEMPLE","WEST TAMPLE","TRASH COLLECTOR","NORTH TEMPLE","TEMPLE SQUARE","GO TO JAIL","SOUTH STREET","BROAD STREET","WALNUT STREET","COMMUNITY CHEST","MARKET STREET","BUS TICKET","SEWAGE SYSTEM","UTE CAB CO.","BIRTHDAY GIFT","MULHOLLAND DRIVE","VENTURA BOULEVARD","CHANCE","RODEO DRIVE"}};

	public int[][] colors = {{64,0,0,64,64,1,64,1,1,64,2,2,64,2,64,64,64,3,64,3,3,64,64,0,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64},
			{64,4,64,4,64,64,5,64,5,5,64,6,64,6,6,64,7,64,7,7,64,8,64,8,8,64,9,9,64,9,64,10,10,64,10,64,64,11,64,11,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64},
			{64,12,64,12,12,64,64,64,13,13,64,64,13,13,64,64,14,14,64,14,14,64,64,15,64,15,15,15,64,16,64,16,16,16,64,64,64,17,17,64,17,17,64,18,18,18,64,18,64,64,64,64,19,19,64,19}};
	
	public int[][] price = {{-64,210,250,150,-64,330,-64,330,380,200,430,430,-64,500,-64,150,-64,130,-64,130,150,200,-64,210,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64},
			{-64,60,-64,60,-64,200,100,-64,100,120,-64,140,150,140,160,200,180,-64,180,200,-64,220,-64,220,240,200,260,260,150,280,-64,300,300,-64,320,200,-64,350,-64,400,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64},
			{-64,30,-64,30,60,-64,300,200,90,90,-64,150,120,120,-64,-64,150,150,150,180,180,-64,300,210,-64,210,240,240,-64,270,-64,270,300,300,300,200,-64,330,330,150,360,360,-64,390,390,420,-64,420,-64,150,300,-64,450,480,-64,510}};

	public int[][] rent = {{-64,21,25,15,-64,33,-64,33,38,20,43,43,-64,50,-64,15,-64,13,-64,13,15,20,-64,21,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64},
			{-64,6,-64,6,-64,20,10,-64,10,12,-64,14,15,14,16,20,18,-64,18,20,-64,22,-64,22,24,20,26,26,15,28,-64,30,30,-64,32,20,-64,35,-64,40,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64,-64},
			{-64,3,-64,3,6,-64,30,20,9,9,-64,15,12,12,-64,-64,15,15,15,18,18,-64,30,21,-64,21,24,24,-64,27,-64,27,30,30,30,20,-64,33,33,15,36,36,-64,39,39,42,-64,42,-64,15,30,-64,45,48,-64,51}};

	String[][] cardDescriptions = {{"Bus Ticket","All Travel vouchers,except this one, immediately expire. Move forward to any space on this side of the Board(Corner Spaces count as the first and last space on a side.Keep until needed(or expire). Play on your turn instead of rolling."},{"Buy with Chance Card","Player can buy any property with chance card!"},{"Advance to the Nearest Utility","If unowned,you may buy it from the Bank, or put it up for Auction. If owned,roll 2 dice and pay the owner 10 times the roll."},{"Go to Jail","Go directly to Jail. Do not pass any Pay Corner. Do not collect any money."},{"Advance to the Nearest Railroad","If unowned, you may buy it from the Bank, or put it up for Auction. If owned, pay the owner twice the rent otherwise due."},
			{"Advance to the Nearest Railroad","If unowned, you may buy it from the Bank, or put it up for Auction. If owned, pay the owner twice the rent otherwise due."},{"Make General Repairs to all your properties.","$25 per House, Cab Stand, and Transit Station. $100per Hotel and Skyscraper."},{"Get Out of Jail Free!",""},{"Traffic Ticket!","Pay the pool $15"},{"Go Back Three (3) Spaces",""},{"School Fees","Pay the Pool $150."},{"Advance to the Stock Exchange","If you pass Pay Day, collect $300 from the Bank."},{"Stock Market CRASH!","All Stockholders turn in 1/2 (rounded up) of all of their stocks for each company to the bank."},{"Video Killed the Radio Star!","Dividends for General Radio are 1/2 (rounded up to the nearest dollar) for all stockholders for the next 2 payouts."},{"Entertainment Rocks!","Stockholders in Motion Pictures and General Radio can immediately collect dividends."},
			{"Caught Insider Trading!","Pay the Pool a fine equal to the dividends on all the stock you hold-AND-Go directly to Jail. If you do not own any stock- you do not got to jail."},{"Loan Matures!","Collect $150 from the Bank."},{"You are elected as the Chairperson","Pay each player $50."},{"Just Say NO!","Play at any time to stop another player's action against you."},{"Excellent Accounting","Advance to Tax Refund. Collect ALL of the Pool"},{"Get Rollin'","Advance to Roll 1. Roll the dice."},{"Hurricane makes landfall!","Remove 1 House from each property in any player's 1 color group. (Downgrade Skyscrapers to Hotels; Hotels to 4 houses.)"},{"Property Taxes","Pay $25 to the Pool for each unmortgaged property you own."},{"HEY! TAXI!!!*whistle*","Advance to Black&white Cab Co. If unowned, you may buy it from the Bank, or put it up for Auction. If owned, pay the owner the normal rent due."},
			{"Social Media Fail!","Someone posting to your company's official online presence made you look bad. Pay each other player $50 to restore good PR."},{"Pay Back!","Go directly to jail for illegal business practies. While in jail, you may not collect rent. All rent due to you goes to the Pool."},{"Assets Seized!","Surrender any one undeveloped, unmortgaged property-or any one building to the Bank. If you do not own property-go directly to Jail."},{"Win the Marathon!","Take a victory lap around the board(on your current Track) and collect the Play Corner income from the Banl."},{"Strong-armed Deal","Swap a property card with a player of your choice. It can't be part of a monopoly."},{"Madri Gras!!","Everyone has to see the parade of Rex, King of Carnival. All players must move directly to Canal Street."},{"Slick Move","Steal a property card from a player of your choice. It can't be part of a monopoly."},
			{"Taxi Wars are not Fare!","Take any 1 CAB Company from any player. If none are owned, purchase your choice from the bank. Advance to that space. If you pass a Pay Corner, collect your income."},{"Zero Dollars Down!","Build 1 Free house on any property in a monopoly you own."},{"Comped Room","The next time you land on anyone else's property, you are excused from paying rent."},
			{"Changing Lanes" ,"Move directly to the space that is 1 Track below this one. If you are on the Outer Track, do nothing."},{"Changing Lanes" ,"Move directly to the space that is 1 Track below this one. If you are on the Inner Track, do nothing."},{"You Inherit $100","Collect $100 from the Bank."},{"Happy Birdthay!","Collect $10 from each player, and move to the Birthday Gift space and follow the instructions."},{"Game Night!","Pick an opponent. Both roll. Highest roller collects $200 from the Bank."},{"Doctor's Fee","Pay $50 to the pool."},{"April 15, Taxes Due!","Move directly to Income Tax, (do not pass any Pay Corneri do not collect any money),and pay the fine- OR-go directly to Jail."},{"A Moving Experience","Move to any transportation property(Railroad or Cab Co.). If unowned, you may purchase it or pu it up for auction. If owned, pay rent."},
			{"Vehicle Impounded!","Pay $50 to Pool, move directly to Just Visiting to pick up your car.Lose 1 turn."},{"Assessed for Street Repairs","$25 per Cab Stand & Transit Station, $40per house, &115 per Hotel, and $100 per Skyscraper."},{"Get Out of Jail Free!",""},{"Go to Jail","Go directly to Jail. Do not pass any Pay Corner. Do not collect any money."},{"Unions on Strike","Stockholders in United Railways, Acme Motors, and Allied Steamships must pay the Pool fines to Dividends."},{"Inherit Stock", "You may chose any 1 share of any unpurchased stock to add your portfolio."},{"Lawyer on Retainer", "Avoid Stock Fine Penalties,See you in Court, or Cought insider trading cards."},
			{"Tornado Hits!","Remove one House from each property in any 1 of your color groups. (Downgrade Skyscrapers to Hotels; Hotels to 4 houses.)"}};
	Queue<CardCommunity> communityDeck = new LinkedList<CardCommunity>();
	Queue<CardChance> chanceDeck = new LinkedList<CardChance>();

	String[] playerNames = {"Ezgi","Cagatay","Bugra","Berika","Epsilon","Zeta","Eta","Theta","Iota","Kappa","Lambda","Pi"};


	public int numOfPlayers(){
		int num=0;
		for (int i = 0; i < players.size(); i++) {
			if(players.get(i).isPlaying)
				num++;
		}
		return num;
	}
	private void initializePlayers(int totalPlayer){
			
			for(int i=0;i<this.totalPlayer;i++){
				Player p = new Player(playerNames[i],3200,i,1,0,this);
				players.add(p);
			}	
		
	}
	
	public ArrayList<Integer> getUnpurchasedStocks(){
		ArrayList<Integer> unpurchasedStocks = new ArrayList<Integer>();
		for(int i=0;i<6;i++){
			if(bank.companies.get(i).share<6){
				unpurchasedStocks.add(bank.companies.get(i).id);
			}
		}
		return unpurchasedStocks;
	}
	
	@Override
	public String toString() {
		return "Board [bank=" + bank + ", pool=" + pool + ", totalPlayer=" + totalPlayer + ", players=" + players
				+ ", currentPlayer=" + currentPlayer + ", squares=" + Arrays.toString(squares) + ", gui=" + gui
				+ ", names=" + Arrays.toString(names) + ", colors=" + Arrays.toString(colors) + ", price="
				+ Arrays.toString(price) + ", rent=" + Arrays.toString(rent) + ", cardDescriptions="
				+ Arrays.toString(cardDescriptions) + ", communityDeck=" + communityDeck + ", chanceDeck=" + chanceDeck
				+ ", playerNames=" + Arrays.toString(playerNames) + "]";
	}

	private void debugMode(){
		
		try{
			BufferedReader rd = new BufferedReader(new FileReader("debug.txt"));
			String line = rd.readLine();
			if(line.equals("DEBUG OFF")){
				// Do nothing
			}else if(line.equals("DEBUG ON")){
				// Debug is on.
				
				for(int i=0;i<players.size();i++){
				rd.readLine(); // empty line
				
				// next line is player id.
				line = rd.readLine();
				StringTokenizer ss = new StringTokenizer(line);
				ss.nextToken();
				int playerid = Integer.parseInt(ss.nextToken());
				Player p = players.get(playerid);
				
				// next line is "row position money"
				line = rd.readLine();
				StringTokenizer s1= new StringTokenizer(line);
				int row = Integer.parseInt(s1.nextToken());
				int position = Integer.parseInt(s1.nextToken());
				int money = Integer.parseInt(s1.nextToken());
				p.row=row;
				p.position=position;
				p.money=money;
				
				// next line is how many properties are added.
				line = rd.readLine();
				int propertyNumber = Integer.parseInt(line);				
				for(int i1=0;i1<propertyNumber;i1++){
					line = rd.readLine();
					StringTokenizer st = new StringTokenizer(line);
					st.nextToken();//property part is omitted.
					int sqid = Integer.parseInt(st.nextToken());
					int houseN = Integer.parseInt(st.nextToken());
					p.addPropertyDebug(sqid, houseN);
				}
				
				// next line is how many cards are added.
				line = rd.readLine(); 
				int cardNumber = Integer.parseInt(line);
				
				for(int i1=0;i1<cardNumber;i1++){
					line = rd.readLine();
					StringTokenizer st = new StringTokenizer(line);
					st.nextToken();//card part is omitted.
					int cardid = Integer.parseInt(st.nextToken());
					p.addCardDebug(cardid);
				}
				
			}
			}
			rd.close();	
		}
		
		catch(IOException ex){
			System.out.println("There is an error with the file." + ""+ex);
		}
		
	}

	/**
	 * This method creates the Card objects, chanceDeck and communityDeck.
	 * After creating the decks, it shuffles it so that cards are in random order.
	 * @requires There is no requirement for this method.
	 * @modifies This method modifies the chanceDeck and communityDeck
	 * @effects chanceDeck and communityDeck are full of cards in random order.
	 */
	public void initializeCards(boolean specialCards){
		if(specialCards){
			CardChance c1 = new CardChance(0,cardDescriptions[0][0],cardDescriptions[0][1],this);
			chanceDeck.add(c1);
			CardChance c2 = new CardChance(14,cardDescriptions[14][0],cardDescriptions[14][1],this);
			chanceDeck.add(c2);
			CardChance c3 = new CardChance(35,cardDescriptions[35][0],cardDescriptions[35][1],this);
			chanceDeck.add(c3);

			CardCommunity c4 = new CardCommunity(42,cardDescriptions[42][0],cardDescriptions[42][1],this);
			communityDeck.add(c4);
			CardCommunity c5 = new CardCommunity(43,cardDescriptions[43][0],cardDescriptions[43][1],this);
			communityDeck.add(c5);				
			CardCommunity c6 = new CardCommunity(47,cardDescriptions[47][0],cardDescriptions[47][1],this);
			communityDeck.add(c6);
		}else{
		for(int i=0;i<50;i++){
			if(i==1 || i==5 || i==8 || i==12 || i==13||i==15||i==18 || i ==25|| i==28 || i==30 ||i==31 || i==38 || i==46 || i==48 || i==41)
				continue;
			if(i<36){//ChanceCards
				CardChance c = new CardChance(i,cardDescriptions[i][0],cardDescriptions[i][1],this);
				chanceDeck.add(c);	
			}else{//CommunityChestCards
				CardCommunity c = new CardCommunity(i,cardDescriptions[i][0],cardDescriptions[i][1],this);
				communityDeck.add(c);
			}
		}}
		
		Collections.shuffle((List<?>) chanceDeck);
		Collections.shuffle((List<?>) communityDeck);

	}
	/**
	 * This method returns the CardChance object at the top of the chanceDeck.
	 * @requires There is no requirement for this method.
	 * @modifies This method doesn't modify the chanceDeck object. It just returns the top of it.
	 * @effects The card which is at the top of deck will be returned.
	 * @return It returns CardChance object
	 */
	public CardChance peekChanceCard(){

		CardChance x = chanceDeck.peek();
		return x;
	}
	
	/**
	 * This method returns the CardCommunity object at the top of the communityDeck.
	 * @requires There is no requirement for this method.
	 * @modifies This method doesn't modify the communityDeck object. It just returns the top of it.
	 * @effects The card which is at the top of deck will be returned.
	 * @return It returns CardCommunity object
	 */
	public CardCommunity peekCommunityCard(){

		CardCommunity x = communityDeck.peek();
		return x;
	}

	/**
	 * This method picks the card at the top of the deck and puts it to the bottom.
	 * After this method is called, the next card which will be picked will change.
	 * @requires There is no requirement for this method.
	 * @modifies It modifies the communityDeck.
	 * @effects The card at the top of the deck is changed.
	 */
	public void pullPushChance(){
		CardChance c = chanceDeck.poll();
		chanceDeck.add(c);
	}

	/**
	 * This method picks the card at the top of the deck and puts it to the bottom.
	 * After this method is called, the next card which will be picked will change.
	 * @requires There is no requirement for this method.
	 * @modifies It modifies the communityDeck.
	 * @effects The card at the top of the deck is changed.
	 */
	public void pullPushCommunity(){
		CardCommunity c = communityDeck.poll();
		communityDeck.add(c);
	}


	/**
	 * This method takes a color code and gives the all properties with the given color.
	 * @param color
	 * @requires color must be between 0 and 19.
	 * @modifies This method does not modifies anything.
	 * @effects Properties in the same color group will be returned.
	 * @return Returns the array of integers with the id's of the properties.
	 */
	public int[] getOtherProperties(int color) {
		// TODO Auto-generated method stub
		int length = getNumberOfSameColor(color);
		int[] result = new int[length];
		int x=0;
		for(int i=0;i<3;i++){
			for(int j=0;j<56;j++){
				if(colors[i][j]==color){
					result[x] = squares[i][j].id;
					x++;
				}
			}
		}
		return result;

	}
/**
 * This method takes the square id as a parameter and returns the square object to the user.
 * @param id
 * @requires id must be between 0 and 119
 * @modifies This method does not modifies anything.
 * @effects Square with the input id is returned to the user.
 * @return This method returns the Square object with the given id.
 */
	public Square getSquareFromBoard(int id) {
		// TODO Auto-generated method stub
		for(int i=0;i<3;i++){
			for(int j=0;j<56;j++){
				if(squares[i][j]!=null && squares[i][j].id==id){
					return (squares[i][j]);
				}
			}
		}
		return null;
	}
	
	/**
	 * This method takes the coordinates of the square and returns the square object from Board.
	 * @param row
	 * @param position
	 * @requires row must be between 0-2 and position must be limited depends on the row.
	 * @modifies This method does not modifies anything.
	 * @effects Square with the input row and position is returned to the user.
	 * @return This method returns the Square object with the given row and position.
	 */
	public Square getSquareWithRowAndPosition(int row,int position){
		int x = position;
		if(row==1)
			x+=24;
		if(row==2)
			x+=64;
		
		return getSquareFromBoard(x);
	}

	/**
	 * This method takes a color code and returns the number of properties in the same color group.
	 * @param color
	 * @requires color must be between 0 and 19.
	 * @modifies This method does not modifies anything.
	 * @effects number of properties in the given integer color will be returned to the user.
	 * @return number of properties with given color.
	 */
	public int getNumberOfSameColor(int color) {
		// TODO Auto-generated method stub
		int result=0;
		for(int i=0;i<3;i++){
			for(int j=0;j<56;j++){
				if(colors[i][j]==color){
					result++;
				}
			}
		}
		return result;
	}

	/**
	 * This method returns an empty String array.
	 * @requires There is no requirement for this method.
	 * @modifies This method does not modifies anything.
	 * @effects String array with the length of 14 is given to the user. All indexes are initialized to 0.
	 * @return It returns a string array which all elements are zero.
	 */
	public String[] getResultArray(){
		
		String[] result = new String[14];
		for(int i=0;i<14;i++){
			result[i]= "0";
		}
		return result;
	}
	
	/**
	 * This method takes a square id and returns the next square to that square.
	 * @param id
	 * @requires id must be between 0 and 119
	 * @modifies This method does not modifies anything.
	 * @effects Square which is next to the given square id is given to the user.
	 * @return Square object which is next to the given id of Square in the same row.
	 */
	public Square nextSquareForSetPosition(int id){
		int x = id;
		if(id==23){
			x=0;
		}else if(id==63){
			x=24;
		}else if(id==119){
			x=64;
		}else{
			x++;
		}		
		return getSquareFromBoard(x);
	}
	
	public Square nextSquare(int id,boolean useTransit){
		int x = id;
		Square cs = getSquareFromBoard(id);
		
		if(id==23){
			x=0;
		}else if(id==63){
			x=24;
		}else if(id==119){
			x=64;
		}else{
			if(useTransit && cs.type.equals("Transit")){			
					SquareTransit ck = (SquareTransit)getSquareFromBoard(id);
					x = ck.twin.id+1;
				
			}else{
				x++;
			}
		}
		return getSquareFromBoard(x);
	}
	
	/**
	 * This method returns the player ArrayList to the user.
	 * @requires There is no requirement for this method.
	 * @modifies This method does not modifies anything.
	 * @effects players Arraylist is returned to the user.
	 * @return It returns the players ArrayList
	 */
	public ArrayList<Player> getPlayers(){
		return players;
	}

	/**
	 * This method returns the number of players in the game.
	 * @requires There is no requirement for this method.
	 * @modifies This method does not modifies anything.
	 * @effects Number of players are given.
	 * @return size of players arraylist
	 */
	public int getNumberOfPlayers(){
		return players.size();
	}

	/**
	 * This method takes an integer money and adds it to the pool.
	 * @requires money should be positive.
	 * @modifies pool
	 * @effects money is added to the pool.
	 * @param money
	 */
	public void payToPool(int money){
		pool = pool + money;
	}

	/**
	 * This method gives the half of the pool to the player.
	 * @param p
	 * @requires Player p must be an active player.
	 * @modifies pool and money of the player
	 * @effects Half of the pool is added to the player and pool is decreased to its half.
	 */
	public void getHalfPool(Player p){
		p.addMoney((int)Math.ceil(pool/2));
		pool = (int) (pool - Math.ceil(pool/2));
	}

	/**
	 * This method gives the all of the pool to the player.
	 * @param p
	 * @requires Player p must be an active player.
	 * @modifies pool and money of the player
	 * @effects All of the pool is added to the player and pool is decreased to 0.
	 */
	public void getAllPool(Player p){
		p.addMoney(pool);
		pool = 0;
	}

	/**
	 * This method is the Constructor method of the board. This method initializes the cards,players,all of the squares of the board.
	 * @requires totalPlayer must be between 2-12.
	 * @modifies totalPlayer,players,chanceDeck,communityDeck,squares
	 * @effects Board is created and ready for the game.
	 * @param totalPlayer
	 */
	public Board(int totalPlayer) {
		
		// Initialization Processes
		if(totalPlayer<2)
			this.totalPlayer = 2;
		else if (totalPlayer<13)
			this.totalPlayer = totalPlayer;
		else
			this.totalPlayer = 12;
			
		initializePlayers(this.totalPlayer);
		currentPlayer = players.get(0);
		initializeCards(specialCards); // Chance and Community Chest cards are initialized.
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 56; j++) {
				squares[i][j]=null;
			}
		}

		//Square(String type,String name,int id, int position, int row) 
		for (int j = 0; j <24 ; j++) {
			if (j== 0){
				squares[0][j] = new SquareSqueezePlay("SqueezePlay",names[0][j],j,j,0);
			}else if(j == 9 || j== 21 ){
				squares[0][j] = new SquareTransit("Transit",names[0][j],j,j,0);
			}else if( j == 4){
				squares[0][j] = new SquareCommunity("Community",names[0][j],j,j,0);	
			}else if(j == 16 ){
				squares[0][j] = new SquareChance("Chance",names[0][j],j,j,0);
			}else if(j == 3 || j== 15  ){
				squares[0][j] = new SquareUtility("Utility",names[0][j],j,j,0);			
			}else if(j == 6){
				squares[0][j] = new SquareGo("Go",names[0][j],j,j,0);
				//bonus
			}else if(j == 12){
				squares[0][j] = new SquareStock("Stock",names[0][j],j,j,0);
				//stock exchange
			}else if(j == 14 ){
				squares[0][j] = new SquareTaxRefund("TaxRefund",names[0][j],j,j,0);
			}else if(j == 18 ){
				squares[0][j] = new SquareTunnel("Tunnel",names[0][j],j,j,0);
			}else if(j == 22){
				squares[0][j] = new SquareReverse("Reverse",names[0][j],j,j,0);
			}else{
				squares[0][j] = new SquareProperty("Property",names[0][j],j,j,0,colors[0][j],price[0][j],rent[0][j]);
			}
		}

		for (int j = 0; j <40 ; j++) {
			if (j== 0){
				squares[1][j] = new SquareGo("Go",names[1][j],j + 24,j,1);
				//go
			}else if(j == 5 || j== 15 ||j == 25 || j== 35 ){
				squares[1][j] = new SquareTransit("Transit",names[1][j],j+24,j,1);
			}else if( j == 2 || j == 17 || j == 33){
				squares[1][j] = new SquareCommunity("Community",names[1][j],j + 24,j,1);
			}else if(j == 7 || j == 22 || j == 36){
				squares[1][j] = new SquareChance("Chance",names[1][j],j + 24,j,1);	
			}else if(j == 12 || j== 28){
				squares[1][j] = new SquareUtility("Utility",names[1][j],j + 24,j,1);			
			}else if(j == 4 || j == 38){
				squares[1][j] = new SquareTax("Tax",names[1][j],j + 24,j,1);
			}else if(j == 20 ){
				squares[1][j] = new SquareFree("Free",names[1][j],j + 24,j,1);
				//free parking
			}else if(j == 30 ){
				squares[1][j] = new SquareRollOnce("RollOnce",names[1][j],j + 24,j,1);
			}else if(j == 10){
				squares[1][j] = new SquareInJail("InJail",names[1][j],j + 24,j,1);
			}else{
				squares[1][j] = new SquareProperty("Property",names[1][j],j+24,j,1,colors[1][j],price[1][j],rent[1][j]);
			}
		}

		((SquareTransit) squares[0][9]).SquareTransitTwin(9,this);
		((SquareTransit) squares[0][21]).SquareTransitTwin(21,this);
		((SquareTransit) squares[1][15]).SquareTransitTwin(39,this);
		((SquareTransit) squares[1][35]).SquareTransitTwin(59,this);

		for (int j = 0; j <56 ; j++) {
			if (j==0 || j == 5 || j == 48){
				squares[2][j] = new SquareFree("Free",names[2][j],j + 64 ,j,2);
				//subway(0), bus ticket(5 48), auction(15),
			}else if(j == 15 ){
				squares[2][j] = new SquareAuction("Auction",names[2][j],j + 64,j,2);
			}else if(j == 7 || j== 35 ){
				squares[2][j] = new SquareTransit("Transit",names[2][j],j+64,j,2);
				((SquareTransit) squares[2][j]).SquareTransitTwin(j+64,this);
			}else if( j == 2 || j== 24 || j == 36 || j == 46){
				squares[2][j] = new SquareCommunity("Community",names[2][j],j + 64,j,2);
			}else if(j == 10 || j == 21 || j == 30 || j == 54){
				squares[2][j] = new SquareChance("Chance",names[2][j],j + 64,j,2);
			}else if(j == 11 || j== 18 || j == 39 || j== 49  ){
				squares[2][j] = new SquareUtility("Utility",names[2][j],j + 64,j,2);
			}else if(j == 28){
				squares[2][j] = new SquareGo("Go",names[2][j],j + 64,j,2);
				//pay day(28) birthday(51)
			}else if(j== 51 ){
				squares[2][j] = new SquareBirthday("Birthday",names[2][j],j + 64,j,2);
			}else if(j == 14 ){
				squares[2][j] = new SquareTunnel("Tunnel",names[2][j],j + 64,j,2);	
			}else if(j == 6 || j == 22 || j == 34 || j == 50){
				squares[2][j] = new SquareCabCompany("CabCompany",names[2][j],j + 64,j,2);
			}else if(j == 42 ){
				squares[2][j] = new SquareGoToJail("GoToJail",names[2][j],j + 64,j,2);
			}else{
				squares[2][j] = new SquareProperty("Property",names[2][j],j+64,j,2,colors[2][j],price[2][j],rent[2][j]);
			}
		}

		((SquareTransit) squares[1][5]).SquareTransitTwin(29,this);
		((SquareTransit) squares[1][25]).SquareTransitTwin(49,this);
		
		
		
		bank = new Bank(players,squares);
		
		
		if(!repOk()){
			throw new InvalidBoardException("Invalid Board Construction");
		}
		//debugMode();
	}
	
	/**
	 * This method changes the current player to the parameter p.
	 * @param p
	 * @requires Player p must be an active player.
	 * @modifies currentPlayer
	 * @effects currentPlayer is changed to the p.
	 */
	public void setCurrentPlayer(Player p){
		this.currentPlayer = p;
	}
	
	/**
	 * This method returns the current player in the game.
	 * @requires There is no requirement for this method.
	 * @modifies This method does not modifies anything.
	 * @effects current player is returned to the user.
	 * @return currentPlayer
	 */
	public Player getCurrentPlayer(){
		return this.currentPlayer;
	}
	
	/**
	 * This method returns the next player for the game.
	 * @requires There is no requirement for this method.
	 * @modifies currentPlayer
	 * @effects next player is returned to the user.
	 * @return player which will play next.
	 */
	public Player nextPlayer(){
		int npID = (this.currentPlayer.id+1)%players.size();
		int ppID = (this.currentPlayer.id-1+players.size())%players.size();

		Player np = players.get(npID);
		Player pp = players.get(ppID);
		

		
		if(pp.money<=0){
			pp.destroy();
			gui.refresh();}

		if(currentPlayer.money<=0){
			currentPlayer.destroy();
			gui.refresh();}
		
		
		
		if(MonopolyGame.dieOne!=MonopolyGame.dieTwo){
			while(np.isPlaying==false || np.countJail==10){
				if(np.countJail==10)
					gui.currentMessage.setText("Vehicle impounded: Previous lost one turn.\n\n"+gui.currentMessage.getText());
				np.countJail=4;
				npID = (npID+1)%totalPlayer;
				np = players.get(npID);
			}
		}
		
		if(!MonopolyGame.monopolyGuyFlag){
			this.currentPlayer = np;
			GUI.playerChange = true;}
		
		return np;
	}
	
	/**
	 * This method refreshes the GUI of the board.
	 * @requires There is no requirement for this method.
	 * @modifies This method modifies the gui.
	 * @effects GUI is changed to its newest version.
	 */
	public void refreshGUI(){
		if(gui!=null)
			gui.refresh();
	}
	
	
	
	// EFFECTS: Returns true if the rep invariant holds 	
		// for this; otherwise returns false. 
		/**
		 * @effects Returns true if the rep invariant holds for this; otherwise returns false. 
		 * @return boolean true or false
		 */
		public boolean repOk(){
			if(this==null||communityDeck==null||chanceDeck==null||players==null||squares == null)
				return false;
			else if(totalPlayer<2 || totalPlayer>12)
				return false;
			else 
				return true;
		}

	
	
	
	public void addressGUI(GUI g){gui = g;}
	


}



