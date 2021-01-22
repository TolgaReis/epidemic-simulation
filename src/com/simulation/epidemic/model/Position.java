package com.simulation.epidemic.model;

import javafx.scene.layout.Pane;

/**
 * Position method for the individual that provides the position in canvas.
 */
public class Position {
    private double x;
    private double y;

    /**
     * Can be setted by using this constructor.
     * @param x x coordinate of the individual.
     * @param y y coordinate of the individual.
     */
    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Sets the position by randomly.
     * @param world the canvas.
     */
    public Position(Pane world) {
        this(Config.EDGE + Math.random() * (world.getWidth() - Config.EDGE), Config.EDGE + Math.random() * (world.getHeight() - Config.EDGE));
    }

    /**
     * Getter method for x coordinate.
     * @return x coordinate.
     */
    public double getX() {
        return x;
    }

    /**
     * Getter method for the y coordinate.
     * @return y coordinate.
     */
    public double getY() {
        return y;
    }

    /**
     * Changes the position of the individual so does movement.
     * @param direction the current direction that individual has.
     * @param world canvas.
     */
    public void move(Direction direction, Pane world) {
        this.x += direction.getDx();
        this.y += direction.getDy();
        if (this.x < Config.EDGE || this.x > world.getWidth() - Config.EDGE) {
            direction.reverseX();
            this.x += direction.getDx();
        }
        if (this.y < Config.EDGE || this.y > world.getHeight() - Config.EDGE) {
            direction.reverseY();
            this.y += direction.getDy();
        }
    }

    /**
     * Checks whether there is a collision according to social distance.
     * @param other the other individual's position.
     * @param socialDistance social distance the other one's.
     * @return true if they collide false otherwise.
     */
    public boolean collision(Position other, double socialDistance) {
        return (Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2))) <= socialDistance;
    }

    /**
     * Direction class for the individiuals.
     */
    public static class Direction {
        public double speed;
        private double dirX;
        private double dirY;

        /**
         * Constructor that provides random direction to individuals by using 180 degree.
         */
        public Direction() {
            this.dirX = Math.sin(2 * Math.PI * Math.random());
            this.dirY = Math.cos(2 * Math.PI * Math.random());
        }

        /**
         * Getter method for direction x.
         * @return
         */
        public double getDx() {
            return dirX * getSpeed();
        }

        /**
         * Getter method for direction y.
         * @return
         */
        public double getDy() {
            return dirY * getSpeed();
        }

        /**
         * Reverses the direction of the x.
         */
        public void reverseX() {
            this.dirX *= (-1.0);
        }

        /**
         * Reverses the direction of y.
         */
        public void reverseY() {
            this.dirY *= (-1.0);
        }

        /**
         * Getter method for the speed.
         * @return  speed of the individual.
         */
        public double getSpeed() {
            return speed;
        }

        /**
         * Setter method for the speed.
         * @param speed sets the speed.
         */
        public void setSpeed(double speed) {
            this.speed = speed;
        }
    }

}
