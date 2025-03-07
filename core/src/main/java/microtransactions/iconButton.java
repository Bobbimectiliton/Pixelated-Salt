package microtransactions;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static microtransactions.MainScreen.ITEM_BOX_WIDTH;

public class iconButton extends Actor{
    Sprite rarity;
    Sprite item;
    int index;
    public iconButton(Sprite rarity, Sprite item, int index){
        this.item = item;
        this.rarity = rarity;
        this.index = index;

        setSize(ITEM_BOX_WIDTH,ITEM_BOX_WIDTH);   
    }

    public int getIndex(){
        return index;
    }

    public void draw(SpriteBatch batch){
        rarity.setPosition(getX(), getX());
        item.setPosition(getX(), getX());
        rarity.draw(batch);
        item.draw(batch);
    }

    @Override
    public void setPosition(float x, float y){
        super.setPosition(x, y);
        rarity.setPosition(x, y);
        item.setPosition(x, y);
    }
}
