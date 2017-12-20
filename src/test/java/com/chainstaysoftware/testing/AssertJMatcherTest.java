package com.chainstaysoftware.testing;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static com.chainstaysoftware.testing.AssertJMatcher.assertArg;
import static com.chainstaysoftware.testing.AssertJMatcher.closeTo;
import static com.chainstaysoftware.testing.AssertJMatcher.contains;
import static com.chainstaysoftware.testing.AssertJMatcher.instanceOf;
import static com.chainstaysoftware.testing.AssertJMatcher.notNullValue;
import static com.chainstaysoftware.testing.AssertJMatcher.nullValue;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
class AssertJMatcherTest {

   @Test
   void testAssertArg() {
      final Consumer<String> mockConsumer = mock(Consumer.class);

      mockConsumer.accept("foo");
      verify(mockConsumer).accept(assertArg(s -> Assertions.assertThat(s).isEqualTo("foo")));
   }

   @Test
   void testAssertArg_Not() {
      final Consumer<String> mockConsumer = mock(Consumer.class);

      mockConsumer.accept("foo");
      verify(mockConsumer).accept(not(assertArg(s -> Assertions.assertThat(s).isEqualTo("foobar"))));
   }

   @Test
   void testInstanceOf() {
      final Consumer mockConsumer = mock(Consumer.class);

      mockConsumer.accept("foo");

      verify(mockConsumer).accept(instanceOf(String.class));
   }

   @Test
   void testInstanceOf_Not() {
      final Consumer mockConsumer = mock(Consumer.class);

      mockConsumer.accept(10);
      verify(mockConsumer).accept(not(instanceOf(String.class)));
   }

   @Test
   void testNotNullValue() {
      final Consumer mockConsumer = mock(Consumer.class);

      mockConsumer.accept("foo");

      verify(mockConsumer).accept(notNullValue());
   }

   @Test
   void testNotNullValue_Not() {
      final Consumer<String> mockConsumer = mock(Consumer.class);

      mockConsumer.accept(null);

      verify(mockConsumer).accept(not(notNullValue()));
   }

   @Test
   void testNullValue() {
      final Consumer mockConsumer = mock(Consumer.class);

      mockConsumer.accept(null);

      verify(mockConsumer).accept(nullValue());
   }

   @Test
   void testNullValue_Not() {
      final Consumer<String> mockConsumer = mock(Consumer.class);

      mockConsumer.accept("str");

      verify(mockConsumer).accept(not(nullValue()));
   }

   @Test
   void testContains() {
      final Consumer<List<String>> mockConsumer = mock(Consumer.class);

      mockConsumer.accept(Arrays.asList("foo", "bar"));

      verify(mockConsumer).accept(contains("foo"));
   }

   @Test
   void testContains_Not() {
      final Consumer<List<String>> mockConsumer = mock(Consumer.class);

      mockConsumer.accept(Arrays.asList("foo", "bar"));

      verify(mockConsumer).accept(not(contains("foobar")));
   }

   @Test
   void testContainsAll() {
      final Consumer<List<String>> mockConsumer = mock(Consumer.class);

      mockConsumer.accept(Arrays.asList("foo", "bar"));

      verify(mockConsumer).accept(contains("foo", "bar"));
   }

   @Test
   void testContainsAll_Not() {
      final Consumer<List<String>> mockConsumer = mock(Consumer.class);

      mockConsumer.accept(Arrays.asList("foo", "bar"));

      verify(mockConsumer).accept(not(contains("foo", "bar", "foobar")));
   }

   @Test
   void testCloseTo() {
      final Consumer<Double> mockConsumer = mock(Consumer.class);

      mockConsumer.accept(1.0);

      verify(mockConsumer).accept(closeTo(0.9, .1));
   }

   @Test
   void testCloseTo_Not() {
      final Consumer<Double> mockConsumer = mock(Consumer.class);

      mockConsumer.accept(1.0);

      verify(mockConsumer).accept(not(closeTo(0.9, .01)));
   }
}