package com.synlabs.ipsaa.enums;

public enum BloodGroup
{
  A_POS("A+"),
  A_NEG("A-"),
  B_POS("B+"),
  B_NEG("B-"),
  AB_POS("AB+"),
  AB_NEG("AB-"),
  O_POS("O+"),
  O_NEG("O-");

  private String disp;

  BloodGroup(String disp)
  {
    this.disp = disp;
  }
}
