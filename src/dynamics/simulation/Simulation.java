/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamics.simulation;

import dynamics.DynamicSystem;
import dynamics.SystemState;

/**
 *
 * @author nathan
 * @param <TSystem>
 * @param <TState>
 */
public class Simulation<TSystem extends DynamicSystem<TState>, TState extends SystemState> implements Runnable {
    
    // Fields
    private final DynamicSystem<TState> system;
    private final ExitCondition<TState> exit;
    private final SimulationRecorder<TState> recorder;
    private final double deltaT;
    
    
    // Initialization
    public Simulation(TSystem system, ExitCondition<TState> exit, double timeIncrement) {
        this.system = system;
        this.deltaT = timeIncrement;
        this.exit = exit;
        
        this.recorder = null;
    }
    
    public Simulation(TSystem system, ExitCondition<TState> exit, SimulationRecorder<TState> recorder,
            double timeIncrement) {
        this.system = system;
        this.exit = exit;
        this.recorder = recorder;
        this.deltaT = timeIncrement;
    }
    
    
    // Public Methods
    @Override
    public void run() {
        TState state = this.system.getState();
        
        this.recorder.start(state);
        
        while(!this.exit.isFinished(state)) {
            this.system.updateState(deltaT);
            state = this.system.getState();
            this.recorder.recordState(state);
        }
        
        state = this.system.getState();
        this.recorder.finish(state);
    }
    
}
