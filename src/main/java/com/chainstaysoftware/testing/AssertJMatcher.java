package com.chainstaysoftware.testing;

import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;


/**
 * {@link ArgumentMatcher} that utilizes AssertJ to perform assertion logic.
 */
public class AssertJMatcher<T> implements ArgumentMatcher<T> {

   private final Consumer<T> consumer;

   private String errorMessage;

   private AssertJMatcher(final Consumer<T> consumer) {
      this.consumer = consumer;
   }

   @SuppressWarnings("unchecked")
   @Override
   public boolean matches(final T argument) {
      try {
         consumer.accept(argument);
         return true;
      } catch (AssertionError e) {
         errorMessage = e.getMessage();
         return false;
      }
   }

   @Override
   public String toString() {
      return Optional.ofNullable(errorMessage)
         .map(msg -> "AssertionMatcher exception - " + msg)
         .orElse("AssertionMatcher");
   }

   /**
    * Creates a matcher that executes the passed in Consumer. The Consumer
    * should implement whatever match logic is desired.
    */
   public static <T> T assertArg(final Consumer<T> consumer) {
      final AssertJMatcher<T> matcher = new AssertJMatcher<>(consumer);
      Mockito.argThat(matcher);
      return null;
   }

   /**
    * Creates a matcher that matches when the examined object is an instance of the
    * specified class.
    */
   public static <T> T instanceOf(final Class<T> clazz) {
      return assertArg(object -> Assertions.assertThat(object).isInstanceOf(clazz));
   }

   /**
    * Creates a matcher that matches if examined object is not null.
    */
   public static <T> T notNullValue() {
      return assertArg(object -> Assertions.assertThat(object).isNotNull());
   }

   /**
    * Creates a matcher that matches if examined object is null.
    */
   public static <T> T nullValue() {
      return assertArg(object -> Assertions.assertThat(object).isNull());
   }

   /**
    * Creates a matcher for List that matches when a single pass over the examined
    * Iterable yields a single item that satisfies the specified matcher.
    */
   public static <T> List<T> contains(final T obj) {
      return assertArg(object -> Assertions.assertThat(object).contains(obj));
   }

   /**
    * Creates a matcher for List that matches when a single pass over the examined List
    * yields a series of items, each satisfying the corresponding matcher in the specified matchers.
    */
   @SafeVarargs
   public static <T> List<T> contains(final T ...objs) {
      return assertArg(object -> Assertions.assertThat(object).contains(objs));
   }

   /**
    * Creates a matcher of Doubles that matches when an examined double is equal to the specified
    * operand, within a range of +/- delta.
    */
   public static Double closeTo(final double val, final double delta) {
      assertArg((Consumer<Double>) aDouble -> {
         if (aDouble == null)
            Assertions.fail("Object is null");
         else
            Assertions.assertThat(aDouble).isCloseTo(val, Offset.offset(delta));

      });

      return 0.0;
   }
}