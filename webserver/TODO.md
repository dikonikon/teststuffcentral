
TODO
====

# simple web page

    - upload yaml

    - sends request to deploy/undeploy tomcat

    - shows checks status of deployment/undeployment

# server

1. stores yaml in workspace

1. Receives deploy request over websocket
    - RequestHandlerActor actor sends request to WorkActor to do deployment
    - WorkActor calls juju
    - When finished WorkActor sends message to RequestHandlerActor
    - RequestHandlerActor sends message to web page over WebSocket

