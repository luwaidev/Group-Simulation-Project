import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * Code to copy paste into animal class: 
 * <p>
 * public void checkInShelter(){<br>
 *     //need to add a collision detection to detect the shelter in front<br> 
 *     if(shelter.getCurAnimals()>0){<br>
 *          if(shelter.getTypeAnimal()==this.getClass() && shelter.limitHit() != false){<br>
 *              //animal goes into shelter<br>
 *              shelter.addCurAnimals();<br>
 *          } else{<br>
 *              //move away<br>
 *          }<br>
 *     } else{<br>
 *          shelter.setTypeAnimal(this.getClass()); // not sure if getClass() works<br>
 *          shelter.addCurAnimals();<br>
 *     }<br>
 * }</p><br>
 * 
 * MAJOR THING FOR ANIMAL TO IMPLEMENT:     
 * Animals that want to enter a shelter need to:
 * <ol><br>
 *      <li> Check if there is any animals in the shelter using getCurAnimals()</li>
 *      <li> If there is: 
 *      <ul> 
 *          <li> Check what type of animal is there using getTypeAnimal()</li>
 *          <li> Check if limit has been hit using limitHit()</li>
 *      </ul>
 *      <li> If there is not: </li>
 *      <ul> 
 *          <li> Animal needs to set the typeAnimal using setTypeAnimal</li>
 *          <li> Animal needs to add one to the addCurAnimals()</li>
 *      </ul>
 * </ol>
 * <br>
 * 
 * <p> Shelter comes in when the day ends and animals are finding a place to 
 * stay for the night. There can only be one type of animal per shelter and there
 * is a limit on how many animals can fit in one shelter. </p>
 * 
 * @author (Max) 
 * @version (April 20th, 2022)
 */
public class Shelter extends Actor
{
    /**
     * Act - do whatever the Shelter wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    private int sizeLimit = 6;
    private int curSize = 0;
    private boolean typeAnimal = false; // False is herbivore, true is carnivore
    private static int numShelters = 0;
    GreenfootImage s = new GreenfootImage("Plants.png");
    GreenfootImage shelter = AnimationManager.getSlice(s,3, 1, 48, 48);
    
    private ArrayList<Animal> animals;
    private int size = 0;
    
    public Shelter(){
        numShelters++;
        this.setImage(shelter);
        animals = new ArrayList();
    }
    public Shelter(boolean type){
        numShelters++;
        this.setImage(shelter);
        typeAnimal = type;
        animals = new ArrayList();
    }
    public static int getNumShelters()
    {
        return numShelters;
    }
    
    public static void setNumShelters(int xx)
    {
        numShelters = xx;
    }
    
    public void act()
    {
        // because it isn't clearing properly
        if (animals.size() >0 && !MainWorld.night){
            animals.clear();
        }
    }
    
    /**
     * This method checks when the limit of animals that can fit into a shelter.
     */
    public boolean limitHit(){
        if(curSize >= sizeLimit){
            return true;
        }
        return false;
    }
    
    public boolean getTypeAnimal(){
        return typeAnimal;
    }
    
    public void setTypeAnimal(boolean animal){
        typeAnimal = animal;
    }
    
    public int calcSize(){
        int size = 0;
        for (int i  = 0; i < animals.size(); i++){
            size += animals.get(i).getSize();
        }
        return size;
    }
    
    public double generateRandomNumber(){
        return (double)(Greenfoot.getRandomNumber((int)(Animal.randomness*2000))/(double)1000)-(Animal.randomness/2);
    }
    public void spawnNewAnimal(){
        if (animals.size() <= 1 ){
            // Life always finds a way
            if (animals.size() != 0&& !typeAnimal){
                getWorld().addObject(new Herbivore(), getX(), getY());
            }
            
            return;
        }
        Animal a = animals.get(0);
        Animal b = animals.get(1);
        int animalSize = animals.size();
        // Get top two animals with the most health
        for (int i = 0; i < animals.size(); i++){
            if (animals.get(i).getHealth() > a.getHealth()){
                a = animals.get(i);
            }   else if (animals.get(i) != a && animals.get(i).getHealth() > b.getHealth()){
                b = animals.get(i);
            }
        }
        
        
        // remove all old animals
        for (int i = 0; i < animals.size(); i++){
            if (animals.get(i) != a && animals.get(i) != b){
                Animal anim = animals.get(i);
                animals.remove(a);
                getWorld().removeObject(anim);
            }
        }
        
        // Spawn in the original amount of animals + 1
        for (int i  = 0; i < animalSize+(animalSize/3); i++){
                
                spawn(a,b);
                
                animals.clear();
            
        }
    }
    
    public void spawn(Animal a, Animal b){
        double speed = (a.getSpeed()+b.getSpeed())/2 + generateRandomNumber();
        double attack = (a.getAttack()+b.getAttack())/2 + generateRandomNumber();
        double size = (a.getSize()+b.getSize())/2 + generateRandomNumber();
        double altruism = (a.getAltruism()+b.getAltruism())/2+ generateRandomNumber();
        
        if (typeAnimal){
            getWorld().addObject(new Carnivore(speed, attack, size, altruism), getX(), getY());
        }   else {                    
            getWorld().addObject(new Herbivore(speed, attack, size, altruism), getX(), getY());
        }
    }
    
    public boolean addAnimal(Animal a){
        //System.out.println(typeAnimal +","+a.getType());
        //System.out.println(size+","+a.getSize());
        if (calcSize()+a.getSize() <= sizeLimit){
            
            //System.out.println(a.getSize());
            animals.add(a);
            
            return true;
        }   else {
            return false;
        }
    }
    
}
