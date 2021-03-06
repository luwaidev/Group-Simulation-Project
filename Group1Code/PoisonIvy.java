import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Poison Ivy plant. Reproduces just by surviving, unlike the cherry plant.
 * 
 * @author (Max) 
 * @version (a version number or a date)
 */
public class PoisonIvy extends Plant
{
    private static int numPoisonIvy = 0;
    GreenfootImage ivy = AnimationManager.getSlice(Plants,12, 3);
    private double toxicityTakenAway = 10;
    private int day = MainWorld.dayNumber;
    private int nextDay = day + 1;
    /**
     * Constructor for Poison Ivy. Sets the image and basic variables for health, toughness, toxicity, health limit
     * and heal speed.
     */
    public PoisonIvy()
    {
        numPoisonIvy++;
        this.setImage(ivy);
        toughness = .5;//poison ivey is weaker than cherry
        health = 200;
        maxHealth = health;
        healthPerTick = 2;
        isToxic = true;
        wantsCarry = false;
        //getter not needed
        healthLimit = 150;
        selfHealSpeed = 2;
    }
    
    /**
     * Returns the number of Poison Ivy in the world. 
     */
    public static int getNumPoisonIvy()
    {
        return numPoisonIvy;
    }
    
    /**
     * Sets the number of Poison Ivy in the world.
     */
    public static void setNumPoisonIvy(int xx)
    {
        numPoisonIvy = xx;
    }
    
    /**
     * Checks if this instance of Poison Ivy is eaten and subtracts one from
     * the overall number of Poison Ivy in the world.
     */
    public void deathCheck()
    {
        if(health == 0){
            numPoisonIvy--;
            getWorld().removeObject(this);
        }
    }
    
    /**
     * This method allows the Poison Ivy to reproduce every day cycle until it caps
     * at around 30 and stops it from spawning on the number display at the top.
     */
    public void reproduce(){
        if(numPoisonIvy > 29){
            return;
        }
        if(day == nextDay){
            nextDay++;
            if(this.getX() >= 650 || this.getY() <= 120){ // too right and too high
                getWorld().addObject(new PoisonIvy(), this.getX() - 50, this.getY()+50);
            } else if(this.getX() >= 650 || this.getY() <= 400){//too right and too low
                getWorld().addObject(new PoisonIvy(), this.getX() - 50, this.getY()-50);
            } else if(this.getX() <= 150 || this.getY() <= 120){//too left and too high
                getWorld().addObject(new PoisonIvy(), this.getX() + 50, this.getY()+50);
            } else if(this.getX() >= 150 || this.getY() <= 400){//too left and too low
                getWorld().addObject(new PoisonIvy(), this.getX() + 50, this.getY()-50);
            } else{
                getWorld().addObject(new PoisonIvy(), this.getX() + 50, this.getY()+50);
            }            
        }
    }
    
    /**
     * Calls superclass act()  and reproduces at the start of each day.
     */
    public void act()
    {
        day = MainWorld.dayNumber;
        reproduce();
        super.act();
    }
}
