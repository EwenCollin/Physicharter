package net.gamescode.ewen.physicharter.experiments;

import java.util.ArrayList;

public class MeterUtils {

    public static boolean isDecreasing(ArrayList<Double> Values, int numberOfDecreasingValues) {
        int valuesDecreasing = 0;
        for(int i = 0; i < numberOfDecreasingValues; i++) {
            if(Values.size() - 2 - i >= 0 && Values.get(Values.size() - 1 - i) > Values.get(Values.size() - 2 - i)) {
                valuesDecreasing++;
            }
        }
        if(valuesDecreasing >= numberOfDecreasingValues) {
            return true;
        }
        return false;
    }
    /*
    public static double integrationTrapeze(double elapsedTime, double toIntegrate, double lastValue) {
    }*/

}
