package se233.gameproject.model;
import javafx.scene.image.Image;
public class Brick {
    private double x;
    private double y;
    private double width;
    private double height;
    private boolean destroyed;

    public Brick (double x, double y, double width, double height){
        this.x =x;
        this.y =y;
        this.width = width;
        this.height = height;
        this.destroyed = false;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void hit(){
        destroyed = true;
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
}
