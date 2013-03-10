/*
 *    Copyright 2013 Juan Alberto López Cavallotti
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mule.modules;

import org.apache.solr.common.SolrInputDocument;
import org.hamcrest.Matcher;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.construct.Flow;
import org.mule.modules.pojos.WebPagePojo;
import org.mule.tck.junit4.FunctionalTestCase;

import java.util.HashMap;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * Basic tests that exercises the operations on this connector.
 *
 * @author Juan Alberto López Cavallotti
 */
public class SolrConnectorTest extends FunctionalTestCase {

    @BeforeClass
    public static void configureSystemProps() {
        System.setProperty("mule.verbose.exceptions", "true");
    }

    @Override
    protected String getConfigResources() {
        return "mule-config.xml";
    }

    @Test
    public void testFlow() throws Exception {
         runFlowAndAssertThatPayload("testQueryFlow", allOf(startsWith("[{"), endsWith("}]")));
    }

    @Test
    public void testIndexPojo() throws Exception {

        WebPagePojo pojo = new WebPagePojo("http://www.test.com", "This is a unit test test", "Pojo inserted by a unit test test");

        runFlowWithPayloadAndAssertThatPayload("testPostUpdate", pojo, is("0"));
        runFlowWithPayloadAndAssertThatPayload("testDeleteById", pojo.getId(), is("0"));
    }

    @Test
    public void testDeleteContent() throws Exception{

        String query = "url:\"www.test.com\"";
        runFlowWithPayloadAndAssertThatPayload("testDeleteUpdate", query, is("0"));
    }

    @Test
    public void testCreateDocument() throws Exception {

        //populate the test info
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("id", "http://www.test.com");
        data.put("title", "This is a unit test test");
        data.put("url", "http://www.test.com");
        data.put("content", "Pojo inserted by a unit test test");

        SolrInputDocument document = runFlowWithPayloadAndReturnPayload("testCreateDocument", data);

        //assert about the document created
        assertEquals(data.get("id"), document.getFieldValue("id"));
        assertEquals(data.get("title"), document.getFieldValue("title"));
        assertEquals(data.get("url"), document.getFieldValue("url"));
        assertEquals(data.get("content"), document.getFieldValue("content"));

    }


    /**
     * Run the flow specified by name and assert equality on the expected output
     *
     * @param flowName The name of the flow to run
     * @param expect   The expected output
     */
    protected <T> void runFlowAndExpect(String flowName, T expect) throws Exception {
        runFlowWithPayloadAndExpect(flowName, expect, null);
    }


    protected <T> T runFlowWithPayloadAndReturnPayload(String flowName, Object payload) throws Exception {
        Flow flow = lookupFlowConstruct(flowName);
        MuleEvent event = getTestEvent(payload);
        MuleEvent responseEvent = flow.process(event);
        return (T) responseEvent.getMessage().getPayload();
    }

    protected <T> void runFlowWithPayloadAndAssertThatPayload(String flowName, Object payload, Matcher<T> matcher) throws Exception {
        Flow flow = lookupFlowConstruct(flowName);
        MuleEvent event = getTestEvent(payload);
        MuleEvent responseEvent = flow.process(event);
        logger.info("Payload is: " + responseEvent.getMessage().getPayload());
        assertThat((T)responseEvent.getMessage().getPayload(), matcher);
    }

    protected <T> void runFlowAndAssertThatPayload(String flowName, Matcher<T> matcher) throws Exception {
        runFlowWithPayloadAndAssertThatPayload(flowName, null, matcher);
    }

    /**
     * Run the flow specified by name using the specified payload and assert
     * equality on the expected output
     *
     * @param flowName The name of the flow to run
     * @param expect   The expected output
     * @param payload  The payload of the input event
     */
    protected <T, U> void runFlowWithPayloadAndExpect(String flowName, T expect, U payload) throws Exception {
        Flow flow = lookupFlowConstruct(flowName);
        MuleEvent event = getTestEvent(payload);
        MuleEvent responseEvent = flow.process(event);

        assertEquals(expect, responseEvent.getMessage().getPayload());
    }

    /**
     * Retrieve a flow by name from the registry
     *
     * @param name Name of the flow to retrieve
     */
    protected Flow lookupFlowConstruct(String name) {
        return (Flow) muleContext.getRegistry().lookupFlowConstruct(name);
    }
}
