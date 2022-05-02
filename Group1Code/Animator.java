import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * An animation system integrating Mr.Cohens Animation and AnimationManager classes
 * 
 * <br> Animator acts as a superclass which handles any and all animations
 * 
 * @author (Lu-Wai)
 * @version (a version number or a date)
 */
public abstract class Animator extends SuperSmoothMover
{
    
    /**
     * The frames per second for all animations
     */
    protected static int fps = 6;

    protected Animation[] animations;
    private int curAnim; // index of current playing animation
    private int curFrame; // index of current frame
    private boolean playing;
    private int timer;

    /**
     * Constructor for objects of class Animator, does nothing lmao
     */
    public Animator() {
    }

    /**
     * The act method of animator plays the animations when the boolean <i>playing<i> is true
     * <br> It loops all the frames of the animation
     * 
     * @param y a sample parameter for a method
     * @return the sum of x and y
     */
    public void act() {
        if (playing) {
            // Playing currently set animations
            timer++;
            if (timer >= 60.0 / (double) fps) { // Tick forward at the speed of the fps
                timer = 0;
                curFrame++;

                // Reset frame timer to zero if at end of animation
                if (curFrame < animations[curAnim].getFrames()) {
                    setImage(animations[curAnim].getFrame(curFrame)); // Set current frame
                } else {
                    // Return to first frame if at end
                    curFrame = 0;
                    setImage(animations[curAnim].getFrame(curFrame));
                }
            }
        }

    }
    
    /**
     * Takes a string for the name of the animation
     * <br> If an animation with a matching name is found, that animation is set to play
     */
    public void playAnimation(String name) {
        playing = true;
        for (int i = 0; i < animations.length; i++) {
            // System.out.println(animations[i].getName().equals(name));
            if (animations[i].getName().equals(name)) {
                curAnim = i;
                break;
            }
        }
    }

    /**
     * Takes a int for the index of the animation
     * <br> If an animation at that index is found, that animation is set to play
     */
    public void playAnimation(int index) {
        playing = true;
        if (index >= 0 && index < animations.length) {
            curAnim = index;
        }
    }

    /**
     * Takes an Animation object and adds it into the current animations
     */
    public void addAnimation(Animation anim) {
        if (animations != null) {
            //System.out.println("Added new");
            Animation[] temp = new Animation[animations.length + 1];
            for (int i = 0; i < animations.length; i++) {
                temp[i] = animations[i];
            }
            animations = temp;
            animations[animations.length - 1] = anim;
            //System.out.println(temp.length);
        } else {
            animations = new Animation[1];
            animations[0] = anim;
        }
    }
    
    
    /**
     * Sets the list of all the animations
     */
    public void setAnimations(Animation[] anim) {
        animations = anim;
    }
    
    
    /**
     * Takes a string for the name of the animation
     * <br> If an animation with a matching name is found, the animations will have all its frames flipped.
     * <br> Used to flip animations for Left, Right versions
     */
    public void flipAnimation(String name){
        Animation anim = null;
        for (int i = 0; i < animations.length; i++) {
            // System.out.println(animations[i].getName().equals(name));
            if (animations[i].getName().equals(name)) {
                anim = animations[i];
                break;
            }
        }
        
        if (anim == null){
            return;
        }
        GreenfootImage[] images = anim.getNonDirectionalImages();
        for (int i = 0; i < images.length; i++){
            images[i].mirrorHorizontally();
        }
    }
}
