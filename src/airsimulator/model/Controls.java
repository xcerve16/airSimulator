package airsimulator.model;

/**
 * Ovládáací prvky letadla
 * 
 * @author fanda
 */
public class Controls
{
    /** tažná síla letadla: -1, 0..100 */
    private int power;
    
    /** ovládání stoupání: -1, 0 nebo 1 */
    private int gradient;
    
    public int getPower()
    {
        return power;
    }
    
    public int getGradient()
    {
        return gradient;
    }
    
    public void setPower(int power)
    {
        if(power > 100)
            this.power = 100;
        else if(power < -1)
            this.power = -1;
        else
            this.power = power;
    }
    
    public void setGradient(int gradient)
    {
        if(gradient == -1 || gradient == 0 || gradient == 1)
            this.gradient = gradient;
        else
            this.gradient = 0;
    }
}
