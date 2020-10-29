package com.nathanaelg.messagingapp.shared;

import java.net.InetAddress;

public class Registration {
    private final String name;
    private final InetAddress address;
    private final int port;

    public Registration(String name, InetAddress address, int port) {
        this.name = name;
        this.address = address;
        this.port = port;
    }

    public Registration(String name) {
        this.name = name;
        address = null;
        port = -1;
    }

    public String getName() {
        return name;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Registration that = (Registration) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
