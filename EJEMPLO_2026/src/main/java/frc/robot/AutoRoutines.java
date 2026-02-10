package frc.robot;

import choreo.auto.AutoFactory;
import choreo.auto.AutoRoutine;
import choreo.auto.AutoTrajectory;
import edu.wpi.first.wpilibj2.command.Commands;

public class AutoRoutines {
    private final AutoFactory m_factory;

    public AutoRoutines(AutoFactory factory) {
        m_factory = factory;
    }

    public AutoRoutine InicioIzquierdaAnotarAgarrarAnotar() {
        final AutoRoutine routine = m_factory.newRoutine("Inicio Izquierda - Anotar Centro - Agarrar Pelotas - Anotar Centro");
        final AutoTrajectory HubAPelotas = routine.trajectory("HubAPelotas");
        final AutoTrajectory IzquieroAHub = routine.trajectory("IzquieroAHub");
        final AutoTrajectory PelotasAHub = routine.trajectory("PelotasAHub");

        routine.active().onTrue(
            IzquieroAHub.resetOdometry()
                .andThen(IzquieroAHub.cmd())
                .andThen(Commands.waitSeconds(1))
                //.andThen(disparar)
                .andThen(Commands.waitSeconds(4))
                //.andThen(dejar de disparar)
                .andThen(
                    Commands.parallel(
                        //intake.intakeIn(),
                        HubAPelotas.cmd()
                    )
                )
                .andThen(PelotasAHub.cmd())
                .andThen(Commands.waitSeconds(1))
                //.andThen(disparar)
                .andThen(Commands.waitSeconds(4))
                //.andThen(dejar de disparar)
                
        );
        return routine;
    }
}
