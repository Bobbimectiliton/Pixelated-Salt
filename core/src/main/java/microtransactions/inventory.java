package microtransactions;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static helper.Constants.HEIGHT;
import static helper.Constants.WIDTH;
import static microtransactions.MainScreen.ITEM_BOX_WIDTH;
import static microtransactions.MainScreen.WORLD_HEIGHT;
import static microtransactions.MainScreen.WORLD_WIDTH;
public class inventory implements Screen{

    io.github.killtheinnocents.Main main;
    LinkedList<Items> inventory;
    
    FitViewport invViewport, descViewport;
    SpriteBatch batch;
    Stage stage;
    Stage descPanel;
    Table cardContent;
    Table menus;
    Skin skin;
    BitmapFont font;
    GlyphLayout layout;
    OrthographicCamera invCam,descCam;

    Sprite common;
    Sprite rare;
    Sprite epic;
    Sprite legendary;
    Texture invBg;
    Texture cardBg;

    private String moneyText = "";
    private String scoreText = "";

    private double money;
    private float score;
    float scrollSpeed = 100;

    TextButton openCrate;
    TextButton inputMon;
    TextButton openShop;

    TextField.TextFieldStyle textFieldStyle;

    public inventory(LinkedList<Items> inventory, io.github.killtheinnocents.Main main, microtransactions.MainScreen prevScreen, float score){
        System.out.println("Switched");
        this.main = main;
        this.inventory =inventory;
        this.score = score;

        invCam = new OrthographicCamera();
        descCam = new OrthographicCamera();
        invViewport = new FitViewport(WIDTH/2, HEIGHT, invCam);
        descViewport = new FitViewport(WIDTH/2, HEIGHT, descCam);
        invViewport.apply();
        descViewport.apply();
        
        batch = new SpriteBatch();
        stage = new Stage(invViewport,batch);
        descPanel = new Stage(descViewport,batch);
        skin = new Skin(Gdx.files.internal("shop/skin/uiskin.json"));
        menus = new Table(skin);
        cardContent = new Table(skin);
        layout = new GlyphLayout();

        Texture font_texture = new Texture(Gdx.files.internal("game_font.png"));
        font_texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        font = new BitmapFont(Gdx.files.internal("game_font.fnt"), new TextureRegion(font_texture));
        font.getData().setScale(1.1f);
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub
        Gdx.input.setInputProcessor(stage);

        cardBg = new Texture(Gdx.files.internal("shop/CardBg.png"));
        invBg = new Texture(Gdx.files.internal("shop/InvBg.png"));
        
        menus.setFillParent(true);
        cardContent.setFillParent(true);

        // Create a the Menu to Hold all the items
        for(int i =inventory.size()-1;i>=0; i--){
            Items item = inventory.get(i);
            if(i%2==0 && i>0){
                menus.row();
            }
            iconButton icon = new iconButton(getItemBackground(item.getRarity()), item.getSprite(), i);
            icon.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    displayCard(item);
                }
            });
            if(icon.getIndex() % 2 ==0 && icon.getIndex() >0){
                menus.row();
            }
            menus.add(icon).pad(5);
        }
 
        stage.addActor(menus);
        descPanel.addActor(cardContent);

        // Create a new TextFieldStyle and set the font
        textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = font;
        textFieldStyle.background = skin.newDrawable("default-round"); // Set the background

        // Create a new TextButtonStyle and set the font
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.fontColor = Color.YELLOW;
        textButtonStyle.up = skin.newDrawable("default-round");
        textButtonStyle.down = skin.newDrawable("default-round-down");

        // Rarity BGs
        common = new Sprite(new Texture(Gdx.files.internal("shop/common.png")));
        common.setSize(ITEM_BOX_WIDTH, ITEM_BOX_WIDTH);
        rare = new Sprite(new Texture(Gdx.files.internal("shop/rare.png")));
        rare.setSize(ITEM_BOX_WIDTH, ITEM_BOX_WIDTH);
        epic = new Sprite(new Texture(Gdx.files.internal("shop/epic.png")));
        epic.setSize(ITEM_BOX_WIDTH, ITEM_BOX_WIDTH);
        legendary = new Sprite(new Texture(Gdx.files.internal("shop/legendary.png")));
        legendary.setSize(ITEM_BOX_WIDTH, ITEM_BOX_WIDTH);
    }

    private Sprite getItemBackground(int rarity) {
        switch (rarity) {
            case 1:
                return new Sprite(common);
            case 2:
                return new Sprite(rare);
            case 3:
                return new Sprite(epic);
            case 4:
                return new Sprite(legendary);
            default:
                System.out.println("Rarity not found: Line 209");
                return new Sprite(common); // Default to common if rarity is invalid
        }
    }

    private void displayCard(Items item){
        cardContent.clear();
        Sprite sprite = item.getSprite();
        String name = item.getName();
        String rarity = (item.getRarity()>3)?"Legendary":(item.getRarity()>2)?"Epic":(item.getRarity()>1)?"Rare":"Common";
        String type = item.getCategory();
        String stats = Float.toString(item.getScore());
        String desc = item.getDescription();

        TextField title = new TextField(rarity + " " + name, textFieldStyle);
        cardContent.add(new iconButton(getItemBackground(item.getRarity()), sprite, 5)).row();
        cardContent.add(new TextField(desc, textFieldStyle)).row();      
    }

    @Override
    public void render(float delta) {
        // TODO Auto-generated method stub
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // Render first viewport
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        invViewport.apply();
        //Set the projection matrix
        batch.setProjectionMatrix(invCam.combined);

        if (true) {
            invCam.position.x += scrollSpeed * Gdx.graphics.getDeltaTime();
            invCam.update();
        }

        batch.begin();
        batch.draw(invBg,0,0,invViewport.getWorldWidth(), invViewport.getWorldHeight());
        
        batch.end();

        stage.act();
        stage.draw();

        // Render the second viewport (right half of the screen)
        Gdx.gl.glViewport(Gdx.graphics.getWidth() / 2, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());
        descViewport.apply();
        batch.setProjectionMatrix(descCam.combined);

        if (true) {
            invCam.position.x += scrollSpeed * Gdx.graphics.getDeltaTime();
            invCam.update();
        }

        batch.begin();
        // Render objects for the second player
        batch.draw(cardBg,0,0,descViewport.getWorldWidth(), descViewport.getWorldHeight());
        
        layout.setText(font, moneyText);
        font.draw(batch, moneyText, WORLD_WIDTH-layout.width-10, WORLD_HEIGHT-1.1f*layout.height);
        
        scoreText = "Current Score: " + String.format("%.2f", score);
        layout.setText(font, scoreText);
        font.draw(batch, scoreText, WORLD_WIDTH-layout.width-10, WORLD_HEIGHT-2.4f*layout.height);
        batch.end();

        descPanel.act();
        descPanel.draw();
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub
        invViewport.update(width/2, height);
        descViewport.update(width/2, height);
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        cardBg.dispose();
        invBg.dispose();
    }
}
