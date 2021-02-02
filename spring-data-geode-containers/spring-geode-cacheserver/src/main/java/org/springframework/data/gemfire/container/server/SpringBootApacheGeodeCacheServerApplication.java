/*
 *  Copyright 2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 *  or implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 */
package org.springframework.data.gemfire.container.server;

import java.io.File;
import java.io.IOException;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.gemfire.config.annotation.CacheServerApplication;
import org.springframework.data.gemfire.config.annotation.EnableHttpService;
import org.springframework.data.gemfire.config.annotation.EnableLocator;
import org.springframework.data.gemfire.config.annotation.EnableManager;
import org.springframework.geode.util.GeodeConstants;

/**
 * {@link SpringBootApplication} used to bootstrap and configure an Apache Geode {@link CacheServerApplication}.
 *
 * @author John Blum
 * @see org.springframework.boot.autoconfigure.SpringBootApplication
 * @see org.springframework.boot.builder.SpringApplicationBuilder
 * @see org.springframework.data.gemfire.config.annotation.CacheServerApplication
 * @since 0.0.23
 */
@SpringBootApplication
@CacheServerApplication(name = "DockerizedSpringBootApacheGeodeCacheServerApplication")
public class SpringBootApacheGeodeCacheServerApplication {

	private static final String GEODE_HOME_PROPERTY = GeodeConstants.GEMFIRE_PROPERTY_PREFIX + "home";

	public static void main(String[] args) throws IOException {

		resolveAndConfigureGeodeHome();

		new SpringApplicationBuilder(SpringBootApacheGeodeCacheServerApplication.class)
			.web(WebApplicationType.NONE)
			.build()
			.run(args);
	}

	private static void resolveAndConfigureGeodeHome() throws IOException {

		ClassPathResource geodeHomeResource = new ClassPathResource("/geode-home");

		if (geodeHomeResource.exists()) {

			File geodeHomeFile = geodeHomeResource.getFile();

			System.setProperty(GEODE_HOME_PROPERTY, geodeHomeFile.getAbsolutePath());
		}
	}

	@Configuration
	@EnableHttpService
	@EnableLocator
	@EnableManager(start = true)
	@Profile("locator-manager")
	@SuppressWarnings("unused")
	static class LocatorManagerConfiguration { }

}
