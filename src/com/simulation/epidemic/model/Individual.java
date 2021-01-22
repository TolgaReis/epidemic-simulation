package com.simulation.epidemic.model;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.util.Random;

/**
 * Individual class that represents the individuals on the simulation.
 */
public class Individual {
    private Case state;
    private Position location;
    private Position.Direction direction;
    private Rectangle rectangle;
    private Pane world;
    public double distance;
    public double collisionTime;
    public double mask;
    public int wait;
    public int illTime;
    public int hospitalTime;

    /**
     * Contructor used for first individual.
     * @param state the state of the individual.
     * @param world canvas.
     */
    public Individual(Case state, Pane world) {
        this.state = state;
        this.direction = new Position.Direction();
        this.location = new Position(world);
        this.rectangle = new Rectangle(Config.EDGE, Config.EDGE, state.getColor());
        this.rectangle.setStroke(state.getColor());
        this.world = world;

        this.mask = new Random().nextBoolean() ? 0.2 : 1.0;
        this.collisionTime = (Math.random() * 5);
        this.distance = (Math.random() * 9);
        this.wait = 0;
        this.illTime = 0;
        this.hospitalTime = 0;
        this.direction.setSpeed(1 + (int)(Math.random() * 500));
        world.getChildren().add(this.rectangle);
    }
    /**
     * Contructor used for to create any individual.
     * @param state the state of the individual.
     * @param world canvas.
     */
    public Individual(Case state, Pane world, double mask, double speed, double socialDistance, double collisionTime) {
        this.state = state;
        this.direction = new Position.Direction();
        this.location = new Position(world);
        this.rectangle = new Rectangle(Config.EDGE, Config.EDGE, state.getColor());
        this.rectangle.setStroke(state.getColor());
        this.world = world;

        this.mask = mask;
        this.collisionTime = collisionTime;
        this.distance = socialDistance;
        this.wait = 0;
        this.illTime = 0;
        this.hospitalTime = 0;
        this.direction.setSpeed(speed);
        world.getChildren().add(this.rectangle);
    }

    /**
     * Getter method for the state of the individual.
     * @return state of the individual.
     */
    public Case getState() {
        return state;
    }

    /**
     * Setter method for the individual.
     * @param state sets this to state.
     */
    public void setState(Case state) {
        this.state = state;
        this.rectangle.setFill(state.getColor());
    }

    /**
     * Used to move the individual.
     */
    public void move() {
        this.location.move(this.direction, this.world);
    }

    /**
     * Used to draw the individual.
     */
    public void draw() {
        if (this.getState() != Case.DEATH || this.getState() != Case.HOSPITALIZE) {
            this.rectangle.setHeight(Config.EDGE);
            this.rectangle.setWidth(Config.EDGE);
            this.rectangle.setTranslateX(this.location.getX());
            this.rectangle.setTranslateY(this.location.getY());
        }
    }

    /**
     * Checks the collision to infect to other ones.
     * @param other other individual.
     */
    public void collisionCheck(Individual other) {
        if (this.getState() != Case.DEATH &&
            other.getState() != Case.DEATH &&
            this.getState() != Case.HOSPITALIZE &&
            other.getState() != Case.HOSPITALIZE) {
            double collisionTime = this.getMaxCollisionTime(this.getCollisionTime(), other.getCollisionTime());
            double socialDistance = this.getMinSocialDistance(this.getDistance(), other.getDistance());
            if (this.location.collision(other.location, socialDistance)) {
                double probability = Config.R * (1.0 + collisionTime / 10.0) * this.getMask() * other.getMask() * (1.0 - socialDistance / 10.0);
                double random = Math.random() % 1.0;
                if ((random <= probability) && (this.getWait() == 0) && (other.getWait() == 0)) {
                    if ((other.getState() == Case.ILL && this.state == Case.HEALTHY)) {
                        this.setState(Case.ILL);
                        Config.ILL_NUMBER++;
                        Config.HEALTH_NUMBER--;
                        this.setWait((int)collisionTime);
                        other.setWait((int)collisionTime);
                    } else if ((other.getState() == Case.HEALTHY && this.state == Case.ILL)) {
                        other.setState(Case.ILL);
                        Config.ILL_NUMBER++;
                        Config.HEALTH_NUMBER--;
                        this.setWait((int)collisionTime);
                        other.setWait((int)collisionTime);
                    }
                }
            }
        }
    }

    /**
     * Handles the minimum social distance
     * @param d1 first social distance.
     * @param d2 second social distance.
     * @return minimum one
     */
    private double getMinSocialDistance(double d1, double d2) {
        if (d1 > d2)
            return d2;
        else
            return d1;
    }
    /**
     * Handles the maximum collision time
     * @param c1 first collision time.
     * @param c2 second collision time.
     * @return maximum one
     */
    private double getMaxCollisionTime(double c1, double c2) {
        if (c1 < c2)
            return c2;
        else
            return c1;
    }

    /**
     * Getter method for social distance.
     * @return social distance.
     */
    public double getDistance() {
        return distance;
    }
    /**
     * Getter method for collision time.
     * @return collision time.
     */
    public double getCollisionTime() {
        return collisionTime;
    }

    /**
     * Getter method for mask.
     * @return mask.
     */
    public double getMask() {
        return mask;
    }

    /**
     * Getter method for collision wait.
     * @return collision wait.
     */
    public int getWait() {
        return wait;
    }

    /**
     * Setter method for collision wait.
     * @param wait collision wait.
     */
    public void setWait(int wait) {
        this.wait = wait;
    }

    /**
     * Getter method for illnes time.
     * @return illness time.
     */
    public int getIllTime() {
        return illTime;
    }

    /**
     * Setter method for illness time.
     * @param illTime illness time.
     */
    public void setIllTime(int illTime) {
        this.illTime = illTime;
    }

    /**
     * Getter method for hospital time.
     * @return hospital time.
     */
    public int getHospitalTime() {
        return hospitalTime;
    }

    /**
     * Setter method for hospital time.
     * @param hospitalTime hospital time.
     */
    public void setHospitalTime(int hospitalTime) {
        this.hospitalTime = hospitalTime;
    }

    /**
     * Builder class used to implement Builder Design Pattern.
     */
    public static class IndividualBuilder {
        private Case state;
        private Pane world;
        public double distance;
        public double collisionTime;
        public double mask;
        public double speed;

        /**
         * Constructor to create canvas and state.
         * @param state state.
         * @param world canvas.
         */
        public IndividualBuilder(Case state, Pane world) {
            this.state = state;
            this.world = world;
        }

        /**
         * Builder method for distance.
         * @param distance social distance.
         * @return object.
         */
        public IndividualBuilder distance(double distance) {
            this.distance = distance;
            return this;
        }

        /**
         * Builder method for collision time.
         * @param collisionTime collision time.
         * @return object
         */
        public IndividualBuilder collisionTime(double collisionTime) {
            this.collisionTime = collisionTime;
            return this;
        }

        /**
         * Builder method for mask.
         * @param mask mask.
         * @return object.
         */
        public IndividualBuilder mask(double mask) {
            this.mask = mask;
            return this;
        }

        /**
         * Builder method for speed.
         * @param speed speed.
         * @return object.
         */
        public IndividualBuilder speed(double speed) {
            this.speed = speed;
            return this;
        }

        /**
         * Builder method for infected individual.
         * @return Individual object.
         */
        public Individual buildFirstInfected() {
            return new Individual(state, world);
        }

        /**
         * Builder method for normal individual.
         * @return Individual object.
         */
        public Individual buildIndividual() {
            return new Individual(state, world, mask, speed, distance, collisionTime);
        }
    }
}

