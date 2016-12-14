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
    private int simulationTime;
    private double verticalSpeed;
    private double verticalAcceleration;
    private double horizontalSpeed;
    private double horizontalAcceleration;
    private int gradient;
    private double altitude;
    private Controls controls;
    
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
        return (int) horizontalSpeed;
    }
    
    /* Výška letu letadla */
    public int getAltitude()
    {
        return (int) altitude;
    }
    
    /* čas simulace v sekundách */
    public int getSimulationTime()
    {
        return simulationTime / 5;
    }
    
    /* Provede krok simulace (jakoby 1/5 vteřiny herního času */
    public void simulationStep()
    {
        ++simulationTime;
        int cGradient = controls.getGradient();
        
        if(cGradient == 1 && gradient < 30)
            gradient += 2;
        else if(cGradient == -1 && gradient > -30)
            gradient -= 2;
        
        computeHorizontalAcceleration();
        computeVerticalAcceleration();
        
        verticalSpeed += verticalAcceleration / 5;
        horizontalSpeed += horizontalAcceleration / 5;
        altitude += verticalSpeed / 5;
    }
    
    private void computeHorizontalAcceleration()
    {
        double acceleration = 5.0;
        
        // akcelerace je dána tahem letadla
        int power = controls.getPower();
        if(power >= 0)
            acceleration *= power;
        else
            acceleration = -1.0;
        
        // snížená o odpor vzduchu, který závisí na rychlosti
        acceleration -= Math.abs(((double) horizontalSpeed / 270) * acceleration);
        
        // ovlivnění náklonem (náklon dolů zvyšuje, nahoru snižuje)
        acceleration *= (double) (-gradient) / 100 + 1;
        
        // aby nám letadlo necouvalo
        if(horizontalSpeed <= 0 && acceleration < 0)
            acceleration = 0;
        
        horizontalAcceleration = acceleration;
    }
    
    private void computeVerticalAcceleration()
    {
        double acceleration;
        
        // vliv na vztlak rychlostí letounu
        if(horizontalSpeed < 70)
            acceleration = 0.1 * horizontalSpeed - 5;
        else if(horizontalSpeed < 130)
            acceleration = 0.05 * horizontalSpeed - 3.5;
        else
            acceleration = 3;
        
        // vliv náklonem a letovou výškou letounu
        if(altitude == 0) {
            if(acceleration < 0)
                acceleration = 0;
        }
        else if(altitude < 300) {
            if(gradient >= 0)
                acceleration = (double) gradient * 0.1 + 0.3;
            else
                acceleration = (double) gradient * 0.167;            
        }
        else if(altitude < 4000) {
            if(gradient >= 0)
                acceleration = (double) gradient * 0.1;
            else
                acceleration = (double) gradient * 0.167; 
        }
        else {
            if(gradient >= 0)
                acceleration = (double) gradient * 0.1;
            else
                acceleration = (double) gradient * 0.167;
            
            acceleration -= (double) (altitude - 4000) / 100;
        }
        
        verticalAcceleration = acceleration;
    }
}
