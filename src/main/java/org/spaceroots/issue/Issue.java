package org.spaceroots.issue;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class Issue {

    private static final Logger LOGGER = LoggerFactory.getLogger(Issue.class);

    public static void main(final String[] args) {
        try
        {
            System.out.println("starting program");
            final LoggerContext     context      = (LoggerContext) LoggerFactory.getILoggerFactory();
            final JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(context);
            context.reset();
            configurator.doConfigure(Path.of(args[0]).toFile());
            System.out.println("using configuration file: " + args[0]);
            System.out.println("stopping program");
        }
        catch (JoranException je)
        {
            System.err.println(je.getMessage());
        }
    }

}
