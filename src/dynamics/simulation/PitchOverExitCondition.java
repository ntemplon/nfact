/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamics.simulation;

import dynamics.AeroSystemState;

/**
 *
 * @author nathan
 */
public class PitchOverExitCondition implements ExitCondition<AeroSystemState> {

    private boolean threshholdReached = false;

    @Override
    public boolean isFinished(AeroSystemState state) {
        if (state.get(AeroSystemState.DynamicPressure) == null || state.get(AeroSystemState.Z_VEL) == null ||
                state.get(AeroSystemState.THRUST) == null) {
            return false;
        }
        
        if (state.get(AeroSystemState.DynamicPressure) > 1) {
            threshholdReached = true;
        }
        return threshholdReached && !(state.get(AeroSystemState.Z_VEL) > 0
            || state.get(AeroSystemState.THRUST) > 0);
    }

}
