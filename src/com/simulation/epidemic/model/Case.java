package com.simulation.epidemic.model;

import javafx.scene.paint.Color;

/**
 * Case enum type that represents the cases of the individuals
 */
public enum Case {
    HEALTHY {
        public Color getColor() {
            return Color.YELLOW;
        }
    },
    ILL {
        public Color getColor() {
            return Color.RED;
        }
    },
    DEATH {
        public Color getColor() {
            return Color.WHITE;
        }
    },
    HOSPITALIZE {
        public Color getColor() {
            return Color.WHITE;
        }
    };

    public abstract Color getColor();
}
