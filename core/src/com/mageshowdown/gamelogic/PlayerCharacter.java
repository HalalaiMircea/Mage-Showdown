package com.mageshowdown.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.Stage;


public abstract class PlayerCharacter extends DynamicGameActor {
    protected static final float MAXIMUM_ENERGY_SHIELD = 5f;
    protected static final float MAXIMUM_HEALTH = 15f;
    protected static final float FREEZE_DURATION = 2f;

    protected Stage gameStage;

    protected Weapon freezeWeapon;
    protected Weapon fireWeapon;
    protected Weapon currWeapon;

    protected float energyShield = MAXIMUM_ENERGY_SHIELD;
    protected float health = MAXIMUM_HEALTH;

    protected boolean dmgImmune = false;
    protected boolean frozen = false;
    protected float frozenTimer = 0f;

    protected int score = 0;
    protected int kills = 0;

    protected PlayerCharacter(Stage stage, Vector2 position, int weaponEquipped, boolean loadWeaponAnimation) {
        super(stage, position, new Vector2(22, 32), 0f, new Vector2(1.5f, 1.5f));

        createBody(BodyDef.BodyType.DynamicBody);
        gameStage = stage;
        body.setFixedRotation(true);

        freezeWeapon = new Weapon(stage, loadWeaponAnimation, Weapon.AmmoType.FREEZE, 1.5f, 25);
        fireWeapon = new Weapon(stage, loadWeaponAnimation, Weapon.AmmoType.FIRE, 1.5f, 25);

        freezeWeapon.remove();
        fireWeapon.remove();

        if (weaponEquipped == 1)
            currWeapon = freezeWeapon;
        else currWeapon = fireWeapon;

        gameStage.addActor(currWeapon);
    }

    @Override
    public boolean remove() {
        destroyWeapons();
        return super.remove();
    }


    protected void updateWeaponPos() {
        if (currWeapon != null)
            currWeapon.updatePosition(new Vector2(getX(), getY()));
    }

    protected void updateFrozenState() {
        if (frozen) {
            frozenTimer += Gdx.graphics.getDeltaTime();
            if (frozenTimer > FREEZE_DURATION) {
                frozen = false;
                frozenTimer = 0f;
            }
        }
    }

    protected void destroyWeapons() {
        freezeWeapon.remove();
        fireWeapon.remove();
    }

    public void switchMyWeapons() {
        currWeapon.unequipWeapon();

        if (currWeapon.equals(freezeWeapon)) {
            currWeapon = fireWeapon;
        } else {
            currWeapon = freezeWeapon;
        }
        currWeapon.equipWeapon();
    }

    public Weapon getCurrWeapon() {
        return currWeapon;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public static float getMaxShield() {
        return MAXIMUM_ENERGY_SHIELD;
    }

    public static float getMaxHealth() {
        return MAXIMUM_HEALTH;
    }

    public float getEnergyShield() {
        return energyShield;
    }

    public void setEnergyShield(float energyShield) {
        this.energyShield = energyShield;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public boolean isDmgImmune() {
        return dmgImmune;
    }

    public void setDmgImmune(boolean dmgImmune) {
        this.dmgImmune = dmgImmune;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getWeaponEquipped() {
        if (currWeapon.equals(freezeWeapon))
            return 1;
        else return 2;
    }
}
