/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all the things a Hunter can do in town.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class Town {
    // instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private double toughness;
    private boolean toughTown;
    private boolean dugBefore = false;
    private boolean treasureSearched = false;
    private String[] treasures = {"dust", "gem", "crown", "trophy"};
    private String treasure;
    private String mode;


    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     *
     * @param shop The town's shoppe.
     * @param mode Mode of game.
     */
    public Town(Shop shop, String mode) {
        this.shop = shop;
        this.terrain = getNewTerrain();
        this.mode = mode;

        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;

        printMessage = "";

        // higher toughness = more likely to be a tough town
        toughTown = (Math.random() < toughness);
        treasure = chooseTreasure();
    }

    public String getLatestNews() {
        String prevMessage = printMessage;
        printMessage = "";
        return prevMessage;
    }

    public String chooseTreasure(){
        return treasures[(int)(Math.random() * 4)];
    }

    /**
     * Assigns an object to the Hunter in town.
     *
     * @param hunter The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter) {
        this.hunter = hunter;
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";

        if (toughTown) {
            printMessage += "\nIt's pretty rough around here, so watch yourself.";
        } else {
            printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
        }
    }

    /**
     * Handles the action of the Hunter leaving the town.
     *
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown() {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown) {
            String item = terrain.getNeededItem();
            if (terrain.getTerrainName().equals("Jungle") && hunter.hasItemInKit("sword")){
                item = Colors.RED + "Sword" + Colors.RESET;
            }
            printMessage = "You used your " + item + " to cross the " + terrain.getTerrainName() + ".";
            if (checkItemBreak() && !item.equals(Colors.RED + "Sword" + Colors.RESET)) {
                hunter.removeItemFromKit(item);
                printMessage += "\nUnfortunately, you lost your " + item;
            }

            return true;
        }

        printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + terrain.getNeededItem() + ".";
        return false;
    }

    /**
     * Handles calling the enter method on shop whenever the user wants to access the shop.
     *
     * @param choice If the user wants to buy or sell items at the shop.
     */
    public void enterShop(String choice) {
        shop.enter(hunter, choice);
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
    public void lookForTrouble() {
        double noTroubleChance;
        if (toughTown) {
            noTroubleChance = 0.66;
        } else {
            noTroubleChance = 0.33;
        }
        if (Math.random() > noTroubleChance) {
            printMessage = "You couldn't find any trouble";
        } else {
            int goldDiff = (int) (Math.random() * 10) + 1;
            printMessage = Colors.RED;
            if (!hunter.hasItemInKit("sword")) {
                printMessage += "You want trouble, stranger!  You got it!\nOof! Umph! Ow!\n";
                double chance = 0;
                if (mode.equals("easy")) {
                    chance = Math.random() * 1.5;
                } else if (mode.equals("medium")) {
                    chance = Math.random();
                } else if (mode.equals("hard")) {
                    chance = Math.random() * 0.5;
                }
                if (chance > Math.random()) {
                    printMessage += "Okay, stranger! You proved yer mettle. Here, take my gold.";
                    printMessage += "\nYou won the brawl and receive " + Colors.YELLOW + goldDiff + Colors.RESET + " gold.";
                    hunter.changeGold(goldDiff);
                } else {
                    printMessage += "That'll teach you to go lookin' fer trouble in MY town! Now pay up!";
                    printMessage += "\nYou lost the brawl and pay " + goldDiff + " gold.";
                    hunter.changeGold(-goldDiff);
                    printMessage += Colors.RESET;
                }
            } else {
                System.out.println("\nDude how is that even fair, you got a literal sword.");
                System.out.println("You're actually so cringe.");
                System.out.println("Just take my money and leave me alone!");
                System.out.println("You got " + Colors.YELLOW + goldDiff + Colors.RESET + " gold.");
                hunter.changeGold(goldDiff);
                printMessage += Colors.RESET;
            }
        }
    }

    public void digForGold() {
        if (dugBefore) {
            System.out.println("You already dug for gold in this town");
        } else if (!hunter.hasItemInKit("shovel")) {
            System.out.println("You can't dig for gold without a shovel");
        } else if (Math.random() > 0.5) {
            int dugUp = (int) (Math.random() * 20) + 1;
            System.out.println("You dug up " + Colors.YELLOW + dugUp + " gold!" + Colors.RESET);
            dugBefore = true;
        } else {
            System.out.println("You dug but only found dirt");
        }
    }

    public String toString() {
        return "This nice little town is surrounded by " + terrain.getTerrainName() + ".";
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain() {
        int rnd = (int) (Math.random() * 6);
        dugBefore = false;
        if (rnd == 0) {
            return new Terrain("Mountains", "Rope");
        } else if (rnd == 1) {
            return new Terrain("Ocean", "Boat");
        } else if (rnd == 2) {
            return new Terrain("Plains", "Horse");
        } else if (rnd == 3) {
            return new Terrain("Desert", "Water");
        } else if (rnd == 4) {
            return new Terrain("Jungle", "Machete");
        } else {
            return new Terrain("Marsh", "Boots");
        }
    }

    // search for treasure
    public String searchTreasure(){
        if (!treasureSearched){
            treasureSearched = true;
            if (treasure.equals("dust")){
                System.out.println("You found some dust");
            } else {
                System.out.println("You found a " + treasure);
                return treasure;
            }
        } else {
            System.out.println("This town has already been searched");
        }
        return "N/A";
    }

    /**
     * Determines whether a used item has broken.
     *
     * @return true if the item broke.
     */
    private boolean checkItemBreak() {
        double rand = Math.random();
        if (mode.equals("easy")) {
            return false;
        }
        return (rand < 0.5);
    }
}