package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.AutoOptions.AutonomousIntOption;
import org.firstinspires.ftc.teamcode.AutoOptions.AutonomousOptions;
import org.firstinspires.ftc.teamcode.AutoOptions.AutonomousTextOption;

public class BioBuzzAutoRobot extends BioBuzzRobot {

    AutonomousOptions autoOptions = new AutonomousOptions();
    AutonomousTextOption allianceColor = autoOptions.addTextOption("Alliance", "red", new String[]{"red", "blue"});
    AutonomousTextOption startPosition = autoOptions.addTextOption("StartPostion", "uptilt", new String[]{"uptilt", "downtilt", "sidewall"});

    AutonomousIntOption startDelay = autoOptions.addIntOption("Start Delay", 0, 0, 20);

    //TODO Add the rest of autonomous options(ball collect)




}
