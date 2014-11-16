/*
 * The MIT License
 *
 * Copyright 2014 Nathan Templon.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package tester;

import aero.fluid.FluidState;
import aero.fluid.IdealGas;
import aero.fluid.IdealGasState;
import dynamics.AeroSystemState;
import dynamics.airplane.RocketPlane;
import dynamics.airplane.RocketPlaneParameters;
import dynamics.simulation.ExitCondition;
import dynamics.simulation.PitchOverExitCondition;
import dynamics.simulation.PitchOverRecorder;
import dynamics.simulation.Simulation;
import geometry.angle.Angle;
import geometry.angle.Angle.AngleType;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;
import propulsion.rocket.HobbyRocketEngine;
import util.PhysicalConstants;

/**
 *
 * @author nathant
 */
public class TestForm extends javax.swing.JFrame {

    /**
     * Creates new form TestForm
     */
    public TestForm() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        contentPanel = new javax.swing.JPanel();
        simulateButton = new javax.swing.JButton();
        enginePanel = new javax.swing.JPanel();
        rocketEngineLabel = new javax.swing.JLabel();
        engineTextField = new javax.swing.JTextField();
        fillerPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("nFACT");
        setMinimumSize(new java.awt.Dimension(500, 300));

        contentPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3));
        contentPanel.setLayout(new java.awt.GridBagLayout());

        simulateButton.setText("Run Simulation");
        simulateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simulateButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        contentPanel.add(simulateButton, gridBagConstraints);

        enginePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Engine Selection"));
        enginePanel.setLayout(new java.awt.GridBagLayout());

        rocketEngineLabel.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        rocketEngineLabel.setText("Rocket Engine");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        enginePanel.add(rocketEngineLabel, gridBagConstraints);

        engineTextField.setText("G-78");
        engineTextField.setToolTipText("");
        engineTextField.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        enginePanel.add(engineTextField, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        enginePanel.add(fillerPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.2;
        contentPanel.add(enginePanel, gridBagConstraints);

        getContentPane().add(contentPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void simulateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simulateButtonActionPerformed
        ExitCondition<AeroSystemState> exit;

        RocketPlaneParameters prms = new RocketPlaneParameters();
        prms.setAspectRatio(6.3);
        prms.setCBar(0.5291);
        prms.setCd0(0.02);
        prms.setClAlpha(4.5891);
        prms.setClDeltaE(0.0);
        prms.setClQ(0.0);
        prms.setCpm0(-0.02);
        prms.setCpmAlpha(-0.373);
        prms.setCpmQ(-12.6);
        prms.setIyy(0.2 * (3 * 3 + 0.33 * 0.33) * (2.8 / PhysicalConstants.GRAVITY_ACCELERATION));
        prms.setMass(2.8 / PhysicalConstants.GRAVITY_ACCELERATION);
        prms.setRocketEngine(HobbyRocketEngine.G25);
        prms.setSRef(2.4);
        prms.setSpanEfficiency(0.87);
        prms.setZThrust(0.0);
        AeroSystemState initialState = new AeroSystemState();
        initialState.set(AeroSystemState.ANGULAR_POS, new Angle(89.5, Angle.AngleType.DEGREES));
        initialState.set(AeroSystemState.ANGULAR_VEL, 0.0);
        initialState.set(AeroSystemState.X_POS, 0.0);
        initialState.set(AeroSystemState.X_VEL, 0.0);
        initialState.set(AeroSystemState.Z_POS, 0.0);
        initialState.set(AeroSystemState.Z_VEL, 0.1);
        FluidState fluid = new IdealGasState(new IdealGas(28.97, 1.4), 95.0 + 459.0, 2018.68); // Air on a hot day
        initialState.set(AeroSystemState.FLUID_STATE, fluid);

        prms.setInitialState(initialState);
        RocketPlane system;

        String outputFolder = "/home/nathan/NFACalc/";
        DecimalFormat format = new DecimalFormat("0.0000");
        StringBuilder sb = new StringBuilder();
        sb.append("CPM0 Variation (CPM_Alpha = -0.373, Thrust = +0%):").append(System.lineSeparator());

        // CPM0 sweeps
        for (double cm0 = 0.0; cm0 > -0.03; cm0 -= 0.001) {
            prms.setCpm0(cm0);
            initialState = new AeroSystemState();
            initialState.set(AeroSystemState.ANGULAR_POS, new Angle(89.5, Angle.AngleType.DEGREES));
            initialState.set(AeroSystemState.ANGULAR_VEL, 0.0);
            initialState.set(AeroSystemState.X_POS, 0.0);
            initialState.set(AeroSystemState.X_VEL, 0.0);
            initialState.set(AeroSystemState.Z_POS, 0.0);
            initialState.set(AeroSystemState.Z_VEL, 0.1);
            fluid = new IdealGasState(new IdealGas(28.97, 1.4), 95.0 + 459.0, 2018.68); // Air on a hot day
            initialState.set(AeroSystemState.FLUID_STATE, fluid);

            prms.setInitialState(initialState);
            system = new RocketPlane(prms);

            File file = new File(outputFolder + "simulation-cpm0_" + format.format(cm0) + ".csv");
            PitchOverRecorder recorder = new PitchOverRecorder(file, 100);
            exit = new PitchOverExitCondition();

            Simulation sim = new Simulation(system, exit, recorder, 0.0005);
            sim.run();

            sb.append("\tCase CPM0 = ").append(format.format(cm0)).append(":").append(System.lineSeparator());
            sb.append("\t\tMax Speed: ").append(format.format(recorder.getMaxSpeed())).append(" ft/s").append(System.lineSeparator());
            sb.append("\t\tBurnout before Pitch Over: ").append(recorder.getSimulationTime() > 5.4).append(System.lineSeparator());
            sb.append("\t\tFinal Speed: ").append(format.format(recorder.getFinalVelocity())).append(" ft/s").append(System.lineSeparator());
            sb.append("\t\tFinal Theta: ").append(format.format(recorder.getFinalTheta().getMeasure(AngleType.DEGREES)))
                    .append(" degrees").append(System.lineSeparator());
            sb.append("\t\tMax Altitude: ").append(format.format(recorder.getMaxAltitude())).append(" ft").append(System.lineSeparator());
            sb.append("\t\tCritical Normal Load Factor: ").append(format.format(recorder.getMinNormalLoadFactor())).append(System.lineSeparator());
            sb.append("\t\tMax Axial Load Factor: ").append(format.format(recorder.getMaxAxialLoadFactor())).append(System.lineSeparator());
        }
        prms.setCpm0(-0.011);
        
        // CPMAlpha Sweep
        sb.append(System.lineSeparator());
        sb.append("CPM_Alpha Variation (CPM0 = -0.011, Thrust = +0%):").append(System.lineSeparator());
        for (double cmA = -0.18; cmA > -0.55; cmA -= 0.02) {
            prms.setCpmAlpha(cmA);
            initialState = new AeroSystemState();
            initialState.set(AeroSystemState.ANGULAR_POS, new Angle(89.5, Angle.AngleType.DEGREES));
            initialState.set(AeroSystemState.ANGULAR_VEL, 0.0);
            initialState.set(AeroSystemState.X_POS, 0.0);
            initialState.set(AeroSystemState.X_VEL, 0.0);
            initialState.set(AeroSystemState.Z_POS, 0.0);
            initialState.set(AeroSystemState.Z_VEL, 0.1);
            fluid = new IdealGasState(new IdealGas(28.97, 1.4), 95.0 + 459.0, 2018.68); // Air on a hot day
            initialState.set(AeroSystemState.FLUID_STATE, fluid);

            prms.setInitialState(initialState);
            system = new RocketPlane(prms);

            File file = new File(outputFolder + "simulation-cpmAlpha_" + format.format(cmA) + ".csv");
            PitchOverRecorder recorder = new PitchOverRecorder(file, 100);
            exit = new PitchOverExitCondition();

            Simulation sim = new Simulation(system, exit, recorder, 0.0005);
            sim.run();

            sb.append("\tCase CPM_Alpha = ").append(format.format(cmA)).append(":").append(System.lineSeparator());
            sb.append("\t\tMax Speed: ").append(format.format(recorder.getMaxSpeed())).append(" ft/s").append(System.lineSeparator());
            sb.append("\t\tBurnout before Pitch Over: ").append(recorder.getSimulationTime() > 5.4).append(System.lineSeparator());
            sb.append("\t\tFinal Speed: ").append(format.format(recorder.getFinalVelocity())).append(" ft/s").append(System.lineSeparator());
            sb.append("\t\tFinal Theta: ").append(format.format(recorder.getFinalTheta().getMeasure(AngleType.DEGREES)))
                    .append(" degrees").append(System.lineSeparator());
            sb.append("\t\tMax Altitude: ").append(format.format(recorder.getMaxAltitude())).append(" ft").append(System.lineSeparator());
            sb.append("\t\tCritical Normal Load Factor: ").append(format.format(recorder.getMinNormalLoadFactor())).append(System.lineSeparator());
            sb.append("\t\tMax Axial Load Factor: ").append(format.format(recorder.getMaxAxialLoadFactor())).append(System.lineSeparator());
        }
        prms.setCpmAlpha(-0.373);
        
        // Engine Sweeps
        sb.append(System.lineSeparator());
        sb.append("Thrust Variation (CPM0 = -0.011, CPM_Alpha = -0.373):").append(System.lineSeparator());
        for (int modPercent = -20; modPercent <= 20; modPercent += 4) {
            double variation = 1 + ((double)modPercent) / 100.0;
            HobbyRocketEngine newEngine = HobbyRocketEngine.G25.getThrustVariationEngine(variation);
            prms.setRocketEngine(newEngine);
            
            initialState = new AeroSystemState();
            initialState.set(AeroSystemState.ANGULAR_POS, new Angle(89.5, Angle.AngleType.DEGREES));
            initialState.set(AeroSystemState.ANGULAR_VEL, 0.0);
            initialState.set(AeroSystemState.X_POS, 0.0);
            initialState.set(AeroSystemState.X_VEL, 0.0);
            initialState.set(AeroSystemState.Z_POS, 0.0);
            initialState.set(AeroSystemState.Z_VEL, 0.1);
            fluid = new IdealGasState(new IdealGas(28.97, 1.4), 95.0 + 459.0, 2018.68); // Air on a hot day
            initialState.set(AeroSystemState.FLUID_STATE, fluid);

            prms.setInitialState(initialState);
            system = new RocketPlane(prms);

            File file = new File(outputFolder + "simulation-thrust_" + modPercent + ".csv");
            PitchOverRecorder recorder = new PitchOverRecorder(file, 100);
            exit = new PitchOverExitCondition();

            Simulation sim = new Simulation(system, exit, recorder, 0.0005);
            sim.run();

            sb.append("\tCase Thrust = ").append(modPercent + 100).append("%:").append(System.lineSeparator());
            sb.append("\t\tMax Speed: ").append(format.format(recorder.getMaxSpeed())).append(" ft/s").append(System.lineSeparator());
            sb.append("\t\tBurnout before Pitch Over: ").append(recorder.getSimulationTime() > 5.4).append(System.lineSeparator());
            sb.append("\t\tFinal Speed: ").append(format.format(recorder.getFinalVelocity())).append(" ft/s").append(System.lineSeparator());
            sb.append("\t\tFinal Theta: ").append(format.format(recorder.getFinalTheta().getMeasure(AngleType.DEGREES)))
                    .append(" degrees").append(System.lineSeparator());
            sb.append("\t\tMax Altitude: ").append(format.format(recorder.getMaxAltitude())).append(" ft").append(System.lineSeparator());
            sb.append("\t\tCritical Normal Load Factor: ").append(format.format(recorder.getMinNormalLoadFactor())).append(System.lineSeparator());
            sb.append("\t\tMax Axial Load Factor: ").append(format.format(recorder.getMaxAxialLoadFactor())).append(System.lineSeparator());
        }
        prms.setRocketEngine(HobbyRocketEngine.G25);

        File summary = new File(outputFolder + "summary.txt");
        try (FileWriter fw = new FileWriter(summary)) {
            try (PrintWriter pw = new PrintWriter(fw)) {
                pw.println(sb.toString());
            }
        } catch (IOException ex) {

        }

        JOptionPane.showMessageDialog(this, "Simulation Complete!");
    }//GEN-LAST:event_simulateButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TestForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TestForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TestForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TestForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new TestForm().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel contentPanel;
    private javax.swing.JPanel enginePanel;
    private javax.swing.JTextField engineTextField;
    private javax.swing.JPanel fillerPanel;
    private javax.swing.JLabel rocketEngineLabel;
    private javax.swing.JButton simulateButton;
    // End of variables declaration//GEN-END:variables
}
