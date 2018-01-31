package state;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

/// Abstract class representing a state that the game is in.
/// Handles updating, rendering and input handling for the game while
/// it is in this state.
public abstract class SceneState {
    
    /// Objects that make up the game scene.
    protected final Context mContext;

    /// Initialise state with game context.
    public SceneState(Context context) {
        
        mContext = context;

    }

    /// \param delta time since last update.
    public SceneState update(float delta) {
        
        return this;
        
    }

    /// Renders the scene.
    /// \param core Processing core to use for rendering the scene.
    public void render(PApplet core) { 

        mContext.asteroidManager.render(core, mContext.camera);
        mContext.ship.render(core, mContext.camera);
        mContext.turret.render(core, mContext.camera);
        mContext.randomManager.render(core, mContext.camera);
        mContext.chaserManager.render(core, mContext.camera);
        mContext.flockerManager.render(core, mContext.camera);
        mContext.ghostManager.render(core, mContext.camera);
        mContext.powerUpManager.render(core, mContext.camera);
        mContext.projectileManager.render(core, mContext.camera);

    }

    /// Update mouse position.
    /// \param mousePos the new mouse position.
    public void updateMousePos(PVector mousePos) {

        mContext.mousePos = mousePos;

    }

    /// handle mouse press.
    /// \param mouseButton the mouse button pressed.
    /// \param mousePos the position the mouse was pressed.
    /// \return SceneState the new state.
    public SceneState handleMousePress(int mouseButton, PVector mousePos) {

        return this;

    }

    /// handle mouse release.
    /// \param mouseButton the mouse button released.
    /// \param mousePos the position the mouse was released.
    /// \return SceneState the new state.
    public SceneState handleMouseRelease(int mouseButton, PVector mousePos) {

        return this;

    }

    /// Handle key press.
    /// \param key the key that was pressed.
    /// \return the new state.
    public SceneState handleKeyPress(char key) {

        return this;

    }

    /// Handle key release.
    /// \param key the key that was released.
    /// \return the new state.
    public SceneState handleKeyRelease(char key) {

        return this;

    }

}
