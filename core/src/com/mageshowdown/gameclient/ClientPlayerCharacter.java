package com.mageshowdown.gameclient;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mageshowdown.gamelogic.AnimatedActorInterface;
import com.mageshowdown.gamelogic.GameWorld;
import com.mageshowdown.gamelogic.Orb;
import com.mageshowdown.gamelogic.PlayerCharacter;
import com.mageshowdown.packets.Network;
import com.mageshowdown.packets.Network.CastSpellProjectile;
import com.mageshowdown.packets.Network.KeyUp;
import com.mageshowdown.packets.Network.MoveKeyDown;
import com.mageshowdown.packets.Network.SwitchOrbs;
import com.mageshowdown.utils.PrefsKeys;

import java.util.Comparator;
import java.util.Random;

import static com.mageshowdown.gameclient.ClientAssetLoader.prefs;

public class ClientPlayerCharacter extends PlayerCharacter
        implements AnimatedActorInterface, Comparator<ClientPlayerCharacter>, InputProcessor {

    private GameClient myClient = GameClient.getInstance();

    private TextureRegion currShieldFrame;
    private TextureRegion currFrozenFrame;

    private boolean moveLeft = false;
    private boolean moveRight = false;
    private boolean jump = false;
    private boolean isMyPlayer;
    private boolean castSpellProjectile = false;
    private boolean castBomb = false;
    private boolean switchOrbs = false;

    private String userName;
    private int id;
    private static Random rndSound = new Random();

    public ClientPlayerCharacter(ClientGameStage stage, Vector2 position, Orb.SpellType orbEquipped, String userName, boolean isMyPlayer) {
        super(stage, position, orbEquipped, true);
        this.isMyPlayer = isMyPlayer;

        //if its the client's player, load the ally assets for the character, otherwise load the ones for the enemy
        if (isMyPlayer) {
            addAnimation(4, 1, 1.2f, "idle", ClientAssetLoader.friendlyIdleSpritesheet);
            addAnimation(2, 1, .8f, "jumping", ClientAssetLoader.friendlyJumpingSpritesheet);
            addAnimation(8, 1, 1f, "running", ClientAssetLoader.friendlyRunningSpritesheet);
        } else {
            addAnimation(4, 1, 1.2f, "idle", ClientAssetLoader.enemyIdleSpritesheet);
            addAnimation(2, 1, .8f, "jumping", ClientAssetLoader.enemyJumpingSpritesheet);
            addAnimation(8, 1, 1f, "running", ClientAssetLoader.enemyRunningSpritesheet);
        }

        addAnimation(5, 4, 2f, "energy shield", ClientAssetLoader.energyShieldSpritesheet);
        addAnimation(5, 7, 2f, "frozen", ClientAssetLoader.frozenSpritesheet);
        this.userName = userName;

    }

    @Override
    public void act(float delta) {
        setBodyFixedRotation();
        sendInputPackets();
        pickFrame();
        calcState();
        updateOrbPosition();
        updateFrozenState();
        updateSpellState();
        updateGameActor(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (dmgImmune)
            batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 0);
        if (energyShield > 0 && currShieldFrame != null)
            batch.draw(currShieldFrame, getX() - getWidth() / 2, getY() - getHeight() / 2, getWidth() * getScaleX() * 1.5f, getHeight() * getScaleY() * 1.5f);
        if (frozen && currFrozenFrame != null)
            batch.draw(currFrozenFrame, getX() - getWidth() / 2, getY() - getHeight() / 2, getWidth() * getScaleX() * 1.5f, getHeight() * getScaleY() * 1.5f);
        if (dmgImmune)
            batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 255);
    }

    @Override
    public void pickFrame() {
        if (animations.containsKey("energy shield"))
            currShieldFrame = animations.get("energy shield").getKeyFrame(passedTime, true);
        if (frozen && animations.containsKey("frozen"))
            currFrozenFrame = animations.get("frozen").getKeyFrame(frozenTimer, true);

        /*
         * if the character is grounded, we have to see if hes moving left or right or standing and change the animation accordingly
         * regardless of wether its flying or its grounded, the animation frame needs
         * to be flipped if youre going left, looking right being the "default" position
         */
        switch (verticalState) {
            case GROUNDED:
                if (animations.containsKey(horizontalState.toString()))
                    currFrame = animations.get(horizontalState.toString()).getKeyFrame(passedTime, true);
                break;
            case FLYING:
                if (animations.containsKey(verticalState.toString()))
                    currFrame = animations.get(verticalState.toString()).getKeyFrame(passedTime, false);
                break;
        }

    }

    private void sendInputPackets() {
        MoveKeyDown keyPress = new MoveKeyDown();

        if (moveLeft) {
            keyPress.keycode = Input.Keys.A;
            myClient.sendTCP(keyPress);
        } else if (moveRight) {
            keyPress.keycode = Input.Keys.D;
            myClient.sendTCP(keyPress);
        } else if (jump) {
            keyPress.keycode = Input.Keys.W;
            myClient.sendTCP(keyPress);
        }
        if (castSpellProjectile) {
            castMySpellProjectile();
        } else if (castBomb) {
            castMyBomb();
        } else if (switchOrbs) {
            switchMyOrbs();
            myClient.sendTCP(new SwitchOrbs());
            switchOrbs = false;
        }
    }

    private void calcState() {
        if (body != null) {
            if (body.getLinearVelocity().y > 0.0001f || body.getLinearVelocity().y < -0.0001f) {
                //if we detect that we start flying, then we need to reset the passed time so the animation wont loop
                if (verticalState != VerticalState.FLYING)
                    passedTime = 0f;

                verticalState = VerticalState.FLYING;
            } else verticalState = VerticalState.GROUNDED;


            if (body.getLinearVelocity().x > 0.0001f) {
                horizontalState = HorizontalState.GOING_RIGHT;
            } else if (body.getLinearVelocity().x < -0.0001f) {
                horizontalState = HorizontalState.GOING_LEFT;
            } else {
                horizontalState = HorizontalState.STANDING;
            }

            if (body.getLinearVelocity().y != 0) {
                velocity.y = body.getLinearVelocity().y;
            }
        }
        if (currFrame != null) {
            switch (horizontalState) {
                case GOING_LEFT:
                    //as there is no setFlip() for TextureRegion we have to check if the texture's already flipped, libgdx pls
                    if (!currFrame.isFlipX())
                        currFrame.flip(true, false);
                    break;
                case GOING_RIGHT:
                    if (currFrame.isFlipX())
                        currFrame.flip(true, false);
                    break;
                case STANDING:
                    //only the client's own character flips in the direction of the mouse
                    if (isMyPlayer) {
                        if (GameWorld.getMousePos().x < getX()) {
                            currFrame.flip(!currFrame.isFlipX(), false);
                        } else {
                            currFrame.flip(currFrame.isFlipX(), false);
                        }
                    } else {
                        currFrame.flip(currFrame.isFlipX(), false);
                    }
                    break;
            }
        }
    }

    //when casting a projectile we need the rotation angle from which we calculate the direction vector
    public void castMySpellProjectile() {
        CastSpellProjectile packet = new CastSpellProjectile();
        float rotation = GameWorld.getMouseVectorAngle(new Vector2(currentOrb.getX() + currentOrb.getOriginX(), currentOrb.getY() + currentOrb.getOriginY()));
        Vector2 direction = new Vector2((float) Math.cos(rotation * Math.PI / 180), (float) Math.sin(rotation * Math.PI / 180));
        if (currentOrb.getSpellType() == Orb.SpellType.FROST)
            hasJustCastFrostProjectile();
        else hasJustCastLaser();

        packet.id = myClient.getID();
        packet.rot = rotation;
        myClient.sendTCP(packet);
        currentOrb.castSpellProjectile(direction, rotation, myClient.getID());

        castSpellProjectile = false;
    }

    //when casting a bomb we only want the position of the mouse
    public void castMyBomb() {
        Network.CastBomb packet = new Network.CastBomb();
        if (currentOrb.getSpellType() == Orb.SpellType.FROST)
            hasJustCastFrostBomb();
        else hasJustCastFireBomb();

        packet.id = myClient.getID();
        packet.pos = GameWorld.getMousePos(new Vector2(95, 95));
        myClient.sendTCP(packet);
        currentOrb.castBomb(packet.pos, myClient.getID());

        castBomb = false;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public int compare(ClientPlayerCharacter o1, ClientPlayerCharacter o2) {
        if (o1.score < o2.score)
            return -1;
        else if (o1.score == o2.score)
            return 0;
        else return 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ClientPlayerCharacter) {
            ClientPlayerCharacter temp = (ClientPlayerCharacter) obj;
            return temp.id == this.id;
        }
        return false;
    }

    @Override
    public String toString() {
        return userName + " " + kills + " " + score;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.D) {
            moveRight = true;
        } else if (keycode == Input.Keys.A) {
            moveLeft = true;
        }
        if (keycode == Input.Keys.W) {
            jump = true;
        }
        if (keycode == Input.Keys.Q) {
            switchOrbs = true;
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        KeyUp ku = new KeyUp();
        ku.keycode = keycode;
        if (keycode == Input.Keys.A) {
            moveLeft = false;
            myClient.sendTCP(ku);
        } else if (keycode == Input.Keys.D) {
            moveRight = false;
            myClient.sendTCP(ku);
        } else if (keycode == Input.Keys.W) {
            jump = false;
            myClient.sendTCP(ku);
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT)
            castSpellProjectile = true;
        else if (button == Input.Buttons.RIGHT)
            castBomb = true;
        return true;
    }

    public void hasJustFrozen() {
        System.out.println("someone just froze");
    }

    public void hasJustCastFrostProjectile() {
        System.out.println("someone just cast a frost spell projectile");
        ClientAssetLoader.frostShot.play(prefs.getFloat(PrefsKeys.SOUNDVOLUME) / 2);
    }

    public void hasJustCastLaser() {
        System.out.println("someone just cast a laser spell");
        if (rndSound.nextBoolean())
            ClientAssetLoader.fireShot1.play(prefs.getFloat(PrefsKeys.SOUNDVOLUME) / 2);
        else
            ClientAssetLoader.fireShot2.play(prefs.getFloat(PrefsKeys.SOUNDVOLUME) / 2);
    }

    public void hasJustCastFireBomb() {
        System.out.println("someone just cast a fire bomb");
    }

    public void hasJustCastFrostBomb() {
        System.out.println("someone just cast a frost bomb");
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public void setMyPlayer(boolean myPlayer) {
        isMyPlayer = myPlayer;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
