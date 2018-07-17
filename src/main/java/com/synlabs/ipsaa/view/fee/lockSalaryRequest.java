package com.synlabs.ipsaa.view.fee;

import com.synlabs.ipsaa.view.common.Request;

public class lockSalaryRequest  implements Request {
    String id;
    boolean lock;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }
}
