/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamics.simulation;

import dynamics.SystemState;

/**
 *
 * @author nathan
 * @param <TState>
 */
public interface SimulationRecorder<TState extends SystemState> {
    
    void start(TState initialState);
    void recordState(TState state);
    void finish(TState finalState);
    
}
