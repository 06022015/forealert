package com.forealert.web.provider;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jersey2.InstrumentedResourceMethodApplicationListener;
import com.forealert.web.service.MessageService;
import com.forealert.web.service.UserService;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ext.Provider;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/25/17
 * Time: 1:10 PM
 * To change this template use File | Settings | File Templates.
 */
/*@Provider*/
public class FAMetricRegistry extends ResourceConfig {

    private static final Logger logger = LoggerFactory.getLogger(FAMetricRegistry.class);
    //private Set<Class<?>> classes = new HashSet<Class<?>>();

    public FAMetricRegistry() {
        /*register(new InstrumentedResourceMethodApplicationListener(new MetricRegistry()));*/
        initializeApplication();
        register(MessageService.class);
        register(UserService.class);
    }


    private void initializeApplication() {
        registerListeners(); // Register listeners
    }
    private void registerListeners() {
        final MetricRegistry metricRegistry = new MetricRegistry();
        register(new InstrumentedResourceMethodApplicationListener(metricRegistry));
        ConsoleReporter.forRegistry(metricRegistry)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build()
                .start(1, TimeUnit.MINUTES);
        logger.info("Console reporter is enabled successfully!");
    }
}
