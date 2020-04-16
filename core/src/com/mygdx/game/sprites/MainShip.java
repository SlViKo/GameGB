package com.mygdx.game.sprites;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.base.Ship;
import com.mygdx.game.exception.GameException;
import com.mygdx.game.math.Rect;
import com.mygdx.game.pool.BulletPool;
import com.mygdx.game.pool.ExplosionPool;

public class MainShip extends Ship {

    private static final int HP = 100;
    private final float HIGHT_SHIP = 0.15f;
    private final float BOTTOM_MARGIN = 0.02f;
    private static final int INVALID_POINTER = -1;

    private boolean pressedLeft;
    private boolean pressedRight;

    private int leftPointer = INVALID_POINTER;
    private int rightPointer = INVALID_POINTER;

    public MainShip(TextureAtlas atlas, BulletPool bulletPool, ExplosionPool explosionPool, Sound shootSound) throws GameException {
        super(atlas.findRegion("main_ship"), 1, 2, 2);
        this.bulletPool = bulletPool;
        this.shootSound = shootSound;
        this.explosionPool = explosionPool;
        bulletRegion = atlas.findRegion("bulletMainShip");
        bulletV = new Vector2(0, 0.5f);
        bulletPos = new Vector2();
        v0 = new Vector2(0.5f, 0);
        v = new Vector2();
        reloadInterval = 0.3f;
        reloadTimer = reloadInterval;
        bulletHeight = 0.01f;
        damage = 1;
        hp = HP;
        isAutoShoot = true;
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        setHeightProportion(HIGHT_SHIP);
        setBottom(worldBounds.getBottom() + BOTTOM_MARGIN);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        bulletPos.set(pos.x, pos.y + getHalfHeight());
        if (getLeft() < worldBounds.getLeft()) {
            setLeft(worldBounds.getLeft());
            stop();
        }
        if (getRight() > worldBounds.getRight()) {
            setRight(worldBounds.getRight());
            stop();
        }
    }



    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (touch.x < worldBounds.pos.x) {
            if (leftPointer != INVALID_POINTER) {
                return false;
            }
            leftPointer = pointer;
            moveShipLeft();
        } else {
            if (rightPointer != INVALID_POINTER) {
                return false;
            }
            rightPointer = pointer;
            moveShipRight();
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (pointer == leftPointer) {
            leftPointer = INVALID_POINTER;
            if (rightPointer != INVALID_POINTER) {
                moveShipRight();
            } else {
                stop();
            }
        } else if (pointer == rightPointer) {
            rightPointer = INVALID_POINTER;
            if (leftPointer != INVALID_POINTER) {
                moveShipLeft();
            } else {
                stop();
            }
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {

        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = true;
                moveShipLeft();
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = true;
                moveShipRight();
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = false;
                if (pressedRight) {
                    moveShipRight();
                } else {
                    stop();
                }
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = false;
                if (pressedLeft) {
                    moveShipLeft();
                } else {
                    stop();
                }

                break;
        }
        return false;
    }

    public boolean isBulletCollision(Rect bullet) {
        return !(bullet.getRight() < getLeft()
                || bullet.getLeft() > getRight()
                || bullet.getBottom() > pos.y
                || bullet.getTop() < getBottom());
    }

    public void newGame() {
        flushDestroy();
        pressedLeft = false;
        pressedRight = false;
        leftPointer = INVALID_POINTER;
        reloadTimer = INVALID_POINTER;
        hp = HP;
        pos.x = 0;
        stop();
    }


    private void moveShipRight() {
        v.set(v0);
    }


    private void moveShipLeft() {
        v.set(v0).rotate(180);
    }

    private void stop() {
        v.setZero();
    }
}
