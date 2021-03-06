import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * Herbivore Class
 * Contains the behavior for most states, the herbivores animations, and the mechanisms for dropping seeds
 * 
 * @author (Lu-Wai and Ethan)
 * @version (a version number or a date)
 */
public class Herbivore extends Animal {
    private Plant targetPlant;
    private ArrayList<Plant> plants;
    protected boolean type = false;
    
    // Base stats
    private int numSeeds;
    protected int attackDistance = 50;
    private int poopTime;
    private int specialTimer = 0;
    private boolean isPoopTime = false;
    private int currentTime = 0;
    private double healthEaten = 0;
    private static int numHerbivores = 0;
    ////////// CONSTRUCTOR /////////
    
    /** Constructor of the herbivore*/
    public Herbivore() {
        super();
        
        numHerbivores++;
        SetAnimations();

        setImage(animations[1].getImage(0));
        curHealth = maxHealth;
        SetValues();
        
    }
    
    /** Constructor of the herbivore with parameters to set the speed, attack, size, and altruism */
    public Herbivore(double _speed, double _attack, double _size, double _altruism) {
        super(_speed, _attack, _size, _altruism);
        
        numHerbivores++;
        SetAnimations();
        setImage(animations[1].getImage(0));

        playAnimation("Walk Side");
        
    }
    
    /** Sets all animations */
    public void SetAnimations(){
        ///// Setting up animations /////
        GreenfootImage img = new GreenfootImage("spritesheet.png");
        addAnimation(AnimationManager.createAnimation(img, 9 * 16, 0 * 16, 1, 1, 1, 16, 16, "Idle Right"));
        addAnimation(AnimationManager.createAnimation(img, 9 * 16, 0 * 16, 1, 1, 1, 16, 16, "Idle Left"));
        addAnimation(AnimationManager.createAnimation(img, 10 * 16, 0 * 16, 1, 1, 1, 16, 16, "Idle Down"));
        addAnimation(AnimationManager.createAnimation(img, 11 * 16, 0 * 16, 1, 1, 1, 16, 16, "Idle Up"));

        addAnimation(AnimationManager.createAnimation(img, 9 * 16, 4 * 16, 1, 4, 4, 16, 16, "Walk Right"));
        addAnimation(AnimationManager.createAnimation(img, 9 * 16, 4 * 16, 1, 4, 4, 16, 16, "Walk Left"));
        addAnimation(AnimationManager.createAnimation(img, 10 * 16, 4 * 16, 1, 4, 4, 16, 16, "Walk Down"));
        addAnimation(AnimationManager.createAnimation(img, 11 * 16, 4 * 16, 1, 4, 4, 16, 16, "Walk Up"));
        
        // Flip the left animations
        flipAnimation("Idle Left");
        flipAnimation("Walk Left");
        
    }
    
    // refer to animal
    public void SetValues(){
        speed = 2; // Animal movement speed, increases more health decay when moving
        healthDecay = 0.3; // How fast animals health goes down, hunger
        attackDistance = 50;
        attack = 2; // Animal attack damage, decreases health
        maxHealth = 200; // Animal health/hp, decreases speed
        size = 1;
        senseRange = 200; // How far animal can detect threats/food, increases health decay
        altruism = 0.5; // Chance of animal giving up its spot
        type = false;
    }

    ////////// GREENFOOT FUNCTIONS //////////
    /**
     * Act - do whatever the Herbivore wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() {
        hpBar.update((int)curHealth);
        super.act();
        setRotation(rotation);
        animalPoop();
        setRotation(0);
        
        // Check if dead
        if (curHealth <=0){
            getWorld().removeObject(this);    
            numHerbivores--;
        }
    }
    
    public void addedToWorld(World w) {
        w.addObject(hpBar, getX(), getY());
        hpBar.update((int)curHealth);
    }

    public void started() {
    }

    public void stopped() {
        //System.out.println("How");
    }

    ////////// STATES //////////
    /**
     * The searching state of Herbivore
     * 
     * Every 600 acts, the animal will switch directions until a plant comes into
     * its sensory range
     * It will then switch its state to following
     */
    private int randomDirectionDelay=60;
    private int waitTime;

    protected void Searching() {
        if (time < randomDirectionDelay) {
            time++;
        } else {
            time = 0;
            waitTime = Greenfoot.getRandomNumber(randomDirectionDelay)+randomDirectionDelay/2;
            rotation = Greenfoot.getRandomNumber(360);
            
        }
        
        // Check if at edge
        if (MainWorld.onEdge(getX(), getY())){
            rotation = Greenfoot.getRandomNumber(360);
            MainWorld.PlaceOnEdge(this);
        }
         
        setRotation(rotation);
        move(speed);
        

        if (targetClosestPlant() == 1) {
            state = State.Following;
        }
        
        
        playAnimation("Walk "+direction);
    }

    /**
     * The following state of Herbivore
     * 
     * The herbivore will go towards a preset target until it is within 15 pixels
     */
    ///// TODO: FIGURE OUT IF HERBIVORES SHOULD ATTACK CARNIVORES OR JUST RUN AWAY
    protected void Following() {
        // Prevent error and animal from eating already eaten plant
        if (targetPlant == null || targetPlant.getWorld() == null){
            state = State.Searching;
            //System.out.println("stop");
            return;
        }
        
        // Move towards selected plant
        MoveTowardsObject(targetPlant);
        turnTowards(targetPlant.getX(), targetPlant.getY());
        rotation = getRotation();

        move(speed);

        if (this.getNeighbours(10, true, Plant.class).size() > 0) {
            // If I was able to eat, increase by life by Plant's nibble power

            state = State.Attacking;
        }
    
        
        playAnimation("Walk "+direction);
    }

    protected void Attacking() {
        
        if (targetPlant == null || targetPlant.getWorld() == null){
            state = State.Searching;
            return;
        }
        
        double tryToEatPlant = targetPlant.eatPlant(attack);
        if (tryToEatPlant > 0) {
            curHealth += tryToEatPlant;
            healthEaten += tryToEatPlant;
            
            //System.out.println("Health eaten:" + healthEaten);
            if(healthEaten >= (targetPlant.getMaxHealth()/2))
            {
                this.numSeeds++;
                healthEaten = 0;
                SoundPlayer.instance.playHerbivoreEatingSounds();
            }
        }
        if (MainWorld.getDistance(targetPlant, this) > attackDistance || targetPlant == null || targetPlant.getWorld() == null) {
            state = State.Searching;

        }
        playAnimation("Idle "+direction);
    }
    
    protected void Night() {
        super.Night();
        
    }   
    
    protected void InShelter(){
        super.InShelter();
    }
    ////////// FUNCTIONS //////////

    
    private int targetClosestPlant() {

        double closestTargetDistance = 0;
        double distanceToActor;
        int numplants;
        // Get a list of all plants in the World, cast it to ArrayList
        // for easy management

        numplants = getWorld().getObjects(Shelter.class).size();

        plants = (ArrayList) getObjectsInRange((int)senseRange, Plant.class);
        //System.out.println(plants.size());  
        if (plants.size() > 0) {

            // set the first one as my target
            targetPlant = plants.get(0);
            // Use method to get distance to target. This will be used
            // to check if any other targets are closer
            closestTargetDistance = MainWorld.getDistance(this, targetPlant);

            // Loop through the objects in the ArrayList to find the closest target
            for (Plant o : plants) {

                // Cast for use in generic method
                // Actor a = (Actor) o;
                // Measure distance from me
                distanceToActor = MainWorld.getDistance(this, o);
                // If I find a plant closer than my current target, I will change
                // targets
                if (distanceToActor < closestTargetDistance) {
                    targetPlant = o;
                    closestTargetDistance = distanceToActor;
                }

            }
                    return 1;

        }
        return 0;

    }
    
    public double eatHerbivore(double damage) {
        if (curHealth >= damage) {
            curHealth -= damage;
            return damage;
        } else {
            double lastHealth = curHealth;
            curHealth = 0;
            
            
            return lastHealth;
        }
    }
    ////////// OLD //////////

    
    public static int getNumHerbivores()
    {
        return numHerbivores;
    }
    
    public static void setNumHerbivores(int xx)
    {
        numHerbivores = xx;
    }
    
    
    /**
     * Method that controls when and how the seeds drop from herbivore
     * made by Nathan (not sound)
     */
    public void animalPoop()
    {
        //if the number of seeds goes up then this method is called
       if(numSeeds > 0)
       {
           //special timer starts going up
           specialTimer++;
           //if a second has passed...
           if(specialTimer >= 60){
               //reset the special timer
               specialTimer = 0;
               //create a new cherry seed at the current location
               CherrySeed seed = new CherrySeed(60);
               getWorld().addObject(seed, getX(), getY());
               //decrease the number of seeds in the herbivore
               numSeeds--;
               SoundPlayer.instance.playSeedDropSounds();
           }
       }
    }
}
