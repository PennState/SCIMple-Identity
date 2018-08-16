/*
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership.  The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License.  You may obtain a copy of the License at
 
* http://www.apache.org/licenses/LICENSE-2.0

* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

package edu.psu.swe.scim.client.filter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import edu.psu.swe.scim.spec.protocol.attribute.AttributeReference;
import edu.psu.swe.scim.spec.protocol.filter.AttributeComparisonExpression;
import edu.psu.swe.scim.spec.protocol.filter.CompareOperator;
import edu.psu.swe.scim.spec.protocol.filter.FilterExpression;
import edu.psu.swe.scim.spec.protocol.filter.FilterParseException;
import edu.psu.swe.scim.spec.protocol.filter.GroupExpression;
import edu.psu.swe.scim.spec.protocol.filter.LogicalExpression;
import edu.psu.swe.scim.spec.protocol.filter.LogicalOperator;
import edu.psu.swe.scim.spec.protocol.filter.ValuePathExpression;
import edu.psu.swe.scim.spec.protocol.search.Filter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FilterClient {

  private FilterClient() {
    
  }
  
  public abstract static class SimpleLogicalBuilder extends Builder {
    SimpleLogicalBuilder() {
    }

    @Override
    public Builder and() {
      if (filterExpression == null) {
        throw new IllegalStateException();
      }

      LogicalExpression logicalExpression = new LogicalExpression();
      logicalExpression.setLeft(filterExpression);
      logicalExpression.setOperator(LogicalOperator.AND);
      filterExpression = logicalExpression;

      return this;
    }

    @Override
    public Builder or() {
      if (filterExpression == null) {
        throw new IllegalStateException();
      }

      LogicalExpression logicalExpression = new LogicalExpression();
      logicalExpression.setLeft(filterExpression);
      logicalExpression.setOperator(LogicalOperator.OR);
      filterExpression = logicalExpression;

      return this;
    }
  }

  public abstract static class ComplexLogicalBuilder extends SimpleLogicalBuilder {

    @Override
    public Builder or(FilterExpression fe1) {
      if (filterExpression instanceof AttributeComparisonExpression) {
        LogicalExpression logicalExpression = new LogicalExpression();
        logicalExpression.setLeft(filterExpression);
        logicalExpression.setRight(fe1);
        logicalExpression.setOperator(LogicalOperator.OR);
        filterExpression = logicalExpression;
        return this;
      }
      
      LogicalExpression logicalExpression = new LogicalExpression();
      logicalExpression.setLeft(fe1);
      logicalExpression.setOperator(LogicalOperator.OR);

      return handleLogicalExpression(logicalExpression, LogicalOperator.OR);
    }
    
    @Override
    public Builder or(FilterExpression fe1, FilterExpression fe2) {
      LogicalExpression logicalExpression = new LogicalExpression();
      logicalExpression.setLeft(fe1);
      logicalExpression.setRight(fe2);
      logicalExpression.setOperator(LogicalOperator.OR);

      return handleLogicalExpression(logicalExpression, LogicalOperator.OR);
    }

    @Override
    public Builder and(FilterExpression fe1, FilterExpression fe2) {
      LogicalExpression logicalExpression = new LogicalExpression();
      logicalExpression.setLeft(fe1);
      logicalExpression.setRight(fe2);
      logicalExpression.setOperator(LogicalOperator.AND);

      return handleLogicalExpression(logicalExpression, LogicalOperator.AND);
    }
    
    @Override
    public Builder and(FilterExpression fe1) {
      if (filterExpression instanceof AttributeComparisonExpression) {
        LogicalExpression logicalExpression = new LogicalExpression();
        logicalExpression.setLeft(filterExpression);
        logicalExpression.setRight(fe1);
        logicalExpression.setOperator(LogicalOperator.AND);
        filterExpression = logicalExpression;
        return this;
      }
      
      LogicalExpression logicalExpression = new LogicalExpression();
      logicalExpression.setLeft(fe1);
      logicalExpression.setOperator(LogicalOperator.AND);

      return handleLogicalExpression(logicalExpression, LogicalOperator.AND);
    }
  }

  // This class is returned from comparison operations to ensure
  // that the next step is correct
  public static class ComparisonBuilder extends ComplexLogicalBuilder {

    @Override
    public Builder equalTo(String key, String value) {

      AttributeReference ar = new AttributeReference(key);
      FilterExpression filterExpression = new AttributeComparisonExpression(ar, CompareOperator.EQ, value);

      handleComparisonExpression(filterExpression);

      return this;
    }

    @Override
    public Builder equalTo(String key, Boolean value) {

      AttributeReference ar = new AttributeReference(key);
      FilterExpression filterExpression = new AttributeComparisonExpression(ar, CompareOperator.EQ, value);

      handleComparisonExpression(filterExpression);

      return this;
    }

    @Override
    public Builder equalTo(String key, Date value) {
      AttributeReference ar = new AttributeReference(key);
      FilterExpression filterExpression = new AttributeComparisonExpression(ar, CompareOperator.EQ, value);

      handleComparisonExpression(filterExpression);

      return this;
    }

    @Override
    public Builder equalTo(String key, LocalDate value) {

      AttributeReference ar = new AttributeReference(key);
      FilterExpression filterExpression = new AttributeComparisonExpression(ar, CompareOperator.EQ, value);

      handleComparisonExpression(filterExpression);

      return this;
    }

    @Override
    public Builder equalTo(String key, LocalDateTime value) {
      AttributeReference ar = new AttributeReference(key);
      FilterExpression filterExpression = new AttributeComparisonExpression(ar, CompareOperator.EQ, value);

      handleComparisonExpression(filterExpression);

      return this;
    }

    @Override
    public <T extends Number> Builder equalTo(String key, T value) {
      AttributeReference ar = new AttributeReference(key);
      FilterExpression filterExpression = new AttributeComparisonExpression(ar, CompareOperator.EQ, value);

      handleComparisonExpression(filterExpression);

      return this;
    }

    @Override
    public Builder equalNull(String key) {
      AttributeReference ar = new AttributeReference(key);
      FilterExpression filterExpression = new AttributeComparisonExpression(ar, CompareOperator.EQ, null);

      handleComparisonExpression(filterExpression);

      return this;
    }

    @Override
    public Builder notEqual(String key, String value) {
      AttributeReference ar = new AttributeReference(key);
      FilterExpression filterExpression = new AttributeComparisonExpression(ar, CompareOperator.NE, value);

      handleComparisonExpression(filterExpression);

      return this;
    }

    @Override
    public Builder notEqual(String key, Boolean value) {
      AttributeReference ar = new AttributeReference(key);
      FilterExpression filterExpression = new AttributeComparisonExpression(ar, CompareOperator.NE, value);

      handleComparisonExpression(filterExpression);

      return this;
    }

    @Override
    public Builder notEqual(String key, Date value) {
      AttributeReference ar = new AttributeReference(key);
      FilterExpression filterExpression = new AttributeComparisonExpression(ar, CompareOperator.NE, value);

      handleComparisonExpression(filterExpression);

      return this;
    }

    @Override
    public Builder notEqual(String key, LocalDate value) {
      AttributeReference ar = new AttributeReference(key);
      FilterExpression filterExpression = new AttributeComparisonExpression(ar, CompareOperator.NE, value);

      handleComparisonExpression(filterExpression);

      return this;
    }

    @Override
    public Builder notEqual(String key, LocalDateTime value) {
      AttributeReference ar = new AttributeReference(key);
      FilterExpression filterExpression = new AttributeComparisonExpression(ar, CompareOperator.NE, value);

      handleComparisonExpression(filterExpression);

      return this;
    }

    @Override
    public <T extends Number> Builder notEqual(String key, T value) {
      AttributeReference ar = new AttributeReference(key);
      FilterExpression filterExpression = new AttributeComparisonExpression(ar, CompareOperator.NE, value);

      handleComparisonExpression(filterExpression);

      return this;
    }

    @Override
    public Builder notEqualNull(String key) {
      AttributeReference ar = new AttributeReference(key);
      FilterExpression filterExpression = new AttributeComparisonExpression(ar, CompareOperator.NE, null);

      handleComparisonExpression(filterExpression);

      return this;
    }

    @Override
    public Builder endsWith(String key, String value) {
      AttributeReference ar = new AttributeReference(key);
      FilterExpression filterExpression = new AttributeComparisonExpression(ar, CompareOperator.EW, value);

      handleComparisonExpression(filterExpression);

      return this;
    }

    @Override
    public Builder startsWith(String key, String value) {
      AttributeReference ar = new AttributeReference(key);
      FilterExpression filterExpression = new AttributeComparisonExpression(ar, CompareOperator.SW, value);

      handleComparisonExpression(filterExpression);

      return this;
    }

    @Override
    public Builder contains(String key, String value) {
      AttributeReference ar = new AttributeReference(key);
      FilterExpression filterExpression = new AttributeComparisonExpression(ar, CompareOperator.CO, value);

      handleComparisonExpression(filterExpression);

      return this;
    }

    @Override
    public <T extends Number> Builder greaterThan(String key, T value) {
      AttributeReference ar = new AttributeReference(key);
      FilterExpression filterExpression = new AttributeComparisonExpression(ar, CompareOperator.GT, value);

      handleComparisonExpression(filterExpression);

      return this;
    }

    @Override
    public Builder greaterThan(String key, Date value) {
      AttributeReference ar = new AttributeReference(key);
      FilterExpression filterExpression = new AttributeComparisonExpression(ar, CompareOperator.GT, value);

      handleComparisonExpression(filterExpression);

      return this;
    }

    @Override
    public Builder greaterThan(String key, LocalDate value) {
      AttributeReference ar = new AttributeReference(key);
      FilterExpression filterExpression = new AttributeComparisonExpression(ar, CompareOperator.GT, value);

      handleComparisonExpression(filterExpression);

      return this;
    }

    @Override
    public Builder greaterThan(String key, LocalDateTime value) {
      AttributeReference ar = new AttributeReference(key);
      FilterExpression filterExpression = new AttributeComparisonExpression(ar, CompareOperator.GT, value);

      handleComparisonExpression(filterExpression);

      return this;
    }

    @Override
    public <T extends Number> Builder greaterThanOrEquals(String key, T value) {
      AttributeReference ar = new AttributeReference(key);
      FilterExpression filterExpression = new AttributeComparisonExpression(ar, CompareOperator.GT, value);

      handleComparisonExpression(filterExpression);

      return this;
    }

    @Override
    public Builder greaterThanOrEquals(String key, Date value) {
      AttributeReference ar = new AttributeReference(key);
      FilterExpression filterExpression = new AttributeComparisonExpression(ar, CompareOperator.GE, value);

      handleComparisonExpression(filterExpression);

      return this;
    }

    @Override
    public Builder greaterThanOrEquals(String key, LocalDate value) {
      AttributeReference ar = new AttributeReference(key);
      FilterExpression filterExpression = new AttributeComparisonExpression(ar, CompareOperator.GE, value);

      handleComparisonExpression(filterExpression);

      return this;
    }

    @Override
    public Builder greaterThanOrEquals(String key, LocalDateTime value) {
      AttributeReference ar = new AttributeReference(key);
      FilterExpression filterExpression = new AttributeComparisonExpression(ar, CompareOperator.GE, value);

      handleComparisonExpression(filterExpression);

      return this;
    }

    @Override
    public <T extends Number> Builder lessThan(String key, T value) {
      AttributeReference ar = new AttributeReference(key);
      FilterExpression filterExpression = new AttributeComparisonExpression(ar, CompareOperator.LT, value);

      handleComparisonExpression(filterExpression);

      return this;
    }

    @Override
    public Builder lessThan(String key, Date value) {
      AttributeReference ar = new AttributeReference(key);
      FilterExpression filterExpression = new AttributeComparisonExpression(ar, CompareOperator.LT, value);

      handleComparisonExpression(filterExpression);

      return this;
    }

    @Override
    public Builder lessThan(String key, LocalDate value) {
      AttributeReference ar = new AttributeReference(key);
      FilterExpression filterExpression = new AttributeComparisonExpression(ar, CompareOperator.LT, value);

      handleComparisonExpression(filterExpression);

      return this;
    }

    @Override
    public Builder lessThan(String key, LocalDateTime value) {
      AttributeReference ar = new AttributeReference(key);
      FilterExpression filterExpression = new AttributeComparisonExpression(ar, CompareOperator.LT, value);

      handleComparisonExpression(filterExpression);

      return this;
    }

    @Override
    public <T extends Number> Builder lessThanOrEquals(String key, T value) {
      AttributeReference ar = new AttributeReference(key);
      FilterExpression filterExpression = new AttributeComparisonExpression(ar, CompareOperator.LE, value);

      handleComparisonExpression(filterExpression);

      return this;
    }

    @Override
    public Builder lessThanOrEquals(String key, Date value) {
      AttributeReference ar = new AttributeReference(key);
      FilterExpression filterExpression = new AttributeComparisonExpression(ar, CompareOperator.LE, value);

      handleComparisonExpression(filterExpression);

      return this;
    }

    @Override
    public Builder lessThanOrEquals(String key, LocalDate value) {
      AttributeReference ar = new AttributeReference(key);
      FilterExpression filterExpression = new AttributeComparisonExpression(ar, CompareOperator.LE, value);

      handleComparisonExpression(filterExpression);

      return this;
    }

    @Override
    public Builder lessThanOrEquals(String key, LocalDateTime value) {
      AttributeReference ar = new AttributeReference(key);
      FilterExpression filterExpression = new AttributeComparisonExpression(ar, CompareOperator.LE, value);

      handleComparisonExpression(filterExpression);

      return this;
    }

    @Override
    public Builder not(FilterExpression expression) {
      GroupExpression groupExpression = new GroupExpression();

      groupExpression.setNot(true);
      groupExpression.setFilterExpression(expression);

      handleComparisonExpression(groupExpression);

      return this;
    }

    @Override
    public Builder attributeHas(String attribute, FilterExpression expression) throws FilterParseException {
      handleComparisonExpression(ValuePathExpression.fromFilterExpression(attribute, expression));

      return this;
    }
  }

  public abstract static class Builder {

    FilterExpression filterExpression;

    Builder() {
    }

    public abstract Builder and();
    
    public abstract Builder and(FilterExpression fe1);

    public abstract Builder and(FilterExpression fe1, FilterExpression fe2);

    public abstract Builder or();
    
    public abstract Builder or(FilterExpression fe1);

    public abstract Builder or(FilterExpression fe1, FilterExpression fe2);

    public abstract Builder equalTo(String key, String value);

    public abstract Builder equalTo(String key, Boolean value);

    public abstract Builder equalTo(String key, Date value);

    public abstract Builder equalTo(String key, LocalDate value);

    public abstract Builder equalTo(String key, LocalDateTime value);

    public abstract <T extends Number> Builder equalTo(String key, T value);

    public abstract Builder equalNull(String key);

    public abstract Builder notEqual(String key, String value);

    public abstract Builder notEqual(String key, Boolean value);

    public abstract Builder notEqual(String key, Date value);

    public abstract Builder notEqual(String key, LocalDate value);

    public abstract Builder notEqual(String key, LocalDateTime value);

    public abstract <T extends Number> Builder notEqual(String key, T value);

    public abstract Builder notEqualNull(String key);

    public abstract <T extends Number> Builder greaterThan(String key, T value);

    public abstract Builder greaterThan(String key, Date value);

    public abstract Builder greaterThan(String key, LocalDate value);

    public abstract Builder greaterThan(String key, LocalDateTime value);

    public abstract <T extends Number> Builder greaterThanOrEquals(String key, T value);

    public abstract Builder greaterThanOrEquals(String key, Date value);

    public abstract Builder greaterThanOrEquals(String key, LocalDate value);

    public abstract Builder greaterThanOrEquals(String key, LocalDateTime value);

    public abstract <T extends Number> Builder lessThan(String key, T value);

    public abstract Builder lessThan(String key, Date value);

    public abstract Builder lessThan(String key, LocalDate value);

    public abstract Builder lessThan(String key, LocalDateTime value);

    public abstract <T extends Number> Builder lessThanOrEquals(String key, T value);

    public abstract Builder lessThanOrEquals(String key, Date value);

    public abstract Builder lessThanOrEquals(String key, LocalDate value);

    public abstract Builder lessThanOrEquals(String key, LocalDateTime value);

    public abstract Builder endsWith(String key, String value);

    public abstract Builder startsWith(String key, String value);

    public abstract Builder contains(String key, String value);

    public abstract Builder not(FilterExpression filter);

    public abstract Builder attributeHas(String attribute, FilterExpression filter) throws FilterParseException;

    protected Builder handleLogicalExpression(LogicalExpression expression, LogicalOperator operator) {
      log.info("In handleLogicalExpression");
      if (filterExpression == null) {
        filterExpression = expression;
      } else if (filterExpression instanceof AttributeComparisonExpression) {
        log.info("Adding a logical expression as the new root");

        log.info("Setting as left: " + filterExpression.toFilter());
        expression.setLeft(filterExpression);
        log.info("Setting as right: " + expression.toFilter());

        filterExpression = expression;
      } else if (filterExpression instanceof LogicalExpression) {
        log.info("filter exression is a logical expression");
        LogicalExpression le = (LogicalExpression) filterExpression;

        if (le.getLeft() == null) {
          log.info("Setting left to: " + expression.toFilter());
          le.setLeft(expression);
        } else if (le.getRight() == null) {
          log.info("Setting right to: " + expression.toFilter());
          le.setRight(expression);
        } else {
          log.info("The current base is complete, raising up one level");
          LogicalExpression newRoot = new LogicalExpression();
          log.info("Setting left to: " + expression);
          newRoot.setLeft(expression);
          filterExpression = newRoot;
        }
      } else if (filterExpression instanceof GroupExpression) {
        log.info("Found group expression");
        LogicalExpression newRoot = new LogicalExpression();
        newRoot.setLeft(filterExpression);
        newRoot.setRight(expression);
        newRoot.setOperator(operator);
        filterExpression = newRoot;
      }

      log.info("New filter expression: " + filterExpression.toFilter());

      return this;
    }

    protected void handleComparisonExpression(FilterExpression expression) {

      if (expression == null) {
        log.error("*** in handle comparison ---> expression == null");
      }
      
      if (filterExpression == null) {
        filterExpression = expression;
      } else {
        if (!(filterExpression instanceof LogicalExpression)) {
          throw new IllegalStateException();
        }

        LogicalExpression le = (LogicalExpression) filterExpression;
        le.setRight(expression);
      }
    }

    @Override
    public String toString() {
      
      String filterString = filterExpression.toFilter();
      try {
        return URLEncoder.encode(filterString, "UTF-8").replace("+", "%20");
      } catch (UnsupportedEncodingException e) {
        log.error("Unsupported encoding:", e);
        return null;
      }
    }
    
    public Filter build() {
      return new Filter(filterExpression);
    }

    public FilterExpression filter() {
      return filterExpression;
    }
  }

  public static Builder builder() {
    return new FilterClient.ComparisonBuilder();
  }
}
