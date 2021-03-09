package com.Ebook.catalogue;

import com.Ebook.catalogue.resources.CurdOperations;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class artifactsApplication extends Application<artifactsConfiguration> {

    
    public static void main(final String[] args) throws Exception {
        new artifactsApplication().run(args);
    }
//
//    @Override
//    public String getName() {
//        return "artifacts";
//    }

    @Override
    public void initialize(final Bootstrap<artifactsConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final artifactsConfiguration configuration,
                    final Environment environment) {
    	environment.jersey().register(new CurdOperations());
    	
    	AllowCORS.insecure(environment);
    }

}
