package gameobject;

import processing.core.PApplet;
import processing.core.PVector;
import texture.Texture;
import scene.ProjectileManager;
import camera.Camera;
import config.Config;

/// Turret attached to ship that shoots lasers.
public class Turret extends TextureObject {

    // Constants.
    private static final float RECHARGE = 0.5f;
    private static final float POWERUP_RECHARGE = 0.25f;
    private static final float FIRING_OFFSET = 60f;
    private static final float LASER_SPEED = 750f;
    private static final float POWERUP_DURATION = 10f;

    /// The ship that the turret is attached to.
    private final Ship mShip;

    /// Projectile manager for managing lasers.
    private final ProjectileManager mProjectileManager;

    /// Time it takes turret to recharge.
    private float mRechargeDuration;

    /// Time passed recharging turret.
    private float mRechargeProgress;

    /// Whether the turret is currently firing.
    private boolean mFiring;

    /// Current ID of lasers being fired.
    private int mLaserId;

    /// Time passed using current power-up.
    private float mPowerUpProgress;

    /// Initialise turret.
    public Turret(Texture texture, 
                  Ship ship, 
                  ProjectileManager projectileManager) {

        super(texture);
        mShip = ship;
        mProjectileManager = projectileManager;

        mRechargeDuration = RECHARGE;
        mRechargeProgress = 0f;

        mFiring = false;
        mLaserId = NormalLaser.ID;
        mPowerUpProgress = 0f;

        setOrigin(15f, 50f);

    }

    /// Set whether the turret is firing.
    /// \param firing whether the turret is firing.
    public void setFiring(boolean firing) {

        mFiring = firing;

    }

    /// Adjust where the turret is pointing.
    /// \param camera camera to adjust position with.
    /// \param target position to point turret towards.
    public void adjustTarget(Camera camera, PVector target) {

        // adjust translation based on camera being used.
        PVector translation = getTranslation();
        translation.x *= camera.getXScale();
        translation.y *= camera.getXScale();
        translation.add(camera.getTranslation());
        
        PVector distance = PVector.sub(target, translation);

        // Get new rotation.
        float rotation = (float)Math.atan(distance.y / distance.x) * (180f / (float)Math.PI);

        // Orientate rotation to match turret.
        if (distance.x >= 0f) {

            rotation += 90f;

        } else {

            rotation += 270f;

        }
        setRotation(rotation);

    }

    // Translate relative to ship.
    public PVector getTranslation() {

        PVector translation = super.getTranslation();

        PVector shipTranslation = mShip.getTranslation();
        float shipRotation = mShip.getRadianRotation();
        PVector shipScale = mShip.getScale();

        translation.x *= shipScale.x;
        translation.y *= shipScale.y;
        translation.rotate(shipRotation);

        return translation.add(shipTranslation);

    }

    /// Scale relative to ship.
    /// \return turret scale combined with ship scale.
    public PVector getScale() {

        PVector scale = super.getScale();
        PVector shipScale = mShip.getScale();
        scale.x *= shipScale.x;
        scale.y *= shipScale.y;
        return scale;

    }

    /// Update the turret.
    /// \param delta the time since the last update.
    public void update(float delta) {

        updateFiring(delta);
        updatePowerUps(delta);


    }

    /// Render the turret.
    /// \param core processing core to use for rendering.
    public void renderCurrent(PApplet core) {

        // Only render when ship is visible.
        if (mShip.isVisible()) {

            super.renderCurrent(core);

        }

    }

    /// Update laser firing.
    /// \param delta time since last update.
    private void updateFiring(float delta) {

        mRechargeProgress -= delta;

        // Fire lasers as long as turret is firing and has fully recharged.
        if (mRechargeProgress < 0f) {

            if (mFiring) {

                float radianRotation = getRadianRotation();
                PVector laserTranslation = getTranslation();
                PVector fireTranslation = new PVector(0f, -FIRING_OFFSET);
                fireTranslation.rotate(radianRotation);
                laserTranslation.add(fireTranslation);

                PVector laserVelocity = new PVector(0f, -LASER_SPEED);
                laserVelocity.rotate(radianRotation);

                while (mRechargeProgress < 0f) {

                    mRechargeProgress += mRechargeDuration;
                    mProjectileManager.addLaser(mLaserId, 
                                                laserVelocity, 
                                                laserTranslation);

                }

            } else {

                mRechargeProgress = 0f;

            }

        }

    }

    /// Update the turret's power-up usage.
    /// \param delta time since last update.
    private void updatePowerUps(float delta) {

        // Use power-ups collected by ship.
        while (mShip.hasPowerUps()) {

            resetPowerUp();
            int powerUp = mShip.getPowerUp();
            if (powerUp == PowerUp.REFLECT_ID) {

                mLaserId = ReflectLaser.ID;

            } else if (powerUp == PowerUp.PENETRATE_ID) {

                mLaserId = PenetrateLaser.ID;

            } else if (powerUp == PowerUp.FIRE_RATE_ID) {

                mRechargeDuration = POWERUP_RECHARGE;

            }
            mPowerUpProgress = POWERUP_DURATION;

        }
        mPowerUpProgress -= delta;
        if (mPowerUpProgress < 0f) {

            resetPowerUp();

        }

    }

    /// Reset the power-up used by the turret when firing lasers.
    private void resetPowerUp() {

        mPowerUpProgress = 0f;
        mLaserId = NormalLaser.ID;
        mRechargeDuration = RECHARGE;

    }

}
