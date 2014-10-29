/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamics;

/**
 *
 * @author nathant
 * @param <TState>
 */
public interface DynamicSystem<TState extends SystemState> {

    // Properties
    TState getState();


    // Public Methods
    void updateState(double detlaT);

}
