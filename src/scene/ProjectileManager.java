package scene;

import java.util.Iterator;
import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PVector;
import gameobject.Laser;
import gameobject.NormalLaser;
import gameobject.PenetrateLaser;
import gameobject.ReflectLaser;
import grid.Grid;
import camera.Camera;
import texture.Texture;


/// Manages updating and rendering of lasers.
public class ProjectileManager {

    /// Texture to use for lasers.
    private final Texture mLaserTexture;

    /// Grid to put lasers in.
    private final Grid mGrid;

    /// Lasers currently in the game.
    private final ArrayList<Laser> mLasers;

    /// Score accumulated from lasers destroying enemies.
    private int mScore;

    /// Initialise projectile manager.
    public ProjectileManager(Texture laserTexture, Grid grid) {

        mScore = 0;
        mLaserTexture = laserTexture;
        mGrid = grid;
        mLasers = new ArrayList<Laser>();

    }

    /// Get score accumulated by lasers.
    /// \return the score accumulated by lasers.
    public int consumeScore() {

        int score = mScore;
        mScore = 0;
        return score;

    }

    /// Add a new laser to the manager.
    /// \param id the ID of the laser type.
    /// \param velocity the velocity of the laser.
    /// \param translation the initial position of the laser.
    public void addLaser(int id,
                         PVector velocity, 
                         PVector translation) {

        Laser laser = null;
        switch (id) {
            case PenetrateLaser.ID:
                laser = new PenetrateLaser(mLaserTexture, mGrid, velocity); 
                break;
            case ReflectLaser.ID:
                laser = new ReflectLaser(mLaserTexture, mGrid, velocity); 
                break;
            default:
                laser = new NormalLaser(mLaserTexture, mGrid, velocity); 
                break;
        }

        laser.setTranslation(translation);
        mLasers.add(laser);

    }

    /// Update all lasers, accumulating score from them and destroying lasers
    /// that go out of bounds.
    public void update(float delta) {

        Iterator<Laser> it = mLasers.iterator();
        while (it.hasNext()) {
        
            Laser laser = it.next();
            laser.update(delta);
            mScore += laser.consumeScore();
            if (laser.isDestroyed()) {

                laser.removeFromGrid();
                it.remove();

            }

        }

    }

    /// Render all lasers.
    /// \param core processing core to use for rendering.
    /// \param camera camera to use for rendering.
    public void render(PApplet core, Camera camera) {

        Iterator<Laser> it = mLasers.iterator();
        while (it.hasNext()) {
        
            it.next().render(core, camera);

        }

    }

}
