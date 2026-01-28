package frc.robot;

import choreo.auto.AutoFactory;
import choreo.auto.AutoRoutine;
import choreo.auto.AutoTrajectory;

public class AutoRoutines {
    private final AutoFactory m_factory;

    public AutoRoutines(AutoFactory factory) {
        m_factory = factory;
    }

    public AutoRoutine simplePathAuto() {
        final AutoRoutine routine = m_factory.newRoutine("Basico");
        final AutoTrajectory InicioAPieza = routine.trajectory("InicioAPieza");
        final AutoTrajectory PiezaAAnotar = routine.trajectory("PiezaAAnotar");

        routine.active().onTrue(
            InicioAPieza.resetOdometry()
                .andThen(InicioAPieza.cmd())
        );
        return routine;
    }
}
