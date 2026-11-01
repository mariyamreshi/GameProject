package se233.gameproject.model;
import javafx.scene.image.Image;
public class Brick {
    private double x, y, width, height;
    private boolean destroyed;
    private int hp;
    private int scoreValue;
    private int type; // 0=Normal, 1=Strong, 2=Bonus, 3=Special

    public Brick (double x, double y, double width, double height, int type){
        this.x =x;
        this.y =y;
        this.width = width;
        this.height = height;
        this.type = type;
        this.destroyed = false;

        switch (type) {
            case 1:
                this.hp = 2;
                this.scoreValue = 20;
                break;
            case 2:
                this.hp = 1;
                this.scoreValue = 50;
                break;
            case 3:
                this.hp = 1;
                this.scoreValue = 10;
                break;
            default:
                this.hp = 1;
                this.scoreValue = 10;
                break;
        }
    }

    public void hit(){
        hp--;
        if (hp <= 0){
            destroyed = true;
        }
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }

    public double getWidth() {
        return width;
    }
    public double getHeight() {
        return height;
    }

    public int getType() { return type; }
    public int getHp() { return hp; }
    public int getScoreValue() { return scoreValue; }
}
