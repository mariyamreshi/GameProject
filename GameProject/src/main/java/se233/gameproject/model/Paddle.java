package se233.gameproject.model;
import javafx.scene.image.Image;
public class Paddle {
    private double x;
    private double y;
    private double width;
    private double height;
    private double speed;

    public Paddle(double x, double y, double width, double height, double speed){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
    }

    public void setWidth(double width) {this.width = width;}

    public void moveLeft(double gameAreaLeft){
        x -= speed;
        if (x < gameAreaLeft){
            x = gameAreaLeft;
        }
    }

    public void moveRight(double gameAreaRight){
        x += speed;
        if (x + width > gameAreaRight){
            x = gameAreaRight - width;
        }
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public double getWidth(){
        return width;
    }

    public double getHeight(){
        return height;
    }

}
