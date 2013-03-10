Mule Solr Connector
===================

Welcome to the Mule Solr Connector project, this project aims to add integration of [Apache Solr](http://lucene.apache.org/solr/) and [Mule ESB](http://www.mulesoft.org). In order to do this, this project takes advantage of solr's SolrJ library which has a great deal of flexibilty.

Plugin Documentation
--------------------

Please check out the project pages for documentation, install instructions and Javadoc: http://juancavallotti.github.com/mule-module-solr/

Building from the Source
------------------------

In order to use this project, a not-so-difficult way is to build it and integrate it into MuleStudio, this can be done by just cloning the source and typing:

```bash
$ mvn clean package -DskipTests -Ddevkit.studio.package.skip=false
```

This will generate a Eclipse software update site from which you can install the extension, the full procedure is described on the following link:
[Devkit: your first cloud connector](http://www.mulesoft.org/documentation/display/current/Your+First+Cloud+Connector)

In order to run the tests you'd need a running Solr core with and a schema with at least the following fields:

  - id
  - content
  - url
  - title

The actual schema used to build the tests is the one provided by the webcrawler project [Apache Nutch](http://nutch.apache.org/).

Using it on Maven Mule Projects
-------------------------------

Please take a look at the following [link](http://juancavallotti.github.com/mule-module-solr/guide/install.html) for [install instructions](http://juancavallotti.github.com/mule-module-solr/guide/install.html), if you have compiled the module, then you can *skip* the definition of repositories.


Example Usage
-------------

Here I present a sample application which can be used to index web pages (as Apache Nutch does) or query for them by content and returning the result in json format:

```xml
<?xml version="1.0" encoding="UTF-8"?>

<mule xmlns:scripting="http://www.mulesoft.org/schema/mule/scripting" xmlns:json="http://www.mulesoft.org/schema/mule/json" xmlns:http="http://www.mulesoft.org/schema/mule/http" xmlns:solr="http://www.mulesoft.org/schema/mule/solr" xmlns="http://www.mulesoft.org/schema/mule/core" xmlns:doc="http://www.mulesoft.org/schema/mule/documentation" xmlns:spring="http://www.springframework.org/schema/beans" version="CE-3.3.1" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="
http://www.mulesoft.org/schema/mule/json http://www.mulesoft.org/schema/mule/json/current/mule-json.xsd 
http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd 
http://www.mulesoft.org/schema/mule/scripting http://www.mulesoft.org/schema/mule/scripting/current/mule-scripting.xsd 
http://www.mulesoft.org/schema/mule/solr http://www.mulesoft.org/schema/mule/solr/1.0.0/mule-solr.xsd 
http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd 
http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd ">
    
    <!-- configure the solr connector -->
    <solr:config name="Solr" serverUrl="http://localhost:8080/solr" doc:name="Solr"/>
    
    <flow name="queryFlow" doc:name="queryFlow">
        <http:inbound-endpoint exchange-pattern="request-response" address="http://localhost:8081/search" doc:name="HTTP"/>
        
        <!-- query solr -->
        <solr:query config-ref="Solr" q="content:#[header:inbound:query]" doc:name="Solr" highlightField="content"/>
		
        <!-- return results -->
		<set-payload value="#[groovy: payload.getResults()]" />
        <json:object-to-json-transformer doc:name="Object to JSON"/>
        <set-property propertyName="Content-Type" value="application/json" doc:name="Property" />
    </flow>
    <flow name="indexFlow" doc:name="indexFlow">
    	<http:inbound-endpoint address="http://localhost:8081/index" doc:name="HTTP" exchange-pattern="request-response"/>
    	<http:body-to-parameter-map-transformer doc:name="Body to Parameter Map"/>
    	
    	<!-- convert the response values to a solr document -->
    	<solr:message-to-input-document-transformer doc:name="Convert Map to InputDocument"/>
    	
    	<!-- index the given document -->
    	<solr:index config-ref="Solr" doc:name="Index document in Solr"/>
    	
    	<!-- return a status code to the caller -->
    	<set-payload value="#[groovy: [success:true]]" doc:name="Set Payload"/>
    	<json:object-to-json-transformer doc:name="Object to JSON"/>
    	<set-property propertyName="Content-Type" value="application/json" doc:name="Property" />
    </flow>
</mule>
```
