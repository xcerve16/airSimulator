/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airsimulator.model;

/**
 * Model letadla
 * 
 * @author fanda
 */
public class Airplane
{
    private double verticalSpeed;
    private double horizontalSpeed;
    private int gradient;
    private double altitude;
    private final Controls controls;
    private boolean destroyed = false;
    
    public Airplane()
    {
        controls = new Controls();
    }
    
    public Controls getControls()
    {
        return controls;
    }
    
    /* Vrátí aktuální naklonění letounu (od -30° do 30°) */
    public int getGradient()
    {
        return gradient;
    }
    
    /* Aktuální vertikální rychlost (v m/s) */
    public int getVerticalSpeed()
    {
        return (int) verticalSpeed;
    }
    
    /* Aktuální horizontální rychlost (v m/s) */
    public int getHorizontalSpeed()
    {
        return destroyed ? 0 : (int) horizontalSpeed;
    }
    
    /* Výška letu letadla */
    public int getAltitude()
    {
        return destroyed ? 0 :(int) altitude;
    }
    
    /* Je letadlo zničeno pádem? */
    public boolean isDestroyed()
    {
        return destroyed;
    }    
       
    /* Provede krok simulace (jakoby 1/5 vteřiny herního času */
    public void simulationStep()
    {
        int cGradient = controls.getGradient();
        if(!destroyed) {
            if(cGradient == 1) {
                if(gradient < 30)
                    gradient += 2;
            }
            else if(cGradient == -1) {
                if(gradient > -30)
                    gradient -= 2;
        }
        
        computeHorizontalSpeed();
        computeVerticalSpeed();
        }
    }
    
    private void computeHorizontalSpeed()
    {
        double acceleration = 5.0;
        
        // akcelerace je dána tahem letadla
        int power = controls.getPower();
        if(power >= 0)
            acceleration *= (double) power / 100;
        else
            acceleration = -1.0;
        
        // snížená o odpor vzduchu, který závisí na rychlosti
        acceleration -= Math.abs(horizontalSpeed / 270 * 5.0);
        
          
        // aby nám letadlo necouvalo
        if(horizontalSpeed <= 0 && acceleration <= 0) {
            acceleration = 0;
            horizontalSpeed = 0;
        }
        
        horizontalSpeed += acceleration;
    }
    
    private void computeVerticalSpeed()
    {
        double speed;
        if(horizontalSpeed < 50)
            speed = -300; // pád střemhlav
        else if(horizontalSpeed < 120)
            speed = (gradient*0.02 - 0.1) * horizontalSpeed;
        else
            speed = gradient*0.02 * horizontalSpeed;
        
        if(altitude > 1 && (altitude + speed) < -11) {
            destroyed = true;
        }
        
        if(altitude <= 0 && speed < 0) {
            altitude = 0;
            speed = 0;
        }
        else if(altitude > 2500 && speed > 0) {
            speed -= speed * (altitude - 2500) / 500;
        }
        
        verticalSpeed = speed;
        altitude += speed / 5;
    }
}
