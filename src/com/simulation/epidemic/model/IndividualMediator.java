package com.simulation.epidemic.model;

import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class IndividualMediator {
    private ArrayList<Individual> individuals;

    public IndividualMediator(Pane world) {
        this.individuals = new ArrayList<Individual>();
        Individual individual = new Individual.IndividualBuilder(Case.ILL, world).buildFirstInfected();
        individuals.add(individual);
        Config.ILL_NUMBER = 1;
        Config.TOTAL = this.individuals.size();
        Config.HOSPITALIZE_NUMBER = 0;
        Config.DEATH_NUMBER = 0;
        Config.HEALTH_NUMBER = 0;
        Config.VENTILATOR = 0;
    }

    public ArrayList<Individual> getIndividual() {
        return this.individuals;
    }

    public void addIndividual(int number, Pane world, double mask, double speed, double socialDistance, double collisionTime) {
        for (int i = 0; i <number ; i++) {
            Individual individual = new Individual.IndividualBuilder(Case.HEALTHY, world).mask(mask).speed(speed).distance(socialDistance).collisionTime(collisionTime).buildIndividual();
            this.individuals.add(individual);
            Config.TOTAL = this.individuals.size();
            Config.HEALTH_NUMBER++;
        }
    }

    public void move() {
        for (Individual p : this.individuals) {
            if (p.getWait() == 0)
                p.move();
            else
                p.setWait(p.getWait()-1);
        }
    }

    public void draw() {
        int count = 0;
        for (Individual p : this.individuals) {
            if (p.getState() == Case.HEALTHY) {
                p.draw();
            }
            else if (p.getState() == Case.HOSPITALIZE) {
                if (p.getHospitalTime() >= 10) {
                    p.setState(Case.HEALTHY);
                    p.setIllTime(0);
                    p.setHospitalTime(0);
                    p.setWait(0);
                    Config.VENTILATOR--;
                    Config.HEALTH_NUMBER++;
                    Config.HOSPITALIZE_NUMBER--;
                    Config.ILL_NUMBER--;
                }
                else
                    p.setHospitalTime(p.getHospitalTime() + 1);
            }
            else if (p.getState() == Case.ILL) {
                if (p.getIllTime() >= (100.0 * (1.0 - Config.Z))) {
                    p.setState(Case.DEATH);
                    p.setWait(-1);
                    Config.DEATH_NUMBER++;
                    Config.ILL_NUMBER--;
                }
                else if (p.getIllTime() >= 25){
                    if (Config.VENTILATOR < this.individuals.size() / 100) {
                        p.setState(Case.HOSPITALIZE);
                        p.setIllTime(0);
                        p.setHospitalTime(0);
                        Config.HOSPITALIZE_NUMBER++;
                        Config.VENTILATOR++;
                    }
                    else
                        p.setIllTime(p.getIllTime() + 1);
                }
                else {
                    p.draw();
                    p.setIllTime(p.getIllTime() + 1);
                }
            }
            else {

            }
        }
    }

    public void resolveCollisions() {
        for (Individual p: this.individuals) {
            for (Individual q: this.individuals) {
                if (p != q) {
                    p.collisionCheck(q);
                }
            }
        }
    }
}
