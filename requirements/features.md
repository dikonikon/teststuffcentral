
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

- need to be able to define an environment (Chef or Puppet)
- specify configuration of either individual or classes of machine in the environment (Chef of Puppet)
- deploy builds into the distributed environment (Chef or Puppet)

# Ideas

- DSL describes the components of the system and what logical machines they are on
- Relationships between components carry the information needed to resolve connections between them
- Deploying the DSL-based model results in deployment of the components to machines
- It also populates a central state model
- When components are deployed agents are deployed with them that determine when they are ready and signal the state model
- When all components are ready the test run/s are triggered

# Startup Sequence

1. user describes two environments using Chef - the test-target and the test-runner environments
1. initiates run using tsctool
1. tsctool connects to tsc-orch-server (physical server)
1. tsc-orch-server creates tsc-testhub-server as a VM and returns the address of tsc-testhub-server to tsctool
1. tsctool loads Chef environments into tsc-testhub-server
1. tsctool loads VagrantFile onto tsc-testhub-server
1. tsctool tells tsc-orc-server that all data is uploaded
1. tsc-orch-server reads environment data from tsc-testhub-server and modifies machine configs to include nagios clients, log streamer clients, DNS publishers and test runners as appropriate
1. It also loads a list of objects whose status is significant into the tsc-testhub-server
1. tsc-orch-server tells tsc-testhub-server that it has finished modifying the Chef environment
1. tsc-testhub-server creates and starts the environments VMs using Vagrant
1. Each VM downloads its Chef config from tsc-testhub-server and configures itself, downloading software and data either from the internet, or from a proxied nexus server supplied by the user
1. The nagios client informs the nagios server on the tsc-testhub-server of the status of the servers
1. The tsc-testhub-server inspects its list of significant objects - when they are all ready it sends a start test event to each of the test runners in the test-runner environment
1. The test runners run their tests - when they have finished they send a message to the tsc-testhub-server
1. When all test runners have reported completion the tsc-testhub-server copies all transmitted test data and results to an archive in the cloud specified by the user
1. The tsc-testhub-server stops and destroys all of the VMs in the test-target and test-runner environments, then signals the tsc-orch-server
1. tsc-orch-server stops and destroys the tsc-testhub-server
1. finally tsc-orc-server signals the tsctool that the test is complete


