package com.project.core.tokenlib.util;

import org.junit.Before;
import org.junit.Test;
import tokenlib.util.AbstractTokenFactory;
import tokenlib.util.TokenProcessorContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AbstractTokenFactoryTest {
  AbstractTokenFactory abstractTokenFactoryMock;

  @Before
  public void setUp() {
    abstractTokenFactoryMock = mock(AbstractTokenFactory.class);
    when(abstractTokenFactoryMock.generate(0)).thenReturn(mock(TokenProcessorContext.class));
    when(abstractTokenFactoryMock.generate(1)).thenReturn(mock(TokenProcessorContext.class));
  }

  @Test
  public void testGenerate1() {
    assertTrue(abstractTokenFactoryMock.generate(0).equals(mock(TokenProcessorContext.class)));
  }
}
