/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.test.metadata.extension;

import static java.lang.Thread.currentThread;

import org.mule.runtime.core.api.util.IOUtils;
import org.mule.runtime.extension.api.annotation.metadata.OutputResolver;
import org.mule.runtime.extension.api.annotation.metadata.TypeResolver;
import org.mule.runtime.extension.api.annotation.metadata.fixed.InputJsonType;
import org.mule.runtime.extension.api.annotation.metadata.fixed.InputXmlType;
import org.mule.runtime.extension.api.annotation.metadata.fixed.OutputJsonType;
import org.mule.runtime.extension.api.annotation.metadata.fixed.OutputXmlType;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.test.metadata.extension.resolver.CsvInputStaticTypeResolver;
import org.mule.test.metadata.extension.resolver.JavaOutputStaticTypeResolver;
import org.mule.test.metadata.extension.resolver.JsonInputStaticTypeResolver;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

public class CustomStaticMetadataOperations {

  private static final ClassLoader cl = currentThread().getContextClassLoader();
  public static final String CSV_VALUE = "Name,LastName\\njuan,desimoni\\nesteban,wasinger";
  public static final String XML_VALUE = IOUtils.toString(cl.getResourceAsStream("order.xml"));
  public static final String JSON_VALUE = "{\"age\":12,\"dni\": 1478231}";

  @OutputXmlType(outputSchema = "order.xsd", outputQName = "shiporder")
  public InputStream xmlOutput() {
    return cl.getResourceAsStream("order.xml");
  }

  @OutputXmlType(outputSchema = "order.xsd", outputQName = "shiporder")
  public String xmlInput(@InputXmlType(schema = "order.xsd", qName = "shiporder") InputStream xml) {
    return XML_VALUE;
  }

  @OutputJsonType(outputSchema = "person-schema.json")
  public InputStream jsonOutput() {
    return new ByteArrayInputStream(JSON_VALUE.getBytes());
  }

  @MediaType(value = "application/json")
  public String jsonInputStream(@InputJsonType(schema = "person-schema.json") InputStream json) {
    return IOUtils.toString(json);
  }

  public int jsonInputMap(@InputJsonType(schema = "person-schema.json") Map<String, Object> json) {
    return (int) json.get("age");
  }

  @OutputResolver(output = CsvInputStaticTypeResolver.class)
  public Object customTypeOutput() {
    return CSV_VALUE;
  }

  @MediaType("application/json")
  public String customTypeInput(@TypeResolver(JsonInputStaticTypeResolver.class) InputStream type) {
    return IOUtils.toString(type);
  }

  @OutputResolver(output = JavaOutputStaticTypeResolver.class)
  public Object customInputAndOutput(@TypeResolver(JsonInputStaticTypeResolver.class) InputStream type) {
    return null;
  }
}
