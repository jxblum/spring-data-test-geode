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
package org.springframework.data.gemfire.tests.testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.apache.geode.cache.client.ClientCache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;
import org.springframework.data.gemfire.mapping.annotation.Region;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import org.springframework.data.gemfire.tests.integration.IntegrationTestsSupport;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Integration Tests using Spring (Data) for Apache Geode to bootstrap and configure an Apache Geode {@link ClientCache}
 * connected to an Apache Geode cluster run from Docker Image using Testcontainers.
 *
 * @author John Blum
 * @see org.junit.Test
 * @see org.apache.geode.cache.client.ClientCache
 * @see org.springframework.data.gemfire.config.annotation.ClientCacheApplication
 * @see org.springframework.data.gemfire.tests.integration.IntegrationTestsSupport
 * @see org.springframework.test.context.ContextConfiguration
 * @see org.springframework.test.context.junit4.SpringRunner
 * @since 0.0.23
 */
@RunWith(SpringRunner.class)
@ContextConfiguration
@SuppressWarnings("unused")
public class ClientServerWithTestcontainersIntegrationTests extends IntegrationTestsSupport {

	@Autowired
	private UserRepository userRepository;

	@BeforeClass
	public static void startGeodeClusterWithTestcontainers() {
	}

	@Test
	public void saveAndFindUserWorks() {

		User jonDoe = User.as("jonDoe");

		this.userRepository.save(jonDoe);

		User jonDoeFoundById = this.userRepository.findById(jonDoe.getName()).orElse(null);

		assertThat(jonDoeFoundById).isNotNull();
		assertThat(jonDoeFoundById).isEqualTo(jonDoe);
		assertThat(jonDoeFoundById).isNotSameAs(jonDoe);
	}

	@ClientCacheApplication(name = "ClientServerWithTestcontainersIntegrationTests")
	@EnableEntityDefinedRegions(basePackageClasses = User.class)
	@EnableGemfireRepositories(basePackageClasses = UserRepository.class)
	static class TestGeodeClientConfiguration { }

	@Getter
	@ToString
	@Region("Users")
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "as")
	static class User {

		@lombok.NonNull @Id
		private final String name;

	}

	interface UserRepository extends CrudRepository<User, String> { }

}
