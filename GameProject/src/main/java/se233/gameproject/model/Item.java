package se233.gameproject.model;

public class Item {
    private double x,y;
    private double width;
    private double height;
    private double speed = 1.5;
    private String type;
    private boolean active = true;

    public Item(double x, double y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;
        if ("Dizzy".equals(type)) {
            width = 28;
            height = 24;
        } else {
            width = 20;
            height = 10;
        }
    }

    public void move() {
        y += speed;
        if (y > 600) {
            active = false;
        }
    }

    public void deactivate() { active = false; }
    public boolean isActive() { return active; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public String getType() { return type; }
}
