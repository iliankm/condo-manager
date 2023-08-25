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
  
# Build and run
``./mvnw test`` </br>
Compiles the source code and runs unit tests. </br>
</br>
``./mvnw package`` </br>
Packages Spring Boot executable jar in the ``condo-manager-spring-boot-app/target`` directory. </br>
Builds docker image in the local repository with tag: ``condo-manager:${project.version}`` </br>
If the image has tag to be versioned with ``latest``, enable maven profile: ``local-dev`` </br>
The build of the docker image can be omitted with switching off the maven profile ``build-docker-image`` or via ``./mvnw package -DskipBuildDocker``. </br>
If any specific image version tag is needed, pass ``image.tag`` property: ``./mvnw package -Dimage.tag=DEV123``</br>
</br>
``./mvnw verify`` </br>
TODO: Runs integration tests against the build image.</br>
</br>
``./mvnw install`` </br>
Installs the maven artifacts in the local maven repo.</br>

# CI
# Deploy

