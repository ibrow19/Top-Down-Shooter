package state;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import camera.Camera;
import grid.Grid;
import gameobject.Ship;
import gameobject.Turret;
import gameobject.RandomEnemy;
import gameobject.Chaser;
import gameobject.Flocker;
import gameobject.Ghost;
import scene.ProjectileManager;
import scene.AsteroidManager;
import scene.PowerUpManager;
import scene.EnemyManager;
import config.Config;

/// Main game state in which the game is played.
public class PlayState extends StatusState {
    
    /// Initialise state with game context.
    public PlayState(Context context) {
        
        super(context);

    }

    /// Update the game state.
    /// \param delta time since last update.
    public SceneState update(float delta) {

        super.update(delta);

        // Update game objects.
        mContext.ship.update(delta);
        mContext.turret.update(delta);
        mContext.projectileManager.update(delta);
        mContext.asteroidManager.update(delta);
        mContext.powerUpManager.update(delta);
        mContext.randomManager.update(delta);
        mContext.chaserManager.update(delta);
        mContext.flockerManager.update(delta);
        mContext.ghostManager.update(delta);

        // Update score.
        mContext.score += mContext.projectileManager.consumeScore();

        // Update camera.
        mContext.camera.centre(mContext.ship.getTranslation());
        mContext.turret.adjustTarget(mContext.camera, mContext.mousePos);

        // Move to game over state if ship's health reaches 0.
        if (mContext.ship.getHealth() <= 0) {

            return new GameOverState(mContext);

        }

        updatePlanner();
        return this;
        
    }

    /// Handle mouse button presses.
    /// \param mouseButton the mouse button pressed.
    /// \param mousePos the position at which the mouse was pressed.
    public SceneState handleMousePress(int mouseButton, PVector mousePos) {

        // Set firing when left mouse button pressed.
        if (mouseButton == PConstants.LEFT) {

            mContext.turret.setFiring(true);

        }
        return this;

    }

    /// Handle mouse button releases.
    /// \param mouseButton the mouse button released.
    /// \param mousePos the position at which the mouse was released.
    public SceneState handleMouseRelease(int mouseButton, PVector mousePos) {

        // Set not firing when left mouse button released.
        if (mouseButton == PConstants.LEFT) {

            mContext.turret.setFiring(false);

        }
        return this;

    }

    /// Handle key press events.
    /// \param key the key that was pressed.
    public SceneState handleKeyPress(char key) {

        // handle movement, rotation and camera zoom.
        switch (key) {

            case 'w':
                mContext.ship.setBoost(true);
                break;

            case 'a':
                mContext.ship.setLeft(true);
                break;

            case 'd':
                mContext.ship.setRight(true);
                break;

            case 'c':
                mContext.ship.setCheat(true);
                break;

            case 'x':
                mContext.ship.setCheat(false);
                break;

            case 'i':
                mContext.camera.increaseScale();
                break;

            case 'o':
                mContext.camera.decreaseScale();
                break;

            case 'p':
                return new PauseState(mContext);

        }
        return this;

    }

    /// Handle key release events.
    /// \param key the key that was released.
    public SceneState handleKeyRelease(char key) {

        // Cease movement and rotation.
        switch (key) {

            case 'w':
                mContext.ship.setBoost(false);
                break;

            case 'a':
                mContext.ship.setLeft(false);
                break;

            case 'd':
                mContext.ship.setRight(false);
                break;

        }
        return this;

    }

    /// Update the planning AI.
    private void updatePlanner() {

        // Manage health pickup spawn rate based on ship health.
        int health = mContext.ship.getHealth();
        float healthRate = 1f - ((float)health / Ship.MAX_HEALTH);
        mContext.powerUpManager.setHealthRate(healthRate);

        /// reduce enemy spawn rate for enemies that have damaged ship.
        while (mContext.ship.hasEnemyDamage()) {
            
            int damageId = mContext.ship.getEnemyDamage();
            switch (damageId) {
                case RandomEnemy.ID:
                    mContext.randomManager.limitBound();
                    break;
                case Chaser.ID:
                    mContext.chaserManager.limitBound();
                    break;
                case Flocker.ID:
                    mContext.flockerManager.limitBound();
                    break;
                case Ghost.ID:
                    mContext.ghostManager.limitBound();
                    break;
            }

        }

    }

}
