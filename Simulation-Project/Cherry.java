import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Cherry here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Cherry extends Plant
{
    private static int numCherries = 0;
    GreenfootImage cherry = AnimationManager.getSlice(Plants, 12, 1);
    
    public Cherry()
    {
        numCherries++;
        this.setImage(cherry);
        toughness = .4;
        totalSeeds = 3;
        health = 200;
        healthPerTick = 8;
        isToxic = false;
        wantsCarry = false;
        //getter not needed
        healthLimit = 150;
        selfHealSpeed = 3;
    }
    
    public static int getNumCherries()
    {
        return numCherries;
    }
    
    public static void setNumCherries(int xx)
    {
        numCherries = xx;
    }
    
    /**
     * Act - do whatever the Cherry wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        // Add your action code here.
    }
}
