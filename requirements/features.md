
log aggregation:
	detect out of sequence messages and report an error
sequential test execution in a distributed environment
environment creation
deployment of many types of property
aggregation of test results -> one test script per environment?
relation of test results to logs and other evidence

# Requirements

1. need to be able to determine when the entire system has reached a consistent state, in order to know when it is safe to start a test run
1. need to create and provision the system: Vagrant/Chef? Are Vagrant/Chef friendly enough for the average dev team?

- need to be able to define an environment (Chef)
- specify configuration of either individual or classes of machine in the environment
- deploy builds into the distributed environment

# Ideas

- DSL describes the components of the system and what logical machines they are on
- Relationships between components carry the information needed to resolve connections between them
- Deploying the DSL-based model results in deployment of the components to machines
- It also populates a central state model
- When components are deployed agents are deployed with them that determine when they are ready and signal the state model
- When all components are ready the test run/s are triggered




