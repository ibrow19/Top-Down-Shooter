package grid;

import java.util.ArrayList;
import gameobject.Ship;
import gameobject.Laser;
import gameobject.Asteroid;
import gameobject.PowerUp;
import gameobject.Enemy;

/// Cell in a grid that keeps reference to objects in it.
public class Cell {

    /// Objects contained in the cell.
    public Ship ship;
    public ArrayList<Laser> lasers;
    public ArrayList<Asteroid> asteroids;
    public ArrayList<PowerUp> powerUps;
    public ArrayList<Enemy> enemies;

    /// Initialise containers.
    public Cell() {

        ship = null;
        lasers = new ArrayList<Laser>();
        asteroids = new ArrayList<Asteroid>();
        powerUps = new ArrayList<PowerUp>();
        enemies = new ArrayList<Enemy>();

    }

}
