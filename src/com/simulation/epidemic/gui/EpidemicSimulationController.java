package com.simulation.epidemic.gui;

import com.simulation.epidemic.model.Config;
import com.simulation.epidemic.model.IndividualMediator;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Main controller class that is used by interface.
 */
public class EpidemicSimulationController {
    @FXML
    Slider R;

    @FXML
    Slider Z;

    @FXML
    TextField timerText;

    @FXML
    TextField totalIndividual;

    @FXML
    TextField numberOfDeath;

    @FXML
    TextField hospitalize;

    @FXML
    TextField caseNumber;

    @FXML
    TextField healthNumber;

    @FXML
    Pane world;

    @FXML
    Button startButton;

    @FXML
    Button stopButton;

    @FXML
    Button insertButton;

    @FXML
    RadioButton mask;

    @FXML
    Slider speed;

    @FXML
    Slider socialDistance;

    @FXML
    Slider collisionTime;

    @FXML
    Slider individualNumber;

    IndividualMediator individualMediator;
    private Timer timer;

    /**
     * AnimationTimer extension that refreshes the page every second
     */
    private class Timer extends AnimationTimer {
        private long last = 0;
        private int timer = 0;

        /**
         * The main method that does the refreshing job each second.
         * @param now the current time in nanoseconds.
         */
        @Override
        public void handle(long now) {
            if (getSeconds(now)) {
                individualMediator.resolveCollisions();
                individualMediator.move();
                individualMediator.draw();
                texts();
                last = now;
                timer++;
            }
        }

        /**
         * Getter method for timer.
         * @return timer.
         */
        public int getTimer() {
            return this.timer;
        }

        /**
         * Setter method for timer.
         * @param timer new value of the timer.
         */
        public void setTimer(int timer) {
            this.timer = timer;
        }

        /**
         * Returns true if it passed 1 second false otherwise.
         * @param now the last time
         * @return true if 1 second.
         */
        private boolean getSeconds(long now) {
            return (now - last) >= 1000000000L;
        }
    }

    /**
     * Setter method for mortality rate that comes from slider.
     */
    public void setMortalityRate() {
        Config.Z = (double) this.Z.getValue();
    }
    /**
     * Setter method for spreading factor that comes from slider.
     */
    public void setSpreadingFactor() {
        Config.R = (double) this.R.getValue();
    }

    /**
     * Initializes the system by reading from gui.
     */
    @FXML
    public void initialize() {
        this.timer = new Timer();
        R.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                setSpreadingFactor();
            }
        });
        Z.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                setMortalityRate();
            }
        });
        disableButtons(true, true);
        world.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        individualMediator = new IndividualMediator(world);
    }

    /**
     * Init method clicked at the start.
     */
    @FXML
    public void init() {
        stop();
        timer.setTimer(0);
        texts();
    }

    /**
     * Starts the simulation.
     */
    @FXML
    public void start() {
        timer.start();
        this.disableButtons(true, false);
    }

    /**
     * Stops the simulation.
     */
    @FXML
    public void stop() {
        timer.stop();
        this.disableButtons(false, true);
    }

    /**
     * Getter method for mask value according to reading radio button from gui.
     * @return 0.2 if it is selected 1.0 otherwise.
     */
    @FXML
    public double getMask() {
        if ((boolean) this.mask.isSelected())
            return 0.2;
        else
            return 1.0;
    }

    /**
     * Getter method for speed of individual that will be added.
     * @return speed of the individual.
     */
    @FXML
    public double getSpeed() {
        return (double) this.speed.getValue();
    }
    /**
     * Getter method for social distance of individual that will be added.
     * @return social distance of the individual.
     */
    @FXML
    public double getSocialDistance() {
        return (double) this.socialDistance.getValue();
    }
    /**
     * Getter method for collision time of individual that will be added.
     * @return collision time of the individual
     */
    @FXML
    public double getCollisionTime() {
        return (double) this.collisionTime.getValue();
    }
    /**
     * Getter method for number of individual that will be added.
     * @return individual number.
     */
    @FXML
    public int getIndividualNumber() {
        return (int) this.individualNumber.getValue();
    }

    /**
     * When insert button clicked this method will insert the individuals.
     */
    @FXML
    public void insert() {
        double mask = getMask();
        double speed = getSpeed();
        double socialDistance = getSocialDistance();
        double collisionTime = getCollisionTime();
        int individualNumber = getIndividualNumber();
        this.individualMediator.addIndividual(individualNumber, world, mask, speed, socialDistance, collisionTime);
    }

    /**
     * Helper methods displays the texts on the screen.
     */
    private void texts() {
        timerText.setText("" + timer.getTimer());
        numberOfDeath.setText("" + Config.DEATH_NUMBER);
        caseNumber.setText("" + Config.ILL_NUMBER);
        hospitalize.setText("" + Config.HOSPITALIZE_NUMBER);
        totalIndividual.setText("" + Config.TOTAL);
        healthNumber.setText("" + Config.HEALTH_NUMBER);
    }

    /**
     * Used to disable buttons on gui.
     * @param start true if start button will be disabled false otherwise.
     * @param stop true if stop button will be disabled false otherwise.
     */
    public void disableButtons(boolean start, boolean stop) {
        startButton.setDisable(start);
        stopButton.setDisable(stop);
    }
}
