teststuffcentral
================

Test Stuff Central is in development.

# What is Test Stuff Central?

Are you working in a software engineering team as technical lead, quality manager or build manager, or do you have someone performing that role in your software engineering team?

You probably have a pretty mature approach to automated build and test of the software you are developing, and you may already have worked with the likes of Vagrant, Chef and Puppet to automate the creation of your environment as part of your test run, achieving true [black box](http://en.wikipedia.org/wiki/Black-box_testing) test practice at the unit, component and whole system level.

If so have you felt the need for many test environments, and wished that it was easier and cheaper to set them up and run them? Test Stuff Central focuses on that problem. We are developing open source components that will slot into a Chef or Puppet build to make it easier to automate a complete environment build, run tests, archive the run data such as test results and logs, and tear the environment down quickly and cheaply.

This means you can afford to create and simultaneously test many more environments, supporting concurrent testing of many different platform matrices, as well as tests at many different levels from individual components to systems comprising multiple running components on multiple machines.
