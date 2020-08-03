package com.freightmate.harbour.model;

// This is currently not used as a type because existing data contains invalid state value
// Having an enum assigned to the Address model might cause issue
// todo: need to create a separate ticket to fix data
public enum AddressState {
    NSW,
    QLD,
    SA,
    TAS,
    VIC,
    WA,
    ACT,
    NT,
    OTHER
}
