# condo-manager
# Description
Experimental project aiming to shape and automate the management of condominium properties.
# Domain model
https://drive.google.com/file/d/1mMVuYvXhrQM7_x-BCF56c7djFKVmBqT2/view?usp=sharing
# Tech. stack
kotlin, Spring / Spring Boot, maven, PostgreSQL, Keycloak, docker, kubernetes
# Architecture
Hexagonal architecture with kotlin and Spring.
https://en.wikipedia.org/wiki/Hexagonal_architecture_(software)

# Modules:
  ## condo-manager-core
  ### condo-manager-core-domain
    Objects with behaviour and self-validation, defining the domain model. 
    Written in pure kotlin without any external dependencies.
  ### condo-manager-core-use-case
    Simple interfaces with methods accepting and returning domain objects and without any external dependencies. 
    Might be concidered as first-class citizens of the system shaping the inbound operations.
  ### condo-manager-core-out-port
    An output port is again a simple interface that can be called by our use cases if they need 
    something from the outside (database access, for instance). 
    This interface is designed to fit the needs of the use cases, 
    but it’s implemented by an outside component called an output or “driven” adapter.
  ### condo-manager-core-application
    Implementations of the use case interfaces - ususaly Services. 
    Use cases have no dependecies on the outward components.
    So when it needs such we create and use internally an out port for that.
    It is very important to keep this module (and all the core related ones) decoupled from dependencies
    to a specific technology, framework, api, external library etc.
    All these are considered as "details" and our mission is to draw a solid boundary between policy, core application and details.
  
  ## condo-manager-adapter
  ### condo-manager-adapter-persistence
    Components which are implementations of the persistence related out ports.
    Here we maintain persistence specific configurations and Spring Data JPA automatic repositories internals.
  ### condo-manager-adapter-web
    Top level adapters represented by Spring MVC REST controllers.
  
  ## condo-manager-infrastructure
  ### condo-manager-infrastructure-exception
    Application specific exceptions thrown and mapped by all layers.
  ### condo-manager-infrastructure-spring-configuration
    Spring specific configurations like security, different aspects etc.
  
  ## condo-manager-spring-boot-app
    Spring Boot application packaging all application modules and dependencies.
  
  ## condo-manager-test
  ### condo-manager-integration-test
Real integration tests for the REST api. </br> 
All tests are run against a real environment and the docker image produced by the build: ``condo-manager:${image.tag}``. </br>
Tech stack: [Testcontainers](https://testcontainers.com/) with [REST Assured](https://rest-assured.io/).
  
# Build and run
``./mvnw test`` </br>
Compiles the source code and runs unit tests. </br>
</br>
``./mvnw package`` </br>
Packages Spring Boot executable jar in the ``condo-manager-spring-boot-app/target`` directory. </br>
Builds docker image in the local repository with tag: ``condo-manager:${image.tag}`` </br>
The Maven property ``image.tag`` default value is ``latest`` </br>
Building the docker image can be omitted with switching off the maven profile ``build-docker-image`` or via ``./mvnw package -DskipBuildDocker``. </br>
If any specific image version tag is needed, pass ``image.tag`` property: ``./mvnw package -Dimage.tag=DEV123``</br>
</br>
``./mvnw verify`` </br>
Runs integration tests against the build image.</br>
</br>
``./mvnw install`` </br>
Installs the maven artifacts in the local maven repo.</br>
</br>
``./mvnw install -DskipTests`` </br>
Skip unit and integration tests.</br>
</br>
``./mvnw install -DskipUTs`` </br>
Skip only unit tests.</br>
</br>
``./mvnw install -DskipITs`` </br>
Skip only integration tests.</br>

# CI
Continuous integration is introduced in [GitHub Actions](https://github.com/iliankm/condo-manager/actions) via the following workflows:
### [Build](https://github.com/iliankm/condo-manager/actions/workflows/build.yml) workflow
Multibranch workflow triggered automatically on each branch push or pull request. </br>
The code is being compiled, lint, tested (unit and integration tests), code coverage is reported in the PRs etc.

Docker image is being built and published to the [registry](https://github.com/iliankm?tab=packages&repo_name=condo-manager)
The image tag depends on the branch that was built:
- `dev` -> condo-manager:latest
- `release` -> condo-manager:0.1 (the released version)
- `some_feature_branch` -> condo-manager:some_feature_branch

There's special handling if it is a pull request for the `release` branch. 
In that case additional job is triggered for merging and tagging the release PR in the `main` branch.

### [Release](https://github.com/iliankm/condo-manager/actions/workflows/release.yml) workflow
Manually triggered workflow for preparing a release with a version that is required as an input. </br>
Only certain users enumerated in RELEASE_WORKFLOW_ALLOWED_USERS variable are allowed to trigger that workflow.
- `release` branch is reset onto `dev` HEAD
- pom versions are set according to the given input
- `release` branch is force pushed
- GitHub PR is created for merging `release` branch into `main`

After the workflow successfully finished, the Build workflow shall be triggered for the `release` branch PR. </br>
If all the checks passed, a special job in the Build workflow will merge the PR into `main` and tag the release.

### Deploy workflow
TODO

# Deploy
TODO