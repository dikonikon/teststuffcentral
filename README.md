teststuffcentral
================

Test Stuff Central is in development.

# What is Test Stuff Central?

Are you working in a software engineering team as technical lead, quality manager or build manager, or do you have someone performing that role in your software engineering team?

Maybe you already have a pretty mature approach to automated build and test of the software you are developing, and you may already have worked with the likes of Vagrant to automate virtual machine creation, and Chef, Puppet or Apache ACE to automate the creation of your environment as part of your test run, achieving true [black box](http://en.wikipedia.org/wiki/Black-box_testing) test practice at the unit, component and whole system level.

If so have you felt the need for many test environments, and wished that it was easier and cheaper to set them up and run them? Test Stuff Central focuses on that problem. We are developing open source components that will slot into any automatically created environment to make it easier to automate a complete environment build, run tests, archive the run data such as test results and logs.

For example currently under development are components to:

* maintain a simple state model, and update that state model as components of the system become ready (that might be application code or an underlying process that is required such as a database server)
* trigger test runs based on events from that state model
* extract and publish endpoint details into a local dns server to facilitate connectivity between client and server modules in the system
* stream logs from many parts of the system to a central file system using websockets
* package and archive test results and test evidence from multiple test runs