package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.base.BaseScreen;
import com.mygdx.game.base.Font;
import com.mygdx.game.exception.GameException;
import com.mygdx.game.math.Rect;
import com.mygdx.game.pool.BulletPool;
import com.mygdx.game.pool.EnemyPool;
import com.mygdx.game.pool.ExplosionPool;
import com.mygdx.game.sprites.Background;
import com.mygdx.game.sprites.Bullet;
import com.mygdx.game.sprites.Enemy;
import com.mygdx.game.sprites.GameOver;
import com.mygdx.game.sprites.MainShip;
import com.mygdx.game.sprites.ButtonNewGame;
import com.mygdx.game.sprites.Star;
import com.mygdx.game.utils.EnemyEmitter;

import java.util.List;

public class GameScreen extends BaseScreen {

    private enum State {PLAYING, PAUSE, GAME_OVER}
    private static final float FONT_MARGIN = 0.01f;
    private static final float FONT_SIZE = 0.02f;
    private static final String FRAGS = "Frags: ";
    private static final String HP = "HP: ";
    private static final String LEVEL = "Level: ";


    private static final int STAR_COUNT = 64;

    private Texture bg;
    private Background background;
    private TextureAtlas atlas;

    private Star[] stars;

    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private ExplosionPool explosionPool;

    private EnemyEmitter enemyEmitter;

    private MainShip shipMain;
    private GameOver gameOver;
    private ButtonNewGame buttonNewGame;


    private Music music;
    private Sound laserSound;
    private Sound explosion;
    private Sound bulletSound;
    private State state;
    private State prevState;

    private Font font;
    private StringBuilder sbFrags;
    private StringBuilder sbHP;
    private StringBuilder sbLevel;

    private int frags;

    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/bg.png");
        atlas = new TextureAtlas(Gdx.files.internal("textures/mainAtlas.tpack"));
        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        explosion = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        bulletPool = new BulletPool();
        explosionPool = new ExplosionPool(atlas, explosion);
        enemyPool = new EnemyPool(bulletPool, explosionPool, worldBounds);
        enemyEmitter = new EnemyEmitter(atlas, enemyPool, worldBounds, bulletSound);
        font = new Font("font/font.fnt", "font/font.png");
        font.setSize(FONT_SIZE);
        sbFrags = new StringBuilder();
        sbHP = new StringBuilder();
        sbLevel = new StringBuilder();
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        initSprites();
        state = State.PLAYING;
        frags = 0;
    }

    @Override
    public void render(float delta) {
        update(delta);
        checkCollisions();
        freeAllDestroyed();
        draw();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (Star star : stars) {
            star.resize(worldBounds);
        }
        shipMain.resize(worldBounds);
        gameOver.resize(worldBounds);
        buttonNewGame.resize(worldBounds);
    }

    @Override
    public void pause() {
        prevState = state;
        state = State.PAUSE;
        music.pause();
    }

    @Override
    public void resume() {
        state = prevState;
        music.play();
    }

    @Override
    public void dispose() {
        bg.dispose();
        atlas.dispose();
        bulletPool.dispose();
        music.dispose();
        laserSound.dispose();
        bulletSound.dispose();
        enemyPool.dispose();
        explosion.dispose();
        explosionPool.dispose();
        font.dispose();
        super.dispose();

    }

    @Override
    public boolean keyDown(int keycode) {
        if (state == State.PLAYING) {
            shipMain.keyDown(keycode);
        }
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        if (state == State.PLAYING) {
            shipMain.keyUp(keycode);
        }
        return super.keyUp(keycode);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (state == State.PLAYING) {
            shipMain.touchDown(touch, pointer, button);
        } else if (state == State.GAME_OVER) {
            buttonNewGame.touchDown(touch, pointer, button);

        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (state == State.PLAYING) {
            shipMain.touchUp(touch, pointer, button);
        } else if (state == State.GAME_OVER) {
            buttonNewGame.touchUp(touch, pointer, button);
        }
        return false;
    }

    public void startNewGAme() {
        state = State.PLAYING;
        frags = 0;
        bulletPool.freeAllActiveObjects();
        enemyPool.freeAllActiveObjects();
    }

    private void update(float delta) {
        for (Star star : stars) {
            star.update(delta);
        }
        explosionPool.updateActiveSprites(delta);
        if (state == State.PLAYING) {
            shipMain.update(delta);
            bulletPool.updateActiveSprites(delta);
            enemyPool.updateActiveSprites(delta);
            enemyEmitter.generate(delta, frags);
        } else if (state == State.GAME_OVER){
            buttonNewGame.update(delta);
        }
    }

    private void checkCollisions() {
        if (state != State.PLAYING) {
            return;
        }
        List<Enemy> enemyList = enemyPool.getActiveObjects();
        List<Bullet> bulletList = bulletPool.getActiveObjects();
        for (Enemy enemy : enemyList) {
            if (enemy.isDestroyed()) {
                continue;
            }
            float minDist = enemy.getHalfWidth() + shipMain.getHalfWidth();
            if (shipMain.pos.dst(enemy.pos) < minDist) {
                enemy.destroy();
                frags++;
                shipMain.damage(enemy.getDamage());
            }
            for (Bullet bullet : bulletList) {
                if (bullet.getOwner() != shipMain || bullet.isDestroyed()) {
                    continue;
                }
                if (enemy.isBulletCollision(bullet)) {
                    enemy.damage(bullet.getDamage());
                    bullet.destroy();
                    if(enemy.isDestroyed()) {
                        frags++;
                    }
                }
            }

        }
        for (Bullet bullet : bulletList) {
            if (bullet.getOwner() == shipMain || bullet.isDestroyed()) {
                continue;
            }
            if (shipMain.isBulletCollision(bullet)) {
                shipMain.damage(bullet.getDamage());
                bullet.destroy();
            }
        }
        if (shipMain.isDestroyed()) {
            state = State.GAME_OVER;
        }

    }

    private void freeAllDestroyed() {
        bulletPool.freeAllDestroyedActiveObjects();
        enemyPool.freeAllDestroyedActiveObjects();
        explosionPool.freeAllDestroyedActiveObjects();
    }

    private void draw() {
        Gdx.gl.glClearColor(0.5f, 0.7f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for (Star star : stars) {
            star.draw(batch);
        }
        switch (state) {
            case PLAYING:
                shipMain.draw(batch);
                enemyPool.drawActiveSprites(batch);
                bulletPool.drawActiveSprites(batch);
                break;
            case GAME_OVER:
                gameOver.draw(batch);
                buttonNewGame.draw(batch);
                break;
        }
        explosionPool.drawActiveSprites(batch);
        printInfo();
        batch.end();
    }

    private void printInfo() {
        sbFrags.setLength(0);
        sbHP.setLength(0);
        sbLevel.setLength(0);
        font.draw(batch, sbFrags.append(FRAGS).append(frags), worldBounds.getLeft() + FONT_MARGIN, worldBounds.getTop() - FONT_MARGIN);
        font.draw(batch, sbHP.append(HP).append(shipMain.getHp()), worldBounds.pos.x, worldBounds.getTop() - FONT_MARGIN, Align.center);
        font.draw(batch, sbLevel.append(LEVEL).append(enemyEmitter.getLevel()), worldBounds.getRight() - FONT_MARGIN, worldBounds.getTop() - FONT_MARGIN, Align.right);
    }


    private void initSprites() {
        try {
            background = new Background(bg);
            stars = new Star[STAR_COUNT];
            for (int i = 0; i < STAR_COUNT; i++) {
                stars[i] = new Star(atlas);
            }
            shipMain = new MainShip(atlas, bulletPool, explosionPool, laserSound);
            gameOver = new GameOver(atlas);
            buttonNewGame = new ButtonNewGame(atlas, this, shipMain);
            music.play();
            music.setLooping(true);
        } catch (GameException e) {
            throw new RuntimeException(e);
        }
    }
}
