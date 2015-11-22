//status=1 just continue
//status=2 player has to sell something + update player's position on screen
//status=3 ask user msg, if says yes, then call buy(Player p, int total)
//status=4 ask user msg, if says yes, then call build(Player p, int total)
//status=5 display msg and update player position on screen
//status=9 display msg with option "NO", "FREE PARKING", "PENNSYLVANIA RAILROAD" , "SHORT LINE", "READING RAILROAD", "B&O RAILROAD", "CHECKER CAB CO." , "BLACK & WHITE CAB CO." , "YELLOW CAB CO." ,"UTE CAB CO." then call taxiRide(Player p, String n)    
//status=10 ise result[2]'de roll once kartı bulunuyo. Bunu ekrana yansıt ve rollOnce(Player p, int card, int die)'ı çağır.(card kısmına result[2]'yi ver.)
//status=11 or 35 ask user msg, if says yes call applyCard(Square s, Player p, int status). 
//status=12 13 14 15 16 17 18 19 first update position and then ask user msg, if says yes, call taxiRideAction(Player p, int status)
//status=20 ask user msg with options "Try to throw double", "Pay $50 to bank", "Use 'Get Out of Jail Card'" , then call SquareInJail.getOutJail(Player p, int choice)
//status=21 ask user msg with options "Try to throw double", "Pay $50 to bank"
//status=0 play with current player again. if result[2] has number other than zero, play with it.(it is double play again.)
//status=22 update position and call landOn of that position

//check always player.countJail<4, if so, call SquareInJail.landOn()
//check always player.reverse=true, if so, call SquareReverse.landOn()

public abstract class Square {
	String name,type;
	int id;
	int position;
	int row;


	public Square(String type,String name,int id, int position, int row) {
		this.type=type;
		this.name = name;
		this.id = id;
		this.position = position;
		this.row = row;
	}

	public abstract String[] landOn(Player player, Board board, int total);

	public void initializeResult(String[] result){
		for (int i = 0; i < result.length; i++) {
			result[i]= "0";
		}

	}


	public String[] applyCard(Square s, Player p,int cardId){
		String[] result = new String[14];
		initializeResult(result);

		if(cardId==11){  // buy mortgaged pro
			int unmortgagePrice = (((SquareProperty)s).price/2)*(11/10);
			p.substract(unmortgagePrice);
			((SquareProperty)s).owner.deleteProperty(((SquareProperty)s));
			p.addProperty(((SquareProperty)s));
			((SquareProperty)s).isMortgaged=false;
			p.deleteCard(cardId);
			result[0]="1";
			result[1]="Player used Foreclosed Property Sale card and bought this property.";
			result[p.id+2]="-"+""+unmortgagePrice;
			return result;


		}else if(cardId==35){ // reverse
			p.addMoney(((SquareProperty)s).rent);
			((SquareProperty)s).owner.substract(((SquareProperty)s).rent);
			result[0]="1";
			result[1]="Reverse rent, owner pays the rent to the guest.";
			result[p.id+2]=""+((SquareProperty)s).rent;
			result[((SquareProperty)s).owner.id+2]="-"+""+((SquareProperty)s).rent;
			return result;
		}
		return result;
	}


	public String[] buy(Player p, int total){
		if(this.type.equals("Property")){
			return ((SquareProperty)this).buyProperty(p);
		}else if(this.type.equals("Utility")){
			return ((SquareUtility)this).buyUtility(p);
		}else if(this.type.equals("CabCompany")){
			return ((SquareCabCompany)this).buyCabCompany(p);
		}else{
			return ((SquareTransit)this).buyTransit(p,total);
		}

	}

	public String[] build(Player p,int total){
		if(this.type.equals("Property")){
			return ((SquareProperty)this).buildToProperty(p);
		}else{
			return ((SquareTransit)this).buildTrainDepot(p,total);
		}

	}



	public String[] rollOnce(Player p, int card, int die){
		String[] result = new String[14];
		initializeResult(result);
		result[0]="1";
		if(card==die){
			result[1]=p.name+" won Roll Once Game and took $100";
			p.addMoney(100);
			result[p.id+2]="100";
		}else{
			result[1]=p.name+" lost Roll Once Game.";
		}

		return result;
	}
	public String[] taxiRide(Player p, String n){
		String[] result = new String[14];
		initializeResult(result);
		result[0]="5";

		if (((SquareCabCompany)this).owner==p){
			p.substract(20);
			p.board.pool +=20;
			result[1] = p.name + " move to " + ""+name+".";
			result[p.id+2] = "-20";
		}else{
			p.substract(50);
			((SquareCabCompany)this).owner.addMoney(50);
			result[1] = p.name + " move to " + ""+name+".";
			result[p.id+2] = "-50";
			result[((SquareCabCompany)this).owner.id+2]= "50";
		}

		if(n.equals("FREE PARKING")){
			p.position=44;
			p.row=1;
		}else if (n.equals("PENNSYLVANIA RAILROAD")){
			p.position=9;
			p.row=0;
			if(((SquareTransit)p.board.getSquareFromBoard(9)).owner==null && p.money>=200){  
				result[0]="12";
				result[1] += " Do you want to buy "+n+" ?";
			}
		}else if (n.equals("SHORT LINE")){
			p.position=21;
			p.row=0;
			if(((SquareTransit)p.board.getSquareFromBoard(21)).owner==null && p.money>=200){  
				result[0]="13";
				result[1] += " Do you want to buy "+n+" ?";
			}
		}else if (n.equals("READING RAILROAD")){
			p.position=5;
			p.row=1;
			if(((SquareTransit)p.board.getSquareFromBoard(29)).owner==null && p.money>=200){  
				result[0]="14";
				result[1] += " Do you want to buy "+n+" ?";
			}
		}else if (n.equals("B&O RAILROAD")){
			p.position=25;
			p.row=1;
			if(((SquareTransit)p.board.getSquareFromBoard(49)).owner==null && p.money>=200){  
				result[0]="15";
				result[1] += " Do you want to buy "+n+" ?";
			}
		}else if (n.equals("CHECKER CAB CO.")){
			p.position=6;
			p.row=3;
			if(((SquareCabCompany)p.board.getSquareFromBoard(70)).owner==null && p.money>=300){  
				result[0]="16";
				result[1] += " Do you want to buy "+n+" ?";
			}
		}else if (n.equals("BLACK & WHITE CAB CO.")){
			p.position=22;
			p.row=3;
			if(((SquareCabCompany)p.board.getSquareFromBoard(86)).owner==null && p.money>=300){  
				result[0]="17";
				result[1] += " Do you want to buy "+n+" ?";
			}
		}else if (n.equals("YELLOW CAB CO.")){
			p.position=34;
			p.row=3;
			if(((SquareCabCompany)p.board.getSquareFromBoard(98)).owner==null && p.money>=300){  
				result[0]="18";
				result[1] += " Do you want to buy "+n+" ?";
			}
		}else if (n.equals("UTE CAB CO." )){
			p.position=50;
			p.row=3;
			if(((SquareCabCompany)p.board.getSquareFromBoard(114)).owner==null && p.money>=300){  
				result[0]="19";
				result[1] += " Do you want to buy "+n+" ?";
			}
		}

		return result;
	}

	public String[] taxiRideAction(Player p, int status){
		String[] result = new String[14];
		initializeResult(result);

		if (status == 12 ){
			result = ((SquareTransit)p.board.getSquareFromBoard(9)).buyTransit(p, 1);
		}else if (status == 13 ){
			result = ((SquareTransit)p.board.getSquareFromBoard(21)).buyTransit(p, 1);
		}else if (status == 14 ){
			result = ((SquareTransit)p.board.getSquareFromBoard(29)).buyTransit(p, 1);
		}else if (status == 15 ){
			result = ((SquareTransit)p.board.getSquareFromBoard(49)).buyTransit(p, 1);
		}else if (status == 16 ){
			result = ((SquareCabCompany)p.board.getSquareFromBoard(70)).buyCabCompany(p);
			result[1] = p.name + " has bought the CHECKER CAB CO.";
		}else if (status == 17 ){
			result = ((SquareCabCompany)p.board.getSquareFromBoard(86)).buyCabCompany(p);
			result[1] = p.name + " has bought the BLACK & WHITE CAB CO.";
		}else if (status == 18 ){
			result = ((SquareCabCompany)p.board.getSquareFromBoard(98)).buyCabCompany(p);
			result[1] = p.name + " has bought the YELLOW CAB CO.";
		}else if (status == 19 ){
			result = ((SquareCabCompany)p.board.getSquareFromBoard(114)).buyCabCompany(p);
			result[1] = p.name + " has bought the UTE CAB CO.";
		}
		result[0]="1";
		return result;

	}
}

