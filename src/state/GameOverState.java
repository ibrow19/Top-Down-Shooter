package state;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import gameobject.TextObject;
import config.Config;

/// State managing game over screen.
public class GameOverState extends SceneState {

    /// Text stating game over status.
    private final TextObject mStatus;

    /// Text showing final score.
    private final TextObject mScore;

    /// Text showing restart options.
    private final TextObject mRestart;

    /// Initialise game over text.
    public GameOverState(Context context) {

        super(context);
        int textSize = 25;
        boolean centred = true;
        mStatus = new TextObject(textSize, centred);
        mScore = new TextObject(textSize, centred);
        mRestart = new TextObject(textSize, centred);
        mStatus.setText("Game Over"); 
        mScore.setText("Final Score: " + mContext.score); 
        mRestart.setText("Press space to restart"); 
        mStatus.translate(Config.WINDOW_WIDTH / 2f, Config.WINDOW_HEIGHT / 2f - 100f);
        mScore.translate(Config.WINDOW_WIDTH / 2f, Config.WINDOW_HEIGHT / 2f - 50f);
        mRestart.translate(Config.WINDOW_WIDTH / 2f, Config.WINDOW_HEIGHT / 2f);

    }

    /// Update remaining enemies and lasers but not score or ship.
    /// \param delta time since last update.
    public SceneState update(float delta) {

        mContext.projectileManager.update(delta);
        mContext.asteroidManager.update(delta);
        mContext.powerUpManager.update(delta);
        mContext.randomManager.update(delta);
        mContext.chaserManager.update(delta);
        mContext.flockerManager.update(delta);
        mContext.ghostManager.update(delta);
        mContext.camera.centre(mContext.ship.getTranslation());
        return this;
        
    }

    /// Render scene and game over text.
    /// \param core Processing core to use for rendering.
    public void render(PApplet core) { 

        super.render(core);
        mStatus.render(core);
        mScore.render(core);
        mRestart.render(core);

    }

    /// Handle key press.
    /// \param key the key that was pressed.
    /// \return the new state.
    public SceneState handleKeyPress(char key) {

        // Rest context and start new game in play state when space pressed.
        if (key == ' ') {

            mContext.reset();
            return new PlayState(mContext);

        }
        return this;

    }

}
