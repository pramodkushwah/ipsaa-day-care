package com.synlabs.ipsaa.enums;

/**
 * Created by sushil on 10/07/2017.
 */
public enum LeadSource
{
  BUILDING("Building"),
  CORPORATE("Corporate"),
  ADVERTISEMENT("Advertisement"),
  REFERENCE("Reference"),
  WEBSITE("Website"),
  NEWSPAPER("Newspaper"),
  SIGNBOARDS("Signboards"),
  OTHERS("Others");

  private String disp;

  LeadSource(String disp)
  {
    this.disp = disp;
  }

  }
