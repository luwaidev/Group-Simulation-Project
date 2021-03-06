import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * A superclass for carnivore and herbivore
 * <br> Handles the (admittedly very bad) statemachine behavior and holds most stats for the animals
 * 
 * @author (Lu-Wai)
 * @version (a version number or a date)
 */
public abstract class Animal extends Animator {
    
    /**
     * Possible states for the Animals
     */
    public enum State {
        Waiting, Searching, Following, Attacking, Running, Night, InShelter
    }

    
    /** The current state of the animal*/
    protected State state;
    
    /** The type of the animal, false means a herbivore, true means a carnivore*/
    protected boolean type; // False == herbivore, true == carnivore

    /** Current food target*/
    protected Actor target;

    // Animal variables
    
    /** Animal movement speed, increases more health decay */
    protected double speed = 2; // Animal movement speed, increases more health decay when moving
    /** How fast animals health goes down, hunger */
    protected double healthDecay = 0.1; // How fast animals health goes down, hunger
    /** Animal attack damage, decreases sensory and attack range */
    protected double attack = 1; 
    /** Animal attack distance, how far an animal can eat/attack */
    protected double attackDistance = 50;
    /** Animal size, increases health but reduces animals that can fit into a shelter */
    protected double size = 3;
    /** Animal health, regenerated by eating */
    protected int maxHealth = 300; // Animal health/hp, decreases speed
    /** Animal sensory Range, how far an animal can detect food*/
    protected double senseRange = 500; // How far animal can detect threats/food, increases health decay
    /** Altruism, never used, but intended to be used as a "kindness" modifier, 
     * deciding if an animal will give up its spot in a shelter for another with better survival chances
     */
    protected double altruism = 0.5; // Chance of animal giving up its spot

    /** Current health of animal */
    protected double curHealth;
    /** Current rotation of animal */
    protected double rotation;
    
    // Spawning Settings
    /** Modifier to decide how the size affects the health*/
    protected double sizeHealthModifier = 200;
    /** Modifier to decide how the speed affects the health decay*/
    protected double speedDecayModifier = 0.25;
    /** Modifier to decide how the attack affects the attack distance*/
    protected double attackDistanceModifier = 1.5;
    /** Modifier to decide how the attack affects the sensory distance*/
    protected double attackSenseModifier = 1.5;
    /** How much randomness is applied to the stats every generation */
    public static double randomness = 0.3;

    /** String which holds a value of  "Left", "Right", etc, used to determine and play correct direction animations*/
    protected String direction;
    
    /** Int to be used as a timer*/
    protected int time;
    
    
    /** Current selected shelter*/
    protected Shelter targetShelter;
    
    
    /** A reference to the animals health bar*/
    SuperStatBar hpBar;
    
    /** Animal Constructor which takes no values*/
    public Animal(){
        state = State.Searching;
        
        // Set current health
        curHealth = maxHealth;
        
        // init hp bar
        hpBar = new SuperStatBar(maxHealth, (int)curHealth, this, 24, 4, 10, Color.GREEN, Color.RED, false, Color.BLACK, 1);
        
        // Set basic animation
        GreenfootImage img = new GreenfootImage("spritesheet.png");
        addAnimation(AnimationManager.createAnimation(img, 1000, 1000, 1, 1, 1, 16, 16, "Hidden"));
    }
    
    /** Animal Contructor taking 4 doubles for base animal values*/
    public Animal(double _speed, double _attack, double _size, double _altruism){
        SetValues();
        state = State.Searching;
        
        // Set current health
        curHealth = maxHealth;
        
        // init hp bar
        hpBar = new SuperStatBar(maxHealth, (int)curHealth, this, 24, 4, 10, Color.GREEN, Color.RED, false, Color.BLACK, 1);
        
        // Set basic animation
        GreenfootImage img = new GreenfootImage("spritesheet.png");
        addAnimation(AnimationManager.createAnimation(img, 1000, 1000, 1, 1, 1, 16, 16, "Hidden"));
        
        // Generate values
        speed = _speed;
        healthDecay = _speed*speedDecayModifier;
        attack = _attack;
        senseRange = senseRange/(attack*attackSenseModifier);
        attackDistance  = attackDistance/(attack*attackDistanceModifier);
        altruism = _altruism;
        size = _size;
        maxHealth = (int)(size*sizeHealthModifier);
        
        curHealth = maxHealth;
    }
    /**
     * Allows an animal to set its values to anything before values are adjusted from parents
     * 
     */
    // Not in the documentation but this is just here because I didn't realize that java overides the variables if you specify new ones
    // and doesn't use the new values in the superclass and I was overidding all the variables and i had no idea what the problem was
    public void SetValues(){
        speed = 2; // Animal movement speed, increases more health decay when moving
        healthDecay = 0.1; // How fast animals health goes down, hunger
        attack = 1; // Animal attack damage, decreases health
        attackDistance = 50;
    
        size = 3;
        maxHealth = 300; // Animal health/hp, decreases speed
        senseRange = 400; // How far animal can detect threats/food, increases health decay
        altruism = 0.5; // Chance of animal giving up its spot
    }
    /**
     * Act - do whatever the Animal wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     * <br> Here, the act method calls all the state manager functions as well as the behavior and animation direction functions
     */
    public void act() {
        super.act();
        // Add your action code here.

        // Super basic terrible awful statemanager

        switch (state) {
            case Waiting:
                Waiting();
                break;
            case Searching:
                Searching();
                break;
            case Following:
                Following();
                break;
            case Attacking:
                Attacking();
                break;
            case Night:
                Night();
                break;
            case InShelter:
                InShelter();
                break;
        }

        //System.out.println(state);

        Behaviour();
        Animations();
        
        
    }

    /**
     * Handles all other behaviors that need to happen regardless of class
     */
    protected void Behaviour() {
        // If at night but not already in the shelter, set the state to search for a shelter
        if (MainWorld.night && state != State.InShelter){
            state = State.Night;
        }
        
        // Health Decay
        if (state != State.InShelter){
            curHealth -= healthDecay;
        }
    }

    ///// STATES /////
    /**
     * A Waiting state for animals, is called when state is State.Waiting
     */
    protected void Waiting() {

    }
/**
     * A Searching state for animals, is called when state is State.Searching
     */
    protected void Searching() {

    }
/**
     * A Following state for animals, is called when state is State.Following
     */
    protected void Following() {

    }
/**
     * An Attacking state for animals, is called when state is State.Attacking
     */
    protected void Attacking() {

    }
/**
     * A Night state for animals, is called when state is State.Night
     * <br> Night already handles all behavior for searching and reaching shelters
     */
    protected void Night() {
        // Return if the world isn't at night
        if (!MainWorld.night){
            state = State.Searching;
            return;
        }
        // Get the closest shelter to start
        if (targetShelter== null ){
            double closestTargetDistance = 0;
            ArrayList<Shelter> shelters= (ArrayList) getObjectsInRange(10000, Shelter.class);
            
            // Filter out shelters that aren't the correct type
            for (int i = 0; i < shelters.size(); i++){
                if (shelters.get(i).getTypeAnimal() != type){
                    shelters.remove(shelters.get(i));
                }
            }
            
            // Find the closest shelter
            if (shelters.size() > 0) {
                
                // set the first one as my target
                targetShelter = shelters.get(0);
                // Use method to get distance to target. This will be used
                // to check if any other targets are closer
                closestTargetDistance = MainWorld.getDistance(this, targetShelter);
    
                // Loop through the objects in the ArrayList to find the closest target
                for (Shelter o : shelters) {
    
                    // Measure distance and set as target if closer
                    double distanceToActor = MainWorld.getDistance(this, o);
                    if (distanceToActor < closestTargetDistance) {
                        targetShelter = o;
                        closestTargetDistance = distanceToActor;
                    }
    
                }
            
            }
        }
        
        // Check if the shelter is close enough to enter
        if (MainWorld.getDistance(this, targetShelter) <= 5){
            // Check if shelter has space
            if (targetShelter.addAnimal(this)){
                state = State.InShelter; // If target shelter has space, set state to in shelter and add self to shelter
            } else { // If no space find shelter again 
                //Kinda sucks that i need to copy paste the whole ass shelter searching function but I don't have time and can't think of a better solution
                double closestTargetDistance = 0;
                ArrayList<Shelter> shelters= (ArrayList) getObjectsInRange(10000, Shelter.class);
                
                // Filter out shelters that aren't the correct type
                for (int i = 0; i < shelters.size(); i++){
                    if (shelters.get(i).getTypeAnimal() != type){
                        shelters.remove(shelters.get(i));
                    }
                }
                if (shelters.size() > 0) {
                    
                    // grab first target that is not current target
                    Shelter target = shelters.get(0);
                    for (int i = 0; i < shelters.size(); i++){
                        if (shelters.get(i) != targetShelter){
                            target = shelters.get(i);
                        }
                    }
                    
                    // Use method to get distance to target. This will be used
                    // to check if any other targets are closer
                    closestTargetDistance = MainWorld.getDistance(this, targetShelter);
        
                    // Loop through the objects in the ArrayList to find the closest target
                    for (Shelter o : shelters) {
                        // Measure distance from me
                        double distanceToActor = MainWorld.getDistance(this, o);
                        if (o != targetShelter && distanceToActor < closestTargetDistance) {
                            target = o;
                            closestTargetDistance = distanceToActor;
                        }
    
                    }
                    targetShelter = target;
            
                }
                
            }
        }   else { // Move towards currently set shelter
            turnTowards(targetShelter.getX(), targetShelter.getY());
            rotation = getRotation();
            move(speed);
            
            // Set Walking animation
            playAnimation("Walk "+direction);
        }
        
    }
    
    /**
     * A In Shelter state for animals, is called when state is State.InShelter
     * <br> Hides the animal from view and returns to searching when day
     */
    protected void InShelter(){
        playAnimation("Hidden"); // Hide animal
        // Return to searching when time is day
        if (!MainWorld.night){
            curHealth = maxHealth;
            state = State.Searching;
        }
    }

    ////// FUNCTIONS /////
    /**
     * Takes an actor as a parameter and moves towards the actor
     */
    protected void MoveTowardsObject(Actor target) {
        turnTowards(target.getX(), target.getY());
        rotation = getRotation();

        move(speed);
    }

    /**
     * Takes a boolean for the type of shelter and sets targetShelter to the closest shelter of type
     * <br> Not used in the class
     */
    protected Shelter targetShelter(boolean type) {

        double closestTargetDistance = 0;
        double distanceToActor;
        // Get a list of all plants in the World, cast it to ArrayList
        // for easy management
        

        ArrayList<Shelter> shelters= (ArrayList) getObjectsInRange(10000, Shelter.class);
        
        // Filter out shelters that aren't the correct type
        for (int i = 0; i < shelters.size(); i++){
            if (shelters.get(i).getTypeAnimal() != type){
                shelters.remove(shelters.get(i));
            }
        }
        if (shelters.size() > 0) {
            
            // set the first one as my target
            Shelter targetShelter = shelters.get(0);
            // Use method to get distance to target. This will be used
            // to check if any other targets are closer
            closestTargetDistance = MainWorld.getDistance(this, targetShelter);

            // Loop through the objects in the ArrayList to find the closest target
            for (Shelter o : shelters) {

                // Cast for use in generic method
                // Measure distance from me
                distanceToActor = MainWorld.getDistance(this, o);
                if (distanceToActor < closestTargetDistance) {
                    targetShelter = o;
                    closestTargetDistance = distanceToActor;
                }

            }
            
            return targetShelter;

        }
        return null;

    }
    
    /**
     * Contrary to the name, doesn't actually do any animations :P
     * <br> Uses the current rotation of the animal to set a string to its corresponding direction
     * <br> Direction is then used to call the correct animations
     */
    protected void Animations() {
        if (rotation > 45&& rotation <= 135){
            direction = "Down";
        }   else if (rotation > 135&& rotation <= 225){
            direction = "Left";
        }   else if (rotation > 225&& rotation <= 305){
            direction = "Up";
        }   else{
            direction = "Right";
        }
    }
    
    /**
     * A public function to set the animal to full health
     */
    public void setFullHealth(){
        curHealth = maxHealth;
    }
    
    ///// Getters /////
    /**
     * A getter for the speed
     */
    public double getSpeed(){
        
        return speed;
    }
    /**
     * A getter for the attack 
     */
    public double getAttack(){
        return attack;
    }
    /**
     * A getter for the size
     */
    public double getSize(){
        return size;
    }
    /**
     * A getter for the altruism
     */
    public double getAltruism(){
        return altruism;
    }
    /**
     * A getter for the type
     */
    public boolean getType(){
        return type;
    }
    /**
     * A getter for the health
     */
    public double getHealth(){
        return curHealth;
    }
    /**
     * A getter for the max health
     */
    public double getMaxHealth(){
        return curHealth;
    }
    
}
